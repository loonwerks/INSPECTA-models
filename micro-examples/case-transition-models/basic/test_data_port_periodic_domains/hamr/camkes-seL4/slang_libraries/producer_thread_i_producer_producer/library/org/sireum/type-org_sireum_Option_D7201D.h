#ifndef SIREUM_TYPE_H_org_sireum_Option_D7201D
#define SIREUM_TYPE_H_org_sireum_Option_D7201D

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]

#include <type-org_sireum_None_F1003F.h>
#include <type-org_sireum_Some_E023D9.h>

typedef union Option_D7201D *Option_D7201D;
union Option_D7201D {
  TYPE type;
  struct None_F1003F None_F1003F;
  struct Some_E023D9 Some_E023D9;
};

#define DeclNewOption_D7201D(x) union Option_D7201D x = { 0 }

#ifdef __cplusplus
}
#endif

#endif