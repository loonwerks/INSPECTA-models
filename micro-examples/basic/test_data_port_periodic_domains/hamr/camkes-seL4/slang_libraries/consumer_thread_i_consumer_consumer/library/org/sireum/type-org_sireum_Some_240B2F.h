#ifndef SIREUM_TYPE_H_org_sireum_Some_240B2F
#define SIREUM_TYPE_H_org_sireum_Some_240B2F

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// Some[S8]
#include <type-org_sireum_S8.h>

typedef struct Some_240B2F *Some_240B2F;
struct Some_240B2F {
  TYPE type;
  S8 value;
};

#define DeclNewSome_240B2F(x) struct Some_240B2F x = { .type = TSome_240B2F }

#ifdef __cplusplus
}
#endif

#endif