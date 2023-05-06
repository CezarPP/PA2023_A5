package org.example.repositories;

import org.example.entities.AlbumsEntity;
import org.example.entities.GenresEntity;
import org.example.misc.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class GenreRepository extends AbstractRepository<GenresEntity>  {

    GenreRepository() {
        super(GenresEntity.class);
    }
    public List<GenresEntity> findByName(String name) {
        EntityManager em = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        TypedQuery<GenresEntity> query = em.createNamedQuery("Genre.findByName", GenresEntity.class);
        query.setParameter("name", "%" + name + "%");
        List<GenresEntity> genres = query.getResultList();
        em.close();
        return genres;
    }
}
