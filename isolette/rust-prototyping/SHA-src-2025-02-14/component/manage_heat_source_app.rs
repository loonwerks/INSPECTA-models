use vstd::prelude::*;
use vstd::invariant::*;

use crate::data;
use super::manage_heat_source_api::*;

verus! {

struct Manage_Heat_Source_i_thermostat_rt {
    pub lastCmd: data::OnOff
}

impl Manage_Heat_Source_i_thermostat_rt {
    fn new() -> Self {
        Self { lastCmd: data::OnOff::Off }
    }

    fn initialise<API: Manage_Heat_Source_i_Put_Api>(&mut self, api: &mut Manage_Heat_Source_i_Application_Api<API>)
        ensures
            self.lastCmd == data::OnOff::Off
        &&  api.heat_control == data::OnOff::Off
    {
        self.lastCmd = data::OnOff::Off;
        // REQ-MHS-1: If the Regulator Mode is INIT, the Heat Control shall be
        // set to Off
        let currentCmd = data::OnOff::Off;
        api.put_heat_control(currentCmd)
    }

    fn timeTriggered<API: Manage_Heat_Source_i_Full_Api>(&mut self, api: &mut Manage_Heat_Source_i_Application_Api<API>)
        // The requires clause represents a system level invariant. Neither of the variables is modified by
        // function `timeTriggered`.
        requires
            old(api).lower_desired_temp.degrees <= old(api).upper_desired_temp.degrees
        ensures
            // guarantee lastCmd
            //   Set lastCmd to value of output Cmd port
            (self.lastCmd == api.heat_control)
        &&  // case REQ_MHS_1
            //   If the Regulator Mode is INIT, the Heat Control shall be
            //   set to Off.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == data::Regulator_Mode::Init_Regulator_Mode) ==> (api.heat_control == data::OnOff::Off))
        &&  // case REQ_MHS_2
            //  If the Regulator Mode is NORMAL and the Current Temperature is less than
            //  the Lower Desired Temperature, the Heat Control shall be set to On.
            //  http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == data::Regulator_Mode::Normal_Regulator_Mode &&
                api.current_tempWstatus.degrees < api.lower_desired_temp.degrees) ==> (api.heat_control == data::OnOff::Onn))
        &&  // case REQ_MHS_3
            //   If the Regulator Mode is NORMAL and the Current Temperature is greater than
            //   the Upper Desired Temperature, the Heat Control shall be set to Off.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == data::Regulator_Mode::Normal_Regulator_Mode &&
                api.current_tempWstatus.degrees > api.upper_desired_temp.degrees) ==> (api.heat_control == data::OnOff::Off))
        &&  // case REQ_MHS_4
            //   If the Regulator Mode is NORMAL and the Current
            //   Temperature is greater than or equal to the Lower Desired Temperature
            //   and less than or equal to the Upper Desired Temperature, the value of
            //   the Heat Control shall not be changed.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
            ((api.regulator_mode == data::Regulator_Mode::Normal_Regulator_Mode &&
                (api.current_tempWstatus.degrees >= api.lower_desired_temp.degrees &&
                  api.current_tempWstatus.degrees <= api.upper_desired_temp.degrees)) ==> (api.heat_control == old(self).lastCmd))
        &&  // case REQ_MHS_5
            //   If the Regulator Mode is FAILED, the Heat Control shall be
            //   set to Off.
            //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=111 
            ((api.regulator_mode == data::Regulator_Mode::Failed_Regulator_Mode) ==> (api.heat_control == data::OnOff::Off))
            // END COMPUTE ENSURES timeTriggered
     {
    
        // -------------- Get values of input ports ------------------
        let lower: data::Temp_i = api.get_lower_desired_temp(); // gives lower <= api.upper_desired_temp.degrees
        let upper: data::Temp_i = api.get_upper_desired_temp(); // gives api.lower_desired_temp.degrees <= upper
        assert(lower.degrees <= upper.degrees); 
        let regulator_mode: data::Regulator_Mode = api.get_regulator_mode();
        let currentTemp: data::TempWstatus_i = api.get_current_tempWstatus();
    
        //================ compute / control logic ===========================
    
        // current command defaults to value of last command (REQ-MHS-4)
        let mut currentCmd: data::OnOff = self.lastCmd;
    
        assert(lower.degrees == api.lower_desired_temp.degrees);
        assert(upper.degrees == api.upper_desired_temp.degrees);
        assert(lower.degrees <= upper.degrees);
        assert(regulator_mode == api.regulator_mode);
        assert(currentTemp.degrees == api.current_tempWstatus.degrees);
        match regulator_mode {
    
            // ----- INIT Mode --------
            data::Regulator_Mode::Init_Regulator_Mode => {
                // REQ-MHS-1
                currentCmd = data::OnOff::Off;
            },
    
            // ------ NORMAL Mode -------
            data::Regulator_Mode::Normal_Regulator_Mode => {
                if (currentTemp.degrees > upper.degrees) {
                    // REQ-MHS-3
                    currentCmd = data::OnOff::Off;
                } else if (currentTemp.degrees < lower.degrees) {
                    assert(api.current_tempWstatus.degrees < api.lower_desired_temp.degrees);
                    // REQ-MHS-2
                    //currentCmd = data::OnOff::Off; // seeded bug/error
                    currentCmd = data::OnOff::Onn;
                }
        },
    
          // otherwise currentCmd defaults to lastCmd (REQ-MHS-4)
    
            // ------ FAILED Mode -------
            data::Regulator_Mode::Failed_Regulator_Mode => {
                // REQ-MHS-5
                currentCmd = data::OnOff::Off;
            }
        }
    
        // -------------- Set values of output ports ------------------
        api.put_heat_control(currentCmd);
    
        //api.logInfo(s"Sent on currentCmd data port: $currentCmd")
    
        self.lastCmd = currentCmd
      }
    

}

}