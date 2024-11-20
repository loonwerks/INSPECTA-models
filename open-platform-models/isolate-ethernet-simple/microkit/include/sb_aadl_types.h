#ifndef SB_AADL_TYPES_H
#define SB_AADL_TYPES_H

#include <stdint.h>

typedef struct base_SW_SizedEthernetMessage_Impl base_SW_SizedEthernetMessage_Impl;

#define base_SW_RawEthernetMessage_Impl_SIZE 1600

typedef uint8_t base_SW_RawEthernetMessage_Impl [base_SW_RawEthernetMessage_Impl_SIZE];

typedef
  struct base_SW_RawEthernetMessage_Impl_container{
    base_SW_RawEthernetMessage_Impl f;
  } base_SW_RawEthernetMessage_Impl_container;

#define base_SW_RawEthernetMessage_SIZE 1600

typedef uint8_t base_SW_RawEthernetMessage [base_SW_RawEthernetMessage_SIZE];

typedef
  struct base_SW_RawEthernetMessage_container{
    base_SW_RawEthernetMessage f;
  } base_SW_RawEthernetMessage_container;

struct base_SW_SizedEthernetMessage_Impl {
   message;
   size;
};

#endif
