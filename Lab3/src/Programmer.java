import java.time.LocalDate;

public class Programmer extends Person {
    int wordsTypedPerMinute;

    Programmer() {
        wordsTypedPerMinute = 0;
    }

    Programmer(String name, LocalDate birthDate, int wordsTypedPerMinute) {
        super(name, birthDate);
        this.wordsTypedPerMinute = wordsTypedPerMinute;
    }
}
