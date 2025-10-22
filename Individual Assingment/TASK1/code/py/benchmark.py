import sys # que hace esta libreria: manejo de argumentos de linea de comandos, salida de errores, etc.
from time import time # medir tiempo de ejecucion, funcion time()
import matrix_multiplier as mm # importar el modulo de multiplicacion de matrices

def benchmark():
    if len(sys.argv) != 3: # verificar que se pasen exactamente 2 argumentos, el tamaño N y el número de ejecuciones, además del nombre del script
        print("Uso: python benchmark.py <tamaño_N> <num_ejecuciones>", file=sys.stderr)
        sys.exit(1)

    try:
        n = int(sys.argv[1])
        num_runs = int(sys.argv[2])
    except ValueError:
        print("Los argumentos deben ser números enteros.", file=sys.stderr) # mensaje de error si los argumentos no son enteros, y se imprime en stderr
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
        
        # print(f"Run {run + 1}: {run_time:.6f} s")

    average_time = total_time / num_runs
    print(f"Tiempo promedio (segundos): {average_time:.6f}")
    
    # print(f"C[0][0] para verificación: {C[0][0]:.6f}")

if __name__ == "__main__":
    benchmark()