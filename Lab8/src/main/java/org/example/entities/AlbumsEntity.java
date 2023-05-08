package org.example.entities;

import org.example.DAOs.AlbumGenreDAO;
import org.example.classes.Genre;

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

    private int artistId;

    @ManyToMany
    @JoinTable(
            name = "album_genres",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenresEntity> genres;

    public AlbumsEntity(int id, Integer releaseYear, String title, ArtistsEntity artist, List<GenresEntity> genres) {
        this.id = id;
        this.releaseYear = releaseYear;
        this.title = title;
        this.artist = artist;
        this.genres = genres;
    }

    public AlbumsEntity(int id, Integer releaseYear, String title, int artistId) {
        this.id = id;
        this.releaseYear = releaseYear;
        this.title = title;
        this.artistId = artistId;
    }

    public AlbumsEntity(int id, Integer releaseYear, String title, ArtistsEntity artist) {
        this.id = id;
        this.releaseYear = releaseYear;
        this.title = title;
        this.artist = artist;
    }

    public AlbumsEntity() {

    }

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

    public boolean checkAreRelated(AlbumsEntity album) {
        if (this.artist == album.artist || Objects.equals(this.releaseYear, album.releaseYear))
            return true;
        AlbumGenreDAO albumGenreDAO = new AlbumGenreDAO();
        List<Genre> genreList1 = albumGenreDAO.getGenresOfAlbum(this.id);
        List<Genre> genreList2 = albumGenreDAO.getGenresOfAlbum(album.id);

        return genreList1.stream().anyMatch(genreList2::contains);
    }
}
