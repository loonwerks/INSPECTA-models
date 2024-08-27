#ifndef SIREUM_H_org_sireum_Option_7640EA
#define SIREUM_H_org_sireum_Option_7640EA

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]

#define Option_7640EA__eq(this, other) Type__eq(this, other)
#define Option_7640EA__ne(this, other) (!Type__eq(this, other))
#define Option_7640EA__equiv(this, other) Type__equiv(this, other)
#define Option_7640EA__inequiv(this, other) (!Type__equiv(this, other))
#define Option_7640EA_cprint(this, isOut) Type_cprint(this, isOut)
B Option_7640EA__is(STACK_FRAME void *this);
Option_7640EA Option_7640EA__as(STACK_FRAME void *this);
inline void Option_7640EA_string_(STACK_FRAME String result, Option_7640EA this) {
  Type_string_(CALLER result, this);
}

void Option_7640EA_get_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api result, Option_7640EA this);

#ifdef __cplusplus
}
#endif

#endif