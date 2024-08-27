#ifndef SIREUM_TYPE_H_base_Base_Types_Bits_Payload
#define SIREUM_TYPE_H_base_Base_Types_Bits_Payload

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// base.Base_Types.Bits_Payload
#include <type-org_sireum_IS_C4F575.h>

typedef struct base_Base_Types_Bits_Payload *base_Base_Types_Bits_Payload;
struct base_Base_Types_Bits_Payload {
  TYPE type;
  struct IS_C4F575 value;
};

#define DeclNewbase_Base_Types_Bits_Payload(x) struct base_Base_Types_Bits_Payload x = { .type = Tbase_Base_Types_Bits_Payload }

#ifdef __cplusplus
}
#endif

#endif