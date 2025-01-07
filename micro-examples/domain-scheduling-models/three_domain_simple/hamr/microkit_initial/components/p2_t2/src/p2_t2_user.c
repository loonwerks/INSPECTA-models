#include "p2_t2.h"

// This file will not be overwritten if codegen is rerun

void p2_t2_initialize(void) {
  printf("%s: p2_t2_initialize invoked\n", microkit_name);
}

void p2_t2_timeTriggered(void) {
  printf("%s: p2_t2_timeTriggered invoked\n", microkit_name);
}

void p2_t2_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
