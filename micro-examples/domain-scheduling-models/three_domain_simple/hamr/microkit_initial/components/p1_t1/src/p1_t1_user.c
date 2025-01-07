#include "p1_t1.h"

// This file will not be overwritten if codegen is rerun

void p1_t1_initialize(void) {
  printf("%s: p1_t1_initialize invoked\n", microkit_name);
}

void p1_t1_timeTriggered(void) {
  printf("%s: p1_t1_timeTriggered invoked\n", microkit_name);
}

void p1_t1_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
