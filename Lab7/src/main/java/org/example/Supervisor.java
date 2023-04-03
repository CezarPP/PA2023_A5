package org.example;

import java.util.ArrayList;
import java.util.List;

class Supervisor {
    private final List<Thread> robots;

    public Supervisor() {
        robots = new ArrayList<>();
    }

    public void addRobot(Robot robot) {
        robots.add(new Thread(robot));
    }

    public void startAll() {
        for (Thread robot : robots) {
            robot.start();
        }
    }

    public void pauseAll() {
        for (Thread robot : robots) {
            robot.interrupt();
        }
    }
}
