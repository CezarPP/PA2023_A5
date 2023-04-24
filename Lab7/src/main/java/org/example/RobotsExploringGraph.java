package org.example;

import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.generate.GraphGenerator;

import java.util.Random;

public class RobotsExploringGraph {

    static final int SIZE_OF_GRAPH = 50;
    static final int REAL_SIZE_OF_GRAPH = SIZE_OF_GRAPH * SIZE_OF_GRAPH;

    void run() {
        Supervisor supervisor = new Supervisor();
        Graph<NodeInterface, Edge> graph = GraphGenerator.randomGnp(REAL_SIZE_OF_GRAPH, 0.3);
        Node.graph = graph;
        for (int i = 0; i < REAL_SIZE_OF_GRAPH; i++)
            graph.setVertexLabel(i, new Node(i));

        SharedMemory sharedMemory = new SharedMemory(SIZE_OF_GRAPH);
        Random random = new Random();

        for (int i = 0; i < 10; i++)
            supervisor.addRobot(new Robot("Robot " + i,
                    graph.getVertexLabel(random.nextInt(REAL_SIZE_OF_GRAPH)), SIZE_OF_GRAPH, sharedMemory));

        ExplorationStatus explorationStatus = new ExplorationStatus();
        TimeKeeper timeKeeper = new TimeKeeper(20000, explorationStatus); // 20 second time limit
        timeKeeper.setDaemon(true);
        timeKeeper.start();

        supervisor.startAll();

        new ExplorationCommandLine(explorationStatus, supervisor).run();

        supervisor.displayNumberOfPlacedTokensOnGraph(graph);

        System.exit(0);
    }
}
