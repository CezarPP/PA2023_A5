package org.example;

import org.graph4j.Edge;
import org.graph4j.Graph;

import java.util.ArrayList;
import java.util.List;

public class Node implements NodeInterface {
    static Graph<NodeInterface, Edge> graph;
    private boolean visited;
    private final int index;
    final private List<Integer> tokens;
    String wasVisitedBy;

    public Node(int index) {
        this.index = index;
        this.visited = false;
        tokens = new ArrayList<>();
    }

    @Override
    public String getWasVisitedBy() {
        return wasVisitedBy;
    }

    @Override
    public synchronized boolean visit(String robotName) {
        if (!visited) {
            wasVisitedBy = robotName;
            visited = true;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void addTokens(List<Integer> newTokens) {
        tokens.addAll(newTokens);
    }

    @Override
    public List<Integer> getTokens() {
        return tokens;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }

    @Override
    public List<NodeInterface> getNeighbours() {
        List<NodeInterface> neighbours = new ArrayList<>();
        for (int it : graph.neighbors(index)) {
            neighbours.add(graph.getVertexLabel(it));
        }
        return neighbours;
    }
}
