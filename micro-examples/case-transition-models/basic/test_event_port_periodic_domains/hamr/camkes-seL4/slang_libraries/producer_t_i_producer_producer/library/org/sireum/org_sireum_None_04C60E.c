#include <all.h>

// None[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]

B None_04C60E__eq(None_04C60E this, None_04C60E other) {
  return T;
}

B None_04C60E__equiv(None_04C60E this, None_04C60E other) {
  return T;
}

B None_04C60E__ne(None_04C60E this, None_04C60E other);
B None_04C60E__inequiv(None_04C60E this, None_04C60E other);

void None_04C60E_string_(STACK_FRAME String result, None_04C60E this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_04C60E_cprint(None_04C60E this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_04C60E__is(STACK_FRAME void* this);
None_04C60E None_04C60E__as(STACK_FRAME void *this);

void None_04C60E_apply(STACK_FRAME None_04C60E this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_04C60E", "apply", 0);
}