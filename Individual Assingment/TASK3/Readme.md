# Task 3: Parallelization Benchmark of Matrix Multiplication

**Student:** Adrián Ojeda Viera  
**Course:** Big Data (ULPGC)  
**Date:** December 6, 2025

## Overview
This project investigates the impact of parallel computing techniques and memory optimization on Matrix Multiplication performance in Java. It compares a basic sequential implementation against several parallel approaches (Streams, Threads, Executors) and a memory-layout optimized version (simulated vectorization).

## Project Structure
```text
TASK3
├── code
│   └── java                 # Source code
│       ├── Benchmark.java             # Main runner
│       ├── MatrixMultiplier.java      # Interface
│       ├── SequentialMultiplier.java  # O(n^3) Baseline
│       ├── ParallelStreamMultiplier.java
│       ├── ThreadMultiplier.java
│       ├── AtomicMultiplier.java
│       └── VectorizedMultiplier.java  # Memory optimized (Transposed)
├── data
│   └── results_parallel.csv  # Benchmark output data
├── results
│   └── task3_results.png     # Performance graphs (Time & Speedup)
├── scripts
│   └── plots_results_task3.py # Python script for visualization
└── report
    └── Task3_Paper.pdf       # Final LaTeX report
```

## How to Run
1. Prerequisites
Java Development Kit (JDK) 8 or higher.

Python 3.x (with pandas, matplotlib, seaborn) for plotting.

2. Compilation
Navigate to the TASK3 root directory and compile the Java sources:

```Bash
javac code/java/*.java
```
3. Running the Benchmark
Execute the benchmark specifying the matrix size (N) and the number of runs per implementation. Example for N=1024 with 5 runs:

```Bash
java -cp . code.java.Benchmark 1024 5
```
Note: The results will be automatically appended to data/results_parallel.csv.

4. Generating Plots
Once data is collected, generate the performance graphs:

```Bash
python scripts/plots_results_task3.py
```
The image will be saved to results/task3_results.png.

Key Results (N=4096)
Sequential Time: ~2034s

Parallel Stream Speedup: ~9.2x (using available cores)

Vectorized Speedup: ~38.9x (Single-threaded, memory optimized)

The study demonstrates that while parallelization provides significant gains, optimizing memory access patterns (cache locality) yields the highest performance improvement for matrix multiplication.