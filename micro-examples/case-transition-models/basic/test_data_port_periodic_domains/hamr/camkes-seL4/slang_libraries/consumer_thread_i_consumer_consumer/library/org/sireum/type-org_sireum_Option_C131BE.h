#ifndef SIREUM_TYPE_H_org_sireum_Option_C131BE
#define SIREUM_TYPE_H_org_sireum_Option_C131BE

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]

#include <type-org_sireum_None_F97B7F.h>
#include <type-org_sireum_Some_13E02D.h>

typedef union Option_C131BE *Option_C131BE;
union Option_C131BE {
  TYPE type;
  struct None_F97B7F None_F97B7F;
  struct Some_13E02D Some_13E02D;
};

#define DeclNewOption_C131BE(x) union Option_C131BE x = { 0 }

#ifdef __cplusplus
}
#endif

#endif