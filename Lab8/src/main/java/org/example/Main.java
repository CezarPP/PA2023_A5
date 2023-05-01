package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // new AlbumImporter("/album_list.csv").importData();

        AlbumDAO albumDAO = new AlbumDAO();
        System.out.println("Listing all albums:");
        List<Album> albumList = albumDAO.getAll();
        for (Album album : albumList)
            System.out.println(album);

        ArtistDAO artistDAO = new ArtistDAO();
        System.out.println("Listing all artists:");
        List<Artist> artistList = artistDAO.getAll();
        for (Artist artist : artistList)
            System.out.println(artist);
    }
}