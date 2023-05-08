package org.example.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.example.entities.ArtistsEntity;
import org.example.misc.DatabaseConnection;
import org.example.classes.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO implements DAO<ArtistsEntity> {
    private final HikariDataSource dataSource;

    public ArtistDAO() {
        this.dataSource = (HikariDataSource) DatabaseConnection.getInstance().getDataSource();
    }

    @Override
    public ArtistsEntity findById(ArtistsEntity id) {
        String query = "SELECT * FROM artists WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id.getId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new ArtistsEntity(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ArtistsEntity> getAll() {
        List<ArtistsEntity> artists = new ArrayList<>();
        String query = "SELECT * FROM artists";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                artists.add(new ArtistsEntity(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public void create(ArtistsEntity artist) {
        String query = "INSERT INTO artists (name) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

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
    }

    public int update(Artist artist) {
        String query = "UPDATE artists SET name = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, artist.getName());
            statement.setInt(2, artist.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Artist artist) {
        String query = "DELETE FROM artists WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, artist.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Artist getByName(String name) {
        String query = "SELECT * FROM artists WHERE name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Artist(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
