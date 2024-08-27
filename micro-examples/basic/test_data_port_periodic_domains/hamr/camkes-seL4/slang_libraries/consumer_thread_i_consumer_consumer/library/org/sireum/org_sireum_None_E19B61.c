#include <all.h>

// None[S8]

B None_E19B61__eq(None_E19B61 this, None_E19B61 other) {
  return T;
}

B None_E19B61__equiv(None_E19B61 this, None_E19B61 other) {
  return T;
}

B None_E19B61__ne(None_E19B61 this, None_E19B61 other);
B None_E19B61__inequiv(None_E19B61 this, None_E19B61 other);

void None_E19B61_string_(STACK_FRAME String result, None_E19B61 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_E19B61_cprint(None_E19B61 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_E19B61__is(STACK_FRAME void* this);
None_E19B61 None_E19B61__as(STACK_FRAME void *this);

void None_E19B61_apply(STACK_FRAME None_E19B61 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_E19B61", "apply", 0);
}