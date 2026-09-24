// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_monitor_api::*;
use vstd::prelude::*;

verus! {

  pub struct monitor_monitor {
    // BEGIN MARKER STATE VARS
    pub sprevious_level: i32,
    // END MARKER STATE VARS
  }

  impl monitor_monitor {
    pub fn new() -> Self
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        sprevious_level: 0,
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: monitor_monitor_Put_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).echo.is_none(),
        old(api).alert_result.is_none(),
        old(api).ack.is_none(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER TIME TRIGGERED ENSURES
    {
      let level = api.get_level();
      let boolean_value = api.get_boolean_value();
      let character_value = api.get_character_value();
      let signed_8_value = api.get_signed_8_value();
      let signed_16_value = api.get_signed_16_value();
      let unsigned_8_value = api.get_unsigned_8_value();
      let unsigned_16_value = api.get_unsigned_16_value();
      let sample = api.get_sample();
      let flag = api.get_flag();
      let operating_state = api.get_operating_state();
      let samples = api.get_samples();
      let telemetry = api.get_telemetry();
      let pulse = api.get_pulse();
      log_info("compute entrypoint invoked");
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel)
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          log_warn_channel(channel)
        }
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `input_event`.
    pub fn handle_input_event_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 input_event VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 input_event VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("input_event is currently true"),
        Some(false) => log_info("input_event is currently false"),
        None => log_info("input_event is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `input_event_data`.
    pub fn handle_input_event_data_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 input_event_data VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 input_event_data VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("input_event_data is currently true"),
        Some(false) => log_info("input_event_data is currently false"),
        None => log_info("input_event_data is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `input_data`.
    pub fn handle_input_data_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 input_data VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 input_data VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("input_data is currently true"),
        Some(false) => log_info("input_data is currently false"),
        None => log_info("input_data is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_boolean`.
    pub fn handle_base_type_boolean_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_boolean VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_boolean VERDICT ENSURES
    {
      // This verdict is already mapped to monitor-owned alert port
      // alert_result. Do not write this port from this callback.
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_character`.
    pub fn handle_base_type_character_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_character VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_character VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("base_type_character is currently true"),
        Some(false) => log_info("base_type_character is currently false"),
        None => log_info("base_type_character is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_integer_8`.
    pub fn handle_base_type_integer_8_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_integer_8 VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_integer_8 VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("base_type_integer_8 is currently true"),
        Some(false) => log_info("base_type_integer_8 is currently false"),
        None => log_info("base_type_integer_8 is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_integer_16`.
    pub fn handle_base_type_integer_16_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_integer_16 VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_integer_16 VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("base_type_integer_16 is currently true"),
        Some(false) => log_info("base_type_integer_16 is currently false"),
        None => log_info("base_type_integer_16 is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_integer_32`.
    pub fn handle_base_type_integer_32_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_integer_32 VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_integer_32 VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("base_type_integer_32 is currently true"),
        Some(false) => log_info("base_type_integer_32 is currently false"),
        None => log_info("base_type_integer_32 is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_unsigned_8`.
    pub fn handle_base_type_unsigned_8_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_unsigned_8 VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_unsigned_8 VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("base_type_unsigned_8 is currently true"),
        Some(false) => log_info("base_type_unsigned_8 is currently false"),
        None => log_info("base_type_unsigned_8 is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `base_type_unsigned_16`.
    pub fn handle_base_type_unsigned_16_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 base_type_unsigned_16 VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 base_type_unsigned_16 VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("base_type_unsigned_16 is currently true"),
        Some(false) => log_info("base_type_unsigned_16 is currently false"),
        None => log_info("base_type_unsigned_16 is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `bool_signal`.
    pub fn handle_bool_signal_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 bool_signal VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 bool_signal VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("bool_signal is currently true"),
        Some(false) => log_info("bool_signal is currently false"),
        None => log_info("bool_signal is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_must_send`.
    pub fn handle_output_event_must_send_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 output_event_must_send VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 output_event_must_send VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("output_event_must_send is currently true"),
        Some(false) => log_info("output_event_must_send is currently false"),
        None => log_info("output_event_must_send is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_data_must_send`.
    pub fn handle_output_event_data_must_send_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 output_event_data_must_send VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 output_event_data_must_send VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("output_event_data_must_send is currently true"),
        Some(false) => log_info("output_event_data_must_send is currently false"),
        None => log_info("output_event_data_must_send is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_no_send`.
    pub fn handle_output_event_no_send_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 output_event_no_send VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 output_event_no_send VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("output_event_no_send is currently true"),
        Some(false) => log_info("output_event_no_send is currently false"),
        None => log_info("output_event_no_send is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_data_no_send`.
    pub fn handle_output_event_data_no_send_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 output_event_data_no_send VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 output_event_data_no_send VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("output_event_data_no_send is currently true"),
        Some(false) => log_info("output_event_data_no_send is currently false"),
        None => log_info("output_event_data_no_send is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `output_event_data_must_send_value`.
    pub fn handle_output_event_data_must_send_value_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 output_event_data_must_send_value VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 output_event_data_must_send_value VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("output_event_data_must_send_value is currently true"),
        Some(false) => log_info("output_event_data_must_send_value is currently false"),
        None => log_info("output_event_data_must_send_value is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `output_data`.
    pub fn handle_output_data_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 output_data VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 output_data VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("output_data is currently true"),
        Some(false) => log_info("output_data is currently false"),
        None => log_info("output_data is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_future`.
    pub fn handle_temporal_future_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_future VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_future VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_future is currently true"),
        Some(false) => log_info("temporal_future is currently false"),
        None => log_info("temporal_future is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_eventually`.
    pub fn handle_temporal_eventually_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_eventually VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_eventually VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_eventually is currently true"),
        Some(false) => log_info("temporal_eventually is currently false"),
        None => log_info("temporal_eventually is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_globally`.
    pub fn handle_temporal_globally_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_globally VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_globally VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_globally is currently true"),
        Some(false) => log_info("temporal_globally is currently false"),
        None => log_info("temporal_globally is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_until`.
    pub fn handle_temporal_until_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_until VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_until VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_until is currently true"),
        Some(false) => log_info("temporal_until is currently false"),
        None => log_info("temporal_until is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_release`.
    pub fn handle_temporal_release_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_release VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_release VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_release is currently true"),
        Some(false) => log_info("temporal_release is currently false"),
        None => log_info("temporal_release is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `comparison_operators`.
    pub fn handle_comparison_operators_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 comparison_operators VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 comparison_operators VERDICT ENSURES
    {
      // This verdict is already mapped to monitor-owned alert port
      // ack. Do not write this port from this callback.
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `arithmetic_operators`.
    pub fn handle_arithmetic_operators_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 arithmetic_operators VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 arithmetic_operators VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("arithmetic_operators is currently true"),
        Some(false) => log_info("arithmetic_operators is currently false"),
        None => log_info("arithmetic_operators is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `bitwise_operators`.
    pub fn handle_bitwise_operators_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 bitwise_operators VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 bitwise_operators VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("bitwise_operators is currently true"),
        Some(false) => log_info("bitwise_operators is currently false"),
        None => log_info("bitwise_operators is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `boolean_operators`.
    pub fn handle_boolean_operators_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 boolean_operators VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 boolean_operators VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("boolean_operators is currently true"),
        Some(false) => log_info("boolean_operators is currently false"),
        None => log_info("boolean_operators is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `implication_operator`.
    pub fn handle_implication_operator_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 implication_operator VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 implication_operator VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("implication_operator is currently true"),
        Some(false) => log_info("implication_operator is currently false"),
        None => log_info("implication_operator is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `enum_signal`.
    pub fn handle_enum_signal_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 enum_signal VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 enum_signal VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("enum_signal is currently true"),
        Some(false) => log_info("enum_signal is currently false"),
        None => log_info("enum_signal is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `array_signal`.
    pub fn handle_array_signal_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 array_signal VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 array_signal VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("array_signal is currently true"),
        Some(false) => log_info("array_signal is currently false"),
        None => log_info("array_signal is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `record_signal`.
    pub fn handle_record_signal_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 record_signal VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 record_signal VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("record_signal is currently true"),
        Some(false) => log_info("record_signal is currently false"),
        None => log_info("record_signal is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `conditional_boolean`.
    pub fn handle_conditional_boolean_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 conditional_boolean VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 conditional_boolean VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("conditional_boolean is currently true"),
        Some(false) => log_info("conditional_boolean is currently false"),
        None => log_info("conditional_boolean is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_all_until`.
    pub fn handle_quantified_all_until_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_all_until VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_all_until VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_all_until is currently true"),
        Some(false) => log_info("quantified_all_until is currently false"),
        None => log_info("quantified_all_until is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_all_to`.
    pub fn handle_quantified_all_to_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_all_to VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_all_to VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_all_to is currently true"),
        Some(false) => log_info("quantified_all_to is currently false"),
        None => log_info("quantified_all_to is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_exists_until`.
    pub fn handle_quantified_exists_until_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_exists_until VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_exists_until VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_exists_until is currently true"),
        Some(false) => log_info("quantified_exists_until is currently false"),
        None => log_info("quantified_exists_until is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_exists_to`.
    pub fn handle_quantified_exists_to_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_exists_to VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_exists_to VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_exists_to is currently true"),
        Some(false) => log_info("quantified_exists_to is currently false"),
        None => log_info("quantified_exists_to is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_slice`.
    pub fn handle_quantified_slice_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_slice VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_slice VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_slice is currently true"),
        Some(false) => log_info("quantified_slice is currently false"),
        None => log_info("quantified_slice is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_index_sensitive`.
    pub fn handle_quantified_index_sensitive_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_index_sensitive VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_index_sensitive VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_index_sensitive is currently true"),
        Some(false) => log_info("quantified_index_sensitive is currently false"),
        None => log_info("quantified_index_sensitive is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `quantified_nested`.
    pub fn handle_quantified_nested_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 quantified_nested VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 quantified_nested VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("quantified_nested is currently true"),
        Some(false) => log_info("quantified_nested is currently false"),
        None => log_info("quantified_nested is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `state_variable`.
    pub fn handle_state_variable_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 state_variable VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 state_variable VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("state_variable is currently true"),
        Some(false) => log_info("state_variable is currently false"),
        None => log_info("state_variable is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `state_variable_in`.
    pub fn handle_state_variable_in_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 state_variable_in VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 state_variable_in VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("state_variable_in is currently true"),
        Some(false) => log_info("state_variable_in is currently false"),
        None => log_info("state_variable_in is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `boolean_function`.
    pub fn handle_boolean_function_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 boolean_function VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 boolean_function VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("boolean_function is currently true"),
        Some(false) => log_info("boolean_function is currently false"),
        None => log_info("boolean_function is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `value_function`.
    pub fn handle_value_function_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 value_function VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 value_function VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("value_function is currently true"),
        Some(false) => log_info("value_function is currently false"),
        None => log_info("value_function is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_once`.
    pub fn handle_temporal_once_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_once VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_once VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_once is currently true"),
        Some(false) => log_info("temporal_once is currently false"),
        None => log_info("temporal_once is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_historically`.
    pub fn handle_temporal_historically_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_historically VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_historically VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_historically is currently true"),
        Some(false) => log_info("temporal_historically is currently false"),
        None => log_info("temporal_historically is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_since`.
    pub fn handle_temporal_since_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_since VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_since VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_since is currently true"),
        Some(false) => log_info("temporal_since is currently false"),
        None => log_info("temporal_since is currently unknown"),
      }
    }

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `temporal_trigger`.
    pub fn handle_temporal_trigger_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 temporal_trigger VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).ack == final(api).ack,
        old(api).alert_result == final(api).alert_result,
        // END MARKER R2U2 temporal_trigger VERDICT ENSURES
    {
      match verdict {
        Some(true) => log_info("temporal_trigger is currently true"),
        Some(false) => log_info("temporal_trigger is currently false"),
        None => log_info("temporal_trigger is currently unknown"),
      }
    }
  }

  #[verifier::external_body]
  pub fn log_info(msg: &str)
  {
    log::info!("{0}", msg);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32)
  {
    log::warn!("Unexpected channel: {0}", channel);
  }

  // BEGIN MARKER GUMBO METHODS
  pub open spec fn isNonNegative(value: i32) -> bool
  {
    value >= 0i32
  }

  pub open spec fn absoluteValue(value: i32) -> i32
  {
    if (value < 0i32) {
      (-value) as i32
    } else {
      value
    }
  }
  // END MARKER GUMBO METHODS

}
