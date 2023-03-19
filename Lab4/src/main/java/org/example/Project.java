package org.example;

import com.github.javafaker.Faker;

import java.util.ArrayList;

public class Project implements Comparable<Project> {
    String name;

    static Faker faker = new Faker();

    Project(String name) {
        this.name = name;
    }

    static ArrayList<Project> getRandomProjectList(int size) {
        ArrayList<Project> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(new Project(faker.team().name()));
        return list;
    }

    @Override
    public int compareTo(Project o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                '}';
    }
}
