#include <all.h>

// None[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]

B None_5F2743__eq(None_5F2743 this, None_5F2743 other) {
  return T;
}

B None_5F2743__equiv(None_5F2743 this, None_5F2743 other) {
  return T;
}

B None_5F2743__ne(None_5F2743 this, None_5F2743 other);
B None_5F2743__inequiv(None_5F2743 this, None_5F2743 other);

void None_5F2743_string_(STACK_FRAME String result, None_5F2743 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_5F2743_cprint(None_5F2743 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_5F2743__is(STACK_FRAME void* this);
None_5F2743 None_5F2743__as(STACK_FRAME void *this);

void None_5F2743_apply(STACK_FRAME None_5F2743 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_5F2743", "apply", 0);
}