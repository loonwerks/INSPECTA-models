#include <all.h>

B base_producer_thread_i_producer_producer_producer_initialized_ = F;

struct base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge _base_producer_thread_i_producer_producer_producer_producerBridge;
union art_Bridge_EntryPoints _base_producer_thread_i_producer_producer_producer_entryPoints;
union Option_8E9F45 _base_producer_thread_i_producer_producer_producer_noData;
art_Art_PortId _base_producer_thread_i_producer_producer_producer_write_port_id;
union Option_8E9F45 _base_producer_thread_i_producer_producer_producer_write_port_port;

void base_producer_thread_i_producer_producer_producer_init(STACK_FRAME_ONLY) {
  if (base_producer_thread_i_producer_producer_producer_initialized_) return;
  base_producer_thread_i_producer_producer_producer_initialized_ = T;
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "<init>", 0);
  base_producer_thread_i_producer_producer_producer_init_producerBridge(SF_LAST);
  base_producer_thread_i_producer_producer_producer_init_entryPoints(SF_LAST);
  base_producer_thread_i_producer_producer_producer_init_noData(SF_LAST);
  base_producer_thread_i_producer_producer_producer_init_write_port_id(SF_LAST);
  base_producer_thread_i_producer_producer_producer_init_write_port_port(SF_LAST);
}

base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge base_producer_thread_i_producer_producer_producer_producerBridge(STACK_FRAME_ONLY) {
  base_producer_thread_i_producer_producer_producer_init(CALLER_LAST);
  return (base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge) &_base_producer_thread_i_producer_producer_producer_producerBridge;
}

art_Bridge_EntryPoints base_producer_thread_i_producer_producer_producer_entryPoints(STACK_FRAME_ONLY) {
  base_producer_thread_i_producer_producer_producer_init(CALLER_LAST);
  return (art_Bridge_EntryPoints) &_base_producer_thread_i_producer_producer_producer_entryPoints;
}

Option_8E9F45 base_producer_thread_i_producer_producer_producer_noData(STACK_FRAME_ONLY) {
  base_producer_thread_i_producer_producer_producer_init(CALLER_LAST);
  return (Option_8E9F45) &_base_producer_thread_i_producer_producer_producer_noData;
}

art_Art_PortId base_producer_thread_i_producer_producer_producer_write_port_id(STACK_FRAME_ONLY) {
  base_producer_thread_i_producer_producer_producer_init(CALLER_LAST);
  return _base_producer_thread_i_producer_producer_producer_write_port_id;
}

Option_8E9F45 base_producer_thread_i_producer_producer_producer_write_port_port(STACK_FRAME_ONLY) {
  base_producer_thread_i_producer_producer_producer_init(CALLER_LAST);
  return (Option_8E9F45) &_base_producer_thread_i_producer_producer_producer_write_port_port;
}

void base_producer_thread_i_producer_producer_producer_write_port_port_a(STACK_FRAME Option_8E9F45 p_write_port_port) {
  base_producer_thread_i_producer_producer_producer_init(CALLER_LAST);
  Type_assign(&_base_producer_thread_i_producer_producer_producer_write_port_port, p_write_port_port, sizeof(union Option_8E9F45));
}

Z base_producer_thread_i_producer_producer_producer_main(STACK_FRAME IS_948B60 args) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "main", 0);

  sfUpdateLoc(82);
  {
    base_producer_thread_i_producer_producer_producer_initialiseArchitecture(SF_LAST);
  }

  sfUpdateLoc(83);
  {
    base_producer_thread_i_producer_producer_producer_initialiseEntryPoint(SF_LAST);
  }

  sfUpdateLoc(84);
  {
    base_producer_thread_i_producer_producer_producer_computeEntryPoint(SF_LAST);
  }

  sfUpdateLoc(85);
  {
    base_producer_thread_i_producer_producer_producer_finaliseEntryPoint(SF_LAST);
  }

  sfUpdateLoc(87);
  {
    base_producer_thread_i_producer_producer_producer_touch(SF_LAST);
  }
  return Z_C(0);
}

Unit base_producer_thread_i_producer_producer_producer_initialiseArchitecture(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "initialiseArchitecture", 0);
}

Unit base_producer_thread_i_producer_producer_producer_initialiseEntryPoint(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "initialiseEntryPoint", 0);

  sfUpdateLoc(73);
  {
    art_Bridge_EntryPoints_initialise_(SF base_producer_thread_i_producer_producer_producer_entryPoints(SF_LAST));
  }
}

Unit base_producer_thread_i_producer_producer_producer_computeEntryPoint(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "computeEntryPoint", 0);

  sfUpdateLoc(75);
  {
    art_Bridge_EntryPoints_compute_(SF base_producer_thread_i_producer_producer_producer_entryPoints(SF_LAST));
  }
}

Unit base_producer_thread_i_producer_producer_producer_finaliseEntryPoint(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "finaliseEntryPoint", 0);

  sfUpdateLoc(77);
  {
    art_Bridge_EntryPoints_finalise_(SF base_producer_thread_i_producer_producer_producer_entryPoints(SF_LAST));
  }
}

Unit base_producer_thread_i_producer_producer_producer_touch_printDataContent(STACK_FRAME art_DataContent a) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer.touch", "printDataContent", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(101);
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

Unit base_producer_thread_i_producer_producer_producer_touch(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "touch", 0);

  sfUpdateLoc(93);
  B t_0;
  {
    t_0 = F;
  }
  if (t_0) {

    sfUpdateLoc(94);
    {
      base_TranspilerToucher_touch(SF_LAST);
    }

    sfUpdateLoc(97);
    DeclNewMBox2_43CC67(_mbox2Boolean_Payload);
    MBox2_43CC67 mbox2Boolean_Payload = (MBox2_43CC67) &_mbox2Boolean_Payload;
    {
      DeclNewbase_Base_Types_Boolean_Payload(t_2);
      base_Base_Types_Boolean_Payload_apply(SF &t_2, T);
      DeclNewMBox2_43CC67(t_1);
      MBox2_43CC67_apply(SF &t_1, art_Art_PortId_C(0), (art_DataContent) (&t_2));
      Type_assign(mbox2Boolean_Payload, (&t_1), sizeof(struct MBox2_43CC67));
    }

    sfUpdateLoc(98);
    DeclNewMBox2_37E193(_mbox2OptionDataContent);
    MBox2_37E193 mbox2OptionDataContent = (MBox2_37E193) &_mbox2OptionDataContent;
    {
      DeclNewNone_964667(t_4);
      None_964667_apply(SF &t_4);
      DeclNewMBox2_37E193(t_3);
      MBox2_37E193_apply(SF &t_3, art_Art_PortId_C(0), (Option_8E9F45) (&t_4));
      Type_assign(mbox2OptionDataContent, (&t_3), sizeof(struct MBox2_37E193));
    }

    sfUpdateLoc(103);
    {
      S8 t_6 = base_Base_Types_Integer_8_example(SF_LAST);
      DeclNewbase_Base_Types_Integer_8_Payload(t_5);
      base_Base_Types_Integer_8_Payload_apply(SF &t_5, t_6);
      base_producer_thread_i_producer_producer_producer_touch_printDataContent(SF (art_DataContent) (&t_5));
    }

    sfUpdateLoc(104);
    {
      DeclNewart_Empty(t_7);
      art_Empty_apply(SF &t_7);
      base_producer_thread_i_producer_producer_producer_touch_printDataContent(SF (art_DataContent) (&t_7));
    }

    sfUpdateLoc(106);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Initialization_Api(t_8);
      Option_B8220B_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_8, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_initialization_api(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_logInfo_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_8), (String) string(""));
    }

    sfUpdateLoc(107);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Initialization_Api(t_9);
      Option_B8220B_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_9, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_initialization_api(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_logDebug_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_9), (String) string(""));
    }

    sfUpdateLoc(108);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Initialization_Api(t_10);
      Option_B8220B_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_10, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_initialization_api(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_logError_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_10), (String) string(""));
    }

    sfUpdateLoc(109);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Operational_Api(t_11);
      Option_D7201D_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_11, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_operational_api(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_logInfo_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_11), (String) string(""));
    }

    sfUpdateLoc(110);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Operational_Api(t_12);
      Option_D7201D_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_12, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_operational_api(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_logDebug_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_12), (String) string(""));
    }

    sfUpdateLoc(111);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Operational_Api(t_13);
      Option_D7201D_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_13, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_operational_api(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_logError_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_13), (String) string(""));
    }

    sfUpdateLoc(112);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Initialization_Api(t_14);
      Option_B8220B_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_14, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_initialization_api(SF_LAST));
      S8 t_15 = base_Base_Types_Integer_8_example(SF_LAST);
      base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api_put_write_port_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) &t_14), t_15);
    }

    sfUpdateLoc(113);
    {
      DeclNewbase_test_data_port_periodic_domains_producer_thread_i_Operational_Api(t_16);
      Option_D7201D_get_(SF (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_16, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_c_operational_api(SF_LAST));
      S8 t_17 = base_Base_Types_Integer_8_example(SF_LAST);
      base_test_data_port_periodic_domains_producer_thread_i_Operational_Api_put_write_port_(SF ((base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) &t_16), t_17);
    }
  }
}

Unit base_producer_thread_i_producer_producer_producer_logInfo(STACK_FRAME String title, String msg) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "logInfo", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(118);
  {
    String_cprint(base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_name_(base_producer_thread_i_producer_producer_producer_producerBridge(SF_LAST)), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(119);
  {
    String_cprint(string(": "), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(120);
  {
    String_cprint(msg, T);
    cprintln(T);
    cflush(T);
  }

  #endif
}

Unit base_producer_thread_i_producer_producer_producer_logDebug(STACK_FRAME String title, String msg) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "logDebug", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(130);
  {
    String_cprint(base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_name_(base_producer_thread_i_producer_producer_producer_producerBridge(SF_LAST)), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(131);
  {
    String_cprint(string(": "), T);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(132);
  {
    String_cprint(msg, T);
    cprintln(T);
    cflush(T);
  }

  #endif
}

Unit base_producer_thread_i_producer_producer_producer_logError(STACK_FRAME String title, String msg) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "logError", 0);

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(124);
  {
    String_cprint(base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_name_(base_producer_thread_i_producer_producer_producer_producerBridge(SF_LAST)), F);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(125);
  {
    String_cprint(string(": "), F);
  }

  #endif

  #ifndef SIREUM_NO_PRINT

  sfUpdateLoc(126);
  {
    String_cprint(msg, F);
    cprintln(F);
    cflush(F);
  }

  #endif
}

Unit base_producer_thread_i_producer_producer_producer_putValue(STACK_FRAME art_Art_PortId portId, art_DataContent data) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "putValue", 0);

  sfUpdateLoc(53);
  B t_0;
  {
    t_0 = art_Art_PortId__eq(portId, base_producer_thread_i_producer_producer_producer_write_port_id(SF_LAST));
  }
  if (t_0) {

    sfUpdateLoc(54);
    {
      DeclNewSome_D29615(t_1);
      Some_D29615_apply(SF &t_1, (art_DataContent) data);
      base_producer_thread_i_producer_producer_producer_write_port_port_a(SF (Option_8E9F45) (&t_1));
    }
  } else {

    sfUpdateLoc(56);
    {
      DeclNewString(t_2);
      String t_3 = (String) &t_2;
      DeclNewString(t_4);
      String_string_(SF (String) &t_4, string("Unexpected: producer.putValue called with: "));
      art_Art_PortId_string_(SF (String) &t_4, portId);
      String_string_(SF (String) &t_4, string(""));
      String_string_(SF t_3, ((String) &t_4));
      sfAbort(t_3->value);
      abort();
    }
  }
}

Unit base_producer_thread_i_producer_producer_producer_sendOutput(STACK_FRAME IS_D10119 eventPortIds, IS_D10119 dataPortIds) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "sendOutput", 0);

  sfUpdateLoc(63);
  B t_0;
  {
    B t_1 = Option_8E9F45_nonEmpty_(SF base_producer_thread_i_producer_producer_producer_write_port_port(SF_LAST));
    t_0 = t_1;
  }
  if (t_0) {

    sfUpdateLoc(64);
    {
      DeclNewart_DataContent(t_2);
      Option_8E9F45_get_(SF (art_DataContent) &t_2, base_producer_thread_i_producer_producer_producer_write_port_port(SF_LAST));
      base_test_data_port_periodic_domains_producer_thread_i_producer_producer_seL4Nix_write_port_Send(SF (art_DataContent) ((art_DataContent) &t_2));
    }

    sfUpdateLoc(65);
    {
      base_producer_thread_i_producer_producer_producer_write_port_port_a(SF (Option_8E9F45) base_producer_thread_i_producer_producer_producer_noData(SF_LAST));
    }
  }
}

Unit base_producer_thread_i_producer_producer_producer_receiveInput(STACK_FRAME IS_D10119 eventPortIds, IS_D10119 dataPortIds) {
  DeclNewStackFrame(caller, "producer.scala", "base.producer_thread_i_producer_producer.producer", "receiveInput", 0);
}

void base_producer_thread_i_producer_producer_producer_init_producerBridge(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(18);
  {

    sfUpdateLoc(19);
    art_Port_A2C07C write_port;
    DeclNewart_Port_A2C07C(t_0);
    art_Port_A2C07C_apply(SF &t_0, art_Art_PortId_C(0), (String) string("top_impl_Instance_producer_producer_write_port"), art_PortMode_Type_DataOut);
    write_port = (art_Port_A2C07C) (&t_0);
    DeclNewart_DispatchPropertyProtocol_Periodic(t_2);
    art_DispatchPropertyProtocol_Periodic_apply(SF &t_2, Z_C(1000));
    DeclNewNone_CFD2B0(t_3);
    None_CFD2B0_apply(SF &t_3);
    DeclNewbase_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge(t_1);
    base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_apply(SF &t_1, art_Art_BridgeId_C(0), (String) string("top_impl_Instance_producer_producer"), (art_DispatchPropertyProtocol) (&t_2), (Option_0247A1) (&t_3), (art_Port_A2C07C) write_port);
    Type_assign(&_base_producer_thread_i_producer_producer_producer_producerBridge, (&t_1), sizeof(struct base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge));
  }
};

void base_producer_thread_i_producer_producer_producer_init_entryPoints(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(31);
  Type_assign(&_base_producer_thread_i_producer_producer_producer_entryPoints, base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_entryPoints_(base_producer_thread_i_producer_producer_producer_producerBridge(SF_LAST)), sizeof(union art_Bridge_EntryPoints));
};

void base_producer_thread_i_producer_producer_producer_init_noData(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(32);
  DeclNewNone_964667(t_4);
  None_964667_apply(SF &t_4);
  Type_assign(&_base_producer_thread_i_producer_producer_producer_noData, (&t_4), sizeof(struct None_964667));
};

void base_producer_thread_i_producer_producer_producer_init_write_port_id(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(35);
  _base_producer_thread_i_producer_producer_producer_write_port_id = art_Port_A2C07C_id_(base_test_data_port_periodic_domains_producer_thread_i_producer_producer_Bridge_write_port_(base_producer_thread_i_producer_producer_producer_producerBridge(SF_LAST)));
};

void base_producer_thread_i_producer_producer_producer_init_write_port_port(STACK_FRAME_ONLY) {
  #ifdef SIREUM_LOC
  StackFrame sf = caller;
  #endif
  sfUpdateLoc(36);
  Type_assign(&_base_producer_thread_i_producer_producer_producer_write_port_port, base_producer_thread_i_producer_producer_producer_noData(SF_LAST), sizeof(union Option_8E9F45));
};