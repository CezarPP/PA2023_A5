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

    public static ArtistsEntity createFakeArtist() {
        String name = FAKER.artist().name();
        ArtistsEntity artist = new ArtistsEntity();
        artist.setName(name);
        return artist;
    }

    public static AlbumsEntity createFakeAlbum() {
        String title = FAKER.music().instrument();
        AlbumsEntity album = new AlbumsEntity();
        album.setTitle(title);
        album.setReleaseYear(FAKER.number().numberBetween(1970, 2023));
        return album;
    }

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
