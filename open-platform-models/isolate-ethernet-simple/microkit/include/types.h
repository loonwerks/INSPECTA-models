#ifndef TYPES_H
#define TYPES_H

#include <stdint.h>


#define base_SW_RawEthernetMessage_Impl_SIZE 1600

typedef uint8_t base_SW_RawEthernetMessage_Impl [base_SW_RawEthernetMessage_Impl_SIZE];

typedef
  struct base_SW_RawEthernetMessage_Impl_container{
    base_SW_RawEthernetMessage_Impl f;
  } base_SW_RawEthernetMessage_Impl_container;

#endif
