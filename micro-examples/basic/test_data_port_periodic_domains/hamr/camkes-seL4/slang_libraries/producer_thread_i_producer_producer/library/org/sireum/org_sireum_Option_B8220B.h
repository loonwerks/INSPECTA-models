#ifndef SIREUM_H_org_sireum_Option_B8220B
#define SIREUM_H_org_sireum_Option_B8220B

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]

#define Option_B8220B__eq(this, other) Type__eq(this, other)
#define Option_B8220B__ne(this, other) (!Type__eq(this, other))
#define Option_B8220B__equiv(this, other) Type__equiv(this, other)
#define Option_B8220B__inequiv(this, other) (!Type__equiv(this, other))
#define Option_B8220B_cprint(this, isOut) Type_cprint(this, isOut)
B Option_B8220B__is(STACK_FRAME void *this);
Option_B8220B Option_B8220B__as(STACK_FRAME void *this);
inline void Option_B8220B_string_(STACK_FRAME String result, Option_B8220B this) {
  Type_string_(CALLER result, this);
}

void Option_B8220B_get_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api result, Option_B8220B this);

#ifdef __cplusplus
}
#endif

#endif