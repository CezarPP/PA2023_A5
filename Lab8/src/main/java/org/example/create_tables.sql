DROP TABLE IF EXISTS album_genres CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS albums CASCADE;
DROP TABLE IF EXISTS artists CASCADE;

CREATE TABLE artists
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE albums
(
    id           SERIAL PRIMARY KEY,
    release_year INT,
    title        VARCHAR(255) NOT NULL,
    artist       INT          NOT NULL,
    CONSTRAINT fk_albums_artist FOREIGN KEY (artist) REFERENCES artists (id)
);

CREATE TABLE genres
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE album_genres
(
    id       SERIAL PRIMARY KEY,
    album_id INT NOT NULL,
    genre_id INT NOT NULL,
    CONSTRAINT fk_album_genres_album_id FOREIGN KEY (album_id) REFERENCES albums (id),
    CONSTRAINT fk_album_genres_genre_id FOREIGN KEY (genre_id) REFERENCES genres (id),
    CONSTRAINT unique_album_genre UNIQUE (album_id, genre_id)
);
