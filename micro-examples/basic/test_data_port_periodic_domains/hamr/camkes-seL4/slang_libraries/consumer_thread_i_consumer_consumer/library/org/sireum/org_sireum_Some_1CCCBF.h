#ifndef SIREUM_H_org_sireum_Some_1CCCBF
#define SIREUM_H_org_sireum_Some_1CCCBF

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]

#define Some_1CCCBF_value_(this) ((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &(this)->value)

B Some_1CCCBF__eq(Some_1CCCBF this, Some_1CCCBF other);
inline B Some_1CCCBF__ne(Some_1CCCBF this, Some_1CCCBF other) {
  return !Some_1CCCBF__eq(this, other);
};
B Some_1CCCBF__equiv(Some_1CCCBF this, Some_1CCCBF other);
inline B Some_1CCCBF__inequiv(Some_1CCCBF this, Some_1CCCBF other) {
  return !Some_1CCCBF__equiv(this, other);
};
void Some_1CCCBF_string_(STACK_FRAME String result, Some_1CCCBF this);
void Some_1CCCBF_cprint(Some_1CCCBF this, B isOut);

inline B Some_1CCCBF__is(STACK_FRAME void* this) {
  return ((Some_1CCCBF) this)->type == TSome_1CCCBF;
}

inline Some_1CCCBF Some_1CCCBF__as(STACK_FRAME void *this) {
  if (Some_1CCCBF__is(CALLER this)) return (Some_1CCCBF) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api].");
  abort();
}

void Some_1CCCBF_apply(STACK_FRAME Some_1CCCBF this, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api value);

#ifdef __cplusplus
}
#endif

#endif