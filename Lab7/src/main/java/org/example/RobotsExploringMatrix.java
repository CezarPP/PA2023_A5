package org.example;

import java.util.Random;

public class RobotsExploringMatrix {

    static final int SIZE_OF_MATRIX = 50;

    void run() {
        Supervisor supervisor = new Supervisor();
        Map map = new Map(SIZE_OF_MATRIX);
        Cell.map = map;
        SharedMemory sharedMemory = new SharedMemory(SIZE_OF_MATRIX);
        Random random = new Random();


        for (int i = 0; i < 10; i++)
            supervisor.addRobot(new Robot("Robot " + i,
                    map.getCell(random.nextInt(SIZE_OF_MATRIX), random.nextInt(SIZE_OF_MATRIX)), map.getSize(), sharedMemory));

        ExplorationStatus explorationStatus = new ExplorationStatus();
        TimeKeeper timeKeeper = new TimeKeeper(20000, explorationStatus); // 20 second time limit
        timeKeeper.setDaemon(true);
        timeKeeper.start();

        supervisor.startAll();

        new ExplorationCommandLine(explorationStatus, supervisor).run();

        supervisor.displayNumberOfPlacedTokensOnMap(map);

        System.exit(0);
    }
}
