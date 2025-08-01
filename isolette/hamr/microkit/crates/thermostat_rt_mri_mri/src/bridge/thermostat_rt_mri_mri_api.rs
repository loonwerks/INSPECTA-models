// Do not edit this file as it will be overwritten if codegen is rerun

use vstd::prelude::*;
use data::*;
use super::extern_c_api as extern_api;

verus! {
  pub trait thermostat_rt_mri_mri_Api {}

  pub trait thermostat_rt_mri_mri_Put_Api: thermostat_rt_mri_mri_Api {
    #[verifier::external_body]
    fn unverified_put_upper_desired_temp(
      &mut self,
      value: Isolette_Data_Model::Temp_i)
    {
      extern_api::unsafe_put_upper_desired_temp(&value);
    }

    #[verifier::external_body]
    fn unverified_put_lower_desired_temp(
      &mut self,
      value: Isolette_Data_Model::Temp_i)
    {
      extern_api::unsafe_put_lower_desired_temp(&value);
    }

    #[verifier::external_body]
    fn unverified_put_displayed_temp(
      &mut self,
      value: Isolette_Data_Model::Temp_i)
    {
      extern_api::unsafe_put_displayed_temp(&value);
    }

    #[verifier::external_body]
    fn unverified_put_regulator_status(
      &mut self,
      value: Isolette_Data_Model::Status)
    {
      extern_api::unsafe_put_regulator_status(&value);
    }

    #[verifier::external_body]
    fn unverified_put_interface_failure(
      &mut self,
      value: Isolette_Data_Model::Failure_Flag_i)
    {
      extern_api::unsafe_put_interface_failure(&value);
    }
  }

  pub trait thermostat_rt_mri_mri_Get_Api: thermostat_rt_mri_mri_Api {
    #[verifier::external_body]
    fn unverified_get_regulator_mode(
      &mut self,
      value: &Ghost<Isolette_Data_Model::Regulator_Mode>) -> (res : Isolette_Data_Model::Regulator_Mode)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_regulator_mode();
    }

    #[verifier::external_body]
    fn unverified_get_lower_desired_tempWstatus(
      &mut self,
      value: &Ghost<Isolette_Data_Model::TempWstatus_i>) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_lower_desired_tempWstatus();
    }

    #[verifier::external_body]
    fn unverified_get_upper_desired_tempWstatus(
      &mut self,
      value: &Ghost<Isolette_Data_Model::TempWstatus_i>) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        res == value@
    {
      return extern_api::unsafe_get_upper_desired_tempWstatus();
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

  pub trait thermostat_rt_mri_mri_Full_Api: thermostat_rt_mri_mri_Put_Api + thermostat_rt_mri_mri_Get_Api {}

  pub struct thermostat_rt_mri_mri_Application_Api<API: thermostat_rt_mri_mri_Api> {
    pub api: API,

    pub ghost upper_desired_temp: Isolette_Data_Model::Temp_i,
    pub ghost lower_desired_temp: Isolette_Data_Model::Temp_i,
    pub ghost displayed_temp: Isolette_Data_Model::Temp_i,
    pub ghost regulator_status: Isolette_Data_Model::Status,
    pub ghost interface_failure: Isolette_Data_Model::Failure_Flag_i,
    pub ghost regulator_mode: Isolette_Data_Model::Regulator_Mode,
    pub ghost lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
    pub ghost upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
    pub ghost current_tempWstatus: Isolette_Data_Model::TempWstatus_i
  }

  impl<API: thermostat_rt_mri_mri_Put_Api> thermostat_rt_mri_mri_Application_Api<API> {
    pub fn put_upper_desired_temp(
      &mut self,
      value: Isolette_Data_Model::Temp_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        self.upper_desired_temp == value,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_put_upper_desired_temp(value);
      self.upper_desired_temp = value;
    }
    pub fn put_lower_desired_temp(
      &mut self,
      value: Isolette_Data_Model::Temp_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        self.lower_desired_temp == value,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_put_lower_desired_temp(value);
      self.lower_desired_temp = value;
    }
    pub fn put_displayed_temp(
      &mut self,
      value: Isolette_Data_Model::Temp_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        self.displayed_temp == value,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_put_displayed_temp(value);
      self.displayed_temp = value;
    }
    pub fn put_regulator_status(
      &mut self,
      value: Isolette_Data_Model::Status)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        self.regulator_status == value,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_put_regulator_status(value);
      self.regulator_status = value;
    }
    pub fn put_interface_failure(
      &mut self,
      value: Isolette_Data_Model::Failure_Flag_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        self.interface_failure == value
    {
      self.api.unverified_put_interface_failure(value);
      self.interface_failure = value;
    }
  }

  impl<API: thermostat_rt_mri_mri_Get_Api> thermostat_rt_mri_mri_Application_Api<API> {
    pub fn get_regulator_mode(&mut self) -> (res : Isolette_Data_Model::Regulator_Mode)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        res == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_get_regulator_mode(&Ghost(self.regulator_mode))
    }
    pub fn get_lower_desired_tempWstatus(&mut self) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        res == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_get_lower_desired_tempWstatus(&Ghost(self.lower_desired_tempWstatus))
    }
    pub fn get_upper_desired_tempWstatus(&mut self) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        res == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_get_upper_desired_tempWstatus(&Ghost(self.upper_desired_tempWstatus))
    }
    pub fn get_current_tempWstatus(&mut self) -> (res : Isolette_Data_Model::TempWstatus_i)
      ensures
        old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
        old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
        old(self).current_tempWstatus == self.current_tempWstatus,
        res == self.current_tempWstatus,
        old(self).regulator_mode == self.regulator_mode,
        old(self).upper_desired_temp == self.upper_desired_temp,
        old(self).lower_desired_temp == self.lower_desired_temp,
        old(self).displayed_temp == self.displayed_temp,
        old(self).regulator_status == self.regulator_status,
        old(self).interface_failure == self.interface_failure
    {
      self.api.unverified_get_current_tempWstatus(&Ghost(self.current_tempWstatus))
    }
  }

  pub struct thermostat_rt_mri_mri_Initialization_Api;
  impl thermostat_rt_mri_mri_Api for thermostat_rt_mri_mri_Initialization_Api {}
  impl thermostat_rt_mri_mri_Put_Api for thermostat_rt_mri_mri_Initialization_Api {}

  pub const fn init_api() -> thermostat_rt_mri_mri_Application_Api<thermostat_rt_mri_mri_Initialization_Api> {
    return thermostat_rt_mri_mri_Application_Api {
      api: thermostat_rt_mri_mri_Initialization_Api {},

      upper_desired_temp: Isolette_Data_Model::Temp_i { degrees: 0 },
      lower_desired_temp: Isolette_Data_Model::Temp_i { degrees: 0 },
      displayed_temp: Isolette_Data_Model::Temp_i { degrees: 0 },
      regulator_status: Isolette_Data_Model::Status::Init_Status,
      interface_failure: Isolette_Data_Model::Failure_Flag_i { flag: false },
      regulator_mode: Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode,
      lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid },
      upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid },
      current_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid }
    }
  }

  pub struct thermostat_rt_mri_mri_Compute_Api;
  impl thermostat_rt_mri_mri_Api for thermostat_rt_mri_mri_Compute_Api {}
  impl thermostat_rt_mri_mri_Put_Api for thermostat_rt_mri_mri_Compute_Api {}
  impl thermostat_rt_mri_mri_Get_Api for thermostat_rt_mri_mri_Compute_Api {}
  impl thermostat_rt_mri_mri_Full_Api for thermostat_rt_mri_mri_Compute_Api {}

  pub const fn compute_api() -> thermostat_rt_mri_mri_Application_Api<thermostat_rt_mri_mri_Compute_Api> {
    return thermostat_rt_mri_mri_Application_Api {
      api: thermostat_rt_mri_mri_Compute_Api {},

      upper_desired_temp: Isolette_Data_Model::Temp_i { degrees: 0 },
      lower_desired_temp: Isolette_Data_Model::Temp_i { degrees: 0 },
      displayed_temp: Isolette_Data_Model::Temp_i { degrees: 0 },
      regulator_status: Isolette_Data_Model::Status::Init_Status,
      interface_failure: Isolette_Data_Model::Failure_Flag_i { flag: false },
      regulator_mode: Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode,
      lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid },
      upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid },
      current_tempWstatus: Isolette_Data_Model::TempWstatus_i { degrees: 0, status: Isolette_Data_Model::ValueStatus::Valid }
    }
  }
}