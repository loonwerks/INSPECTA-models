#ifndef SIREUM_H_org_sireum_Some_E023D9
#define SIREUM_H_org_sireum_Some_E023D9

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]

#define Some_E023D9_value_(this) ((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &(this)->value)

B Some_E023D9__eq(Some_E023D9 this, Some_E023D9 other);
inline B Some_E023D9__ne(Some_E023D9 this, Some_E023D9 other) {
  return !Some_E023D9__eq(this, other);
};
B Some_E023D9__equiv(Some_E023D9 this, Some_E023D9 other);
inline B Some_E023D9__inequiv(Some_E023D9 this, Some_E023D9 other) {
  return !Some_E023D9__equiv(this, other);
};
void Some_E023D9_string_(STACK_FRAME String result, Some_E023D9 this);
void Some_E023D9_cprint(Some_E023D9 this, B isOut);

inline B Some_E023D9__is(STACK_FRAME void* this) {
  return ((Some_E023D9) this)->type == TSome_E023D9;
}

inline Some_E023D9 Some_E023D9__as(STACK_FRAME void *this) {
  if (Some_E023D9__is(CALLER this)) return (Some_E023D9) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api].");
  abort();
}

void Some_E023D9_apply(STACK_FRAME Some_E023D9 this, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api value);

#ifdef __cplusplus
}
#endif

#endif