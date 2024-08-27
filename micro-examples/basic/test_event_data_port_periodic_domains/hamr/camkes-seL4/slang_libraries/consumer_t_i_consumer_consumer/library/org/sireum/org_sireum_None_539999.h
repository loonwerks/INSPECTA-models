#ifndef SIREUM_H_org_sireum_None_539999
#define SIREUM_H_org_sireum_None_539999

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]


B None_539999__eq(None_539999 this, None_539999 other);
inline B None_539999__ne(None_539999 this, None_539999 other) {
  return !None_539999__eq(this, other);
};
B None_539999__equiv(None_539999 this, None_539999 other);
inline B None_539999__inequiv(None_539999 this, None_539999 other) {
  return !None_539999__equiv(this, other);
};
void None_539999_string_(STACK_FRAME String result, None_539999 this);
void None_539999_cprint(None_539999 this, B isOut);

inline B None_539999__is(STACK_FRAME void* this) {
  return ((None_539999) this)->type == TNone_539999;
}

inline None_539999 None_539999__as(STACK_FRAME void *this) {
  if (None_539999__is(CALLER this)) return (None_539999) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api].");
  abort();
}

void None_539999_apply(STACK_FRAME None_539999 this);

#ifdef __cplusplus
}
#endif

#endif