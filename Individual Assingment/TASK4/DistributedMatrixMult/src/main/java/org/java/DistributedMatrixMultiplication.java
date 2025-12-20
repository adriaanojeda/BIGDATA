package org.java;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DistributedMatrixMultiplication {

    // Generador de filas para Spark
    public static List<double[]> generateDistributedMatrix(int rows, int cols) {
        List<double[]> matrixList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            double[] row = new double[cols];
            for (int j = 0; j < cols; j++) {
                row[j] = rand.nextDouble();
            }
            matrixList.add(row);
        }
        return matrixList;
    }

    // Generador de matriz normal (para B, que se hace broadcast)
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

    // Lógica del Map: Multiplicar 1 fila de A por toda la matriz B
    private static double[] multiplyRowByMatrix(double[] rowA, double[][] B) {
        int n = rowA.length;    // Columnas de A
        int p = B[0].length;    // Columnas de B
        double[] resultRow = new double[p];

        for (int j = 0; j < p; j++) {
            double sum = 0.0;
            for (int k = 0; k < n; k++) {
                sum += rowA[k] * B[k][j];
            }
            resultRow[j] = sum;
        }
        return resultRow;
    }

    public static void main(String[] args) {
        // Configurar Spark para usar todos los cores locales
        SparkSession spark = SparkSession.builder()
                .appName("DistributedMatrixMultiplication")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        sc.setLogLevel("ERROR"); // Menos ruido en consola

        int N = 1000; // TAMAÑO DE PRUEBA (Igual que en Basic)

        System.out.println("Generando datos distribuidos " + N + "x" + N + "...");

        // 1. Matriz A se convierte en RDD (Distribuida en nodos)
        List<double[]> listA = generateDistributedMatrix(N, N);
        JavaRDD<double[]> rddA = sc.parallelize(listA);

        // 2. Matriz B se queda en local y luego se hace Broadcast (se envía copia a todos los nodos)
        double[][] matrixB = generateMatrix(N, N);
        Broadcast<double[][]> broadcastB = sc.broadcast(matrixB);

        System.out.println("Iniciando multiplicación DISTRIBUIDA (Spark)...");
        long startTime = System.nanoTime();

        // 3. Transformación: Map
        JavaRDD<double[]> rddC = rddA.map(rowA ->
                multiplyRowByMatrix(rowA, broadcastB.value())
        );

        // 4. Acción: Count (para forzar la ejecución sin traer todos los datos a RAM)
        // Usamos count() en vez de collect() para medir solo el tiempo de cálculo distribuido
        long count = rddC.count();

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1e9;

        System.out.println("Tiempo Spark Distribuido (N=" + N + "): " + duration + " segundos");
        System.out.println("Filas procesadas: " + count);

        sc.close();
        spark.stop();
    }
}