package connection;

import java.sql.*;

public class OracleConnection {
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/EE.oracle.docker";
    private static final String USER = "tsinjo";
    private static final String PASSWORD = "maintso";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
