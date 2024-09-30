#include <all.h>

// None[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]

B None_5A630D__eq(None_5A630D this, None_5A630D other) {
  return T;
}

B None_5A630D__equiv(None_5A630D this, None_5A630D other) {
  return T;
}

B None_5A630D__ne(None_5A630D this, None_5A630D other);
B None_5A630D__inequiv(None_5A630D this, None_5A630D other);

void None_5A630D_string_(STACK_FRAME String result, None_5A630D this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_5A630D_cprint(None_5A630D this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_5A630D__is(STACK_FRAME void* this);
None_5A630D None_5A630D__as(STACK_FRAME void *this);

void None_5A630D_apply(STACK_FRAME None_5A630D this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_5A630D", "apply", 0);
}