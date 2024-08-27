#include <camkes.h>
#include <stdio.h>
#include <sb_types.h>
#include <sb_consumer_t_i.h>

static int8_t _value;

void init(const int64_t *in_arg) {
  printf("[%s] init called\n", get_instance_name());
  _value = 0;
}

void compute(const int64_t * in_arg) {
  int8_t old_value = _value;
  while(sb_consume_dequeue()) {
    _value = (_value + 1) % 500;
  }
  
  printf("[%s] Received %i events this dispatch, %i total\n", get_instance_name(), (_value - old_value), _value);
}