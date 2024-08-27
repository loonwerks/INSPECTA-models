#ifndef SIREUM_H_org_sireum_None_5A630D
#define SIREUM_H_org_sireum_None_5A630D

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]


B None_5A630D__eq(None_5A630D this, None_5A630D other);
inline B None_5A630D__ne(None_5A630D this, None_5A630D other) {
  return !None_5A630D__eq(this, other);
};
B None_5A630D__equiv(None_5A630D this, None_5A630D other);
inline B None_5A630D__inequiv(None_5A630D this, None_5A630D other) {
  return !None_5A630D__equiv(this, other);
};
void None_5A630D_string_(STACK_FRAME String result, None_5A630D this);
void None_5A630D_cprint(None_5A630D this, B isOut);

inline B None_5A630D__is(STACK_FRAME void* this) {
  return ((None_5A630D) this)->type == TNone_5A630D;
}

inline None_5A630D None_5A630D__as(STACK_FRAME void *this) {
  if (None_5A630D__is(CALLER this)) return (None_5A630D) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api].");
  abort();
}

void None_5A630D_apply(STACK_FRAME None_5A630D this);

#ifdef __cplusplus
}
#endif

#endif