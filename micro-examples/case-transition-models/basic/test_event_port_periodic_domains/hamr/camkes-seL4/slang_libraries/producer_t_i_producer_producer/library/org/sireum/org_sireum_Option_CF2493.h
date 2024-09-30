#ifndef SIREUM_H_org_sireum_Option_CF2493
#define SIREUM_H_org_sireum_Option_CF2493

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]

#define Option_CF2493__eq(this, other) Type__eq(this, other)
#define Option_CF2493__ne(this, other) (!Type__eq(this, other))
#define Option_CF2493__equiv(this, other) Type__equiv(this, other)
#define Option_CF2493__inequiv(this, other) (!Type__equiv(this, other))
#define Option_CF2493_cprint(this, isOut) Type_cprint(this, isOut)
B Option_CF2493__is(STACK_FRAME void *this);
Option_CF2493 Option_CF2493__as(STACK_FRAME void *this);
inline void Option_CF2493_string_(STACK_FRAME String result, Option_CF2493 this) {
  Type_string_(CALLER result, this);
}

void Option_CF2493_get_(STACK_FRAME base_test_event_port_periodic_domains_producer_t_i_Operational_Api result, Option_CF2493 this);

#ifdef __cplusplus
}
#endif

#endif