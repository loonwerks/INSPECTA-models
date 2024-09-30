#include <all.h>

// None[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]

B None_F1EF25__eq(None_F1EF25 this, None_F1EF25 other) {
  return T;
}

B None_F1EF25__equiv(None_F1EF25 this, None_F1EF25 other) {
  return T;
}

B None_F1EF25__ne(None_F1EF25 this, None_F1EF25 other);
B None_F1EF25__inequiv(None_F1EF25 this, None_F1EF25 other);

void None_F1EF25_string_(STACK_FRAME String result, None_F1EF25 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_F1EF25_cprint(None_F1EF25 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_F1EF25__is(STACK_FRAME void* this);
None_F1EF25 None_F1EF25__as(STACK_FRAME void *this);

void None_F1EF25_apply(STACK_FRAME None_F1EF25 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_F1EF25", "apply", 0);
}