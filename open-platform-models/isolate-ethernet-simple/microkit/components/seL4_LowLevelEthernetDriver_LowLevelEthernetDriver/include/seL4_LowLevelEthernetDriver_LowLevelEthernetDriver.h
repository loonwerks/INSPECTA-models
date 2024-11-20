#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool get_EthernetFramesTx(base_SW_SizedEthernetMessage_Impl *data);
bool put_EthernetFramesRx(const base_SW_RawEthernetMessage_Impl *data);
