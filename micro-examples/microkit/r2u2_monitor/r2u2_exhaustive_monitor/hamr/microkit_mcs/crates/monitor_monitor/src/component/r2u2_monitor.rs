// Content between markers will be preserved if codegen is rerun

use crate::bridge::monitor_monitor_api::*;
use crate::bridge::monitor_monitor_GUMBOX as GUMBOX;
use data::*;
use super::monitor_monitor_app::{monitor_monitor, log_info};
use vstd::prelude::*;

// Instance of the R2U2 monitor.
static mut R2U2_MONITOR: Option<R2U2Monitor> = None;

#[allow(non_camel_case_types)]
pub enum R2U2Property {
  input_event,
  input_event_data,
  input_data,
  base_type_boolean,
  base_type_character,
  base_type_integer_8,
  base_type_integer_16,
  base_type_integer_32,
  base_type_unsigned_8,
  base_type_unsigned_16,
  bool_signal,
  output_event_must_send,
  output_event_data_must_send,
  output_event_no_send,
  output_event_data_no_send,
  output_event_data_must_send_value,
  output_data,
  temporal_future,
  temporal_eventually,
  temporal_globally,
  temporal_until,
  temporal_release,
  comparison_operators,
  arithmetic_operators,
  bitwise_operators,
  boolean_operators,
  implication_operator,
  enum_signal,
  array_signal,
  record_signal,
  conditional_boolean,
  quantified_all_until,
  quantified_all_to,
  quantified_exists_until,
  quantified_exists_to,
  quantified_slice,
  quantified_index_sensitive,
  quantified_nested,
  state_variable,
  state_variable_in,
  boolean_function,
  value_function,
  temporal_once,
  temporal_historically,
  temporal_since,
  temporal_trigger,
}

verus! {
  impl monitor_monitor {
    pub fn handle_r2u2_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      property: R2U2Property,
      verdict: Option<bool>)
      ensures
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
    {
      // BEGIN MARKER R2U2 VERDICT HANDLER
      // Optional implementation placeholder for handling R2U2 verdicts.
      match property {
        R2U2Property::input_event => {
          match verdict {
            Some(true) => log_info("input_event is currently True"),
            Some(false) => log_info("input_event is currently False"),
            None => log_info("input_event is currently unknown"),
          }
        },
        R2U2Property::input_event_data => {
          match verdict {
            Some(true) => log_info("input_event_data is currently True"),
            Some(false) => log_info("input_event_data is currently False"),
            None => log_info("input_event_data is currently unknown"),
          }
        },
        R2U2Property::input_data => {
          match verdict {
            Some(true) => log_info("input_data is currently True"),
            Some(false) => log_info("input_data is currently False"),
            None => log_info("input_data is currently unknown"),
          }
        },
        R2U2Property::base_type_character => {
          match verdict {
            Some(true) => log_info("base_type_character is currently True"),
            Some(false) => log_info("base_type_character is currently False"),
            None => log_info("base_type_character is currently unknown"),
          }
        },
        R2U2Property::base_type_integer_8 => {
          match verdict {
            Some(true) => log_info("base_type_integer_8 is currently True"),
            Some(false) => log_info("base_type_integer_8 is currently False"),
            None => log_info("base_type_integer_8 is currently unknown"),
          }
        },
        R2U2Property::base_type_integer_16 => {
          match verdict {
            Some(true) => log_info("base_type_integer_16 is currently True"),
            Some(false) => log_info("base_type_integer_16 is currently False"),
            None => log_info("base_type_integer_16 is currently unknown"),
          }
        },
        R2U2Property::base_type_integer_32 => {
          match verdict {
            Some(true) => log_info("base_type_integer_32 is currently True"),
            Some(false) => log_info("base_type_integer_32 is currently False"),
            None => log_info("base_type_integer_32 is currently unknown"),
          }
        },
        R2U2Property::base_type_unsigned_8 => {
          match verdict {
            Some(true) => log_info("base_type_unsigned_8 is currently True"),
            Some(false) => log_info("base_type_unsigned_8 is currently False"),
            None => log_info("base_type_unsigned_8 is currently unknown"),
          }
        },
        R2U2Property::base_type_unsigned_16 => {
          match verdict {
            Some(true) => log_info("base_type_unsigned_16 is currently True"),
            Some(false) => log_info("base_type_unsigned_16 is currently False"),
            None => log_info("base_type_unsigned_16 is currently unknown"),
          }
        },
        R2U2Property::bool_signal => {
          match verdict {
            Some(true) => log_info("bool_signal is currently True"),
            Some(false) => log_info("bool_signal is currently False"),
            None => log_info("bool_signal is currently unknown"),
          }
        },
        R2U2Property::output_event_must_send => {
          match verdict {
            Some(true) => log_info("output_event_must_send is currently True"),
            Some(false) => log_info("output_event_must_send is currently False"),
            None => log_info("output_event_must_send is currently unknown"),
          }
        },
        R2U2Property::output_event_data_must_send => {
          match verdict {
            Some(true) => log_info("output_event_data_must_send is currently True"),
            Some(false) => log_info("output_event_data_must_send is currently False"),
            None => log_info("output_event_data_must_send is currently unknown"),
          }
        },
        R2U2Property::output_event_no_send => {
          match verdict {
            Some(true) => log_info("output_event_no_send is currently True"),
            Some(false) => log_info("output_event_no_send is currently False"),
            None => log_info("output_event_no_send is currently unknown"),
          }
        },
        R2U2Property::output_event_data_no_send => {
          match verdict {
            Some(true) => log_info("output_event_data_no_send is currently True"),
            Some(false) => log_info("output_event_data_no_send is currently False"),
            None => log_info("output_event_data_no_send is currently unknown"),
          }
        },
        R2U2Property::output_event_data_must_send_value => {
          match verdict {
            Some(true) => log_info("output_event_data_must_send_value is currently True"),
            Some(false) => log_info("output_event_data_must_send_value is currently False"),
            None => log_info("output_event_data_must_send_value is currently unknown"),
          }
        },
        R2U2Property::output_data => {
          match verdict {
            Some(true) => log_info("output_data is currently True"),
            Some(false) => log_info("output_data is currently False"),
            None => log_info("output_data is currently unknown"),
          }
        },
        R2U2Property::temporal_future => {
          match verdict {
            Some(true) => log_info("temporal_future is currently True"),
            Some(false) => log_info("temporal_future is currently False"),
            None => log_info("temporal_future is currently unknown"),
          }
        },
        R2U2Property::temporal_eventually => {
          match verdict {
            Some(true) => log_info("temporal_eventually is currently True"),
            Some(false) => log_info("temporal_eventually is currently False"),
            None => log_info("temporal_eventually is currently unknown"),
          }
        },
        R2U2Property::temporal_globally => {
          match verdict {
            Some(true) => log_info("temporal_globally is currently True"),
            Some(false) => log_info("temporal_globally is currently False"),
            None => log_info("temporal_globally is currently unknown"),
          }
        },
        R2U2Property::temporal_until => {
          match verdict {
            Some(true) => log_info("temporal_until is currently True"),
            Some(false) => log_info("temporal_until is currently False"),
            None => log_info("temporal_until is currently unknown"),
          }
        },
        R2U2Property::temporal_release => {
          match verdict {
            Some(true) => log_info("temporal_release is currently True"),
            Some(false) => log_info("temporal_release is currently False"),
            None => log_info("temporal_release is currently unknown"),
          }
        },
        R2U2Property::arithmetic_operators => {
          match verdict {
            Some(true) => log_info("arithmetic_operators is currently True"),
            Some(false) => log_info("arithmetic_operators is currently False"),
            None => log_info("arithmetic_operators is currently unknown"),
          }
        },
        R2U2Property::bitwise_operators => {
          match verdict {
            Some(true) => log_info("bitwise_operators is currently True"),
            Some(false) => log_info("bitwise_operators is currently False"),
            None => log_info("bitwise_operators is currently unknown"),
          }
        },
        R2U2Property::boolean_operators => {
          match verdict {
            Some(true) => log_info("boolean_operators is currently True"),
            Some(false) => log_info("boolean_operators is currently False"),
            None => log_info("boolean_operators is currently unknown"),
          }
        },
        R2U2Property::implication_operator => {
          match verdict {
            Some(true) => log_info("implication_operator is currently True"),
            Some(false) => log_info("implication_operator is currently False"),
            None => log_info("implication_operator is currently unknown"),
          }
        },
        R2U2Property::enum_signal => {
          match verdict {
            Some(true) => log_info("enum_signal is currently True"),
            Some(false) => log_info("enum_signal is currently False"),
            None => log_info("enum_signal is currently unknown"),
          }
        },
        R2U2Property::array_signal => {
          match verdict {
            Some(true) => log_info("array_signal is currently True"),
            Some(false) => log_info("array_signal is currently False"),
            None => log_info("array_signal is currently unknown"),
          }
        },
        R2U2Property::record_signal => {
          match verdict {
            Some(true) => log_info("record_signal is currently True"),
            Some(false) => log_info("record_signal is currently False"),
            None => log_info("record_signal is currently unknown"),
          }
        },
        R2U2Property::conditional_boolean => {
          match verdict {
            Some(true) => log_info("conditional_boolean is currently True"),
            Some(false) => log_info("conditional_boolean is currently False"),
            None => log_info("conditional_boolean is currently unknown"),
          }
        },
        R2U2Property::quantified_all_until => {
          match verdict {
            Some(true) => log_info("quantified_all_until is currently True"),
            Some(false) => log_info("quantified_all_until is currently False"),
            None => log_info("quantified_all_until is currently unknown"),
          }
        },
        R2U2Property::quantified_all_to => {
          match verdict {
            Some(true) => log_info("quantified_all_to is currently True"),
            Some(false) => log_info("quantified_all_to is currently False"),
            None => log_info("quantified_all_to is currently unknown"),
          }
        },
        R2U2Property::quantified_exists_until => {
          match verdict {
            Some(true) => log_info("quantified_exists_until is currently True"),
            Some(false) => log_info("quantified_exists_until is currently False"),
            None => log_info("quantified_exists_until is currently unknown"),
          }
        },
        R2U2Property::quantified_exists_to => {
          match verdict {
            Some(true) => log_info("quantified_exists_to is currently True"),
            Some(false) => log_info("quantified_exists_to is currently False"),
            None => log_info("quantified_exists_to is currently unknown"),
          }
        },
        R2U2Property::quantified_slice => {
          match verdict {
            Some(true) => log_info("quantified_slice is currently True"),
            Some(false) => log_info("quantified_slice is currently False"),
            None => log_info("quantified_slice is currently unknown"),
          }
        },
        R2U2Property::quantified_index_sensitive => {
          match verdict {
            Some(true) => log_info("quantified_index_sensitive is currently True"),
            Some(false) => log_info("quantified_index_sensitive is currently False"),
            None => log_info("quantified_index_sensitive is currently unknown"),
          }
        },
        R2U2Property::quantified_nested => {
          match verdict {
            Some(true) => log_info("quantified_nested is currently True"),
            Some(false) => log_info("quantified_nested is currently False"),
            None => log_info("quantified_nested is currently unknown"),
          }
        },
        R2U2Property::state_variable => {
          match verdict {
            Some(true) => log_info("state_variable is currently True"),
            Some(false) => log_info("state_variable is currently False"),
            None => log_info("state_variable is currently unknown"),
          }
        },
        R2U2Property::state_variable_in => {
          match verdict {
            Some(true) => log_info("state_variable_in is currently True"),
            Some(false) => log_info("state_variable_in is currently False"),
            None => log_info("state_variable_in is currently unknown"),
          }
        },
        R2U2Property::boolean_function => {
          match verdict {
            Some(true) => log_info("boolean_function is currently True"),
            Some(false) => log_info("boolean_function is currently False"),
            None => log_info("boolean_function is currently unknown"),
          }
        },
        R2U2Property::value_function => {
          match verdict {
            Some(true) => log_info("value_function is currently True"),
            Some(false) => log_info("value_function is currently False"),
            None => log_info("value_function is currently unknown"),
          }
        },
        R2U2Property::temporal_once => {
          match verdict {
            Some(true) => log_info("temporal_once is currently True"),
            Some(false) => log_info("temporal_once is currently False"),
            None => log_info("temporal_once is currently unknown"),
          }
        },
        R2U2Property::temporal_historically => {
          match verdict {
            Some(true) => log_info("temporal_historically is currently True"),
            Some(false) => log_info("temporal_historically is currently False"),
            None => log_info("temporal_historically is currently unknown"),
          }
        },
        R2U2Property::temporal_since => {
          match verdict {
            Some(true) => log_info("temporal_since is currently True"),
            Some(false) => log_info("temporal_since is currently False"),
            None => log_info("temporal_since is currently unknown"),
          }
        },
        R2U2Property::temporal_trigger => {
          match verdict {
            Some(true) => log_info("temporal_trigger is currently True"),
            Some(false) => log_info("temporal_trigger is currently False"),
            None => log_info("temporal_trigger is currently unknown"),
          }
        },
        _ => {}
      }
      // END MARKER R2U2 VERDICT HANDLER
    }
  }
}

struct R2U2Monitor {
  monitor: r2u2_core::Monitor,
  // Cache latest verdict (if applicable) per C2PO specification between monitor steps.
  verdict_cache: [Option<r2u2_core::r2u2_verdict>; 46],
}

impl R2U2Monitor {
  fn new() -> Self {
    Self {
      monitor: r2u2_core::Monitor::default(),
      verdict_cache: [None; 46],
    }
  }
}

impl monitor_monitor {
  pub fn r2u2_monitor_initialize(&mut self)
  {
    let mut r2u2_monitor = R2U2Monitor::new();
    r2u2_core::update_binary_file(include_bytes!("spec.bin"), &mut r2u2_monitor.monitor);
    unsafe { R2U2_MONITOR = Some(r2u2_monitor); }
  }

  pub fn r2u2_monitor_pre_timeTriggered<API: monitor_monitor_Full_Api> (
    &mut self,
    api: &monitor_monitor_Application_Api<API>)
  {
    let level = api.peek_level();
    let boolean_value = api.peek_boolean_value();
    let character_value = api.peek_character_value();
    let signed_8_value = api.peek_signed_8_value();
    let signed_16_value = api.peek_signed_16_value();
    let unsigned_8_value = api.peek_unsigned_8_value();
    let unsigned_16_value = api.peek_unsigned_16_value();
    let sample = api.peek_sample();
    let flag = api.peek_flag();
    let operating_state = api.peek_operating_state();
    let samples = api.peek_samples();
    let telemetry = api.peek_telemetry();
    let pulse = api.peek_pulse();

    let r2u2_monitor = unsafe {
      R2U2_MONITOR.as_mut().expect("R2U2 monitor used before initialization")
    };

    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 0, pulse); // Loading signal api_pulse_nonEmpty into index 0
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 1, sample.is_some()); // Loading signal api_sample_nonEmpty into index 1
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 2, (sample.unwrap_or_default()) as i32); // Loading signal api_sample into index 2
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 3, (level) as i32); // Loading signal api_level into index 3
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 4, boolean_value); // Loading signal api_boolean_value into index 4
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 5, (character_value) as i32); // Loading signal api_character_value into index 5
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 6, (signed_8_value) as i32); // Loading signal api_signed_8_value into index 6
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 7, (signed_16_value) as i32); // Loading signal api_signed_16_value into index 7
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 8, (unsigned_8_value) as i32); // Loading signal api_unsigned_8_value into index 8
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 9, (unsigned_16_value) as i32); // Loading signal api_unsigned_16_value into index 9
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 10, flag.is_some()); // Loading signal api_flag_nonEmpty into index 10
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 11, flag.unwrap_or_default()); // Loading signal api_flag into index 11
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 18, operating_state.is_some()); // Loading signal api_operating_state_nonEmpty into index 18
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 19, operating_state.unwrap_or_default() as i32); // Loading enum signal api_operating_state into index 19
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 20, samples.is_some()); // Loading signal api_samples_nonEmpty into index 20
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 21, (samples.unwrap_or_default().len()) as i32); // Loading signal api_samples_size into index 21
    r2u2_core::load_int_array(&mut r2u2_monitor.monitor, 22, &(samples.unwrap_or_default()).map(|value| value as i32)); // Loading array signal api_samples into indices 22..25
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 26, telemetry.is_some()); // Loading signal api_telemetry_nonEmpty into index 26
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 27, ((telemetry.unwrap_or_default()).sequence_number) as i32); // Loading signal api_telemetry_sequence_number into index 27
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 28, (telemetry.unwrap_or_default()).valid); // Loading signal api_telemetry_valid into index 28
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 29, (telemetry.unwrap_or_default()).operating_state as i32); // Loading enum signal api_telemetry_operating_state into index 29
    r2u2_core::load_int_array(&mut r2u2_monitor.monitor, 30, &((telemetry.unwrap_or_default()).values).map(|value| value as i32)); // Loading array signal api_telemetry_values into indices 30..33
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 35, (self.sprevious_level) as i32); // Loading signal in_sprevious_level into index 35
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 36, GUMBOX::isNonNegative(level)); // Loading signal fn_isNonNegative_level into index 36
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 37, (GUMBOX::absoluteValue(level)) as i32); // Loading signal fn_absoluteValue_level into index 37
  }

  pub fn r2u2_monitor_post_timeTriggered<API: monitor_monitor_Full_Api> (
    &mut self,
    api: &mut monitor_monitor_Application_Api<API>)
  {
    let healthy = api.peek_healthy();
    let echo = api.peek_echo();
    let ack = api.peek_ack();

    let r2u2_monitor = unsafe {
      R2U2_MONITOR.as_mut().expect("R2U2 monitor used before initialization")
    };

    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 12, ack); // Loading signal api_ack_nonEmpty into index 12
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 13, echo.is_some()); // Loading signal api_echo_nonEmpty into index 13
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 14, !ack); // Loading signal api_ack_isEmpty into index 14
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 15, echo.is_none()); // Loading signal api_echo_isEmpty into index 15
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 16, (echo.unwrap_or_default()) as i32); // Loading signal api_echo into index 16
    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 17, healthy); // Loading signal api_healthy into index 17
    r2u2_core::load_int_signal(&mut r2u2_monitor.monitor, 34, (self.sprevious_level) as i32); // Loading signal sprevious_level into index 34

    r2u2_core::monitor_step(&mut r2u2_monitor.monitor);
    let r2u2_time_stamp = r2u2_monitor.monitor.time_stamp;
    // Expire cached verdicts before applying this step's new outputs.
    for verdict in r2u2_monitor.verdict_cache.iter_mut() {
        if verdict.is_some() && r2u2_time_stamp > verdict.unwrap().time {
            *verdict = None;
        }
    }
    // Keep false verdicts visible when a later output replaces the cache entry.
    let output_buffer = r2u2_core::get_output_buffer(&r2u2_monitor.monitor);
    let verdict_cache = &mut r2u2_monitor.verdict_cache;
    let mut false_verdict_seen = [false; 46];
    for out in output_buffer {
        let spec_num = out.spec_num as usize;
        if !out.verdict.truth {
            false_verdict_seen[spec_num] = true;
        }
        verdict_cache[spec_num] = Some(out.verdict);
    }
    // Delegate each current verdict to the preserved policy handler.
    let r2u2_input_event_verdict = r2u2_monitor.verdict_cache[0]
        .map(|verdict| verdict.truth && !false_verdict_seen[0]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::input_event,
        r2u2_input_event_verdict);
    let r2u2_input_event_data_verdict = r2u2_monitor.verdict_cache[1]
        .map(|verdict| verdict.truth && !false_verdict_seen[1]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::input_event_data,
        r2u2_input_event_data_verdict);
    let r2u2_input_data_verdict = r2u2_monitor.verdict_cache[2]
        .map(|verdict| verdict.truth && !false_verdict_seen[2]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::input_data,
        r2u2_input_data_verdict);
    let r2u2_base_type_boolean_verdict = r2u2_monitor.verdict_cache[3]
        .map(|verdict| verdict.truth && !false_verdict_seen[3]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_boolean,
        r2u2_base_type_boolean_verdict);
    let r2u2_base_type_character_verdict = r2u2_monitor.verdict_cache[4]
        .map(|verdict| verdict.truth && !false_verdict_seen[4]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_character,
        r2u2_base_type_character_verdict);
    let r2u2_base_type_integer_8_verdict = r2u2_monitor.verdict_cache[5]
        .map(|verdict| verdict.truth && !false_verdict_seen[5]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_integer_8,
        r2u2_base_type_integer_8_verdict);
    let r2u2_base_type_integer_16_verdict = r2u2_monitor.verdict_cache[6]
        .map(|verdict| verdict.truth && !false_verdict_seen[6]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_integer_16,
        r2u2_base_type_integer_16_verdict);
    let r2u2_base_type_integer_32_verdict = r2u2_monitor.verdict_cache[7]
        .map(|verdict| verdict.truth && !false_verdict_seen[7]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_integer_32,
        r2u2_base_type_integer_32_verdict);
    let r2u2_base_type_unsigned_8_verdict = r2u2_monitor.verdict_cache[8]
        .map(|verdict| verdict.truth && !false_verdict_seen[8]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_unsigned_8,
        r2u2_base_type_unsigned_8_verdict);
    let r2u2_base_type_unsigned_16_verdict = r2u2_monitor.verdict_cache[9]
        .map(|verdict| verdict.truth && !false_verdict_seen[9]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::base_type_unsigned_16,
        r2u2_base_type_unsigned_16_verdict);
    let r2u2_bool_signal_verdict = r2u2_monitor.verdict_cache[10]
        .map(|verdict| verdict.truth && !false_verdict_seen[10]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::bool_signal,
        r2u2_bool_signal_verdict);
    let r2u2_output_event_must_send_verdict = r2u2_monitor.verdict_cache[11]
        .map(|verdict| verdict.truth && !false_verdict_seen[11]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::output_event_must_send,
        r2u2_output_event_must_send_verdict);
    let r2u2_output_event_data_must_send_verdict = r2u2_monitor.verdict_cache[12]
        .map(|verdict| verdict.truth && !false_verdict_seen[12]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::output_event_data_must_send,
        r2u2_output_event_data_must_send_verdict);
    let r2u2_output_event_no_send_verdict = r2u2_monitor.verdict_cache[13]
        .map(|verdict| verdict.truth && !false_verdict_seen[13]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::output_event_no_send,
        r2u2_output_event_no_send_verdict);
    let r2u2_output_event_data_no_send_verdict = r2u2_monitor.verdict_cache[14]
        .map(|verdict| verdict.truth && !false_verdict_seen[14]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::output_event_data_no_send,
        r2u2_output_event_data_no_send_verdict);
    let r2u2_output_event_data_must_send_value_verdict = r2u2_monitor.verdict_cache[15]
        .map(|verdict| verdict.truth && !false_verdict_seen[15]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::output_event_data_must_send_value,
        r2u2_output_event_data_must_send_value_verdict);
    let r2u2_output_data_verdict = r2u2_monitor.verdict_cache[16]
        .map(|verdict| verdict.truth && !false_verdict_seen[16]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::output_data,
        r2u2_output_data_verdict);
    let r2u2_temporal_future_verdict = r2u2_monitor.verdict_cache[17]
        .map(|verdict| verdict.truth && !false_verdict_seen[17]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_future,
        r2u2_temporal_future_verdict);
    let r2u2_temporal_eventually_verdict = r2u2_monitor.verdict_cache[18]
        .map(|verdict| verdict.truth && !false_verdict_seen[18]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_eventually,
        r2u2_temporal_eventually_verdict);
    let r2u2_temporal_globally_verdict = r2u2_monitor.verdict_cache[19]
        .map(|verdict| verdict.truth && !false_verdict_seen[19]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_globally,
        r2u2_temporal_globally_verdict);
    let r2u2_temporal_until_verdict = r2u2_monitor.verdict_cache[20]
        .map(|verdict| verdict.truth && !false_verdict_seen[20]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_until,
        r2u2_temporal_until_verdict);
    let r2u2_temporal_release_verdict = r2u2_monitor.verdict_cache[21]
        .map(|verdict| verdict.truth && !false_verdict_seen[21]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_release,
        r2u2_temporal_release_verdict);
    let r2u2_comparison_operators_verdict = r2u2_monitor.verdict_cache[22]
        .map(|verdict| verdict.truth && !false_verdict_seen[22]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::comparison_operators,
        r2u2_comparison_operators_verdict);
    let r2u2_arithmetic_operators_verdict = r2u2_monitor.verdict_cache[23]
        .map(|verdict| verdict.truth && !false_verdict_seen[23]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::arithmetic_operators,
        r2u2_arithmetic_operators_verdict);
    let r2u2_bitwise_operators_verdict = r2u2_monitor.verdict_cache[24]
        .map(|verdict| verdict.truth && !false_verdict_seen[24]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::bitwise_operators,
        r2u2_bitwise_operators_verdict);
    let r2u2_boolean_operators_verdict = r2u2_monitor.verdict_cache[25]
        .map(|verdict| verdict.truth && !false_verdict_seen[25]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::boolean_operators,
        r2u2_boolean_operators_verdict);
    let r2u2_implication_operator_verdict = r2u2_monitor.verdict_cache[26]
        .map(|verdict| verdict.truth && !false_verdict_seen[26]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::implication_operator,
        r2u2_implication_operator_verdict);
    let r2u2_enum_signal_verdict = r2u2_monitor.verdict_cache[27]
        .map(|verdict| verdict.truth && !false_verdict_seen[27]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::enum_signal,
        r2u2_enum_signal_verdict);
    let r2u2_array_signal_verdict = r2u2_monitor.verdict_cache[28]
        .map(|verdict| verdict.truth && !false_verdict_seen[28]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::array_signal,
        r2u2_array_signal_verdict);
    let r2u2_record_signal_verdict = r2u2_monitor.verdict_cache[29]
        .map(|verdict| verdict.truth && !false_verdict_seen[29]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::record_signal,
        r2u2_record_signal_verdict);
    let r2u2_conditional_boolean_verdict = r2u2_monitor.verdict_cache[30]
        .map(|verdict| verdict.truth && !false_verdict_seen[30]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::conditional_boolean,
        r2u2_conditional_boolean_verdict);
    let r2u2_quantified_all_until_verdict = r2u2_monitor.verdict_cache[31]
        .map(|verdict| verdict.truth && !false_verdict_seen[31]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_all_until,
        r2u2_quantified_all_until_verdict);
    let r2u2_quantified_all_to_verdict = r2u2_monitor.verdict_cache[32]
        .map(|verdict| verdict.truth && !false_verdict_seen[32]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_all_to,
        r2u2_quantified_all_to_verdict);
    let r2u2_quantified_exists_until_verdict = r2u2_monitor.verdict_cache[33]
        .map(|verdict| verdict.truth && !false_verdict_seen[33]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_exists_until,
        r2u2_quantified_exists_until_verdict);
    let r2u2_quantified_exists_to_verdict = r2u2_monitor.verdict_cache[34]
        .map(|verdict| verdict.truth && !false_verdict_seen[34]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_exists_to,
        r2u2_quantified_exists_to_verdict);
    let r2u2_quantified_slice_verdict = r2u2_monitor.verdict_cache[35]
        .map(|verdict| verdict.truth && !false_verdict_seen[35]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_slice,
        r2u2_quantified_slice_verdict);
    let r2u2_quantified_index_sensitive_verdict = r2u2_monitor.verdict_cache[36]
        .map(|verdict| verdict.truth && !false_verdict_seen[36]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_index_sensitive,
        r2u2_quantified_index_sensitive_verdict);
    let r2u2_quantified_nested_verdict = r2u2_monitor.verdict_cache[37]
        .map(|verdict| verdict.truth && !false_verdict_seen[37]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::quantified_nested,
        r2u2_quantified_nested_verdict);
    let r2u2_state_variable_verdict = r2u2_monitor.verdict_cache[38]
        .map(|verdict| verdict.truth && !false_verdict_seen[38]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::state_variable,
        r2u2_state_variable_verdict);
    let r2u2_state_variable_in_verdict = r2u2_monitor.verdict_cache[39]
        .map(|verdict| verdict.truth && !false_verdict_seen[39]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::state_variable_in,
        r2u2_state_variable_in_verdict);
    let r2u2_boolean_function_verdict = r2u2_monitor.verdict_cache[40]
        .map(|verdict| verdict.truth && !false_verdict_seen[40]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::boolean_function,
        r2u2_boolean_function_verdict);
    let r2u2_value_function_verdict = r2u2_monitor.verdict_cache[41]
        .map(|verdict| verdict.truth && !false_verdict_seen[41]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::value_function,
        r2u2_value_function_verdict);
    let r2u2_temporal_once_verdict = r2u2_monitor.verdict_cache[42]
        .map(|verdict| verdict.truth && !false_verdict_seen[42]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_once,
        r2u2_temporal_once_verdict);
    let r2u2_temporal_historically_verdict = r2u2_monitor.verdict_cache[43]
        .map(|verdict| verdict.truth && !false_verdict_seen[43]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_historically,
        r2u2_temporal_historically_verdict);
    let r2u2_temporal_since_verdict = r2u2_monitor.verdict_cache[44]
        .map(|verdict| verdict.truth && !false_verdict_seen[44]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_since,
        r2u2_temporal_since_verdict);
    let r2u2_temporal_trigger_verdict = r2u2_monitor.verdict_cache[45]
        .map(|verdict| verdict.truth && !false_verdict_seen[45]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::temporal_trigger,
        r2u2_temporal_trigger_verdict);
    // Send one result through each mapped alert port.
    if let Some(truth) = r2u2_base_type_boolean_verdict {
        api.put_alert_result(truth);
    }
    if let Some(truth) = r2u2_comparison_operators_verdict {
        if !truth {
            api.put_ack();
        }
    }
  }
}
