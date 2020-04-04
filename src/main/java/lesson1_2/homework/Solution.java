package lesson1_2.homework;

import java.sql.*;

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

    public void increasePrice() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            statement.executeUpdate("UPDATE PRODUCT SET PRICE = PRICE + 100 WHERE PRICE < 970");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeDescription() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = conn.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT ID, DESCRIPTION FROM PRODUCT WHERE LENGTH(DESCRIPTION) > 100");

            while (resultSet.next()) {
                String[] sentences = resultSet.getString(2).split("\\.");

                sentences[sentences.length - 1] = null;

                String result = "";
                for (String str : sentences) {
                    if (str != null) result += str + ".";
                }

                PreparedStatement prst = conn.prepareStatement("UPDATE PRODUCT SET DESCRIPTION = ? WHERE ID = ?");
                prst.setString(1, result);
                prst.setLong(2, resultSet.getLong(1));
                prst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}