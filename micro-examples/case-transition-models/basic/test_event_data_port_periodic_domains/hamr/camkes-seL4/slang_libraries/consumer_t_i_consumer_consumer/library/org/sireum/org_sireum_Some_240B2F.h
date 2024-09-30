#ifndef SIREUM_H_org_sireum_Some_240B2F
#define SIREUM_H_org_sireum_Some_240B2F

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// Some[S8]

#define Some_240B2F_value_(this) ((this)->value)

B Some_240B2F__eq(Some_240B2F this, Some_240B2F other);
inline B Some_240B2F__ne(Some_240B2F this, Some_240B2F other) {
  return !Some_240B2F__eq(this, other);
};
B Some_240B2F__equiv(Some_240B2F this, Some_240B2F other);
inline B Some_240B2F__inequiv(Some_240B2F this, Some_240B2F other) {
  return !Some_240B2F__equiv(this, other);
};
void Some_240B2F_string_(STACK_FRAME String result, Some_240B2F this);
void Some_240B2F_cprint(Some_240B2F this, B isOut);

inline B Some_240B2F__is(STACK_FRAME void* this) {
  return ((Some_240B2F) this)->type == TSome_240B2F;
}

inline Some_240B2F Some_240B2F__as(STACK_FRAME void *this) {
  if (Some_240B2F__is(CALLER this)) return (Some_240B2F) this;
  sfAbortImpl(CALLER "Invalid cast to Some[S8].");
  abort();
}

void Some_240B2F_apply(STACK_FRAME Some_240B2F this, S8 value);

#ifdef __cplusplus
}
#endif

#endif