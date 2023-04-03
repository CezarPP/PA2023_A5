package org.example;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Main {
    static final int SIZE_OF_MATRIX = 10;

    public static void main(String[] args) {

        Supervisor supervisor = new Supervisor();
        Map map = new Map(SIZE_OF_MATRIX);
        SharedMemory sharedMemory = new SharedMemory(SIZE_OF_MATRIX);
        Random random = new Random();
        for (int i = 0; i < 10; i++)
            supervisor.addRobot(new Robot("", random.nextInt(SIZE_OF_MATRIX), random.nextInt(SIZE_OF_MATRIX), map, sharedMemory));
        supervisor.startAll();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        supervisor.pauseAll();
        for (int i = 0; i < SIZE_OF_MATRIX; i++) {
            for (int j = 0; j < SIZE_OF_MATRIX; j++)
                System.out.print(map.getCell(i, j).isVisited() + " ");
            System.out.println();
        }
    }
}