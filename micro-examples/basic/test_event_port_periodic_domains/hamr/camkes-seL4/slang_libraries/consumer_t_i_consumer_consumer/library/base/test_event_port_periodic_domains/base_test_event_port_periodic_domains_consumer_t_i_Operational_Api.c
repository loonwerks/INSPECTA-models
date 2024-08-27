#include <all.h>

// base.test_event_port_periodic_domains.consumer_t_i_Operational_Api

B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__eq(base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api other) {
  if (art_Art_BridgeId__ne(this->id, other->id)) return F;
  if (art_Art_PortId__ne(this->consume_Id, other->consume_Id)) return F;
  return T;
}

B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__equiv(base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api other) {
  if (art_Art_BridgeId__inequiv(this->id, other->id)) return F;
  if (art_Art_PortId__inequiv(this->consume_Id, other->consume_Id)) return F;
  return T;
}

B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__ne(base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api other);
B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__inequiv(base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api other);

void base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_string_(STACK_FRAME String result, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this) {
  DeclNewStackFrame(caller, "consumer_t_i_Api.scala", "base.test_event_port_periodic_domains.consumer_t_i_Operational_Api", "string", 0);
  String_string_(SF result, string("consumer_t_i_Operational_Api("));
  String sep = string(", ");
  art_Art_BridgeId_string_(SF result, this->id);
  String_string_(SF result, sep);
  art_Art_PortId_string_(SF result, this->consume_Id);
  String_string_(SF result, string(")"));
}

void base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_cprint(base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("consumer_t_i_Operational_Api("), isOut);
  String sep = string(", ");
  art_Art_BridgeId_cprint(this->id, isOut);
  String_cprint(sep, isOut);
  art_Art_PortId_cprint(this->consume_Id, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__is(STACK_FRAME void* this);
base_test_event_port_periodic_domains_consumer_t_i_Operational_Api base_test_event_port_periodic_domains_consumer_t_i_Operational_Api__as(STACK_FRAME void *this);

void base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_apply(STACK_FRAME base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, art_Art_BridgeId id, art_Art_PortId consume_Id) {
  DeclNewStackFrame(caller, "consumer_t_i_Api.scala", "base.test_event_port_periodic_domains.consumer_t_i_Operational_Api", "apply", 0);
  this->id = id;
  this->consume_Id = consume_Id;
}

inline B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_extract_45_12_8E9F45(STACK_FRAME_SF Option_8E9F45 t_0, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this) {
  if (!Some_D29615__is(SF t_0)) return F;
  if (!art_Empty__is(SF Some_D29615_value_(Some_D29615__as(SF t_0)))) return F;
  return T;
}

inline B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_extract_46_12_8E9F45(STACK_FRAME_SF Option_8E9F45 t_0, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this, art_DataContent *_v_46_17) {
  if (!Some_D29615__is(SF t_0)) return F;
  *_v_46_17 = (art_DataContent) Some_D29615_value_(Some_D29615__as(SF t_0));
  return T;
}

inline B base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_extract_49_12_8E9F45(STACK_FRAME_SF Option_8E9F45 t_0, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this) {
  return T;
}

void base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_(STACK_FRAME Option_C622DB result, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api this) {
  DeclNewStackFrame(caller, "consumer_t_i_Api.scala", "base.test_event_port_periodic_domains.consumer_t_i_Operational_Api", "get_consume", 0);

  sfUpdateLoc(44);
  Option_C622DB value;
  DeclNewOption_8E9F45(t_1);
  art_Art_getValue(SF (Option_8E9F45) &t_1, base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_consume_Id_(this));
  Option_8E9F45 t_0 = ((Option_8E9F45) &t_1);
  B match_44_37 = F;
  if (!match_44_37) {
    match_44_37 = base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_extract_45_12_8E9F45(SF t_0, this);
    if (match_44_37) {
      DeclNewart_Empty(t_3);
      art_Empty_apply(SF &t_3);
      DeclNewSome_4782C6(t_2);
      Some_4782C6_apply(SF &t_2, (art_Empty) (&t_3));
      value = (Option_C622DB) (&t_2);
    }
  }
  if (!match_44_37) {
    art_DataContent v_46_17;
    match_44_37 = base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_extract_46_12_8E9F45(SF t_0, this, &v_46_17);
    if (match_44_37) {

      sfUpdateLoc(47);
      {
        DeclNewString(t_4);
        String_string_(SF (String) &t_4, string("Unexpected payload on port consume.  Expecting 'Empty' but received "));
        art_DataContent_string_(SF (String) &t_4, v_46_17);
        String_string_(SF (String) &t_4, string(""));
        art_Art_logError(SF base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_id_(this), (String) ((String) &t_4));
      }
      DeclNewNone_ED72E1(t_5);
      None_ED72E1_apply(SF &t_5);
      value = (Option_C622DB) (&t_5);
    }
  }
  if (!match_44_37) {
    match_44_37 = base_test_event_port_periodic_domains_consumer_t_i_Operational_Api_get_consume_extract_49_12_8E9F45(SF t_0, this);
    if (match_44_37) {
      DeclNewNone_ED72E1(t_6);
      None_ED72E1_apply(SF &t_6);
      value = (Option_C622DB) (&t_6);
    }
  }
  sfAssert(match_44_37, "Error when pattern matching.");
  Type_assign(result, value, sizeof(union Option_C622DB));
  return;
}