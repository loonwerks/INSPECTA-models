#ifndef SIREUM_H_org_sireum_Some_FDB73B
#define SIREUM_H_org_sireum_Some_FDB73B

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]

#define Some_FDB73B_value_(this) ((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &(this)->value)

B Some_FDB73B__eq(Some_FDB73B this, Some_FDB73B other);
inline B Some_FDB73B__ne(Some_FDB73B this, Some_FDB73B other) {
  return !Some_FDB73B__eq(this, other);
};
B Some_FDB73B__equiv(Some_FDB73B this, Some_FDB73B other);
inline B Some_FDB73B__inequiv(Some_FDB73B this, Some_FDB73B other) {
  return !Some_FDB73B__equiv(this, other);
};
void Some_FDB73B_string_(STACK_FRAME String result, Some_FDB73B this);
void Some_FDB73B_cprint(Some_FDB73B this, B isOut);

inline B Some_FDB73B__is(STACK_FRAME void* this) {
  return ((Some_FDB73B) this)->type == TSome_FDB73B;
}

inline Some_FDB73B Some_FDB73B__as(STACK_FRAME void *this) {
  if (Some_FDB73B__is(CALLER this)) return (Some_FDB73B) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api].");
  abort();
}

void Some_FDB73B_apply(STACK_FRAME Some_FDB73B this, base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api value);

#ifdef __cplusplus
}
#endif

#endif