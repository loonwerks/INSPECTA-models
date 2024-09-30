#include <all.h>

// Some[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]

B Some_0B69ED__eq(Some_0B69ED this, Some_0B69ED other) {
  if (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api__ne((base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value, (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_0B69ED__equiv(Some_0B69ED this, Some_0B69ED other) {
  if (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api__inequiv((base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value, (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &other->value)) return F;
  return T;
}

B Some_0B69ED__ne(Some_0B69ED this, Some_0B69ED other);
B Some_0B69ED__inequiv(Some_0B69ED this, Some_0B69ED other);

void Some_0B69ED_string_(STACK_FRAME String result, Some_0B69ED this) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some", "string", 0);
  String_string_(SF result, string("Some("));
  base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api_string_(SF result, (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value);
  String_string_(SF result, string(")"));
}

void Some_0B69ED_cprint(Some_0B69ED this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("Some("), isOut);
  base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api_cprint((base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) &this->value, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B Some_0B69ED__is(STACK_FRAME void* this);
Some_0B69ED Some_0B69ED__as(STACK_FRAME void *this);

void Some_0B69ED_apply(STACK_FRAME Some_0B69ED this, base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api value) {
  DeclNewStackFrame(caller, "Option.scala", "org.sireum.Some_0B69ED", "apply", 0);
  Type_assign(&this->value, value, sizeof(struct base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api));
}