package org.example;

import java.util.ArrayList;
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

        Problem problem = Problem.getRandomProblem(1000, 1000, 5000);
        problem.doMatching();
        ArrayList<Object> vertexCover = problem.getMinVertexCover();
        ArrayList<Object> independentSet = problem.getMaxIndependentSet();
        System.out.println("The minimum vertex cover is: ");
        for (Object o : vertexCover)
            System.out.println(o);
        System.out.println();
        System.out.println("The maximum independent set is: ");
        for (Object o : independentSet)
            System.out.println(o);

    }
}