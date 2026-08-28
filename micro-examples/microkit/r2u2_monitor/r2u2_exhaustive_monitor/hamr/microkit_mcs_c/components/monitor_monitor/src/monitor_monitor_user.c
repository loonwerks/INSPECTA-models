#include "monitor_monitor.h"

// This file will not be overwritten if HAMR codegen is rerun

void monitor_monitor_initialize(void) {
  printf("%s: monitor_monitor_initialize invoked\n", microkit_name);
}

void monitor_monitor_timeTriggered(void) {
  int32_t level;
  get_level(&level);
  bool boolean_value;
  get_boolean_value(&boolean_value);
  char character_value;
  get_character_value(&character_value);
  int8_t signed_8_value;
  get_signed_8_value(&signed_8_value);
  int16_t signed_16_value;
  get_signed_16_value(&signed_16_value);
  uint8_t unsigned_8_value;
  get_unsigned_8_value(&unsigned_8_value);
  uint16_t unsigned_16_value;
  get_unsigned_16_value(&unsigned_16_value);
  int32_t sample;
  get_sample(&sample);
  bool flag;
  get_flag(&flag);
  Exhaustive_Monitor_C_OperatingState operating_state;
  get_operating_state(&operating_state);
  Exhaustive_Monitor_C_Samples samples;
  get_samples(&samples);
  Exhaustive_Monitor_C_Telemetry_i telemetry;
  get_telemetry(&telemetry);
  get_pulse();
  printf("%s: monitor_monitor_timeTriggered invoked\n", microkit_name);
}

void monitor_monitor_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
