package jdbc.lesson4.homework.DAO;

import jdbc.lesson4.homework.exceptions.BadRequestException;
import jdbc.lesson4.homework.exceptions.InternalServerException;
import jdbc.lesson4.homework.model.Storage;

import java.sql.*;
import java.util.UUID;

public class StorageDAO extends DaoTools {

    public static Storage save(Storage storage) throws InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO STORAGE VALUES (?, ?, ?, ?, ?)")) {

            long storageId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
            StringBuilder formatsSupported = new StringBuilder();
            for (String str : storage.getFormatsSupported()) {
                formatsSupported.append(str);
                formatsSupported.append(", ");
            }
            formatsSupported.delete(formatsSupported.lastIndexOf(", "), formatsSupported.length());

            ps.setLong(1, storageId);
            ps.setString(2, formatsSupported.toString());
            ps.setString(3, storage.getStorageCountry());
            ps.setLong(4, storage.getStorageSize());
            ps.setLong(5, storage.getStorageSize());
            ps.executeUpdate();

            storage.setId(storageId);
            return storage;
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to create the storage: " +
                    e.getMessage());
        }
    }

    public static void delete(long id) throws InternalServerException {
        try (Connection conn = getConnection()) {
            delete(id, conn);
        } catch (SQLException | BadRequestException e) {
            throw new InternalServerException("An error occurred while trying to delete the storage " + id + " : " +
                    e.getMessage());
        }
    }

    public static Storage update(Storage storage) throws InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("UPDATE STORAGE SET FORMATS_SUPPORTED = ?, COUNTRY = ?, STORAGE_SIZE = ?, FREE_SPACE = ? WHERE ID = ?")) {

            StringBuilder formatsSupported = new StringBuilder();
            for (String str : storage.getFormatsSupported()) {
                formatsSupported.append(str);
                formatsSupported.append(", ");
            }
            formatsSupported.delete(formatsSupported.lastIndexOf(", "), formatsSupported.length());

            long freeSpace = storage.getStorageSize();
            for (File file : FileDAO.getFilesByStorage(storage)) {
                freeSpace -= file.getSize();
            }

            ps.setString(1, formatsSupported.toString());
            ps.setString(2, storage.getStorageCountry());
            ps.setLong(3, storage.getStorageSize());
            ps.setLong(4, freeSpace);
            ps.setLong(5, storage.getId());
            ps.executeUpdate();

            return storage;
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to update the storage " +
                    storage.getId() + " : " + e.getMessage());
        }
    }

    public static Storage findById(long id) throws BadRequestException, InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM STORAGE WHERE ID = ?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new BadRequestException("Storage with id " + id + " is missing");
            }
            Storage storage = new Storage(
                    rs.getLong(1),
                    null,
                    rs.getString(2).split(", "),
                    rs.getString(3),
                    rs.getLong(4),
                    rs.getLong(5));
            storage.setFiles(FileDAO.getFilesByStorage(storage));

            return storage;
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to find storage with id " + id + " : " +
                    e.getMessage());
        }
    }

    public static void updateFreeSpace(Storage storage, long freeSpace) throws InternalServerException {
        try (PreparedStatement ps = getConnection().prepareStatement("UPDATE STORAGE SET FREE_SPACE = ? WHERE ID = ?")) {
            ps.setLong(1, freeSpace);
            ps.setLong(2, storage.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to update free space in storage with id " +
                    storage.getId() + " : " +
                    e.getMessage());
        }
    }

    private static void delete(long id, Connection conn)
            throws SQLException, InternalServerException, BadRequestException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE STORAGE WHERE ID = ?")) {
            conn.setAutoCommit(false);

            Storage storage = findById(id);
            for (File file : storage.getFiles()) {
                FileDAO.delete(storage, file);
            }

            ps.setLong(1, id);
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException | InternalServerException | BadRequestException e) {
            conn.rollback();
            throw e;
        }
    }
}