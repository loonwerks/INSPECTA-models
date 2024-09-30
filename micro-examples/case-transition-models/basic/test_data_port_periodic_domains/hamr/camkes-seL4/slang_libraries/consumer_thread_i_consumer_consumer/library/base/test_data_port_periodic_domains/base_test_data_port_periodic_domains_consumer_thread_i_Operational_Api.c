#include <all.h>

// base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api

B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__eq(base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api other) {
  if (art_Art_BridgeId__ne(this->id, other->id)) return F;
  if (art_Art_PortId__ne(this->read_port_Id, other->read_port_Id)) return F;
  return T;
}

B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__equiv(base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api other) {
  if (art_Art_BridgeId__inequiv(this->id, other->id)) return F;
  if (art_Art_PortId__inequiv(this->read_port_Id, other->read_port_Id)) return F;
  return T;
}

B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__ne(base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api other);
B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__inequiv(base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api other);

void base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_string_(STACK_FRAME String result, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this) {
  DeclNewStackFrame(caller, "consumer_thread_i_Api.scala", "base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api", "string", 0);
  String_string_(SF result, string("consumer_thread_i_Operational_Api("));
  String sep = string(", ");
  art_Art_BridgeId_string_(SF result, this->id);
  String_string_(SF result, sep);
  art_Art_PortId_string_(SF result, this->read_port_Id);
  String_string_(SF result, string(")"));
}

void base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_cprint(base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  void *sf = NULL;
  String_cprint(string("consumer_thread_i_Operational_Api("), isOut);
  String sep = string(", ");
  art_Art_BridgeId_cprint(this->id, isOut);
  String_cprint(sep, isOut);
  art_Art_PortId_cprint(this->read_port_Id, isOut);
  String_cprint(string(")"), isOut);
  #endif
}

B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__is(STACK_FRAME void* this);
base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api__as(STACK_FRAME void *this);

void base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_apply(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, art_Art_BridgeId id, art_Art_PortId read_port_Id) {
  DeclNewStackFrame(caller, "consumer_thread_i_Api.scala", "base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api", "apply", 0);
  this->id = id;
  this->read_port_Id = read_port_Id;
}

inline B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_extract_45_12_8E9F45(STACK_FRAME_SF Option_8E9F45 t_0, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, S8 *_v_45_46) {
  if (!Some_D29615__is(SF t_0)) return F;
  if (!base_Base_Types_Integer_8_Payload__is(SF Some_D29615_value_(Some_D29615__as(SF t_0)))) return F;
  *_v_45_46 = base_Base_Types_Integer_8_Payload_value_(base_Base_Types_Integer_8_Payload__as(SF Some_D29615_value_(Some_D29615__as(SF t_0))));
  return T;
}

inline B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_extract_46_12_8E9F45(STACK_FRAME_SF Option_8E9F45 t_0, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this, art_DataContent *_v_46_17) {
  if (!Some_D29615__is(SF t_0)) return F;
  *_v_46_17 = (art_DataContent) Some_D29615_value_(Some_D29615__as(SF t_0));
  return T;
}

inline B base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_extract_49_12_8E9F45(STACK_FRAME_SF Option_8E9F45 t_0, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this) {
  return T;
}

void base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_(STACK_FRAME Option_FC3847 result, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api this) {
  DeclNewStackFrame(caller, "consumer_thread_i_Api.scala", "base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api", "get_read_port", 0);

  sfUpdateLoc(44);
  Option_FC3847 value;
  DeclNewOption_8E9F45(t_1);
  art_Art_getValue(SF (Option_8E9F45) &t_1, base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_read_port_Id_(this));
  Option_8E9F45 t_0 = ((Option_8E9F45) &t_1);
  B match_44_48 = F;
  if (!match_44_48) {
    S8 v_45_46;
    match_44_48 = base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_extract_45_12_8E9F45(SF t_0, this, &v_45_46);
    if (match_44_48) {
      DeclNewSome_240B2F(t_2);
      Some_240B2F_apply(SF &t_2, v_45_46);
      value = (Option_FC3847) (&t_2);
    }
  }
  if (!match_44_48) {
    art_DataContent v_46_17;
    match_44_48 = base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_extract_46_12_8E9F45(SF t_0, this, &v_46_17);
    if (match_44_48) {

      sfUpdateLoc(47);
      {
        DeclNewString(t_3);
        String_string_(SF (String) &t_3, string("Unexpected payload on port read_port.  Expecting 'Base_Types.Integer_8_Payload' but received "));
        art_DataContent_string_(SF (String) &t_3, v_46_17);
        String_string_(SF (String) &t_3, string(""));
        art_Art_logError(SF base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_id_(this), (String) ((String) &t_3));
      }
      DeclNewNone_E19B61(t_4);
      None_E19B61_apply(SF &t_4);
      value = (Option_FC3847) (&t_4);
    }
  }
  if (!match_44_48) {
    match_44_48 = base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api_get_read_port_extract_49_12_8E9F45(SF t_0, this);
    if (match_44_48) {
      DeclNewNone_E19B61(t_5);
      None_E19B61_apply(SF &t_5);
      value = (Option_FC3847) (&t_5);
    }
  }
  sfAssert(match_44_48, "Error when pattern matching.");
  Type_assign(result, value, sizeof(union Option_FC3847));
  return;
}