// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::tcp_tct_api::*;
use vstd::prelude::*;

verus! {

  pub struct tcp_tct {
    // BEGIN MARKER STATE VARS
    pub currentSetPoint: TempControl_SysVerif::SetPoint,
    pub currentFanState: TempControl_SysVerif::FanCmd,
    pub latestTemp: TempControl_SysVerif::Temperature,
    pub fanError: bool,
    // END MARKER STATE VARS
  }

  impl tcp_tct {
    pub fn new() -> Self
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        currentSetPoint: TempControl_SysVerif::SetPoint::default(),
        currentFanState: TempControl_SysVerif::FanCmd::default(),
        latestTemp: TempControl_SysVerif::Temperature::default(),
        fanError: false,
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: tcp_tct_Put_Api> (
      &mut self,
      api: &mut tcp_tct_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee defaultSetPoint
        (self.currentSetPoint.low.degrees == 70i32) &&
          (self.currentSetPoint.high.degrees == 90i32),
        // guarantee defaultFanStates
        self.currentFanState == TempControl_SysVerif::FanCmd::Off,
        // guarantee defaultLatestTemp
        self.latestTemp.degrees == 72i32,
        // END MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: tcp_tct_Full_Api> (
      &mut self,
      api: &mut tcp_tct_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).fanCmd.is_none(),
        // assume a1
        //   If the previously received currentTemp was less than the previously
        //   received setPoint then the last fan command must have been Off
        (!(old(self).fanError) &&
          ((old(self).latestTemp).degrees < (old(self).currentSetPoint).low.degrees)) ==>
          (old(self).currentFanState == TempControl_SysVerif::FanCmd::Off),
        // assume a2
        //   If the previously received currentTemp was more than the previously
        //   received setPoint then the last fan command must have been On
        (!(old(self).fanError) &&
          ((old(self).latestTemp).degrees > (old(self).currentSetPoint).high.degrees)) ==>
          (old(self).currentFanState == TempControl_SysVerif::FanCmd::On),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee errorState
        //   If the fan sent FanAck.Error then continue sending the
        //   last fan command until the fan sends FanAck.Ok
        self.fanError ==>
          ((old(self).currentFanState == self.currentFanState) &&
            (api.fanCmd.is_some() &&
              (api.fanCmd.unwrap() == self.currentFanState))),
        // guarantee TC_Req_01
        //   If the current temperature is less than the set point, then the fan state shall be Off.
        (!self.fanError &&
          (self.latestTemp.degrees < self.currentSetPoint.low.degrees)) ==>
          (self.currentFanState == TempControl_SysVerif::FanCmd::Off),
        // guarantee TC_Req_02
        //   If the current temperature is greater than the set point,
        //   then the fan state shall be On.
        (!self.fanError &&
          (self.latestTemp.degrees > self.currentSetPoint.high.degrees)) ==>
          (self.currentFanState == TempControl_SysVerif::FanCmd::On),
        // guarantee TC_Req_03
        //   If the current temperature is greater than or equal to the
        //   current low set point and less than or equal to the current high set point,
        //   then the current fan state is maintained.
        ((!self.fanError &&
          (self.latestTemp.degrees >= self.currentSetPoint.low.degrees)) &&
          (self.latestTemp.degrees <= self.currentSetPoint.high.degrees)) ==>
          (self.currentFanState == old(self).currentFanState),
        // guarantee mustSendFanCmd
        //   If the local record of the fan state was updated,
        //   then send a fan command event with this updated value.
        ((!self.fanError &&
          (old(self).currentFanState != self.currentFanState)) ==>
          api.fanCmd.is_some() &&
            (api.fanCmd.unwrap() == self.currentFanState)) &&
          ((!self.fanError &&
            (self.currentFanState == old(self).currentFanState)) ==>
            api.fanCmd.is_none()),
        // guarantee setPointChanged
        api.setPoint.is_some() ==>
          (self.currentSetPoint == api.setPoint.unwrap()),
        // guarantee fanAck_setPointNotModified
        (api.fanAck.is_some() && !(api.setPoint.is_some())) ==>
          (self.currentSetPoint == old(self).currentSetPoint),
        // guarantee currentTemp_setPointNotModified
        (api.currentTemp.is_some() && !(api.setPoint.is_some())) ==>
          (self.currentSetPoint == old(self).currentSetPoint),
        // guarantee tempChanged
        api.currentTemp.is_some() ==>
          (self.latestTemp == api.currentTemp.unwrap()),
        // guarantee setPoint_latestTempNotModified
        (api.setPoint.is_some() && !(api.currentTemp.is_some())) ==>
          (self.latestTemp == old(self).latestTemp),
        // guarantee fanAck_latestTempNotModified
        (api.fanAck.is_some() && !(api.currentTemp.is_some())) ==>
          (self.latestTemp == old(self).latestTemp),
        // guarantee manageErrorState
        api.fanAck.is_some() ==>
          (((api.fanAck.unwrap() == TempControl_SysVerif::FanAck::Ok) ==>
            !self.fanError) &&
            ((api.fanAck.unwrap() == TempControl_SysVerif::FanAck::Error) ==>
              self.fanError)),
        // END MARKER TIME TRIGGERED ENSURES
    {
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
  pub open spec fn validRange(temp: i32) -> bool
  {
    (temp >= -129i32) &&
      (temp <= 134i32)
  }
  // END MARKER GUMBO METHODS

}
