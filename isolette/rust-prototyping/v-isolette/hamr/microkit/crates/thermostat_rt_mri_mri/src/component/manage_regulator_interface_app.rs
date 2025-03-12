#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use vstd::prelude::*;

use crate::data::Isolette_Data_Model;
use crate::data::Isolette_Data_Model::*;
use crate::bridge::manage_regulator_interface_api::*;

#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

verus! {

pub struct Manage_Regulator_Interface {
    pub example_state_variable: i32
}

impl Manage_Regulator_Interface {

    pub const fn new() -> Self {
        Self { example_state_variable: 0 }
    }

    // not modeling this
    //   fn ROUND(num: Bases.Float_32): Bases.Float_32 = num

   pub fn initialise<API: Manage_Regulator_Interface_Put_Api>(&mut self, api: &mut Manage_Regulator_Interface_Application_Api<API>)
        ensures
            // guarantee RegulatorStatusIsInitiallyInit
            api.regulator_status == Isolette_Data_Model::Status::Init_Status {

        api.put_regulator_status(Status::Init_Status);
        //api.put_regulator_status(Status::Failed_Status); // seeded bug
        
        self.example_state_variable = 2001; // should set any state variables

        api.put_displayed_temp(Temp_i::default());
        api.put_interface_failure(Failure_Flag_i::default());
        api.put_lower_desired_temp(Temp_i::default());
        api.put_upper_desired_temp(Temp_i::default());
    }

    pub fn timeTriggered<API: Manage_Regulator_Interface_Full_Api>(&mut self, api: &mut Manage_Regulator_Interface_Application_Api<API>)
        requires
            // assume lower_is_not_higher_than_upper
            old(api).lower_desired_tempWstatus.degrees <= old(api).upper_desired_tempWstatus.degrees
        ensures
            // BEGIN COMPUTE ENSURES timeTriggered
            // case REQ_MRI_1
            //   If the Regulator Mode is INIT,
            //   the Regulator Status shall be set to Init.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
            (api.regulator_mode == Regulator_Mode::Init_Regulator_Mode) ==> 
                (api.regulator_status == Status::Init_Status),
            // case REQ_MRI_2
            //   If the Regulator Mode is NORMAL,
            //   the Regulator Status shall be set to On
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
            (api.regulator_mode == Regulator_Mode::Normal_Regulator_Mode) ==> 
                (api.regulator_status == Status::On_Status),
            // case REQ_MRI_3
            //   If the Regulator Mode is FAILED,
            //   the Regulator Status shall be set to Failed.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
            (api.regulator_mode == Regulator_Mode::Failed_Regulator_Mode) ==>
                (api.regulator_status == Status::Failed_Status),
            // case REQ_MRI_4
            //   If the Regulator Mode is NORMAL, the
            //   Display Temperature shall be set to the value of the
            //   Current Temperature rounded to the nearest integer.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (api.regulator_mode == Regulator_Mode::Normal_Regulator_Mode) ==>
                //(api.displayed_temp.degrees == Manage_Regulator_Interface_impl_thermostat_regulate_temperature_manage_regulator_interface.ROUND(api.current_tempWstatus.value)),
                (api.displayed_temp.degrees == api.current_tempWstatus.degrees),
            // case REQ_MRI_5
            //   If the Regulator Mode is not NORMAL,
            //   the value of the Display Temperature is UNSPECIFIED.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (true) ==> (true),
            // case REQ_MRI_6
            //   If the Status attribute of the Lower Desired Temperature
            //   or the Upper Desired Temperature is Invalid,
            //   the Regulator Interface Failure shall be set to True.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (api.upper_desired_tempWstatus.status != ValueStatus::Valid ||
             api.upper_desired_tempWstatus.status == ValueStatus::Invalid) ==>
                    (api.interface_failure.flag),
            // case REQ_MRI_7
            //   If the Status attribute of the Lower Desired Temperature
            //   and the Upper Desired Temperature is Valid,
            //   the Regulator Interface Failure shall be set to False.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (true) ==> 
                (api.interface_failure.flag == 
                    !(api.upper_desired_tempWstatus.status == ValueStatus::Valid &&
                      api.lower_desired_tempWstatus.status == ValueStatus::Valid)),
            // case REQ_MRI_8
            //   If the Regulator Interface Failure is False,
            //   the Desired Range shall be set to the Desired Temperature Range.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (true) ==>
                (!(api.interface_failure.flag) ==>
                    (api.lower_desired_temp.degrees == api.lower_desired_tempWstatus.degrees &&
                     api.upper_desired_temp.degrees == api.upper_desired_tempWstatus.degrees)),
            // case REQ_MRI_9
            //   If the Regulator Interface Failure is True,
            //   the Desired Range is UNSPECIFIED.
            //   the Desired Range shall be set to the Desired Temperature Range.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (true) ==> (true)
            // END COMPUTE ENSURES timeTriggered
       {
        let lower: TempWstatus_i = api.get_lower_desired_tempWstatus();
        let upper: TempWstatus_i = api.get_upper_desired_tempWstatus();
        let regulator_mode: Regulator_Mode = api.get_regulator_mode();
        let current_temp: TempWstatus_i = api.get_current_tempWstatus();

        #[cfg(feature = "sel4")] 
        info!("{current_temp:?}");

        #[allow(unused_assignments)]
        let mut regulator_status: Status = Status::Init_Status;

        match regulator_mode {
            // INIT Mode
            Regulator_Mode::Init_Regulator_Mode => {
                // REQ-MRI-1
                regulator_status = Status::Init_Status;
            }

            // NORMAL Mode
            Regulator_Mode::Normal_Regulator_Mode => {
                // REQ-MRI-2
                regulator_status = Status::On_Status;
            }

            // FAILED Mode
            Regulator_Mode::Failed_Regulator_Mode => {
                // REQ-MRI-3    
                regulator_status = Status::Failed_Status;
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

        let mut display_temperature: Temp_i = Temp_i::default();

        match regulator_mode {
            // NORMAL mode
            Regulator_Mode::Normal_Regulator_Mode => {
                // REQ-MRI-4
                display_temperature = Temp_i { degrees: current_temp.degrees };
            }

            // INIT, FAILED Modes

            // REQ-MRI
            //
            // leave the current temperature set to the default temperature value above.
            // This fulfills the requirement since the value when the Regulator Mode is not NORMAL
            // is unspecified.

            Regulator_Mode::Init_Regulator_Mode => {} // do nothing
            Regulator_Mode::Failed_Regulator_Mode => {} // do nothing
        }

        api.put_displayed_temp(display_temperature);


        // =============================================
        //  Set values for Regulator Interface Failure internal variable
        // =============================================

        // The interface_failure status defaults to TRUE (i.e., failing), which is the safe modality.
        #[allow(unused_assignments)]
        let mut interface_failure: bool = true;

        // Extract the value status from both the upper and lower alarm range
        let upper_desired_temp_status: ValueStatus = upper.status;
        let lower_desired_temp_status: ValueStatus = lower.status;

        // Set the Monitor Interface Failure value based on the status values of the
        //   upper and lower temperature
        
        if !(upper_desired_temp_status == ValueStatus::Valid) ||
            !(lower_desired_temp_status == ValueStatus::Valid) {
            // REQ-MRI-6
            interface_failure = true;
        } else {
            // REQ-MRI-7
            interface_failure = false;
        }
            
        /* alt version using match rather than if/else
        match (upper_desired_temp_status, lower_desired_temp_status) {
            (ValueStatus::Invalid, _) |
            (_, ValueStatus::Invalid) 
              => interface_failure = true,
            _ => interface_failure = false
        }
        */

        // create the appropriately typed value to send on the output port and set the port value
        let interface_failure_flag = Failure_Flag_i { flag: interface_failure };
        api.put_interface_failure(interface_failure_flag);

        // =============================================
        //  Set values for Desired Range internal variable
        // =============================================

        if !interface_failure {
            // REQ-MRI-8
            api.put_lower_desired_temp(Temp_i { degrees: lower.degrees } );
            api.put_upper_desired_temp(Temp_i { degrees: upper.degrees } );

        } else {
            // REQ-MRI-9
            api.put_lower_desired_temp(Temp_i::default() );
            api.put_upper_desired_temp(Temp_i::default() );
        }
    }
    

}

}