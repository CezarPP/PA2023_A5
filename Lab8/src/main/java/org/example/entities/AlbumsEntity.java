package org.example.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "albums", schema = "public", catalog = "postgres")
@NamedQuery(name = "Album.findByTitle", query = "SELECT a FROM AlbumsEntity a WHERE a.title LIKE :title")
public class AlbumsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "release_year")
    private Integer releaseYear;
    @Basic
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "artist", referencedColumnName = "id")
    private ArtistsEntity artist;

    @ManyToMany
    @JoinTable(
            name = "album_genres",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenresEntity> genres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArtistsEntity getArtist() {
        return artist;
    }

    public void setArtist(ArtistsEntity artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumsEntity that = (AlbumsEntity) o;
        return id == that.id && artist == that.artist && Objects.equals(releaseYear, that.releaseYear) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, releaseYear, title, artist);
    }
}
