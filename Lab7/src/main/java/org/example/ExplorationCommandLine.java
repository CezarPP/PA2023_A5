package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExplorationCommandLine {
    ExplorationStatus explorationStatus;
    Supervisor supervisor;

    ExplorationCommandLine(ExplorationStatus explorationStatus, Supervisor supervisor) {
        this.explorationStatus = explorationStatus;
        this.supervisor = supervisor;
    }

    void run() {
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
                    System.out.println("Resuming all");
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
                    return;
                }
                default -> System.out.println("Invalid command");
            }
        }
    }
}
