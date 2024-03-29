package org.example;

import org.chocosolver.solver.Model;
import org.example.DAOs.AlbumDAO;
import org.example.DAOs.ArtistDAO;
import org.example.DAOs.DAO;
import org.example.bonus.MaximalIndependentSets;
import org.example.bonus.Playlist;
import org.example.classes.Album;
import org.example.classes.Artist;
import org.example.entities.AlbumsEntity;
import org.example.entities.ArtistsEntity;
import org.example.factory.AbstractFactory;
import org.example.misc.AlbumConstraintSolver;
import org.example.misc.FakeDataGenerator;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.example.factory.DAOTypes;

import java.util.List;
import java.util.Set;

public class Main {
    static final int MAX_MIS = 10;

    public static void main(String[] args) {
        // new AlbumImporter("/album_list.csv").importData();

        // LAB8
/*        AlbumDAO albumDAO = new AlbumDAO();
        System.out.println("Listing all albums:");
        List<AlbumsEntity> albumList = albumDAO.getAll();
        for (AlbumsEntity album : albumList)
            System.out.println(album);

        ArtistDAO artistDAO = new ArtistDAO();
        System.out.println("Listing all artists:");
        List<ArtistsEntity> artistList = artistDAO.getAll();
        for (ArtistsEntity artist : artistList)
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
        for (Integer i : playlistAlbums)
            playlist.addAlbum(albumList.get(i));
        System.out.println(playlist);*/


        // LAB9
        // FakeDataGenerator.insertRandomArtistsAndAlbums();

        DAOTypes daoType = DAOTypes.JPA;
        AbstractFactory factory = AbstractFactory.getDAOFactory(daoType);
        DAO<AlbumsEntity> albumDAO = factory.getAlbumDAO();

        List<AlbumsEntity> albums = albumDAO.getAll();
        AlbumConstraintSolver.findAlbums(albums, 10, 10);
    }
}