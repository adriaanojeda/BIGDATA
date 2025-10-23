import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class Benchmark {

    /**
     * Intenta encontrar la ruta de la carpeta 'data' subiendo hasta 3 niveles 
     * desde el Directorio de Trabajo Actual (CWD).
     * Esto asegura que la carpeta 'data' se encuentre en la raíz del proyecto (TASK1).
     */
    private static Path findDataPath(String fileName) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        
        // El directorio 'data' se encuentra en la raíz del proyecto (TASK1).
        // Si ejecutamos desde 'code/java', necesitamos subir dos niveles.
        
        Path projectRoot = null;
        
        // 1. Buscamos la raíz subiendo hasta 3 niveles.
        Path tempPath = currentPath;
        for (int i = 0; i < 3; i++) {
            // Verificamos si 'data' existe en este nivel
            if (Files.exists(tempPath.resolve("data"))) {
                projectRoot = tempPath;
                break;
            }
            Path parentPath = tempPath.getParent();
            if (parentPath == null) {
                break;
            }
            tempPath = parentPath;
        }

        if (projectRoot != null) {
            // 2. Si encontramos la raíz (TASK1), resolvemos a data/results_java.csv
            return projectRoot.resolve("data").resolve(fileName);
        } else {
            // Fallback: Si no se encuentra 'data' después de subir, usamos una ruta relativa que suele funcionar
            // cuando el usuario ejecuta el binario desde la raíz del proyecto.
            return Paths.get("data").resolve(fileName); 
        }
    }

    private static void saveData(int n, int numRuns, double averageTimeMs) {
        double averageTimeSec = averageTimeMs * 1e-3;
        
        // Utilizamos la función robusta para encontrar la ruta
        Path filePath = findDataPath("results_java.csv");

        try {
            // Aseguramos que el directorio exista (en caso de que lo encuentre lejos)
            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            
            java.io.FileWriter fw = new java.io.FileWriter(filePath.toFile(), true);
            
            fw.write("Java," + n + "," + numRuns + "," + String.format(Locale.US, "%.6f", averageTimeSec) + "\n");
            fw.close();
            System.out.println("Resultado guardado en " + filePath.toString());
        } catch (IOException e) {
            System.err.println("ADVERTENCIA: No se pudo guardar el archivo de datos.");
            System.err.println("Ruta intentada: " + filePath.toString());
            // Opcional: e.printStackTrace();
        }
    }

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
        
        long totalTimeMs = 0;

        for (int run = 0; run < numRuns; run++) {
            long start = System.currentTimeMillis();
            
            MatrixMultiplier.multiply(n, a, b, c);

            long stop = System.currentTimeMillis();
            long runTimeMs = stop - start;
            totalTimeMs += runTimeMs;
        }

        double averageTimeMs = (double) totalTimeMs / numRuns;
        System.out.printf("Tiempo promedio (segundos): %.6f\n", averageTimeMs * 1e-3);
        
        saveData(n, numRuns, averageTimeMs);
    }
}
