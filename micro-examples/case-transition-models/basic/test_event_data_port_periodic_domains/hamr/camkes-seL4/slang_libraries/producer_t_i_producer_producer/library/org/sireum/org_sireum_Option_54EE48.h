#ifndef SIREUM_H_org_sireum_Option_54EE48
#define SIREUM_H_org_sireum_Option_54EE48

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]

#define Option_54EE48__eq(this, other) Type__eq(this, other)
#define Option_54EE48__ne(this, other) (!Type__eq(this, other))
#define Option_54EE48__equiv(this, other) Type__equiv(this, other)
#define Option_54EE48__inequiv(this, other) (!Type__equiv(this, other))
#define Option_54EE48_cprint(this, isOut) Type_cprint(this, isOut)
B Option_54EE48__is(STACK_FRAME void *this);
Option_54EE48 Option_54EE48__as(STACK_FRAME void *this);
inline void Option_54EE48_string_(STACK_FRAME String result, Option_54EE48 this) {
  Type_string_(CALLER result, this);
}

void Option_54EE48_get_(STACK_FRAME base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api result, Option_54EE48 this);

#ifdef __cplusplus
}
#endif

#endif