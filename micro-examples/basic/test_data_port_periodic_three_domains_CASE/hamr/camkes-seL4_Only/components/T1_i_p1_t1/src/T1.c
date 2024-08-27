#include <camkes.h>
#include <stdio.h>
#include <sb_types.h>
#include <sb_T1_i.h>

static int8_t _value;

void init(const int64_t *in_arg) {
  printf("[%s] called\n", get_instance_name());
  _value = 0;
}

void compute(const int64_t *in_arg) {
  printf("---------------------------------------\n");
  if (sb_write_port_write( &_value )) {
    printf("[%s] Sent %d\n", get_instance_name(), _value);
    _value = (_value + 1) % 500;
  } else {
    printf("[%s] Unable to send\n", get_instance_name());
  }
}