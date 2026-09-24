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

// Default logging policy for monitored properties without an alert mapping.
static void log_r2u2_verdict(const char *property, r2u2_verdict_status_t verdict) {
  const char *status = "unknown";
  switch (verdict) {
    case R2U2_VERDICT_TRUE:
      status = "true";
      break;
    case R2U2_VERDICT_FALSE:
      status = "false";
      break;
    case R2U2_VERDICT_UNKNOWN:
      break;
  }
  printf("%s is currently %s\n", property, status);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `input_event`.
void handle_input_event_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("input_event", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `input_event_data`.
void handle_input_event_data_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("input_event_data", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `input_data`.
void handle_input_data_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("input_data", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_boolean`.
void handle_base_type_boolean_verdict(r2u2_verdict_status_t verdict) {
  // This verdict is already mapped to monitor-owned alert port
  // alert_result. Do not write this port from this callback.
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_character`.
void handle_base_type_character_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("base_type_character", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_integer_8`.
void handle_base_type_integer_8_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("base_type_integer_8", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_integer_16`.
void handle_base_type_integer_16_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("base_type_integer_16", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_integer_32`.
void handle_base_type_integer_32_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("base_type_integer_32", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_unsigned_8`.
void handle_base_type_unsigned_8_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("base_type_unsigned_8", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_unsigned_16`.
void handle_base_type_unsigned_16_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("base_type_unsigned_16", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `bool_signal`.
void handle_bool_signal_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("bool_signal", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_must_send`.
void handle_output_event_must_send_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("output_event_must_send", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_data_must_send`.
void handle_output_event_data_must_send_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("output_event_data_must_send", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_no_send`.
void handle_output_event_no_send_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("output_event_no_send", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_data_no_send`.
void handle_output_event_data_no_send_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("output_event_data_no_send", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_data_must_send_value`.
void handle_output_event_data_must_send_value_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("output_event_data_must_send_value", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `output_data`.
void handle_output_data_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("output_data", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_future`.
void handle_temporal_future_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_future", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_eventually`.
void handle_temporal_eventually_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_eventually", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_globally`.
void handle_temporal_globally_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_globally", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_until`.
void handle_temporal_until_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_until", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_release`.
void handle_temporal_release_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_release", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `comparison_operators`.
void handle_comparison_operators_verdict(r2u2_verdict_status_t verdict) {
  // This verdict is already mapped to monitor-owned alert port
  // ack. Do not write this port from this callback.
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `arithmetic_operators`.
void handle_arithmetic_operators_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("arithmetic_operators", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `bitwise_operators`.
void handle_bitwise_operators_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("bitwise_operators", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `boolean_operators`.
void handle_boolean_operators_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("boolean_operators", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `implication_operator`.
void handle_implication_operator_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("implication_operator", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `enum_signal`.
void handle_enum_signal_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("enum_signal", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `array_signal`.
void handle_array_signal_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("array_signal", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `record_signal`.
void handle_record_signal_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("record_signal", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `conditional_boolean`.
void handle_conditional_boolean_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("conditional_boolean", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_all_until`.
void handle_quantified_all_until_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_all_until", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_all_to`.
void handle_quantified_all_to_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_all_to", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_exists_until`.
void handle_quantified_exists_until_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_exists_until", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_exists_to`.
void handle_quantified_exists_to_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_exists_to", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_slice`.
void handle_quantified_slice_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_slice", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_index_sensitive`.
void handle_quantified_index_sensitive_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_index_sensitive", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_nested`.
void handle_quantified_nested_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("quantified_nested", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `state_variable`.
void handle_state_variable_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("state_variable", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `state_variable_in`.
void handle_state_variable_in_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("state_variable_in", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `boolean_function`.
void handle_boolean_function_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("boolean_function", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `value_function`.
void handle_value_function_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("value_function", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_once`.
void handle_temporal_once_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_once", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_historically`.
void handle_temporal_historically_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_historically", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_since`.
void handle_temporal_since_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_since", verdict);
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_trigger`.
void handle_temporal_trigger_verdict(r2u2_verdict_status_t verdict) {
  log_r2u2_verdict("temporal_trigger", verdict);
}
