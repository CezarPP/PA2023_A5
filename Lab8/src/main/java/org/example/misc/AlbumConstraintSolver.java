package org.example.misc;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.example.entities.AlbumsEntity;

import java.util.Comparator;
import java.util.List;

public class AlbumConstraintSolver {

    public static void findAlbums(List<AlbumsEntity> albums, int k, int p) {
        System.out.println("Started solving for k -> " + k + " and p -> " + p);
        Model model = new Model("Albums solver");

        albums.sort(Comparator.comparing(AlbumsEntity::getTitle));

        int n = albums.size();

        IntVar[] isSelected = model.intVarArray("isSelected", n, 0, 1);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (albums.get(i).getTitle().charAt(0) != albums.get(j).getTitle().charAt(0)
                        || Math.abs(albums.get(i).getReleaseYear() - albums.get(j).getReleaseYear()) > p) {
                    model.arithm(isSelected[i], "+", isSelected[j], "<=", 1).post();
                }
            }
        }

        IntVar sum = model.intVar("sum", k, n);
        model.sum(isSelected, "=", sum).post();

        Solution solution = model.getSolver().findSolution();

        if (solution != null) {
            System.out.println("Solution found:");
            for (int i = 0; i < n; i++) {
                if (solution.getIntVal(isSelected[i]) == 1) {
                    System.out.println(albums.get(i).getTitle() + ", released in " + albums.get(i).getReleaseYear());
                }
            }
        } else {
            System.out.println("No solution found");
        }
    }
}