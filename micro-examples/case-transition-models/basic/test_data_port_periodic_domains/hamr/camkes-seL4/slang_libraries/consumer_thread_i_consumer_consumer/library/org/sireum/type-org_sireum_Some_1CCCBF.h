#ifndef SIREUM_TYPE_H_org_sireum_Some_1CCCBF
#define SIREUM_TYPE_H_org_sireum_Some_1CCCBF

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Some[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]
#include <type-base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api.h>

typedef struct Some_1CCCBF *Some_1CCCBF;
struct Some_1CCCBF {
  TYPE type;
  struct base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api value;
};

#define DeclNewSome_1CCCBF(x) struct Some_1CCCBF x = { .type = TSome_1CCCBF }

#ifdef __cplusplus
}
#endif

#endif