package org.example;

public class TriangleCounter {
    public static int countTriangles(int[][] adjacencyMatrix) {
        int[][] A3 = matrixPower(adjacencyMatrix, 3);
        int count = 0;
        for (int i = 0; i < A3.length; i++) {
            count += A3[i][i];
        }
        return count / 6;
    }

    private static int[][] matrixPower(int[][] matrix, int power) {
        if (power == 1) {
            return matrix;
        } else if (power % 2 == 0) {
            int[][] halfPower = matrixPower(matrix, power / 2);
            return multiplyMatrices(halfPower, halfPower);
        } else {
            return multiplyMatrices(matrix, matrixPower(matrix, power - 1));
        }
    }

    private static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }
}
