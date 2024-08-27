#include <all.h>

// base.Base_Types.Boolean_Payload

B base_Base_Types_Boolean_Payload__eq(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other) {
  if (B__ne(this->value, other->value)) return F;
  return T;
}

B base_Base_Types_Boolean_Payload__equiv(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other) {
  if (B__inequiv(this->value, other->value)) return F;
  return T;
}

B base_Base_Types_Boolean_Payload__ne(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other);
B base_Base_Types_Boolean_Payload__inequiv(base_Base_Types_Boolean_Payload this, base_Base_Types_Boolean_Payload other);

void base_Base_Types_Boolean_Payload_string_(STACK_FRAME String result, base_Base_Types_Boolean_Payload this) {
  DeclNewStackFrame(caller, "Base_Types.scala", "base.Base_Types.Boolean_Payload", "string", 0);
  String_string_(SF result, string("Boolean_Payload("));
  B_string_(SF result, this->value);
  String_string_(SF result, string(")"));
}

void base_Base_Types_Boolean_Payload_cprint(base_Base_Types_Boolean_Payload this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Boolean_Payload("), isOut);
  B_cprint(this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_Base_Types_Boolean_Payload__is(STACK_FRAME void* this);
base_Base_Types_Boolean_Payload base_Base_Types_Boolean_Payload__as(STACK_FRAME void *this);

void base_Base_Types_Boolean_Payload_apply(STACK_FRAME base_Base_Types_Boolean_Payload this, B value) {
  DeclNewStackFrame(caller, "Base_Types.scala", "base.Base_Types.Boolean_Payload", "apply", 0);
  this->value = value;
}