#include "top_sub_B_sub_b.h"

// This file will not be overwritten if HAMR codegen is rerun

void top_sub_B_sub_b_initialize(void) {
  printf("%s: top_sub_B_sub_b_initialize invoked\n", microkit_name);
}

void top_sub_B_sub_b_timeTriggered(void) {
  printf("%s: top_sub_B_sub_b_timeTriggered invoked\n", microkit_name);
}

void top_sub_B_sub_b_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
