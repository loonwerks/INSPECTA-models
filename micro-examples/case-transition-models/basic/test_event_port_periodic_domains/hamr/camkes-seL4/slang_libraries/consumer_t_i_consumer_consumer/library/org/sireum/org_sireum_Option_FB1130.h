#ifndef SIREUM_H_org_sireum_Option_FB1130
#define SIREUM_H_org_sireum_Option_FB1130

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]

#define Option_FB1130__eq(this, other) Type__eq(this, other)
#define Option_FB1130__ne(this, other) (!Type__eq(this, other))
#define Option_FB1130__equiv(this, other) Type__equiv(this, other)
#define Option_FB1130__inequiv(this, other) (!Type__equiv(this, other))
#define Option_FB1130_cprint(this, isOut) Type_cprint(this, isOut)
B Option_FB1130__is(STACK_FRAME void *this);
Option_FB1130 Option_FB1130__as(STACK_FRAME void *this);
inline void Option_FB1130_string_(STACK_FRAME String result, Option_FB1130 this) {
  Type_string_(CALLER result, this);
}

void Option_FB1130_get_(STACK_FRAME base_test_event_port_periodic_domains_consumer_t_i_Operational_Api result, Option_FB1130 this);

#ifdef __cplusplus
}
#endif

#endif