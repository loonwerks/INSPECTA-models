#ifndef SIREUM_TYPE_H_org_sireum_Option_FC3847
#define SIREUM_TYPE_H_org_sireum_Option_FC3847

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Option[S8]

#include <type-org_sireum_None_E19B61.h>
#include <type-org_sireum_Some_240B2F.h>

typedef union Option_FC3847 *Option_FC3847;
union Option_FC3847 {
  TYPE type;
  struct None_E19B61 None_E19B61;
  struct Some_240B2F Some_240B2F;
};

#define DeclNewOption_FC3847(x) union Option_FC3847 x = { 0 }

#ifdef __cplusplus
}
#endif

#endif