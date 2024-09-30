#include <all.h>

// Some[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]

B Some_1CCCBF__eq(Some_1CCCBF this, Some_1CCCBF other) {
  if (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__ne((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->value, (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_1CCCBF__equiv(Some_1CCCBF this, Some_1CCCBF other) {
  if (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__inequiv((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->value, (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_1CCCBF__ne(Some_1CCCBF this, Some_1CCCBF other);
B Some_1CCCBF__inequiv(Some_1CCCBF this, Some_1CCCBF other);

void Some_1CCCBF_string_(STACK_FRAME String result, Some_1CCCBF this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_string_(SF result, (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_1CCCBF_cprint(Some_1CCCBF this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_cprint((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_1CCCBF__is(STACK_FRAME void* this);
Some_1CCCBF Some_1CCCBF__as(STACK_FRAME void *this);

void Some_1CCCBF_apply(STACK_FRAME Some_1CCCBF this, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_1CCCBF", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api));
}