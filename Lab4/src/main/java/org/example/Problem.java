package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Problem {
    int n, m;
    List<List<Integer>> graph;
    ArrayList<Student> students;
    ArrayList<Project> projects;
    HashMap<Student, Integer> studentIntHashMap;

    /**
     * Displays the number of students that have fewer preferences than the average number
     */

    Problem(ArrayList<Student> students, ArrayList<Project> projects) {
        n = students.size();
        m = projects.size();
        this.studentIntHashMap = new HashMap<>();
        this.students = new ArrayList<>();
        this.projects = new ArrayList<>();
        for (Student student : students) {
            this.students.add(student);
            studentIntHashMap.put(student, this.students.size() - 1);
        }
        this.projects = projects;
    }

    void addConnection(int x, int y) {
        graph.get(x).add(y);
        graph.get(y).add(x);
    }

    int getAvgNumberOfPreferences() {
        int s = graph.stream().map(List::size).reduce(0, Integer::sum);
        return s / n;
    }

    List<Student> displayStudentsWithLessPreferences() {
        final int avg = getAvgNumberOfPreferences();
        return students.stream().filter(i -> graph.get(studentIntHashMap.get(i)).size() < avg).toList();
    }



}
