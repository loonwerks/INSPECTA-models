#include <camkes.h>
#include <stdio.h>
#include <sb_types.h>
#include <sb_consumer_t_i.h>

void init(const int64_t *in_arg) {
  printf("[%s] init called\n", get_instance_name());
}

void compute(const int64_t * in_arg) {
  /* keep dequeuing until no more things can be had
   */
  int8_t value;

  while (sb_read_port_dequeue(&value)) {
    printf("[%s] value {%d}\n", get_instance_name(), value);
  } 
}