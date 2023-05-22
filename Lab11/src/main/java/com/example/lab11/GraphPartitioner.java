package com.example.lab11;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.bitCount;

public class GraphPartitioner {
    private final static Random random = new Random();
    List<List<Integer>> v;
    int[] color;
    boolean isBipartiteVar;

    GraphPartitioner(List<List<Integer>> graph) {
        this.v = graph;
    }

    private void dfs(int node) {
        if (!isBipartiteVar)
            return;
        for (Integer i : v.get(node))
            if (color[i] == 0) {
                color[i] = (color[node] == 1) ? 2 : 1;
                dfs(i);
            } else if (color[i] == color[node]) {
                isBipartiteVar = false;
                break;
            }
    }

    private List<List<Integer>> vectorCopy(List<List<Integer>> a) {
        return a.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a graph is bipartite
     *
     * @return true if the graph is bipartite
     */
    private boolean isBipartite() {
        int N = v.size();
        isBipartiteVar = true;
        color = new int[N];
        for (int i = 0; i < N; i++)
            if (color[i] == 0)
                dfs(i);
        return isBipartiteVar;
    }

    /**
     * @param n number of vertices of arbitrary graph
     */
    public static List<List<Integer>> getArbitraryGraph(int n, double p) {
        List<List<Integer>> l = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            l.add(new ArrayList<>());
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (random.nextDouble() <= p) {
                    l.get(i).add(j);
                    l.get(j).add(i);
                }
        return l;
    }

    /**
     * Given a graph with N vertices, find the smallest set C, such that V \ C = {A, B}
     * s.t. there is no edge between A and B and max(|A|, |B|) < 2n / 3
     *
     * @return C as a list
     */
    List<Integer> solve() {
        int N = v.size();
        List<Integer> C = new ArrayList<>();
        for (int i = 0; i < N; i++)
            C.add(i);
        final int cnfMax = (1 << N);
        for (int cnf = 0; cnf < cnfMax; cnf++) {
            if (bitCount(cnf) >= C.size())
                continue;
            List<List<Integer>> auxV = vectorCopy(v);
            Set<Integer> toRemove = new TreeSet<>();
            for (int i = 0; i < N; i++)
                if ((cnf & (1 << i)) != 0) {
                    toRemove.add(i);
                }
            for (int i = 0; i < N; i++)
                if (toRemove.contains(i))
                    v.get(i).clear();
                else {
                    v.get(i).removeAll(toRemove);
                }
            if (isBipartite()) {
                C.clear();
                C.addAll(toRemove);
            }
            v = vectorCopy(auxV);
        }

        return C;
    }
}
