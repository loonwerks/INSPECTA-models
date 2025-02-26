#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use crate::types::sb_aadl_types::*;
use super::manage_regulator_interface_api::*;

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct Manage_Regulator_Interface {
    pub example_state_variable: u32
}

impl Manage_Regulator_Interface {

    pub fn new() -> Self {
        Self { example_state_variable: 0 }
    }

    // not modeling this
    //   fn ROUND(num: Base_Types.Float_32): Base_Types.Float_32 = num

   pub fn initialise<API: Manage_Regulator_Interface_Put_Api>(&mut self, api: &mut Manage_Regulator_Interface_Application_Api<API>)
      {
        api.put_regulator_status(isolette_Isolette_Data_Model_Status_Type::Init_Status);
        //api.put_regulator_status(isolette_Isolette_Data_Model_Status_Type::Failed_Status); // seeded bug
        
        self.example_state_variable = 2001; // should set any state variables

        api.put_displayed_temp(isolette_Isolette_Data_Model_Temp_i::default());
        api.put_interface_failure(isolette_Isolette_Data_Model_Failure_Flag_i::default());
        api.put_lower_desired_temp(isolette_Isolette_Data_Model_Temp_i::default());
        api.put_upper_desired_temp(isolette_Isolette_Data_Model_Temp_i::default());
    }

    pub fn timeTriggered<API: Manage_Regulator_Interface_Full_Api>(&mut self, api: &mut Manage_Regulator_Interface_Application_Api<API>)
       {
        let lower: isolette_Isolette_Data_Model_TempWstatus_i = api.get_lower_desired_tempWstatus();
        let upper: isolette_Isolette_Data_Model_TempWstatus_i = api.get_upper_desired_tempWstatus();
        let regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type = api.get_regulator_mode();
        let current_temp: isolette_Isolette_Data_Model_TempWstatus_i = api.get_current_tempWstatus();

        info!("current_temp = {current_temp:?}");

        #[allow(unused_assignments)]
        let mut regulator_status: isolette_Isolette_Data_Model_Status_Type = isolette_Isolette_Data_Model_Status_Type::Init_Status;

        match regulator_mode {
            // INIT Mode
            isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode => {
                // REQ-MRI-1
                regulator_status = isolette_Isolette_Data_Model_Status_Type::Init_Status;
            }

            // NORMAL Mode
            isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode => {
                // REQ-MRI-2
                regulator_status = isolette_Isolette_Data_Model_Status_Type::On_Status;
            }

            // FAILED Mode
            isolette_Isolette_Data_Model_Regulator_Mode_Type::Failed_Regulator_Mode => {
                // REQ-MRI-3    
                regulator_status = isolette_Isolette_Data_Model_Status_Type::Failed_Status;
            }
        }
        //self.example_state_variable = 1; // seeded bug

        api.put_regulator_status(regulator_status);

        // =============================================
        //  Set values for Display Temperature (Table A-6)
        // =============================================

        // Latency: < Max Operator Response Time
        // Tolerance: +/- 0.6 degrees F
        //

        let mut display_temperature: isolette_Isolette_Data_Model_Temp_i = isolette_Isolette_Data_Model_Temp_i::default();

        match regulator_mode {
            // NORMAL mode
            isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode => {
                // REQ-MRI-4
                display_temperature = isolette_Isolette_Data_Model_Temp_i { degrees: current_temp.degrees };
            }

            // INIT, FAILED Modes

            // REQ-MRI
            //
            // leave the current temperature set to the default temperature value above.
            // This fulfills the requirement since the value when the Regulator Mode is not NORMAL
            // is unspecified.

            isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode => {} // do nothing
            isolette_Isolette_Data_Model_Regulator_Mode_Type::Failed_Regulator_Mode => {} // do nothing
        }

        api.put_displayed_temp(display_temperature);


        // =============================================
        //  Set values for Regulator Interface Failure internal variable
        // =============================================

        // The interface_failure status defaults to TRUE (i.e., failing), which is the safe modality.
        #[allow(unused_assignments)]
        let mut interface_failure: bool = true;

        // Extract the value status from both the upper and lower alarm range
        let upper_desired_temp_status: isolette_Isolette_Data_Model_ValueStatus_Type = upper.status;
        let lower_desired_temp_status: isolette_Isolette_Data_Model_ValueStatus_Type = lower.status;

        // Set the Monitor Interface Failure value based on the status values of the
        //   upper and lower temperature

        // TODO this version yields 
        //   "error: The verifier does not yet support the following Rust feature: ==/!= for non smt equality types"
        /*
        if !(upper_desired_temp_status == isolette_Isolette_Data_Model_ValueStatus_Type::Valid) ||
            !(lower_desired_temp_status == isolette_Isolette_Data_Model_ValueStatus_Type::Valid) {
            // REQ-MRI-6
            interface_failure = true;
        } else {
            // REQ-MRI-7
            interface_failure = false;
        }
        */

        match (upper_desired_temp_status, lower_desired_temp_status) {
            (isolette_Isolette_Data_Model_ValueStatus_Type::Invalid, _) |
            (_, isolette_Isolette_Data_Model_ValueStatus_Type::Invalid) 
              => interface_failure = true,
            _ => interface_failure = false
        }

        // create the appropriately typed value to send on the output port and set the port value
        let interface_failure_flag = isolette_Isolette_Data_Model_Failure_Flag_i { flag: interface_failure };
        api.put_interface_failure(interface_failure_flag);

        // =============================================
        //  Set values for Desired Range internal variable
        // =============================================

        if !interface_failure {
            // REQ-MRI-8
            api.put_lower_desired_temp(isolette_Isolette_Data_Model_Temp_i { degrees: lower.degrees } );
            api.put_upper_desired_temp(isolette_Isolette_Data_Model_Temp_i { degrees: upper.degrees } );

        } else {
            // REQ-MRI-9
            api.put_lower_desired_temp(isolette_Isolette_Data_Model_Temp_i::default() );
            api.put_upper_desired_temp(isolette_Isolette_Data_Model_Temp_i::default() );
        }
    }
    

}
