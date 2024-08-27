#include <all.h>

// Some[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]

B Some_E023D9__eq(Some_E023D9 this, Some_E023D9 other) {
  if (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__ne((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &this->value, (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_E023D9__equiv(Some_E023D9 this, Some_E023D9 other) {
  if (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__inequiv((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &this->value, (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_E023D9__ne(Some_E023D9 this, Some_E023D9 other);
B Some_E023D9__inequiv(Some_E023D9 this, Some_E023D9 other);

void Some_E023D9_string_(STACK_FRAME String result, Some_E023D9 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_string_(SF result, (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_E023D9_cprint(Some_E023D9 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_cprint((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_E023D9__is(STACK_FRAME void* this);
Some_E023D9 Some_E023D9__as(STACK_FRAME void *this);

void Some_E023D9_apply(STACK_FRAME Some_E023D9 this, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_E023D9", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_data_port_periodic_domains_producer_thread_i_Operational_Api));
}