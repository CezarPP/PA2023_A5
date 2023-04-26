package org.example;

import org.graph4j.Edge;
import org.graph4j.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentBoruvka {
    private static final int NUM_THREADS = Math.min(4, Runtime.getRuntime().availableProcessors() - 1);

    public static List<Edge> findMST(Graph graph) {
        int N = graph.vertices().length;
        System.out.println("Running concurrent on " + NUM_THREADS + " threads for a graph of " + N + " nodes");

        int[] components = new int[N];
        for (int i = 0; i < N; i++) {
            components[i] = i;
        }

        List<List<Integer>> componentLists = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            componentLists.add(new ArrayList<>(List.of(i)));
        }

        List<Edge> mstEdges = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        while (true) {
            List<Future<Edge>> futuresList = new ArrayList<>();

            int cntNotEmpty = 0;
            for (int i = 0; i < N; i++)
                if (!componentLists.get(i).isEmpty()) {
                    cntNotEmpty++;
                    List<Integer> componentNodes = new ArrayList<>(componentLists.get(i));
                    futuresList.add(executor.submit(() -> findMinEdge(graph, componentNodes, components)));
                }
            if (cntNotEmpty == 1)
                break;

            for (Future<Edge> future : futuresList) {
                try {
                    Edge minEdge = future.get();
                    if (mergeComponents(componentLists, components, minEdge)) {
                        mstEdges.add(minEdge);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.out.println("Exception while getting future of edge: " + e.getMessage());
                }
            }
        }

        executor.shutdown();

        return mstEdges;
    }

    private static boolean mergeComponents(List<List<Integer>> componentLists, int[] components, Edge edge) {
        if (edge == null)
            return false;
        int src = components[edge.source()];
        int dest = components[edge.target()];
        List<Integer> srcComponent = componentLists.get(src);
        List<Integer> destComponent = componentLists.get(dest);
        if (src == dest)
            return false;
        if (srcComponent.size() > destComponent.size()) {
            for (Integer node : destComponent) {
                components[node] = src;
            }
            srcComponent.addAll(destComponent);
            destComponent.clear();
        } else {
            for (Integer node : srcComponent) {
                components[node] = dest;
            }
            destComponent.addAll(srcComponent);
            srcComponent.clear();
        }
        return true;
    }

    private static Edge findMinEdge(Graph graph, List<Integer> componentNodes, int[] components) {
        Edge minEdge = null;
        for (Integer node : componentNodes) {
            for (Edge edge : graph.edgesOf(node)) {
                if (components[edge.source()] != components[edge.target()] &&
                        (minEdge == null || edge.weight() < minEdge.weight())) {
                    minEdge = edge;
                }
            }
        }

        return minEdge;
    }
}
