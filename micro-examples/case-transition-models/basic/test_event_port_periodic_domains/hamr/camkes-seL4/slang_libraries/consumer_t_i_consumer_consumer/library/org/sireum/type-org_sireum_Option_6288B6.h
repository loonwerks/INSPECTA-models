#ifndef SIREUM_TYPE_H_org_sireum_Option_6288B6
#define SIREUM_TYPE_H_org_sireum_Option_6288B6

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]

#include <type-org_sireum_None_9DDC16.h>
#include <type-org_sireum_Some_0B69ED.h>

typedef union Option_6288B6 *Option_6288B6;
union Option_6288B6 {
  TYPE type;
  struct None_9DDC16 None_9DDC16;
  struct Some_0B69ED Some_0B69ED;
};

#define DeclNewOption_6288B6(x) union Option_6288B6 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif