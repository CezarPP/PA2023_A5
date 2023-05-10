package org.example.game;

import java.util.concurrent.ConcurrentHashMap;

public class GameList {
    ConcurrentHashMap<Integer, Game> gameMap;

    public GameList() {
        gameMap = new ConcurrentHashMap<>();
    }

    public void addGame(int gameId, Game game) {
        gameMap.put(gameId, game);
    }

    public Game getGame(int gameId) {
        return gameMap.get(gameId);
    }

    public void removeGame(int gameId) {
        gameMap.remove(gameId);
    }

}

