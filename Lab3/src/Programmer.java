import java.time.LocalDate;

public class Programmer extends Person {
    int wordsTypedPerMinute;

    Programmer() {
        wordsTypedPerMinute = 0;
    }

    public void setWordsTypedPerMinute(int wordsTypedPerMinute) {
        this.wordsTypedPerMinute = wordsTypedPerMinute;
    }

    public int getWordsTypedPerMinute() {
        return wordsTypedPerMinute;
    }

    Programmer(String name, LocalDate birthDate, int wordsTypedPerMinute) {
        super(name, birthDate);
        this.wordsTypedPerMinute = wordsTypedPerMinute;
    }
}
