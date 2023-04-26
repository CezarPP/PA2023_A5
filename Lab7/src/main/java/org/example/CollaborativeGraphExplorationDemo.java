package org.example;

import org.graph4j.Graph;
import org.graph4j.generate.GraphGenerator;

public class CollaborativeGraphExplorationDemo {
    private static final int NUM_THREADS = Math.min(4, Runtime.getRuntime().availableProcessors() - 1);

    void run() {
        Graph graph = GraphGenerator.randomGnp(10000, 0.1);
        System.out.println("Running collaborative exploration on " + NUM_THREADS + " threads");
        long startTime = System.currentTimeMillis();
        new FastCollaborativeGraphExploration(graph).exploreGraph(NUM_THREADS);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("The algorithm ran in " + totalTime + " ms");
    }
}
