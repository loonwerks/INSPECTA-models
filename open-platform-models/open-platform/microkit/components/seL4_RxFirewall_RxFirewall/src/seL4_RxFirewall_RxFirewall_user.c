#include "seL4_RxFirewall_RxFirewall.h"

// This file will not be overwritten if codegen is rerun

void seL4_RxFirewall_RxFirewall_initialize(void) {
  printf("%s: seL4_RxFirewall_RxFirewall_initialize invoked\n", microkit_name);
}

void seL4_RxFirewall_RxFirewall_timeTriggered(void) {
  printf("%s: seL4_RxFirewall_RxFirewall_timeTriggered invoked\n", microkit_name);
}

void seL4_RxFirewall_RxFirewall_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
