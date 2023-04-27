package org.example;

public class Album {
    private int id;
    private int release_year;
    private String title;
    int artist;

    public Album(int id, int release_year, String title, int artist) {
        this.id = id;
        this.release_year = release_year;
        this.title = title;
        this.artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArtist() {
        return artist;
    }

    public void setArtist(int artist) {
        this.artist = artist;
    }
}
