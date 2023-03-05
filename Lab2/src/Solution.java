import java.util.*;

public class Solution {
    int N;
    boolean[] viz;

    class Edge {
        int y, cost;

        Edge(int y, int cost) {
            this.y = y;
            this.cost = cost;
        }
    }

    List<List<Edge>> graph;
    Collection<int[]> edges; // nodeA, nodeB, cost

    @Override
    public String toString() {
        return "Solution{" +
                "N=" + N +
                ", edges=" + edges +
                '}';
    }

    /**
     * Constructs the data structures necessary for the solution
     *
     * @param p -> problem
     */
    Solution(Problem p) {
        N = p.getN();
        graph = new ArrayList<>();
        for (int i = 0; i < N; i++)
            graph.add(new ArrayList<>());
        edges = p.getEdges();
        for (int[] it : edges) {
            assert (it.length == 2 || it.length == 3);
            graph.get(it[0]).add(new Edge(it[1], it[2]));
            graph.get(it[1]).add(new Edge(it[0], it[2]));
        }
        viz = new boolean[N];
    }


    /**
     * @return whether we can reach the last location from the first
     */
    boolean isReachable() {
        for (int i = 0; i < N; i++)
            viz[i] = false;
        dfsViz(0);
        return viz[N - 1];
    }

    void dfsViz(int node) {
        viz[node] = true;
        for (Edge it : graph.get(node))
            if (!viz[it.y]) {
                dfsViz(it.y);
            }
    }

    int findShortestRoute() {
        int[] d = new int[N];
        for (int i = 0; i < N; i++) {
            d[i] = Integer.MAX_VALUE;
            viz[i] = false;
        }

        PriorityQueue<Node> q = new PriorityQueue<>();
        d[0] = 0;
        q.add(new Node(0, 0));
        while (!q.isEmpty()) {
            int node = q.poll().index();
            viz[node] = true;
            for (Edge it : graph.get(node))
                if (!viz[it.y] && d[node] + it.cost < d[it.y]) {
                    d[it.y] = d[node] + it.cost;
                    q.add(new Node(it.y, d[it.y]));
                }
        }
        return d[N - 1];
    }

}
