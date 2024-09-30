#ifndef SIREUM_H_org_sireum_Some_A8A5E0
#define SIREUM_H_org_sireum_Some_A8A5E0

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]

#define Some_A8A5E0_value_(this) ((base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &(this)->value)

B Some_A8A5E0__eq(Some_A8A5E0 this, Some_A8A5E0 other);
inline B Some_A8A5E0__ne(Some_A8A5E0 this, Some_A8A5E0 other) {
  return !Some_A8A5E0__eq(this, other);
};
B Some_A8A5E0__equiv(Some_A8A5E0 this, Some_A8A5E0 other);
inline B Some_A8A5E0__inequiv(Some_A8A5E0 this, Some_A8A5E0 other) {
  return !Some_A8A5E0__equiv(this, other);
};
void Some_A8A5E0_string_(STACK_FRAME String result, Some_A8A5E0 this);
void Some_A8A5E0_cprint(Some_A8A5E0 this, B isOut);

inline B Some_A8A5E0__is(STACK_FRAME void* this) {
  return ((Some_A8A5E0) this)->type == TSome_A8A5E0;
}

inline Some_A8A5E0 Some_A8A5E0__as(STACK_FRAME void *this) {
  if (Some_A8A5E0__is(CALLER this)) return (Some_A8A5E0) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_port_periodic_domains.producer_t_i_Operational_Api].");
  abort();
}

void Some_A8A5E0_apply(STACK_FRAME Some_A8A5E0 this, base_test_event_port_periodic_domains_producer_t_i_Operational_Api value);

#ifdef __cplusplus
}
#endif

#endif