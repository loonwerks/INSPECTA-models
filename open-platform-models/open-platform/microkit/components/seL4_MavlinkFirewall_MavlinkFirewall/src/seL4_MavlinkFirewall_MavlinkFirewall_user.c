#include "seL4_MavlinkFirewall_MavlinkFirewall.h"

// This file will not be overwritten if codegen is rerun

void seL4_MavlinkFirewall_MavlinkFirewall_initialize(void) {
  printf("%s: seL4_MavlinkFirewall_MavlinkFirewall_initialize invoked\n", microkit_name);
}

void seL4_MavlinkFirewall_MavlinkFirewall_timeTriggered(void) {
  printf("%s: seL4_MavlinkFirewall_MavlinkFirewall_timeTriggered invoked\n", microkit_name);
}

void seL4_MavlinkFirewall_MavlinkFirewall_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
