package org.example.entity;

import org.example.DAOs.AlbumGenreDAO;

import java.util.List;

public class Album {
    private int id;
    private int release_year;
    private String title;
    private int artist;

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

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", release_year=" + release_year +
                ", title='" + title + '\'' +
                ", artist=" + artist +
                '}';
    }

    public boolean checkAreRelated(Album album) {
        if (this.artist == album.artist || this.release_year == album.release_year)
            return true;
        AlbumGenreDAO albumGenreDAO = new AlbumGenreDAO();
        List<Genre> genreList1 = albumGenreDAO.getGenresOfAlbum(this.id);
        List<Genre> genreList2 = albumGenreDAO.getGenresOfAlbum(album.id);

        return genreList1.stream().anyMatch(genreList2::contains);
    }
}
