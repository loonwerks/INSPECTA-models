#include <camkes.h>
#include <stdio.h>
#include <sb_types.h>
#include <sb_consumer_thread_i.h>

void init(const int64_t *in_arg) {
  printf("[%s] init called\n", get_instance_name());
}

void compute(const int64_t * in_arg) {
  int8_t value;

  if (sb_read_port_read(&value)) {
    printf("[%s] value {%d}\n", get_instance_name(), value);
  } else {
    printf("[%s] no value consumed.\n", get_instance_name());
  }
}