#include <camkes.h>
#include <stdio.h>
#include <sb_types.h>
#include <sb_T2_i.h>

void init(const int64_t *in_arg) {
  printf("[%s] init called\n", get_instance_name());
}

void compute(const int64_t * in_arg) {
  int8_t value;
  
  if (sb_read_port_read(&value)) {
    printf("[%s] value {%d}\n", get_instance_name(), value);

    if (sb_write_port_write( &value )) {
      printf("[%s] Sent %d\n", get_instance_name(), value);
    } else {
      printf("[%s] Unable to send\n", get_instance_name());
    }
  } else {
    printf("[%s] no value consumed.\n", get_instance_name());
  }
}