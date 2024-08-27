#ifndef SIREUM_TYPE_H_org_sireum_Some_2EDC17
#define SIREUM_TYPE_H_org_sireum_Some_2EDC17

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Some[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]
#include <type-base_test_event_port_periodic_domains_consumer_t_i_Operational_Api.h>

typedef struct Some_2EDC17 *Some_2EDC17;
struct Some_2EDC17 {
  TYPE type;
  struct base_test_event_port_periodic_domains_consumer_t_i_Operational_Api value;
};

#define DeclNewSome_2EDC17(x) struct Some_2EDC17 x = { .type = TSome_2EDC17 }

#ifdef __cplusplus
}
#endif

#endif