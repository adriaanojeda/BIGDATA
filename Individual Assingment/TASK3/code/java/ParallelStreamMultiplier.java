package code.java;

import java.util.stream.IntStream;

public class ParallelStreamMultiplier implements MatrixMultiplier {
    @Override
    public void multiply(double[][] A, double[][] B, double[][] C) {
        int n = A.length;
        
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        });
    }
}