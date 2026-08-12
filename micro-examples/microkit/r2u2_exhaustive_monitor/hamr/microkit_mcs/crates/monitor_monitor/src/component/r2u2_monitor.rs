use crate::bridge::monitor_monitor_api::*;
use super::monitor_monitor_app::monitor_monitor;
use vstd::prelude::*;

verus! {

  impl monitor_monitor {
    // BEGIN MARKER R2U2 MONITOR PRE TIME TRIGGERED
    #[verifier::external_body]
    pub fn pre_timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &monitor_monitor_Application_Api<API>)
    {
      // HAMR-generated R2U2 hook. Do not edit; changes are replaced during regeneration.
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
      let pulse = api.peek_pulse();

      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 0, pulse); // Loading signal api_pulse_nonEmpty into index 0
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 1, sample.is_some()); // Loading signal api_sample_nonEmpty into index 1
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 2, sample.unwrap_or_default().into()); // Loading signal api_sample into index 2
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 3, level.into()); // Loading signal api_level into index 3
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 4, boolean_value); // Loading signal api_boolean_value into index 4
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 5, character_value.into()); // Loading signal api_character_value into index 5
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 6, signed_8_value.into()); // Loading signal api_signed_8_value into index 6
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 7, signed_16_value.into()); // Loading signal api_signed_16_value into index 7
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 8, unsigned_8_value.into()); // Loading signal api_unsigned_8_value into index 8
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 9, unsigned_16_value.into()); // Loading signal api_unsigned_16_value into index 9
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 10, flag.is_some()); // Loading signal api_flag_nonEmpty into index 10
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 11, flag.unwrap_or_default()); // Loading signal api_flag into index 11
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 18, operating_state.is_some()); // Loading signal api_operating_state_nonEmpty into index 18
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 19, operating_state.unwrap_or_default() as i32); // Loading enum signal api_operating_state into index 19
    }
    // END MARKER R2U2 MONITOR PRE TIME TRIGGERED

    // BEGIN MARKER R2U2 MONITOR POST TIME TRIGGERED
    #[verifier::external_body]
    pub fn post_timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
    {
      // HAMR-generated R2U2 hook. Do not edit; changes are replaced during regeneration.
      let healthy = api.peek_healthy();
      let echo = api.peek_echo();
      let ack = api.peek_ack();

      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 12, ack); // Loading signal api_ack_nonEmpty into index 12
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 13, echo.is_some()); // Loading signal api_echo_nonEmpty into index 13
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 14, !ack); // Loading signal api_ack_isEmpty into index 14
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 15, echo.is_none()); // Loading signal api_echo_isEmpty into index 15
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 16, echo.unwrap_or_default().into()); // Loading signal api_echo into index 16
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 17, healthy); // Loading signal api_healthy into index 17

      r2u2_core::monitor_step(&mut self.r2u2_monitor);
      for out in r2u2_core::get_output_buffer(&self.r2u2_monitor) {
          log::info!("{}:{},{}", out.spec_num, out.verdict.time, if out.verdict.truth {"T"} else {"F"} );
      }
    }
    // END MARKER R2U2 MONITOR POST TIME TRIGGERED
  }

  // BEGIN MARKER R2U2 SPEC
  #[verifier::external_body]
  pub struct R2U2Monitor { inner: r2u2_core::Monitor }

  #[verifier::external]
  impl core::ops::Deref for R2U2Monitor {
    type Target = r2u2_core::Monitor;
    fn deref(&self) -> &Self::Target { &self.inner }
  }

  #[verifier::external]
  impl core::ops::DerefMut for R2U2Monitor {
    fn deref_mut(&mut self) -> &mut Self::Target { &mut self.inner }
  }

  #[verifier::external_body]
  pub(super) fn default_r2u2_monitor() -> R2U2Monitor {
    R2U2Monitor { inner: r2u2_core::Monitor::default() }
  }

  #[verifier::external_body]
  pub(super) fn load_spec(monitor: &mut R2U2Monitor) {
    r2u2_core::update_binary_file(include_bytes!("spec.bin"), monitor);
  }
  // END MARKER R2U2 SPEC

}
