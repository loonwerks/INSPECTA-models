#ifndef SIREUM_H_org_sireum_None_608F86
#define SIREUM_H_org_sireum_None_608F86

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]


B None_608F86__eq(None_608F86 this, None_608F86 other);
inline B None_608F86__ne(None_608F86 this, None_608F86 other) {
  return !None_608F86__eq(this, other);
};
B None_608F86__equiv(None_608F86 this, None_608F86 other);
inline B None_608F86__inequiv(None_608F86 this, None_608F86 other) {
  return !None_608F86__equiv(this, other);
};
void None_608F86_string_(STACK_FRAME String result, None_608F86 this);
void None_608F86_cprint(None_608F86 this, B isOut);

inline B None_608F86__is(STACK_FRAME void* this) {
  return ((None_608F86) this)->type == TNone_608F86;
}

inline None_608F86 None_608F86__as(STACK_FRAME void *this) {
  if (None_608F86__is(CALLER this)) return (None_608F86) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api].");
  abort();
}

void None_608F86_apply(STACK_FRAME None_608F86 this);

#ifdef __cplusplus
}
#endif

#endif