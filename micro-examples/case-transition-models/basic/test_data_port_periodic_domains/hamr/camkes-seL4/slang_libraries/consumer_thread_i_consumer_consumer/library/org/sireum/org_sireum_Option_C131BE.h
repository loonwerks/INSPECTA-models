#ifndef SIREUM_H_org_sireum_Option_C131BE
#define SIREUM_H_org_sireum_Option_C131BE

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]

#define Option_C131BE__eq(this, other) Type__eq(this, other)
#define Option_C131BE__ne(this, other) (!Type__eq(this, other))
#define Option_C131BE__equiv(this, other) Type__equiv(this, other)
#define Option_C131BE__inequiv(this, other) (!Type__equiv(this, other))
#define Option_C131BE_cprint(this, isOut) Type_cprint(this, isOut)
B Option_C131BE__is(STACK_FRAME void *this);
Option_C131BE Option_C131BE__as(STACK_FRAME void *this);
inline void Option_C131BE_string_(STACK_FRAME String result, Option_C131BE this) {
  Type_string_(CALLER result, this);
}

void Option_C131BE_get_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api result, Option_C131BE this);

#ifdef __cplusplus
}
#endif

#endif