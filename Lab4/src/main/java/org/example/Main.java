package org.example;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        var studentsList = IntStream.rangeClosed(0, 3)
                .mapToObj(i -> new Student("S" + i))
                .toArray();
        LinkedList<Student> studentLinkedList = new LinkedList<>();
        for (Object s : studentsList)
            studentLinkedList.add((Student) s);
        TreeSet<Student> studentTreeSet = new TreeSet<>(studentLinkedList);

    }
}