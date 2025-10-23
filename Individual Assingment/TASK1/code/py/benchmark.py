import sys 
from time import time
import matrix_multiplier as mm 
import os 

def save_data(n, num_runs, average_time):
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    data_dir = os.path.join(script_dir, "..", "..", "data")
    
    file_path = os.path.join(data_dir, "results_py.csv")

    try:
        if not os.path.exists(data_dir):
            os.makedirs(data_dir)

        with open(file_path, "a") as f:
            f.write(f"Python,{n},{num_runs},{average_time:.6f}\n")
        print(f"Resultado guardado en {file_path}")
    except IOError as e:
        print(f"ADVERTENCIA: No se pudo guardar el archivo de datos. Error: {e}", file=sys.stderr)

def benchmark():
    if len(sys.argv) != 3:
        print("Uso: python benchmark.py <tamaño_N> <num_ejecuciones>", file=sys.stderr)
        sys.exit(1)

    try:
        n = int(sys.argv[1])
        num_runs = int(sys.argv[2])
    except ValueError:
        print("Los argumentos deben ser números enteros.", file=sys.stderr)
        sys.exit(1)

    A, B, C = mm.initialize_matrices(n)

    print(f"Python Benchmark - N={n}, Runs={num_runs}")
    
    total_time = 0.0

    for run in range(num_runs):
        start = time()
        
        mm.multiply(n, A, B, C) 

        end = time()
        run_time = end - start
        total_time += run_time
        
    average_time = total_time / num_runs
    print(f"Tiempo promedio (segundos): {average_time:.6f}")
    
    save_data(n, num_runs, average_time)

if __name__ == "__main__":
    benchmark()