#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool get_EthernetFramesTx0(base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx1(base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx2(base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx3(base_SW_SizedEthernetMessage_Impl *data);
bool put_EthernetFramesRx0(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRx1(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRx2(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRx3(const base_SW_RawEthernetMessage_Impl *data);
