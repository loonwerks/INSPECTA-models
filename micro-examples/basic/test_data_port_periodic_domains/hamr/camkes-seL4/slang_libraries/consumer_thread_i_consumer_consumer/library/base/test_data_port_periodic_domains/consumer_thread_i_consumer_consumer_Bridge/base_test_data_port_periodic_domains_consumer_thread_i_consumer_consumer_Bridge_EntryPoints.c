#include <all.h>

// base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints

B base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints__eq(base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this, base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints other) {
  if (art_Art_BridgeId__ne(this->consumer_thread_i_consumer_consumer_BridgeId, other->consumer_thread_i_consumer_consumer_BridgeId)) return F;
  if (art_Art_PortId__ne(this->read_port_Id, other->read_port_Id)) return F;
  if (Option_0247A1__ne((Option_0247A1) &this->dispatchTriggers, (Option_0247A1) &other->dispatchTriggers)) return F;
  if (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api__ne((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->initialization_api, (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &other->initialization_api)) return F;
  if (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__ne((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->operational_api, (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &other->operational_api)) return F;
  return T;
}

B base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints__equiv(base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this, base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints other) {
  if (IS_D10119__inequiv((IS_D10119) &this->dataInPortIds, (IS_D10119) &other->dataInPortIds)) return F;
  if (IS_D10119__inequiv((IS_D10119) &this->eventInPortIds, (IS_D10119) &other->eventInPortIds)) return F;
  if (IS_D10119__inequiv((IS_D10119) &this->dataOutPortIds, (IS_D10119) &other->dataOutPortIds)) return F;
  if (IS_D10119__inequiv((IS_D10119) &this->eventOutPortIds, (IS_D10119) &other->eventOutPortIds)) return F;
  if (art_Art_BridgeId__inequiv(this->consumer_thread_i_consumer_consumer_BridgeId, other->consumer_thread_i_consumer_consumer_BridgeId)) return F;
  if (art_Art_PortId__inequiv(this->read_port_Id, other->read_port_Id)) return F;
  if (Option_0247A1__inequiv((Option_0247A1) &this->dispatchTriggers, (Option_0247A1) &other->dispatchTriggers)) return F;
  if (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api__inequiv((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->initialization_api, (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &other->initialization_api)) return F;
  if (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__inequiv((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->operational_api, (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &other->operational_api)) return F;
  return T;
}

B base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints__ne(base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this, base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints other);
B base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints__inequiv(base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this, base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints other);

void base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_string_(STACK_FRAME String result, base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer_Bridge.scala", "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints", "string", 0);
  String_string_(SF result, string("EntryPoints("));
  String sep = string(", ");
  art_Art_BridgeId_string_(SF result, this->consumer_thread_i_consumer_consumer_BridgeId);
  String_string_(SF result, sep);
  art_Art_PortId_string_(SF result, this->read_port_Id);
  String_string_(SF result, sep);
  Option_0247A1_string_(SF result, (Option_0247A1) &this->dispatchTriggers);
  String_string_(SF result, sep);
  base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api_string_(SF result, (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->initialization_api);
  String_string_(SF result, sep);
  base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_string_(SF result, (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->operational_api);
  String_string_(SF result, string(")"));
}

void base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_cprint(base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("EntryPoints("), isOut);
  String sep = string(", ");
  art_Art_BridgeId_cprint(this->consumer_thread_i_consumer_consumer_BridgeId, isOut);
  String_cprint(sep, isOut);
  art_Art_PortId_cprint(this->read_port_Id, isOut);
  String_cprint(sep, isOut);
  Option_0247A1_cprint((Option_0247A1) &this->dispatchTriggers, isOut);
  String_cprint(sep, isOut);
  base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api_cprint((base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) &this->initialization_api, isOut);
  String_cprint(sep, isOut);
  base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_cprint((base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) &this->operational_api, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints__is(STACK_FRAME void* this);
base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints__as(STACK_FRAME void *this);

void base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_apply(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this, art_Art_BridgeId consumer_thread_i_consumer_consumer_BridgeId, art_Art_PortId read_port_Id, Option_0247A1 dispatchTriggers, base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api initialization_api, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api operational_api) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer_Bridge.scala", "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints", "apply", 0);
  this->consumer_thread_i_consumer_consumer_BridgeId = consumer_thread_i_consumer_consumer_BridgeId;
  this->read_port_Id = read_port_Id;
  Type_assign(&this->dispatchTriggers, dispatchTriggers, sizeof(union Option_0247A1));
  Type_assign(&this->initialization_api, initialization_api, sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api));
  Type_assign(&this->operational_api, operational_api, sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api));
  {
    sfUpdateLoc(73);
    STATIC_ASSERT(1 <= MaxIS_D10119, "Insufficient maximum for IS[Z, art.Art.PortId] elements.");
    DeclNewIS_D10119(t_0);
    t_0.size = (int8_t) 1;
    IS_D10119_up(&t_0, 0, (art_Art_PortId) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_read_port_Id_(this));
    Type_assign(&this->dataInPortIds, (&t_0), sizeof(struct IS_D10119));
  }
  {
    sfUpdateLoc(75);
    STATIC_ASSERT(0 <= MaxIS_D10119, "Insufficient maximum for IS[Z, art.Art.PortId] elements.");
    DeclNewIS_D10119(t_1);
    t_1.size = (int8_t) 0;
    Type_assign(&this->eventInPortIds, (&t_1), sizeof(struct IS_D10119));
  }
  {
    sfUpdateLoc(77);
    STATIC_ASSERT(0 <= MaxIS_D10119, "Insufficient maximum for IS[Z, art.Art.PortId] elements.");
    DeclNewIS_D10119(t_2);
    t_2.size = (int8_t) 0;
    Type_assign(&this->dataOutPortIds, (&t_2), sizeof(struct IS_D10119));
  }
  {
    sfUpdateLoc(79);
    STATIC_ASSERT(0 <= MaxIS_D10119, "Insufficient maximum for IS[Z, art.Art.PortId] elements.");
    DeclNewIS_D10119(t_3);
    t_3.size = (int8_t) 0;
    Type_assign(&this->eventOutPortIds, (&t_3), sizeof(struct IS_D10119));
  }
}

Unit base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_initialise_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer_Bridge.scala", "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints", "initialise", 0);

  sfUpdateLoc(83);
  {
    base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_initialise(SF (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_initialization_api_(this));
  }

  sfUpdateLoc(84);
  {
    art_Art_sendOutput(SF (IS_D10119) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_eventOutPortIds_(this), (IS_D10119) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_dataOutPortIds_(this));
  }
}

Unit base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_compute_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer_Bridge.scala", "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints", "compute", 0);

  sfUpdateLoc(88);
  {
    art_Art_receiveInput(SF (IS_D10119) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_eventInPortIds_(this), (IS_D10119) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_dataInPortIds_(this));
  }

  sfUpdateLoc(91);
  {
    base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_timeTriggered(SF (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_operational_api_(this));
  }

  sfUpdateLoc(93);
  {
    art_Art_sendOutput(SF (IS_D10119) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_eventOutPortIds_(this), (IS_D10119) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_dataOutPortIds_(this));
  }
}

Unit base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_finalise_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints this) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer_Bridge.scala", "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints", "finalise", 0);

  sfUpdateLoc(98);
  {
    base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_finalise(SF (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints_operational_api_(this));
  }
}