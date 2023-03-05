import java.util.Objects;
import java.util.Random;

public class Road {
    static final Random random = new Random();
    static final int MAX_SPEED_LIMIT = 100;
    static final int MAX_DISTANCE_ADD = 100;

    static final int NO_ROAD_TYPES = 3;
    RoadType type;
    int length;
    int speedLimit;

    Location a, b;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Road road = (Road) o;
        return Double.compare(road.length, length) == 0 &&
                speedLimit == road.speedLimit && type == road.type &&
                a.equals(road.a) && b.equals(road.b);
    }

    static int getRandomSpeedLimit() {
        return Math.abs(random.nextInt()) % MAX_SPEED_LIMIT + 1;
    }

    static RoadType getRandomRoadType() {
        return RoadType.values()[Math.abs(random.nextInt()) % NO_ROAD_TYPES];
    }

    static Road getRandomRoad(Location a, Location b) {
        int speedLimit = getRandomSpeedLimit();
        int length = (int) Math.round(getDistance(a, b) + (Math.abs(random.nextInt()) % MAX_DISTANCE_ADD));
        RoadType roadType = getRandomRoadType();
        return new Road(roadType, length, speedLimit, a, b);
    }

    public void setA(Location a) {
        this.a = a;
    }

    public void setB(Location b) {
        this.b = b;
    }

    public Location getA() {
        return a;
    }

    public Location getB() {
        return b;
    }

    @Override
    public String toString() {
        return "Road{" +
                "type=" + type +
                ", length=" + length +
                ", speedLimit=" + speedLimit +
                '}';
    }

    public void setType(RoadType type) {
        this.type = type;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public RoadType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    static double getDistance(Location a, Location b) {
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) +
                (a.getY() - b.getY()) * (a.getY() - b.getY()));
    }

    Road(RoadType type, int length, int speedLimit, Location a, Location b) {
        this.type = type;
        this.length = length;
        this.speedLimit = speedLimit;
        this.a = a;
        this.b = b;
    }

}

enum RoadType {
    HIGHWAYS,
    EXPRESS,
    COUNTRY
}
