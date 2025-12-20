package org.java;

import java.util.Random;

public class BasicMatrixMultiplication {

    // Generador de matrices aleatorias
    public static double[][] generateMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextDouble();
            }
        }
        return matrix;
    }

    // Multiplicación clásica (O(N^3))
    public static double[][] multiply(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        int p = B[0].length;
        double[][] C = new double[m][p];

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                double a_ik = A[i][k];
                for (int j = 0; j < p; j++) {
                    C[i][j] += a_ik * B[k][j];
                }
            }
        }
        return C;
    }

    public static void main(String[] args) {
        int N = 1000; // TAMAÑO DE LA PRUEBA (Ajustar para ver escalabilidad: 100, 500, 1000)

        System.out.println("Generando matrices " + N + "x" + N + "...");
        double[][] A = generateMatrix(N, N);
        double[][] B = generateMatrix(N, N);

        System.out.println("Iniciando multiplicación SECUENCIAL...");
        long startTime = System.nanoTime();

        multiply(A, B);

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1e9; // Segundos

        System.out.println("Tiempo Secuencial (N=" + N + "): " + duration + " segundos");
    }
}