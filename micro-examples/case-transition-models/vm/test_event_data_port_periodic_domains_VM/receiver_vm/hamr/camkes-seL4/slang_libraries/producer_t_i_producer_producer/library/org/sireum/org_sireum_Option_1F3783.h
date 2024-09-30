#ifndef SIREUM_H_org_sireum_Option_1F3783
#define SIREUM_H_org_sireum_Option_1F3783

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]

#define Option_1F3783__eq(this, other) Type__eq(this, other)
#define Option_1F3783__ne(this, other) (!Type__eq(this, other))
#define Option_1F3783__equiv(this, other) Type__equiv(this, other)
#define Option_1F3783__inequiv(this, other) (!Type__equiv(this, other))
#define Option_1F3783_cprint(this, isOut) Type_cprint(this, isOut)
B Option_1F3783__is(STACK_FRAME void *this);
Option_1F3783 Option_1F3783__as(STACK_FRAME void *this);
inline void Option_1F3783_string_(STACK_FRAME String result, Option_1F3783 this) {
  Type_string_(CALLER result, this);
}

void Option_1F3783_get_(STACK_FRAME base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api result, Option_1F3783 this);

#ifdef __cplusplus
}
#endif

#endif