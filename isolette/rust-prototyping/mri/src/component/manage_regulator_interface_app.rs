#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use vstd::prelude::*;

use crate::data::*;
use super::manage_regulator_interface_api::*;

verus! {

pub struct Manage_Regulator_Interface {

}

impl Manage_Regulator_Interface {

    // not modeling this
    //   @strictpure def ROUND(num: Base_Types.Float_32): Base_Types.Float_32 = num

   pub fn initialise<API: Manage_Regulator_Interface_Put_Api>(&mut self, api: &mut Manage_Regulator_Interface_Application_Api<API>)
        ensures
            // guarantee RegulatorStatusIsInitiallyInit
            api.regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status {

        api.put_regulator_status(isolette_Isolette_Data_Model_Status_Type::Init_Status);
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
            (api.regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode) ==> 
                (api.regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status),
            // case REQ_MRI_2
            //   If the Regulator Mode is NORMAL,
            //   the Regulator Status shall be set to On
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
            (api.regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode) ==> 
                (api.regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status),
            // case REQ_MRI_3
            //   If the Regulator Mode is FAILED,
            //   the Regulator Status shall be set to Failed.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
            (api.regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Failed_Regulator_Mode) ==>
                (api.regulator_status == isolette_Isolette_Data_Model_Status_Type::Failed_Status),
            // case REQ_MRI_4
            //   If the Regulator Mode is NORMAL, the
            //   Display Temperature shall be set to the value of the
            //   Current Temperature rounded to the nearest integer.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (api.regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode) ==>
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
            (api.upper_desired_tempWstatus.status != isolette_Isolette_Data_Model_ValueStatus_Type::Valid ||
             api.upper_desired_tempWstatus.status == isolette_Isolette_Data_Model_ValueStatus_Type::Invalid) ==>
                    (api.interface_failure.flag),
            // case REQ_MRI_7
            //   If the Status attribute of the Lower Desired Temperature
            //   and the Upper Desired Temperature is Valid,
            //   the Regulator Interface Failure shall be set to False.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
            (true) ==> 
                (api.interface_failure.flag == 
                    !(api.upper_desired_tempWstatus.status == isolette_Isolette_Data_Model_ValueStatus_Type::Valid &&
                      api.lower_desired_tempWstatus.status == isolette_Isolette_Data_Model_ValueStatus_Type::Valid)),
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
        let lower: isolette_Isolette_Data_Model_TempWstatus_i = api.get_lower_desired_tempWstatus();
        let upper: isolette_Isolette_Data_Model_TempWstatus_i = api.get_upper_desired_tempWstatus();
        let regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type = api.get_regulator_mode();
        let current_temp: isolette_Isolette_Data_Model_TempWstatus_i = api.get_current_tempWstatus();
        

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

}