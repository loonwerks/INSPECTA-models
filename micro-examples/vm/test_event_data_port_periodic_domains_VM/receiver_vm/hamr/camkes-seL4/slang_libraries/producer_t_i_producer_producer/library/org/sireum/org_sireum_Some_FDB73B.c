#include <all.h>

// Some[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]

B Some_FDB73B__eq(Some_FDB73B this, Some_FDB73B other) {
  if (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api__ne((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &this->value, (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_FDB73B__equiv(Some_FDB73B this, Some_FDB73B other) {
  if (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api__inequiv((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &this->value, (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_FDB73B__ne(Some_FDB73B this, Some_FDB73B other);
B Some_FDB73B__inequiv(Some_FDB73B this, Some_FDB73B other);

void Some_FDB73B_string_(STACK_FRAME String result, Some_FDB73B this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api_string_(SF result, (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_FDB73B_cprint(Some_FDB73B this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api_cprint((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_FDB73B__is(STACK_FRAME void* this);
Some_FDB73B Some_FDB73B__as(STACK_FRAME void *this);

void Some_FDB73B_apply(STACK_FRAME Some_FDB73B this, base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_FDB73B", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api));
}