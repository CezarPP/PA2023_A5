public class Main {
    public static void main(String[] args) {
        Network network = Network.getRandomNetwork(10, 15);
        network.printNodes();
        network.printNetwork();
    }
}