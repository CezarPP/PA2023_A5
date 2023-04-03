package org.example;

import java.util.List;
import java.util.Random;

class Robot implements Runnable {

    static final int[] dx = {1, -1, 0, 0};
    static final int[] dy = {0, 0, 1, -1};
    final private String name;
    private int row;
    private int col;
    private final Map map;
    private final SharedMemory sharedMemory;
    Random random = new Random();

    public Robot(String name, int row, int col, Map map, SharedMemory sharedMemory) {
        this.name = name;
        this.row = row;
        this.col = col;
        this.map = map;
        this.sharedMemory = sharedMemory;
    }

    public void run() {
        int size = map.getSize();
        while (true) {
            int indexOfMove = random.nextInt(4);
            Cell cell = new Cell(row + dx[indexOfMove], col + dy[indexOfMove]);
            if (cell.isInMatrix(size)) {
                Cell newCell = map.getCell(cell.getRow(), cell.getCol());
                if (newCell.visit()) {
                    List<Integer> tokens = sharedMemory.extractTokens(size);
                    newCell.addTokens(tokens);
                }
                row = cell.getRow();
                col = cell.getCol();
            }
        }
    }

    public String getName() {
        return name;
    }
}
