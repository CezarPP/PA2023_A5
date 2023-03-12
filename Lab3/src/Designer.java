import java.time.LocalDate;

public class Designer extends Person {
    int noOfProjects;

    Designer() {
        noOfProjects = 0;
    }

    public int getNoOfProjects() {
        return noOfProjects;
    }

    public void setNoOfProjects(int noOfProjects) {
        this.noOfProjects = noOfProjects;
    }

    Designer(String name, LocalDate birthDate, int noOfProjects) {
        super(name, birthDate);
        this.noOfProjects = noOfProjects;
    }
}
