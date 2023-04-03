package org.example;

import java.util.ArrayList;
import java.util.List;

class Cell {
    final private int row;
    final private int col;
    private boolean visited;
    private boolean containsRobot;
    final private List<Integer> tokens;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.tokens = new ArrayList<>();
        containsRobot = false;
    }

    public synchronized boolean visit() {
        if (!visited && !containsRobot) {
            visited = containsRobot = true;
            return true;
        }
        return false;
    }

    public void leaveCell() {
        containsRobot = false;
    }

    public synchronized void addTokens(List<Integer> newTokens) {
        tokens.addAll(newTokens);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    boolean isInMatrix(int size) {
        return (row >= 0 && row < size && col >= 0 && col < size);
    }

    public List<Integer> getTokens() {
        return tokens;
    }

    // DO NOT USE IN THREAD CONTEXT
    public boolean isVisited() {
        return visited;
    }
}
