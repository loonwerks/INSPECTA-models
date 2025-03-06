#![cfg_attr(not(test), no_std)]

#![allow(non_camel_case_types)]
#![allow(non_upper_case_globals)]
#![allow(non_snake_case)]
#![allow(unused_parens)]
#![allow(unused_imports)]
#![allow(static_mut_refs)]

mod art;
mod extern_c_api;
mod component;
mod logging;

use crate::component::manage_regulator_interface_app::*;
use crate::component::manage_regulator_interface_api::*;

use types::Isolette_Data_Model::*;
use types::sb_microkit_types::*;

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

//use std::sync::Mutex;

// Version 1
// TODO: verus complains "use of undeclared crate or module `once_cell`" for the following and
//       seems to ignore the verifier attributes
//#[verifier::external]
//static mri: once_cell::sync::Lazy<Mutex<Manage_Regulator_Interface>> = once_cell::sync::Lazy::new(|| Mutex::new(Manage_Regulator_Interface::new()));
//#[verifier::external]
//static init_api: once_cell::sync::Lazy<Mutex<Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api>>> = once_cell::sync::Lazy::new(|| Mutex::new(Manage_Regulator_Interface_Application_Api::new(MRI_Initialization_Api {})));
//#[verifier::external]
//static compute_api: once_cell::sync::Lazy<Mutex<Manage_Regulator_Interface_Application_Api<MRI_Compute_Api>>> = once_cell::sync::Lazy::new(|| Mutex::new(Manage_Regulator_Interface_Application_Api::new(MRI_Compute_Api {})));

// Version 2
// TODO: verus complains "use of undeclared crate or module `lazy_static`" and seems to
//       ignore the verifier attribute
//#[verifier::external]
//lazy_static::lazy_static! { 
//    static ref mri: Mutex<Manage_Regulator_Interface> = Mutex::new(Manage_Regulator_Interface::new());
//    static ref init_api: Mutex<Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api>> = Mutex::new(Manage_Regulator_Interface_Application_Api::new(MRI_Initialization_Api {}));
//    static ref compute_api: Mutex<Manage_Regulator_Interface_Application_Api<MRI_Compute_Api>> = Mutex::new(Manage_Regulator_Interface_Application_Api::new(MRI_Compute_Api {}));
//}

// Version 3
// TODO: this requires all useages to be wrapped in unsafe
static mut mri: Manage_Regulator_Interface = Manage_Regulator_Interface{example_state_variable: 0};
static mut init_api: Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api> = Manage_Regulator_Interface_Application_Api { 
    api: MRI_Initialization_Api {},
   };
static mut compute_api: Manage_Regulator_Interface_Application_Api<MRI_Compute_Api> = Manage_Regulator_Interface_Application_Api { 
    api: MRI_Compute_Api {},
   };

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_initialize() {
  logging::LOGGER.set().unwrap();

  info!("initialize invoked!");

  //mri.lock().unwrap().initialise(&mut init_api.lock().unwrap());

  unsafe {
    mri.initialise(&mut init_api);
  }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_timeTriggered() {
    info!("timeTriggered invoked!");

    //mri.lock().unwrap().timeTriggered(&mut compute_api.lock().unwrap());

    unsafe {
        mri.timeTriggered(&mut compute_api);
    }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_notify(channel: microkit_channel) {
  // this method is called when the monitor does not handle the passed in channel
  match channel {
      _ => {
        //warn!("Unexpected channel {}", channel)
        }
  }
}

// Need a Panic handler in a no_std environment
#[cfg(not(test))]
#[panic_handler]
fn panic(info: &core::panic::PanicInfo) -> ! {
  error!("PANIC: {info:#?}");
  loop {}
}


#[cfg(test)]
mod tests {
    use serial_test::serial;
    use super::*;
    use types::Isolette_Data_Model::*;
    use crate::extern_c_api;
    use crate::component::manage_regulator_interface_GUMBOX as GUMBOX;
    
    const failOnUnsatPrecondition: bool = false;

    #[test]
    #[serial]
    fn test_initialization() {

        // [InvokeEntryPoint]: invoke the entry point test method
        //mri.lock().unwrap().initialise(&mut init_api.lock().unwrap());

        unsafe {
            mri.initialise(&mut init_api);
        }

        // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
        let regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
        let display_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
        let upper_desired = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
        let lower_desired = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
        let interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
        //let example_state_variable = mri.lock().unwrap().example_state_variable;

        unsafe {
            let example_state_variable = mri.example_state_variable;

            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::inititialize_IEP_Post(
                display_temp,
                interface_failure,
                lower_desired,
                regulator_status,
                upper_desired));

            // example of manual testing
            assert!(regulator_status == Status::Init_Status);
            assert!(display_temp == Temp_i::default());
            assert!(upper_desired == Temp_i::default());
            assert!(lower_desired == Temp_i::default());
            assert!(interface_failure == Failure_Flag_i::default());
            assert!(example_state_variable == 2001);
        }
    }

    #[test]
    #[serial]
    fn test_compute_normal() {
        // generate values for the incoming ports and state variables
        let api_current_tempWstatus = TempWstatus_i {degrees: 99, status: ValueStatus::Valid};
        let api_lower_desired_tempWstatus = TempWstatus_i {degrees: 98, status: ValueStatus::Valid};
        let api_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let api_upper_desired_tempWstatus = TempWstatus_i {degrees: 101, status: ValueStatus::Valid};
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
            //mri.lock().unwrap().example_state_variable = Old_example_state_variable;

            unsafe {
                mri.example_state_variable = Old_example_state_variable;

                // [InvokeEntryPoint]: invoke the entry point test method
                //mri.lock().unwrap().timeTriggered(&mut compute_api.lock().unwrap());
                mri.timeTriggered(&mut compute_api);

                // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
                let api_regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
                let api_displayed_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
                let api_upper_desired_temp = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
                let api_lower_desired_temp = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
                let api_interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
                //let example_state_variable = mri.lock().unwrap().example_state_variable;
                let example_state_variable = mri.example_state_variable;

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
                assert!(api_regulator_status == Status::On_Status);
                assert!(api_displayed_temp.degrees == api_current_tempWstatus.degrees);
                assert!(api_lower_desired_temp.degrees == api_lower_desired_tempWstatus.degrees);
                assert!(api_upper_desired_temp.degrees == api_upper_desired_tempWstatus.degrees);
            }
        } 
        
    }

    #[test]
    #[serial]
    fn test_compute_failed() {
        // generate values for the incoming ports and state variables
        let api_current_tempWstatus = TempWstatus_i {degrees: 99, status: ValueStatus::Valid};
        let api_lower_desired_tempWstatus = 
            TempWstatus_i {degrees: 98, status: ValueStatus::Invalid};
        let api_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let api_upper_desired_tempWstatus = TempWstatus_i {degrees: 101, status: ValueStatus::Valid};
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

            unsafe {
                // [SetInStateVars]: set the pre-state values of state variables
                //ri.lock().unwrap().example_state_variable = Old_example_state_variable;
                mri.example_state_variable = Old_example_state_variable;

                // [InvokeEntryPoint]: invoke the entry point test method
                //mri.lock().unwrap().timeTriggered(&mut compute_api.lock().unwrap());
                mri.timeTriggered(&mut compute_api);

                // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable// get values of outgoing ports
                let api_regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
                let api_displayed_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
                let api_upper_desired_temp = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
                let api_lower_desired_temp = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
                let api_interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
                //let example_state_variable = mri.lock().unwrap().example_state_variable;
                let example_state_variable = mri.example_state_variable;

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
                assert!(api_regulator_status == Status::On_Status);
                assert!(api_displayed_temp.degrees == 99);
                assert!(api_lower_desired_temp.degrees == Temp_i::default().degrees);
                assert!(api_upper_desired_temp.degrees == Temp_i::default().degrees);
            }
        }  
    }
}
