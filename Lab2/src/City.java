import java.util.Random;

public class City extends Location {
    int population;
    static final Random random = new Random();
    static final int MAX_POPULATION = 100;

    City(String name, LocationType type, int x, int y, int population) {
        super(name, type, x, y);
        this.population = population;
    }

    static int getRandomPopulation() {
        return Math.abs(random.nextInt()) % MAX_POPULATION;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "City{" +
                "population=" + population +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", maxCoordinate=" + maxCoordinate +
                '}';
    }
}
