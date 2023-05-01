package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

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
                    Artist artist = new Artist(0, name);
                    artistDAO.insert(artist);
                    return artist.getId();
                });

                Album album = new Album(0, year, albumTitle, artistId);
                albumDAO.insert(album);

                int genreId = genreMap.computeIfAbsent(genreName, name -> {
                    Genre genre = new Genre(0, name);
                    genreDAO.insert(genre);
                    return genre.getId();
                });

                AlbumGenre albumGenre = new AlbumGenre(0, album.getId(), genreId);
                albumGenreDAO.insert(albumGenre);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
