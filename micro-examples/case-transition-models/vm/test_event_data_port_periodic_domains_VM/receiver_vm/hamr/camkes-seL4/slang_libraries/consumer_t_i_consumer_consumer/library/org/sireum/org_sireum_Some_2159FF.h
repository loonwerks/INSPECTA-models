#ifndef SIREUM_H_org_sireum_Some_2159FF
#define SIREUM_H_org_sireum_Some_2159FF

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]

#define Some_2159FF_value_(this) ((base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &(this)->value)

B Some_2159FF__eq(Some_2159FF this, Some_2159FF other);
inline B Some_2159FF__ne(Some_2159FF this, Some_2159FF other) {
  return !Some_2159FF__eq(this, other);
};
B Some_2159FF__equiv(Some_2159FF this, Some_2159FF other);
inline B Some_2159FF__inequiv(Some_2159FF this, Some_2159FF other) {
  return !Some_2159FF__equiv(this, other);
};
void Some_2159FF_string_(STACK_FRAME String result, Some_2159FF this);
void Some_2159FF_cprint(Some_2159FF this, B isOut);

inline B Some_2159FF__is(STACK_FRAME void* this) {
  return ((Some_2159FF) this)->type == TSome_2159FF;
}

inline Some_2159FF Some_2159FF__as(STACK_FRAME void *this) {
  if (Some_2159FF__is(CALLER this)) return (Some_2159FF) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api].");
  abort();
}

void Some_2159FF_apply(STACK_FRAME Some_2159FF this, base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api value);

#ifdef __cplusplus
}
#endif

#endif