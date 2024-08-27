#include <all.h>

// Some[S8]

B Some_240B2F__eq(Some_240B2F this, Some_240B2F other) {
  if (S8__ne(this->value, other->value)) return F;
  return T;
}

B Some_240B2F__equiv(Some_240B2F this, Some_240B2F other) {
  if (S8__inequiv(this->value, other->value)) return F;
  return T;
}

B Some_240B2F__ne(Some_240B2F this, Some_240B2F other);
B Some_240B2F__inequiv(Some_240B2F this, Some_240B2F other);

void Some_240B2F_string_(STACK_FRAME String result, Some_240B2F this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  S8_string_(SF result, this->value);
  String_string_(SF result, string(")"));
}

void Some_240B2F_cprint(Some_240B2F this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  S8_cprint(this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_240B2F__is(STACK_FRAME void* this);
Some_240B2F Some_240B2F__as(STACK_FRAME void *this);

void Some_240B2F_apply(STACK_FRAME Some_240B2F this, S8 value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_240B2F", "apply", 0);
  this->value = value;
}