import java.time.LocalDate;

public class Designer extends Person {
    int noOfProjects;

    Designer() {
        noOfProjects = 0;
    }

    Designer(String name, LocalDate birthDate, int noOfProjects) {
        super(name, birthDate);
        this.noOfProjects = noOfProjects;
    }
}
