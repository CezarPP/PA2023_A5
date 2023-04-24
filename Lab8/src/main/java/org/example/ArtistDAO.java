package org.example;

import java.sql.*;

public class ArtistDAO {
    private Connection connection;

    public ArtistDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("SQLException when connecting to DB: " + e.getMessage());
        }
    }

    public void addArtist(String name) {
        String query = "INSERT INTO artists (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException when adding artists: " + e.getMessage());
        }
    }

    public void listArtists() {
        String query = "SELECT * FROM artists";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + " | Name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("SQLException when listing artists: " + e.getMessage());
        }
    }
}
