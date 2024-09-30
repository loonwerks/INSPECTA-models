#include <all.h>

// base.test_data_port_periodic_domains.producer_thread_i_Api

B base_test_data_port_periodic_domains_producer_thread_i_Api__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case Tbase_test_data_port_periodic_domains_producer_thread_i_Initialization_Api: return T;
    case Tbase_test_data_port_periodic_domains_producer_thread_i_Operational_Api: return T;
    default: return F;
  }
}

base_test_data_port_periodic_domains_producer_thread_i_Api base_test_data_port_periodic_domains_producer_thread_i_Api__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case Tbase_test_data_port_periodic_domains_producer_thread_i_Initialization_Api: break;
    case Tbase_test_data_port_periodic_domains_producer_thread_i_Operational_Api: break;
    default:
      fprintf(stderr, "Invalid cast from %s to base.test_data_port_periodic_domains.producer_thread_i_Api.", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (base_test_data_port_periodic_domains_producer_thread_i_Api) this;
}

void base_test_data_port_periodic_domains_producer_thread_i_Api_string_(STACK_FRAME String result, base_test_data_port_periodic_domains_producer_thread_i_Api this);

Unit base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_logInfo_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api this, String msg) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "logInfo", 0);

  sfUpdateLoc(31);
  {
    art_Art_logInfo(SF base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_id_(this), (String) msg);
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_logDebug_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api this, String msg) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "logDebug", 0);

  sfUpdateLoc(35);
  {
    art_Art_logDebug(SF base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_id_(this), (String) msg);
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_logError_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api this, String msg) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "logError", 0);

  sfUpdateLoc(39);
  {
    art_Art_logError(SF base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_id_(this), (String) msg);
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_logInfo_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, String msg) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "logInfo", 0);

  sfUpdateLoc(31);
  {
    art_Art_logInfo(SF base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_id_(this), (String) msg);
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_logDebug_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, String msg) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "logDebug", 0);

  sfUpdateLoc(35);
  {
    art_Art_logDebug(SF base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_id_(this), (String) msg);
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_logError_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, String msg) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "logError", 0);

  sfUpdateLoc(39);
  {
    art_Art_logError(SF base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_id_(this), (String) msg);
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_put_write_port_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api this, S8 value) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "put_write_port", 0);

  sfUpdateLoc(27);
  {
    DeclNewbase_Base_Types_Integer_8_Payload(t_0);
    base_Base_Types_Integer_8_Payload_apply(SF &t_0, value);
    art_Art_putValue(SF base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_write_port_Id_(this), (art_DataContent) (&t_0));
  }
}

Unit base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_put_write_port_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api this, S8 value) {
  DeclNewStackFrame(caller, "producer_thread_i_Api.scala", "base.test_data_port_periodic_domains.producer_thread_i_Api", "put_write_port", 0);

  sfUpdateLoc(27);
  {
    DeclNewbase_Base_Types_Integer_8_Payload(t_0);
    base_Base_Types_Integer_8_Payload_apply(SF &t_0, value);
    art_Art_putValue(SF base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_write_port_Id_(this), (art_DataContent) (&t_0));
  }
}