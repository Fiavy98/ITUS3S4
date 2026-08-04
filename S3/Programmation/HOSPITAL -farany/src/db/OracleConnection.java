package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {

    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/EE.oracle.docker";
    private static final String USER = "vaniala";
    private static final String PASSWORD = "vaniala";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
