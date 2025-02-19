#pragma once

#include <printf.h>
#include <util.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

// Do not edit this file as it will be overwritten if codegen is rerun


bool EthernetFramesTx_is_empty(void);
bool get_EthernetFramesTx_poll(sb_event_counter_t *numDropped, base_SW_StructuredEthernetMessage_i *data);
bool get_EthernetFramesTx(base_SW_StructuredEthernetMessage_i *data);
bool put_EthernetFramesRx(const base_SW_StructuredEthernetMessage_i *data);
