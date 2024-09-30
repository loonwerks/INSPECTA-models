#include <all.h>

// base.test_data_port_periodic_domains.producer_thread_i_Operational_Api

B base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__eq(base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api other) {
  if (art_Art_BridgeId__ne(this->id, other->id)) return F;
  if (art_Art_PortId__ne(this->write_port_Id, other->write_port_Id)) return F;
  return T;
}

B base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__equiv(base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api other) {
  if (art_Art_BridgeId__inequiv(this->id, other->id)) return F;
  if (art_Art_PortId__inequiv(this->write_port_Id, other->write_port_Id)) return F;
  return T;
}

B base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__ne(base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api other);
B base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__inequiv(base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api other);

void base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_string_(STACK_FRAME String result, base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Operational_Api", "string", 0);
  String_string_(SF result, string("producer_thread_i_Operational_Api("));
  String sep = string(", ");
  art_Art_BridgeId_string_(SF result, this->id);
  String_string_(SF result, sep);
  art_Art_PortId_string_(SF result, this->write_port_Id);
  String_string_(SF result, string(")"));
}

void base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_cprint(base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("producer_thread_i_Operational_Api("), isOut);
  String sep = string(", ");
  art_Art_BridgeId_cprint(this->id, isOut);
  String_cprint(sep, isOut);
  art_Art_PortId_cprint(this->write_port_Id, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__is(STACK_FRAME void* this);
base_test_data_port_periodic_domains_producer_thread_i_Operational_Api base_test_data_port_periodic_domains_producer_thread_i_Operational_Api__as(STACK_FRAME void *this);

void base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_apply(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, art_Art_BridgeId id, art_Art_PortId write_port_Id) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Operational_Api", "apply", 0);
  this->id = id;
  this->write_port_Id = write_port_Id;
}