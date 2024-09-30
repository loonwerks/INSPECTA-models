#ifndef SIREUM_H_org_sireum_None_F97B7F
#define SIREUM_H_org_sireum_None_F97B7F

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]


B None_F97B7F__eq(None_F97B7F this, None_F97B7F other);
inline B None_F97B7F__ne(None_F97B7F this, None_F97B7F other) {
  return !None_F97B7F__eq(this, other);
};
B None_F97B7F__equiv(None_F97B7F this, None_F97B7F other);
inline B None_F97B7F__inequiv(None_F97B7F this, None_F97B7F other) {
  return !None_F97B7F__equiv(this, other);
};
void None_F97B7F_string_(STACK_FRAME String result, None_F97B7F this);
void None_F97B7F_cprint(None_F97B7F this, B isOut);

inline B None_F97B7F__is(STACK_FRAME void* this) {
  return ((None_F97B7F) this)->type == TNone_F97B7F;
}

inline None_F97B7F None_F97B7F__as(STACK_FRAME void *this) {
  if (None_F97B7F__is(CALLER this)) return (None_F97B7F) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api].");
  abort();
}

void None_F97B7F_apply(STACK_FRAME None_F97B7F this);

#ifdef __cplusplus
}
#endif

#endif