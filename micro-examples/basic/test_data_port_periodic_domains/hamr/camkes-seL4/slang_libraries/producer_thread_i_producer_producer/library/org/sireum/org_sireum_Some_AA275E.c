#include <all.h>

// Some[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]

B Some_AA275E__eq(Some_AA275E this, Some_AA275E other) {
  if (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api__ne((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &this->value, (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_AA275E__equiv(Some_AA275E this, Some_AA275E other) {
  if (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api__inequiv((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &this->value, (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_AA275E__ne(Some_AA275E this, Some_AA275E other);
B Some_AA275E__inequiv(Some_AA275E this, Some_AA275E other);

void Some_AA275E_string_(STACK_FRAME String result, Some_AA275E this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_string_(SF result, (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_AA275E_cprint(Some_AA275E this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_cprint((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_AA275E__is(STACK_FRAME void* this);
Some_AA275E Some_AA275E__as(STACK_FRAME void *this);

void Some_AA275E_apply(STACK_FRAME Some_AA275E this, base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_AA275E", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api));
}