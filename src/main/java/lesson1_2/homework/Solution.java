package lesson1_2.homework;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Solution {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    public void saveProduct() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            statement.executeUpdate("INSERT INTO PRODUCT VALUES (999, 'toy', 'for children', 60)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProducts() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            statement.executeUpdate("DELETE FROM PRODUCT WHERE NAME != 'toy'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            statement.executeUpdate("DELETE FROM PRODUCT WHERE PRICE < 100");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllProducts() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            return statement.executeQuery("SELECT * FROM PRODUCT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getProductByPrice() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            return statement.executeQuery("SELECT * FROM PRODUCT WHERE PRICE <= 100");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getProductsByDescription() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            return statement.executeQuery("SELECT * FROM PRODUCT WHERE LENGTH(DESCRIPTION) > 50");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}