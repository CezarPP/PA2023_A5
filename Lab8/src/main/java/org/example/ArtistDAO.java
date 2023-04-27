package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO implements DAO<Artist> {
    private Connection connection;

    public ArtistDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("SQLException when connecting to DB: " + e.getMessage());
        }
    }

    @Override
    public Artist get(int id) {
        String query = "SELECT * FROM artists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Artist(rs.getInt("id"), rs.getString("name"));
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Artist> getAll() {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM artists";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                artists.add(new Artist(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public int insert(Artist artist) {
        String query = "INSERT INTO artists (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, artist.getName());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating artist failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    artist.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating artist failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artist.getId();
    }

    @Override
    public int update(Artist artist) {
        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, artist.getName());
            statement.setInt(2, artist.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Artist artist) {
        String query = "DELETE FROM artists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, artist.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
