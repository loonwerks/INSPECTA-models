#include <all.h>

// Some[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]

B Some_A8A5E0__eq(Some_A8A5E0 this, Some_A8A5E0 other) {
  if (base_test_event_port_periodic_domains_producer_t_i_Operational_Api__ne((base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &this->value, (base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_A8A5E0__equiv(Some_A8A5E0 this, Some_A8A5E0 other) {
  if (base_test_event_port_periodic_domains_producer_t_i_Operational_Api__inequiv((base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &this->value, (base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_A8A5E0__ne(Some_A8A5E0 this, Some_A8A5E0 other);
B Some_A8A5E0__inequiv(Some_A8A5E0 this, Some_A8A5E0 other);

void Some_A8A5E0_string_(STACK_FRAME String result, Some_A8A5E0 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_port_periodic_domains_producer_t_i_Operational_Api_string_(SF result, (base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_A8A5E0_cprint(Some_A8A5E0 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_port_periodic_domains_producer_t_i_Operational_Api_cprint((base_test_event_port_periodic_domains_producer_t_i_Operational_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_A8A5E0__is(STACK_FRAME void* this);
Some_A8A5E0 Some_A8A5E0__as(STACK_FRAME void *this);

void Some_A8A5E0_apply(STACK_FRAME Some_A8A5E0 this, base_test_event_port_periodic_domains_producer_t_i_Operational_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_A8A5E0", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_port_periodic_domains_producer_t_i_Operational_Api));
}