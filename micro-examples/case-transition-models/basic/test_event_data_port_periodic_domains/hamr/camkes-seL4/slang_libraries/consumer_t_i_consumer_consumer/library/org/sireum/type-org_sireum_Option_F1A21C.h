#ifndef SIREUM_TYPE_H_org_sireum_Option_F1A21C
#define SIREUM_TYPE_H_org_sireum_Option_F1A21C

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]

#include <type-org_sireum_None_539999.h>
#include <type-org_sireum_Some_2159FF.h>

typedef union Option_F1A21C *Option_F1A21C;
union Option_F1A21C {
  TYPE type;
  struct None_539999 None_539999;
  struct Some_2159FF Some_2159FF;
};

#define DeclNewOption_F1A21C(x) union Option_F1A21C x = { 0 }

#ifdef __cplusplus
}
#endif

#endif