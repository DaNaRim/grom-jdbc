package jdbc.lesson4.homework.DAO;

import jdbc.lesson4.homework.exceptions.BadRequestException;
import jdbc.lesson4.homework.exceptions.InternalServerException;
import jdbc.lesson4.homework.model.File;
import jdbc.lesson4.homework.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FileDAO {

    private static final StorageDAO storageDAO = new StorageDAO();

    private static final String SAVE_QUERY = "INSERT INTO FILES VALUES(?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILES WHERE ID = ?";
    private static final String UPDATE_QUERY = "UPDATE FILES SET NAME = ?, FORMAT = ?, FILE_SIZE = ?, STORAGE_ID = ? WHERE ID = ?";
    private static final String DELETE_QUERY = "DELETE FROM FILES WHERE ID = ?";

    private static final String PUT_QUERY = "UPDATE FILES SET STORAGE_ID = ? WHERE ID = ?";
    private static final String DELETE_FROM_STORAGE_QUERY = "UPDATE FILES SET STORAGE_ID = 0 WHERE ID = ?";
    private static final String TRANSFER_ALL_QUERY = "UPDATE FILES SET STORAGE_ID = ? WHERE STORAGE_ID = ?";
    private static final String DELETE_FILES_BY_STORAGE_QUERY = "DELETE FROM FILES WHERE STORAGE_ID = ?";

    private static final String CHECK_FILE_NAME_QUERY = "SELECT * FROM FILES WHERE NAME = ?";
    private static final String CHECK_STORAGE_FOR_EMPTY_QUERY = "SELECT COUNT(*) FROM FILES WHERE STORAGE_ID = ?";
    private static final String GET_FILES_SIZE_BY_STORAGE_QUERY = "SELECT SUM(FILE_SIZE) FROM FILES WHERE STORAGE_ID = ?";
    private static final String CHECK_FORMAT_QUERY = "SELECT FORMAT FROM FILES GROUP BY FORMAT";

    public File save(File file) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(SAVE_QUERY)) {

            file.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

            ps.setLong(1, file.getId());
            ps.setString(2, file.getName());
            ps.setString(3, file.getFormat());
            ps.setLong(4, file.getSize());
            ps.setLong(5, 0);
            ps.executeUpdate();

            return file;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to save the file " + file.getName()
                    + " : " + e.getMessage());
        }
    }

    public File findById(long id) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(FIND_BY_ID_QUERY)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new BadRequestException("missing file with id: " + id);
            }

            long storageId = rs.getLong(5);
            return new File(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getLong(4),
                    storageId == 0 ? null : storageDAO.findById(storageId));

        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to find file with id " + id + " : "
                    + e.getMessage());
        }
    }

    public File update(File file) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(UPDATE_QUERY)) {

            Storage fileStorage = file.getStorage();

            ps.setString(1, file.getName());
            ps.setString(2, file.getFormat());
            ps.setLong(3, file.getSize());
            ps.setLong(4, fileStorage == null ? 0 : fileStorage.getId());
            ps.setLong(5, file.getId());
            ps.executeUpdate();

            return file;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to update the file " + file.getId()
                    + " : " + e.getMessage());
        }
    }

    public void delete(long id) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(DELETE_QUERY)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to delete the file " + id
                    + " : " + e.getMessage());
        }
    }

    public File put(Storage storage, File file) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(PUT_QUERY)) {

            file.setStorage(storage);

            ps.setLong(1, file.getStorage().getId());
            ps.setLong(2, file.getId());
            ps.executeUpdate();

            return file;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to put the file " + file.getId()
                    + "in storage " + storage.getId() + " : " + e.getMessage());
        }
    }

    public void putAll(Storage storage, List<File> files) throws InternalServerException {

        try (Connection conn = DaoTools.getConnection()) {

            putAll(storage, files, conn);

        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException("something went wrong while trying to put all files in storage "
                    + storage.getId() + " : " + e.getMessage());
        }
    }

    public File deleteFromStorage(long StorageId, File file) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(DELETE_FROM_STORAGE_QUERY)) {

            file.setStorage(null);

            ps.setLong(1, file.getId());
            ps.executeUpdate();

            return file;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to delete file " + file.getId()
                    + "from storage " + StorageId + " : " + e.getMessage());
        }
    }

    public void transferAll(long storageFromId, long storageToId) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(TRANSFER_ALL_QUERY)) {

            ps.setLong(1, storageToId);
            ps.setLong(2, storageFromId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to transfer files from storage "
                    + storageFromId + " to storage " + storageToId + " : " + e.getMessage());
        }
    }

    public void transferFile(long storageFromId, long storageToId, long id) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(PUT_QUERY)) {

            ps.setLong(1, storageToId);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to transfer file " + id
                    + " from storage " + storageFromId + " to storage " + storageToId + " : " + e.getMessage());
        }
    }

    public void deleteFilesByStorage(long storageId) throws InternalServerException {
        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(DELETE_FILES_BY_STORAGE_QUERY)) {

            ps.setLong(1, storageId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to delete all files from storage "
                    + storageId + " : " + e.getMessage());
        }
    }

    public void checkFileName(String name) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(CHECK_FILE_NAME_QUERY)) {

            ps.setString(1, name);

            if (ps.executeUpdate() == 1) {
                throw new BadRequestException("The file with name " + name + "is already exist");
            }

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to check file name : "
                    + e.getMessage());
        }
    }

    public void checkStorageIsEmpty(long storageId) throws BadRequestException, InternalServerException {
        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(CHECK_STORAGE_FOR_EMPTY_QUERY)) {

            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            if (rs.getLong(1) == 0) {
                throw new BadRequestException("Storage is Empty");
            }

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to check storage for empty"
                    + storageId + " : " + e.getMessage());
        }
    }

    public long getFilesSizeByStorageId(long storageId) throws InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(GET_FILES_SIZE_BY_STORAGE_QUERY)) {

            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            return rs.getLong(1);

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to get size of all files from storage "
                    + storageId + " : " + e.getMessage());
        }
    }

    public void checkFormat(String[] formatSupported) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DaoTools.getConnection().prepareStatement(CHECK_FORMAT_QUERY)) {

            ResultSet rs = ps.executeQuery();

            List<String> formats = new ArrayList();
            while (rs.next()) {
                formats.add(rs.getString(1));
            }

            if (!formats.contains(Arrays.asList(formatSupported))) {
                throw new BadRequestException("Files from this storage have a format that is no longer available");
            }

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to check format updated of updated "
                    + "storage");
        }
    }

    private void putAll(Storage storage, List<File> files, Connection conn)
            throws InternalServerException, SQLException {
        try {
            conn.setAutoCommit(false);

            for (File file : files) {
                put(storage, file);
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}