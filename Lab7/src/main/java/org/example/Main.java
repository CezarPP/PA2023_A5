package org.example;

import java.io.IOException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    static final int SIZE_OF_MATRIX = 100;

    public static void main(String[] args) {

        Supervisor supervisor = new Supervisor();
        Map map = new Map(SIZE_OF_MATRIX);
        SharedMemory sharedMemory = new SharedMemory(SIZE_OF_MATRIX);
        Random random = new Random();

        ExplorationStatus explorationStatus = new ExplorationStatus();

        for (int i = 0; i < 10; i++)
            supervisor.addRobot(new Robot("Robot " + i, random.nextInt(SIZE_OF_MATRIX), random.nextInt(SIZE_OF_MATRIX), map, sharedMemory));
        TimeKeeper timeKeeper = new TimeKeeper(20000, explorationStatus); // 20 second time limit
        timeKeeper.setDaemon(true);
        timeKeeper.start();

        supervisor.startAll();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (!explorationStatus.isExplorationComplete()) {
            String command = "";
            System.out.println("Enter command (start, pause, start_specific, pause_specific, quit): ");
            try {
                command = reader.readLine();
            } catch (IOException e) {
                System.out.println("IO exception while reading command");
                System.exit(-1);
            }
            command = command.strip();
            switch (command) {
                case "start" -> {
                    supervisor.resumeAll();
                    System.out.println("Starting all");
                }
                case "pause" -> {
                    supervisor.pauseAll();
                    System.out.println("Pausing all");
                }
                case "start_specific" -> {
                    System.out.println("Enter robot index: ");
                    int startIndex = 0;
                    try {
                        startIndex = Integer.parseInt(reader.readLine());
                    } catch (IOException e) {
                        System.out.println("Error reading index to start");
                        System.exit(-1);
                    }
                    supervisor.resumeSpecific(startIndex);
                }
                case "pause_specific" -> {
                    System.out.println("Enter robot index: ");
                    int pauseIndex = 0;
                    try {
                        pauseIndex = Integer.parseInt(reader.readLine());
                    } catch (IOException e) {
                        System.out.println("Error reading index to pause");
                        System.exit(-1);
                    }
                    supervisor.pauseSpecific(pauseIndex);
                }
                case "quit" -> {
                    System.out.println("Quitting");
                    supervisor.pauseAll();
                    supervisor.displayNumberOfPlacedTokens(map);
                    System.exit(0);
                }
                default -> System.out.println("Invalid command");
            }
        }

        supervisor.displayNumberOfPlacedTokens(map);

        System.exit(0);
    }
}