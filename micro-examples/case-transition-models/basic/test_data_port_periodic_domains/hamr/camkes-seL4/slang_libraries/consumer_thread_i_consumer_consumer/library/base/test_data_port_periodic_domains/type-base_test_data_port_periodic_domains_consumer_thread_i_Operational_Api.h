#ifndef SIREUM_TYPE_H_base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api
#define SIREUM_TYPE_H_base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api
#include <type-art_Art_BridgeId.h>
#include <type-art_Art_PortId.h>

typedef struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api *base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api;
struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api {
  TYPE type;
  art_Art_BridgeId id;
  art_Art_PortId read_port_Id;
};

#define DeclNewbase_test_data_port_periodic_domains_consumer_thread_i_Operational_Api(x) struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api x = { .type = Tbase_test_data_port_periodic_domains_consumer_thread_i_Operational_Api }

#ifdef __cplusplus
}
#endif

#endif