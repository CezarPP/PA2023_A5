package org.example.misc;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {
    private static PersistenceManager instance;
    private final EntityManagerFactory entityManagerFactory;

    private PersistenceManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
    }

    public static synchronized PersistenceManager getInstance() {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
