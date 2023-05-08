package org.example.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "album_genres", schema = "public", catalog = "postgres")
public class AlbumGenresEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "album_id")
    private int albumId;
    @Basic
    @Column(name = "genre_id")
    private int genreId;

    public AlbumGenresEntity(int id, int albumId, int genreId) {
        this.id = id;
        this.albumId = albumId;
        this.genreId = genreId;
    }

    public AlbumGenresEntity() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumGenresEntity that = (AlbumGenresEntity) o;
        return id == that.id && albumId == that.albumId && genreId == that.genreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, albumId, genreId);
    }
}
