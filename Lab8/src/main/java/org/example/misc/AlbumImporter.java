package org.example.misc;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.DAOs.AlbumDAO;
import org.example.DAOs.AlbumGenreDAO;
import org.example.DAOs.ArtistDAO;
import org.example.DAOs.GenreDAO;
import org.example.classes.Album;
import org.example.classes.AlbumGenre;
import org.example.classes.Artist;
import org.example.classes.Genre;
import org.example.entities.AlbumGenresEntity;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.entities.GenresEntity;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AlbumImporter {
    private final String datasetPath;

    public AlbumImporter(String datasetPath) {
        this.datasetPath = datasetPath;
    }

    public void importData() {
        ArtistDAO artistDAO = new ArtistDAO();
        AlbumDAO albumDAO = new AlbumDAO();
        GenreDAO genreDAO = new GenreDAO();
        AlbumGenreDAO albumGenreDAO = new AlbumGenreDAO();

        Map<String, Integer> artistMap = new HashMap<>();
        Map<String, Integer> genreMap = new HashMap<>();

        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects
                .requireNonNull(AlbumImporter.class.getResourceAsStream(datasetPath)));
             CSVReader reader = new CSVReader(inputStreamReader)) {
            // Skip the header row
            reader.skip(1);


            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                int number = Integer.parseInt(record[0]);
                int year = Integer.parseInt(record[1]);
                String albumTitle = record[2];
                String artistName = record[3];
                String genreName = record[4];

                int artistId = artistMap.computeIfAbsent(artistName, name -> {
                    ArtistsEntity artist = new ArtistsEntity(0, name);
                    artistDAO.create(artist);
                    return artist.getId();
                });

                AlbumsEntity album = new AlbumsEntity(0, year, albumTitle, artistId);
                albumDAO.create(album);

                int genreId = genreMap.computeIfAbsent(genreName, name -> {
                    GenresEntity genre = new GenresEntity(0, name);
                    genreDAO.create(genre);
                    return genre.getId();
                });

                AlbumGenresEntity albumGenre = new AlbumGenresEntity(0, album.getId(), genreId);
                albumGenreDAO.create(albumGenre);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
