#ifndef SIREUM_TYPE_H_org_sireum_Option_1F3783
#define SIREUM_TYPE_H_org_sireum_Option_1F3783

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]

#include <type-org_sireum_None_608F86.h>
#include <type-org_sireum_Some_FDB73B.h>

typedef union Option_1F3783 *Option_1F3783;
union Option_1F3783 {
  TYPE type;
  struct None_608F86 None_608F86;
  struct Some_FDB73B Some_FDB73B;
};

#define DeclNewOption_1F3783(x) union Option_1F3783 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif