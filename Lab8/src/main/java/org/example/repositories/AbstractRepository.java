package org.example.repositories;

import org.example.misc.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class AbstractRepository<T> {
    private final Class<T> entityClass;

    protected AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        return PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
    }

    public void create(T entity) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
    }

    public T findById(Object id) {
        EntityManager em = getEntityManager();
        T entity = em.find(entityClass, id);
        em.close();
        return entity;
    }

    public List<T> findByName(String name, String queryName) {
        EntityManager em = getEntityManager();
        TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
        query.setParameter("name", "%" + name + "%");
        List<T> resultList = query.getResultList();
        em.close();
        return resultList;
    }
}
