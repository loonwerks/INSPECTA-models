#ifndef SIREUM_TYPE_H_org_sireum_Option_CFF091
#define SIREUM_TYPE_H_org_sireum_Option_CFF091

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]

#include <type-org_sireum_None_5A630D.h>
#include <type-org_sireum_Some_4CD075.h>

typedef union Option_CFF091 *Option_CFF091;
union Option_CFF091 {
  TYPE type;
  struct None_5A630D None_5A630D;
  struct Some_4CD075 Some_4CD075;
};

#define DeclNewOption_CFF091(x) union Option_CFF091 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif