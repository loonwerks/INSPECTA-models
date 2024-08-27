#ifndef SIREUM_TYPE_H_org_sireum_Some_2159FF
#define SIREUM_TYPE_H_org_sireum_Some_2159FF

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Some[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]
#include <type-base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api.h>

typedef struct Some_2159FF *Some_2159FF;
struct Some_2159FF {
  TYPE type;
  struct base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api value;
};

#define DeclNewSome_2159FF(x) struct Some_2159FF x = { .type = TSome_2159FF }

#ifdef __cplusplus
}
#endif

#endif