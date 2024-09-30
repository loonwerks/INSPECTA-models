#ifndef SIREUM_H_org_sireum_Option_FC3847
#define SIREUM_H_org_sireum_Option_FC3847

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Option[S8]

#define Option_FC3847__eq(this, other) Type__eq(this, other)
#define Option_FC3847__ne(this, other) (!Type__eq(this, other))
#define Option_FC3847__equiv(this, other) Type__equiv(this, other)
#define Option_FC3847__inequiv(this, other) (!Type__equiv(this, other))
#define Option_FC3847_cprint(this, isOut) Type_cprint(this, isOut)
B Option_FC3847__is(STACK_FRAME void *this);
Option_FC3847 Option_FC3847__as(STACK_FRAME void *this);
inline void Option_FC3847_string_(STACK_FRAME String result, Option_FC3847 this) {
  Type_string_(CALLER result, this);
}

#ifdef __cplusplus
}
#endif

#endif