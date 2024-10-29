#ifndef TYPES_H
#define TYPES_H

#include <stdint.h>

typedef struct slang_SW_SizedEthernetMessage_Impl slang_SW_SizedEthernetMessage_Impl;

#define slang_SW_RawEthernetMessage_Impl_SIZE 1600

typedef uint8_t slang_SW_RawEthernetMessage_Impl [slang_SW_RawEthernetMessage_Impl_SIZE];

typedef
  struct slang_SW_RawEthernetMessage_Impl_container{
    slang_SW_RawEthernetMessage_Impl f;
  } slang_SW_RawEthernetMessage_Impl_container;

#define slang_SW_RawEthernetMessage_SIZE 1600

typedef uint8_t slang_SW_RawEthernetMessage [slang_SW_RawEthernetMessage_SIZE];

typedef
  struct slang_SW_RawEthernetMessage_container{
    slang_SW_RawEthernetMessage f;
  } slang_SW_RawEthernetMessage_container;

struct slang_SW_SizedEthernetMessage_Impl {
  slang_SW_RawEthernetMessage message;
  uint16_t size;
};

#endif
