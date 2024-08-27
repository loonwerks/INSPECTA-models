#include <all.h>

// None[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]

B None_C54AB4__eq(None_C54AB4 this, None_C54AB4 other) {
  return T;
}

B None_C54AB4__equiv(None_C54AB4 this, None_C54AB4 other) {
  return T;
}

B None_C54AB4__ne(None_C54AB4 this, None_C54AB4 other);
B None_C54AB4__inequiv(None_C54AB4 this, None_C54AB4 other);

void None_C54AB4_string_(STACK_FRAME String result, None_C54AB4 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_C54AB4_cprint(None_C54AB4 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_C54AB4__is(STACK_FRAME void* this);
None_C54AB4 None_C54AB4__as(STACK_FRAME void *this);

void None_C54AB4_apply(STACK_FRAME None_C54AB4 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_C54AB4", "apply", 0);
}