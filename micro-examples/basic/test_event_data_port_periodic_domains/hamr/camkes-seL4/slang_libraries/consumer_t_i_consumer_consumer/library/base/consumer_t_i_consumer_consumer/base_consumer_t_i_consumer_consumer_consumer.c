#include <all.h>

B base_consumer_t_i_consumer_consumer_consumer_initialized_ = F;

struct base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge _base_consumer_t_i_consumer_consumer_consumer_consumerBridge;
union art_Bridge_EntryPoints _base_consumer_t_i_consumer_consumer_consumer_entryPoints;
union Option_8E9F45 _base_consumer_t_i_consumer_consumer_consumer_noData;
art_Art_PortId _base_consumer_t_i_consumer_consumer_consumer_read_port_id;
union Option_8E9F45 _base_consumer_t_i_consumer_consumer_consumer_read_port_port;

void base_consumer_t_i_consumer_consumer_consumer_init(STACK_FRAME_ONLY) {
  if (base_consumer_t_i_consumer_consumer_consumer_initialized_) return;
  base_consumer_t_i_consumer_consumer_consumer_initialized_ = T;
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "<init>", 0);
  base_consumer_t_i_consumer_consumer_consumer_init_consumerBridge(SF_LAST);
  base_consumer_t_i_consumer_consumer_consumer_init_entryPoints(SF_LAST);
  base_consumer_t_i_consumer_consumer_consumer_init_noData(SF_LAST);
  base_consumer_t_i_consumer_consumer_consumer_init_read_port_id(SF_LAST);
  base_consumer_t_i_consumer_consumer_consumer_init_read_port_port(SF_LAST);
}

base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge base_consumer_t_i_consumer_consumer_consumer_consumerBridge(STACK_FRAME_ONLY) {
  base_consumer_t_i_consumer_consumer_consumer_init(CALLER_LAST);
  return (base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge) &_base_consumer_t_i_consumer_consumer_consumer_consumerBridge;
}

art_Bridge_EntryPoints base_consumer_t_i_consumer_consumer_consumer_entryPoints(STACK_FRAME_ONLY) {
  base_consumer_t_i_consumer_consumer_consumer_init(CALLER_LAST);
  return (art_Bridge_EntryPoints) &_base_consumer_t_i_consumer_consumer_consumer_entryPoints;
}

Option_8E9F45 base_consumer_t_i_consumer_consumer_consumer_noData(STACK_FRAME_ONLY) {
  base_consumer_t_i_consumer_consumer_consumer_init(CALLER_LAST);
  return (Option_8E9F45) &_base_consumer_t_i_consumer_consumer_consumer_noData;
}

art_Art_PortId base_consumer_t_i_consumer_consumer_consumer_read_port_id(STACK_FRAME_ONLY) {
  base_consumer_t_i_consumer_consumer_consumer_init(CALLER_LAST);
  return _base_consumer_t_i_consumer_consumer_consumer_read_port_id;
}

Option_8E9F45 base_consumer_t_i_consumer_consumer_consumer_read_port_port(STACK_FRAME_ONLY) {
  base_consumer_t_i_consumer_consumer_consumer_init(CALLER_LAST);
  return (Option_8E9F45) &_base_consumer_t_i_consumer_consumer_consumer_read_port_port;
}

void base_consumer_t_i_consumer_consumer_consumer_read_port_port_a(STACK_FRAME Option_8E9F45 p_read_port_port) {
  base_consumer_t_i_consumer_consumer_consumer_init(CALLER_LAST);
  Type_assign(&_base_consumer_t_i_consumer_consumer_consumer_read_port_port, p_read_port_port, sizeof(union Option_8E9F45));
}

Z base_consumer_t_i_consumer_consumer_consumer_main(STACK_FRAME IS_948B60 args) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "main", 0);

  sfUpdateLoc(79);
  {
    base_consumer_t_i_consumer_consumer_consumer_initialiseArchitecture(SF_LAST);
  }

  sfUpdateLoc(80);
  {
    base_consumer_t_i_consumer_consumer_consumer_initialiseEntryPoint(SF_LAST);
  }

  sfUpdateLoc(81);
  {
    base_consumer_t_i_consumer_consumer_consumer_computeEntryPoint(SF_LAST);
  }

  sfUpdateLoc(82);
  {
    base_consumer_t_i_consumer_consumer_consumer_finaliseEntryPoint(SF_LAST);
  }

  sfUpdateLoc(84);
  {
    base_consumer_t_i_consumer_consumer_consumer_touch(SF_LAST);
  }
  return Z_C(0);
}

Unit base_consumer_t_i_consumer_consumer_consumer_initialiseArchitecture(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "initialiseArchitecture", 0);
}

Unit base_consumer_t_i_consumer_consumer_consumer_initialiseEntryPoint(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "initialiseEntryPoint", 0);

  sfUpdateLoc(70);
  {
    art_Bridge_EntryPoints_initialise_(SF base_consumer_t_i_consumer_consumer_consumer_entryPoints(SF_LAST));
  }
}

Unit base_consumer_t_i_consumer_consumer_consumer_computeEntryPoint(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "computeEntryPoint", 0);

  sfUpdateLoc(72);
  {
    art_Bridge_EntryPoints_compute_(SF base_consumer_t_i_consumer_consumer_consumer_entryPoints(SF_LAST));
  }
}

Unit base_consumer_t_i_consumer_consumer_consumer_finaliseEntryPoint(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "finaliseEntryPoint", 0);

  sfUpdateLoc(74);
  {
    art_Bridge_EntryPoints_finalise_(SF base_consumer_t_i_consumer_consumer_consumer_entryPoints(SF_LAST));
  }
}

Unit base_consumer_t_i_consumer_consumer_consumer_touch_printDataContent(STACK_FRAME art_DataContent a) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer.touch", "printDataContent", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(98);
  {
    DeclNewString(t_0);
    String_string_(SF (String) &t_0, string(""));
    art_DataContent_string_(SF (String) &t_0, a);
    String_string_(SF (String) &t_0, string(""));
    String_cprint(((String) &t_0), T);
    cprintln(T);
    cflush(T);
  }

  #endif
}

Unit base_consumer_t_i_consumer_consumer_consumer_touch(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "touch", 0);

  sfUpdateLoc(90);
  B t_0;
  {
    t_0 = F;
  }
  if (t_0) {

    sfUpdateLoc(91);
    {
      base_TranspilerToucher_touch(SF_LAST);
    }

    sfUpdateLoc(94);
    DeclNewMBox2_43CC67(_mbox2Boolean_Payload);
    MBox2_43CC67 mbox2Boolean_Payload = (MBox2_43CC67) &_mbox2Boolean_Payload;
    {
      DeclNewbase_Base_Types_Boolean_Payload(t_2);
      base_Base_Types_Boolean_Payload_apply(SF &t_2, T);
      DeclNewMBox2_43CC67(t_1);
      MBox2_43CC67_apply(SF &t_1, art_Art_PortId_C(0), (art_DataContent) (&t_2));
      Type_assign(mbox2Boolean_Payload, (&t_1), sizeof(struct MBox2_43CC67));
    }

    sfUpdateLoc(95);
    DeclNewMBox2_37E193(_mbox2OptionDataContent);
    MBox2_37E193 mbox2OptionDataContent = (MBox2_37E193) &_mbox2OptionDataContent;
    {
      DeclNewNone_964667(t_4);
      None_964667_apply(SF &t_4);
      DeclNewMBox2_37E193(t_3);
      MBox2_37E193_apply(SF &t_3, art_Art_PortId_C(0), (Option_8E9F45) (&t_4));
      Type_assign(mbox2OptionDataContent, (&t_3), sizeof(struct MBox2_37E193));
    }

    sfUpdateLoc(100);
    {
      S8 t_6 = base_Base_Types_Integer_8_example(SF_LAST);
      DeclNewbase_Base_Types_Integer_8_Payload(t_5);
      base_Base_Types_Integer_8_Payload_apply(SF &t_5, t_6);
      base_consumer_t_i_consumer_consumer_consumer_touch_printDataContent(SF (art_DataContent) (&t_5));
    }

    sfUpdateLoc(101);
    {
      DeclNewart_Empty(t_7);
      art_Empty_apply(SF &t_7);
      base_consumer_t_i_consumer_consumer_consumer_touch_printDataContent(SF (art_DataContent) (&t_7));
    }

    sfUpdateLoc(103);
    {
      DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api(t_8);
      Option_0CB775_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &t_8, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_initialization_api(SF_LAST));
      base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api_logInfo_(SF ((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &t_8), (String) string(""));
    }

    sfUpdateLoc(104);
    {
      DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api(t_9);
      Option_0CB775_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &t_9, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_initialization_api(SF_LAST));
      base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api_logDebug_(SF ((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &t_9), (String) string(""));
    }

    sfUpdateLoc(105);
    {
      DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api(t_10);
      Option_0CB775_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &t_10, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_initialization_api(SF_LAST));
      base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api_logError_(SF ((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &t_10), (String) string(""));
    }

    sfUpdateLoc(106);
    {
      DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api(t_11);
      Option_F1A21C_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_11, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_operational_api(SF_LAST));
      base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api_logInfo_(SF ((base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_11), (String) string(""));
    }

    sfUpdateLoc(107);
    {
      DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api(t_12);
      Option_F1A21C_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_12, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_operational_api(SF_LAST));
      base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api_logDebug_(SF ((base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_12), (String) string(""));
    }

    sfUpdateLoc(108);
    {
      DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api(t_13);
      Option_F1A21C_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_13, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_operational_api(SF_LAST));
      base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api_logError_(SF ((base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_13), (String) string(""));
    }

    sfUpdateLoc(109);
    Option_FC3847 apiUsage_read_port;
    DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api(t_14);
    Option_F1A21C_get_(SF (base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_14, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_c_operational_api(SF_LAST));
    DeclNewOption_FC3847(t_15);
    base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api_get_read_port_(SF (Option_FC3847) &t_15, ((base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &t_14));
    apiUsage_read_port = (Option_FC3847) ((Option_FC3847) &t_15);
  }
}

Unit base_consumer_t_i_consumer_consumer_consumer_logInfo(STACK_FRAME String title, String msg) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "logInfo", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(114);
  {
    String_cprint(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_name_(base_consumer_t_i_consumer_consumer_consumer_consumerBridge(SF_LAST)), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(115);
  {
    String_cprint(string(": "), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(116);
  {
    String_cprint(msg, T);
    cprintln(T);
    cflush(T);
  }

  #endif
}

Unit base_consumer_t_i_consumer_consumer_consumer_logDebug(STACK_FRAME String title, String msg) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "logDebug", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(126);
  {
    String_cprint(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_name_(base_consumer_t_i_consumer_consumer_consumer_consumerBridge(SF_LAST)), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(127);
  {
    String_cprint(string(": "), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(128);
  {
    String_cprint(msg, T);
    cprintln(T);
    cflush(T);
  }

  #endif
}

Unit base_consumer_t_i_consumer_consumer_consumer_logError(STACK_FRAME String title, String msg) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "logError", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(120);
  {
    String_cprint(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_name_(base_consumer_t_i_consumer_consumer_consumer_consumerBridge(SF_LAST)), F);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(121);
  {
    String_cprint(string(": "), F);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(122);
  {
    String_cprint(msg, F);
    cprintln(F);
    cflush(F);
  }

  #endif
}

void base_consumer_t_i_consumer_consumer_consumer_getValue(STACK_FRAME Option_8E9F45 result, art_Art_PortId portId) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "getValue", 0);

  sfUpdateLoc(43);
  B t_0;
  {
    t_0 = art_Art_PortId__eq(portId, base_consumer_t_i_consumer_consumer_consumer_read_port_id(SF_LAST));
  }
  if (t_0) {
    Type_assign(result, base_consumer_t_i_consumer_consumer_consumer_read_port_port(SF_LAST), sizeof(union Option_8E9F45));
    return;
  } else {

    sfUpdateLoc(46);
    {
      DeclNewString(t_1);
      String t_2 = (String) &t_1;
      DeclNewString(t_3);
      String_string_(SF (String) &t_3, string("Unexpected: consumer.getValue called with: "));
      art_Art_PortId_string_(SF (String) &t_3, portId);
      String_string_(SF (String) &t_3, string(""));
      String_string_(SF t_2, ((String) &t_3));
      sfAbort(t_2->value);
      abort();
    }
  }
}

Unit base_consumer_t_i_consumer_consumer_consumer_sendOutput(STACK_FRAME IS_D10119 eventPortIds, IS_D10119 dataPortIds) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "sendOutput", 0);
}

Unit base_consumer_t_i_consumer_consumer_consumer_receiveInput(STACK_FRAME IS_D10119 eventPortIds, IS_D10119 dataPortIds) {
  DeclNewStackFrame(caller, "consumer.scala", "base.consumer_t_i_consumer_consumer.consumer", "receiveInput", 0);

  sfUpdateLoc(53);
  {
    DeclNewOption_8E9F45(t_0);
    base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_seL4Nix_read_port_Receive(SF (Option_8E9F45) &t_0);
    base_consumer_t_i_consumer_consumer_consumer_read_port_port_a(SF (Option_8E9F45) ((Option_8E9F45) &t_0));
  }
}

void base_consumer_t_i_consumer_consumer_consumer_init_consumerBridge(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(18);
  {

    sfUpdateLoc(19);
    art_Port_A2C07C read_port;
    DeclNewart_Port_A2C07C(t_0);
    art_Port_A2C07C_apply(SF &t_0, art_Art_PortId_C(0), (String) string("top_impl_Instance_consumer_consumer_read_port"), art_PortMode_Type_EventIn);
    read_port = (art_Port_A2C07C) (&t_0);
    DeclNewart_DispatchPropertyProtocol_Periodic(t_2);
    art_DispatchPropertyProtocol_Periodic_apply(SF &t_2, Z_C(1000));
    DeclNewNone_CFD2B0(t_3);
    None_CFD2B0_apply(SF &t_3);
    DeclNewbase_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge(t_1);
    base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_apply(SF &t_1, art_Art_BridgeId_C(0), (String) string("top_impl_Instance_consumer_consumer"), (art_DispatchPropertyProtocol) (&t_2), (Option_0247A1) (&t_3), (art_Port_A2C07C) read_port);
    Type_assign(&_base_consumer_t_i_consumer_consumer_consumer_consumerBridge, (&t_1), sizeof(struct base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge));
  }
};

void base_consumer_t_i_consumer_consumer_consumer_init_entryPoints(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(31);
  Type_assign(&_base_consumer_t_i_consumer_consumer_consumer_entryPoints, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_entryPoints_(base_consumer_t_i_consumer_consumer_consumer_consumerBridge(SF_LAST)), sizeof(union art_Bridge_EntryPoints));
};

void base_consumer_t_i_consumer_consumer_consumer_init_noData(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(32);
  DeclNewNone_964667(t_4);
  None_964667_apply(SF &t_4);
  Type_assign(&_base_consumer_t_i_consumer_consumer_consumer_noData, (&t_4), sizeof(struct None_964667));
};

void base_consumer_t_i_consumer_consumer_consumer_init_read_port_id(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(35);
  _base_consumer_t_i_consumer_consumer_consumer_read_port_id = art_Port_A2C07C_id_(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_read_port_(base_consumer_t_i_consumer_consumer_consumer_consumerBridge(SF_LAST)));
};

void base_consumer_t_i_consumer_consumer_consumer_init_read_port_port(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(36);
  Type_assign(&_base_consumer_t_i_consumer_consumer_consumer_read_port_port, base_consumer_t_i_consumer_consumer_consumer_noData(SF_LAST), sizeof(union Option_8E9F45));
};