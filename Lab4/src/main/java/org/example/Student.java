package org.example;

public class Student implements Comparable<Student> {
    String name;

    Student(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Student o) {
        return name.compareTo(o.name);
    }
}
