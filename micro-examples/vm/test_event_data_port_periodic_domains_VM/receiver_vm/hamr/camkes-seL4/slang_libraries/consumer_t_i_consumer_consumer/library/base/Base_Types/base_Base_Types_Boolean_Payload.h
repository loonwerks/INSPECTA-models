#ifndef SIREUM_H_base_Base_Types_Boolean_Payload
#define SIREUM_H_base_Base_Types_Boolean_Payload

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// base.Base_Types.Boolean_Payload

#define base_Base_Types_Boolean_Payload_value_(this) ((this)->value)

B base_Base_Types_Boolean_Payload__eq(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other);
inline B base_Base_Types_Boolean_Payload__ne(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other) {
  return !base_Base_Types_Boolean_Payload__eq(this, other);
};
B base_Base_Types_Boolean_Payload__equiv(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other);
inline B base_Base_Types_Boolean_Payload__inequiv(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other) {
  return !base_Base_Types_Boolean_Payload__equiv(this, other);
};
void base_Base_Types_Boolean_Payload_string_(STACK_FRAME String result, base_Base_Types_Boolean_Payload this);
void base_Base_Types_Boolean_Payload_cprint(base_Base_Types_Boolean_Payload this, B isOut);

inline B base_Base_Types_Boolean_Payload__is(STACK_FRAME void* this) {
  return ((base_Base_Types_Boolean_Payload) this)->type == Tbase_Base_Types_Boolean_Payload;
}

inline base_Base_Types_Boolean_Payload base_Base_Types_Boolean_Payload__as(STACK_FRAME void *this) {
  if (base_Base_Types_Boolean_Payload__is(CALLER this)) return (base_Base_Types_Boolean_Payload) this;
  sfAbortImpl(CALLER "Invalid cast to base.Base_Types.Boolean_Payload.");
  abort();
}

void base_Base_Types_Boolean_Payload_apply(STACK_FRAME base_Base_Types_Boolean_Payload this, B value);

#ifdef __cplusplus
}
#endif

#endif