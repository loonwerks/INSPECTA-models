#include <all.h>

// Some[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]

B Some_13E02D__eq(Some_13E02D this, Some_13E02D other) {
  if (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api__ne((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->value, (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_13E02D__equiv(Some_13E02D this, Some_13E02D other) {
  if (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api__inequiv((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->value, (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_13E02D__ne(Some_13E02D this, Some_13E02D other);
B Some_13E02D__inequiv(Some_13E02D this, Some_13E02D other);

void Some_13E02D_string_(STACK_FRAME String result, Some_13E02D this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api_string_(SF result, (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_13E02D_cprint(Some_13E02D this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api_cprint((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_13E02D__is(STACK_FRAME void* this);
Some_13E02D Some_13E02D__as(STACK_FRAME void *this);

void Some_13E02D_apply(STACK_FRAME Some_13E02D this, base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_13E02D", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api));
}