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
        System.out.println("Running concurrent on " + NUM_THREADS + " threads");
        int N = graph.vertices().length;

        int[] components = new int[N];
        for (int i = 0; i < N; i++) {
            components[i] = i;
        }

        List<List<Integer>> componentLists = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            List<Integer> initialCompForNode = new ArrayList<>();
            initialCompForNode.add(i);
            componentLists.add(initialCompForNode);
        }


        List<Edge> mstEdges = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        while (true) {
            List<Future<Edge>> futuresList = new ArrayList<>();

            int cntNotEmpty = 0;
            for (int i = 0; i < componentLists.size(); i++)
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
        int srcComponent = components[edge.source()];
        int destComponent = components[edge.target()];
        if (srcComponent == destComponent)
            return false;
        if (componentLists.get(srcComponent).size() > componentLists.get(destComponent).size()) {
            for (Integer node : componentLists.get(destComponent)) {
                components[node] = srcComponent;
            }
            componentLists.get(srcComponent).addAll(componentLists.get(destComponent));
            componentLists.get(destComponent).clear();
        } else {
            for (Integer node : componentLists.get(srcComponent)) {
                components[node] = destComponent;
            }
            componentLists.get(destComponent).addAll(componentLists.get(srcComponent));
            componentLists.get(srcComponent).clear();
        }
        return true;
    }

    private static Edge findMinEdge(Graph graph, List<Integer> componentNodes, int[] components) {
        Edge minEdge = null;
        for (Integer nodeIndex : componentNodes) {
            int srcComponent = components[nodeIndex];
            for (Edge edge : graph.edgesOf(nodeIndex)) {
                assert (edge.source() == srcComponent);
                int destComponent = components[edge.target()];
                if (srcComponent != destComponent && (minEdge == null || edge.weight() < minEdge.weight())) {
                    minEdge = edge;
                }
            }
        }

        return minEdge;
    }
}
