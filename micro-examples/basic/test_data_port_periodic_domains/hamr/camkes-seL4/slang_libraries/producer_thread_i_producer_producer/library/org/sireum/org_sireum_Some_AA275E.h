#ifndef SIREUM_H_org_sireum_Some_AA275E
#define SIREUM_H_org_sireum_Some_AA275E

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]

#define Some_AA275E_value_(this) ((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &(this)->value)

B Some_AA275E__eq(Some_AA275E this, Some_AA275E other);
inline B Some_AA275E__ne(Some_AA275E this, Some_AA275E other) {
  return !Some_AA275E__eq(this, other);
};
B Some_AA275E__equiv(Some_AA275E this, Some_AA275E other);
inline B Some_AA275E__inequiv(Some_AA275E this, Some_AA275E other) {
  return !Some_AA275E__equiv(this, other);
};
void Some_AA275E_string_(STACK_FRAME String result, Some_AA275E this);
void Some_AA275E_cprint(Some_AA275E this, B isOut);

inline B Some_AA275E__is(STACK_FRAME void* this) {
  return ((Some_AA275E) this)->type == TSome_AA275E;
}

inline Some_AA275E Some_AA275E__as(STACK_FRAME void *this) {
  if (Some_AA275E__is(CALLER this)) return (Some_AA275E) this;
  sfAbortImpl(CALLER "Invalid cast to Some[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api].");
  abort();
}

void Some_AA275E_apply(STACK_FRAME Some_AA275E this, base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api value);

#ifdef __cplusplus
}
#endif

#endif