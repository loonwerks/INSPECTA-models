#include <all.h>

// None[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]

B None_B7029D__eq(None_B7029D this, None_B7029D other) {
  return T;
}

B None_B7029D__equiv(None_B7029D this, None_B7029D other) {
  return T;
}

B None_B7029D__ne(None_B7029D this, None_B7029D other);
B None_B7029D__inequiv(None_B7029D this, None_B7029D other);

void None_B7029D_string_(STACK_FRAME String result, None_B7029D this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_B7029D_cprint(None_B7029D this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_B7029D__is(STACK_FRAME void* this);
None_B7029D None_B7029D__as(STACK_FRAME void *this);

void None_B7029D_apply(STACK_FRAME None_B7029D this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_B7029D", "apply", 0);
}