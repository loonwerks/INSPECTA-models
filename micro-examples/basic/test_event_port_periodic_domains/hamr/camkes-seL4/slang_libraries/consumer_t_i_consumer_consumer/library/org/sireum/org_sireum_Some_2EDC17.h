#ifndef SIREUM_H_org_sireum_Some_2EDC17
#define SIREUM_H_org_sireum_Some_2EDC17

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]

#define Some_2EDC17_value_(this) ((base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &(this)->value)

B Some_2EDC17__eq(Some_2EDC17 this, Some_2EDC17 other);
inline B Some_2EDC17__ne(Some_2EDC17 this, Some_2EDC17 other) {
  return !Some_2EDC17__eq(this, other);
};
B Some_2EDC17__equiv(Some_2EDC17 this, Some_2EDC17 other);
inline B Some_2EDC17__inequiv(Some_2EDC17 this, Some_2EDC17 other) {
  return !Some_2EDC17__equiv(this, other);
};
void Some_2EDC17_string_(STACK_FRAME String result, Some_2EDC17 this);
void Some_2EDC17_cprint(Some_2EDC17 this, B isOut);

inline B Some_2EDC17__is(STACK_FRAME void* this) {
  return ((Some_2EDC17) this)->type == TSome_2EDC17;
}

inline Some_2EDC17 Some_2EDC17__as(STACK_FRAME void *this) {
  if (Some_2EDC17__is(CALLER this)) return (Some_2EDC17) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api].");
  abort();
}

void Some_2EDC17_apply(STACK_FRAME Some_2EDC17 this, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api value);

#ifdef __cplusplus
}
#endif

#endif