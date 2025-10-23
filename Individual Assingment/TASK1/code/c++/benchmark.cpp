#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <ctime>
#include <sys/time.h>
#include "matrix_multiplier.hpp"

// Función de temporización
double wtime() {
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec / 1000000.0;
}

// Función para guardar resultados, probando las rutas más probables.
void saveData(int n, int numRuns, double averageTime) {
    
    // Rutas que cubren los principales escenarios de ejecución:
    const char* paths_to_try[] = {
        // 1. Ejecución desde code/c++ (sube dos niveles: ../../data)
        "../../data/results_cpp.csv", 
        // 2. Ejecución desde la raíz del proyecto (TASK1)
        "data/results_cpp.csv",
        // 3. Ejecución desde code/ (sube un nivel: ../data)
        "../data/results_cpp.csv"
    };
    
    std::string final_filepath;
    std::ofstream file;

    // Iterar sobre las rutas hasta que se pueda abrir el archivo
    for (const char* filepath : paths_to_try) {
        file.open(filepath, std::ios_base::app);
        if (file.is_open()) {
            final_filepath = filepath;
            break; // Salimos del bucle si encontramos una ruta válida
        }
    }
    
    if (file.is_open()) {
        file << "C++," << n << "," << numRuns << "," << std::fixed << std::setprecision(6) << averageTime << "\n";
        file.close();
        std::cout << "Resultado guardado en " << final_filepath << std::endl;
    } else {
        std::cerr << "ADVERTENCIA: No se pudo abrir el archivo de datos para escritura." << std::endl;
        std::cerr << "Por favor, asegúrese de que la carpeta 'data' existe en la raíz del proyecto (TASK1)." << std::endl;
        // Imprimimos las rutas intentadas para depuración
        std::cerr << "Rutas intentadas (desde CWD): " << paths_to_try[0] << ", " << paths_to_try[1] << ", " << paths_to_try[2] << std::endl;
    }
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        std::cerr << "Uso: " << argv[0] << " <tamaño_N> <num_ejecuciones>" << std::endl;
        return 1;
    }

    int n = std::atoi(argv[1]);
    int numRuns = std::atoi(argv[2]);

    std::vector<std::vector<double>> a, b, c;

    std::srand(std::time(0)); 
    initialize_matrices(n, a, b); 

    std::cout << "C++ Benchmark - N=" << n << ", Runs=" << numRuns << std::endl;

    double totalTime = 0.0;

    for (int run = 0; run < numRuns; run++) {
        double start = wtime();
        
        multiply(n, a, b, c); 

        double stop = wtime();
        double runTime = stop - start;
        totalTime += runTime;
    }

    double averageTime = totalTime / numRuns;
    std::cout << "Tiempo promedio (segundos): " << std::fixed << std::setprecision(6) << averageTime << std::endl;

    saveData(n, numRuns, averageTime);

    return 0;
}
