//! C-interface for the component.
//! This code must be unsafe.
//! Assumptions about correctness are introduced and need to verified by other means.


#![allow(dead_code)]
#![allow(non_snake_case)]

// Do not edit this file as it will be overwritten if codegen is rerun

use crate::data::*;

extern "C" {
  pub fn put_upper_desired_temp(data: *mut isolette_Isolette_Data_Model_Temp_i) -> bool;
  pub fn put_lower_desired_temp(data: *mut isolette_Isolette_Data_Model_Temp_i) -> bool;
  pub fn put_displayed_temp(data: *mut isolette_Isolette_Data_Model_Temp_i) -> bool;
  pub fn put_regulator_status(data: *mut isolette_Isolette_Data_Model_Status_Type) -> bool;
  pub fn put_interface_failure(data: *mut isolette_Isolette_Data_Model_Failure_Flag_i) -> bool;
  pub fn get_regulator_mode(data: *mut isolette_Isolette_Data_Model_Regulator_Mode_Type) -> bool;
  pub fn get_lower_desired_tempWstatus(data: *mut isolette_Isolette_Data_Model_TempWstatus_i) -> bool;
  pub fn get_upper_desired_tempWstatus(data: *mut isolette_Isolette_Data_Model_TempWstatus_i) -> bool;
  pub fn get_current_tempWstatus(data: *mut isolette_Isolette_Data_Model_TempWstatus_i) -> bool;
}

pub fn unsafe_put_upper_desired_temp(data: &isolette_Isolette_Data_Model_Temp_i) -> bool {
  unsafe { 
    let mut value: isolette_Isolette_Data_Model_Temp_i = *data;
    let valptr: *mut isolette_Isolette_Data_Model_Temp_i = &mut value;
    return put_upper_desired_temp(valptr) 
  }
}

pub fn unsafe_put_lower_desired_temp(data: &isolette_Isolette_Data_Model_Temp_i) -> bool {
  unsafe { 
    let mut value: isolette_Isolette_Data_Model_Temp_i = *data;
    let valptr: *mut isolette_Isolette_Data_Model_Temp_i = &mut value;
    return put_lower_desired_temp(valptr) 
  }
}

pub fn unsafe_put_displayed_temp(data: &isolette_Isolette_Data_Model_Temp_i) -> bool {
  unsafe {
    let mut value: isolette_Isolette_Data_Model_Temp_i = *data;
    let valptr: *mut isolette_Isolette_Data_Model_Temp_i = &mut value;
    return put_displayed_temp(valptr) 
  }
}

pub fn unsafe_put_regulator_status(data: &isolette_Isolette_Data_Model_Status_Type) -> bool {
  unsafe {
    let mut value: isolette_Isolette_Data_Model_Status_Type = *data;
    let valptr: *mut isolette_Isolette_Data_Model_Status_Type = &mut value;
    return put_regulator_status(valptr);
  }
}

pub fn unsafe_put_interface_failure(data: &isolette_Isolette_Data_Model_Failure_Flag_i) -> bool {
  unsafe {
    let mut value: isolette_Isolette_Data_Model_Failure_Flag_i = *data;
    let valptr: *mut isolette_Isolette_Data_Model_Failure_Flag_i = &mut value;
    return put_interface_failure(valptr);
  }
}

pub fn unsafe_get_regulator_mode() -> isolette_Isolette_Data_Model_Regulator_Mode_Type {
  let data: *mut isolette_Isolette_Data_Model_Regulator_Mode_Type = &mut isolette_Isolette_Data_Model_Regulator_Mode_Type::default();
  unsafe { 
    get_regulator_mode(data);
    return *data;
  }
}

pub fn unsafe_get_lower_desired_tempWstatus() -> isolette_Isolette_Data_Model_TempWstatus_i {
  let data: *mut isolette_Isolette_Data_Model_TempWstatus_i = &mut isolette_Isolette_Data_Model_TempWstatus_i::default();
  unsafe { 
    get_lower_desired_tempWstatus(data);
    return *data;
  };
}

pub fn unsafe_get_upper_desired_tempWstatus() -> isolette_Isolette_Data_Model_TempWstatus_i {
  let data: *mut isolette_Isolette_Data_Model_TempWstatus_i = &mut isolette_Isolette_Data_Model_TempWstatus_i::default();
  unsafe { 
    get_upper_desired_tempWstatus(data);
    return *data;
  };
}

pub fn unsafe_get_current_tempWstatus() -> isolette_Isolette_Data_Model_TempWstatus_i {
  let data: *mut isolette_Isolette_Data_Model_TempWstatus_i = &mut isolette_Isolette_Data_Model_TempWstatus_i::default();
  unsafe { 
    get_current_tempWstatus(data);
    return *data;
  };
}
