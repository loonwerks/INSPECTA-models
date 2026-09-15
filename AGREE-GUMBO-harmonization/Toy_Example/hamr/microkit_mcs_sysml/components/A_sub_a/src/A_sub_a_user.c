#include "A_sub_a.h"

// This file will not be overwritten if HAMR codegen is rerun

void A_sub_a_initialize(void) {
  printf("%s: A_sub_a_initialize invoked\n", microkit_name);
}

void A_sub_a_timeTriggered(void) {
  printf("%s: A_sub_a_timeTriggered invoked\n", microkit_name);
}

void A_sub_a_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
