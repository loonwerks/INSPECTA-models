#include <types.h>

size_t sizeOf(Type t) {
  TYPE type = t->type;
  switch (type) {
    case TIS_948B60: return sizeof(struct IS_948B60); // IS[Z, String]
    case TIS_D10119: return sizeof(struct IS_D10119); // IS[Z, art.Art.PortId]
    case TIS_820232: return sizeof(struct IS_820232); // IS[Z, art.UPort]
    case TMBox2_37E193: return sizeof(struct MBox2_37E193); // MBox2[art.Art.PortId, Option[art.DataContent]]
    case TMBox2_43CC67: return sizeof(struct MBox2_43CC67); // MBox2[art.Art.PortId, art.DataContent]
    case TMS_83D5EB: return sizeof(struct MS_83D5EB); // MS[Z, Option[art.Bridge]]
    case TNone_CFD2B0: return sizeof(struct None_CFD2B0); // None[IS[Z, art.Art.PortId]]
    case TNone_E19B61: return sizeof(struct None_E19B61); // None[S8]
    case TNone_734370: return sizeof(struct None_734370); // None[art.Bridge]
    case TNone_964667: return sizeof(struct None_964667); // None[art.DataContent]
    case TNone_F97B7F: return sizeof(struct None_F97B7F); // None[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]
    case TNone_D3113C: return sizeof(struct None_D3113C); // None[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]
    case TSome_240B2F: return sizeof(struct Some_240B2F); // Some[S8]
    case TSome_D29615: return sizeof(struct Some_D29615); // Some[art.DataContent]
    case TSome_13E02D: return sizeof(struct Some_13E02D); // Some[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]
    case TSome_1CCCBF: return sizeof(struct Some_1CCCBF); // Some[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]
    case TString: return sizeof(struct String); // String
    case Tart_Bridge_Ports: return sizeof(struct art_Bridge_Ports); // art.Bridge.Ports
    case Tart_DispatchPropertyProtocol_Periodic: return sizeof(struct art_DispatchPropertyProtocol_Periodic); // art.DispatchPropertyProtocol.Periodic
    case Tart_Empty: return sizeof(struct art_Empty); // art.Empty
    case Tart_Port_A2C07C: return sizeof(struct art_Port_A2C07C); // art.Port[S8]
    case Tbase_Base_Types_Boolean_Payload: return sizeof(struct base_Base_Types_Boolean_Payload); // base.Base_Types.Boolean_Payload
    case Tbase_Base_Types_Integer_8_Payload: return sizeof(struct base_Base_Types_Integer_8_Payload); // base.Base_Types.Integer_8_Payload
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api: return sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api); // base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_Operational_Api: return sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api); // base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge: return sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge); // base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints: return sizeof(struct base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints); // base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints
    default: fprintf(stderr, "%s: %d\n", "Unexpected TYPE: ", type); exit(1);
  }
}

void Type_assign(void *dest, void *src, size_t destSize) {
  Type srcType = (Type) src;
  if (srcType->type == TString) {
    String_assign((String) dest, (String) src);
    return;
  }
  size_t srcSize = sizeOf(srcType);
  memcpy(dest, src, srcSize);
  memset(((char *) dest) + srcSize, 0, destSize - srcSize);
}

char *TYPE_string_(void *type) {
  static char *strings[] = {
    "IS[Z, String]",
    "IS[Z, art.Art.PortId]",
    "IS[Z, art.UPort]",
    "MBox2[art.Art.PortId, Option[art.DataContent]]",
    "MBox2[art.Art.PortId, art.DataContent]",
    "MS[Z, Option[art.Bridge]]",
    "None[IS[Z, art.Art.PortId]]",
    "None[S8]",
    "None[art.Bridge]",
    "None[art.DataContent]",
    "None[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]",
    "None[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]",
    "Some[S8]",
    "Some[art.DataContent]",
    "Some[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]",
    "Some[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]",
    "String",
    "art.Bridge.Ports",
    "art.DispatchPropertyProtocol.Periodic",
    "art.Empty",
    "art.Port[S8]",
    "base.Base_Types.Boolean_Payload",
    "base.Base_Types.Integer_8_Payload",
    "base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api",
    "base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api",
    "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge",
    "base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge.EntryPoints"
  };
  return strings[((Type) type)->type];
}