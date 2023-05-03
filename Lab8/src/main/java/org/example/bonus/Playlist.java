package org.example.bonus;

import org.example.entity.Album;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final String name;
    private final LocalTime creationTimestamp;
    List<Album> albumList;

    public String getName() {
        return name;
    }
    public LocalTime getCreationTimestamp() {
        return creationTimestamp;
    }
    public void addAlbum(Album album) {
        albumList.add(album);
    }

    public Playlist(String name) {
        this.name = name;
        creationTimestamp = LocalTime.now();
        albumList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", albumList=" + albumList +
                '}';
    }
}
