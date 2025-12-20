import sys
from collections import defaultdict

# --- CONFIGURACIÓN ---
N = 100  # Tamaño de la matriz (ajustar según la prueba)
INPUT_FILE = "input_big_matrix.txt"
OUTPUT_FILE = "resultado_matrix.txt"

def mapper(line):
    """
    Fase MAP: Lee una línea y emite pares clave-valor.
    """
    line = line.strip()
    if not line: return []
    
    parts = line.split(',')
    if len(parts) != 4: return []

    matrix = parts[0]
    r = int(parts[1])
    c = int(parts[2])
    val = float(parts[3])

    emitted = []
    if matrix == 'A':
        # A[r,c] se necesita para calcular C[r, 0], C[r, 1]... C[r, N-1]
        for i in range(N):
            key = (r, i)
            value = ('A', c, val)
            emitted.append((key, value))
    elif matrix == 'B':
        # B[r,c] se necesita para calcular C[0, c], C[1, c]... C[N-1, c]
        # NOTA: En el archivo B, 'r' es la fila y 'c' la columna original.
        # Para multiplicar A*B, el índice común es la columna de A y fila de B.
        for i in range(N):
            key = (i, c)
            value = ('B', r, val)
            emitted.append((key, value))
    
    return emitted

def reducer(key, values):
    """
    Fase REDUCE: Recibe una clave (celda de C) y lista de valores, calcula el resultado.
    """
    vals_a = {}
    vals_b = {}

    for mat, k, val in values:
        if mat == 'A':
            vals_a[k] = val
        elif mat == 'B':
            vals_b[k] = val

    result = 0.0
    # Producto punto: sum(A[i,k] * B[k,j])
    for k in vals_a:
        if k in vals_b:
            result += vals_a[k] * vals_b[k]

    return result

def run_mapreduce():
    print(f"--- Iniciando MapReduce Nativo (N={N}) ---")
    
    # 1. SHUFFLE & SORT (Simulado con un diccionario)
    # Agrupa todos los valores que van a la misma clave (Reducer)
    shuffle_store = defaultdict(list)
    
    print("Ejecutando Mappers...")
    try:
        with open(INPUT_FILE, 'r') as f:
            for line in f:
                # Ejecutar Mapper
                mapped_items = mapper(line)
                # Fase de agrupación (Shuffle)
                for key, value in mapped_items:
                    shuffle_store[key].append(value)
    except FileNotFoundError:
        print(f"Error: No se encuentra el archivo {INPUT_FILE}")
        return

    print(f"Mappers terminados. Ejecutando Reducers en {len(shuffle_store)} claves...")
    
    # 2. REDUCE
    with open(OUTPUT_FILE, 'w') as out:
        # Ordenamos las claves para que el output salga limpio (0,0), (0,1)...
        for key in sorted(shuffle_store.keys()):
            values = shuffle_store[key]
            result = reducer(key, values)
            
            if result != 0:
                out.write(f"{list(key)}\t{result:.2f}\n")
    
    print(f"¡Hecho! Resultados guardados en {OUTPUT_FILE}")

if __name__ == '__main__':
    run_mapreduce()