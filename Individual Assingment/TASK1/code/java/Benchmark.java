public class Benchmark {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: java Benchmark <tamaño_N> <num_ejecuciones>");
            System.exit(1);
        }

        int n = Integer.parseInt(args[0]);
        int numRuns = Integer.parseInt(args[1]);

        double[][] a = new double[n][n];
        double[][] b = new double[n][n];
        double[][] c = new double[n][n];
        
        MatrixMultiplier.initializeMatrix(n, a);
        MatrixMultiplier.initializeMatrix(n, b);

        System.out.println("Java Benchmark - N=" + n + ", Runs=" + numRuns);
        
        long totalTime = 0;

        // Bucle para realizar varias ejecuciones
        for (int run = 0; run < numRuns; run++) {
            long start = System.currentTimeMillis();
            
            // Llama al código de producción
            MatrixMultiplier.multiply(n, a, b, c);

            long stop = System.currentTimeMillis();
            long runTime = stop - start;
            totalTime += runTime;
            
            // Puedes imprimir el tiempo de cada ejecución si lo deseas
            // System.out.printf("Run %d: %.3f s\n", run + 1, runTime * 1e-3);
        }

        double averageTime = (double) totalTime / numRuns;
        System.out.printf("Tiempo promedio (segundos): %.6f\n", averageTime * 1e-3);
        
        // Opcional: imprimir el checksum de un elemento para verificar la corrección
        // System.out.printf("C[0][0] para verificación: %.6f\n", c[0][0]); 
    }
}