package org.java;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DistributedMatrixMult {

    // --- MAPPER ---
    // Recibe: Offset, Linea de texto
    // Emite: Clave (Text "fila,columna"), Valor (Text "Matriz,indice,valor")
    public static class MatrixMapper extends Mapper<LongWritable, Text, Text, Text> {

        // Asumimos matrices cuadradas N x N.
        // En un entorno real, esto se pasaría por configuración (conf.setInt...)
        private static final int N = 4;

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // Formato entrada esperado: Matriz,Fila,Columna,Valor (ej: A,0,1,2.5)
            String line = value.toString();
            if (line.isEmpty()) return;

            String[] parts = line.split(",");
            if (parts.length != 4) return; // Validación básica

            String mat = parts[0];
            int r = Integer.parseInt(parts[1].trim());
            int c = Integer.parseInt(parts[2].trim());
            double val = Double.parseDouble(parts[3].trim());

            if (mat.equals("A")) {
                // Si es A(i,k), necesitamos enviarlo a todas las celdas C(i, j) donde j va de 0 a N
                for (int j = 0; j < N; j++) {
                    context.write(new Text(r + "," + j), new Text("A," + c + "," + val));
                }
            } else if (mat.equals("B")) {
                // Si es B(k,j), necesitamos enviarlo a todas las celdas C(i, j) donde i va de 0 a N
                for (int i = 0; i < N; i++) {
                    context.write(new Text(i + "," + c), new Text("B," + r + "," + val));
                }
            }
        }
    }

    // --- REDUCER ---
    // Recibe: Clave "fila,columna" (Celda de C), Lista de valores de A y B
    // Emite: Clave "fila,columna", Valor (Resultado numérico)
    public static class MatrixReducer extends Reducer<Text, Text, Text, DoubleWritable> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            // Usamos mapas para almacenar los valores dispersos de la fila de A y la columna de B
            Map<Integer, Double> mapA = new HashMap<>();
            Map<Integer, Double> mapB = new HashMap<>();

            for (Text val : values) {
                String[] parts = val.toString().split(",");
                String type = parts[0];
                int index = Integer.parseInt(parts[1]); // Este es el índice 'k' común
                double value = Double.parseDouble(parts[2]);

                if (type.equals("A")) {
                    mapA.put(index, value);
                } else {
                    mapB.put(index, value);
                }
            }

            // Realizar el producto escalar (Dot Product)
            double result = 0.0;
            // Solo iteramos sobre los elementos que existen en A.
            // Si no existen en B, el producto es 0, así que no importa.
            for (Map.Entry<Integer, Double> entry : mapA.entrySet()) {
                int k = entry.getKey();
                double valA = entry.getValue();

                if (mapB.containsKey(k)) {
                    result += valA * mapB.get(k);
                }
            }

            // Emitir solo si el resultado no es cero (preservar dispersión)
            if (result != 0.0) {
                context.write(key, new DoubleWritable(result));
            }
        }
    }

    // --- DRIVER (MAIN) ---
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Distributed Matrix Multiplication");
        job.setJarByClass(DistributedMatrixMult.class);

        job.setMapperClass(MatrixMapper.class);
        job.setReducerClass(MatrixReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Configuración de argumentos de entrada/salida
        // Si estuviéramos en un clúster real, usaríamos args[0] y args[1]
        // Para compilar, esto es suficiente.
        if (args.length >= 2) {
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
        } else {
            System.out.println("Uso: hadoop jar ... <input_path> <output_path>");
            System.exit(0); // No fallamos, solo avisamos
        }

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}