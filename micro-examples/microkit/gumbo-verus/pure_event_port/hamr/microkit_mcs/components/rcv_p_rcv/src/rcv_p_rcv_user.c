#include "rcv_p_rcv.h"

// This file will not be overwritten if HAMR codegen is rerun

void rcv_p_rcv_initialize(void) {
  printf("%s: rcv_p_rcv_initialize invoked\n", microkit_name);
}

void rcv_p_rcv_timeTriggered(void) {
  printf("%s: rcv_p_rcv_timeTriggered invoked\n", microkit_name);
}

void rcv_p_rcv_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
