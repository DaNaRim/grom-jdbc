package lesson3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    //CRUD
    //create, read, update, delete

    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    public Product save(Product product) {
        try (PreparedStatement ps = getConnection().prepareStatement("INSERT INTO PRODUCT VALUES(?, ?, ?, ?)")) {
            ps.setLong(1, product.getId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getPrice());

            int res = ps.executeUpdate();
            System.out.println("Save was finished with result " + res);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> getProducts() {
        try (Statement statement = getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM PRODUCT");

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return null;
    }

    public Product update(Product product) {
        try (PreparedStatement ps = getConnection().prepareStatement("UPDATE PRODUCT SET NAME = ?, DESCRIPTION = ?, PRICE = ? WHERE ID = ?")) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getPrice());
            ps.setLong(4, product.getId());
            ps.executeUpdate();
            System.out.println("Update was finished successfully");
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return product;
    }

    public void delete(long id) {
        try(PreparedStatement ps = getConnection().prepareStatement("DELETE FROM PRODUCT WHERE ID = ?")) {
            ps.setLong(1, id);
            System.out.println("Delete was finished successfully");
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
         return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}