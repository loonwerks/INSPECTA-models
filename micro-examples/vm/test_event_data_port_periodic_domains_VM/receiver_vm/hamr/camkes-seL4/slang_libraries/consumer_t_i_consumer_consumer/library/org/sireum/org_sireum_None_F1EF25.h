#ifndef SIREUM_H_org_sireum_None_F1EF25
#define SIREUM_H_org_sireum_None_F1EF25

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// None[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]


B None_F1EF25__eq(None_F1EF25 this, None_F1EF25 other);
inline B None_F1EF25__ne(None_F1EF25 this, None_F1EF25 other) {
  return !None_F1EF25__eq(this, other);
};
B None_F1EF25__equiv(None_F1EF25 this, None_F1EF25 other);
inline B None_F1EF25__inequiv(None_F1EF25 this, None_F1EF25 other) {
  return !None_F1EF25__equiv(this, other);
};
void None_F1EF25_string_(STACK_FRAME String result, None_F1EF25 this);
void None_F1EF25_cprint(None_F1EF25 this, B isOut);

inline B None_F1EF25__is(STACK_FRAME void* this) {
  return ((None_F1EF25) this)->type == TNone_F1EF25;
}

inline None_F1EF25 None_F1EF25__as(STACK_FRAME void *this) {
  if (None_F1EF25__is(CALLER this)) return (None_F1EF25) this;
  sfAbortImpl(CALLER "Invalid cast to None[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api].");
  abort();
}

void None_F1EF25_apply(STACK_FRAME None_F1EF25 this);

#ifdef __cplusplus
}
#endif

#endif