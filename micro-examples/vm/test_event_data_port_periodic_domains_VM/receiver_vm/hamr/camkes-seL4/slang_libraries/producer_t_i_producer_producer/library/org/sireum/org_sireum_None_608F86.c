#include <all.h>

// None[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]

B None_608F86__eq(None_608F86 this, None_608F86 other) {
  return T;
}

B None_608F86__equiv(None_608F86 this, None_608F86 other) {
  return T;
}

B None_608F86__ne(None_608F86 this, None_608F86 other);
B None_608F86__inequiv(None_608F86 this, None_608F86 other);

void None_608F86_string_(STACK_FRAME String result, None_608F86 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_608F86_cprint(None_608F86 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_608F86__is(STACK_FRAME void* this);
None_608F86 None_608F86__as(STACK_FRAME void *this);

void None_608F86_apply(STACK_FRAME None_608F86 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_608F86", "apply", 0);
}