package data.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

    public static final String JDBC_MYSQL_HOST = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "quiz";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    public static Connection getHostConnection() throws SQLException {
            return DriverManager.getConnection(JDBC_MYSQL_HOST, USER, PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(JDBC_MYSQL_HOST + DB_NAME, USER, PASSWORD);
    }
}