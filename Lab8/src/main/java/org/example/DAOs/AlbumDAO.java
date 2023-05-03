package org.example.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.example.classes.Album;
import org.example.misc.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO implements DAO<Album> {
    private final HikariDataSource dataSource;

    public AlbumDAO() {
        this.dataSource = (HikariDataSource) DatabaseConnection.getInstance().getDataSource();
    }

    @Override
    public Album get(int id) {
        String query = "SELECT * FROM albums WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, album.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
