#include "seL4_Firewall_Firewall.h"

void seL4_Firewall_Firewall_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

int decode(base_SW_RawEthernetMessage x) {
  return (int32_t)(x[0] << 24) |
    (x[1] << 16) |
    (x[2] << 8) |
    (x[3]);
}

void seL4_Firewall_Firewall_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
  printf("-------%s------\n", microkit_name);

  if (!EthernetFramesTxIn_is_empty()) {
    base_SW_StructuredEthernetMessage_i tx;
    get_EthernetFramesTxIn(&tx);

    int txval = decode(tx.rawMessage);

    if (txval % 2 == 0) {
      //put_EthernetFramesTxOut(&tx);
      printf("TX Allowed %d\n",txval);
    } else {
      printf("TX Blocked %d\n", txval);
    }
  }

  ////////////////////////////////////////////////////
  ////////////////////////////////////////////////////

  if (!EthernetFramesRxIn_is_empty()) {
    base_SW_StructuredEthernetMessage_i rx;
    get_EthernetFramesRxIn(&rx);

    int rxval = decode(rx.rawMessage);

    if (rxval % 2 == 0) {
      put_EthernetFramesRxOut(&rx);
      printf("RX Allowed %d\n", rxval);
    } else {
      printf("RX Blocked %d\n",rxval);
    }
  }
}

void seL4_Firewall_Firewall_notify(microkit_channel channel){}