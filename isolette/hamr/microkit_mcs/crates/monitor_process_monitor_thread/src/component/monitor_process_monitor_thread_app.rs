// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_process_monitor_thread_api::*;
use crate::component::thermostat_rt_mhs_mhs_containers::*;
use gumbox_contracts::thermostat_rt_mhs_mhs_GUMBOX as mhs_gumbox;
use vstd::prelude::*;

verus! {

  pub struct monitor_process_monitor_thread {
    frame_period: i32,
    last_index: u32,
    prev_user_ch: hamr::ScheduleChannels,
    next_user_ch: hamr::ScheduleChannels,
    pre_mhs: Option<PreState_thermostat_rt_mhs_mhs>,
    // TODO add pre-state fields for other threads
    // PLACEHOLDER MARKER STATE VARS
  }

  #[repr(u32)]
  #[derive(Debug, Clone, Copy, PartialEq, Eq)]
  pub enum MonitorChannel {
    pad = 0,
    temperature_sensor_cpi_thermostat_MON = 2,
    thermostat_mt_mmm_mmm_MON = 3,
    thermostat_mt_mmi_mmi_MON = 4,
    thermostat_mt_ma_ma_MON = 5,
    thermostat_mt_dmf_dmf_MON = 6,
    thermostat_rt_mri_mri_MON = 7,
    thermostat_rt_mrm_mrm_MON = 8,
    thermostat_rt_mhs_mhs_MON = 9,
    thermostat_rt_drf_drf_MON = 10,
    heat_source_cpi_heat_controller_MON = 11,
    operator_interface_oip_oit_MON = 12,
    monitor_process_monitor_thread_MON = 13,
  }

  impl monitor_process_monitor_thread {
    pub fn new() -> Self
    {
      Self {
        frame_period: 0,
        last_index: u32::MAX,
        prev_user_ch: [0; hamr::hamr_ScheduleChannels_DIM_0],
        next_user_ch: [0; hamr::hamr_ScheduleChannels_DIM_0],
        pre_mhs: None,
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
      self.no_verus(api);
    }

    #[verifier::external_body]
    pub fn no_verus <API: monitor_process_monitor_thread_Full_Api>(
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>) {

      let state = api.get_sched_state();
      let schedule = api.get_sched_schedule();

      if self.last_index == u32::MAX {
        buildUserChannelTables(
          &schedule, &mut self.prev_user_ch, &mut self.next_user_ch);
      }

      if state.current_timeslice <= self.last_index {
        self.frame_period = self.frame_period + 1;
      }

      let idx = state.current_timeslice as usize;
      //log::info!("Schedule State: Index {:?}", state.current_timeslice);

      if self.last_index == u32::MAX {
        log::info!("First compute phase, check initialization guarantees");

        let post_mhs = PostState_thermostat_rt_mhs_mhs {
          lastCmd: api.get_mhs_mhs_sv_lastCmd(),
          api_heat_control: api.get_mhs_mhs_heat_control(),
        };
        let init_result = mhs_gumbox::initialize_IEP_Post(
          post_mhs.lastCmd, post_mhs.api_heat_control);
        log::info!("mhs IEP_Post: {}", init_result);
      } else {
        //log::info!("Check the post condition of the thread that just yielded");

        let prev_ch: MonitorChannel = unsafe { core::mem::transmute(self.prev_user_ch[idx]) };
        match prev_ch {
          MonitorChannel::temperature_sensor_cpi_thermostat_MON => {
            // TODO populate PostState for cpi_thermostat and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_mt_mmm_mmm_MON => {
            // TODO populate PostState for mmm and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_mt_mmi_mmi_MON => {
            // TODO populate PostState for mmi and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_mt_ma_ma_MON => {
            // TODO populate PostState for ma and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_mt_dmf_dmf_MON => {
            // TODO populate PostState for dmf and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_rt_mri_mri_MON => {
            // TODO populate PostState for mri and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_rt_mrm_mrm_MON => {
            // TODO populate PostState for mrm and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::thermostat_rt_mhs_mhs_MON => {
            let post = PostState_thermostat_rt_mhs_mhs {
              lastCmd: api.get_mhs_mhs_sv_lastCmd(),
              api_heat_control: api.get_mhs_mhs_heat_control(),
            };
            log::info!("mhs post state: {:?}", post);
            if let Some(pre) = &self.pre_mhs {
              let post_result = mhs_gumbox::compute_CEP_Post(
                pre.In_lastCmd,
                post.lastCmd,
                pre.api_current_tempWstatus,
                pre.api_lower_desired_temp,
                pre.api_regulator_mode,
                pre.api_upper_desired_temp,
                post.api_heat_control);
              log::info!("mhs CEP_Post: {}", post_result);
            } else {
              log::warn!("mhs post check skipped: no saved pre-state");
            }
          }
          MonitorChannel::thermostat_rt_drf_drf_MON => {
            // TODO populate PostState for drf and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::heat_source_cpi_heat_controller_MON => {
            // TODO populate PostState for cpi_heat_controller and call GUMBOX compute_CEP_Post
          }
          MonitorChannel::operator_interface_oip_oit_MON => {
            // TODO populate PostState for oip_oit and call GUMBOX compute_CEP_Post
          }
          _ => {
            //log::info!("Post Channel: {:?}", prev_ch);
          }
        }
      }

      let next_ch: MonitorChannel = unsafe { core::mem::transmute(self.next_user_ch[idx]) };
      match next_ch {
        MonitorChannel::temperature_sensor_cpi_thermostat_MON => {
          // TODO populate PreState for cpi_thermostat and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_mt_mmm_mmm_MON => {
          // TODO populate PreState for mmm and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_mt_mmi_mmi_MON => {
          // TODO populate PreState for mmi and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_mt_ma_ma_MON => {
          // TODO populate PreState for ma and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_mt_dmf_dmf_MON => {
          // TODO populate PreState for dmf and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_rt_mri_mri_MON => {
          // TODO populate PreState for mri and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_rt_mrm_mrm_MON => {
          // TODO populate PreState for mrm and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::thermostat_rt_mhs_mhs_MON => {
          let pre = PreState_thermostat_rt_mhs_mhs {
            In_lastCmd: api.get_mhs_mhs_sv_lastCmd(),
            api_current_tempWstatus: api.get_cpi_thermostat_current_tempWstatus(),
            api_lower_desired_temp: api.get_mri_mri_lower_desired_temp(),
            api_upper_desired_temp: api.get_mri_mri_upper_desired_temp(),
            api_regulator_mode: api.get_mrm_mrm_regulator_mode(),
          };
          log::info!("mhs pre state: {:?}", pre);
          let pre_result = mhs_gumbox::compute_CEP_Pre(
            pre.In_lastCmd,
            pre.api_current_tempWstatus,
            pre.api_lower_desired_temp,
            pre.api_regulator_mode,
            pre.api_upper_desired_temp);
          log::info!("mhs CEP_Pre: {}", pre_result);
          self.pre_mhs = Some(pre);
        }
        MonitorChannel::thermostat_rt_drf_drf_MON => {
          // TODO populate PreState for drf and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::heat_source_cpi_heat_controller_MON => {
          // TODO populate PreState for cpi_heat_controller and call GUMBOX compute_CEP_Pre
        }
        MonitorChannel::operator_interface_oip_oit_MON => {
          // TODO populate PreState for oip_oit and call GUMBOX compute_CEP_Pre
        }
        _ => {
          //log::info!("Pre Channel: {:?}", next_ch);
        }
      }

      self.last_index = state.current_timeslice;
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
