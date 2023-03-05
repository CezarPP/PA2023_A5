import java.util.*;

import static java.util.concurrent.ThreadLocalRandom.current;

public class Problem {
    // the number of locations and the number of roads
    int n, m;
    Location[] locations;
    HashMap<Road, int[]> roads;

    @Override
    public String toString() {
        return "Problem{" +
                "n=" + n +
                ", m=" + m +
                ", locations=" + Arrays.toString(locations) +
                ", roads=" + roads +
                '}';
    }

    void addRoad(Road r, int[] indices) {
        if (!roadExists(r))
            roads.put(r, indices);
    }

    boolean roadExists(Road r) {
        return roads.containsKey(r);
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Location getLocation(int index) {
        if (index >= n) {
            System.exit(-1);
        }
        return locations[index];
    }

    public Collection<int[]> getRoadIndices() {
        return roads.values();
    }

    Problem(int n, int m) {
        locations = new Location[n];
        roads = new HashMap<>();
        this.n = n;
        this.m = m;
    }

    public HashMap<Road, int[]> getRoads() {
        return roads;
    }

    public List<int[]> getEdges() {
        ArrayList<int[]> edges = new ArrayList<>();
        roads.forEach((road, indices) -> {
            edges.add(new int[]{indices[0], indices[1], road.length});
        });
        return edges;
    }

    public Location[] getLocations() {
        return locations;
    }

    Problem(Problem p) {
        locations = p.getLocations();
        roads = p.getRoads();
        this.n = p.getN();
        this.m = p.getM();
    }

    boolean isValidProblem() {
        if (n <= 0 || m < 0)
            return false;
        HashSet<Location> h = new HashSet<>(); // check for locations presents twice
        for (Location location : locations) {
            if (h.contains(location))
                return false;
            h.add(location);
        }
        return true;
    }

    static int getRandomIndex(int indexMax) {
        return current().nextInt(0, indexMax);
    }

    static int[] getPairOfDifferentIndices(int indexMax) {
        int x = getRandomIndex(indexMax), y = getRandomIndex(indexMax);
        while (x == y) {
            x = getRandomIndex(indexMax);
            y = getRandomIndex(indexMax);
        }
        return new int[]{x, y};
    }

    static Problem getRandomProblem(int cntLocations, int cntRoads) {
        Problem p = new Problem(cntLocations, cntRoads);
        for (int i = 0; i < cntLocations; i++) {
            p.locations[i] = Location.getRandomLocation();
        }

        for (int i = 0; i < cntRoads; i++) {
            Road toAdd;
            int[] indices;
            do {
                indices = getPairOfDifferentIndices(cntLocations);
                toAdd = Road.getRandomRoad(p.locations[indices[0]], p.locations[indices[1]]);
            } while (p.roadExists(toAdd));
            p.addRoad(toAdd, indices);
        }
        return p;
    }
}
