#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool put_EthernetFramesRxOut0(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRxOut1(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRxOut2(const base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesRxOut3(const base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRxIn0_is_empty(void);
bool get_EthernetFramesRxIn0_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRxIn0(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRxIn1_is_empty(void);
bool get_EthernetFramesRxIn1_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRxIn1(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRxIn2_is_empty(void);
bool get_EthernetFramesRxIn2_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRxIn2(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesRxIn3_is_empty(void);
bool get_EthernetFramesRxIn3_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesRxIn3(base_SW_RawEthernetMessage_Impl *data);
