import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class Company implements Node, Comparable<Node> {
    String name;

    Company() {
        name = "";
    }

    Company(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Company getRandomCompany() {
        StringBuilder randomName = new StringBuilder();
        int length = Math.abs(random.nextInt()) % MAX_NAME_LENGTH + 1;
        for (int i = 0; i < length; i++) {
            randomName.append(alphabet[Math.abs(random.nextInt()) % alphabet.length]);
        }
        return new Company(randomName.toString());
    }


    @Override
    public int compareTo(Node o) {
        if (o == null)
            throw new NullPointerException();
        if (!(o instanceof Company) && !(o instanceof Person))
            throw new ClassCastException();
        return getName().compareTo(o.getName());
    }
}
