#ifndef SIREUM_H_org_sireum_Option_6288B6
#define SIREUM_H_org_sireum_Option_6288B6

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]

#define Option_6288B6__eq(this, other) Type__eq(this, other)
#define Option_6288B6__ne(this, other) (!Type__eq(this, other))
#define Option_6288B6__equiv(this, other) Type__equiv(this, other)
#define Option_6288B6__inequiv(this, other) (!Type__equiv(this, other))
#define Option_6288B6_cprint(this, isOut) Type_cprint(this, isOut)
B Option_6288B6__is(STACK_FRAME void *this);
Option_6288B6 Option_6288B6__as(STACK_FRAME void *this);
inline void Option_6288B6_string_(STACK_FRAME String result, Option_6288B6 this) {
  Type_string_(CALLER result, this);
}

void Option_6288B6_get_(STACK_FRAME base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api result, Option_6288B6 this);

#ifdef __cplusplus
}
#endif

#endif