package code.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Benchmark {

    private static Path findDataPath(String fileName) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path projectRoot = null;
        Path tempPath = currentPath;
        
        for (int i = 0; i < 3; i++) {
            if (Files.exists(tempPath.resolve("data"))) {
                projectRoot = tempPath;
                break;
            }
            if (tempPath.getParent() == null) break;
            tempPath = tempPath.getParent();
        }

        if (projectRoot != null) {
            return projectRoot.resolve("data").resolve(fileName);
        } else {
            return Paths.get("data").resolve(fileName); 
        }
    }

    private static void saveData(String method, int n, int numRuns, double averageTimeMs) {
        double averageTimeSec = averageTimeMs * 1e-3;
        Path filePath = findDataPath("results_parallel.csv"); 

        try {
            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            boolean isNew = Files.notExists(filePath);
            java.io.FileWriter fw = new java.io.FileWriter(filePath.toFile(), true);
            
            if (isNew) {
                fw.write("Method,Size,Runs,TimeSec\n");
            }
            
            fw.write(method + "," + n + "," + numRuns + "," + String.format(Locale.US, "%.6f", averageTimeSec) + "\n");
            fw.close();
            System.out.println("   -> Guardado: " + method + " | Time: " + String.format("%.4f", averageTimeSec) + "s");
        } catch (IOException e) {
            System.err.println("Error guardando datos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: java Benchmark <tamaño_N> <num_ejecuciones>");
            System.exit(1);
        }

        int n = Integer.parseInt(args[0]);
        int numRuns = Integer.parseInt(args[1]);

        System.out.println("=== INICIANDO BENCHMARK TASK 3 (N=" + n + ") ===");

        double[][] a = new double[n][n];
        double[][] b = new double[n][n];
        double[][] c = new double[n][n];

        MatrixMultiplier.initializeMatrix(n, a);
        MatrixMultiplier.initializeMatrix(n, b);

        Map<String, MatrixMultiplier> implementations = new LinkedHashMap<>();
        implementations.put("Sequential", new SequentialMultiplier());
        implementations.put("ParallelStream", new ParallelStreamMultiplier());
        implementations.put("Threads_Manual", new ThreadMultiplier());
        implementations.put("Atomic_BagOfTasks", new AtomicMultiplier());
        implementations.put("Vectorized_Sim", new VectorizedMultiplier());

        for (Map.Entry<String, MatrixMultiplier> entry : implementations.entrySet()) {
            String name = entry.getKey();
            MatrixMultiplier multiplier = entry.getValue();

            System.out.print("Ejecutando " + name + "... ");
            
            if (n <= 512) { 
                 multiplier.multiply(a, b, c);
            }

            long totalTimeMs = 0;
            for (int run = 0; run < numRuns; run++) {
                long start = System.currentTimeMillis();
                multiplier.multiply(a, b, c);
                long stop = System.currentTimeMillis();
                totalTimeMs += (stop - start);
            }

            double avgTime = (double) totalTimeMs / numRuns;
            saveData(name, n, numRuns, avgTime);
        }
        System.out.println("=== FIN DEL BENCHMARK ===");
    }
}