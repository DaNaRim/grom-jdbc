package lesson4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TransactionDemo {
    /*
    ACID
    Atomicity
    Consistency
    Isolation
    Durability
     */

    //1. save order - pay money - receive money + ...b
    //2. save order - pay money - receive money

    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    public static void save(List<Product> products) {
        try (Connection conn = getConnection()) {
            saveList(products, conn);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    private static void saveList(List<Product> products, Connection conn) throws SQLException {
        long productID = products.get(0).getId();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO PRODUCT VALUES(?, ?, ?, ?)")) {
            conn.setAutoCommit(false);

            for (Product product : products) {
                productID = product.getId();
                ps.setLong(1, productID);
                ps.setString(2, product.getName());
                ps.setString(3, product.getDescription());
                ps.setInt(4, product.getPrice());

                int res = ps.executeUpdate();
                System.out.println("Save was finished with result " + res);
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Error saving user with id: " + productID + " " + e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}