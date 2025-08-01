// Do not edit this file as it will be overwritten if codegen is rerun

use vstd::prelude::*;
use data::*;
use super::extern_c_api as extern_api;

verus! {
  pub trait thermostat_mt_mmm_mmm_Api {}

  pub trait thermostat_mt_mmm_mmm_Put_Api: thermostat_mt_mmm_mmm_Api {
    #[verifier::external_body]
    fn unverified_put_monitor_mode(
      &mut self,
      value: Isolette_Data_Model::Monitor_Mode)
    {
      extern_api::unsafe_put_monitor_mode(&value);
    }
  }

  pub trait thermostat_mt_mmm_mmm_Get_Api: thermostat_mt_mmm_mmm_Api {
    #[verifier::external_body]
    fn unverified_get_interface_failure(
      &mut self,
      value: &Ghost<Isolette_Data_Model::Failure_Flag_i>) -> (res : Isolette_Data_Model::Failure_Flag_i)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_interface_failure();
    }

    #[verifier::external_body]
    fn unverified_get_internal_failure(
      &mut self,
      value: &Ghost<Isolette_Data_Model::Failure_Flag_i>) -> (res : Isolette_Data_Model::Failure_Flag_i)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_internal_failure();
    }

    #[verifier::external_body]
    fn unverified_get_current_tempWstatus(
      &mut self,
      value: &Ghost<Isolette_Data_Model::TempWstatus_i>) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_current_tempWstatus();
    }
  }

  pub trait thermostat_mt_mmm_mmm_Full_Api: thermostat_mt_mmm_mmm_Put_Api + thermostat_mt_mmm_mmm_Get_Api {}

  pub struct thermostat_mt_mmm_mmm_Application_Api<API: thermostat_mt_mmm_mmm_Api> {
    pub api: API,

    pub ghost interface_failure: Isolette_Data_Model::Failure_Flag_i,
    pub ghost monitor_mode: Isolette_Data_Model::Monitor_Mode,
    pub ghost internal_failure: Isolette_Data_Model::Failure_Flag_i,
    pub ghost current_tempWstatus: Isolette_Data_Model::TempWstatus_i
  }

  impl<API: thermostat_mt_mmm_mmm_Put_Api> thermostat_mt_mmm_mmm_Application_Api<API> {
    pub fn put_monitor_mode(
      &mut self,
      value: Isolette_Data_Model::Monitor_Mode)
      ensures
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).interface_failure == self.interface_failure,
        old(self).internal_failure == self.internal_failure,
        self.monitor_mode == value
    {
      self.api.unverified_put_monitor_mode(value);
      self.monitor_mode = value;
    }
  }

  impl<API: thermostat_mt_mmm_mmm_Get_Api> thermostat_mt_mmm_mmm_Application_Api<API> {
    pub fn get_interface_failure(&mut self) -> (res : Isolette_Data_Model::Failure_Flag_i)
      ensures
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).interface_failure == self.interface_failure,
        res == self.interface_failure,
        old(self).internal_failure == self.internal_failure,
        old(self).monitor_mode == self.monitor_mode
    {
      self.api.unverified_get_interface_failure(&Ghost(self.interface_failure))
    }
    pub fn get_internal_failure(&mut self) -> (res : Isolette_Data_Model::Failure_Flag_i)
      ensures
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).interface_failure == self.interface_failure,
        old(self).internal_failure == self.internal_failure,
        res == self.internal_failure,
        old(self).monitor_mode == self.monitor_mode
    {
      self.api.unverified_get_internal_failure(&Ghost(self.internal_failure))
    }
    pub fn get_current_tempWstatus(&mut self) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        old(self).current_tempWstatus == self.current_tempWstatus,
        res == self.current_tempWstatus,
        old(self).interface_failure == self.interface_failure,
        old(self).internal_failure == self.internal_failure,
        old(self).monitor_mode == self.monitor_mode
    {
      self.api.unverified_get_current_tempWstatus(&Ghost(self.current_tempWstatus))
    }
  }

  pub struct thermostat_mt_mmm_mmm_Initialization_Api;
  impl thermostat_mt_mmm_mmm_Api for thermostat_mt_mmm_mmm_Initialization_Api {}
  impl thermostat_mt_mmm_mmm_Put_Api for thermostat_mt_mmm_mmm_Initialization_Api {}

  pub const fn init_api() -> thermostat_mt_mmm_mmm_Application_Api<thermostat_mt_mmm_mmm_Initialization_Api> {
    return thermostat_mt_mmm_mmm_Application_Api {
      api: thermostat_mt_mmm_mmm_Initialization_Api {},

      interface_failure: Isolette_Data_Model::Failure_Flag_i { flag: false },
      monitor_mode: Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode,
      internal_failure: Isolette_Data_Model::Failure_Flag_i { flag: false },
      current_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid }
    }
  }

  pub struct thermostat_mt_mmm_mmm_Compute_Api;
  impl thermostat_mt_mmm_mmm_Api for thermostat_mt_mmm_mmm_Compute_Api {}
  impl thermostat_mt_mmm_mmm_Put_Api for thermostat_mt_mmm_mmm_Compute_Api {}
  impl thermostat_mt_mmm_mmm_Get_Api for thermostat_mt_mmm_mmm_Compute_Api {}
  impl thermostat_mt_mmm_mmm_Full_Api for thermostat_mt_mmm_mmm_Compute_Api {}

  pub const fn compute_api() -> thermostat_mt_mmm_mmm_Application_Api<thermostat_mt_mmm_mmm_Compute_Api> {
    return thermostat_mt_mmm_mmm_Application_Api {
      api: thermostat_mt_mmm_mmm_Compute_Api {},

      interface_failure: Isolette_Data_Model::Failure_Flag_i { flag: false },
      monitor_mode: Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode,
      internal_failure: Isolette_Data_Model::Failure_Flag_i { flag: false },
      current_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid }
    }
  }
}