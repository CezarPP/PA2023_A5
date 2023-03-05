record Node(int index, int cost) implements Comparable<Node> {

    @Override
    public int compareTo(Node o) {
        if (cost < o.cost)
            return 1;
        else if (cost == o.cost)
            return 0;
        return -1;
    }
}