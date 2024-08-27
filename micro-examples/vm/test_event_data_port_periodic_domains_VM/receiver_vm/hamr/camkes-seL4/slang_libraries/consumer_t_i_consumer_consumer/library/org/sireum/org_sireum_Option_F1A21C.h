#ifndef SIREUM_H_org_sireum_Option_F1A21C
#define SIREUM_H_org_sireum_Option_F1A21C

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]

#define Option_F1A21C__eq(this, other) Type__eq(this, other)
#define Option_F1A21C__ne(this, other) (!Type__eq(this, other))
#define Option_F1A21C__equiv(this, other) Type__equiv(this, other)
#define Option_F1A21C__inequiv(this, other) (!Type__equiv(this, other))
#define Option_F1A21C_cprint(this, isOut) Type_cprint(this, isOut)
B Option_F1A21C__is(STACK_FRAME void *this);
Option_F1A21C Option_F1A21C__as(STACK_FRAME void *this);
inline void Option_F1A21C_string_(STACK_FRAME String result, Option_F1A21C this) {
  Type_string_(CALLER result, this);
}

void Option_F1A21C_get_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api result, Option_F1A21C this);

#ifdef __cplusplus
}
#endif

#endif