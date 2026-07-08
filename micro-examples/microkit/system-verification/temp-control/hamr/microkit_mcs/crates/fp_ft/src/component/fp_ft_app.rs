// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::fp_ft_api::*;
use vstd::prelude::*;

verus! {

  // Configure the failure rate for the fan, i.e., an Error will be simulated
  // 35 out of every 100 acknowledgements.
  pub const ERROR_RATE: u32 = 35;

  pub struct fp_ft {
    // PLACEHOLDER MARKER STATE VARS

    // PRNG state used to simulate intermittent fan actuation failures. This is
    // developer-local state (not a GUMBO state variable) and carries the seed
    // for the xorshift64 generator between dispatches.
    pub rand_state: u64,
  }

  impl fp_ft {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT

        // arbitrary non-zero seed
        rand_state: 0x2545F4914F6CDD1Du64,
      }
    }

    pub fn initialize<API: fp_ft_Put_Api> (
      &mut self,
      api: &mut fp_ft_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: fp_ft_Full_Api> (
      &mut self,
      api: &mut fp_ft_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      // Periodic, merged form of the original sporadic Fan: instead of a
      // per-event fanCmd handler, consume whatever fan command is present this
      // period, simulate the actuation, and acknowledge the result.
      if let Some(cmd) = api.get_fanCmd() {
        // log the received fan command
        log_fanCmd(cmd);

        // compute the acknowledgement value: Ok when the actuation "succeeds",
        // Error when the simulated fault occurs
        let ackVal: TempControl_SysVerif::FanAck = self.next_ack();

        // put an event on the fanAck out event data port to notify subscribers
        // (e.g., the tempControl thermostat) that the fan actuation completed
        api.put_fanAck(ackVal);
      }
    }

    // Advance the PRNG and map the next draw onto an acknowledgement value,
    // yielding FanAck::Error roughly ERROR_RATE percent of the time. Marked
    // external_body because Verus does not reason about the bit-twiddling PRNG.
    #[verifier::external_body]
    fn next_ack(&mut self) -> TempControl_SysVerif::FanAck
    {
      // xorshift64
      let mut x: u64 = self.rand_state;
      x ^= x << 13;
      x ^= x >> 7;
      x ^= x << 17;
      self.rand_state = x;

      // next value in [0, 99]
      let draw: u32 = (x % 100) as u32;
      if draw < 100 - ERROR_RATE {
        TempControl_SysVerif::FanAck::Ok
      } else {
        TempControl_SysVerif::FanAck::Error
      }
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
  pub fn log_fanCmd(value: TempControl_SysVerif::FanCmd)
  {
    log::info!("Received fanCmd {0:?}", value);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32)
  {
    log::warn!("Unexpected channel: {0}", channel);
  }

  // PLACEHOLDER MARKER GUMBO METHODS

}
