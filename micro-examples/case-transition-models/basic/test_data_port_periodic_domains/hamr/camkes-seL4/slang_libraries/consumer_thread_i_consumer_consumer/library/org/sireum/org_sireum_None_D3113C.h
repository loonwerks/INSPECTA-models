#ifndef SIREUM_H_org_sireum_None_D3113C
#define SIREUM_H_org_sireum_None_D3113C

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]


B None_D3113C__eq(None_D3113C this, None_D3113C other);
inline B None_D3113C__ne(None_D3113C this, None_D3113C other) {
  return !None_D3113C__eq(this, other);
};
B None_D3113C__equiv(None_D3113C this, None_D3113C other);
inline B None_D3113C__inequiv(None_D3113C this, None_D3113C other) {
  return !None_D3113C__equiv(this, other);
};
void None_D3113C_string_(STACK_FRAME String result, None_D3113C this);
void None_D3113C_cprint(None_D3113C this, B isOut);

inline B None_D3113C__is(STACK_FRAME void* this) {
  return ((None_D3113C) this)->type == TNone_D3113C;
}

inline None_D3113C None_D3113C__as(STACK_FRAME void *this) {
  if (None_D3113C__is(CALLER this)) return (None_D3113C) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api].");
  abort();
}

void None_D3113C_apply(STACK_FRAME None_D3113C this);

#ifdef __cplusplus
}
#endif

#endif