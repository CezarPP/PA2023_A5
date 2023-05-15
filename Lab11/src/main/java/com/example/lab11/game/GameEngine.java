package com.example.lab11.game;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private static GameEngine instance = null;
    private List<Player> playerList;

    private GameEngine() {
        playerList = new ArrayList<>();
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
}
