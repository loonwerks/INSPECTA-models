#include <all.h>

// Some[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]

B Some_6B73B7__eq(Some_6B73B7 this, Some_6B73B7 other) {
  if (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api__ne((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value, (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_6B73B7__equiv(Some_6B73B7 this, Some_6B73B7 other) {
  if (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api__inequiv((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value, (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_6B73B7__ne(Some_6B73B7 this, Some_6B73B7 other);
B Some_6B73B7__inequiv(Some_6B73B7 this, Some_6B73B7 other);

void Some_6B73B7_string_(STACK_FRAME String result, Some_6B73B7 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api_string_(SF result, (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_6B73B7_cprint(Some_6B73B7 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api_cprint((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_6B73B7__is(STACK_FRAME void* this);
Some_6B73B7 Some_6B73B7__as(STACK_FRAME void *this);

void Some_6B73B7_apply(STACK_FRAME Some_6B73B7 this, base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_6B73B7", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api));
}