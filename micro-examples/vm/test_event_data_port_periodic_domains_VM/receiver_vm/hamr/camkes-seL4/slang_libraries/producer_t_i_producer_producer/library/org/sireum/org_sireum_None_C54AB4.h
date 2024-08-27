#ifndef SIREUM_H_org_sireum_None_C54AB4
#define SIREUM_H_org_sireum_None_C54AB4

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]


B None_C54AB4__eq(None_C54AB4 this, None_C54AB4 other);
inline B None_C54AB4__ne(None_C54AB4 this, None_C54AB4 other) {
  return !None_C54AB4__eq(this, other);
};
B None_C54AB4__equiv(None_C54AB4 this, None_C54AB4 other);
inline B None_C54AB4__inequiv(None_C54AB4 this, None_C54AB4 other) {
  return !None_C54AB4__equiv(this, other);
};
void None_C54AB4_string_(STACK_FRAME String result, None_C54AB4 this);
void None_C54AB4_cprint(None_C54AB4 this, B isOut);

inline B None_C54AB4__is(STACK_FRAME void* this) {
  return ((None_C54AB4) this)->type == TNone_C54AB4;
}

inline None_C54AB4 None_C54AB4__as(STACK_FRAME void *this) {
  if (None_C54AB4__is(CALLER this)) return (None_C54AB4) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api].");
  abort();
}

void None_C54AB4_apply(STACK_FRAME None_C54AB4 this);

#ifdef __cplusplus
}
#endif

#endif