package lesson3.homework2;

import java.sql.*;
import java.util.Date;

public class Solution {

    private static final String DB_URL =
            "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    //141684
    public long testSavePerformance() {
        long start = new Date().getTime();

        String query = "INSERT INTO product VALUES (?, 'TEST', 'TEST', 100)";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            for (int i = 0; i < 1000; i++) {
                ps.setLong(1, i);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Date().getTime() - start;
    }

    //138189
    public long testDeleteByIdPerformance() {
        long start = new Date().getTime();

        try (PreparedStatement ps = getConnection().prepareStatement("DELETE FROM product WHERE ID = ?")) {
            for (int i = 0; i < 1000; i++) {
                ps.setLong(1, i);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Date().getTime() - start;
    }

    //1437
    public long testDeletePerformance() {
        long start = new Date().getTime();

        try (Statement st = getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM product");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Date().getTime() - start;
    }

    //135622
    public long testSelectByIdPerformance() {
        long start = new Date().getTime();
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM product WHERE id = ?")) {
            for (int i = 0; i < 1000; i++) {
                ps.setLong(1, i);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Date().getTime() - start;
    }

    //1484
    public long testSelectPerformance() {
        long start = new Date().getTime();

        try (Statement st = getConnection().createStatement()) {
            st.executeUpdate("SELECT * FROM product");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Date().getTime() - start;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
