package org.example;

import org.graph4j.Edge;
import org.graph4j.Graph;

import java.util.ArrayList;
import java.util.List;

class Supervisor {
    private final List<Robot> robots;
    private final List<Thread> robotThreads;

    public Supervisor() {
        this.robots = new ArrayList<>();
        this.robotThreads = new ArrayList<>();
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
        Thread robotThread = new Thread(robot, robot.getName());
        robotThreads.add(robotThread);
    }

    public void startAll() {
        for (Thread robotThread : robotThreads) {
            robotThread.start();
        }
    }

    public void pauseAll() {
        for (Robot robot : robots) {
            robot.setPaused(true);
        }
    }

    public void resumeSpecific(int index) {
        Thread robotThread = robotThreads.get(index);
        Robot robot = robots.get(index);

        robot.setPaused(false);
        synchronized (robotThread) {
            robotThread.notify();
        }
    }

    public void resumeAll() {
        for (Robot robot : robots) {
            Thread robotThread = robotThreads.get(robots.indexOf(robot));
            robot.setPaused(false);
            synchronized (robotThread) {
                robotThread.notify();
            }
        }
    }

    public void pauseSpecific(int index) {
        Robot robot = robots.get(index);
        robot.setPaused(true);
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public void displayNumberOfPlacedTokensOnMap(Map map) {
        int[] tokenCounts = new int[getRobots().size()];
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                Cell cell = map.getCell(i, j);
                if (cell.isVisited()) {
                    for (Robot robot : getRobots()) {
                        if (robot.getName().equals(cell.getWasVisitedBy())) {
                            int index = getRobots().indexOf(robot);
                            tokenCounts[index] += cell.getTokens().size();
                        }
                    }
                }
            }
        }
        printCounts(tokenCounts);
    }

    public void displayNumberOfPlacedTokensOnGraph(Graph<NodeInterface, Edge> graph) {
        int[] tokenCounts = new int[getRobots().size()];
        for (int i : graph.vertices()) {
            NodeInterface node = graph.getVertexLabel(i);
            if (node.isVisited()) {
                for (Robot robot : getRobots()) {
                    if (robot.getName().equals(node.getWasVisitedBy())) {
                        int index = getRobots().indexOf(robot);
                        tokenCounts[index] += node.getTokens().size();
                    }
                }
            }
        }
        printCounts(tokenCounts);
    }

    public void printCounts(int[] tokenCounts) {
        int total = 0;
        for (int i = 0; i < tokenCounts.length; i++) {
            System.out.println(getRobots().get(i).getName() + " placed " + tokenCounts[i] + " tokens.");
            total += tokenCounts[i];
        }
        System.out.println("Total is " + total);
    }
}
