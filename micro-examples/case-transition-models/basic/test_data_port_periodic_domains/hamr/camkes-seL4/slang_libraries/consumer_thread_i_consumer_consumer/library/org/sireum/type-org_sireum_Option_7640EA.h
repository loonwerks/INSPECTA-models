#ifndef SIREUM_TYPE_H_org_sireum_Option_7640EA
#define SIREUM_TYPE_H_org_sireum_Option_7640EA

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]

#include <type-org_sireum_None_D3113C.h>
#include <type-org_sireum_Some_1CCCBF.h>

typedef union Option_7640EA *Option_7640EA;
union Option_7640EA {
  TYPE type;
  struct None_D3113C None_D3113C;
  struct Some_1CCCBF Some_1CCCBF;
};

#define DeclNewOption_7640EA(x) union Option_7640EA x = { 0 }

#ifdef __cplusplus
}
#endif

#endif