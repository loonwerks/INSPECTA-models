// Minimal math.h for freestanding seL4/Microkit builds (R2U2 aux support).
// Only the functions referenced by R2U2's booleanizer are declared; stub
// implementations live in aux_support.c and are never executed unless a
// specification uses the pow/sqrt operators.
#ifndef AUX_SUPPORT_MATH_H
#define AUX_SUPPORT_MATH_H
double pow(double base, double exponent);
double sqrt(double x);
#endif
