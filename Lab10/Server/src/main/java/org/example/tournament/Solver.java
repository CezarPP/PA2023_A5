package org.example.tournament;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver {
    static final Random rand = new Random();

    /**
     * -> each 2 players play exactly once
     * -> <= p games / day / player
     * -> tournament finish in at most d days
     *
     * @param n number of players
     */
    public static boolean[][][] solveILP(int n, int d, int p) {
        /*
        i, j <= n
        k    <= d
        D(i, j, k) -> whether i plays with j on day k

        forAll i,j, i != j, sum(k = 1 to d) D(i, j, k) = 1
        forAll i, k => sum(j = 1 to n, i != j) D(i, j, k) <= p
         */
        Loader.loadNativeLibraries();
        MPSolver solver = MPSolver.createSolver("SCIP");
        if (solver == null) {
            System.out.println("Could not create solver SCIP");
            return null;
        }
        double infinity = java.lang.Double.POSITIVE_INFINITY;
        // x and y are integer non-negative variables.
        MPVariable[][][] D = new MPVariable[n][n][d];
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                for (int k = 0; k < d; k++)
                    D[i][j][k] = solver.makeBoolVar(getName(i, j, k));
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                MPConstraint ct = solver.makeConstraint(1.0, 1.0, "i" + i + "j" + j);
                for (int k = 0; k < d; k++) {
                    ct.setCoefficient(D[i][j][k], 1.0);
                }
            }
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < d; k++) {
                MPConstraint ct = solver.makeConstraint(0.0, p, "i" + i + "k" + k);
                for (int j = i + 1; j < n; j++) {
                    ct.setCoefficient(D[i][j][k], 1.0);
                }
            }
        }

        System.out.println("Number of variables = " + solver.numVariables());
        solver.solve();
        System.out.println("Solver found solution");
        boolean[][][] ans = new boolean[n][n][d];
        for (int k = 0; k < d; k++) {
            System.out.println("During day " + k + " the matches are: ");
            for (int i = 0; i < n; i++)
                for (int j = i + 1; j < n; j++) {
                    if (D[i][j][k].solutionValue() == 1.0) {
                        ans[i][j][k] = true;
                        System.out.println(i + " plays against " + j);
                    }
                }
        }
        return ans;
    }

    private static String getName(int i, int j, int k) {
        return "i" + i + "j" + j + "k" + k;
    }

    /**
     * Given a complete undirected graph randomly generated try to find a hamiltonian path
     *
     * @param N the number of vertices of the graph
     * @return the hamiltonian path or null if there is no path
     */
    public static List<Integer> solveForSchedule(int N) {
        List<List<Integer>> v = new ArrayList<>();
        List<List<Integer>> inv = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            v.add(new ArrayList<Integer>());
            inv.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++)
                if (rand.nextBoolean()) {
                    v.get(i).add(j);
                    inv.get(j).add(i);
                } else {
                    v.get(j).add(i);
                    inv.get(i).add(j);
                }
        }

        final int cnfMax = (1 << N);
        boolean[][] dp = new boolean[cnfMax][N];
        int[][] prev = new int[cnfMax][N];
        dp[1][0] = true;
        prev[1][0] = 0;
        for (int cnf = 3; cnf < cnfMax; cnf++) {
            for (int i = 1; i < N; i++)
                if ((cnf & (1 << i)) != 0) {
                    int mask = (cnf ^ (1 << i));
                    for (Integer it : inv.get(i))
                        if (dp[mask][it]) {
                            dp[cnf][i] = true;
                            prev[cnf][i] = it;
                        }
                }
        }
        for (int i = 1; i < N; i++)
            if (dp[cnfMax - 1][i]) {
                List<Integer> path = new ArrayList<>();
                int cnf = cnfMax - 1;
                int x = i;
                while (x != 0) {
                    path.add(x);
                    int cnfAux = cnf;
                    cnf ^= (1 << x);
                    x = prev[cnfAux][x];
                }
                path.add(0);
                return path;
            }

        return null;
    }
}
