#ifndef SIREUM_TYPE_H_org_sireum_Some_AA275E
#define SIREUM_TYPE_H_org_sireum_Some_AA275E

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Some[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]
#include <type-base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api.h>

typedef struct Some_AA275E *Some_AA275E;
struct Some_AA275E {
  TYPE type;
  struct base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api value;
};

#define DeclNewSome_AA275E(x) struct Some_AA275E x = { .type = TSome_AA275E }

#ifdef __cplusplus
}
#endif

#endif