#ifndef SIREUM_GEN_TYPE_H
#define SIREUM_GEN_TYPE_H

#ifdef __cplusplus
extern "C" {
#endif
#include <stackframe.h>

typedef enum {
  TIS_948B60 = (int) 0x948B6070, // IS[Z, String]
  TIS_D10119 = (int) 0xD101198A, // IS[Z, art.Art.PortId]
  TIS_820232 = (int) 0x82023297, // IS[Z, art.UPort]
  TMBox2_37E193 = (int) 0x37E19307, // MBox2[art.Art.PortId, Option[art.DataContent]]
  TMBox2_43CC67 = (int) 0x43CC672A, // MBox2[art.Art.PortId, art.DataContent]
  TMS_83D5EB = (int) 0x83D5EBFF, // MS[Z, Option[art.Bridge]]
  TNone_CFD2B0 = (int) 0xCFD2B0C7, // None[IS[Z, art.Art.PortId]]
  TNone_734370 = (int) 0x73437068, // None[art.Bridge]
  TNone_964667 = (int) 0x9646678F, // None[art.DataContent]
  TNone_5A630D = (int) 0x5A630DF8, // None[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]
  TNone_04C60E = (int) 0x04C60E8D, // None[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]
  TSome_D29615 = (int) 0xD29615C0, // Some[art.DataContent]
  TSome_4CD075 = (int) 0x4CD0759C, // Some[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]
  TSome_A8A5E0 = (int) 0xA8A5E066, // Some[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]
  TString = (int) 0xB6F8E8F6, // String
  Tart_Bridge_Ports = (int) 0x14139493, // art.Bridge.Ports
  Tart_DispatchPropertyProtocol_Periodic = (int) 0x4C652984, // art.DispatchPropertyProtocol.Periodic
  Tart_Empty = (int) 0x49C5E24D, // art.Empty
  Tart_Port_9CBF18 = (int) 0x9CBF18B7, // art.Port[art.Empty]
  Tbase_Base_Types_Boolean_Payload = (int) 0x7DD9B8C5, // base.Base_Types.Boolean_Payload
  Tbase_test_event_port_periodic_domains_producer_t_i_Initialization_Api = (int) 0x350EBFCE, // base.test_event_port_periodic_domains.producer_t_i_Initialization_Api
  Tbase_test_event_port_periodic_domains_producer_t_i_Operational_Api = (int) 0xD4B34729, // base.test_event_port_periodic_domains.producer_t_i_Operational_Api
  Tbase_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge = (int) 0x87378265, // base.test_event_port_periodic_domains.producer_t_i_producer_producer_Bridge
  Tbase_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints = (int) 0x2D0F4E62, // base.test_event_port_periodic_domains.producer_t_i_producer_producer_Bridge.EntryPoints
} TYPE;

char *TYPE_string_(void *type);

typedef struct Type *Type;
struct Type {
  TYPE type;
};

#define MaxString 256

typedef struct String *String;
struct String {
  TYPE type;
  Z size;
  C value[];
};

struct StaticString {
  TYPE type;
  Z size;
  C value[MaxString + 1];
};

#define string(v) ((String) &((struct { TYPE type; Z size; C value[sizeof(v)]; }) { TString, Z_C(sizeof (v) - 1), v }))
#define DeclNewString(x) struct StaticString x = { .type = TString }

#ifdef __cplusplus
}
#endif

#endif