#ifndef SIREUM_TYPE_H_org_sireum_Option_54EE48
#define SIREUM_TYPE_H_org_sireum_Option_54EE48

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]

#include <type-org_sireum_None_C54AB4.h>
#include <type-org_sireum_Some_9F6875.h>

typedef union Option_54EE48 *Option_54EE48;
union Option_54EE48 {
  TYPE type;
  struct None_C54AB4 None_C54AB4;
  struct Some_9F6875 Some_9F6875;
};

#define DeclNewOption_54EE48(x) union Option_54EE48 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif