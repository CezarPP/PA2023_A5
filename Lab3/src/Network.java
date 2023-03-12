import java.util.*;

import static java.lang.Math.min;

public class Network {
    int n;
    List<Node> nodes;
    List<TreeSet<Integer>> graph;
    HashMap<String, Integer> nameToIndex;
    static final Random random = new Random();

    // variables for articulation points
    boolean[] visited;
    int[] tin, low;
    int timer = 0;
    TreeSet<Integer> articulationPoints;

    public static class Pair {
        int x, y;

        @Override
        public String toString() {
            return "Pair{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        Pair(int _x, int _y) {
            x = _x;
            y = _y;
        }
    }

    Stack<Pair> s;
    ArrayList<ArrayList<Pair>> cc;
    int cntCC = 0;

    Network(int networkSize) {
        n = networkSize;
        nodes = new ArrayList<>();
        graph = new ArrayList<>();
        for (int i = 0; i < networkSize; i++)
            graph.add(new TreeSet<>());
        nameToIndex = new HashMap<>();
    }

    /**
     * inserts node into network
     *
     * @param node -> Person, Company, Programmer etc
     * @return true if the node has been inserted, false otherwise
     */
    public boolean addNode(Node node) {
        if (nameToIndex.containsKey(node.getName()))
            return false;
        nodes.add(node);
        nameToIndex.put(node.getName(), nodes.size() - 1);
        return true;
    }

    public void addConnection(int index1, int index2) {
        assert (nodes.get(index1) instanceof Person || nodes.get(index2) instanceof Person);
        if (nodes.get(index1) instanceof Person p) {
            if (!p.addConnection(nodes.get(index2).getName()))
                return;
        }
        if (nodes.get(index2) instanceof Person p) {
            if (!p.addConnection(nodes.get(index1).getName()))
                return;
        }
        graph.get(index1).add(index2);
        graph.get(index2).add(index1);
    }

    static public int getRandomIndex(int maxIndex) {
        return Math.abs(random.nextInt()) % maxIndex;
    }

    /**
     * Generates a random network of size networkSize
     *
     * @param networkSize   the size of the returned network
     * @param noConnections the number of connections to be added to said network
     * @return returns the network
     */
    static public Network getRandomNetwork(int networkSize, int noConnections) {
        Network network = new Network(networkSize);
        // at least one person
        while (!network.addNode(Person.getRandomPerson())) ;
        for (int i = 1; i < networkSize; i++) {
            if (Math.abs(random.nextInt()) % 4 == 0)
                while (!network.addNode(Company.getRandomCompany())) ;
            else
                while (!network.addNode(Person.getRandomPerson())) ;
        }
        for (int i = 0; i < noConnections; i++) {
            int i1 = getRandomIndex(networkSize), i2 = getRandomIndex(networkSize);
            while (i1 == i2 || network.nodes.get(i1) instanceof Company &&
                    network.nodes.get(i2) instanceof Company) {
                i1 = getRandomIndex(networkSize);
                i2 = getRandomIndex(networkSize);
            }
            network.addConnection(i1, i2);
        }
        return network;
    }

    int getImportanceOfNode(int index) {
        return graph.get(index).size();
    }

    void printNodes() {
        System.out.println("The network has " + nodes.size() + " nodes");
        for (Node node : nodes)
            System.out.println(node.getName());
    }

    void printNetwork() {
        System.out.println("Nodes printed in increasing order of importance");
        List<Node> nodeList = new ArrayList<>(nodes);
        nodeList.sort((lhs, rhs) ->
                getImportanceOfNode(nameToIndex.get(lhs.getName()))
                        - getImportanceOfNode(nameToIndex.get(rhs.getName())));
        for (Node node : nodeList)
            System.out.println(node.getName());
    }

    void dfs(int node, int p) {
        visited[node] = true;
        tin[node] = low[node] = ++timer;
        int children = 0;
        for (int it : graph.get(node)) {
            if (it == p) continue;
            if (visited[it]) {
                low[node] = min(low[node], tin[it]);
                if (tin[it] < tin[node])
                    s.add(new Pair(node, it));
            } else {
                ++children;
                s.add(new Pair(node, it));
                dfs(it, node);
                low[node] = min(low[node], low[it]);
                if ((tin[node] == 1 && children > 1) ||
                        (tin[node] > 1 && low[it] >= tin[node])) {
                    // is articulation point
                    articulationPoints.add(node);
                    ++cntCC;

                    while (s.peek().x != node || s.peek().y != it) {
                        cc.get(cntCC).add(s.pop());
                    }
                    cc.get(cntCC).add(s.pop());
                }
            }
        }
        if (tin[node] == 1 && !s.empty()) {
            // root node
            cntCC++;
            while (!s.empty()) {
                cc.get(cntCC).add(s.pop());
            }
        }
    }

    ArrayList<Integer> findCutPoints() {
        articulationPoints = new TreeSet<>();
        visited = new boolean[n];
        tin = new int[n];
        low = new int[n];
        cc = new ArrayList<>();
        s = new Stack<>();
        for (int i = 0; i < n; i++) {
            visited[i] = false;
            tin[i] = low[i] = -1;
            cc.add(new ArrayList<>());
        }
        for (int i = 0; i < n; ++i) {
            if (!visited[i]) {
                timer = 0;
                dfs(i, -1);
            }
        }
        return new ArrayList<>(articulationPoints);
    }

    ArrayList<ArrayList<Pair>> getBiconnectedComponents() {
        return cc;
    }
}
