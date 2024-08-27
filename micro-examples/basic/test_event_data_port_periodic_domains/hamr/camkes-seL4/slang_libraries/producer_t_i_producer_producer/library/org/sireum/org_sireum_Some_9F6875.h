#ifndef SIREUM_H_org_sireum_Some_9F6875
#define SIREUM_H_org_sireum_Some_9F6875

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]

#define Some_9F6875_value_(this) ((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &(this)->value)

B Some_9F6875__eq(Some_9F6875 this, Some_9F6875 other);
inline B Some_9F6875__ne(Some_9F6875 this, Some_9F6875 other) {
  return !Some_9F6875__eq(this, other);
};
B Some_9F6875__equiv(Some_9F6875 this, Some_9F6875 other);
inline B Some_9F6875__inequiv(Some_9F6875 this, Some_9F6875 other) {
  return !Some_9F6875__equiv(this, other);
};
void Some_9F6875_string_(STACK_FRAME String result, Some_9F6875 this);
void Some_9F6875_cprint(Some_9F6875 this, B isOut);

inline B Some_9F6875__is(STACK_FRAME void* this) {
  return ((Some_9F6875) this)->type == TSome_9F6875;
}

inline Some_9F6875 Some_9F6875__as(STACK_FRAME void *this) {
  if (Some_9F6875__is(CALLER this)) return (Some_9F6875) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api].");
  abort();
}

void Some_9F6875_apply(STACK_FRAME Some_9F6875 this, base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api value);

#ifdef __cplusplus
}
#endif

#endif