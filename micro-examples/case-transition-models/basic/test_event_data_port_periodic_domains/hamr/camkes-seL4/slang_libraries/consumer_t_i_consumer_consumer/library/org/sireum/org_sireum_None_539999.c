#include <all.h>

// None[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]

B None_539999__eq(None_539999 this, None_539999 other) {
  return T;
}

B None_539999__equiv(None_539999 this, None_539999 other) {
  return T;
}

B None_539999__ne(None_539999 this, None_539999 other);
B None_539999__inequiv(None_539999 this, None_539999 other);

void None_539999_string_(STACK_FRAME String result, None_539999 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_539999_cprint(None_539999 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_539999__is(STACK_FRAME void* this);
None_539999 None_539999__as(STACK_FRAME void *this);

void None_539999_apply(STACK_FRAME None_539999 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_539999", "apply", 0);
}