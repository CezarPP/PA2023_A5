import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Network network = Network.getRandomNetwork(10, 15);
        ArrayList<Integer> articulationPoints = network.findCutPoints();
        System.out.println("Articulation points are ");
        for (int i : articulationPoints)
            System.out.println(i);
        ArrayList<ArrayList<Network.Pair>> cc = network.getBiconnectedComponents();
        System.out.println("The edges of the components are ");
        for (ArrayList<Network.Pair> v : cc)
            if (v.size() > 0) {
                System.out.println(v);
            }
    }
}