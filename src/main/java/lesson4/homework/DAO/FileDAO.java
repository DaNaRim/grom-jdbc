package lesson4.homework.DAO;

import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.exceptions.NotFoundException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private static final String QUERY_PUT = "UPDATE files SET storage_id = ? WHERE id = ?";
    private static final String QUERY_DELETE_FROM_STORAGE = "UPDATE files SET storage_id = NULL WHERE id = ?";
    private static final String QUERY_TRANSFER_ALL = "UPDATE files SET storage_id = ? WHERE storage_id = ?";
    private static final String QUERY_DELETE_FILES_BY_STORAGE = "DELETE FROM files WHERE storage_id = ?";

    private static final String QUERY_IS_EXISTS = "SELECT 1 FROM files WHERE id = ?";
    private static final String QUERY_GET_FILE_NAME_BY_ID = "SELECT name FROM files WHERE id = ?";
    private static final String QUERY_CHECK_FILE_NAME_FOR_UNIQUE = "SELECT 1 FROM files WHERE name = ?";
    private static final String QUERY_GET_FILES_COUNT_BY_STORAGE = "SELECT COUNT(*) FROM files WHERE storage_id = ?";
    private static final String QUERY_GET_FILES_SIZE_BY_STORAGE = "SELECT SUM(file_size) FROM files WHERE storage_id = ?";
    private static final String QUERY_GET_FILES_FORMAT = "SELECT format FROM files WHERE storage_id = ? GROUP BY format";

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
            throw new InternalServerException("Save file with name" + file.getName() + " failed: " + e.getMessage());
        }
    }

    public File findById(long id) throws InternalServerException, NotFoundException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new NotFoundException("missing file with id " + id);
            }

            long storageId = rs.getLong(5);
            return new File(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getLong(4),
                    storageId == 0 ? null : storageDAO.findById(storageId));
        } catch (SQLException e) {
            throw new InternalServerException("find file " + id + " failed: " + e.getMessage());
        }
    }

    public boolean isExists(long id) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_IS_EXISTS)) {
            ps.setLong(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new InternalServerException("check is exists file with id " + id + " failed: " + e.getMessage());
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
            throw new InternalServerException("update file " + file.getId() + " failed: " + e.getMessage());
        }
    }

    public void delete(long id) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException("delete file " + id + " failed: " + e.getMessage());
        }
    }

    public File put(Storage storage, File file) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_PUT)) {
            ps.setLong(1, storage.getId());
            ps.setLong(2, file.getId());
            ps.executeUpdate();

            file.setStorage(storage);
            return file;
        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "put file %din storage %d failed: %s", file.getId(), storage.getId(), e.getMessage()));
        }
    }

    public void putAll(Storage storage, List<File> files) throws InternalServerException {
        try (Connection conn = DAOTools.getConnection()) {
            putAll(storage, files, conn);
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(
                    String.format("put all files in storage %d failed: %s", storage.getId(), e.getMessage()));
        }
    }

    public File deleteFromStorage(long StorageId, File file) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_DELETE_FROM_STORAGE)) {
            ps.setLong(1, file.getId());
            ps.executeUpdate();

            file.setStorage(null);
            return file;
        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "delete file %d from storage %d : %s", file.getId(), StorageId, e.getMessage()));
        }
    }

    public void transferAll(long storageFromId, long storageToId) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_TRANSFER_ALL)) {
            ps.setLong(1, storageToId);
            ps.setLong(2, storageFromId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "transfer files from storage %d to storage %d failed: %s",
                    storageFromId, storageToId, e.getMessage()));
        }
    }

    public void transferFile(long storageFromId, long storageToId, long id) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_PUT)) {
            ps.setLong(1, storageToId);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "transfer file %d from storage %d to storage %d failed: %s",
                    id, storageFromId, storageToId, e.getMessage()));
        }
    }

    public void deleteFilesByStorage(long storageId) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_DELETE_FILES_BY_STORAGE)) {
            ps.setLong(1, storageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "delete all files from storage %d failed: %s", storageId, e.getMessage()));
        }
    }

    public boolean isNameUnique(String name) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_CHECK_FILE_NAME_FOR_UNIQUE)) {
            ps.setString(1, name);

            return !ps.executeQuery().next();
        } catch (SQLException e) {
            throw new InternalServerException("is file name unique failed: " + e.getMessage());
        }
    }

    public String getFileName(long id) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_GET_FILE_NAME_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            throw new InternalServerException("get file name failed: " + e.getMessage());
        }
    }

    public boolean isStorageEmpty(long storageId) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_GET_FILES_COUNT_BY_STORAGE)) {
            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getLong(1) == 0;
        } catch (SQLException e) {
            throw new InternalServerException("check storage " + storageId + " for empty failed: " + e.getMessage());
        }
    }

    public long getFilesSizeByStorageId(long storageId) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_GET_FILES_SIZE_BY_STORAGE)) {
            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new InternalServerException(String.format(
                    "get size of all files from storage %d failed: %s", storageId, e.getMessage()));
        }
    }

    public String[] getFilesFormatsByStorage(long storageId) throws InternalServerException {
        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_GET_FILES_FORMAT)) {
            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            ArrayList<String> formats = new ArrayList<>();
            while (rs.next()) {
                formats.add(rs.getString(1));
            }

            return formats.toArray(new String[0]);
        } catch (SQLException e) {
            throw new InternalServerException("check format failed" + e.getMessage());
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
            throw new InternalServerException("put all files failed: " + e.getMessage());
        }
    }
}
