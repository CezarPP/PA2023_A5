package org.example;

import java.util.List;

public interface NodeInterface {
    List<NodeInterface> getNeighbours();

    boolean isVisited();

    boolean visit(String name);

    String getWasVisitedBy();

    List<Integer> getTokens();

    void addTokens(List<Integer> newTokens);
}
