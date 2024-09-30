#include <all.h>

// Some[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]

B Some_2EDC17__eq(Some_2EDC17 this, Some_2EDC17 other) {
  if (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__ne((base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &this->value, (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_2EDC17__equiv(Some_2EDC17 this, Some_2EDC17 other) {
  if (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__inequiv((base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &this->value, (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &other->value)) return F;
  return T;
}

B Some_2EDC17__ne(Some_2EDC17 this, Some_2EDC17 other);
B Some_2EDC17__inequiv(Some_2EDC17 this, Some_2EDC17 other);

void Some_2EDC17_string_(STACK_FRAME String result, Some_2EDC17 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_string_(SF result, (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_2EDC17_cprint(Some_2EDC17 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_cprint((base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_2EDC17__is(STACK_FRAME void* this);
Some_2EDC17 Some_2EDC17__as(STACK_FRAME void *this);

void Some_2EDC17_apply(STACK_FRAME Some_2EDC17 this, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_2EDC17", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_port_periodic_domains_consumer_t_i_Operational_Api));
}