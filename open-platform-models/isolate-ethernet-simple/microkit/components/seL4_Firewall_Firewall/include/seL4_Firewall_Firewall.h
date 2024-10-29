#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <types.h>

void getEthernetFramesRxIn(uint8_t *value);
void getEthernetFramesTxIn(uint8_t *value);
void putEthernetFramesRxOut(uint8_t *value);
void putEthernetFramesTxOut(slang_SW_SizedEthernetMessage_Impl *value);
