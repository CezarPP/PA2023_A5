package org.example;

import java.util.List;
// import java.util.Random;

class Robot implements Runnable {
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

    void dfs(NodeInterface node) {
        synchronized (this) {
            while (isPaused()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        for (NodeInterface it : node.getNeighbours()) {
            if (it.visit(name)) {
                List<Integer> tokens = sharedMemory.extractTokens(map.getSize());
                it.addTokens(tokens);
                dfs(it);
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
