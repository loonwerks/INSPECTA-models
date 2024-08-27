#include <all.h>

// Some[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]

B Some_9F6875__eq(Some_9F6875 this, Some_9F6875 other) {
  if (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api__ne((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &this->value, (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_9F6875__equiv(Some_9F6875 this, Some_9F6875 other) {
  if (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api__inequiv((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &this->value, (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_9F6875__ne(Some_9F6875 this, Some_9F6875 other);
B Some_9F6875__inequiv(Some_9F6875 this, Some_9F6875 other);

void Some_9F6875_string_(STACK_FRAME String result, Some_9F6875 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api_string_(SF result, (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_9F6875_cprint(Some_9F6875 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api_cprint((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_9F6875__is(STACK_FRAME void* this);
Some_9F6875 Some_9F6875__as(STACK_FRAME void *this);

void Some_9F6875_apply(STACK_FRAME Some_9F6875 this, base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_9F6875", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api));
}