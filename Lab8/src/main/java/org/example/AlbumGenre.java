package org.example;

public class AlbumGenre {
    private int id;
    private int albumId;
    private int genreId;

    public AlbumGenre(int id, int albumId, int genreId) {
        this.id = id;
        this.albumId = albumId;
        this.genreId = genreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
