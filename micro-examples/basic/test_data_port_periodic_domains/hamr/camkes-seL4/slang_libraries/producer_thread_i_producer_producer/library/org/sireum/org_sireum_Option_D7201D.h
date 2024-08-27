#ifndef SIREUM_H_org_sireum_Option_D7201D
#define SIREUM_H_org_sireum_Option_D7201D

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]

#define Option_D7201D__eq(this, other) Type__eq(this, other)
#define Option_D7201D__ne(this, other) (!Type__eq(this, other))
#define Option_D7201D__equiv(this, other) Type__equiv(this, other)
#define Option_D7201D__inequiv(this, other) (!Type__equiv(this, other))
#define Option_D7201D_cprint(this, isOut) Type_cprint(this, isOut)
B Option_D7201D__is(STACK_FRAME void *this);
Option_D7201D Option_D7201D__as(STACK_FRAME void *this);
inline void Option_D7201D_string_(STACK_FRAME String result, Option_D7201D this) {
  Type_string_(CALLER result, this);
}

void Option_D7201D_get_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api result, Option_D7201D this);

#ifdef __cplusplus
}
#endif

#endif