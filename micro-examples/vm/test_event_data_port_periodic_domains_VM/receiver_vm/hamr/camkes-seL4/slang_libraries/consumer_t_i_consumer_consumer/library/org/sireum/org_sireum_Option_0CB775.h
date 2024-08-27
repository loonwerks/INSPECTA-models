#ifndef SIREUM_H_org_sireum_Option_0CB775
#define SIREUM_H_org_sireum_Option_0CB775

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]

#define Option_0CB775__eq(this, other) Type__eq(this, other)
#define Option_0CB775__ne(this, other) (!Type__eq(this, other))
#define Option_0CB775__equiv(this, other) Type__equiv(this, other)
#define Option_0CB775__inequiv(this, other) (!Type__equiv(this, other))
#define Option_0CB775_cprint(this, isOut) Type_cprint(this, isOut)
B Option_0CB775__is(STACK_FRAME void *this);
Option_0CB775 Option_0CB775__as(STACK_FRAME void *this);
inline void Option_0CB775_string_(STACK_FRAME String result, Option_0CB775 this) {
  Type_string_(CALLER result, this);
}

void Option_0CB775_get_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api result, Option_0CB775 this);

#ifdef __cplusplus
}
#endif

#endif