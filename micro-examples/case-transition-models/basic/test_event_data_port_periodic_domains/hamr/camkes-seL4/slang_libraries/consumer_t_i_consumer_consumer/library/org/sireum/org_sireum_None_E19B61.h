#ifndef SIREUM_H_org_sireum_None_E19B61
#define SIREUM_H_org_sireum_None_E19B61

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[S8]


B None_E19B61__eq(None_E19B61 this, None_E19B61 other);
inline B None_E19B61__ne(None_E19B61 this, None_E19B61 other) {
  return !None_E19B61__eq(this, other);
};
B None_E19B61__equiv(None_E19B61 this, None_E19B61 other);
inline B None_E19B61__inequiv(None_E19B61 this, None_E19B61 other) {
  return !None_E19B61__equiv(this, other);
};
void None_E19B61_string_(STACK_FRAME String result, None_E19B61 this);
void None_E19B61_cprint(None_E19B61 this, B isOut);

inline B None_E19B61__is(STACK_FRAME void* this) {
  return ((None_E19B61) this)->type == TNone_E19B61;
}

inline None_E19B61 None_E19B61__as(STACK_FRAME void *this) {
  if (None_E19B61__is(CALLER this)) return (None_E19B61) this;
  sfAbortImpl(CALLER "Invalid cast to None[S8].");
  abort();
}

void None_E19B61_apply(STACK_FRAME None_E19B61 this);

#ifdef __cplusplus
}
#endif

#endif