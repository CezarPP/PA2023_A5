package org.example.repositories;

import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.misc.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AlbumRepository extends AbstractRepository<AlbumsEntity> {

    public AlbumRepository() {
        super(AlbumsEntity.class);
    }

    public List<AlbumsEntity> findByTitle(String title) {
        EntityManager em = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        TypedQuery<AlbumsEntity> query = em.createNamedQuery("Album.findByTitle", AlbumsEntity.class);
        query.setParameter("title", "%" + title + "%");
        List<AlbumsEntity> albums = query.getResultList();
        em.close();
        return albums;
    }

    public List<AlbumsEntity> getAll() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        TypedQuery<AlbumsEntity> query = em.createQuery("SELECT a FROM AlbumsEntity a", AlbumsEntity.class);
        return query.getResultList();
    }

}
