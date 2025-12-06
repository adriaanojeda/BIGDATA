package code.java;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicMultiplier implements MatrixMultiplier {
    @Override
    public void multiply(double[][] A, double[][] B, double[][] C) {
        int n = A.length;
        AtomicInteger currentRow = new AtomicInteger(0);
        
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];

        for (int t = 0; t < numThreads; t++) {
            threads[t] = new Thread(() -> {
                while (true) {
                    int i = currentRow.getAndIncrement();
                    if (i >= n) break;

                    for (int j = 0; j < n; j++) {
                        double sum = 0.0;
                        for (int k = 0; k < n; k++) {
                            sum += A[i][k] * B[k][j];
                        }
                        C[i][j] = sum;
                    }
                }
            });
            threads[t].start();
        }

        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}