package org.example.factory;

import org.example.DAOs.DAO;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.entities.GenresEntity;
import org.example.repositories.AlbumRepository;
import org.example.repositories.ArtistRepository;
import org.example.repositories.GenreRepository;

public class JPADAOFactory extends AbstractFactory {
    @Override
    public DAO<AlbumsEntity> getAlbumDAO() {
        return new AlbumRepository();
    }

    @Override
    public DAO<ArtistsEntity> getArtistDAO() {
        return new ArtistRepository();
    }

    @Override
    public DAO<GenresEntity> getGenreDAO() {
        return new GenreRepository();
    }
}
