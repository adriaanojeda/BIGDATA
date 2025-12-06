Tarea Individual 1: Multiplicación de Matrices - Línea Base ($O(N^3)$)

Este repositorio contiene la implementación del algoritmo básico de multiplicación de matrices en C++, Java y Python. El objetivo de la Tarea 1 es realizar un estudio de rendimiento (benchmark) comparativo entre los tres lenguajes, enfocándose en cómo el tiempo de ejecución escala con el tamaño de la matriz ($N$).

Estructura del Proyecto

El proyecto sigue el principio de Separación de Intereses:

La lógica de la multiplicación ($O(N^3)$) reside en los archivos \texttt{matrix_multiplier.*}.

La lógica de prueba, temporización y persistencia de datos reside en los archivos \texttt{benchmark.*}.

/BIGDATA (Raíz del Proyecto)
|-- /code
|   |-- /c++        (C++: Implementación y Compilación)
|   |-- /java       (Java: Implementación y Compilación)
|   |-- /py         (Python: Scripts)
|-- /data           (Contiene los archivos CSV de resultados)
|-- README.md       (Este archivo)
|-- Task1.pdf       (Informe final LaTeX)


Parámetros del Experimento

Para realizar las pruebas de escalabilidad requeridas por la ULPGC, debes ejecutar cada programa proporcionando dos argumentos clave:

\textless{}tamaño_N\textgreater{} (N): El tamaño de la matriz cuadrada ($N \times N$).

\textless{}num_ejecuciones\textgreater{} (R): El número de repeticiones (Runs) para promediar el tiempo y asegurar la fiabilidad.

Se recomienda ejecutar la siguiente secuencia de 4 experimentos para cada lenguaje:

N (Tamaño de Matriz)

R (Repeticiones)

Carga de Trabajo ($\propto N^3$)

100 - 10 Baja

400 - 5 Media-Baja

800 - 3 Media-Alta

1024 - 3 Alta (Aprox. $1.07 \times 10^9$ Ops)

Guía de Ejecución

IMPORTANTE: Todos los comandos de ejecución deben realizarse desde la raíz del proyecto (/BIGDATA o TASK1) para que el código pueda encontrar la carpeta \texttt{data/} correctamente.

1. C++ (Requiere Compilación con Optimización)

Paso 1.1: Compilación (¡Obligatorio usar -O3!)

# Navegar y compilar con optimización -O3
cd code/c++
g++ -o benchmark_cpp benchmark.cpp matrix_multiplier.cpp -I. -O3
cd ../..


Paso 1.2: Ejecución (desde la Raíz)

# Ejecutar para N=1024, 3 repeticiones
.\code\c++\benchmark_cpp 1024 3


El resultado se añadirá a \texttt{data/results_cpp.csv}.

2. Java (Requiere Compilación)

Paso 2.1: Compilación

# Navegar y compilar
cd code/java
javac *.java
cd ../..


Paso 2.2: Ejecución (desde la Raíz)

# Ejecutar para N=800, 3 repeticiones
java -cp code/java Benchmark 800 3


El resultado se añadirá a \texttt{data/results_java.csv}.

3. Python (Script)

Ejecución (desde la Raíz)

# Ejecutar para N=400, 5 repeticiones
python code/py/benchmark.py 400 5


El resultado se añadirá a \texttt{data/results_py.csv}.