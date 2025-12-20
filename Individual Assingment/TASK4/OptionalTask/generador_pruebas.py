import random

def generar_matriz_dispersa(filename, N, densidad=0.2):
    """
    Genera dos matrices A y B de tamaño N x N.
    densidad: Porcentaje de celdas que no son cero (0.2 = 20% lleno).
    """
    print(f"Generando matrices A y B de {N}x{N} en {filename}...")
    with open(filename, 'w') as f:
        # Generar Matriz A
        for r in range(N):
            for c in range(N):
                if random.random() < densidad: # Solo escribimos si supera la probabilidad (dispersión)
                    val = round(random.uniform(1, 10), 2)
                    f.write(f"A,{r},{c},{val}\n")
        
        # Generar Matriz B
        for r in range(N):
            for c in range(N):
                if random.random() < densidad:
                    val = round(random.uniform(1, 10), 2)
                    f.write(f"B,{r},{c},{val}\n")
    print("¡Matrices generadas!")

def generar_transacciones(filename, num_transacciones, num_items):
    """
    Genera una lista de compras para el problema opcional.
    """
    print(f"Generando {num_transacciones} transacciones en {filename}...")
    items_disponibles = [f"Item_{i}" for i in range(num_items)]
    
    with open(filename, 'w') as f:
        for _ in range(num_transacciones):
            # Cada transacción tiene entre 2 y 5 items aleatorios
            k = random.randint(2, 5)
            compra = random.sample(items_disponibles, k)
            f.write(",".join(compra) + "\n")
    print("¡Transacciones generadas!")

if __name__ == "__main__":
    # 1. Datos para Matriz (N=100)
    generar_matriz_dispersa("input_big_matrix.txt", N=100, densidad=0.05)
    
    # 2. Datos para Frequent Items (1000 transacciones)
    generar_transacciones("input_big_items.txt", num_transacciones=1000, num_items=50)