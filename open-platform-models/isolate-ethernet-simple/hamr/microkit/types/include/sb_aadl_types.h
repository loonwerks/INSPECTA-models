#pragma once

#include <stdbool.h>
#include <stdint.h>

// Do not edit this file as it will be overwritten if codegen is rerun

typedef
  enum {IPV4, IPV6} SW_InternetProtocol;

typedef
  enum {TCP, ARP} SW_FrameProtocol;

typedef
  enum {REQUEST, REPLY, NA} SW_ARP_Type;

#define SW_RawEthernetMessage_BYTE_SIZE 1600
#define SW_RawEthernetMessage_DIM_0 1600

typedef uint8_t SW_RawEthernetMessage [SW_RawEthernetMessage_DIM_0];

typedef struct SW_StructuredEthernetMessage_i {
  bool malformedFrame;
  SW_InternetProtocol internetProtocol;
  SW_FrameProtocol frameProtocol;
  bool portIsWhitelisted;
  SW_ARP_Type arpType;
  SW_RawEthernetMessage rawMessage;
} SW_StructuredEthernetMessage_i;
