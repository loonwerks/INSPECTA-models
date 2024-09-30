#ifndef SIREUM_TYPE_H_org_sireum_Option_FB1130
#define SIREUM_TYPE_H_org_sireum_Option_FB1130

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]

#include <type-org_sireum_None_5F2743.h>
#include <type-org_sireum_Some_2EDC17.h>

typedef union Option_FB1130 *Option_FB1130;
union Option_FB1130 {
  TYPE type;
  struct None_5F2743 None_5F2743;
  struct Some_2EDC17 Some_2EDC17;
};

#define DeclNewOption_FB1130(x) union Option_FB1130 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif