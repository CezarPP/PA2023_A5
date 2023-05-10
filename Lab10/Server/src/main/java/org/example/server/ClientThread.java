package org.example.server;

import org.example.game.Game;
import org.example.game.GameList;
import org.example.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientThread extends Thread {
    private final Socket _socket;
    private final GameList gameList;
    Game game;
    int gameId;
    final int GAME_LENGTH_SECONDS = 300;

    public ClientThread(Socket socket, GameList gameList) {
        this._socket = socket;
        this.gameList = gameList;
        this.game = null;
        gameId = 0;
    }

    public void run() {
        try (Socket socket = _socket; BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()))) {
            boolean stopReached = false;
            while (!stopReached) {
                String request = in.readLine();
                if (request == null) {
                    this.interrupt();
                }
                assert request != null;
                request = request.toLowerCase();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                String response;
                if (Objects.equals(request, "stop") || Objects.equals(request, "exit")) {
                    response = "Server stopped";
                    if (game != null) {
                        game.leaveGame(this);
                        gameList.removeGame(gameId);
                    }
                    stopReached = true;
                } else if (request.startsWith("create game")) {
                    response = handleCreateGameRequest();
                } else if (request.startsWith("join game")) {
                    response = handleJoinGameRequest(request);
                } else if (request.startsWith("submit move")) {
                    response = handleSubmitMoveRequest(request);
                } else {
                    response = "Invalid request " + request;
                }
                out.println(response);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void notifyGameStart(boolean isFirst) {
        try {
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            out.println("The game has started and " + ((isFirst) ? "you are first" : "you are second"));
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void notifyMoveMade(int x, int y) {
        try {
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            out.println("Opponent moved to " + x + " " + y);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void notifyWinner(boolean youAreWinner, boolean wonByTimeout) {
        try {
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            out.println("The game has ended and " + ((youAreWinner) ? "you won" : "you lost") + ((wonByTimeout) ? " by timeout" : ""));
            out.flush();
            exitGame();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void notifyOpponentLeft() {
        try {
            PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            out.println("Your opponent left, you won");
            out.flush();
            exitGame();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    void exitGame() {
        if (game != null) {
            gameList.removeGame(gameId);
        }
        game = null;
        gameId = 0;
    }

    /**
     * @param request -> client request
     * @return -> response
     */
    private String handleSubmitMoveRequest(String request) {
        try {
            if (request.split("\\s+").length < 4) throw new NumberFormatException();
            int x = Integer.parseInt(request.split("\\s+")[2]);
            int y = Integer.parseInt(request.split("\\s+")[3]);
            if (game == null) {
                return "You are not in a game";
            } else {
                boolean success = game.submitMove(x, y, this);
                if (!success) return "Invalid move, either not your turn or not a valid move";
                return "Piece has been moved";
            }
        } catch (NumberFormatException e) {
            return "Please provide 2 numbers describing the positions on the board";
        }
    }

    /**
     * @return -> response
     */
    private String handleCreateGameRequest() {
        if (game == null) {
            gameId = Game.getNewId();
            game = new Game(new Player(0), new Player(1), GAME_LENGTH_SECONDS, this);
            gameList.addGame(gameId, game);
            return "New game created with id " + gameId;
        }
        return "You are already in a game";
    }

    /**
     * @param request -> client request
     * @return -> response
     */
    private String handleJoinGameRequest(String request) {
        try {
            if (request.split("\\s+").length < 3) throw new NumberFormatException();
            int gameId = Integer.parseInt(request.split("\\s+")[2]);
            if (game == null) {
                game = gameList.getGame(gameId);
                if (game == null) {
                    return "Not a valid game number";
                }
                game.join(this);
                game.startGame();
                return "Joining game and starting";
            }
            return "You are already in a game";
        } catch (NumberFormatException e) {
            return "Not a valid game number";
        }
    }
}