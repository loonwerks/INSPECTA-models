#include "ArduPilot_ArduPilot.h"

// This file will not be overwritten if codegen is rerun

void ArduPilot_ArduPilot_initialize(void) {
  printf("%s: ArduPilot_ArduPilot_initialize invoked\n", microkit_name);
}

void ArduPilot_ArduPilot_timeTriggered(void) {
  printf("%s: ArduPilot_ArduPilot_timeTriggered invoked\n", microkit_name);
}

void ArduPilot_ArduPilot_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
