package org.example.factory;

import org.example.DAOs.AlbumDAO;
import org.example.DAOs.ArtistDAO;
import org.example.DAOs.DAO;
import org.example.DAOs.GenreDAO;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.entities.GenresEntity;

public class JDBCDAOFactory extends AbstractFactory {

    @Override
    public DAO<AlbumsEntity> getAlbumDAO() {
        return new AlbumDAO();
    }

    @Override
    public DAO<ArtistsEntity> getArtistDAO() {
        return new ArtistDAO();
    }

    @Override
    public DAO<GenresEntity> getGenreDAO() {
        return new GenreDAO();
    }
}
