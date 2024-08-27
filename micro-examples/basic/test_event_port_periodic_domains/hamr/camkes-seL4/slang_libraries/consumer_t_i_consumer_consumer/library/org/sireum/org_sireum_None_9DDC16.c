#include <all.h>

// None[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]

B None_9DDC16__eq(None_9DDC16 this, None_9DDC16 other) {
  return T;
}

B None_9DDC16__equiv(None_9DDC16 this, None_9DDC16 other) {
  return T;
}

B None_9DDC16__ne(None_9DDC16 this, None_9DDC16 other);
B None_9DDC16__inequiv(None_9DDC16 this, None_9DDC16 other);

void None_9DDC16_string_(STACK_FRAME String result, None_9DDC16 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None", "string", 0);
  String_string_(SF result, string("None("));
  String_string_(SF result, string(")"));
}

void None_9DDC16_cprint(None_9DDC16 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("None("), isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B None_9DDC16__is(STACK_FRAME void* this);
None_9DDC16 None_9DDC16__as(STACK_FRAME void *this);

void None_9DDC16_apply(STACK_FRAME None_9DDC16 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.None_9DDC16", "apply", 0);
}