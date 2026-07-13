// Freestanding support stubs for the R2U2 aux library on seL4/Microkit.
// memcpy/memset are provided by the HAMR-generated util.c.
#include "stdio.h"
#include "math.h"

// R2U2 only calls fprintf when monitor.out_file is non-NULL, which is never
// the case on Microkit; this stub just satisfies the linker
int fprintf(FILE *stream, const char *format, ...) {
  (void)stream;
  (void)format;
  return 0;
}

// naive stubs for R2U2's pow/sqrt booleanizer opcodes, unused by our specs

double pow(double base, double exponent) {
  double result = 1.0;
  if (exponent < 0.0) {
    base = 1.0 / base;
    exponent = -exponent;
  }
  long long n = (long long)exponent;
  for (long long i = 0; i < n; i++) {
    result *= base;
  }
  return result;
}

double sqrt(double x) {
  if (x <= 0.0) {
    return 0.0;
  }
  double guess = x;
  for (int i = 0; i < 64; i++) {
    guess = 0.5 * (guess + x / guess);
  }
  return guess;
}
