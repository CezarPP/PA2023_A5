package org.example;

import org.example.tournament.Solver;

import java.util.List;

public class Main {
    static final int N = 10;
    static final int D = 5;
    static final int P = 5;

    public static void main(String[] args) {
        // SimpleServer.startSimpleServer();
        boolean[][][] d = Solver.solveILP(N, D, P);
        List<Integer> path = Solver.solveForSchedule(10);
        if (path == null) {
            System.out.println("No hamiltonian cycle");
        } else {
            for (Integer i : path)
                System.out.print(i + " ");
            System.out.println();
        }
    }
}