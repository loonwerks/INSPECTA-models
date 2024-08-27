#include <all.h>

// None[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]

B None_D3113C__eq(None_D3113C this, None_D3113C other) {
  return T;
}

B None_D3113C__equiv(None_D3113C this, None_D3113C other) {
  return T;
}

B None_D3113C__ne(None_D3113C this, None_D3113C other);
B None_D3113C__inequiv(None_D3113C this, None_D3113C other);

void None_D3113C_string_(STACK_FRAME String result, None_D3113C this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_D3113C_cprint(None_D3113C this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_D3113C__is(STACK_FRAME void* this);
None_D3113C None_D3113C__as(STACK_FRAME void *this);

void None_D3113C_apply(STACK_FRAME None_D3113C this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_D3113C", "apply", 0);
}