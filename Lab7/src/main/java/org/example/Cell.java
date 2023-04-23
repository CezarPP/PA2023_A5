package org.example;

import java.util.ArrayList;
import java.util.List;

class Cell {
    final private int row, col;
    private boolean visited;
    final private List<Integer> tokens;
    String wasVisitedBy;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.tokens = new ArrayList<>();
    }

    public String getWasVisitedBy() {
        return wasVisitedBy;
    }

    public synchronized boolean visit(String robotName) {
        if (!visited) {
            wasVisitedBy = robotName;
            visited = true;
            return true;
        }
        return false;
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

    static boolean isInMatrix(int row, int col, int size) {
        return (row >= 0 && row < size && col >= 0 && col < size);
    }

    public List<Integer> getTokens() {
        return tokens;
    }

    public boolean isVisited() {
        return visited;
    }
}
