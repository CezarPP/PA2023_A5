import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;

public class Person implements Node, Comparable<Node> {
    String name;
    LocalDate birthDate;
    HashSet<String> connections;

    Person() {
        name = "";
        birthDate = LocalDate.now();
        connections = new HashSet<>();
    }

    Person(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
        connections = new HashSet<>();
    }

    public static Person getRandomPerson() {
        StringBuilder randomName = new StringBuilder();
        int length = Math.abs(random.nextInt()) % MAX_NAME_LENGTH + 1;
        for (int i = 0; i < length; i++) {
            randomName.append(alphabet[Math.abs(random.nextInt()) % alphabet.length]);
        }
        int minDay = (int) LocalDate.of(1900, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
        int randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
        return new Person(randomName.toString(), randomBirthDate);
    }

    /**
     * adds a connection to either a company or another person
     *
     * @param name -> the name of the company/person
     */
    boolean addConnection(String name) {
        if (connections.contains(name))
            return false;
        connections.add(name);
        return true;
    }

    @Override
    public String getName() {
        return name;
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
