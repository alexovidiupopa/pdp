package ro.alexpopa.model;

import java.util.Random;

public final class Matrix {
    public final int n,m;
    public int [][] matrix;

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        matrix = new int [n][m];
    }

    public void populate() {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = random.nextInt(10)+1;
            }
        }
    }

    public int get(int row, int column) {
        return matrix[row][column];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void set(int row, int column, int value) {
        matrix[row][column] = value;
    }

}
