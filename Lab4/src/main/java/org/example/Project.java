package org.example;

public class Project implements Comparable<Project> {
    String name;

    Project(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Project o) {
        return name.compareTo(o.name);
    }
}
