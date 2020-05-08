package lesson4.homework.DAO;

import lesson4.homework.Exceptions.BadRequestException;
import lesson4.homework.Exceptions.InternalServerException;
import lesson4.homework.model.File;
import lesson4.homework.model.Storage;

import java.sql.*;
import java.util.HashSet;
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
        try (PreparedStatement ps = getConnection().prepareStatement("UPDATE STORAGE SET FORMATS_SUPPORTED = ?, COUNTRY = ?, STORAGE_SIZE = ?, FREESPACE = ? WHERE ID = ?")) {

            StringBuilder formatsSupported = new StringBuilder();
            for (String str : storage.getFormatsSupported()) {
                formatsSupported.append(str);
                formatsSupported.append(", ");
            }
            formatsSupported.delete(formatsSupported.lastIndexOf(", "), formatsSupported.length());

            ps.setString(1, formatsSupported.toString());
            ps.setString(2, storage.getStorageCountry());
            ps.setLong(3, storage.getStorageSize());
            ps.setLong(4, storage.getFreeSpace());
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
            HashSet<File> files = FileDAO.getFilesByStorage(storage);
            storage.setFiles(files);

            return storage;
        } catch (SQLException e) {
            throw new InternalServerException("An error occurred while trying to find storage with id " + id + " : " +
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

            ps.setLong(4, id);
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException | InternalServerException | BadRequestException e) {
            conn.rollback();
            throw e;
        }
    }
}