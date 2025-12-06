package code.java;

public class ThreadMultiplier implements MatrixMultiplier {
    @Override
    public void multiply(double[][] A, double[][] B, double[][] C) {
        int n = A.length;
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            final int row = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < n; j++) {
                    double sum = 0.0;
                    for (int k = 0; k < n; k++) {
                        sum += A[row][k] * B[k][j];
                    }
                    C[row][j] = sum;
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}