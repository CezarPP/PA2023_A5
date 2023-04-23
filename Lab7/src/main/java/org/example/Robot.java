package org.example;

import java.util.List;
// import java.util.Random;

class Robot implements Runnable {

    static final int[] dx = {1, -1, 0, 0};
    static final int[] dy = {0, 0, 1, -1};
    final private String name;
    private final int row, col;
    private final Map map;
    private final SharedMemory sharedMemory;

    // Random random = new Random();
    private volatile boolean paused = false;

    public Robot(String name, int row, int col, Map map, SharedMemory sharedMemory) {
        this.name = name;
        this.row = row;
        this.col = col;
        this.map = map;
        this.sharedMemory = sharedMemory;
    }

    void dfs(Cell cell) {
        synchronized (this) {
            while (isPaused()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int row = cell.getRow();
        int col = cell.getCol();
        for (int index = 0; index < 4; index++) {
            if (!Cell.isInMatrix(row + dx[index], col + dy[index], map.getSize()))
                continue;
            Cell newCell = map.getCell(row + dx[index], col + dy[index]);
            if (newCell.visit(name)) {
                List<Integer> tokens = sharedMemory.extractTokens(map.getSize());
                newCell.addTokens(tokens);
                dfs(newCell);
            }
        }
    }

    public void run() {
        Cell startCell = map.getCell(row, col);
        if (startCell.visit(name)) {
            List<Integer> tokens = sharedMemory.extractTokens(map.getSize());
            startCell.addTokens(tokens);
            dfs(startCell);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
