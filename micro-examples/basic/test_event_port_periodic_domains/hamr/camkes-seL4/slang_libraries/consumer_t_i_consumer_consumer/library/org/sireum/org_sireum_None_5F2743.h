#ifndef SIREUM_H_org_sireum_None_5F2743
#define SIREUM_H_org_sireum_None_5F2743

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]


B None_5F2743__eq(None_5F2743 this, None_5F2743 other);
inline B None_5F2743__ne(None_5F2743 this, None_5F2743 other) {
  return !None_5F2743__eq(this, other);
};
B None_5F2743__equiv(None_5F2743 this, None_5F2743 other);
inline B None_5F2743__inequiv(None_5F2743 this, None_5F2743 other) {
  return !None_5F2743__equiv(this, other);
};
void None_5F2743_string_(STACK_FRAME String result, None_5F2743 this);
void None_5F2743_cprint(None_5F2743 this, B isOut);

inline B None_5F2743__is(STACK_FRAME void* this) {
  return ((None_5F2743) this)->type == TNone_5F2743;
}

inline None_5F2743 None_5F2743__as(STACK_FRAME void *this) {
  if (None_5F2743__is(CALLER this)) return (None_5F2743) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api].");
  abort();
}

void None_5F2743_apply(STACK_FRAME None_5F2743 this);

#ifdef __cplusplus
}
#endif

#endif