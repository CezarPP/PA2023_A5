# Lab8 and Lab9

## Lab8

* [x] Compulsory
* [x] Optional
    * [x] Create an object-oriented model
    * [x] Implement all DAO classes
    * [x] Create a connection pool -> I used HikariCP
    * [x] Tool to import data from real dataset

```java
package org.example.misc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final HikariDataSource dataSource;

    private DatabaseConnection() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("post123");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
```

* [x] Bonus
    * [x] Playlists class
    * [x] Maximal playlist of unrelated albums (BKT)

```java
public class MaximalIndependentSets {
    //...
    private void findAllMIS(int index) {
        if (allMIS.size() >= maxMIS)
            return;
        if (index >= graph.numVertices()) {
            allMIS.add(new HashSet<>(currentMIS));
            return;
        }

        if (!visited.contains(index)) {
            visited.add(index);

            Set<Integer> neighbors = new HashSet<>();
            for (int i = 0; i < graph.neighbors(index).length; i++)
                neighbors.add(graph.neighbors(index)[i]);
            Set<Integer> temp = new HashSet<>(visited);
            visited.addAll(neighbors);

            currentMIS.add(index);
            findAllMIS(index + 1);
            currentMIS.remove(index);

            visited = temp;
        }

        findAllMIS(index + 1);
    }
}
```

## Lab9

* [x] Compulsory
* [x] Homework
    * [x] Create all entity classes and repositories. Implement relations.
    * [x] Generic AbstractRepository

```java
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
```

* [x] Insert a large number of fake artists and albums

```java
package org.example.misc;

import com.github.javafaker.Faker;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.repositories.AlbumRepository;
import org.example.repositories.ArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDataGenerator {
    private static final Faker FAKER = new Faker();

    // ...
    public static void insertRandomArtistsAndAlbums() {
        ArtistRepository artistRepository = new ArtistRepository();
        AlbumRepository albumRepository = new AlbumRepository();

        final int numberOfFakeArtists = 1000;
        final int numberOfFakeAlbums = 10000;

        List<ArtistsEntity> fakeArtists = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < numberOfFakeArtists; i++) {
            ArtistsEntity fakeArtist = FakeDataGenerator.createFakeArtist();
            fakeArtists.add(fakeArtist);
            artistRepository.create(fakeArtist);
        }
        long end = System.currentTimeMillis();
        System.out.println("Inserted " + numberOfFakeArtists + " fake artists in " + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < numberOfFakeAlbums; i++) {
            AlbumsEntity fakeAlbum = FakeDataGenerator.createFakeAlbum();
            int randomArtistId = new Random().nextInt(fakeArtists.size());
            fakeAlbum.setArtist(fakeArtists.get(randomArtistId));
            albumRepository.create(fakeAlbum);
        }
        end = System.currentTimeMillis();
        System.out.println("Inserted " + numberOfFakeAlbums + " fake albums in " + (end - start) + " ms");
    }
}
```
Output:
```
Inserted 1000 fake artists in 2356 ms
Inserted 10000 fake albums in 3201 ms
```

* [x] Bonus
  * [x] JDBC and JPA implementations and use AbstractFactory to create DAO objects
```java
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

```
  * [x] Use constraint solver for a set of at least k albums with release years that are not more than p years apart
```java
public class AlbumConstraintSolver {

    public static void findAlbums(List<AlbumsEntity> albums, int k, int p) {
        System.out.println("Started solving for k -> " + k + " and p -> " + p);
        Model model = new Model("Albums solver");

        albums.sort(Comparator.comparing(AlbumsEntity::getTitle));

        int n = albums.size();

        IntVar[] isSelected = model.intVarArray("isSelected", n, 0, 1);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (albums.get(i).getTitle().charAt(0) != albums.get(j).getTitle().charAt(0)
                        || Math.abs(albums.get(i).getReleaseYear() - albums.get(j).getReleaseYear()) > p) {
                    model.arithm(isSelected[i], "+", isSelected[j], "<=", 1).post();
                }
            }
        }

        IntVar sum = model.intVar("sum", k, n);
        model.sum(isSelected, "=", sum).post();

        Solution solution = model.getSolver().findSolution();
        // print solution
    }
}
```

Output:
```
Started solving for k -> 10 and p -> 10
Solution found:
Band on the Run, released in 1973
Berlin, released in 1973
Between the Buttons, released in 1967
Bitches Brew, released in 1970
Blonde on Blonde, released in 1966
Blood on the Tracks, released in 1975
Bookends, released in 1968
Bridge Over Troubled Water, released in 1970
Bringing It All Back Home, released in 1965
Burnin', released in 1973
```