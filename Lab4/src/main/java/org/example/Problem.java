package org.example;

import java.util.*;

public class Problem {
    int n, m, totalSize;
    List<List<Integer>> graph;
    ArrayList<Student> students;
    ArrayList<Project> projects;
    HashMap<Student, Integer> studentIntHashMap;


    // for solution
    List<Integer> ma;
    List<Boolean> viz;

    static Random rand = new Random();

    /**
     * @param sizeN -> number of students
     * @param sizeM -> number of projects
     * @param sizeK -> number of connections
     * @return random problem with these specs
     */
    static Problem getRandomProblem(int sizeN, int sizeM, int sizeK) {
        ArrayList<Student> studentList = Student.getRandomStudentList(sizeN);
        ArrayList<Project> projectList = Project.getRandomProjectList(sizeM);
        Problem problem = new Problem(studentList, projectList);
        for (int i = 0; i < sizeK; i++) {
            int index1 = rand.nextInt(sizeN - 1), index2 = rand.nextInt(sizeM - 1);
            problem.addConnection(index1, index2);
        }
        return problem;
    }


    /**
     * Displays the number of students that have fewer preferences than the average number
     */

    Problem(ArrayList<Student> students, ArrayList<Project> projects) {
        n = students.size();
        m = projects.size();
        totalSize = n + m;
        this.studentIntHashMap = new HashMap<>();
        this.students = new ArrayList<>();
        this.projects = new ArrayList<>();
        graph = new ArrayList<>();
        for (int i = 0; i < totalSize; i++)
            graph.add(new ArrayList<>());
        for (Student student : students) {
            this.students.add(student);
            studentIntHashMap.put(student, this.students.size() - 1);
        }
        this.projects = projects;
    }

    void addConnection(int x, int y) {
        graph.get(x).add(y + n);
        graph.get(y + n).add(x);
    }

    int getAvgNumberOfPreferences() {
        int s = graph.stream().map(List::size).reduce(0, Integer::sum);
        return s / n;
    }

    List<Student> displayStudentsWithLessPreferences() {
        final int avg = getAvgNumberOfPreferences();
        return students.stream().filter(i -> graph.get(studentIntHashMap.get(i)).size() < avg).toList();
    }

    boolean dfsMatching(int node) {
        if (viz.get(node))
            return false;
        viz.set(node, true);
        for (Integer it : graph.get(node))
            if (ma.get(it) == -1 || dfsMatching(ma.get(it))) {
                ma.set(node, it);
                ma.set(it, node);
                return true;
            }
        return false;
    }

    void doMatching() {
        ma = new ArrayList<>(totalSize);
        viz = new ArrayList<>(totalSize);
        for (int i = 0; i < totalSize; i++) {
            ma.add(-1);
            viz.add(false);
        }
        boolean ok = true;
        while (ok) {
            ok = false;
            for (int i = 0; i < totalSize; i++)
                viz.set(i, false);
            for (int i = 0; i < n; i++)
                if (ma.get(i) == -1 && !viz.get(i) && dfsMatching(i))
                    ok = true;
        }
    }

    int getMatchingResult() {
        int cntMatched = 0;
        for (int i = 0; i < n; i++)
            if (ma.get(i) != -1)
                cntMatched++;
        return cntMatched;
    }

    List<Pair> getMatchedPairs() {
        ArrayList<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (ma.get(i) != -1)
                pairs.add(new Pair(students.get(i), projects.get(ma.get(i) - n)));
        return pairs;
    }

    void dfsVertexCover(int node) {
        viz.set(node, true);
        for (Integer it : graph.get(node))
            if (!viz.get(it)) {
                assert (ma.get(it) != -1);
                viz.set(it, true);
                dfsVertexCover(ma.get(it));
            }
    }

    // get any MVC
    ArrayList<Object> getMinVertexCover() {
        doMatching();
        minVertexCoverInit();
        ArrayList<Object> ans = new ArrayList<>();
        for (int i = n; i < n + m; i++)
            if (viz.get(i))
                ans.add(projects.get(i - n));
        for (int i = 0; i < n; i++)
            if (!viz.get(i))
                ans.add(students.get(i));
        return ans;
    }

    ArrayList<Object> getMaxIndependentSet() {
        minVertexCoverInit();
        ArrayList<Object> ans = new ArrayList<>();
        for (int i = n; i < n + m; i++)
            if (!viz.get(i))
                ans.add(projects.get(i - n));
        for (int i = 0; i < n; i++)
            if (viz.get(i))
                ans.add(students.get(i));
        return ans;
    }

    private void minVertexCoverInit() {
        for (int i = 0; i < totalSize; i++)
            viz.set(i, false);
        for (int i = 0; i < n; i++)
            if (ma.get(i) == -1 && !viz.get(i))
                dfsVertexCover(i);
    }

    int getGreedyMatching() {
        ma = new ArrayList<>(totalSize);
        for (int i = 0; i < totalSize; i++)
            ma.add(-1);
        int cntMatched = 0;
        for (int i = 0; i < n; i++) {
            for(Integer it: graph.get(i))
                if(ma.get(it) == -1) {
                    ma.set(i, it);
                    ma.set(it, i);
                    cntMatched++;
                    break;
                }
        }
        return cntMatched;
    }
}
