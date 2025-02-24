#![allow(non_camel_case_types)]
#![allow(non_upper_case_globals)]
#![allow(non_snake_case)]
#![allow(unused_parens)]

mod data;
mod art;
mod extern_c_api;
mod component;

use once_cell::sync::Lazy;
use std::sync::Mutex;

use crate::component::manage_regulator_interface_app::*;
use crate::component::manage_regulator_interface_api::*;

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

static mri: Lazy<Mutex<Manage_Regulator_Interface>> = Lazy::new(|| Mutex::new(Manage_Regulator_Interface::new()));
static init_api: Lazy<Mutex<Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api>>> = Lazy::new(|| Mutex::new(Manage_Regulator_Interface_Application_Api::new(MRI_Initialization_Api {})));
static compute_api: Lazy<Mutex<Manage_Regulator_Interface_Application_Api<MRI_Compute_Api>>> = Lazy::new(|| Mutex::new(Manage_Regulator_Interface_Application_Api::new(MRI_Compute_Api {})));

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_initialize() {
  #[cfg(not(test))]
  logging::LOGGER.set().unwrap();

  info!("initialize invoked");

  mri.lock().unwrap().initialise(&mut init_api.lock().unwrap());
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_timeTriggered() {
    info!("timeTriggered called");

    mri.lock().unwrap().timeTriggered(&mut compute_api.lock().unwrap());
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_notify(channel: data::microkit_channel) {
  // this method is called when the monitor does not handle the passed in channel
  match channel {
      _ => warn!("Unexpected channel {}", channel)
  }
}

#[cfg(test)]
mod tests {
    use serial_test::serial;
    use super::*;
    use crate::{data::*, extern_c_api};
    use crate::component::manage_regulator_interface_GUMBOX as GUMBOX;
    
    const failOnUnsatPrecondition: bool = false;

    #[test]
    #[serial]
    fn test_initialization() {

        mri.lock().unwrap().initialise(&mut init_api.lock().unwrap());

        // get values of outgoing ports
        let regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
        let display_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
        let upper_desired = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
        let lower_desired = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
        let interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");

        // get values of state variables
        let example_state_variable = mri.lock().unwrap().example_state_variable;

        assert!(GUMBOX::inititialize_IEP_Post(
            display_temp,
            interface_failure,
            lower_desired,
            regulator_status,
            upper_desired));

        assert!(regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status);
        assert!(display_temp == isolette_Isolette_Data_Model_Temp_i::default());
        assert!(upper_desired == isolette_Isolette_Data_Model_Temp_i::default());
        assert!(lower_desired == isolette_Isolette_Data_Model_Temp_i::default());
        assert!(interface_failure == isolette_Isolette_Data_Model_Failure_Flag_i::default());
        assert!(example_state_variable == 2001);

    }

    #[test]
    #[serial]
    fn test_compute_normal() {
        // generate values for the incoming ports and state variables
        let api_current_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 99, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let api_lower_desired_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 98, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let api_regulator_mode = isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode;
        let api_upper_desired_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 101, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let Old_example_state_variable: u32 = 42;

        // [CheckPre]: check/filter based on pre-condition.
        if(!GUMBOX::compute_CEP_Pre(
            api_current_tempWstatus, 
            api_lower_desired_tempWstatus, 
            api_regulator_mode, 
            api_upper_desired_tempWstatus)) {

            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }
        } else {
            // [PutInPorts]: put values on the input ports
            *extern_c_api::IN_regulator_mode.lock().unwrap() = Some(api_regulator_mode);
            *extern_c_api::IN_lower_desired_tempWstatus.lock().unwrap() = Some(api_lower_desired_tempWstatus);
            *extern_c_api::IN_upper_desired_tempWstatus.lock().unwrap() = Some(api_upper_desired_tempWstatus);
            *extern_c_api::IN_current_tempWstatus.lock().unwrap() = Some(api_current_tempWstatus);
            
            // [SetInStateVars]: set the pre-state values of state variables
            mri.lock().unwrap().example_state_variable = Old_example_state_variable;

            // [InvokeEntryPoint]: invoke the entry point test method
            mri.lock().unwrap().timeTriggered(&mut compute_api.lock().unwrap());

            // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
            let api_regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
            let api_displayed_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
            let api_upper_desired_temp = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
            let api_lower_desired_temp = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
            let api_interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
            let example_state_variable = mri.lock().unwrap().example_state_variable;

            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                api_current_tempWstatus,
                api_lower_desired_tempWstatus, 
                api_regulator_mode, 
                api_upper_desired_tempWstatus,
                api_displayed_temp, 
                api_interface_failure, 
                api_lower_desired_temp,
                api_regulator_status, 
                api_upper_desired_temp));

            // example of manual testing
            assert!(Old_example_state_variable == example_state_variable);
            assert!(!api_interface_failure.flag);
            assert!(api_regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status);
            assert!(api_displayed_temp.degrees == api_current_tempWstatus.degrees);
            assert!(api_lower_desired_temp.degrees == api_lower_desired_tempWstatus.degrees);
            assert!(api_upper_desired_temp.degrees == api_upper_desired_tempWstatus.degrees);
        } 
        
    }

    #[test]
    #[serial]
    fn test_compute_failed() {
        // generate values for the incoming ports and state variables
        let api_current_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 99, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let api_lower_desired_tempWstatus = 
            isolette_Isolette_Data_Model_TempWstatus_i {degrees: 98, status: isolette_Isolette_Data_Model_ValueStatus_Type::Invalid};
        let api_regulator_mode = isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode;
        let api_upper_desired_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 101, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let Old_example_state_variable: u32 = 19;

        // [CheckPre]: check/filter based on pre-condition.
        if(!GUMBOX::compute_CEP_Pre(
            api_current_tempWstatus, 
            api_lower_desired_tempWstatus, 
            api_regulator_mode, 
            api_upper_desired_tempWstatus)) {

            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }
        } else {
            // [PutInPorts]: put values on the input ports
            *extern_c_api::IN_regulator_mode.lock().unwrap() = Some(api_regulator_mode);
            *extern_c_api::IN_lower_desired_tempWstatus.lock().unwrap() = Some(api_lower_desired_tempWstatus);
            *extern_c_api::IN_upper_desired_tempWstatus.lock().unwrap() = Some(api_upper_desired_tempWstatus);
            *extern_c_api::IN_current_tempWstatus.lock().unwrap() = Some(api_current_tempWstatus);

            // [SetInStateVars]: set the pre-state values of state variables
            mri.lock().unwrap().example_state_variable = Old_example_state_variable;

            // [InvokeEntryPoint]: invoke the entry point test method
            mri.lock().unwrap().timeTriggered(&mut compute_api.lock().unwrap());

            // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable// get values of outgoing ports
            let api_regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
            let api_displayed_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
            let api_upper_desired_temp = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
            let api_lower_desired_temp = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
            let api_interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
            let example_state_variable = mri.lock().unwrap().example_state_variable;

            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                api_current_tempWstatus,
                api_lower_desired_tempWstatus, 
                api_regulator_mode, 
                api_upper_desired_tempWstatus,
                api_displayed_temp, 
                api_interface_failure, 
                api_lower_desired_temp,
                api_regulator_status, 
                api_upper_desired_temp));

            // example of manual testing
            assert!(Old_example_state_variable == example_state_variable);
            assert!(api_interface_failure.flag);
            assert!(api_regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status);
            assert!(api_displayed_temp.degrees == 99);
            assert!(api_lower_desired_temp.degrees == isolette_Isolette_Data_Model_Temp_i::default().degrees);
            assert!(api_upper_desired_temp.degrees == isolette_Isolette_Data_Model_Temp_i::default().degrees);
        }  
    }

}