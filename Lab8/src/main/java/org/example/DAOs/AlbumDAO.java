package org.example.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.example.classes.Album;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.misc.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO implements DAO<AlbumsEntity> {
    private final HikariDataSource dataSource;

    public AlbumDAO() {
        this.dataSource = (HikariDataSource) DatabaseConnection.getInstance().getDataSource();
    }

    @Override
    public AlbumsEntity findById(AlbumsEntity id) {
        String query = "SELECT * FROM albums WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id.getId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int albumId = rs.getInt("id");
                    int release_year = rs.getInt("release_year");
                    String title = rs.getString("title");
                    int artistId = rs.getInt("artist");
                    ArtistsEntity artistsEntity = new ArtistsEntity();
                    artistsEntity.setId(artistId);
                    ArtistsEntity artist = new ArtistDAO().findById(artistsEntity);
                    return new AlbumsEntity(albumId, release_year, title, artist);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AlbumsEntity> getAll() {
        List<AlbumsEntity> albums = new ArrayList<>();
        String query = "SELECT * FROM albums";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int releaseYear = rs.getInt("release_year");
                String title = rs.getString("title");
                int artistId = rs.getInt("artist");
                ArtistsEntity auxArtist = new ArtistsEntity();
                auxArtist.setId(artistId);
                albums.add(new AlbumsEntity(id, releaseYear, title, new ArtistDAO().findById(auxArtist)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return albums;
    }

    @Override
    public void create(AlbumsEntity album) {
        String query = "INSERT INTO albums (release_year, title, artist) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, album.getReleaseYear());
            statement.setString(2, album.getTitle());
            statement.setInt(3, album.getArtist().getId());
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
    }

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
