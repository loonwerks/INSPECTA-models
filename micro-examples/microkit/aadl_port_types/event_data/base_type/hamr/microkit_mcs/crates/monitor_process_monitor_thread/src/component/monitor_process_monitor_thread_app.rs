// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_process_monitor_thread_api::*;
use vstd::prelude::*;

verus! {

  pub struct monitor_process_monitor_thread {
    frame_period: i32,
    last_index: u32,
    prev_user_ch: hamr::ScheduleChannels,
    next_user_ch: hamr::ScheduleChannels,
    // PLACEHOLDER MARKER STATE VARS
  }

  /// Channel Assignments
  /// -------------------
  /// 0 - pad
  /// 2 - producer_p_p1_producer_MON
  /// 3 - producer_p_p2_producer_MON
  /// 4 - consumer_p_p_consumer_MON
  /// 5 - consumer_p_s_consumer_MON
  /// 6 - monitor_process_monitor_thread_MON
  impl monitor_process_monitor_thread {
    pub fn new() -> Self
    {
      Self {
        frame_period: 0,
        last_index: u32::MAX,
        prev_user_ch: [0; hamr::hamr_ScheduleChannels_DIM_0],
        next_user_ch: [0; hamr::hamr_ScheduleChannels_DIM_0],
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
      self.no_verus(api);
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

    #[verifier::external_body]
    pub fn no_verus <API: monitor_process_monitor_thread_Full_Api>(
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>) {

      let state = api.get_sched_state();
      let schedule = api.get_sched_schedule();

      if self.last_index == u32::MAX {
        log::info!("Channel Assignments");
        log::info!("  0 - pad");
        log::info!("  2 - producer_p_p1_producer_MON");
        log::info!("  3 - producer_p_p2_producer_MON");
        log::info!("  4 - consumer_p_p_consumer_MON");
        log::info!("  5 - consumer_p_s_consumer_MON");
        log::info!("  6 - monitor_process_monitor_thread_MON\n");

        let n = schedule.num_timeslices as usize;

        log::info!("Schedule ({} timeslices):", n);
        for i in 0..n {
          let ch: u32 = schedule.timeslice_ch[i];
          let ns: u64 = schedule.timeslices[i];
          let user: bool = schedule.is_user_partition[i];
          log::info!("  [{}] ch={}, duration={}ns, user={}", i, ch, ns, user);
        }

        buildUserChannelTables(
          &schedule, &mut self.prev_user_ch, &mut self.next_user_ch);
      }

      if state.current_timeslice <= self.last_index {
        log::info!("################");
        log::info!("# FRAME PERIOD {}", self.frame_period);
        log::info!("################");

        self.frame_period = self.frame_period + 1;
      }

      //log::info!("Schedule State: Post {:?}, Pre {:?} (index {:?})", state.last_yielded_ch, state.next_dispatch_ch, state.current_timeslice);
      log::info!("Schedule State: Index {:?}", state.current_timeslice);

      let idx = state.current_timeslice as usize;
      if self.last_index == u32::MAX {
        log::info!("First compute phase, check initialization guarantees");

        log::info!("Pre Channel: {:?}", self.next_user_ch[idx]);
      } else {
        log::info!("Post Channel: {:?}", self.prev_user_ch[idx]);
        log::info!("Pre Channel: {:?}", self.next_user_ch[idx]);
      }

      if let Some(v) = api.get_producer_p_p1_producer_write_port() {
        log::info!("Received {:?} from producer_p_p1", v);
      }
      if let Some(v) = api.get_producer_p_p2_producer_write_port() {
        log::info!("Received {:?} from producer_p_p2", v);
      }

      self.last_index = state.current_timeslice;
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
