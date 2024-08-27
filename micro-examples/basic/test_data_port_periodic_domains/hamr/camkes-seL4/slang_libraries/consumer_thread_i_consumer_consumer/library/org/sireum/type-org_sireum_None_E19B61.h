#ifndef SIREUM_TYPE_H_org_sireum_None_E19B61
#define SIREUM_TYPE_H_org_sireum_None_E19B61

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// None[S8]

typedef struct None_E19B61 *None_E19B61;
struct None_E19B61 {
  TYPE type;
};

#define DeclNewNone_E19B61(x) struct None_E19B61 x = { .type = TNone_E19B61 }

#ifdef __cplusplus
}
#endif

#endif