#include <all.h>

// None[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]

B None_F97B7F__eq(None_F97B7F this, None_F97B7F other) {
  return T;
}

B None_F97B7F__equiv(None_F97B7F this, None_F97B7F other) {
  return T;
}

B None_F97B7F__ne(None_F97B7F this, None_F97B7F other);
B None_F97B7F__inequiv(None_F97B7F this, None_F97B7F other);

void None_F97B7F_string_(STACK_FRAME String result, None_F97B7F this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_F97B7F_cprint(None_F97B7F this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_F97B7F__is(STACK_FRAME void* this);
None_F97B7F None_F97B7F__as(STACK_FRAME void *this);

void None_F97B7F_apply(STACK_FRAME None_F97B7F this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_F97B7F", "apply", 0);
}