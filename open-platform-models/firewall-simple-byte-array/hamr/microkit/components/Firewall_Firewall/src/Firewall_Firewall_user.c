#include "Firewall_Firewall.h"

// This file will not be overwritten if codegen is rerun

void Firewall_Firewall_initialize(void) {
  printf("%s: Firewall_Firewall_initialize invoked\n", microkit_name);
}

void Firewall_Firewall_timeTriggered(void) {
  printf("%s: Firewall_Firewall_timeTriggered invoked\n", microkit_name);
}

void Firewall_Firewall_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
