package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    private static final String URL =
        "jdbc:postgresql://localhost:5432/stock";

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() throws Exception {

        Class.forName("org.postgresql.Driver");

        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
    }
}