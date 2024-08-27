#ifndef SIREUM_H_org_sireum_Some_6B73B7
#define SIREUM_H_org_sireum_Some_6B73B7

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]

#define Some_6B73B7_value_(this) ((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &(this)->value)

B Some_6B73B7__eq(Some_6B73B7 this, Some_6B73B7 other);
inline B Some_6B73B7__ne(Some_6B73B7 this, Some_6B73B7 other) {
  return !Some_6B73B7__eq(this, other);
};
B Some_6B73B7__equiv(Some_6B73B7 this, Some_6B73B7 other);
inline B Some_6B73B7__inequiv(Some_6B73B7 this, Some_6B73B7 other) {
  return !Some_6B73B7__equiv(this, other);
};
void Some_6B73B7_string_(STACK_FRAME String result, Some_6B73B7 this);
void Some_6B73B7_cprint(Some_6B73B7 this, B isOut);

inline B Some_6B73B7__is(STACK_FRAME void* this) {
  return ((Some_6B73B7) this)->type == TSome_6B73B7;
}

inline Some_6B73B7 Some_6B73B7__as(STACK_FRAME void *this) {
  if (Some_6B73B7__is(CALLER this)) return (Some_6B73B7) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api].");
  abort();
}

void Some_6B73B7_apply(STACK_FRAME Some_6B73B7 this, base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api value);

#ifdef __cplusplus
}
#endif

#endif