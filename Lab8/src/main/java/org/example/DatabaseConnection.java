package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    final private String URL = "jdbc:postgresql://localhost:5432/postgres";
    // TODO()
    final private String USERNAME = "postgres";
    final private String PASSWORD = "post123";

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed: " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }
}
