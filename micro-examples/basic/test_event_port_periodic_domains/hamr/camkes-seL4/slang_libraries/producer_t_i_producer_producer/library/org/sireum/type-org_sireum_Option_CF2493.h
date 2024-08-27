#ifndef SIREUM_TYPE_H_org_sireum_Option_CF2493
#define SIREUM_TYPE_H_org_sireum_Option_CF2493

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]

#include <type-org_sireum_None_04C60E.h>
#include <type-org_sireum_Some_A8A5E0.h>

typedef union Option_CF2493 *Option_CF2493;
union Option_CF2493 {
  TYPE type;
  struct None_04C60E None_04C60E;
  struct Some_A8A5E0 Some_A8A5E0;
};

#define DeclNewOption_CF2493(x) union Option_CF2493 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif