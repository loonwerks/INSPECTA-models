#include <all.h>

// base.Base_Types.Bits_Payload

B base_Base_Types_Bits_Payload__eq(base_Base_Types_Bits_Payload this, base_Base_Types_Bits_Payload other) {
  if (IS_C4F575__ne((IS_C4F575) &this->value, (IS_C4F575) &other->value)) return F;
  return T;
}

B base_Base_Types_Bits_Payload__equiv(base_Base_Types_Bits_Payload this, base_Base_Types_Bits_Payload other) {
  if (IS_C4F575__inequiv((IS_C4F575) &this->value, (IS_C4F575) &other->value)) return F;
  return T;
}

B base_Base_Types_Bits_Payload__ne(base_Base_Types_Bits_Payload this, base_Base_Types_Bits_Payload other);
B base_Base_Types_Bits_Payload__inequiv(base_Base_Types_Bits_Payload this, base_Base_Types_Bits_Payload other);

void base_Base_Types_Bits_Payload_string_(STACK_FRAME String result, base_Base_Types_Bits_Payload this) {
  DeclNewStackFrame(caller, "Base_Types.scala", "base.Base_Types.Bits_Payload", "string", 0);
  String_string_(SF result, string("Bits_Payload("));
  IS_C4F575_string_(SF result, (IS_C4F575) &this->value);
  String_string_(SF result, string(")"));
}

void base_Base_Types_Bits_Payload_cprint(base_Base_Types_Bits_Payload this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Bits_Payload("), isOut);
  IS_C4F575_cprint((IS_C4F575) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_Base_Types_Bits_Payload__is(STACK_FRAME void* this);
base_Base_Types_Bits_Payload base_Base_Types_Bits_Payload__as(STACK_FRAME void *this);

void base_Base_Types_Bits_Payload_apply(STACK_FRAME base_Base_Types_Bits_Payload this, IS_C4F575 value) {
  DeclNewStackFrame(caller, "Base_Types.scala", "base.Base_Types.Bits_Payload", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct IS_C4F575));
}