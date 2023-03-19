package org.example;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class Student implements Comparable<Student> {
    String name;

    static Faker faker = new Faker();

    Student(String name) {
        this.name = name;
    }

    static List<Student> getRandomStudentList(int size) {
        ArrayList<Student> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(new Student(faker.name().fullName()));
        return list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(Student o) {
        return name.compareTo(o.name);
    }
}
