package org.example;

public class Main {
    public static void main(String[] args) {
        ArtistDAO artistDAO = new ArtistDAO();

        artistDAO.addArtist("The Beatles");
        artistDAO.addArtist("Led Zeppelin");

        System.out.println("Listing all artists:");
        artistDAO.listArtists();
    }
}