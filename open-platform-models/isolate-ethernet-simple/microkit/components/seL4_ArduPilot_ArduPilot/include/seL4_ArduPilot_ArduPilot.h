#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool put_EthernetFramesTx0(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesTx1(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesTx2(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesTx3(const base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRx0_is_empty(void);
bool get_EthernetFramesRx0_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRx0(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRx1_is_empty(void);
bool get_EthernetFramesRx1_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRx1(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRx2_is_empty(void);
bool get_EthernetFramesRx2_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRx2(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRx3_is_empty(void);
bool get_EthernetFramesRx3_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRx3(base_SW_RawEthernetMessage_Impl *data);
