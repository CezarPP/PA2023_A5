package org.example;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumGenreDAO implements DAO<AlbumGenre> {
    private final HikariDataSource dataSource;

    public AlbumGenreDAO() {
        this.dataSource = (HikariDataSource) DatabaseConnection.getInstance().getDataSource();
    }

    @Override
    public AlbumGenre get(int id) {
        String query = "SELECT * FROM album_genres WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int albumId = resultSet.getInt("album_id");
                    int genreId = resultSet.getInt("genre_id");
                    return new AlbumGenre(id, albumId, genreId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AlbumGenre> getAll() {
        List<AlbumGenre> albumGenres = new ArrayList<>();
        String query = "SELECT * FROM album_genres";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int albumId = resultSet.getInt("album_id");
                int genreId = resultSet.getInt("genre_id");
                albumGenres.add(new AlbumGenre(id, albumId, genreId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albumGenres;
    }

    @Override
    public int insert(AlbumGenre albumGenre) {
        String query = "INSERT INTO album_genres (album_id, genre_id) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, albumGenre.getAlbumId());
            statement.setInt(2, albumGenre.getGenreId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int update(AlbumGenre albumGenre) {
        String query = "UPDATE album_genres SET album_id = ?, genre_id = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, albumGenre.getAlbumId());
            statement.setInt(2, albumGenre.getGenreId());
            statement.setInt(3, albumGenre.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int delete(AlbumGenre albumGenre) {
        String query = "DELETE FROM album_genres WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, albumGenre.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Album> getAlbumsByGenre(int genreId) {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT a.* FROM albums a INNER JOIN album_genres ag ON a.id = ag.album_id WHERE ag.genre_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, genreId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int releaseYear = resultSet.getInt("release_year");
                    String title = resultSet.getString("title");
                    int artist = resultSet.getInt("artist");
                    albums.add(new Album(id, releaseYear, title, artist));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public List<Genre> getGenresOfAlbum(int albumId) {
        String query = "SELECT g.id, g.name FROM genres g " +
                "JOIN album_genres ag ON g.id = ag.genre_id " +
                "WHERE ag.album_id = ?";
        List<Genre> genres = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, albumId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                genres.add(new Genre(id, name));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching genres for album " + albumId + ": " + e.getMessage());
        }

        return genres;
    }
}