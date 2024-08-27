#ifndef SIREUM_H_org_sireum_Some_13E02D
#define SIREUM_H_org_sireum_Some_13E02D

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]

#define Some_13E02D_value_(this) ((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &(this)->value)

B Some_13E02D__eq(Some_13E02D this, Some_13E02D other);
inline B Some_13E02D__ne(Some_13E02D this, Some_13E02D other) {
  return !Some_13E02D__eq(this, other);
};
B Some_13E02D__equiv(Some_13E02D this, Some_13E02D other);
inline B Some_13E02D__inequiv(Some_13E02D this, Some_13E02D other) {
  return !Some_13E02D__equiv(this, other);
};
void Some_13E02D_string_(STACK_FRAME String result, Some_13E02D this);
void Some_13E02D_cprint(Some_13E02D this, B isOut);

inline B Some_13E02D__is(STACK_FRAME void* this) {
  return ((Some_13E02D) this)->type == TSome_13E02D;
}

inline Some_13E02D Some_13E02D__as(STACK_FRAME void *this) {
  if (Some_13E02D__is(CALLER this)) return (Some_13E02D) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api].");
  abort();
}

void Some_13E02D_apply(STACK_FRAME Some_13E02D this, base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api value);

#ifdef __cplusplus
}
#endif

#endif