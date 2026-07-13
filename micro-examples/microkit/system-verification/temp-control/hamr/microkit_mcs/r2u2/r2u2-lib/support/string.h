// Minimal string.h for freestanding seL4/Microkit builds (R2U2 aux support).
// Implementations come from the HAMR-generated util.c, which is linked into
// every C component ELF via $(UTIL_OBJS).
#ifndef AUX_SUPPORT_STRING_H
#define AUX_SUPPORT_STRING_H
#include <stddef.h>
void *memcpy(void *restrict dest, const void *restrict src, size_t n);
void *memset(void *dest, int c, size_t n);
#endif
