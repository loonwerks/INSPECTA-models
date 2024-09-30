#ifndef SIREUM_H_org_sireum_None_F1003F
#define SIREUM_H_org_sireum_None_F1003F

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]


B None_F1003F__eq(None_F1003F this, None_F1003F other);
inline B None_F1003F__ne(None_F1003F this, None_F1003F other) {
  return !None_F1003F__eq(this, other);
};
B None_F1003F__equiv(None_F1003F this, None_F1003F other);
inline B None_F1003F__inequiv(None_F1003F this, None_F1003F other) {
  return !None_F1003F__equiv(this, other);
};
void None_F1003F_string_(STACK_FRAME String result, None_F1003F this);
void None_F1003F_cprint(None_F1003F this, B isOut);

inline B None_F1003F__is(STACK_FRAME void* this) {
  return ((None_F1003F) this)->type == TNone_F1003F;
}

inline None_F1003F None_F1003F__as(STACK_FRAME void *this) {
  if (None_F1003F__is(CALLER this)) return (None_F1003F) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api].");
  abort();
}

void None_F1003F_apply(STACK_FRAME None_F1003F this);

#ifdef __cplusplus
}
#endif

#endif