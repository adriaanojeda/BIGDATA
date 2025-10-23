#include <cstdlib>
#include <ctime>
#include "matrix_multiplier.hpp"

void initialize_matrices(int n, std::vector<std::vector<double>>& a, std::vector<std::vector<double>>& b) {
    a.assign(n, std::vector<double>(n));
    b.assign(n, std::vector<double>(n));
    
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            a[i][j] = (double) std::rand() / RAND_MAX;
            b[i][j] = (double) std::rand() / RAND_MAX;
        }
    }
}

void multiply(int n, const std::vector<std::vector<double>>& a, const std::vector<std::vector<double>>& b, std::vector<std::vector<double>>& c) {
    c.assign(n, std::vector<double>(n, 0.0));
    
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            double sum = 0.0;
            for (int k = 0; k < n; ++k) {
                sum += a[i][k] * b[k][j];
            }
            c[i][j] = sum;
        }
    }
}