// Do not edit this file as it will be overwritten if codegen is rerun

use vstd::prelude::*;
use data::*;
use super::extern_c_api as extern_api;

verus! {
  pub trait ArduPilot_ArduPilot_Api {}

  pub trait ArduPilot_ArduPilot_Put_Api: ArduPilot_ArduPilot_Api {
    #[verifier::external_body]
    fn unverified_put_EthernetFramesTx(
      &mut self,
      value: SW::RawEthernetMessage)
    {
      extern_api::unsafe_put_EthernetFramesTx(&value);
    }
  }

  pub trait ArduPilot_ArduPilot_Get_Api: ArduPilot_ArduPilot_Api {
    #[verifier::external_body]
    fn unverified_get_EthernetFramesRx(
      &mut self,
      value: &Ghost<Option<SW::RawEthernetMessage>>) -> (res : Option<SW::RawEthernetMessage>)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_EthernetFramesRx();
    }
  }

  pub trait ArduPilot_ArduPilot_Full_Api: ArduPilot_ArduPilot_Put_Api + ArduPilot_ArduPilot_Get_Api {}

  pub struct ArduPilot_ArduPilot_Application_Api<API: ArduPilot_ArduPilot_Api> {
    pub api: API,

    pub ghost EthernetFramesTx: Option<SW::RawEthernetMessage>,
    pub ghost EthernetFramesRx: Option<SW::RawEthernetMessage>
  }

  impl<API: ArduPilot_ArduPilot_Put_Api> ArduPilot_ArduPilot_Application_Api<API> {
    pub fn put_EthernetFramesTx(
      &mut self,
      value: SW::RawEthernetMessage)
      ensures
        old(self).EthernetFramesRx == self.EthernetFramesRx,
        self.EthernetFramesTx == Some(value)
    {
      self.api.unverified_put_EthernetFramesTx(value);
      self.EthernetFramesTx = Some(value);
    }
  }

  impl<API: ArduPilot_ArduPilot_Get_Api> ArduPilot_ArduPilot_Application_Api<API> {
    pub fn get_EthernetFramesRx(&mut self) -> (res : Option<SW::RawEthernetMessage>)
      ensures
        old(self).EthernetFramesRx == self.EthernetFramesRx,
        res == self.EthernetFramesRx,
        old(self).EthernetFramesTx == self.EthernetFramesTx
    {
      self.api.unverified_get_EthernetFramesRx(&Ghost(self.EthernetFramesRx))
    }
  }

  pub struct ArduPilot_ArduPilot_Initialization_Api;
  impl ArduPilot_ArduPilot_Api for ArduPilot_ArduPilot_Initialization_Api {}
  impl ArduPilot_ArduPilot_Put_Api for ArduPilot_ArduPilot_Initialization_Api {}

  pub const fn init_api() -> ArduPilot_ArduPilot_Application_Api<ArduPilot_ArduPilot_Initialization_Api> {
    return ArduPilot_ArduPilot_Application_Api {
      api: ArduPilot_ArduPilot_Initialization_Api {},

      EthernetFramesTx: None,
      EthernetFramesRx: None
    }
  }

  pub struct ArduPilot_ArduPilot_Compute_Api;
  impl ArduPilot_ArduPilot_Api for ArduPilot_ArduPilot_Compute_Api {}
  impl ArduPilot_ArduPilot_Put_Api for ArduPilot_ArduPilot_Compute_Api {}
  impl ArduPilot_ArduPilot_Get_Api for ArduPilot_ArduPilot_Compute_Api {}
  impl ArduPilot_ArduPilot_Full_Api for ArduPilot_ArduPilot_Compute_Api {}

  pub const fn compute_api() -> ArduPilot_ArduPilot_Application_Api<ArduPilot_ArduPilot_Compute_Api> {
    return ArduPilot_ArduPilot_Application_Api {
      api: ArduPilot_ArduPilot_Compute_Api {},

      EthernetFramesTx: None,
      EthernetFramesRx: None
    }
  }
}