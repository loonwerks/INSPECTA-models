#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool EthernetFramesTxIn0_is_empty(void);
bool get_EthernetFramesTxIn0_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesTxIn0(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesTxIn1_is_empty(void);
bool get_EthernetFramesTxIn1_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesTxIn1(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesTxIn2_is_empty(void);
bool get_EthernetFramesTxIn2_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesTxIn2(base_SW_RawEthernetMessage_Impl *data);
bool EthernetFramesTxIn3_is_empty(void);
bool get_EthernetFramesTxIn3_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data);
bool get_EthernetFramesTxIn3(base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesTxOut0(const base_SW_SizedEthernetMessage_Impl *data);
bool put_EthernetFramesTxOut1(const base_SW_SizedEthernetMessage_Impl *data);
bool put_EthernetFramesTxOut2(const base_SW_SizedEthernetMessage_Impl *data);
bool put_EthernetFramesTxOut3(const base_SW_SizedEthernetMessage_Impl *data);
