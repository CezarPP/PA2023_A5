package org.example;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
/*        var studentsList = IntStream.rangeClosed(0, 3)
                .mapToObj(i -> new Student("S" + i))
                .toArray();
        LinkedList<Student> studentLinkedList = new LinkedList<>();
        for (Object s : studentsList)
            studentLinkedList.add((Student) s);
        TreeSet<Student> studentTreeSet = new TreeSet<>(studentLinkedList);*/

        long startTime = System.currentTimeMillis();
        Problem problem = Problem.getRandomProblem(10000, 10000, 30000);
        problem.doMatching();
        int matchingResult = problem.getMatchingResult();
        int greedyMatchResult = problem.getGreedyMatching();
        long endTime = System.currentTimeMillis();
        System.out.println("The greedy matching yields a result of " + greedyMatchResult);
        System.out.println("The actual matching is " + matchingResult);
        long totalTime = endTime - startTime;
        System.out.println("For an instance of 20.000 nodes and 30.000 edges, the algorithm took " + totalTime + " ms");

/*
        ArrayList<Object> vertexCover = problem.getMinVertexCover();
        ArrayList<Object> independentSet = problem.getMaxIndependentSet();
        System.out.println("The minimum vertex cover is: ");
        for (Object o : vertexCover)
            System.out.println(o);
        System.out.println();
        System.out.println("The maximum independent set is: ");
        for (Object o : independentSet)
            System.out.println(o);*/

    }
}