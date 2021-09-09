package lesson1_2.homework3;

import java.sql.*;

public class Solution {

    private static final String DB_URL =
            "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    public void increasePrice() {
        try (Statement statement = DriverManager.getConnection(DB_URL, USER, PASS).createStatement()) {
            statement.executeUpdate("UPDATE product SET price = price + 100 WHERE price < 970");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeDescription() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = conn.createStatement()) {

            String query = "SELECT id, description FROM product WHERE LENGTH(description) > 100";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String[] sentences = resultSet.getString(2).split("\\.");

                sentences[sentences.length - 1] = null;

                StringBuilder result = new StringBuilder();
                for (String str : sentences) {
                    if (str != null) result.append(str).append(".");
                }

                PreparedStatement prst = conn.prepareStatement("UPDATE product SET description = ? WHERE id = ?");
                prst.setString(1, result.toString());
                prst.setLong(2, resultSet.getLong(1));
                prst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
