#ifndef SIREUM_TYPE_H_base_Base_Types_Integer_8_Payload
#define SIREUM_TYPE_H_base_Base_Types_Integer_8_Payload

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// base.Base_Types.Integer_8_Payload
#include <type-org_sireum_S8.h>

typedef struct base_Base_Types_Integer_8_Payload *base_Base_Types_Integer_8_Payload;
struct base_Base_Types_Integer_8_Payload {
  TYPE type;
  S8 value;
};

#define DeclNewbase_Base_Types_Integer_8_Payload(x) struct base_Base_Types_Integer_8_Payload x = { .type = Tbase_Base_Types_Integer_8_Payload }

#ifdef __cplusplus
}
#endif

#endif