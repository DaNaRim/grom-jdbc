package lesson4.homework.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOTools {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.c2nwr4ze1uqa.us-east-2.rds.amazonaws.com:1521:ORCL";
    private static final String USER = "main";
    private static final String PASS = "PyP2p02rIZ9uyMBpTBwW";

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}