package org.example;

import org.graph4j.Graph;

import java.util.*;

public class MaximalIndependentSets {
    private final List<Set<Integer>> allMIS;
    private Set<Integer> visited;
    private final Set<Integer> currentMIS;
    private final Graph graph;
    private final int maxMIS;

    public MaximalIndependentSets(Graph graph, int maxMIS) {
        this.graph = graph;
        this.allMIS = new ArrayList<>();
        this.visited = new HashSet<>();
        this.currentMIS = new HashSet<>();
        this.maxMIS = maxMIS;
    }

    public List<Set<Integer>> findAllMIS() {
        findAllMIS(0);
        return allMIS;
    }

    private void findAllMIS(int index) {
        if (allMIS.size() >= maxMIS)
            return;
        if (index >= graph.numVertices()) {
            allMIS.add(new HashSet<>(currentMIS));
            return;
        }

        if (!visited.contains(index)) {
            visited.add(index);

            Set<Integer> neighbors = new HashSet<>();
            for (int i = 0; i < graph.neighbors(index).length; i++)
                neighbors.add(graph.neighbors(index)[i]);
            Set<Integer> temp = new HashSet<>(visited);
            visited.addAll(neighbors);

            currentMIS.add(index);
            findAllMIS(index + 1);
            currentMIS.remove(index);

            visited = temp;
        }

        findAllMIS(index + 1);
    }
}
