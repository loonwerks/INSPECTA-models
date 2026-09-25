#include "monitor_monitor.h"
#include <r2u2.h>

// Content between markers will be preserved if codegen is rerun

extern unsigned char r2u2_spec_bin[];
extern unsigned int r2u2_spec_bin_len;

#define R2U2_SPEC_COUNT 46U

typedef struct {
  r2u2_monitor_t monitor;
  r2u2_verdict verdict_cache[R2U2_SPEC_COUNT]; // Cache latest verdict (if applicable) per C2PO specification between monitor steps.
  bool false_verdict_seen[R2U2_SPEC_COUNT]; // True if a false verdict arrived in this step.
  bool verdict_valid[R2U2_SPEC_COUNT]; // Track whether each cached verdict is current.
  bool verdict_updated[R2U2_SPEC_COUNT]; // Track whether each verdict was updated during this monitor step.
} r2u2_monitor_state_t;

// Instance of the R2U2 monitor.
static r2u2_monitor_state_t r2u2_monitor = {
  .monitor = R2U2_DEFAULT_MONITOR
};

extern volatile sb_queue_int32_t_1_t *echo_queue_1;
extern volatile sb_queue_uint8_t_1_t *ack_queue_1;
static sb_event_counter_t r2u2_port_echo_count;
static sb_event_counter_t r2u2_port_ack_count;

int32_t r2u2_state_sprevious_level = {0};

static bool r2u2_gumbo_isNonNegative(int32_t value);
static int32_t r2u2_gumbo_absoluteValue(int32_t value);

static bool r2u2_gumbo_isNonNegative(int32_t value) {
  return ((value) >= (0));
}

static int32_t r2u2_gumbo_absoluteValue(int32_t value) {
  return ((((value) < (0))) ? (-value) : (value));
}

void handle_r2u2_verdict(
    r2u2_property_t property,
    r2u2_verdict_status_t verdict) {
  // BEGIN MARKER R2U2 VERDICT HANDLER
  // Optional implementation placeholder for handling R2U2 verdicts.
  switch (property) {
    case input_event:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("input_event is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("input_event is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("input_event is currently unknown\n");
          break;
      }
      break;
    case input_event_data:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("input_event_data is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("input_event_data is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("input_event_data is currently unknown\n");
          break;
      }
      break;
    case input_data:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("input_data is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("input_data is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("input_data is currently unknown\n");
          break;
      }
      break;
    case base_type_character:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("base_type_character is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("base_type_character is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("base_type_character is currently unknown\n");
          break;
      }
      break;
    case base_type_integer_8:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("base_type_integer_8 is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("base_type_integer_8 is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("base_type_integer_8 is currently unknown\n");
          break;
      }
      break;
    case base_type_integer_16:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("base_type_integer_16 is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("base_type_integer_16 is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("base_type_integer_16 is currently unknown\n");
          break;
      }
      break;
    case base_type_integer_32:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("base_type_integer_32 is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("base_type_integer_32 is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("base_type_integer_32 is currently unknown\n");
          break;
      }
      break;
    case base_type_unsigned_8:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("base_type_unsigned_8 is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("base_type_unsigned_8 is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("base_type_unsigned_8 is currently unknown\n");
          break;
      }
      break;
    case base_type_unsigned_16:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("base_type_unsigned_16 is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("base_type_unsigned_16 is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("base_type_unsigned_16 is currently unknown\n");
          break;
      }
      break;
    case bool_signal:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("bool_signal is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("bool_signal is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("bool_signal is currently unknown\n");
          break;
      }
      break;
    case output_event_must_send:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("output_event_must_send is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("output_event_must_send is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("output_event_must_send is currently unknown\n");
          break;
      }
      break;
    case output_event_data_must_send:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("output_event_data_must_send is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("output_event_data_must_send is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("output_event_data_must_send is currently unknown\n");
          break;
      }
      break;
    case output_event_no_send:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("output_event_no_send is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("output_event_no_send is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("output_event_no_send is currently unknown\n");
          break;
      }
      break;
    case output_event_data_no_send:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("output_event_data_no_send is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("output_event_data_no_send is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("output_event_data_no_send is currently unknown\n");
          break;
      }
      break;
    case output_event_data_must_send_value:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("output_event_data_must_send_value is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("output_event_data_must_send_value is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("output_event_data_must_send_value is currently unknown\n");
          break;
      }
      break;
    case output_data:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("output_data is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("output_data is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("output_data is currently unknown\n");
          break;
      }
      break;
    case temporal_future:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_future is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_future is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_future is currently unknown\n");
          break;
      }
      break;
    case temporal_eventually:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_eventually is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_eventually is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_eventually is currently unknown\n");
          break;
      }
      break;
    case temporal_globally:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_globally is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_globally is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_globally is currently unknown\n");
          break;
      }
      break;
    case temporal_until:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_until is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_until is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_until is currently unknown\n");
          break;
      }
      break;
    case temporal_release:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_release is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_release is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_release is currently unknown\n");
          break;
      }
      break;
    case arithmetic_operators:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("arithmetic_operators is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("arithmetic_operators is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("arithmetic_operators is currently unknown\n");
          break;
      }
      break;
    case bitwise_operators:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("bitwise_operators is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("bitwise_operators is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("bitwise_operators is currently unknown\n");
          break;
      }
      break;
    case boolean_operators:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("boolean_operators is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("boolean_operators is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("boolean_operators is currently unknown\n");
          break;
      }
      break;
    case implication_operator:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("implication_operator is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("implication_operator is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("implication_operator is currently unknown\n");
          break;
      }
      break;
    case enum_signal:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("enum_signal is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("enum_signal is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("enum_signal is currently unknown\n");
          break;
      }
      break;
    case array_signal:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("array_signal is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("array_signal is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("array_signal is currently unknown\n");
          break;
      }
      break;
    case record_signal:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("record_signal is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("record_signal is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("record_signal is currently unknown\n");
          break;
      }
      break;
    case conditional_boolean:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("conditional_boolean is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("conditional_boolean is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("conditional_boolean is currently unknown\n");
          break;
      }
      break;
    case quantified_all_until:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_all_until is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_all_until is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_all_until is currently unknown\n");
          break;
      }
      break;
    case quantified_all_to:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_all_to is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_all_to is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_all_to is currently unknown\n");
          break;
      }
      break;
    case quantified_exists_until:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_exists_until is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_exists_until is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_exists_until is currently unknown\n");
          break;
      }
      break;
    case quantified_exists_to:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_exists_to is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_exists_to is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_exists_to is currently unknown\n");
          break;
      }
      break;
    case quantified_slice:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_slice is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_slice is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_slice is currently unknown\n");
          break;
      }
      break;
    case quantified_index_sensitive:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_index_sensitive is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_index_sensitive is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_index_sensitive is currently unknown\n");
          break;
      }
      break;
    case quantified_nested:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("quantified_nested is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("quantified_nested is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("quantified_nested is currently unknown\n");
          break;
      }
      break;
    case state_variable:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("state_variable is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("state_variable is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("state_variable is currently unknown\n");
          break;
      }
      break;
    case state_variable_in:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("state_variable_in is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("state_variable_in is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("state_variable_in is currently unknown\n");
          break;
      }
      break;
    case boolean_function:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("boolean_function is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("boolean_function is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("boolean_function is currently unknown\n");
          break;
      }
      break;
    case value_function:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("value_function is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("value_function is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("value_function is currently unknown\n");
          break;
      }
      break;
    case temporal_once:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_once is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_once is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_once is currently unknown\n");
          break;
      }
      break;
    case temporal_historically:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_historically is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_historically is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_historically is currently unknown\n");
          break;
      }
      break;
    case temporal_since:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_since is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_since is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_since is currently unknown\n");
          break;
      }
      break;
    case temporal_trigger:
      switch (verdict) {
        case R2U2_VERDICT_TRUE:
          printf("temporal_trigger is currently True\n");
          break;
        case R2U2_VERDICT_FALSE:
          printf("temporal_trigger is currently False\n");
          break;
        case R2U2_VERDICT_UNKNOWN:
          printf("temporal_trigger is currently unknown\n");
          break;
      }
      break;
    default:
      break;
  }
  // END MARKER R2U2 VERDICT HANDLER
}

// Cache the latest verdict and remember any false verdict in this step.
static r2u2_status_t r2u2_cache_output(
    r2u2_mltl_instruction_t instruction,
    r2u2_verdict *verdict) {
  size_t spec_number = instruction.op2_value;
  if (verdict == NULL || spec_number >= R2U2_SPEC_COUNT) {
    return R2U2_ERR_OTHER;
  }
  if (!get_verdict_truth(*verdict)) {
    r2u2_monitor.false_verdict_seen[spec_number] = true;
  }
  r2u2_monitor.verdict_cache[spec_number] = *verdict;
  r2u2_monitor.verdict_valid[spec_number] = true;
  r2u2_monitor.verdict_updated[spec_number] = true;
  return R2U2_OK;
}

void r2u2_monitor_initialize(void) {
  for (size_t i = 0; i < R2U2_SPEC_COUNT; ++i) {
    r2u2_monitor.verdict_valid[i] = false;
  }
  r2u2_monitor.monitor.out_file = NULL;
  r2u2_monitor.monitor.out_func = r2u2_cache_output;
  if (r2u2_spec_bin_len == 0U) {
    printf("R2U2 specification is empty\n");
    return;
  }
  r2u2_status_t status = r2u2_load_specification(r2u2_spec_bin, &r2u2_monitor.monitor);
  if (status != R2U2_OK) {
    printf("R2U2 specification load failed: %d\n", (int) status);
    return;
  }
}

void r2u2_monitor_pre_timeTriggered(void) {
  int32_t r2u2_port_level;
  peek_level(&r2u2_port_level);

  bool r2u2_port_boolean_value;
  peek_boolean_value(&r2u2_port_boolean_value);

  char r2u2_port_character_value;
  peek_character_value(&r2u2_port_character_value);

  int8_t r2u2_port_signed_8_value;
  peek_signed_8_value(&r2u2_port_signed_8_value);

  int16_t r2u2_port_signed_16_value;
  peek_signed_16_value(&r2u2_port_signed_16_value);

  uint8_t r2u2_port_unsigned_8_value;
  peek_unsigned_8_value(&r2u2_port_unsigned_8_value);

  uint16_t r2u2_port_unsigned_16_value;
  peek_unsigned_16_value(&r2u2_port_unsigned_16_value);

  int32_t r2u2_port_sample = {0};
  bool r2u2_port_sample_present = peek_sample(&r2u2_port_sample);

  bool r2u2_port_flag = {0};
  bool r2u2_port_flag_present = peek_flag(&r2u2_port_flag);

  Exhaustive_Monitor_C_OperatingState r2u2_port_operating_state = {0};
  bool r2u2_port_operating_state_present = peek_operating_state(&r2u2_port_operating_state);

  Exhaustive_Monitor_C_Samples r2u2_port_samples = {0};
  bool r2u2_port_samples_present = peek_samples(&r2u2_port_samples);

  Exhaustive_Monitor_C_Telemetry_i r2u2_port_telemetry = {0};
  bool r2u2_port_telemetry_present = peek_telemetry(&r2u2_port_telemetry);

  r2u2_port_echo_count = echo_queue_1->numSent;

  bool r2u2_port_pulse_present = peek_pulse();

  r2u2_port_ack_count = ack_queue_1->numSent;

  r2u2_load_bool_signal(&r2u2_monitor.monitor, 0, r2u2_port_pulse_present); // Loading signal api_pulse_nonEmpty into index 0
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 1, r2u2_port_sample_present); // Loading signal api_sample_nonEmpty into index 1
  r2u2_load_int_signal(&r2u2_monitor.monitor, 2, (int32_t) (r2u2_port_sample)); // Loading signal api_sample into index 2
  r2u2_load_int_signal(&r2u2_monitor.monitor, 3, (int32_t) (r2u2_port_level)); // Loading signal api_level into index 3
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 4, r2u2_port_boolean_value); // Loading signal api_boolean_value into index 4
  r2u2_load_int_signal(&r2u2_monitor.monitor, 5, (int32_t) (r2u2_port_character_value)); // Loading signal api_character_value into index 5
  r2u2_load_int_signal(&r2u2_monitor.monitor, 6, (int32_t) (r2u2_port_signed_8_value)); // Loading signal api_signed_8_value into index 6
  r2u2_load_int_signal(&r2u2_monitor.monitor, 7, (int32_t) (r2u2_port_signed_16_value)); // Loading signal api_signed_16_value into index 7
  r2u2_load_int_signal(&r2u2_monitor.monitor, 8, (int32_t) (r2u2_port_unsigned_8_value)); // Loading signal api_unsigned_8_value into index 8
  r2u2_load_int_signal(&r2u2_monitor.monitor, 9, (int32_t) (r2u2_port_unsigned_16_value)); // Loading signal api_unsigned_16_value into index 9
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 10, r2u2_port_flag_present); // Loading signal api_flag_nonEmpty into index 10
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 11, r2u2_port_flag); // Loading signal api_flag into index 11
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 18, r2u2_port_operating_state_present); // Loading signal api_operating_state_nonEmpty into index 18
  r2u2_load_int_signal(&r2u2_monitor.monitor, 19, (int32_t) (r2u2_port_operating_state)); // Loading enum signal api_operating_state into index 19
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 20, r2u2_port_samples_present); // Loading signal api_samples_nonEmpty into index 20
  r2u2_load_int_signal(&r2u2_monitor.monitor, 21, (int32_t) (4)); // Loading signal api_samples_size into index 21
  {
    // Loading array signal api_samples into indices 22..25
    for (size_t r2u2_i = 0; r2u2_i < 4; ++r2u2_i) {
      r2u2_load_int_signal(&r2u2_monitor.monitor, 22 + r2u2_i, (int32_t) ((r2u2_port_samples)[r2u2_i]));
    }
  }
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 26, r2u2_port_telemetry_present); // Loading signal api_telemetry_nonEmpty into index 26
  r2u2_load_int_signal(&r2u2_monitor.monitor, 27, (int32_t) ((r2u2_port_telemetry).sequence_number)); // Loading signal api_telemetry_sequence_number into index 27
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 28, (r2u2_port_telemetry).valid); // Loading signal api_telemetry_valid into index 28
  r2u2_load_int_signal(&r2u2_monitor.monitor, 29, (int32_t) ((r2u2_port_telemetry).operating_state)); // Loading enum signal api_telemetry_operating_state into index 29
  {
    // Loading array signal api_telemetry_values into indices 30..33
    for (size_t r2u2_i = 0; r2u2_i < 4; ++r2u2_i) {
      r2u2_load_int_signal(&r2u2_monitor.monitor, 30 + r2u2_i, (int32_t) (((r2u2_port_telemetry).values)[r2u2_i]));
    }
  }
  r2u2_load_int_signal(&r2u2_monitor.monitor, 35, (int32_t) (r2u2_state_sprevious_level)); // Loading signal in_sprevious_level into index 35
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 36, r2u2_gumbo_isNonNegative(r2u2_port_level)); // Loading signal fn_isNonNegative_level into index 36
  r2u2_load_int_signal(&r2u2_monitor.monitor, 37, (int32_t) (r2u2_gumbo_absoluteValue(r2u2_port_level))); // Loading signal fn_absoluteValue_level into index 37
}

void r2u2_monitor_post_timeTriggered(void) {
  bool r2u2_port_healthy;
  peek_healthy(&r2u2_port_healthy);

  int32_t r2u2_port_echo = {0};
  bool r2u2_port_echo_present = echo_queue_1->numSent != r2u2_port_echo_count;
  if (r2u2_port_echo_present) {
    peek_echo(&r2u2_port_echo);
  }

  bool r2u2_port_ack_present = ack_queue_1->numSent != r2u2_port_ack_count;

  r2u2_load_bool_signal(&r2u2_monitor.monitor, 12, r2u2_port_ack_present); // Loading signal api_ack_nonEmpty into index 12
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 13, r2u2_port_echo_present); // Loading signal api_echo_nonEmpty into index 13
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 14, !r2u2_port_ack_present); // Loading signal api_ack_isEmpty into index 14
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 15, !r2u2_port_echo_present); // Loading signal api_echo_isEmpty into index 15
  r2u2_load_int_signal(&r2u2_monitor.monitor, 16, (int32_t) (r2u2_port_echo)); // Loading signal api_echo into index 16
  r2u2_load_bool_signal(&r2u2_monitor.monitor, 17, r2u2_port_healthy); // Loading signal api_healthy into index 17
  r2u2_load_int_signal(&r2u2_monitor.monitor, 34, (int32_t) (r2u2_state_sprevious_level)); // Loading signal sprevious_level into index 34

  for (size_t i = 0; i < R2U2_SPEC_COUNT; ++i) {
    r2u2_monitor.verdict_updated[i] = false;
    r2u2_monitor.false_verdict_seen[i] = false;
  }
  r2u2_status_t status = r2u2_step(&r2u2_monitor.monitor);
  if (status != R2U2_OK) {
    printf("R2U2 monitor step failed: %d\n", (int) status);
    return;
  }

  // The callback runs during r2u2_step. Expire older cached verdicts
  // against the new timestamp without discarding this step's outputs.
  for (size_t i = 0; i < R2U2_SPEC_COUNT; ++i) {
    if (r2u2_monitor.verdict_valid[i] && !r2u2_monitor.verdict_updated[i] &&
        r2u2_monitor.monitor.time_stamp > get_verdict_time(r2u2_monitor.verdict_cache[i])) {
      r2u2_monitor.verdict_valid[i] = false;
    }
  }

  // Delegate each current verdict to the preserved policy handler.
  r2u2_verdict_status_t r2u2_input_event_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[0]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[0]) &&
        !r2u2_monitor.false_verdict_seen[0];
    r2u2_input_event_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(input_event, r2u2_input_event_verdict);
  r2u2_verdict_status_t r2u2_input_event_data_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[1]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[1]) &&
        !r2u2_monitor.false_verdict_seen[1];
    r2u2_input_event_data_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(input_event_data, r2u2_input_event_data_verdict);
  r2u2_verdict_status_t r2u2_input_data_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[2]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[2]) &&
        !r2u2_monitor.false_verdict_seen[2];
    r2u2_input_data_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(input_data, r2u2_input_data_verdict);
  r2u2_verdict_status_t r2u2_base_type_boolean_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[3]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[3]) &&
        !r2u2_monitor.false_verdict_seen[3];
    r2u2_base_type_boolean_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_boolean, r2u2_base_type_boolean_verdict);
  r2u2_verdict_status_t r2u2_base_type_character_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[4]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[4]) &&
        !r2u2_monitor.false_verdict_seen[4];
    r2u2_base_type_character_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_character, r2u2_base_type_character_verdict);
  r2u2_verdict_status_t r2u2_base_type_integer_8_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[5]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[5]) &&
        !r2u2_monitor.false_verdict_seen[5];
    r2u2_base_type_integer_8_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_integer_8, r2u2_base_type_integer_8_verdict);
  r2u2_verdict_status_t r2u2_base_type_integer_16_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[6]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[6]) &&
        !r2u2_monitor.false_verdict_seen[6];
    r2u2_base_type_integer_16_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_integer_16, r2u2_base_type_integer_16_verdict);
  r2u2_verdict_status_t r2u2_base_type_integer_32_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[7]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[7]) &&
        !r2u2_monitor.false_verdict_seen[7];
    r2u2_base_type_integer_32_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_integer_32, r2u2_base_type_integer_32_verdict);
  r2u2_verdict_status_t r2u2_base_type_unsigned_8_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[8]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[8]) &&
        !r2u2_monitor.false_verdict_seen[8];
    r2u2_base_type_unsigned_8_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_unsigned_8, r2u2_base_type_unsigned_8_verdict);
  r2u2_verdict_status_t r2u2_base_type_unsigned_16_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[9]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[9]) &&
        !r2u2_monitor.false_verdict_seen[9];
    r2u2_base_type_unsigned_16_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(base_type_unsigned_16, r2u2_base_type_unsigned_16_verdict);
  r2u2_verdict_status_t r2u2_bool_signal_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[10]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[10]) &&
        !r2u2_monitor.false_verdict_seen[10];
    r2u2_bool_signal_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(bool_signal, r2u2_bool_signal_verdict);
  r2u2_verdict_status_t r2u2_output_event_must_send_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[11]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[11]) &&
        !r2u2_monitor.false_verdict_seen[11];
    r2u2_output_event_must_send_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(output_event_must_send, r2u2_output_event_must_send_verdict);
  r2u2_verdict_status_t r2u2_output_event_data_must_send_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[12]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[12]) &&
        !r2u2_monitor.false_verdict_seen[12];
    r2u2_output_event_data_must_send_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(output_event_data_must_send, r2u2_output_event_data_must_send_verdict);
  r2u2_verdict_status_t r2u2_output_event_no_send_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[13]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[13]) &&
        !r2u2_monitor.false_verdict_seen[13];
    r2u2_output_event_no_send_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(output_event_no_send, r2u2_output_event_no_send_verdict);
  r2u2_verdict_status_t r2u2_output_event_data_no_send_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[14]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[14]) &&
        !r2u2_monitor.false_verdict_seen[14];
    r2u2_output_event_data_no_send_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(output_event_data_no_send, r2u2_output_event_data_no_send_verdict);
  r2u2_verdict_status_t r2u2_output_event_data_must_send_value_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[15]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[15]) &&
        !r2u2_monitor.false_verdict_seen[15];
    r2u2_output_event_data_must_send_value_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(output_event_data_must_send_value, r2u2_output_event_data_must_send_value_verdict);
  r2u2_verdict_status_t r2u2_output_data_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[16]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[16]) &&
        !r2u2_monitor.false_verdict_seen[16];
    r2u2_output_data_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(output_data, r2u2_output_data_verdict);
  r2u2_verdict_status_t r2u2_temporal_future_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[17]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[17]) &&
        !r2u2_monitor.false_verdict_seen[17];
    r2u2_temporal_future_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_future, r2u2_temporal_future_verdict);
  r2u2_verdict_status_t r2u2_temporal_eventually_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[18]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[18]) &&
        !r2u2_monitor.false_verdict_seen[18];
    r2u2_temporal_eventually_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_eventually, r2u2_temporal_eventually_verdict);
  r2u2_verdict_status_t r2u2_temporal_globally_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[19]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[19]) &&
        !r2u2_monitor.false_verdict_seen[19];
    r2u2_temporal_globally_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_globally, r2u2_temporal_globally_verdict);
  r2u2_verdict_status_t r2u2_temporal_until_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[20]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[20]) &&
        !r2u2_monitor.false_verdict_seen[20];
    r2u2_temporal_until_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_until, r2u2_temporal_until_verdict);
  r2u2_verdict_status_t r2u2_temporal_release_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[21]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[21]) &&
        !r2u2_monitor.false_verdict_seen[21];
    r2u2_temporal_release_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_release, r2u2_temporal_release_verdict);
  r2u2_verdict_status_t r2u2_comparison_operators_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[22]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[22]) &&
        !r2u2_monitor.false_verdict_seen[22];
    r2u2_comparison_operators_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(comparison_operators, r2u2_comparison_operators_verdict);
  r2u2_verdict_status_t r2u2_arithmetic_operators_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[23]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[23]) &&
        !r2u2_monitor.false_verdict_seen[23];
    r2u2_arithmetic_operators_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(arithmetic_operators, r2u2_arithmetic_operators_verdict);
  r2u2_verdict_status_t r2u2_bitwise_operators_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[24]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[24]) &&
        !r2u2_monitor.false_verdict_seen[24];
    r2u2_bitwise_operators_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(bitwise_operators, r2u2_bitwise_operators_verdict);
  r2u2_verdict_status_t r2u2_boolean_operators_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[25]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[25]) &&
        !r2u2_monitor.false_verdict_seen[25];
    r2u2_boolean_operators_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(boolean_operators, r2u2_boolean_operators_verdict);
  r2u2_verdict_status_t r2u2_implication_operator_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[26]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[26]) &&
        !r2u2_monitor.false_verdict_seen[26];
    r2u2_implication_operator_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(implication_operator, r2u2_implication_operator_verdict);
  r2u2_verdict_status_t r2u2_enum_signal_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[27]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[27]) &&
        !r2u2_monitor.false_verdict_seen[27];
    r2u2_enum_signal_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(enum_signal, r2u2_enum_signal_verdict);
  r2u2_verdict_status_t r2u2_array_signal_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[28]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[28]) &&
        !r2u2_monitor.false_verdict_seen[28];
    r2u2_array_signal_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(array_signal, r2u2_array_signal_verdict);
  r2u2_verdict_status_t r2u2_record_signal_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[29]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[29]) &&
        !r2u2_monitor.false_verdict_seen[29];
    r2u2_record_signal_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(record_signal, r2u2_record_signal_verdict);
  r2u2_verdict_status_t r2u2_conditional_boolean_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[30]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[30]) &&
        !r2u2_monitor.false_verdict_seen[30];
    r2u2_conditional_boolean_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(conditional_boolean, r2u2_conditional_boolean_verdict);
  r2u2_verdict_status_t r2u2_quantified_all_until_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[31]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[31]) &&
        !r2u2_monitor.false_verdict_seen[31];
    r2u2_quantified_all_until_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_all_until, r2u2_quantified_all_until_verdict);
  r2u2_verdict_status_t r2u2_quantified_all_to_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[32]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[32]) &&
        !r2u2_monitor.false_verdict_seen[32];
    r2u2_quantified_all_to_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_all_to, r2u2_quantified_all_to_verdict);
  r2u2_verdict_status_t r2u2_quantified_exists_until_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[33]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[33]) &&
        !r2u2_monitor.false_verdict_seen[33];
    r2u2_quantified_exists_until_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_exists_until, r2u2_quantified_exists_until_verdict);
  r2u2_verdict_status_t r2u2_quantified_exists_to_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[34]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[34]) &&
        !r2u2_monitor.false_verdict_seen[34];
    r2u2_quantified_exists_to_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_exists_to, r2u2_quantified_exists_to_verdict);
  r2u2_verdict_status_t r2u2_quantified_slice_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[35]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[35]) &&
        !r2u2_monitor.false_verdict_seen[35];
    r2u2_quantified_slice_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_slice, r2u2_quantified_slice_verdict);
  r2u2_verdict_status_t r2u2_quantified_index_sensitive_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[36]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[36]) &&
        !r2u2_monitor.false_verdict_seen[36];
    r2u2_quantified_index_sensitive_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_index_sensitive, r2u2_quantified_index_sensitive_verdict);
  r2u2_verdict_status_t r2u2_quantified_nested_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[37]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[37]) &&
        !r2u2_monitor.false_verdict_seen[37];
    r2u2_quantified_nested_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(quantified_nested, r2u2_quantified_nested_verdict);
  r2u2_verdict_status_t r2u2_state_variable_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[38]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[38]) &&
        !r2u2_monitor.false_verdict_seen[38];
    r2u2_state_variable_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(state_variable, r2u2_state_variable_verdict);
  r2u2_verdict_status_t r2u2_state_variable_in_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[39]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[39]) &&
        !r2u2_monitor.false_verdict_seen[39];
    r2u2_state_variable_in_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(state_variable_in, r2u2_state_variable_in_verdict);
  r2u2_verdict_status_t r2u2_boolean_function_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[40]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[40]) &&
        !r2u2_monitor.false_verdict_seen[40];
    r2u2_boolean_function_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(boolean_function, r2u2_boolean_function_verdict);
  r2u2_verdict_status_t r2u2_value_function_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[41]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[41]) &&
        !r2u2_monitor.false_verdict_seen[41];
    r2u2_value_function_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(value_function, r2u2_value_function_verdict);
  r2u2_verdict_status_t r2u2_temporal_once_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[42]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[42]) &&
        !r2u2_monitor.false_verdict_seen[42];
    r2u2_temporal_once_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_once, r2u2_temporal_once_verdict);
  r2u2_verdict_status_t r2u2_temporal_historically_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[43]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[43]) &&
        !r2u2_monitor.false_verdict_seen[43];
    r2u2_temporal_historically_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_historically, r2u2_temporal_historically_verdict);
  r2u2_verdict_status_t r2u2_temporal_since_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[44]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[44]) &&
        !r2u2_monitor.false_verdict_seen[44];
    r2u2_temporal_since_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_since, r2u2_temporal_since_verdict);
  r2u2_verdict_status_t r2u2_temporal_trigger_verdict = R2U2_VERDICT_UNKNOWN;
  if (r2u2_monitor.verdict_valid[45]) {
    bool truth = get_verdict_truth(r2u2_monitor.verdict_cache[45]) &&
        !r2u2_monitor.false_verdict_seen[45];
    r2u2_temporal_trigger_verdict = truth ? R2U2_VERDICT_TRUE : R2U2_VERDICT_FALSE;
  }
  handle_r2u2_verdict(temporal_trigger, r2u2_temporal_trigger_verdict);

  // Send one result through each mapped alert port.
  if (r2u2_base_type_boolean_verdict != R2U2_VERDICT_UNKNOWN) {
    bool truth = r2u2_base_type_boolean_verdict == R2U2_VERDICT_TRUE;
    (void) put_alert_result(&truth);
  }
  if (r2u2_comparison_operators_verdict != R2U2_VERDICT_UNKNOWN) {
    bool truth = r2u2_comparison_operators_verdict == R2U2_VERDICT_TRUE;
    if (!truth) {
      (void) put_ack();
    }
  }
}
