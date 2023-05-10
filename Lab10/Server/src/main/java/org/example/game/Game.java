package org.example.game;

import org.example.server.ClientThread;

import java.util.Random;

public class Game implements TimerObserver {
    static int gameId = 0;
    static final Random random = new Random();
    private final Board board;
    private final Player[] players;
    private final ClientThread[] clientThreads;
    private int currentPlayer;
    private final GameTimer[] timers;

    public static synchronized int getNewId() {
        gameId++;
        return gameId;
    }

    public Game(Player p1, Player p2, int timerSeconds, ClientThread cl) {
        this.board = new Board();
        this.players = new Player[]{p1, p2};
        this.currentPlayer = 0;
        this.timers = new GameTimer[]{new GameTimer(timerSeconds, p1, this),
                new GameTimer(timerSeconds, p2, this)};
        this.clientThreads = new ClientThread[]{cl, null};
    }

    public void join(ClientThread clientThread) {
        assert (clientThreads[1] == null);
        clientThreads[1] = clientThread;
    }

    public void startGame() {
        timers[currentPlayer].start();
        boolean randStart = random.nextBoolean();
        if (randStart)
            currentPlayer = 0;
        else
            currentPlayer = 1;
        clientThreads[0].notifyGameStart(randStart);
        clientThreads[1].notifyGameStart(!randStart);
    }

    public boolean submitMove(int x, int y, ClientThread clientThread) {
        // verify it's your turn and the validity of the move
        if (clientThreads[currentPlayer] != clientThread || !board.isValidMove(x, y)) {
            return false;
        }

        timers[currentPlayer].stop();

        board.submitMove(x, y, players[currentPlayer].getPlayerNumber());

        if (checkWinner(x, y)) {
            stopTimers();
            clientThread.notifyWinner(clientThread == clientThreads[currentPlayer], false);
            getOtherThread(clientThread).notifyWinner(clientThread != clientThreads[currentPlayer], false);
            return true;
        }

        currentPlayer = (currentPlayer + 1) % 2;
        timers[currentPlayer].start();
        getOtherThread(clientThread).notifyMoveMade(x, y);

        return true;
    }

    public boolean checkWinner(int lastMoveX, int lastMoveY) {
        int playerNumber = board.getBoard()[lastMoveX][lastMoveY];

        if (checkLine(playerNumber, lastMoveX, lastMoveY, 1, 0)) {
            return true;
        }

        if (checkLine(playerNumber, lastMoveX, lastMoveY, 0, 1)) {
            return true;
        }

        // Check diagonal (/)
        if (checkLine(playerNumber, lastMoveX, lastMoveY, 1, -1)) {
            return true;
        }

        // Check diagonal (\)
        return checkLine(playerNumber, lastMoveX, lastMoveY, 1, 1);
    }

    private boolean checkLine(int playerNumber, int startX, int startY, int dx, int dy) {
        int count = getCountInDirection(playerNumber, startX, startY, dx, dy, 1) +
                getCountInDirection(playerNumber, startX, startY, dx, dy, -1) - 1;
        return (count >= 5);
    }

    int getCountInDirection(int playerNumber, int startX, int startY, int dx, int dy, int sign) {
        int count = 0;
        for (int i = 0; i < board.BOARD_SIZE; i++) {
            int x = startX + sign * dx * i;
            int y = startY + sign * dy * i;
            if (!board.isInBoard(x, y)) {
                break;
            }
            if (board.getBoard()[x][y] == playerNumber) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    public void leaveGame(ClientThread clientThread) {
        stopTimers();
        ClientThread opponent = getOtherThread(clientThread);
        opponent.notifyOpponentLeft();
    }

    public ClientThread getOtherThread(ClientThread clientThread) {
        if (clientThread == clientThreads[0])
            return clientThreads[1];
        return clientThreads[0];
    }

    private void stopTimers() {
        timers[0].stop();
        timers[1].stop();
    }

    @Override
    public void timeUp(Player player) {
        int winner = (player.getPlayerNumber() + 1) % 2;
        int looser = player.getPlayerNumber();
        clientThreads[winner].notifyWinner(true, true);
        clientThreads[looser].notifyWinner(false, true);
    }
}

