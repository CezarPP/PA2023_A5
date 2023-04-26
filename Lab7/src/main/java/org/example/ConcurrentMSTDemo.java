package org.example;

import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.alg.mst.KruskalMinimumSpanningTree;
import org.graph4j.generate.EdgeWeightsGenerator;
import org.graph4j.generate.GraphGenerator;

import java.util.List;

public class ConcurrentMSTDemo {
    void run() {
        Graph graph = GraphGenerator.randomGnp(10000, 0.1);
        EdgeWeightsGenerator.randomIntegers(graph, 3, 100);
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
