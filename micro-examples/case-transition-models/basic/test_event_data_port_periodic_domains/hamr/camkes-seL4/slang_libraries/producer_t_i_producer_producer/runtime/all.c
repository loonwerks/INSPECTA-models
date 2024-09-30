#include <all.h>

B Type__eq(void *t1, void *t2) {
  TYPE type = ((Type) t1)->type;
  if (type != ((Type) t2)->type) return F;
  switch (type) {
    case TIS_948B60: return IS_948B60__eq((IS_948B60) t1, (IS_948B60) t2);
    case TIS_D10119: return IS_D10119__eq((IS_D10119) t1, (IS_D10119) t2);
    case TIS_820232: return IS_820232__eq((IS_820232) t1, (IS_820232) t2);
    case TMBox2_37E193: return MBox2_37E193__eq((MBox2_37E193) t1, (MBox2_37E193) t2);
    case TMBox2_43CC67: return MBox2_43CC67__eq((MBox2_43CC67) t1, (MBox2_43CC67) t2);
    case TMS_83D5EB: return MS_83D5EB__eq((MS_83D5EB) t1, (MS_83D5EB) t2);
    case TNone_CFD2B0: return None_CFD2B0__eq((None_CFD2B0) t1, (None_CFD2B0) t2);
    case TNone_734370: return None_734370__eq((None_734370) t1, (None_734370) t2);
    case TNone_964667: return None_964667__eq((None_964667) t1, (None_964667) t2);
    case TNone_608F86: return None_608F86__eq((None_608F86) t1, (None_608F86) t2);
    case TNone_C54AB4: return None_C54AB4__eq((None_C54AB4) t1, (None_C54AB4) t2);
    case TSome_D29615: return Some_D29615__eq((Some_D29615) t1, (Some_D29615) t2);
    case TSome_FDB73B: return Some_FDB73B__eq((Some_FDB73B) t1, (Some_FDB73B) t2);
    case TSome_9F6875: return Some_9F6875__eq((Some_9F6875) t1, (Some_9F6875) t2);
    case TString: return String__eq((String) t1, (String) t2);
    case Tart_Bridge_Ports: return art_Bridge_Ports__eq((art_Bridge_Ports) t1, (art_Bridge_Ports) t2);
    case Tart_DispatchPropertyProtocol_Periodic: return art_DispatchPropertyProtocol_Periodic__eq((art_DispatchPropertyProtocol_Periodic) t1, (art_DispatchPropertyProtocol_Periodic) t2);
    case Tart_Empty: return art_Empty__eq((art_Empty) t1, (art_Empty) t2);
    case Tart_Port_A2C07C: return art_Port_A2C07C__eq((art_Port_A2C07C) t1, (art_Port_A2C07C) t2);
    case Tbase_Base_Types_Boolean_Payload: return base_Base_Types_Boolean_Payload__eq((base_Base_Types_Boolean_Payload) t1, (base_Base_Types_Boolean_Payload) t2);
    case Tbase_Base_Types_Integer_8_Payload: return base_Base_Types_Integer_8_Payload__eq((base_Base_Types_Integer_8_Payload) t1, (base_Base_Types_Integer_8_Payload) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api: return base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api__eq((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) t1, (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Operational_Api: return base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api__eq((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) t1, (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge: return base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge__eq((base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge) t1, (base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints: return base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints__eq((base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints) t1, (base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints) t2);
    default: fprintf(stderr, "%s: %d\n", "Unexpected TYPE: ", type); exit(1);
  }
}

B Type__equiv(void *t1, void *t2) {
  TYPE type = ((Type) t1)->type;
  if (type != ((Type) t2)->type) return F;
  switch (type) {
    case TIS_948B60: return IS_948B60__equiv((IS_948B60) t1, (IS_948B60) t2);
    case TIS_D10119: return IS_D10119__equiv((IS_D10119) t1, (IS_D10119) t2);
    case TIS_820232: return IS_820232__equiv((IS_820232) t1, (IS_820232) t2);
    case TMBox2_37E193: return MBox2_37E193__equiv((MBox2_37E193) t1, (MBox2_37E193) t2);
    case TMBox2_43CC67: return MBox2_43CC67__equiv((MBox2_43CC67) t1, (MBox2_43CC67) t2);
    case TMS_83D5EB: return MS_83D5EB__equiv((MS_83D5EB) t1, (MS_83D5EB) t2);
    case TNone_CFD2B0: return None_CFD2B0__equiv((None_CFD2B0) t1, (None_CFD2B0) t2);
    case TNone_734370: return None_734370__equiv((None_734370) t1, (None_734370) t2);
    case TNone_964667: return None_964667__equiv((None_964667) t1, (None_964667) t2);
    case TNone_608F86: return None_608F86__equiv((None_608F86) t1, (None_608F86) t2);
    case TNone_C54AB4: return None_C54AB4__equiv((None_C54AB4) t1, (None_C54AB4) t2);
    case TSome_D29615: return Some_D29615__equiv((Some_D29615) t1, (Some_D29615) t2);
    case TSome_FDB73B: return Some_FDB73B__equiv((Some_FDB73B) t1, (Some_FDB73B) t2);
    case TSome_9F6875: return Some_9F6875__equiv((Some_9F6875) t1, (Some_9F6875) t2);
    case TString: return String__equiv((String) t1, (String) t2);
    case Tart_Bridge_Ports: return art_Bridge_Ports__equiv((art_Bridge_Ports) t1, (art_Bridge_Ports) t2);
    case Tart_DispatchPropertyProtocol_Periodic: return art_DispatchPropertyProtocol_Periodic__equiv((art_DispatchPropertyProtocol_Periodic) t1, (art_DispatchPropertyProtocol_Periodic) t2);
    case Tart_Empty: return art_Empty__equiv((art_Empty) t1, (art_Empty) t2);
    case Tart_Port_A2C07C: return art_Port_A2C07C__equiv((art_Port_A2C07C) t1, (art_Port_A2C07C) t2);
    case Tbase_Base_Types_Boolean_Payload: return base_Base_Types_Boolean_Payload__equiv((base_Base_Types_Boolean_Payload) t1, (base_Base_Types_Boolean_Payload) t2);
    case Tbase_Base_Types_Integer_8_Payload: return base_Base_Types_Integer_8_Payload__equiv((base_Base_Types_Integer_8_Payload) t1, (base_Base_Types_Integer_8_Payload) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api: return base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api__equiv((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) t1, (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Operational_Api: return base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api__equiv((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) t1, (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge: return base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge__equiv((base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge) t1, (base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge) t2);
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints: return base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints__equiv((base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints) t1, (base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints) t2);
    default: fprintf(stderr, "%s: %d\n", "Unexpected TYPE: ", type); exit(1);
  }
}

void Type_cprint(void *this, B isOut) {
  #ifndef SIREUM_NO_PRINT
  TYPE type = ((Type) this)->type;
  switch (type) {
    case TIS_948B60: IS_948B60_cprint((IS_948B60) this, isOut); return;
    case TIS_D10119: IS_D10119_cprint((IS_D10119) this, isOut); return;
    case TIS_820232: IS_820232_cprint((IS_820232) this, isOut); return;
    case TMBox2_37E193: MBox2_37E193_cprint((MBox2_37E193) this, isOut); return;
    case TMBox2_43CC67: MBox2_43CC67_cprint((MBox2_43CC67) this, isOut); return;
    case TMS_83D5EB: MS_83D5EB_cprint((MS_83D5EB) this, isOut); return;
    case TNone_CFD2B0: None_CFD2B0_cprint((None_CFD2B0) this, isOut); return;
    case TNone_734370: None_734370_cprint((None_734370) this, isOut); return;
    case TNone_964667: None_964667_cprint((None_964667) this, isOut); return;
    case TNone_608F86: None_608F86_cprint((None_608F86) this, isOut); return;
    case TNone_C54AB4: None_C54AB4_cprint((None_C54AB4) this, isOut); return;
    case TSome_D29615: Some_D29615_cprint((Some_D29615) this, isOut); return;
    case TSome_FDB73B: Some_FDB73B_cprint((Some_FDB73B) this, isOut); return;
    case TSome_9F6875: Some_9F6875_cprint((Some_9F6875) this, isOut); return;
    case TString: String_cprint((String) this, isOut); return;
    case Tart_Bridge_Ports: art_Bridge_Ports_cprint((art_Bridge_Ports) this, isOut); return;
    case Tart_DispatchPropertyProtocol_Periodic: art_DispatchPropertyProtocol_Periodic_cprint((art_DispatchPropertyProtocol_Periodic) this, isOut); return;
    case Tart_Empty: art_Empty_cprint((art_Empty) this, isOut); return;
    case Tart_Port_A2C07C: art_Port_A2C07C_cprint((art_Port_A2C07C) this, isOut); return;
    case Tbase_Base_Types_Boolean_Payload: base_Base_Types_Boolean_Payload_cprint((base_Base_Types_Boolean_Payload) this, isOut); return;
    case Tbase_Base_Types_Integer_8_Payload: base_Base_Types_Integer_8_Payload_cprint((base_Base_Types_Integer_8_Payload) this, isOut); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api: base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api_cprint((base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) this, isOut); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Operational_Api: base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api_cprint((base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) this, isOut); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge: base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_cprint((base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge) this, isOut); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints: base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints_cprint((base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints) this, isOut); return;
    default: fprintf(stderr, "%s: %d\n", "Unexpected TYPE: ", type); exit(1);
  }
  #endif
}

void Type_string_(STACK_FRAME String result, void *this) {
  TYPE type = ((Type) this)->type;
  switch (type) {
    case TIS_948B60: IS_948B60_string_(CALLER result, (IS_948B60) this); return;
    case TIS_D10119: IS_D10119_string_(CALLER result, (IS_D10119) this); return;
    case TIS_820232: IS_820232_string_(CALLER result, (IS_820232) this); return;
    case TMBox2_37E193: MBox2_37E193_string_(CALLER result, (MBox2_37E193) this); return;
    case TMBox2_43CC67: MBox2_43CC67_string_(CALLER result, (MBox2_43CC67) this); return;
    case TMS_83D5EB: MS_83D5EB_string_(CALLER result, (MS_83D5EB) this); return;
    case TNone_CFD2B0: None_CFD2B0_string_(CALLER result, (None_CFD2B0) this); return;
    case TNone_734370: None_734370_string_(CALLER result, (None_734370) this); return;
    case TNone_964667: None_964667_string_(CALLER result, (None_964667) this); return;
    case TNone_608F86: None_608F86_string_(CALLER result, (None_608F86) this); return;
    case TNone_C54AB4: None_C54AB4_string_(CALLER result, (None_C54AB4) this); return;
    case TSome_D29615: Some_D29615_string_(CALLER result, (Some_D29615) this); return;
    case TSome_FDB73B: Some_FDB73B_string_(CALLER result, (Some_FDB73B) this); return;
    case TSome_9F6875: Some_9F6875_string_(CALLER result, (Some_9F6875) this); return;
    case TString: String_string_(CALLER result, (String) this); return;
    case Tart_Bridge_Ports: art_Bridge_Ports_string_(CALLER result, (art_Bridge_Ports) this); return;
    case Tart_DispatchPropertyProtocol_Periodic: art_DispatchPropertyProtocol_Periodic_string_(CALLER result, (art_DispatchPropertyProtocol_Periodic) this); return;
    case Tart_Empty: art_Empty_string_(CALLER result, (art_Empty) this); return;
    case Tart_Port_A2C07C: art_Port_A2C07C_string_(CALLER result, (art_Port_A2C07C) this); return;
    case Tbase_Base_Types_Boolean_Payload: base_Base_Types_Boolean_Payload_string_(CALLER result, (base_Base_Types_Boolean_Payload) this); return;
    case Tbase_Base_Types_Integer_8_Payload: base_Base_Types_Integer_8_Payload_string_(CALLER result, (base_Base_Types_Integer_8_Payload) this); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api: base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api_string_(CALLER result, (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) this); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_Operational_Api: base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api_string_(CALLER result, (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) this); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge: base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_string_(CALLER result, (base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge) this); return;
    case Tbase_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints: base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints_string_(CALLER result, (base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints) this); return;
    default: fprintf(stderr, "%s: %d\n", "Unexpected TYPE: ", type); exit(1);
  }
}

