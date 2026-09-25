// Content between markers will be preserved if codegen is rerun

use crate::bridge::consumer_consumer_api::*;
use crate::bridge::consumer_consumer_GUMBOX as GUMBOX;
use data::*;
use super::consumer_consumer_app::{consumer_consumer, log_info};
use vstd::prelude::*;

// Instance of the R2U2 monitor.
static mut R2U2_MONITOR: Option<R2U2Monitor> = None;

#[allow(non_camel_case_types)]
pub enum R2U2Property {
  sample_arrives,
}

verus! {
  impl consumer_consumer {
    pub fn handle_r2u2_verdict<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>,
      property: R2U2Property,
      verdict: Option<bool>)
      ensures
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).sample_alert == final(api).sample_alert,
    {
      // BEGIN MARKER R2U2 VERDICT HANDLER
      // Optional implementation placeholder for handling R2U2 verdicts.
      match property {
        _ => {}
      }
      // END MARKER R2U2 VERDICT HANDLER
    }
  }
}

struct R2U2Monitor {
  monitor: r2u2_core::Monitor,
  // Cache latest verdict (if applicable) per C2PO specification between monitor steps.
  verdict_cache: [Option<r2u2_core::r2u2_verdict>; 1],
}

impl R2U2Monitor {
  fn new() -> Self {
    Self {
      monitor: r2u2_core::Monitor::default(),
      verdict_cache: [None; 1],
    }
  }
}

impl consumer_consumer {
  pub fn r2u2_monitor_initialize(&mut self)
  {
    let mut r2u2_monitor = R2U2Monitor::new();
    r2u2_core::update_binary_file(include_bytes!("spec.bin"), &mut r2u2_monitor.monitor);
    unsafe { R2U2_MONITOR = Some(r2u2_monitor); }
  }

  pub fn r2u2_monitor_pre_timeTriggered<API: consumer_consumer_Full_Api> (
    &mut self,
    api: &consumer_consumer_Application_Api<API>)
  {
    let sample = api.peek_sample();

    let r2u2_monitor = unsafe {
      R2U2_MONITOR.as_mut().expect("R2U2 monitor used before initialization")
    };

    r2u2_core::load_bool_signal(&mut r2u2_monitor.monitor, 0, sample.is_some()); // Loading signal api_sample_nonEmpty into index 0
  }

  pub fn r2u2_monitor_post_timeTriggered<API: consumer_consumer_Full_Api> (
    &mut self,
    api: &mut consumer_consumer_Application_Api<API>)
  {
    let r2u2_monitor = unsafe {
      R2U2_MONITOR.as_mut().expect("R2U2 monitor used before initialization")
    };


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
    let mut false_verdict_seen = [false; 1];
    for out in output_buffer {
        let spec_num = out.spec_num as usize;
        if !out.verdict.truth {
            false_verdict_seen[spec_num] = true;
        }
        verdict_cache[spec_num] = Some(out.verdict);
    }
    // Delegate each current verdict to the preserved policy handler.
    let r2u2_sample_arrives_verdict = r2u2_monitor.verdict_cache[0]
        .map(|verdict| verdict.truth && !false_verdict_seen[0]);
    self.handle_r2u2_verdict(
        api,
        R2U2Property::sample_arrives,
        r2u2_sample_arrives_verdict);
    // Send one result through each mapped alert port.
    if let Some(truth) = r2u2_sample_arrives_verdict {
        if !truth {
            api.put_sample_alert();
        }
    }
  }
}
