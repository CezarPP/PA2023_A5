package org.example;


class Map {
    final private int size;
    final private Cell[][] m;

    public Map(int size) {
        this.size = size;
        m = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(int row, int col) {
        return m[row][col];
    }

    public int getSize() {
        return size;
    }
}