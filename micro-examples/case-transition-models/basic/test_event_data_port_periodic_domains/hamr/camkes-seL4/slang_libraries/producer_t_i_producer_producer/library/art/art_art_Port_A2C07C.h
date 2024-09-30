#ifndef SIREUM_H_art_art_Port_A2C07C
#define SIREUM_H_art_art_Port_A2C07C

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// art.Port[S8]

#define art_Port_A2C07C_id_(this) ((this)->id)
#define art_Port_A2C07C_name_(this) ((String) &(this)->name)
#define art_Port_A2C07C_mode_(this) ((this)->mode)

B art_Port_A2C07C__eq(art_Port_A2C07C this, art_Port_A2C07C other);
inline B art_Port_A2C07C__ne(art_Port_A2C07C this, art_Port_A2C07C other) {
  return !art_Port_A2C07C__eq(this, other);
};
B art_Port_A2C07C__equiv(art_Port_A2C07C this, art_Port_A2C07C other);
inline B art_Port_A2C07C__inequiv(art_Port_A2C07C this, art_Port_A2C07C other) {
  return !art_Port_A2C07C__equiv(this, other);
};
void art_Port_A2C07C_string_(STACK_FRAME String result, art_Port_A2C07C this);
void art_Port_A2C07C_cprint(art_Port_A2C07C this, B isOut);

inline B art_Port_A2C07C__is(STACK_FRAME void* this) {
  return ((art_Port_A2C07C) this)->type == Tart_Port_A2C07C;
}

inline art_Port_A2C07C art_Port_A2C07C__as(STACK_FRAME void *this) {
  if (art_Port_A2C07C__is(CALLER this)) return (art_Port_A2C07C) this;
  sfAbortImpl(CALLER "Invalid cast to art.Port[S8].");
  abort();
}

void art_Port_A2C07C_apply(STACK_FRAME art_Port_A2C07C this, art_Art_PortId id, String name, art_PortMode_Type mode);

#ifdef __cplusplus
}
#endif

#endif