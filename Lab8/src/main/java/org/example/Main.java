package org.example;

import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import java.util.List;
import java.util.Set;

public class Main {
    static final int MAX_MIS = 10;
    public static void main(String[] args) {
        // new AlbumImporter("/album_list.csv").importData();

        AlbumDAO albumDAO = new AlbumDAO();
        System.out.println("Listing all albums:");
        List<Album> albumList = albumDAO.getAll();
        for (Album album : albumList)
            System.out.println(album);

        ArtistDAO artistDAO = new ArtistDAO();
        System.out.println("Listing all artists:");
        List<Artist> artistList = artistDAO.getAll();
        for (Artist artist : artistList)
            System.out.println(artist);

        Graph albumGraph = GraphBuilder.numVertices(albumList.size()).buildGraph();
        for (int i = 0; i < albumList.size() - 1; i++)
            for (int j = i + 1; j < albumList.size(); j++) {
                if (albumList.get(i).checkAreRelated(albumList.get(j)))
                    albumGraph.addEdge(i, j);
            }
        MaximalIndependentSets mis = new MaximalIndependentSets(albumGraph, MAX_MIS);

        List<Set<Integer>> playlistList = mis.findAllMIS();
        Playlist playlist = new Playlist("First playlist");
        Set<Integer> playlistAlbums = playlistList.get(0);
        for(Integer i : playlistAlbums)
            playlist.addAlbum(albumList.get(i));
        System.out.println(playlist);
    }
}