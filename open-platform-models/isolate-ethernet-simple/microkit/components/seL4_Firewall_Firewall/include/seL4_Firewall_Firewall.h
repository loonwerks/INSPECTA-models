#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool get_EthernetFramesTxIn(base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRxOut(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesTxOut(const base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesRxIn(base_SW_RawEthernetMessage_Impl *data);
