// Do not edit this file as it will be overwritten if codegen is rerun

use crate::bridge::extern_c_api as extern_api;
use data::*;

#[cfg(test)]
pub fn get_upper_desired_temp() -> Isolette_Data_Model::Temp_i 
 {
   return extern_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting None")
 }

#[cfg(test)]
pub fn get_lower_desired_temp() -> Isolette_Data_Model::Temp_i 
 {
   return extern_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None")
 }

#[cfg(test)]
pub fn get_displayed_temp() -> Isolette_Data_Model::Temp_i 
 {
   return extern_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None")
 }

#[cfg(test)]
pub fn get_regulator_status() -> Isolette_Data_Model::Status 
 {
   return extern_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None")
 }

#[cfg(test)]
pub fn get_interface_failure() -> Isolette_Data_Model::Failure_Flag_i 
 {
   return extern_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None")
 }

#[cfg(test)]
pub fn put_regulator_mode(value: Isolette_Data_Model::Regulator_Mode) 
 {
   *extern_api::IN_regulator_mode.lock().unwrap() = Some(value)
 }

#[cfg(test)]
pub fn put_lower_desired_tempWstatus(value: Isolette_Data_Model::TempWstatus_i) 
 {
   *extern_api::IN_lower_desired_tempWstatus.lock().unwrap() = Some(value)
 }

#[cfg(test)]
pub fn put_upper_desired_tempWstatus(value: Isolette_Data_Model::TempWstatus_i) 
 {
   *extern_api::IN_upper_desired_tempWstatus.lock().unwrap() = Some(value)
 }

#[cfg(test)]
pub fn put_current_tempWstatus(value: Isolette_Data_Model::TempWstatus_i) 
 {
   *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(value)
 }