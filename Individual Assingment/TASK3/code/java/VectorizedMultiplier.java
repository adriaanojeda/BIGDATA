package code.java;

public class VectorizedMultiplier implements MatrixMultiplier {
    @Override
    public void multiply(double[][] A, double[][] B, double[][] C) {
        int n = A.length;
        double[][] B_transposed = new double[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                B_transposed[i][j] = B[j][i];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = vectorizedDotProduct(A[i], B_transposed[j]);
            }
        }
    }

    private double vectorizedDotProduct(double[] rowA, double[] colB_as_row) {
        double sum = 0.0;
        for (int k = 0; k < rowA.length; k++) {
            sum += rowA[k] * colB_as_row[k];
        }
        return sum;
    }
}