package org.example.game;

public class Board {
    final int BOARD_SIZE = 15;
    private final int[][] board;

    public Board() {
        this.board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = -1;
    }

    public void submitMove(int x, int y, int player) {
        assert (isValidMove(x, y));
        board[x][y] = player;
    }

    public int[][] getBoard() {
        return this.board;
    }

    boolean isInBoard(int x, int y) {
        return (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE);
    }

    boolean isValidMove(int x, int y) {
        return (isInBoard(x, y) && board[x][y] == -1);
    }
}
