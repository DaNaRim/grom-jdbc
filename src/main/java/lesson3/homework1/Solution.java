package lesson3.homework1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    public List<Product> findProductsByPrice(int price, int delta) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM PRODUCT WHERE PRICE BETWEEN ? AND ?")) {
            ps.setInt(1, price - delta);
            ps.setInt(2, price + delta);
            ResultSet rs = ps.executeQuery();

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
            return products;
        } catch (SQLException e) {
            throw new SQLException("Something went wrong.");
        }
    }

    public List<Product> findProductsByName(String word) throws Exception {
        validateWord(word);

        try (PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM PRODUCT WHERE NAME LIKE ?")) {
            ps.setString(1, word);
            ResultSet rs = ps.executeQuery();

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
            return products;
        } catch (SQLException e) {
            throw new SQLException("Something went wrong.");
        }
    }

    public List<Product> findProductsWithEmptyDescription() throws SQLException {
        try (Statement st = getConnection().createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM PRODUCT WHERE DESCRIPTION IS NULL");

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
            return products;
        } catch (SQLException e) {
            throw new SQLException("Something went wrong.");
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private void validateWord(String word) throws Exception {
        if (word.split(" ").length > 1) {
            throw new Exception("Word " + word +" is incorrect. There should be no more than one word.");
        }
        if (word.length() < 3) {
            throw new Exception("Word " + word +" is incorrect. The word is too short. Must be more than two characters.");
        }
        for (char ch : word.toCharArray()) {
            if (Character.isDigit(ch)) {
                throw new Exception("Word " + word +" is incorrect. The word must not contain special characters.");
            }
        }
    }
}