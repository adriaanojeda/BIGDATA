import pandas as pd
import matplotlib.pyplot as plt 
import seaborn as sns
import os

# --- CORRECCIÓN 1: Rutas robustas ---
# Esto obtiene la ruta donde está ESTE script (carpeta scripts)
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
# Ahora buscamos data relativo al script, no a la consola
FILE_PATH = os.path.join(SCRIPT_DIR, "../data/results_parallel.csv")
OUTPUT_IMAGE = os.path.join(SCRIPT_DIR, "../results/task3_results.png")

def plot_results(): 
    # Normalizamos la ruta para evitar errores de barras invertidas en Windows
    file_path = os.path.normpath(FILE_PATH)
    output_image = os.path.normpath(OUTPUT_IMAGE)

    if not os.path.exists(file_path):
        print(f"Error: No se encuentra el archivo en: {file_path}")
        print("Asegúrate de haber ejecutado el Benchmark de Java primero.")
        return
    
    df = pd.read_csv(file_path)

    # --- CORRECCIÓN 2: Nombres de columnas ---
    # Tu Java guarda 'TimeSec', no 'Time_ms'.
    # Si quieres trabajar en ms para la gráfica, multiplicamos por 1000.
    if 'TimeSec' in df.columns:
        df['Time_ms'] = df['TimeSec'] * 1000
    elif 'Time_ms' not in df.columns:
        print("Error: El CSV no tiene columna 'TimeSec' ni 'Time_ms'. Cabeceras encontradas:", df.columns)
        return

    # Crear Baseline para calcular Speedup
    try:
        baseline = df[df['Method'] == 'Sequential'][['Size', 'Time_ms']].rename(columns={'Time_ms': 'Baseline_Time_ms'})   
        # Si hay varias ejecuciones para el mismo tamaño, hacemos la media antes de mezclar
        baseline = baseline.groupby('Size').mean().reset_index()
        
        # Hacemos la media de los resultados generales también por si hay varias runs
        df_avg = df.groupby(['Method', 'Size'])['Time_ms'].mean().reset_index()
        
        df_merged = df_avg.merge(baseline, on='Size') 
        df_merged['Speedup'] = df_merged['Baseline_Time_ms'] / df_merged['Time_ms']
    except Exception as e:
        print(f"Error procesando datos: {e}")
        return

    # Configuración de Gráficas
    sns.set_theme(style="whitegrid")
    fig, axes = plt.subplots(2, 1, figsize=(10, 12))

    # Gráfica 1: Tiempos
    sns.lineplot(ax=axes[0], data=df_merged, x='Size', y='Time_ms', hue='Method', marker='o', linewidth=2)
    axes[0].set_title('Matrix Multiplication Execution Time')
    axes[0].set_ylabel('Time (milliseconds)')
    axes[0].set_xlabel('Matrix Size (N)')
    axes[0].legend(title='Implementation')
    axes[0].grid(True, which="both", ls="-", alpha=0.5)

    # Gráfica 2: Speedup
    sns.lineplot(ax=axes[1], data=df_merged, x='Size', y='Speedup', hue='Method', marker='o', linewidth=2)
    
    # Línea de referencia (Speedup ideal)
    # Ajusta '4' al número de núcleos físicos de tu CPU si quieres ser preciso
    axes[1].axhline(y=4, color='r', linestyle='--', label='Ideal Speedup (4 cores)')
    
    axes[1].set_title('Speedup vs Sequential')
    axes[1].set_ylabel('Speedup Factor (X times faster)')
    axes[1].set_xlabel('Matrix Size (N)')
    axes[1].legend()
    axes[1].grid(True, which="both", ls="-", alpha=0.5)

    # Crear carpeta results si no existe
    results_dir = os.path.dirname(output_image)
    if not os.path.exists(results_dir):
        os.makedirs(results_dir)
        
    plt.tight_layout()
    plt.savefig(output_image, dpi=300)
    print(f"Gráfica guardada exitosamente en: {output_image}")
    plt.show() # Descomenta si quieres verla emergente

if __name__ == "__main__":
    plot_results()