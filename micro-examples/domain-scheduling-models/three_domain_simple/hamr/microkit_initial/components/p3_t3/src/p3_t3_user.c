#include "p3_t3.h"

// This file will not be overwritten if codegen is rerun

void p3_t3_initialize(void) {
  printf("%s: p3_t3_initialize invoked\n", microkit_name);
}

void p3_t3_timeTriggered(void) {
  printf("%s: p3_t3_timeTriggered invoked\n", microkit_name);
}

void p3_t3_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
