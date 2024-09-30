#ifndef SIREUM_TYPE_H_base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge
#define SIREUM_TYPE_H_base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// base.test_data_port_periodic_domains.consumer_thread_i_consumer_consumer_Bridge
#include <type-art_Art_BridgeId.h>
#include <type-art_DispatchPropertyProtocol.h>
#include <type-org_sireum_Option_0247A1.h>
#include <type-art_art_Port_A2C07C.h>
#include <type-art_Bridge_Ports.h>
#include <type-base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api.h>
#include <type-base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api.h>
#include <type-art_Bridge_EntryPoints.h>

typedef struct base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge *base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge;
struct base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge {
  TYPE type;
  union art_DispatchPropertyProtocol dispatchProtocol;
  union Option_0247A1 dispatchTriggers;
  union art_Bridge_EntryPoints entryPoints;
  struct StaticString name;
  struct art_Port_A2C07C read_port;
  struct art_Bridge_Ports ports;
  struct base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api initialization_api;
  struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api operational_api;
  art_Art_BridgeId id;
};

#define DeclNewbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge(x) struct base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge x = { .type = Tbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge }

#ifdef __cplusplus
}
#endif

#endif