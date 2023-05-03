package org.example.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.example.misc.DatabaseConnection;
import org.example.entity.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO implements DAO<Genre> {
    private final HikariDataSource dataSource;

    public GenreDAO() {
        this.dataSource = (HikariDataSource) DatabaseConnection.getInstance().getDataSource();
    }

    @Override
    public Genre get(int id) {
        String query = "SELECT * FROM genres WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Genre(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Genre> getAll() {
        List<Genre> genres = new ArrayList<>();
        String query = "SELECT * FROM genres";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    @Override
    public int insert(Genre genre) {
        String query = "INSERT INTO genres (name) VALUES (?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, genre.getName());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating genre failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    genre.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating genre failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genre.getId();
    }

    @Override
    public int update(Genre genre) {
        String query = "UPDATE genres SET name = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, genre.getName());
            statement.setInt(2, genre.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Genre genre) {
        String query = "DELETE FROM genres WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, genre.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
