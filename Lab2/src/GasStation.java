import java.util.Random;

public class GasStation extends Location {
    int gasPrice;
    static final int MAX_GAS_PRICE = 10;
    static final Random random = new Random();

    static int getRandomGasPrice() {
        return Math.abs(random.nextInt()) % MAX_GAS_PRICE;
    }

    GasStation(String name, LocationType type, int x, int y, int gasPrice) {
        super(name, type, x, y);
        this.gasPrice = gasPrice;
    }

    public void setGasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
    }

    public int getGasPrice() {
        return gasPrice;
    }

    @Override
    public String toString() {
        return "GasStation{" +
                "gasPrice=" + gasPrice +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", maxCoordinate=" + maxCoordinate +
                '}';
    }
}
