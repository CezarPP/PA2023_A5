import java.util.Random;

public class Location {
    static Random random = new Random();
    static final int MAX_COORDINATE = 1000;
    static final int MAX_NAME_LENGTH = 10;
    static final int NO_LOCATION_TYPES = 3;
    static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    String name;
    LocationType type;
    int x, y;

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    static int getRandomCoordinate() {
        return Math.abs(random.nextInt()) % MAX_COORDINATE;
    }

    static Location getRandomLocation() {
        int x = getRandomCoordinate();
        int y = getRandomCoordinate();
        StringBuilder name = new StringBuilder();
        int length = Math.abs(random.nextInt()) % MAX_NAME_LENGTH;
        for (int i = 0; i < length; i++) {
            name.append(alphabet[Math.abs(random.nextInt()) % alphabet.length]);
        }
        int typeIndex = (Math.abs(random.nextInt()) % NO_LOCATION_TYPES);
        if (typeIndex == 0)
            return new City(name.toString(), LocationType.CITY, x, y, City.getRandomPopulation());
        else if (typeIndex == 1)
            return new Airport(name.toString(), LocationType.AIRPORT, x, y, Airport.getRandomNoTerminals());
        return new GasStation(name.toString(), LocationType.GAS_STATION, x, y, GasStation.getRandomGasPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Location location = (Location) o;
        return x == location.x && y == location.y && name.equals(location.name) && type == location.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public LocationType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Location(String name, LocationType type, int x, int y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    final int maxCoordinate = 1000;
}

enum LocationType {
    CITY,
    AIRPORT,
    GAS_STATION
}
