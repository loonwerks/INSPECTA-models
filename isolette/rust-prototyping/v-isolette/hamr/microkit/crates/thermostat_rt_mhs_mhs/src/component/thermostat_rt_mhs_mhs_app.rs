#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use vstd::prelude::*;

use crate::data::Isolette_Data_Model::*;
use crate::bridge::manage_heat_source_api::*;

#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

verus! {

pub struct Manage_Heat_Source_i_thermostat_rt {
    pub lastCmd: On_Off
}

impl Manage_Heat_Source_i_thermostat_rt {
    pub const fn new() -> Self {
        Self { lastCmd: On_Off::Off }
    }

    pub fn initialise<API: Manage_Heat_Source_i_Put_Api>(&mut self, api: &mut Manage_Heat_Source_i_Application_Api<API>)
        ensures
            self.lastCmd == On_Off::Off
        &&  api.heat_control == On_Off::Off
    {
        self.lastCmd = On_Off::Off;
        // REQ-MHS-1: If the Regulator Mode is INIT, the Heat Control shall be
        // set to Off
        let currentCmd = On_Off::Off;
        api.put_heat_control(currentCmd)
    }

    pub fn timeTriggered<API: Manage_Heat_Source_i_Full_Api>(&mut self, api: &mut Manage_Heat_Source_i_Application_Api<API>)
        // The requires clause represents a system level invariant. Neither of the variables is modified by
        // function `timeTriggered`.
        requires
            old(api).lower_desired_temp.degrees <= old(api).upper_desired_temp.degrees
        ensures
            // guarantee lastCmd
            //   Set lastCmd to value of output Cmd port
            (self.lastCmd == api.heat_control),
            // case REQ_MHS_1
            //   If the Regulator Mode is INIT, the Heat Control shall be
            //   set to Off.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == Regulator_Mode::Init_Regulator_Mode) ==> (api.heat_control == On_Off::Off)),
            // case REQ_MHS_2
            //  If the Regulator Mode is NORMAL and the Current Temperature is less than
            //  the Lower Desired Temperature, the Heat Control shall be set to On.
            //  http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == Regulator_Mode::Normal_Regulator_Mode &&
                api.current_tempWstatus.degrees < api.lower_desired_temp.degrees) ==> (api.heat_control == On_Off::Onn)),
            // case REQ_MHS_3
            //   If the Regulator Mode is NORMAL and the Current Temperature is greater than
            //   the Upper Desired Temperature, the Heat Control shall be set to Off.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == Regulator_Mode::Normal_Regulator_Mode &&
                api.current_tempWstatus.degrees > api.upper_desired_temp.degrees) ==> (api.heat_control == On_Off::Off)),
            // case REQ_MHS_4
            //   If the Regulator Mode is NORMAL and the Current
            //   Temperature is greater than or equal to the Lower Desired Temperature
            //   and less than or equal to the Upper Desired Temperature, the value of
            //   the Heat Control shall not be changed.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == Regulator_Mode::Normal_Regulator_Mode &&
                (api.current_tempWstatus.degrees >= api.lower_desired_temp.degrees &&
                  api.current_tempWstatus.degrees <= api.upper_desired_temp.degrees)) ==> (api.heat_control == old(self).lastCmd)),
            // case REQ_MHS_5
            //   If the Regulator Mode is FAILED, the Heat Control shall be
            //   set to Off.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=111 
            ((api.regulator_mode == Regulator_Mode::Failed_Regulator_Mode) ==> (api.heat_control == On_Off::Off))
            // END COMPUTE ENSURES timeTriggered
     {
    
        // -------------- Get values of input ports ------------------
        let lower: Temp_i = api.get_lower_desired_temp(); // gives lower <= api.upper_desired_temp.degrees
        let upper: Temp_i = api.get_upper_desired_temp(); // gives api.lower_desired_temp.degrees <= upper

        let regulator_mode: Regulator_Mode = api.get_regulator_mode();
        let currentTemp: TempWstatus_i = api.get_current_tempWstatus();
    
        //================ compute / control logic ===========================
    
        // current command defaults to value of last command (REQ-MHS-4)
        let mut currentCmd: On_Off = self.lastCmd;
    
        // Illustrations of appropriate Verus verification -- all of the asserts below are verified by Verus
        // assert(lower.degrees == api.lower_desired_temp.degrees);
        // assert(upper.degrees == api.upper_desired_temp.degrees);
        // assert(lower.degrees <= upper.degrees);
        // assert(regulator_mode == api.regulator_mode);
        // assert(currentTemp.degrees == api.current_tempWstatus.degrees);
        match regulator_mode {
    
            // ----- INIT Mode --------
            Regulator_Mode::Init_Regulator_Mode => {
                // REQ-MHS-1
                currentCmd = On_Off::Off;
            },
    
            // ------ NORMAL Mode -------
            Regulator_Mode::Normal_Regulator_Mode => {
                if (currentTemp.degrees > upper.degrees) {
                    // REQ-MHS-3
                    currentCmd = On_Off::Off;
                } else if (currentTemp.degrees < lower.degrees) {
                    assert(api.current_tempWstatus.degrees < api.lower_desired_temp.degrees);
                    // REQ-MHS-2
                    //currentCmd = On_Off::Off; // seeded bug/error
                    currentCmd = On_Off::Onn;
                }
                // otherwise currentCmd defaults to lastCmd (REQ-MHS-4)
            },

            // ------ FAILED Mode -------
            Regulator_Mode::Failed_Regulator_Mode => {
                // REQ-MHS-5
                currentCmd = On_Off::Off;
            }
        }
    
        // -------------- Set values of output ports ------------------
        api.put_heat_control(currentCmd);
    
        #[cfg(feature = "sel4")]
        info!("Sent {currentCmd:?}");

        //api.logInfo(s"Sent on currentCmd data port: $currentCmd")
    
        self.lastCmd = currentCmd
      }
    

}

}