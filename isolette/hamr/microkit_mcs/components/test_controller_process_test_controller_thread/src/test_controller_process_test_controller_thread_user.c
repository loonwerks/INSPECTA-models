#include "test_controller_process_test_controller_thread.h"

// This file will not be overwritten if HAMR codegen is rerun

void test_controller_process_test_controller_thread_initialize(void) {
  printf("%s: test_controller_process_test_controller_thread_initialize invoked\n", microkit_name);
}

void test_controller_process_test_controller_thread_timeTriggered(void) {
  printf("%s: test_controller_process_test_controller_thread_timeTriggered invoked\n", microkit_name);
}

void test_controller_process_test_controller_thread_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
