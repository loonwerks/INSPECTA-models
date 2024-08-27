#include <all.h>

// base.Base_Types.Integer_8_Payload

B base_Base_Types_Integer_8_Payload__eq(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other) {
  if (S8__ne(this->value, other->value)) return F;
  return T;
}

B base_Base_Types_Integer_8_Payload__equiv(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other) {
  if (S8__inequiv(this->value, other->value)) return F;
  return T;
}

B base_Base_Types_Integer_8_Payload__ne(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other);
B base_Base_Types_Integer_8_Payload__inequiv(base_Base_Types_Integer_8_Payload this, base_Base_Types_Integer_8_Payload other);

void base_Base_Types_Integer_8_Payload_string_(STACK_FRAME String result, base_Base_Types_Integer_8_Payload this) {
  DeclNewStackFrame(caller, "Base_Types.scala", "base.Base_Types.Integer_8_Payload", "string", 0);
  String_string_(SF result, string("Integer_8_Payload("));
  S8_string_(SF result, this->value);
  String_string_(SF result, string(")"));
}

void base_Base_Types_Integer_8_Payload_cprint(base_Base_Types_Integer_8_Payload this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Integer_8_Payload("), isOut);
  S8_cprint(this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_Base_Types_Integer_8_Payload__is(STACK_FRAME void* this);
base_Base_Types_Integer_8_Payload base_Base_Types_Integer_8_Payload__as(STACK_FRAME void *this);

void base_Base_Types_Integer_8_Payload_apply(STACK_FRAME base_Base_Types_Integer_8_Payload this, S8 value) {
  DeclNewStackFrame(caller, "Base_Types.scala", "base.Base_Types.Integer_8_Payload", "apply", 0);
  this->value = value;
}