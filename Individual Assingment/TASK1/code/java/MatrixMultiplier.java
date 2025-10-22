public class MatrixMultiplier {
    
    public static void multiply(int n, double[][] a, double[][] b, double[][] c) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = 0.0; 
                for (int k = 0; k < n; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
    }
    
    public static void initializeMatrix(int n, double[][] matrix) {
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }
    }
}