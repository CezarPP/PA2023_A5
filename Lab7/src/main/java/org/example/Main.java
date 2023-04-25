package org.example;

import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.alg.mst.KruskalMinimumSpanningTree;
import org.graph4j.generate.GraphGenerator;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        /* new RobotsExploringMatrix().run();
        new RobotsExploringGraph().run(); */
        Graph graph = GraphGenerator.randomGnp(20000, 0.1);
        long startTime1 = System.currentTimeMillis();
        double costLibrary = new KruskalMinimumSpanningTree(graph).getWeight();
        long endTime1 = System.currentTimeMillis();


        long startTime2 = System.currentTimeMillis();
        List<Edge> mstEdges = ConcurrentBoruvka.findMST(graph);
        double myCost = mstEdges.stream().mapToDouble(Edge::weight).sum();
        long endTime2 = System.currentTimeMillis();

        long elapsedTime1 = endTime1 - startTime1;
        long elapsedTime2 = endTime2 - startTime2;

        System.out.println("The cost is " + costLibrary + " and my cost is " + myCost);

        System.out.println("Execution time for library: " + elapsedTime1 + " ms");
        System.out.println("Execution time for my concurrent implementation: " + elapsedTime2 + " ms");
    }
}