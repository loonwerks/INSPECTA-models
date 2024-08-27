#ifndef SIREUM_H_org_sireum_Some_0B69ED
#define SIREUM_H_org_sireum_Some_0B69ED

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]

#define Some_0B69ED_value_(this) ((base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &(this)->value)

B Some_0B69ED__eq(Some_0B69ED this, Some_0B69ED other);
inline B Some_0B69ED__ne(Some_0B69ED this, Some_0B69ED other) {
  return !Some_0B69ED__eq(this, other);
};
B Some_0B69ED__equiv(Some_0B69ED this, Some_0B69ED other);
inline B Some_0B69ED__inequiv(Some_0B69ED this, Some_0B69ED other) {
  return !Some_0B69ED__equiv(this, other);
};
void Some_0B69ED_string_(STACK_FRAME String result, Some_0B69ED this);
void Some_0B69ED_cprint(Some_0B69ED this, B isOut);

inline B Some_0B69ED__is(STACK_FRAME void* this) {
  return ((Some_0B69ED) this)->type == TSome_0B69ED;
}

inline Some_0B69ED Some_0B69ED__as(STACK_FRAME void *this) {
  if (Some_0B69ED__is(CALLER this)) return (Some_0B69ED) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api].");
  abort();
}

void Some_0B69ED_apply(STACK_FRAME Some_0B69ED this, base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api value);

#ifdef __cplusplus
}
#endif

#endif