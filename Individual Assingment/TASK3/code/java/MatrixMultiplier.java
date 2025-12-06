package code.java;

public interface MatrixMultiplier {
    void multiply(double[][] A, double[][] B, double[][] C);

    static void initializeMatrix(int n, double[][] matrix) {
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }
    }
}