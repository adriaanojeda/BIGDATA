#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>
#include "matrix_multiplier.h"

double wtime() {
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec / 1000000.0;
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Uso: %s <tamaño_N> <num_ejecuciones>\n", argv[0]);
        return 1;
    }

    int n = atoi(argv[1]);
    int numRuns = atoi(argv[2]);

    double (*a)[n] = malloc(n * n * sizeof(double));
    double (*b)[n] = malloc(n * n * sizeof(double));
    double (*c)[n] = malloc(n * n * sizeof(double));

    if (!a || !b || !c) {
        perror("Fallo en la asignación de memoria");
        return 1;
    }

    srand(time(NULL)); 
    initialize_matrices(n, a, b); 

    printf("C Benchmark - N=%d, Runs=%d\n", n, numRuns);

    double totalTime = 0.0;

    for (int run = 0; run < numRuns; run++) {
        double start = wtime();
        
        multiply(n, a, b, c); 

        double stop = wtime();
        double runTime = stop - start;
        totalTime += runTime;
    }

    double averageTime = totalTime / numRuns;
    printf("Tiempo promedio (segundos): %.6f\n", averageTime);

    free(a);
    free(b);
    free(c);

    return 0;
}