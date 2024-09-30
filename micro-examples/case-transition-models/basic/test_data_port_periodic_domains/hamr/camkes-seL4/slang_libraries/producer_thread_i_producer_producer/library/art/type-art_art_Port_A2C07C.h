#ifndef SIREUM_TYPE_H_art_art_Port_A2C07C
#define SIREUM_TYPE_H_art_art_Port_A2C07C

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// art.Port[S8]
#include <type-art_Art_PortId.h>
#include <type-art_PortMode_Type.h>

typedef struct art_Port_A2C07C *art_Port_A2C07C;
struct art_Port_A2C07C {
  TYPE type;
  struct StaticString name;
  art_Art_PortId id;
  art_PortMode_Type mode;
};

#define DeclNewart_Port_A2C07C(x) struct art_Port_A2C07C x = { .type = Tart_Port_A2C07C }

#ifdef __cplusplus
}
#endif

#endif