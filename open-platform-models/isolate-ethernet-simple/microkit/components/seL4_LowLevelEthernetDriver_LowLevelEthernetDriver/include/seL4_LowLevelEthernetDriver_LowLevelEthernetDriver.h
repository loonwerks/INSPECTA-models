#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool EthernetFramesTx0_is_empty(void);
bool get_EthernetFramesTx0_poll(sb_event_counter_t *numDropped, base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx0(base_SW_SizedEthernetMessage_Impl *data);
bool EthernetFramesTx1_is_empty(void);
bool get_EthernetFramesTx1_poll(sb_event_counter_t *numDropped, base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx1(base_SW_SizedEthernetMessage_Impl *data);
bool EthernetFramesTx2_is_empty(void);
bool get_EthernetFramesTx2_poll(sb_event_counter_t *numDropped, base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx2(base_SW_SizedEthernetMessage_Impl *data);
bool EthernetFramesTx3_is_empty(void);
bool get_EthernetFramesTx3_poll(sb_event_counter_t *numDropped, base_SW_SizedEthernetMessage_Impl *data);
bool get_EthernetFramesTx3(base_SW_SizedEthernetMessage_Impl *data);
bool put_EthernetFramesRx0(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRx1(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRx2(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRx3(const base_SW_RawEthernetMessage_Impl *data);
