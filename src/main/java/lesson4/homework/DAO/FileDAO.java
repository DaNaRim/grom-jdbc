package lesson4.homework.DAO;

import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

import java.math.BigDecimal;
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

    private static final String QUERY_SAVE = "INSERT INTO files VALUES(?, ?, ?, ?, ?)";
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM files WHERE id = ?";
    private static final String QUERY_UPDATE =
            "UPDATE files"
                    + "   SET name = ?,"
                    + "       format = ?,"
                    + "       file_size = ?,"
                    + "       storage_id = ?"
                    + " WHERE id = ?";
    private static final String QUERY_DELETE = "DELETE FROM files WHERE id = ?";

    private static final String QUERY_PUT =
            "UPDATE files"
                    + "   SET storage_id = ?"
                    + " WHERE id = ?";
    private static final String QUERY_DELETE_FROM_STORAGE =
            "UPDATE files"
                    + "   SET storage_id = NULL"
                    + " WHERE id = ?";
    private static final String QUERY_TRANSFER_ALL =
            "UPDATE files"
                    + "   SET storage_id = ?"
                    + " WHERE storage_id = ?";

    private static final String QUERY_DELETE_FILES_BY_STORAGE = "DELETE FROM files WHERE storage_id = ?";

    private static final String QUERY_CHECK_FILE_NAME = "SELECT * FROM files WHERE name = ?";
    private static final String QUERY_CHECK_STORAGE_FOR_EMPTY = "SELECT COUNT(*) FROM files WHERE storage_id = ?";
    private static final String QUERY_GET_FILES_SIZE_BY_STORAGE =
            "SELECT SUM(file_size)"
                    + "  FROM files"
                    + " WHERE storage_id = ?";
    private static final String QUERY_CHECK_FORMAT =
            "SELECT format"
                    + "  FROM files"
                    + " GROUP BY format";

    public File save(File file) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_SAVE)) {

            file.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

            ps.setLong(1, file.getId());
            ps.setString(2, file.getName());
            ps.setString(3, file.getFormat());
            ps.setLong(4, file.getSize());
            ps.setObject(5, null);
            ps.executeUpdate();

            return file;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to save the file " + file.getName()
                    + " : " + e.getMessage());
        }
    }

    public File findById(long id) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_FIND_BY_ID)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new BadRequestException("missing file with id: " + id);
            }

            BigDecimal bd = (BigDecimal) rs.getObject(5);
            return new File(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getLong(4),
                    bd == null ? null : storageDAO.findById(bd.longValue()));

        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to find file with id " + id + " : "
                    + e.getMessage());
        }
    }

    public File update(File file) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_UPDATE)) {

            Storage fileStorage = file.getStorage();

            ps.setString(1, file.getName());
            ps.setString(2, file.getFormat());
            ps.setLong(3, file.getSize());
            ps.setObject(4, fileStorage == null ? null : fileStorage.getId());
            ps.setLong(5, file.getId());
            ps.executeUpdate();

            return file;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to update the file " + file.getId()
                    + " : " + e.getMessage());
        }
    }

    public void delete(long id) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_DELETE)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to delete the file " + id
                    + " : " + e.getMessage());
        }
    }

    public File put(Storage storage, File file) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_PUT)) {

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

        try (Connection conn = DAOTools.getConnection()) {

            putAll(storage, files, conn);

        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException("something went wrong while trying to put all files in storage "
                    + storage.getId() + " : " + e.getMessage());
        }
    }

    public File deleteFromStorage(long StorageId, File file) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_DELETE_FROM_STORAGE)) {

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

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_TRANSFER_ALL)) {

            ps.setLong(1, storageToId);
            ps.setLong(2, storageFromId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to transfer files from storage "
                    + storageFromId + " to storage " + storageToId + " : " + e.getMessage());
        }
    }

    public void transferFile(long storageFromId, long storageToId, long id) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_PUT)) {

            ps.setLong(1, storageToId);
            ps.setLong(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "something went wrong while trying to transfer file %d from storage %d to storage %d : %s",
                    id, storageFromId, storageToId, e.getMessage()));
        }
    }

    public void deleteFilesByStorage(long storageId) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_DELETE_FILES_BY_STORAGE)) {

            ps.setLong(1, storageId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to delete all files from storage "
                    + storageId + " : " + e.getMessage());
        }
    }

    public void checkFileName(String name) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_CHECK_FILE_NAME)) {

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
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_CHECK_STORAGE_FOR_EMPTY)) {

            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            rs.next();
            if (rs.getLong(1) == 0) {
                throw new BadRequestException("Storage is Empty");
            }

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to check storage for empty"
                    + storageId + " : " + e.getMessage());
        }
    }

    public long getFilesSizeByStorageId(long storageId) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_GET_FILES_SIZE_BY_STORAGE)) {

            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to get size of all files from storage "
                    + storageId + " : " + e.getMessage());
        }
    }

    public void checkFormat(String[] formatsSupported) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_CHECK_FORMAT)) {

            ResultSet rs = ps.executeQuery();

            List<String> formats = new ArrayList<>();
            while (rs.next()) {
                formats.add(rs.getString(1));
            }

            for (String format : formats) {
                if (!Arrays.asList(formatsSupported).contains(format)) {
                    throw new BadRequestException("Files from this storage have a format that is no longer available");
                }
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
