#include "LowLevelEthernetDriver_LowLevelEthernetDriver.h"

// This file will not be overwritten if codegen is rerun

void LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void) {
  printf("%s: LowLevelEthernetDriver_LowLevelEthernetDriver_initialize invoked\n", microkit_name);
}

void LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered(void) {
  printf("%s: LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered invoked\n", microkit_name);
}

void LowLevelEthernetDriver_LowLevelEthernetDriver_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
