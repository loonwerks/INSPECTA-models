// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::gumbo_monitor_process_gumbo_monitor_thread_api::*;
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

#[verus_verify]
pub struct gumbo_monitor_process_gumbo_monitor_thread {
  // PLACEHOLDER MARKER STATE VARS,
  frame_period: i32,
  last_index: u32,
  prev_user_ch: hamr::ScheduleChannels,
  next_user_ch: hamr::ScheduleChannels,
  pre_thermostat_rt_mri_mri: Option<PreState_thermostat_rt_mri_mri>,
  pre_thermostat_rt_mhs_mhs: Option<PreState_thermostat_rt_mhs_mhs>,
  pre_thermostat_rt_mrm_mrm: Option<PreState_thermostat_rt_mrm_mrm>,
  pre_thermostat_mt_mmi_mmi: Option<PreState_thermostat_mt_mmi_mmi>,
  pre_thermostat_mt_ma_ma: Option<PreState_thermostat_mt_ma_ma>,
  pre_thermostat_mt_mmm_mmm: Option<PreState_thermostat_mt_mmm_mmm>,
  pre_operator_interface_oip_oit: Option<PreState_operator_interface_oip_oit>
}

#[verus_verify]
impl gumbo_monitor_process_gumbo_monitor_thread {
  pub fn new() -> Self
  {
    Self {
      frame_period: 0,
      last_index: u32::MAX,
      prev_user_ch: [0; hamr::hamr_ScheduleChannels_DIM_0],
      next_user_ch: [0; hamr::hamr_ScheduleChannels_DIM_0],
      pre_thermostat_rt_mri_mri: None,
      pre_thermostat_rt_mhs_mhs: None,
      pre_thermostat_rt_mrm_mrm: None,
      pre_thermostat_mt_mmi_mmi: None,
      pre_thermostat_mt_ma_ma: None,
      pre_thermostat_mt_mmm_mmm: None,
      pre_operator_interface_oip_oit: None,
    }
  }

  #[verus_spec(
    ensures
      // PLACEHOLDER MARKER INITIALIZATION ENSURES
  )]
  pub fn initialize<API: gumbo_monitor_process_gumbo_monitor_thread_Put_Api> (
    &mut self,
    api: &mut gumbo_monitor_process_gumbo_monitor_thread_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");
  }

  #[verus_spec(
    requires
      // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
    ensures
      // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
  )]
  pub fn timeTriggered<API: gumbo_monitor_process_gumbo_monitor_thread_Full_Api> (
    &mut self,
    api: &mut gumbo_monitor_process_gumbo_monitor_thread_Application_Api<API>)
  {
    self.monitor(api);
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

  #[verus_verify(external_body)]
  pub fn monitor<API: gumbo_monitor_process_gumbo_monitor_thread_Full_Api>(
    &mut self,
    api: &mut gumbo_monitor_process_gumbo_monitor_thread_Application_Api<API>)
  {
    let state = api.get_sched_state();

    if self.last_index == u32::MAX {
      let schedule = api.get_sched_schedule();
      buildUserChannelTables(
        &schedule, &mut self.prev_user_ch, &mut self.next_user_ch);
    }

    // Detect schedule wraparound: if the current timeslice index is not
    // greater than the last one seen, the schedule has started a new frame
    if state.current_timeslice <= self.last_index {
      self.frame_period = self.frame_period + 1;
    }

    let idx = state.current_timeslice as usize;

    if self.last_index == u32::MAX {
      // First compute phase, check initialization guarantees
      {
        let post_thermostat_rt_mri_mri = PostState_thermostat_rt_mri_mri {
          api_displayed_temp: api.get_thermostat_rt_mri_mri_displayed_temp(),
          api_interface_failure: api.get_thermostat_rt_mri_mri_interface_failure(),
          api_lower_desired_temp: api.get_thermostat_rt_mri_mri_lower_desired_temp(),
          api_regulator_status: api.get_thermostat_rt_mri_mri_regulator_status(),
          api_upper_desired_temp: api.get_thermostat_rt_mri_mri_upper_desired_temp(),
        };
        if !crate::gumbox::thermostat_rt_mri_mri_GUMBOX::initialize_IEP_Post(
          post_thermostat_rt_mri_mri.api_displayed_temp, post_thermostat_rt_mri_mri.api_interface_failure, post_thermostat_rt_mri_mri.api_lower_desired_temp, post_thermostat_rt_mri_mri.api_regulator_status, post_thermostat_rt_mri_mri.api_upper_desired_temp) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mri_mri IEP_Post not satisfied ***");
          log::warn!("thermostat_rt_mri_mri post: {:?}", post_thermostat_rt_mri_mri);
        }
      }
      {
        let post_thermostat_rt_mhs_mhs = PostState_thermostat_rt_mhs_mhs {
          lastCmd: api.get_thermostat_rt_mhs_mhs_sv_lastCmd(),
          api_heat_control: api.get_thermostat_rt_mhs_mhs_heat_control(),
        };
        if !crate::gumbox::thermostat_rt_mhs_mhs_GUMBOX::initialize_IEP_Post(
          post_thermostat_rt_mhs_mhs.lastCmd, post_thermostat_rt_mhs_mhs.api_heat_control) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mhs_mhs IEP_Post not satisfied ***");
          log::warn!("thermostat_rt_mhs_mhs post: {:?}", post_thermostat_rt_mhs_mhs);
        }
      }
      {
        let post_thermostat_rt_mrm_mrm = PostState_thermostat_rt_mrm_mrm {
          lastRegulatorMode: api.get_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(),
          api_regulator_mode: api.get_thermostat_rt_mrm_mrm_regulator_mode(),
        };
        if !crate::gumbox::thermostat_rt_mrm_mrm_GUMBOX::initialize_IEP_Post(
          post_thermostat_rt_mrm_mrm.lastRegulatorMode, post_thermostat_rt_mrm_mrm.api_regulator_mode) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mrm_mrm IEP_Post not satisfied ***");
          log::warn!("thermostat_rt_mrm_mrm post: {:?}", post_thermostat_rt_mrm_mrm);
        }
      }
      {
        let post_thermostat_mt_mmi_mmi = PostState_thermostat_mt_mmi_mmi {
          lastCmd: api.get_thermostat_mt_mmi_mmi_sv_lastCmd(),
          api_interface_failure: api.get_thermostat_mt_mmi_mmi_interface_failure(),
          api_lower_alarm_temp: api.get_thermostat_mt_mmi_mmi_lower_alarm_temp(),
          api_monitor_status: api.get_thermostat_mt_mmi_mmi_monitor_status(),
          api_upper_alarm_temp: api.get_thermostat_mt_mmi_mmi_upper_alarm_temp(),
        };
        if !crate::gumbox::thermostat_mt_mmi_mmi_GUMBOX::initialize_IEP_Post(
          post_thermostat_mt_mmi_mmi.lastCmd, post_thermostat_mt_mmi_mmi.api_interface_failure, post_thermostat_mt_mmi_mmi.api_lower_alarm_temp, post_thermostat_mt_mmi_mmi.api_monitor_status, post_thermostat_mt_mmi_mmi.api_upper_alarm_temp) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_mt_mmi_mmi IEP_Post not satisfied ***");
          log::warn!("thermostat_mt_mmi_mmi post: {:?}", post_thermostat_mt_mmi_mmi);
        }
      }
      {
        let post_thermostat_mt_ma_ma = PostState_thermostat_mt_ma_ma {
          lastCmd: api.get_thermostat_mt_ma_ma_sv_lastCmd(),
          api_alarm_control: api.get_thermostat_mt_ma_ma_alarm_control(),
        };
        if !crate::gumbox::thermostat_mt_ma_ma_GUMBOX::initialize_IEP_Post(
          post_thermostat_mt_ma_ma.lastCmd, post_thermostat_mt_ma_ma.api_alarm_control) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_mt_ma_ma IEP_Post not satisfied ***");
          log::warn!("thermostat_mt_ma_ma post: {:?}", post_thermostat_mt_ma_ma);
        }
      }
      {
        let post_thermostat_mt_mmm_mmm = PostState_thermostat_mt_mmm_mmm {
          lastMonitorMode: api.get_thermostat_mt_mmm_mmm_sv_lastMonitorMode(),
          api_monitor_mode: api.get_thermostat_mt_mmm_mmm_monitor_mode(),
        };
        if !crate::gumbox::thermostat_mt_mmm_mmm_GUMBOX::initialize_IEP_Post(
          post_thermostat_mt_mmm_mmm.lastMonitorMode, post_thermostat_mt_mmm_mmm.api_monitor_mode) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_mt_mmm_mmm IEP_Post not satisfied ***");
          log::warn!("thermostat_mt_mmm_mmm post: {:?}", post_thermostat_mt_mmm_mmm);
        }
      }
      {
        let post_operator_interface_oip_oit = PostState_operator_interface_oip_oit {
          api_lower_alarm_tempWstatus: api.get_operator_interface_oip_oit_lower_alarm_tempWstatus(),
          api_lower_desired_tempWstatus: api.get_operator_interface_oip_oit_lower_desired_tempWstatus(),
          api_upper_alarm_tempWstatus: api.get_operator_interface_oip_oit_upper_alarm_tempWstatus(),
          api_upper_desired_tempWstatus: api.get_operator_interface_oip_oit_upper_desired_tempWstatus(),
        };
        if !crate::gumbox::operator_interface_oip_oit_GUMBOX::initialize_IEP_Post(
          post_operator_interface_oip_oit.api_lower_alarm_tempWstatus, post_operator_interface_oip_oit.api_lower_desired_tempWstatus, post_operator_interface_oip_oit.api_upper_alarm_tempWstatus, post_operator_interface_oip_oit.api_upper_desired_tempWstatus) {
          log::warn!("*** CONTRACT VIOLATION: operator_interface_oip_oit IEP_Post not satisfied ***");
          log::warn!("operator_interface_oip_oit post: {:?}", post_operator_interface_oip_oit);
        }
      }
    } else {
      let prev_ch = self.prev_user_ch[idx];
      match prev_ch {
        thermostat_rt_mri_mri_MON => {
          let post = PostState_thermostat_rt_mri_mri {
            api_displayed_temp: api.get_thermostat_rt_mri_mri_displayed_temp(),
            api_interface_failure: api.get_thermostat_rt_mri_mri_interface_failure(),
            api_lower_desired_temp: api.get_thermostat_rt_mri_mri_lower_desired_temp(),
            api_regulator_status: api.get_thermostat_rt_mri_mri_regulator_status(),
            api_upper_desired_temp: api.get_thermostat_rt_mri_mri_upper_desired_temp(),
          };
          if let Some(pre) = &self.pre_thermostat_rt_mri_mri {
            if !crate::gumbox::thermostat_rt_mri_mri_GUMBOX::compute_CEP_Post(
              pre.api_current_tempWstatus, pre.api_lower_desired_tempWstatus, pre.api_regulator_mode, pre.api_upper_desired_tempWstatus, post.api_displayed_temp, post.api_interface_failure, post.api_lower_desired_temp, post.api_regulator_status, post.api_upper_desired_temp) {
              log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mri_mri CEP_Post not satisfied ***");
              log::warn!("thermostat_rt_mri_mri pre: {:?}", pre);
              log::warn!("thermostat_rt_mri_mri post: {:?}", post);
            }
          } else {
            log::warn!("thermostat_rt_mri_mri post check skipped: no saved pre-state");
          }
        }
        thermostat_rt_mhs_mhs_MON => {
          let post = PostState_thermostat_rt_mhs_mhs {
            lastCmd: api.get_thermostat_rt_mhs_mhs_sv_lastCmd(),
            api_heat_control: api.get_thermostat_rt_mhs_mhs_heat_control(),
          };
          if let Some(pre) = &self.pre_thermostat_rt_mhs_mhs {
            if !crate::gumbox::thermostat_rt_mhs_mhs_GUMBOX::compute_CEP_Post(
              pre.In_lastCmd, post.lastCmd, pre.api_current_tempWstatus, pre.api_lower_desired_temp, pre.api_regulator_mode, pre.api_upper_desired_temp, post.api_heat_control) {
              log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mhs_mhs CEP_Post not satisfied ***");
              log::warn!("thermostat_rt_mhs_mhs pre: {:?}", pre);
              log::warn!("thermostat_rt_mhs_mhs post: {:?}", post);
            }
          } else {
            log::warn!("thermostat_rt_mhs_mhs post check skipped: no saved pre-state");
          }
        }
        thermostat_rt_mrm_mrm_MON => {
          let post = PostState_thermostat_rt_mrm_mrm {
            lastRegulatorMode: api.get_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(),
            api_regulator_mode: api.get_thermostat_rt_mrm_mrm_regulator_mode(),
          };
          if let Some(pre) = &self.pre_thermostat_rt_mrm_mrm {
            if !crate::gumbox::thermostat_rt_mrm_mrm_GUMBOX::compute_CEP_Post(
              pre.In_lastRegulatorMode, post.lastRegulatorMode, pre.api_current_tempWstatus, pre.api_interface_failure, pre.api_internal_failure, post.api_regulator_mode) {
              log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mrm_mrm CEP_Post not satisfied ***");
              log::warn!("thermostat_rt_mrm_mrm pre: {:?}", pre);
              log::warn!("thermostat_rt_mrm_mrm post: {:?}", post);
            }
          } else {
            log::warn!("thermostat_rt_mrm_mrm post check skipped: no saved pre-state");
          }
        }
        thermostat_mt_mmi_mmi_MON => {
          let post = PostState_thermostat_mt_mmi_mmi {
            lastCmd: api.get_thermostat_mt_mmi_mmi_sv_lastCmd(),
            api_interface_failure: api.get_thermostat_mt_mmi_mmi_interface_failure(),
            api_lower_alarm_temp: api.get_thermostat_mt_mmi_mmi_lower_alarm_temp(),
            api_monitor_status: api.get_thermostat_mt_mmi_mmi_monitor_status(),
            api_upper_alarm_temp: api.get_thermostat_mt_mmi_mmi_upper_alarm_temp(),
          };
          if let Some(pre) = &self.pre_thermostat_mt_mmi_mmi {
            if !crate::gumbox::thermostat_mt_mmi_mmi_GUMBOX::compute_CEP_Post(
              pre.In_lastCmd, post.lastCmd, pre.api_current_tempWstatus, pre.api_lower_alarm_tempWstatus, pre.api_monitor_mode, pre.api_upper_alarm_tempWstatus, post.api_interface_failure, post.api_lower_alarm_temp, post.api_monitor_status, post.api_upper_alarm_temp) {
              log::warn!("*** CONTRACT VIOLATION: thermostat_mt_mmi_mmi CEP_Post not satisfied ***");
              log::warn!("thermostat_mt_mmi_mmi pre: {:?}", pre);
              log::warn!("thermostat_mt_mmi_mmi post: {:?}", post);
            }
          } else {
            log::warn!("thermostat_mt_mmi_mmi post check skipped: no saved pre-state");
          }
        }
        thermostat_mt_ma_ma_MON => {
          let post = PostState_thermostat_mt_ma_ma {
            lastCmd: api.get_thermostat_mt_ma_ma_sv_lastCmd(),
            api_alarm_control: api.get_thermostat_mt_ma_ma_alarm_control(),
          };
          if let Some(pre) = &self.pre_thermostat_mt_ma_ma {
            if !crate::gumbox::thermostat_mt_ma_ma_GUMBOX::compute_CEP_Post(
              pre.In_lastCmd, post.lastCmd, pre.api_current_tempWstatus, pre.api_lower_alarm_temp, pre.api_monitor_mode, pre.api_upper_alarm_temp, post.api_alarm_control) {
              log::warn!("*** CONTRACT VIOLATION: thermostat_mt_ma_ma CEP_Post not satisfied ***");
              log::warn!("thermostat_mt_ma_ma pre: {:?}", pre);
              log::warn!("thermostat_mt_ma_ma post: {:?}", post);
            }
          } else {
            log::warn!("thermostat_mt_ma_ma post check skipped: no saved pre-state");
          }
        }
        thermostat_mt_mmm_mmm_MON => {
          let post = PostState_thermostat_mt_mmm_mmm {
            lastMonitorMode: api.get_thermostat_mt_mmm_mmm_sv_lastMonitorMode(),
            api_monitor_mode: api.get_thermostat_mt_mmm_mmm_monitor_mode(),
          };
          if let Some(pre) = &self.pre_thermostat_mt_mmm_mmm {
            if !crate::gumbox::thermostat_mt_mmm_mmm_GUMBOX::compute_CEP_Post(
              pre.In_lastMonitorMode, post.lastMonitorMode, pre.api_current_tempWstatus, pre.api_interface_failure, pre.api_internal_failure, post.api_monitor_mode) {
              log::warn!("*** CONTRACT VIOLATION: thermostat_mt_mmm_mmm CEP_Post not satisfied ***");
              log::warn!("thermostat_mt_mmm_mmm pre: {:?}", pre);
              log::warn!("thermostat_mt_mmm_mmm post: {:?}", post);
            }
          } else {
            log::warn!("thermostat_mt_mmm_mmm post check skipped: no saved pre-state");
          }
        }
        operator_interface_oip_oit_MON => {
          let post = PostState_operator_interface_oip_oit {
            api_lower_alarm_tempWstatus: api.get_operator_interface_oip_oit_lower_alarm_tempWstatus(),
            api_lower_desired_tempWstatus: api.get_operator_interface_oip_oit_lower_desired_tempWstatus(),
            api_upper_alarm_tempWstatus: api.get_operator_interface_oip_oit_upper_alarm_tempWstatus(),
            api_upper_desired_tempWstatus: api.get_operator_interface_oip_oit_upper_desired_tempWstatus(),
          };
          if let Some(pre) = &self.pre_operator_interface_oip_oit {
            if !crate::gumbox::operator_interface_oip_oit_GUMBOX::compute_CEP_Post(
              pre.api_alarm_control, pre.api_display_temperature, pre.api_monitor_status, pre.api_regulator_status, post.api_lower_alarm_tempWstatus, post.api_lower_desired_tempWstatus, post.api_upper_alarm_tempWstatus, post.api_upper_desired_tempWstatus) {
              log::warn!("*** CONTRACT VIOLATION: operator_interface_oip_oit CEP_Post not satisfied ***");
              log::warn!("operator_interface_oip_oit pre: {:?}", pre);
              log::warn!("operator_interface_oip_oit post: {:?}", post);
            }
          } else {
            log::warn!("operator_interface_oip_oit post check skipped: no saved pre-state");
          }
        }
        _ => {}
      }
    }

    let next_ch = self.next_user_ch[idx];
    match next_ch {
      thermostat_rt_mri_mri_MON => {
        let pre = PreState_thermostat_rt_mri_mri {
          api_current_tempWstatus: api.get_temperature_sensor_cpi_thermostat_current_tempWstatus(),
          api_lower_desired_tempWstatus: api.get_operator_interface_oip_oit_lower_desired_tempWstatus(),
          api_regulator_mode: api.get_thermostat_rt_mrm_mrm_regulator_mode(),
          api_upper_desired_tempWstatus: api.get_operator_interface_oip_oit_upper_desired_tempWstatus(),
        };
        if !crate::gumbox::thermostat_rt_mri_mri_GUMBOX::compute_CEP_Pre(
          pre.api_current_tempWstatus, pre.api_lower_desired_tempWstatus, pre.api_regulator_mode, pre.api_upper_desired_tempWstatus) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mri_mri CEP_Pre not satisfied ***");
          log::warn!("thermostat_rt_mri_mri pre: {:?}", pre);
        }
        self.pre_thermostat_rt_mri_mri = Some(pre);
      }
      thermostat_rt_mhs_mhs_MON => {
        let pre = PreState_thermostat_rt_mhs_mhs {
          In_lastCmd: api.get_thermostat_rt_mhs_mhs_sv_lastCmd(),
          api_current_tempWstatus: api.get_temperature_sensor_cpi_thermostat_current_tempWstatus(),
          api_lower_desired_temp: api.get_thermostat_rt_mri_mri_lower_desired_temp(),
          api_regulator_mode: api.get_thermostat_rt_mrm_mrm_regulator_mode(),
          api_upper_desired_temp: api.get_thermostat_rt_mri_mri_upper_desired_temp(),
        };
        if !crate::gumbox::thermostat_rt_mhs_mhs_GUMBOX::compute_CEP_Pre(
          pre.In_lastCmd, pre.api_current_tempWstatus, pre.api_lower_desired_temp, pre.api_regulator_mode, pre.api_upper_desired_temp) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_rt_mhs_mhs CEP_Pre not satisfied ***");
          log::warn!("thermostat_rt_mhs_mhs pre: {:?}", pre);
        }
        self.pre_thermostat_rt_mhs_mhs = Some(pre);
      }
      thermostat_rt_mrm_mrm_MON => {
        let pre = PreState_thermostat_rt_mrm_mrm {
          In_lastRegulatorMode: api.get_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(),
          api_current_tempWstatus: api.get_temperature_sensor_cpi_thermostat_current_tempWstatus(),
          api_interface_failure: api.get_thermostat_rt_mri_mri_interface_failure(),
          api_internal_failure: api.get_thermostat_rt_drf_drf_internal_failure(),
        };
        self.pre_thermostat_rt_mrm_mrm = Some(pre);
      }
      thermostat_mt_mmi_mmi_MON => {
        let pre = PreState_thermostat_mt_mmi_mmi {
          In_lastCmd: api.get_thermostat_mt_mmi_mmi_sv_lastCmd(),
          api_current_tempWstatus: api.get_temperature_sensor_cpi_thermostat_current_tempWstatus(),
          api_lower_alarm_tempWstatus: api.get_operator_interface_oip_oit_lower_alarm_tempWstatus(),
          api_monitor_mode: api.get_thermostat_mt_mmm_mmm_monitor_mode(),
          api_upper_alarm_tempWstatus: api.get_operator_interface_oip_oit_upper_alarm_tempWstatus(),
        };
        if !crate::gumbox::thermostat_mt_mmi_mmi_GUMBOX::compute_CEP_Pre(
          pre.In_lastCmd, pre.api_current_tempWstatus, pre.api_lower_alarm_tempWstatus, pre.api_monitor_mode, pre.api_upper_alarm_tempWstatus) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_mt_mmi_mmi CEP_Pre not satisfied ***");
          log::warn!("thermostat_mt_mmi_mmi pre: {:?}", pre);
        }
        self.pre_thermostat_mt_mmi_mmi = Some(pre);
      }
      thermostat_mt_ma_ma_MON => {
        let pre = PreState_thermostat_mt_ma_ma {
          In_lastCmd: api.get_thermostat_mt_ma_ma_sv_lastCmd(),
          api_current_tempWstatus: api.get_temperature_sensor_cpi_thermostat_current_tempWstatus(),
          api_lower_alarm_temp: api.get_thermostat_mt_mmi_mmi_lower_alarm_temp(),
          api_monitor_mode: api.get_thermostat_mt_mmm_mmm_monitor_mode(),
          api_upper_alarm_temp: api.get_thermostat_mt_mmi_mmi_upper_alarm_temp(),
        };
        if !crate::gumbox::thermostat_mt_ma_ma_GUMBOX::compute_CEP_Pre(
          pre.In_lastCmd, pre.api_current_tempWstatus, pre.api_lower_alarm_temp, pre.api_monitor_mode, pre.api_upper_alarm_temp) {
          log::warn!("*** CONTRACT VIOLATION: thermostat_mt_ma_ma CEP_Pre not satisfied ***");
          log::warn!("thermostat_mt_ma_ma pre: {:?}", pre);
        }
        self.pre_thermostat_mt_ma_ma = Some(pre);
      }
      thermostat_mt_mmm_mmm_MON => {
        let pre = PreState_thermostat_mt_mmm_mmm {
          In_lastMonitorMode: api.get_thermostat_mt_mmm_mmm_sv_lastMonitorMode(),
          api_current_tempWstatus: api.get_temperature_sensor_cpi_thermostat_current_tempWstatus(),
          api_interface_failure: api.get_thermostat_mt_mmi_mmi_interface_failure(),
          api_internal_failure: api.get_thermostat_mt_dmf_dmf_internal_failure(),
        };
        self.pre_thermostat_mt_mmm_mmm = Some(pre);
      }
      operator_interface_oip_oit_MON => {
        let pre = PreState_operator_interface_oip_oit {
          api_alarm_control: api.get_thermostat_mt_ma_ma_alarm_control(),
          api_display_temperature: api.get_thermostat_rt_mri_mri_displayed_temp(),
          api_monitor_status: api.get_thermostat_mt_mmi_mmi_monitor_status(),
          api_regulator_status: api.get_thermostat_rt_mri_mri_regulator_status(),
        };
        self.pre_operator_interface_oip_oit = Some(pre);
      }
      _ => {}
    }

    self.last_index = state.current_timeslice;
  }
}

#[verus_verify(external_body)]
pub fn log_info(msg: &str)
{
  log::info!("{0}", msg);
}

#[verus_verify(external_body)]
pub fn log_warn_channel(channel: u32)
{
  log::warn!("Unexpected channel: {0}", channel);
}

// PLACEHOLDER MARKER GUMBO METHODS

// Schedule channel IDs used to identify which thread yielded or runs next

pub const pad: u32 = 0;

pub const temperature_sensor_cpi_thermostat_MON: u32 = 2;

pub const thermostat_mt_mmm_mmm_MON: u32 = 3;

pub const thermostat_mt_mmi_mmi_MON: u32 = 4;

pub const thermostat_mt_ma_ma_MON: u32 = 5;

pub const thermostat_mt_dmf_dmf_MON: u32 = 6;

pub const thermostat_rt_mri_mri_MON: u32 = 7;

pub const thermostat_rt_mrm_mrm_MON: u32 = 8;

pub const thermostat_rt_mhs_mhs_MON: u32 = 9;

pub const thermostat_rt_drf_drf_MON: u32 = 10;

pub const heat_source_cpi_heat_controller_MON: u32 = 11;

pub const operator_interface_oip_oit_MON: u32 = 12;

pub const gumbo_monitor_process_gumbo_monitor_thread_MON: u32 = 14;

// For each timeslice index, finds the nearest preceding and following user
// partition channels. This lets the monitor know which thread just yielded
// (prev) and which will run next (next) so it can check post-conditions
// and capture pre-state at the right time.
#[verus_verify(external_body)]
pub fn buildUserChannelTables(
  sched: &hamr::Schedule,
  prev: &mut hamr::ScheduleChannels,
  next: &mut hamr::ScheduleChannels)
{
  let n = sched.num_timeslices as usize;
  for i in 0..n {
    let mut found_prev = false;
    let mut found_next = false;
    for offset in 1..n {
      if !found_prev {
        let backward = (i + n - offset) % n;
        if sched.is_user_partition[backward] {
          prev[i] = sched.timeslice_ch[backward];
          found_prev = true;
        }
      }
      if !found_next {
        let forward = (i + offset) % n;
        if sched.is_user_partition[forward] {
          next[i] = sched.timeslice_ch[forward];
          found_next = true;
        }
      }
      if found_prev && found_next {
        break;
      }
    }
  }
}
