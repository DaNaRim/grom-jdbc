package lesson1_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExamples {

    private static final String DB_URL =
            "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = connection.createStatement()) {

//            boolean res = statement.execute("INSERT INTO PRODUCT VALUES (2, 'toy111', 'for childeren', 60)");
//            System.out.println(res);

//            boolean res = statement.execute("DELETE FROM PRODUCT WHERE  NAME = 'toy111'");
//            System.out.println(res);

//            int response = statement.executeUpdate("INSERT INTO PRODUCT VALUES (5, 'car', 'for childeren', 70)");
//            System.out.println(response);

            int response = statement.executeUpdate("DELETE FROM product WHERE  name = 'car'");
            System.out.println(response);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }
}
