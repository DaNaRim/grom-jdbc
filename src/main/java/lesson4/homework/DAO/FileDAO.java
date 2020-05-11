package lesson4.homework.DAO;

import lesson4.homework.Exceptions.BadRequestException;
import lesson4.homework.Exceptions.InternalServerException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

import java.sql.*;
import java.util.HashSet;
import java.util.UUID;

public class FileDAO extends DaoTools {

    public static File save(Storage storage, File file) throws InternalServerException {
        try (Connection conn = getConnection()) {
            return saveFile(storage, file, conn);
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public static void delete(Storage storage, File file) throws InternalServerException {
        try (Connection conn = getConnection()) {
            deleteFile(storage, file, conn);
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public static File update(File file) throws InternalServerException {
        try (Connection conn = getConnection()) {
            return update(file, conn);
        } catch (SQLException | InternalServerException | BadRequestException e) {
            throw new InternalServerException("An error occurred while trying to update the file " + file.getId() +
                    " : " + e.getMessage());
        }
    }

    public static File findById(long id) throws BadRequestException, InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM FILES WHERE ID = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new File(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getLong(4),
                        StorageDAO.findById(rs.getLong(5)));
            }
            throw new BadRequestException("File with id " + id + " is missing");
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to find file with id " + id + " : " +
                    e.getMessage());
        }
    }

    public static void transferAll(Storage storageFrom, Storage storageTo) throws InternalServerException {
        try (Connection conn = getConnection()) {
            transferAllFiles(storageFrom, storageTo, conn);
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException("An error occurred while trying to transfer files from storage " +
                    storageFrom.getId() + " to storage " + storageTo.getId() + " : " + e.getMessage());
        }
    }

    public static void transferFile(Storage storageFrom, Storage storageTo, long id) throws InternalServerException {
        try (Connection conn = getConnection()) {
            transferFile(storageFrom, storageTo, id, conn);
        } catch (SQLException | BadRequestException | InternalServerException e) {
            throw new InternalServerException("An error occurred while trying to transfer file " + id + " from storage " +
                    storageFrom.getId() + " to storage " + storageTo.getId() + " : " + e.getMessage());
        }
    }

    public static void checkFileName(Storage storage, File file) throws BadRequestException, InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM FILES WHERE NAME = ? AND STORAGE_ID = ?")) {
            ps.setString(1, file.getName());
            ps.setLong(2, storage.getId());
            if (ps.executeUpdate() == 1) throw new BadRequestException("File already exists");
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to check the file: " + e.getMessage());
        }
    }

    public static HashSet<File> getFilesByStorage(Storage storage) throws InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM FILES WHERE STORAGE_ID = ?")) {
            ps.setLong(1, storage.getId());
            ResultSet rs = ps.executeQuery();

            HashSet<File> files = new HashSet<>();
            while (rs.next()) {
                files.add(new File(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getLong(4),
                        storage));
            }
            return files;
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to get all files from storage " +
                    storage.getId() + " : " + e.getMessage());
        }
    }

    private static File saveFile(Storage storage, File file, Connection conn)
            throws SQLException, InternalServerException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO FILES VALUES(?, ?, ?, ?, ?)")) {
            conn.setAutoCommit(false);

            long fileId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

            ps.setLong(1, fileId);
            ps.setString(2, file.getName());
            ps.setString(3, file.getFormat());
            ps.setLong(4, file.getSize());
            ps.setLong(5, storage.getId());
            ps.executeUpdate();

            file.setId(fileId);
            file.setStorage(storage);

            StorageDAO.updateFreeSpace(storage, storage.getFreeSpace() - file.getSize());

            conn.commit();
            return file;
        } catch (SQLException | InternalServerException e) {
            conn.rollback();
            throw e;
        }
    }

    private static void deleteFile(Storage storage, File file, Connection conn)
            throws SQLException, InternalServerException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM FILES WHERE ID = ?")) {
            conn.setAutoCommit(false);

            ps.setLong(1, file.getId());
            ps.executeUpdate();

            StorageDAO.updateFreeSpace(storage, storage.getFreeSpace() + file.getSize());

            conn.commit();
        } catch (SQLException | InternalServerException e) {
            conn.rollback();
            throw e;
        }
    }

    private static File update(File file, Connection conn)
            throws InternalServerException, SQLException, BadRequestException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE FILES SET NAME = ?, FORMAT = ?, FILE_SIZE = ?, STORAGE_ID = ? WHERE ID = ?")) {
            conn.setAutoCommit(false);

            ps.setString(1, file.getName());
            ps.setString(2, file.getFormat());
            ps.setLong(3, file.getSize());
            ps.setLong(4, file.getStorage().getId());
            ps.setLong(5, file.getId());
            ps.executeUpdate();

            File currentFileVersion = findById(file.getId());
            Storage currentStorage = currentFileVersion.getStorage();

            if (currentStorage.getId() == file.getStorage().getId()) {
                StorageDAO.updateFreeSpace(currentStorage, currentStorage.getFreeSpace() + file.getSize() - currentFileVersion.getSize());
            } else {
                StorageDAO.updateFreeSpace(currentStorage, currentStorage.getFreeSpace() + currentFileVersion.getSize());
                StorageDAO.updateFreeSpace(file.getStorage(), file.getStorage().getFreeSpace() - file.getSize());
            }

            conn.commit();
            return file;
        } catch (SQLException | BadRequestException e) {
            conn.rollback();
            throw e;
        }
    }

    private static void transferAllFiles(Storage storageFrom, Storage storageTo, Connection conn)
            throws SQLException, InternalServerException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE FILES SET STORAGE_ID = ? WHERE STORAGE_ID = ?")) {
            conn.setAutoCommit(false);

            ps.setLong(1, storageTo.getId());
            ps.setLong(2, storageFrom.getId());
            ps.executeUpdate();

            long filesSize = storageFrom.getStorageSize() - storageFrom.getFreeSpace();
            StorageDAO.updateFreeSpace(storageFrom, storageFrom.getStorageSize());
            StorageDAO.updateFreeSpace(storageTo, storageTo.getFreeSpace() - filesSize);

            conn.commit();
        } catch (SQLException | InternalServerException e) {
            conn.rollback();
            throw e;
        }
    }

    private static void transferFile(Storage storageFrom, Storage storageTo, long id, Connection conn)
            throws SQLException, BadRequestException, InternalServerException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE FILES SET STORAGE_ID = ? WHERE ID = ?")) {
            conn.setAutoCommit(false);

            ps.setLong(1, storageTo.getId());
            ps.setLong(2, id);
            ps.executeUpdate();

            File file = findById(id);
            StorageDAO.updateFreeSpace(storageFrom, storageFrom.getFreeSpace() + file.getSize());
            StorageDAO.updateFreeSpace(storageTo, storageTo.getFreeSpace() - file.getSize());

            conn.commit();
        } catch (SQLException | BadRequestException | InternalServerException e) {
            conn.rollback();
            throw e;
        }
    }
}