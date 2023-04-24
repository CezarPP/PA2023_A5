package org.example;

import java.util.List;
// import java.util.Random;

class Robot implements Runnable {
    final private String name;
    final private int sizeOfMap;
    NodeInterface startNode;
    private final SharedMemory sharedMemory;

    // Random random = new Random();
    private volatile boolean paused = false;

    public Robot(String name, NodeInterface startNode, int sizeOfMap, SharedMemory sharedMemory) {
        this.name = name;
        this.startNode = startNode;
        this.sizeOfMap = sizeOfMap;
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
                List<Integer> tokens = sharedMemory.extractTokens(sizeOfMap);
                it.addTokens(tokens);
                dfs(it);
            }
        }
    }

    public void run() {
        if (startNode.visit(name)) {
            List<Integer> tokens = sharedMemory.extractTokens(sizeOfMap);
            startNode.addTokens(tokens);
            dfs(startNode);
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
