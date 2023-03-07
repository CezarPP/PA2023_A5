import java.util.Random;

public interface Node {
    String getName();

    Random random = new Random();

    int MAX_NAME_LENGTH = 10;
    char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
}
