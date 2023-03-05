import java.util.Random;

public class Airport extends Location {
    int noTerminals;
    static final Random random = new Random();
    static final int MAX_NO_TERMINALS = 10;

    static int getRandomNoTerminals() {
        return Math.abs(random.nextInt()) % MAX_NO_TERMINALS;
    }

    Airport(String name, LocationType type, int x, int y, int noTerminals) {
        super(name, type, x, y);
        this.noTerminals = noTerminals;
    }

    public void setNoTerminals(int noTerminals) {
        this.noTerminals = noTerminals;
    }

    public int getNoTerminals() {
        return noTerminals;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "noTerminals=" + noTerminals +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", maxCoordinate=" + maxCoordinate +
                '}';
    }
}
