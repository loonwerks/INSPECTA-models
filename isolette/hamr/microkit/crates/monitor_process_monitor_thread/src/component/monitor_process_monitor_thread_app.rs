// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_process_monitor_thread_api::*;
use vstd::prelude::*;
use crate::gumbox::thermostat_rt_mri_mri_containers::*;
use crate::gumbox::thermostat_rt_mhs_mhs_containers::*;
use crate::gumbox::thermostat_rt_mrm_mrm_containers::*;
use crate::gumbox::thermostat_rt_drf_drf_containers::*;
use crate::gumbox::thermostat_mt_mmi_mmi_containers::*;
use crate::gumbox::thermostat_mt_ma_ma_containers::*;
use crate::gumbox::thermostat_mt_mmm_mmm_containers::*;
use crate::gumbox::thermostat_mt_dmf_dmf_containers::*;
use crate::gumbox::operator_interface_oip_oit_containers::*;

verus! {

  pub struct monitor_process_monitor_thread {
    // PLACEHOLDER MARKER STATE VARS
  }

  impl monitor_process_monitor_thread {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: monitor_process_monitor_thread_Put_Api> (
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: monitor_process_monitor_thread_Full_Api> (
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      //log_info("compute entrypoint invoked");
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

  // PLACEHOLDER MARKER GUMBO METHODS

}
