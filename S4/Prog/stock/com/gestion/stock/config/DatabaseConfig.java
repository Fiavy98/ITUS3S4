package com.gestion.stock.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {

    // ================= CONFIG DB =================
    private static final String URL =
            "jdbc:postgresql://localhost:5432/stock";

    private static final String USER =
            "postgres";

    private static final String PASSWORD =
            "postgres";

    // ================= CONNECTION =================
    public static Connection getConnection() {

        try {

            // Charger le driver PostgreSQL
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Connexion PostgreSQL OK");

            return conn;

        } catch (Exception e) {

            System.out.println("Erreur connexion DB");

            e.printStackTrace();

            return null;
        }
    }
}