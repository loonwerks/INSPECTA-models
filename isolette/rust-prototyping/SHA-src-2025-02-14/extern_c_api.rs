//! C-interface for the component.
//! This code must be unsafe.
//! Assumptions about correctness are introduced and need to verified by other means.

use crate::data;

extern "C" {
    pub fn get_upper_desired_temp(data: *mut data::Temp_i) -> bool;
    pub fn get_lower_desired_temp(data: *mut data::Temp_i) -> bool;
    pub fn put_heat_control(data: *mut data::OnOff) -> bool;
    pub fn get_regulator_mode(data: *mut data::Regulator_Mode) -> bool;
    pub fn get_current_tempWstatus(data: *mut data::TempWstatus_i) -> bool;
  }
  
  pub fn unsafe_get_upper_desired_temp() -> data::Temp_i {
    let data: *mut data::Temp_i = &mut data::Temp_i::default();
    unsafe {
        get_upper_desired_temp(data);
        *data
    }
  }
  
  pub fn unsafe_get_lower_desired_temp() -> data::Temp_i {
    let data: *mut data::Temp_i = &mut data::Temp_i::default();
    unsafe {
        get_lower_desired_temp(data);
        *data
    }
  }
  
  pub fn unsafe_put_heat_control(data: &data::OnOff) -> bool {
    let mut value = *data;
    let valptr: *mut data::OnOff = &mut value;
    unsafe {
        put_heat_control(valptr)
    }
  }
  
  pub fn unsafe_get_regulator_mode() -> data::Regulator_Mode {
    let data: *mut data::Regulator_Mode = &mut data::Regulator_Mode::default();
    unsafe {
        get_regulator_mode(data);
        *data
    }
  }
  
  pub fn unsafe_get_current_tempWstatus() -> data::TempWstatus_i {
    let data: *mut data::TempWstatus_i = &mut data::TempWstatus_i::default();
    unsafe {
        get_current_tempWstatus(data);
        *data
    }
  }