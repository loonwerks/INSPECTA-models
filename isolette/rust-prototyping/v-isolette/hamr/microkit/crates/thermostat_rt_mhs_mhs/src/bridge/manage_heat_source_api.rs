#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use vstd::prelude::*;
use crate::data::Isolette_Data_Model::*;
use super::extern_c_api as extern_api;

verus! {

pub trait Manage_Heat_Source_i_Api {}

pub trait Manage_Heat_Source_i_Put_Api: Manage_Heat_Source_i_Api {
    #[verifier::external_body]
    fn put_heat_control_unverified(&mut self, value: On_Off) {
        extern_api::unsafe_put_heat_control(&value);
    }
}

pub trait Manage_Heat_Source_i_Get_Api: Manage_Heat_Source_i_Api {
    #[verifier::external_body]
    fn get_current_tempWstatus_unverified(&self, value: &Ghost<TempWstatus_i>) -> (res: TempWstatus_i)
        ensures res.degrees == value@.degrees && res.status == value@.status
    {
        return extern_api::unsafe_get_current_tempWstatus()
    }

    #[verifier::external_body]
    fn get_lower_desired_temp_unverified(&self, value: &Ghost<Temp_i>) -> (res: Temp_i)
        ensures res.degrees == value@.degrees
    {
        return extern_api::unsafe_get_lower_desired_temp()
    }

    #[verifier::external_body]
    fn get_upper_desired_temp_unverified(&self, value: &Ghost<Temp_i>) -> (res: Temp_i)
        ensures res.degrees == value@.degrees
    {
        return extern_api::unsafe_get_upper_desired_temp()
    }

    #[verifier::external_body]
    fn get_regulator_mode_unverified(&self, value: &Ghost<Regulator_Mode>) -> (res: Regulator_Mode)
        ensures res == value@
    {
        return extern_api::unsafe_get_regulator_mode()
    }
}

pub trait Manage_Heat_Source_i_Full_Api: Manage_Heat_Source_i_Get_Api + Manage_Heat_Source_i_Put_Api {}

pub struct Manage_Heat_Source_i_Application_Api<API: Manage_Heat_Source_i_Api> {
    pub api: API,
    pub ghost heat_control: On_Off,

    pub ghost current_tempWstatus: TempWstatus_i,
    pub ghost lower_desired_temp: Temp_i,
    pub ghost upper_desired_temp: Temp_i,
    pub ghost regulator_mode: Regulator_Mode
}

impl <API: Manage_Heat_Source_i_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn new(api: API) -> Self {
        Self {
            api,

            heat_control: On_Off::Off,

            current_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
            lower_desired_temp: Temp_i { degrees: 0 },
            upper_desired_temp: Temp_i { degrees: 0 },
            regulator_mode: Regulator_Mode::Init_Regulator_Mode
        }
    }
}

impl<API: Manage_Heat_Source_i_Put_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn put_heat_control(&mut self, value : On_Off)
        ensures self.heat_control == value
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
        && old(self).regulator_mode == self.regulator_mode
    {
        self.api.put_heat_control_unverified(value);
        self.heat_control = value
    }
}

// The boundary to the infrastructure is modelled by passing the equally-named ghost variables,
// effectively saying that the values have already been read: The values in the ports correspond to
// those in the ghost variables.
impl<API: Manage_Heat_Source_i_Get_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn get_current_tempWstatus(&mut self) -> (res: TempWstatus_i)
        ensures res == self.current_tempWstatus
        && old(self).heat_control == self.heat_control
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_current_tempWstatus_unverified(&Ghost(self.current_tempWstatus));
        data
    }

    pub fn get_lower_desired_temp(&mut self) -> (res: Temp_i)
        ensures res == self.lower_desired_temp
        && old(self).heat_control == self.heat_control
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_lower_desired_temp_unverified(&Ghost(self.lower_desired_temp));
        data
    }

    pub fn get_upper_desired_temp(&mut self) -> (res: Temp_i)
        ensures res == self.upper_desired_temp
        && old(self).heat_control == self.heat_control
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_upper_desired_temp_unverified(&Ghost(self.upper_desired_temp));
        data
    }

    pub fn get_regulator_mode(&mut self) -> (res: Regulator_Mode)
        ensures res == self.regulator_mode
        && old(self).heat_control == self.heat_control
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
    {
        let data = self.api.get_regulator_mode_unverified(&Ghost(self.regulator_mode));
        self.regulator_mode = data;
        data
    }

}

pub struct MHS_Initialization_Api;
impl Manage_Heat_Source_i_Api for MHS_Initialization_Api {}
impl Manage_Heat_Source_i_Put_Api for MHS_Initialization_Api {}

pub const fn init_api() -> Manage_Heat_Source_i_Application_Api<MHS_Initialization_Api> {
    return Manage_Heat_Source_i_Application_Api {
        api: MHS_Initialization_Api {},
        
        heat_control: On_Off::Off,

        current_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
        lower_desired_temp: Temp_i {degrees: 0 },
        upper_desired_temp: Temp_i {degrees: 0 },
        regulator_mode: Regulator_Mode::Init_Regulator_Mode
    }
}

pub struct MHS_Compute_Api;
impl Manage_Heat_Source_i_Api for MHS_Compute_Api {}
impl Manage_Heat_Source_i_Put_Api for MHS_Compute_Api {}
impl Manage_Heat_Source_i_Get_Api for MHS_Compute_Api {}
impl Manage_Heat_Source_i_Full_Api for MHS_Compute_Api {}

pub const fn compute_api() -> Manage_Heat_Source_i_Application_Api<MHS_Compute_Api> {
    return Manage_Heat_Source_i_Application_Api {
        api: MHS_Compute_Api {},
        
        heat_control: On_Off::Off,

        current_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
        lower_desired_temp: Temp_i {degrees: 0 },
        upper_desired_temp: Temp_i {degrees: 0 },
        regulator_mode: Regulator_Mode::Init_Regulator_Mode
    }
}
}