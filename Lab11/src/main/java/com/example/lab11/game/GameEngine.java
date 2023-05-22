package com.example.lab11.game;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private static GameEngine instance = null;
    private final List<Player> playerList;
    private final List<Game> gameList;

    private GameEngine() {
        playerList = new ArrayList<>();
        gameList = new ArrayList<>();
    }

    public static GameEngine getInstance() {
        if (instance == null)
            instance = new GameEngine();
        return instance;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Player findPlayerById(int id) {
        for (Player player : playerList)
            if (player.getId() == id)
                return player;
        return null;
    }

    public void deletePlayer(Player player) {
        playerList.remove(player);
    }

    public List<Game> getGameList() {
        return gameList;
    }
}
