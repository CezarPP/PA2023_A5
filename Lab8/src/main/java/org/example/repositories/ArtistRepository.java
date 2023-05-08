package org.example.repositories;

import org.example.entities.ArtistsEntity;
import org.example.misc.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import java.util.List;

public class ArtistRepository extends AbstractRepository<ArtistsEntity> {

    public ArtistRepository() {
        super(ArtistsEntity.class);
    }

    public List<ArtistsEntity> findByName(String name) {
        EntityManager em = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        TypedQuery<ArtistsEntity> query = em.createNamedQuery("Artist.findByName", ArtistsEntity.class);
        query.setParameter("name", "%" + name + "%");
        List<ArtistsEntity> artists = query.getResultList();
        em.close();
        return artists;
    }

}
