#include "seL4_Firewall_Firewall.h"

void seL4_Firewall_Firewall_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

int decode(base_SW_RawEthernetMessage_Impl x) {
  return (int32_t)(x[0] << 24) |
    (x[1] << 16) |
    (x[2] << 8) |
    (x[3]);
}
void seL4_Firewall_Firewall_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
  printf("-------%s------\n", microkit_name);

  {
    base_SW_RawEthernetMessage_Impl tx;
    get_EthernetFramesTxIn(&tx);

    int txval = decode(tx);

    if (txval % 2 == 0) {
      put_EthernetFramesTxOut(&tx);
      printf("TX Allowed %d\n",txval);
    } else {
      printf("TX Blocked %d\n", txval);
    }
  }

  ////////////////////////////////////////////////////
  ////////////////////////////////////////////////////

  {
    base_SW_RawEthernetMessage_Impl rx;
    get_EthernetFramesRxIn(&rx);

    int rxval = decode(rx);

    if (rxval % 2 == 0) {
      put_EthernetFramesRxOut(&rx);
      printf("RX Allowed %d\n", rxval);
    } else {
      printf("RX Blocked %d\n",rxval);
    }
  }
}

void seL4_Firewall_Firewall_notify(microkit_channel channel){}