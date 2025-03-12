#include "operator_interface_oip_oit.h"

// This file will not be overwritten if codegen is rerun

void operator_interface_oip_oit_initialize(void) {
  printf("%s: operator_interface_oip_oit_initialize invoked\n", microkit_name);
}

void operator_interface_oip_oit_timeTriggered(void) {
  printf("%s: operator_interface_oip_oit_timeTriggered invoked\n", microkit_name);
}

void operator_interface_oip_oit_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
