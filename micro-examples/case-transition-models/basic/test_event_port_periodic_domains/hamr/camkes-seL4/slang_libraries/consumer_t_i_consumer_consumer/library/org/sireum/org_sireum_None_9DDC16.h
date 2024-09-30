#ifndef SIREUM_H_org_sireum_None_9DDC16
#define SIREUM_H_org_sireum_None_9DDC16

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]


B None_9DDC16__eq(None_9DDC16 this, None_9DDC16 other);
inline B None_9DDC16__ne(None_9DDC16 this, None_9DDC16 other) {
  return !None_9DDC16__eq(this, other);
};
B None_9DDC16__equiv(None_9DDC16 this, None_9DDC16 other);
inline B None_9DDC16__inequiv(None_9DDC16 this, None_9DDC16 other) {
  return !None_9DDC16__equiv(this, other);
};
void None_9DDC16_string_(STACK_FRAME String result, None_9DDC16 this);
void None_9DDC16_cprint(None_9DDC16 this, B isOut);

inline B None_9DDC16__is(STACK_FRAME void* this) {
  return ((None_9DDC16) this)->type == TNone_9DDC16;
}

inline None_9DDC16 None_9DDC16__as(STACK_FRAME void *this) {
  if (None_9DDC16__is(CALLER this)) return (None_9DDC16) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api].");
  abort();
}

void None_9DDC16_apply(STACK_FRAME None_9DDC16 this);

#ifdef __cplusplus
}
#endif

#endif