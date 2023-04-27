package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO implements DAO<Album> {
    private Connection connection;

    public AlbumDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.out.println("SQLException when connecting to DB: " + e.getMessage());
        }
    }

    @Override
    public Album get(int id) {
        String query = "SELECT * FROM albums WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Album(rs.getInt("id"), rs.getInt("release_year"), rs.getString("title"), rs.getInt("artist"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Album> getAll() {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT * FROM albums";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                albums.add(new Album(rs.getInt("id"), rs.getInt("release_year"), rs.getString("title"), rs.getInt("artist")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    @Override
    public int insert(Album album) {
        String query = "INSERT INTO albums (release_year, title, artist) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, album.getRelease_year());
            statement.setString(2, album.getTitle());
            statement.setInt(3, album.getArtist());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating album failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    album.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating album failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return album.getId();
    }

    @Override
    public int update(Album album) {
        String query = "UPDATE albums SET release_year = ?, title = ?, artist = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, album.getRelease_year());
            statement.setString(2, album.getTitle());
            statement.setInt(3, album.getArtist());
            statement.setInt(4, album.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Album album) {
        String query = "DELETE FROM albums WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, album.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
