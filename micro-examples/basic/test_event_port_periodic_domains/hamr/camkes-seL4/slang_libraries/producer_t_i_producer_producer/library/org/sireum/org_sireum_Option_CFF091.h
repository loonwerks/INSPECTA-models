#ifndef SIREUM_H_org_sireum_Option_CFF091
#define SIREUM_H_org_sireum_Option_CFF091

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]

#define Option_CFF091__eq(this, other) Type__eq(this, other)
#define Option_CFF091__ne(this, other) (!Type__eq(this, other))
#define Option_CFF091__equiv(this, other) Type__equiv(this, other)
#define Option_CFF091__inequiv(this, other) (!Type__equiv(this, other))
#define Option_CFF091_cprint(this, isOut) Type_cprint(this, isOut)
B Option_CFF091__is(STACK_FRAME void *this);
Option_CFF091 Option_CFF091__as(STACK_FRAME void *this);
inline void Option_CFF091_string_(STACK_FRAME String result, Option_CFF091 this) {
  Type_string_(CALLER result, this);
}

void Option_CFF091_get_(STACK_FRAME base_test_event_port_periodic_domains_producer_t_i_Initialization_Api result, Option_CFF091 this);

#ifdef __cplusplus
}
#endif

#endif