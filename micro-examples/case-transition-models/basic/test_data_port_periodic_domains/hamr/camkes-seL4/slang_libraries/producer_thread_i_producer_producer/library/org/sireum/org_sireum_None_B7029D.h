#ifndef SIREUM_H_org_sireum_None_B7029D
#define SIREUM_H_org_sireum_None_B7029D

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]


B None_B7029D__eq(None_B7029D this, None_B7029D other);
inline B None_B7029D__ne(None_B7029D this, None_B7029D other) {
  return !None_B7029D__eq(this, other);
};
B None_B7029D__equiv(None_B7029D this, None_B7029D other);
inline B None_B7029D__inequiv(None_B7029D this, None_B7029D other) {
  return !None_B7029D__equiv(this, other);
};
void None_B7029D_string_(STACK_FRAME String result, None_B7029D this);
void None_B7029D_cprint(None_B7029D this, B isOut);

inline B None_B7029D__is(STACK_FRAME void* this) {
  return ((None_B7029D) this)->type == TNone_B7029D;
}

inline None_B7029D None_B7029D__as(STACK_FRAME void *this) {
  if (None_B7029D__is(CALLER this)) return (None_B7029D) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api].");
  abort();
}

void None_B7029D_apply(STACK_FRAME None_B7029D this);

#ifdef __cplusplus
}
#endif

#endif