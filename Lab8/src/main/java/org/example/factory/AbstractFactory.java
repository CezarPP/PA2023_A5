package org.example.factory;

import org.example.DAOs.DAO;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.entities.GenresEntity;

public abstract class AbstractFactory {
    public abstract DAO<AlbumsEntity> getAlbumDAO();
    public abstract DAO<ArtistsEntity> getArtistDAO();
    public abstract DAO<GenresEntity> getGenreDAO();

    public static AbstractFactory getDAOFactory(DAOTypes daoType) {
        return switch (daoType) {
            case JDBC -> new JDBCDAOFactory();
            case JPA -> new JPADAOFactory();
        };
    }
}
