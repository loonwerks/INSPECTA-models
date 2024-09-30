#ifndef SIREUM_H_org_sireum_Some_4CD075
#define SIREUM_H_org_sireum_Some_4CD075

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]

#define Some_4CD075_value_(this) ((base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &(this)->value)

B Some_4CD075__eq(Some_4CD075 this, Some_4CD075 other);
inline B Some_4CD075__ne(Some_4CD075 this, Some_4CD075 other) {
  return !Some_4CD075__eq(this, other);
};
B Some_4CD075__equiv(Some_4CD075 this, Some_4CD075 other);
inline B Some_4CD075__inequiv(Some_4CD075 this, Some_4CD075 other) {
  return !Some_4CD075__equiv(this, other);
};
void Some_4CD075_string_(STACK_FRAME String result, Some_4CD075 this);
void Some_4CD075_cprint(Some_4CD075 this, B isOut);

inline B Some_4CD075__is(STACK_FRAME void* this) {
  return ((Some_4CD075) this)->type == TSome_4CD075;
}

inline Some_4CD075 Some_4CD075__as(STACK_FRAME void *this) {
  if (Some_4CD075__is(CALLER this)) return (Some_4CD075) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api].");
  abort();
}

void Some_4CD075_apply(STACK_FRAME Some_4CD075 this, base_test_event_port_periodic_domains_producer_t_i_Initialization_Api value);

#ifdef __cplusplus
}
#endif

#endif