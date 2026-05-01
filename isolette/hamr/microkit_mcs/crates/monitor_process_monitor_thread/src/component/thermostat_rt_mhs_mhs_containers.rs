// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use vstd::prelude::*;

verus! {

  #[repr(C)]
  #[derive(Debug, Clone, Copy, PartialEq, Eq)]
  pub struct PreState_thermostat_rt_mhs_mhs {
    pub In_lastCmd: Isolette_Data_Model::On_Off,
    pub api_current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
    pub api_lower_desired_temp: Isolette_Data_Model::Temp_i,
    pub api_upper_desired_temp: Isolette_Data_Model::Temp_i,
    pub api_regulator_mode: Isolette_Data_Model::Regulator_Mode,
  }

  #[repr(C)]
  #[derive(Debug, Clone, Copy, PartialEq, Eq)]
  pub struct PostState_thermostat_rt_mhs_mhs {
    pub lastCmd: Isolette_Data_Model::On_Off,
    pub api_heat_control: Isolette_Data_Model::On_Off,
  }

}
