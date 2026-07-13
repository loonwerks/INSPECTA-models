// Minimal stdio.h for freestanding seL4/Microkit builds (R2U2 aux support).
// R2U2 only uses FILE*/fprintf for optional verdict logging (monitor.out_file),
// which is left NULL on Microkit; verdicts are delivered via monitor.out_func.
#ifndef AUX_SUPPORT_STDIO_H
#define AUX_SUPPORT_STDIO_H
typedef struct aux_support_file FILE;
int fprintf(FILE *stream, const char *format, ...);
#endif
