#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool put_EthernetFramesTx(const base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRx(base_SW_RawEthernetMessage_Impl *data);
