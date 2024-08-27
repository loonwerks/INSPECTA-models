#ifndef SIREUM_TYPE_H_org_sireum_Option_0CB775
#define SIREUM_TYPE_H_org_sireum_Option_0CB775

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]

#include <type-org_sireum_None_F1EF25.h>
#include <type-org_sireum_Some_6B73B7.h>

typedef union Option_0CB775 *Option_0CB775;
union Option_0CB775 {
  TYPE type;
  struct None_F1EF25 None_F1EF25;
  struct Some_6B73B7 Some_6B73B7;
};

#define DeclNewOption_0CB775(x) union Option_0CB775 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif