import sys
from collections import defaultdict
from itertools import combinations

INPUT_FILE = "input_big_items.txt"
OUTPUT_FILE = "resultado_items.txt"
MIN_SUPPORT = 2 # Mínimo de veces que deben aparecer juntos

def mapper(line):
    items = line.strip().split(',')
    items.sort() # Ordenar para evitar duplicados (a,b) vs (b,a)
    
    emitted = []
    # Generar todos los pares posibles
    for pair in combinations(items, 2):
        emitted.append((pair, 1))
    return emitted

def reducer(key, counts):
    total = sum(counts)
    if total >= MIN_SUPPORT:
        return total
    return None

def run_mapreduce():
    print("--- Iniciando Frequent Item Set Nativo ---")
    
    shuffle_store = defaultdict(list)
    
    print("Ejecutando Mappers...")
    try:
        with open(INPUT_FILE, 'r') as f:
            for line in f:
                mapped_items = mapper(line)
                for key, value in mapped_items:
                    shuffle_store[key].append(value)
    except FileNotFoundError:
        print(f"Error: No se encuentra {INPUT_FILE}")
        return

    print("Ejecutando Reducers...")
    
    with open(OUTPUT_FILE, 'w') as out:
        # Ordenar por cantidad de apariciones (descendente) para que se vea mejor
        results = []
        for key in shuffle_store:
            total = reducer(key, shuffle_store[key])
            if total:
                results.append((key, total))
        
        # Guardar ordenado por frecuencia
        results.sort(key=lambda x: x[1], reverse=True)
        
        for key, total in results:
            out.write(f"{list(key)}\t{total}\n")

    print(f"¡Hecho! Resultados en {OUTPUT_FILE}")

if __name__ == '__main__':
    run_mapreduce()