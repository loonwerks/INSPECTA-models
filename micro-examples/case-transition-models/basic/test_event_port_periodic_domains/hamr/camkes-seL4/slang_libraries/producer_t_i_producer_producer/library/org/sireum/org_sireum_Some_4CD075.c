#include <all.h>

// Some[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]

B Some_4CD075__eq(Some_4CD075 this, Some_4CD075 other) {
  if (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api__ne((base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &this->value, (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_4CD075__equiv(Some_4CD075 this, Some_4CD075 other) {
  if (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api__inequiv((base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &this->value, (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_4CD075__ne(Some_4CD075 this, Some_4CD075 other);
B Some_4CD075__inequiv(Some_4CD075 this, Some_4CD075 other);

void Some_4CD075_string_(STACK_FRAME String result, Some_4CD075 this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_port_periodic_domains_producer_t_i_Initialization_Api_string_(SF result, (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_4CD075_cprint(Some_4CD075 this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_port_periodic_domains_producer_t_i_Initialization_Api_cprint((base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_4CD075__is(STACK_FRAME void* this);
Some_4CD075 Some_4CD075__as(STACK_FRAME void *this);

void Some_4CD075_apply(STACK_FRAME Some_4CD075 this, base_test_event_port_periodic_domains_producer_t_i_Initialization_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_4CD075", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_port_periodic_domains_producer_t_i_Initialization_Api));
}