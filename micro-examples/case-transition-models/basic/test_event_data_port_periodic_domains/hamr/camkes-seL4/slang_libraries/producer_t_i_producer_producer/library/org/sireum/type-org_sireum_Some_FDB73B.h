#ifndef SIREUM_TYPE_H_org_sireum_Some_FDB73B
#define SIREUM_TYPE_H_org_sireum_Some_FDB73B

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Some[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]
#include <type-base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api.h>

typedef struct Some_FDB73B *Some_FDB73B;
struct Some_FDB73B {
  TYPE type;
  struct base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api value;
};

#define DeclNewSome_FDB73B(x) struct Some_FDB73B x = { .type = TSome_FDB73B }

#ifdef __cplusplus
}
#endif

#endif