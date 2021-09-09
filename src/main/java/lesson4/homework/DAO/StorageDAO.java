package lesson4.homework.DAO;

import lesson4.homework.exceptions.BadRequestException;
import lesson4.homework.exceptions.InternalServerException;
import lesson4.homework.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StorageDAO {

    private static final FileDAO fileDAO = new FileDAO();

    private static final String QUERY_SAVE = "INSERT INTO storage VALUES (?, ?, ?, ?)";
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM storage WHERE id = ?";
    private static final String QUERY_UPDATE =
            "UPDATE storage"
                    + "   SET formats_supported = ?,"
                    + "       country = ?,"
                    + "       storage_size = ?"
                    + " WHERE id = ?";
    private static final String QUERY_DELETE = "DELETE storage WHERE id = ?";

    public Storage save(Storage storage) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_SAVE)) {

            storage.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

            StringBuilder formatsSupported = new StringBuilder();
            for (String str : storage.getFormatsSupported()) {
                formatsSupported.append(str).append(", ");
            }
            formatsSupported.delete(formatsSupported.lastIndexOf(", "), formatsSupported.length());

            ps.setLong(1, storage.getId());
            ps.setString(2, formatsSupported.toString());
            ps.setString(3, storage.getStorageCountry());
            ps.setLong(4, storage.getStorageSize());
            ps.executeUpdate();

            return storage;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to save storage: " + e.getMessage());
        }
    }

    public Storage findById(long id) throws BadRequestException, InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_FIND_BY_ID)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new BadRequestException("missing storage with id: " + id);
            }
            return new Storage(
                    rs.getLong(1),
                    rs.getString(2).split(", "),
                    rs.getString(3),
                    rs.getLong(4));

        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to find storage with id " + id + " : "
                    + e.getMessage());
        }
    }


    public Storage update(Storage storage) throws InternalServerException {

        try (PreparedStatement ps = DAOTools.getConnection().prepareStatement(QUERY_UPDATE)) {

            StringBuilder formatsSupported = new StringBuilder();
            for (String str : storage.getFormatsSupported()) {
                formatsSupported.append(str).append(", ");
            }
            formatsSupported.delete(formatsSupported.lastIndexOf(", "), formatsSupported.length());

            ps.setString(1, formatsSupported.toString());
            ps.setString(2, storage.getStorageCountry());
            ps.setLong(3, storage.getStorageSize());
            ps.setLong(4, storage.getId());
            ps.executeUpdate();

            return storage;
        } catch (SQLException e) {
            throw new InternalServerException("something went wrong while trying to update the storage "
                    + storage.getId() + " : " + e.getMessage());
        }
    }

    public void delete(long id) throws InternalServerException {

        try (Connection conn = DAOTools.getConnection()) {

            delete(id, conn);

        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException("something went wrong while trying to delete storage " + id + " : " +
                    e.getMessage());
        }
    }

    private void delete(long id, Connection conn) throws SQLException, InternalServerException {

        try (PreparedStatement ps = conn.prepareStatement(QUERY_DELETE)) {
            conn.setAutoCommit(false);

            fileDAO.deleteFilesByStorage(id);

            ps.setLong(1, id);
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException | InternalServerException e) {
            conn.rollback();
            throw e;
        }
    }
}
