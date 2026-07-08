#include "tcp_tct.h"

// This file will not be overwritten if HAMR codegen is rerun

void tcp_tct_initialize(void) {
  printf("%s: tcp_tct_initialize invoked\n", microkit_name);
}

void tcp_tct_timeTriggered(void) {
  printf("%s: tcp_tct_timeTriggered invoked\n", microkit_name);
}

void tcp_tct_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
