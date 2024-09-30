#ifndef SIREUM_TYPE_H_org_sireum_Option_B8220B
#define SIREUM_TYPE_H_org_sireum_Option_B8220B

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]

#include <type-org_sireum_None_B7029D.h>
#include <type-org_sireum_Some_AA275E.h>

typedef union Option_B8220B *Option_B8220B;
union Option_B8220B {
  TYPE type;
  struct None_B7029D None_B7029D;
  struct Some_AA275E Some_AA275E;
};

#define DeclNewOption_B8220B(x) union Option_B8220B x = { 0 }

#ifdef __cplusplus
}
#endif

#endif