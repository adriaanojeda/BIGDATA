#ifndef MATRIX_MULTIPLIER_HPP
#define MATRIX_MULTIPLIER_HPP

#include <vector>

void initialize_matrices(int n, std::vector<std::vector<double>>& a, std::vector<std::vector<double>>& b);
void multiply(int n, const std::vector<std::vector<double>>& a, const std::vector<std::vector<double>>& b, std::vector<std::vector<double>>& c);

#endif