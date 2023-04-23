package org.example;

import java.util.ArrayList;
import java.util.List;

class Cell implements NodeInterface {
    static final int[] dx = {1, -1, 0, 0};
    static final int[] dy = {0, 0, 1, -1};

    static Map map;
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

    public List<NodeInterface> getNeighbours() {
        List<NodeInterface> neighbours = new ArrayList<>();
        for (int index = 0; index < 4; index++) {
            if (!Cell.isInMatrix(row + dx[index], col + dy[index], map.getSize()))
                continue;
            neighbours.add(map.getCell(row + dx[index], col + dy[index]));
        }
        return neighbours;
    }
}
