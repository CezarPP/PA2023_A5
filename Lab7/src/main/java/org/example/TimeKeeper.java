package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class TimeKeeper extends Thread {
    private final long timeLimit;
    private final ExplorationStatus explorationStatus;

    public TimeKeeper(long timeLimit, ExplorationStatus explorationStatus) {
        this.timeLimit = timeLimit;
        this.explorationStatus = explorationStatus;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        try (PrintWriter writer = new PrintWriter(new FileWriter("time_keeper_output.txt"))) {
            while (System.currentTimeMillis() - startTime < timeLimit) {
                long currentTime = System.currentTimeMillis() - startTime;
                writer.printf("Elapsed time: %d ms%n", currentTime);
                writer.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("Time limit exceeded. Stopping exploration.");
            explorationStatus.setExplorationComplete(true);
        } catch (IOException e) {
            System.out.println("Error writing to time_keeper_output.txt");
            e.printStackTrace();
        }
    }
}
