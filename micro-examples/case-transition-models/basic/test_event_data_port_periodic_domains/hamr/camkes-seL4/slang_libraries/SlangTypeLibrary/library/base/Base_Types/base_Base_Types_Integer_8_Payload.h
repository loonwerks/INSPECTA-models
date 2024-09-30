#ifndef SIREUM_H_base_Base_Types_Integer_8_Payload
#define SIREUM_H_base_Base_Types_Integer_8_Payload

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// base.Base_Types.Integer_8_Payload

#define base_Base_Types_Integer_8_Payload_value_(this) ((this)->value)

B base_Base_Types_Integer_8_Payload__eq(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other);
inline B base_Base_Types_Integer_8_Payload__ne(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other) {
  return !base_Base_Types_Integer_8_Payload__eq(this, other);
};
B base_Base_Types_Integer_8_Payload__equiv(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other);
inline B base_Base_Types_Integer_8_Payload__inequiv(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other) {
  return !base_Base_Types_Integer_8_Payload__equiv(this, other);
};
void base_Base_Types_Integer_8_Payload_string_(STACK_FRAME String result, base_Base_Types_Integer_8_Payload this);
void base_Base_Types_Integer_8_Payload_cprint(base_Base_Types_Integer_8_Payload this, B isOut);

inline B base_Base_Types_Integer_8_Payload__is(STACK_FRAME void* this) {
  return ((base_Base_Types_Integer_8_Payload) this)->type == Tbase_Base_Types_Integer_8_Payload;
}

inline base_Base_Types_Integer_8_Payload base_Base_Types_Integer_8_Payload__as(STACK_FRAME void *this) {
  if (base_Base_Types_Integer_8_Payload__is(CALLER this)) return (base_Base_Types_Integer_8_Payload) this;
  sfAbortImpl(CALLER "Invalid cast to base.Base_Types.Integer_8_Payload.");
  abort();
}

void base_Base_Types_Integer_8_Payload_apply(STACK_FRAME base_Base_Types_Integer_8_Payload this, S8 value);

#ifdef __cplusplus
}
#endif

#endif