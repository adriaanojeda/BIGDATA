import random

def initialize_matrices(n):
    A = [[random.random() 
          for _ in range(n)] 
          for _ in range(n)]
    B = [[random.random() 
          for _ in range(n)] 
          for _ in range(n)]
    C = [[0 
          for _ in range(n)] 
          for _ in range(n)]
    return A, B, C

def multiply(n, A, B, C):
    for i in range(n):
        for j in range(n):
            C[i][j] = 0
            for k in range(n):
                C[i][j] += A[i][k] * B[k][j]