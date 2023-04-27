package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArtistDAO artistDAO = new ArtistDAO();
/*
        artistDAO.addArtist("The Beatles");
        artistDAO.addArtist("Led Zeppelin");*/

        System.out.println("Listing all artists:");
        List<Artist> artistList = artistDAO.getAll();
        for (Artist artist : artistList)
            System.out.println(artist);
    }
}