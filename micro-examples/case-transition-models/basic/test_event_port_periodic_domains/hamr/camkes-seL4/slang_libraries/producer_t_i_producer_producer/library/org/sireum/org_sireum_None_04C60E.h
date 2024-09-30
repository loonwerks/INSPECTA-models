#ifndef SIREUM_H_org_sireum_None_04C60E
#define SIREUM_H_org_sireum_None_04C60E

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]


B None_04C60E__eq(None_04C60E this, None_04C60E other);
inline B None_04C60E__ne(None_04C60E this, None_04C60E other) {
  return !None_04C60E__eq(this, other);
};
B None_04C60E__equiv(None_04C60E this, None_04C60E other);
inline B None_04C60E__inequiv(None_04C60E this, None_04C60E other) {
  return !None_04C60E__equiv(this, other);
};
void None_04C60E_string_(STACK_FRAME String result, None_04C60E this);
void None_04C60E_cprint(None_04C60E this, B isOut);

inline B None_04C60E__is(STACK_FRAME void* this) {
  return ((None_04C60E) this)->type == TNone_04C60E;
}

inline None_04C60E None_04C60E__as(STACK_FRAME void *this) {
  if (None_04C60E__is(CALLER this)) return (None_04C60E) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_port_periodic_domains.producer_t_i_Operational_Api].");
  abort();
}

void None_04C60E_apply(STACK_FRAME None_04C60E this);

#ifdef __cplusplus
}
#endif

#endif