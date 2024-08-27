#include <all.h>

// None[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]

B None_F1003F__eq(None_F1003F this, None_F1003F other) {
  return T;
}

B None_F1003F__equiv(None_F1003F this, None_F1003F other) {
  return T;
}

B None_F1003F__ne(None_F1003F this, None_F1003F other);
B None_F1003F__inequiv(None_F1003F this, None_F1003F other);

void None_F1003F_string_(STACK_FRAME String result, None_F1003F this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_F1003F_cprint(None_F1003F this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_F1003F__is(STACK_FRAME void* this);
None_F1003F None_F1003F__as(STACK_FRAME void *this);

void None_F1003F_apply(STACK_FRAME None_F1003F this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_F1003F", "apply", 0);
}