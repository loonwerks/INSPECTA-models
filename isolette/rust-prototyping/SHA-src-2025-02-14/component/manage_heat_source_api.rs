use vstd::prelude::*;
use vstd::invariant::*;

use crate::{art, data};

verus! {

pub trait Manage_Heat_Source_i_Api {}

pub trait Manage_Heat_Source_i_Put_Api: Manage_Heat_Source_i_Api {
    // fn put_current_tempWstatus(&mut self, value: data::TempWstatus_i);
    // fn put_lower_desired_temp(&mut self, value: data::Temp_i);
    // fn put_upper_desired_temp(&mut self, value: data::Temp_i);
    // fn put_regulator_mode(&mut self, value: data::Regulator_Mode);
    #[verifier::external_body]
    fn put_heat_control_unverified(&mut self, value: data::OnOff) {
        super::extern_api::unsafe_put_heat_control(&value);
    }
}

pub trait Manage_Heat_Source_i_Get_Api: Manage_Heat_Source_i_Api {
    #[verifier::external_body]
    fn get_current_tempWstatus_unverified(&self, value: &Ghost<data::TempWstatus_i>) -> (res: data::TempWstatus_i)
        ensures res.degrees == value@.degrees && res.status == value@.status
    {
        super::extern_api::unsafe_get_current_tempWstatus()
    }
    #[verifier::external_body]
    fn get_lower_desired_temp_unverified(&self, value: &Ghost<data::Temp_i>) -> (res: data::Temp_i)
        ensures res.degrees == value@.degrees
    {
        super::extern_api::unsafe_get_lower_desired_temp()
    }
    #[verifier::external_body]
    fn get_upper_desired_temp_unverified(&self, value: &Ghost<data::Temp_i>) -> (res: data::Temp_i)
        ensures res.degrees == value@.degrees
    {
        super::extern_api::unsafe_get_upper_desired_temp()
    }
    #[verifier::external_body]
    fn get_regulator_mode_unverified(&self, value: &Ghost<data::Regulator_Mode>) -> (res: data::Regulator_Mode)
        ensures res == value@
    {
        super::extern_api::unsafe_get_regulator_mode()
    }
    // fn get_heat_control(&self) -> Option<data::OnOff>;
}

pub trait Manage_Heat_Source_i_Full_Api: Manage_Heat_Source_i_Get_Api + Manage_Heat_Source_i_Put_Api {}

pub struct Manage_Heat_Source_i_Application_Api<API: Manage_Heat_Source_i_Api> {
    pub api: API,
    pub ghost heat_control: data::OnOff,
    pub ghost current_tempWstatus: data::TempWstatus_i,
    pub ghost lower_desired_temp: data::Temp_i,
    pub ghost upper_desired_temp: data::Temp_i,
    pub ghost regulator_mode: data::Regulator_Mode
}

impl<API: Manage_Heat_Source_i_Put_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn put_heat_control(&mut self, value : data::OnOff)
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
    pub fn get_current_tempWstatus(&mut self) -> (res: data::TempWstatus_i)
        ensures res == self.current_tempWstatus
        && old(self).heat_control == self.heat_control
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_current_tempWstatus_unverified(&Ghost(self.current_tempWstatus));
        data
    }

    pub fn get_lower_desired_temp(&mut self) -> (res: data::Temp_i)
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

    pub fn get_upper_desired_temp(&mut self) -> (res: data::Temp_i)
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

    pub fn get_regulator_mode(&mut self) -> (res: data::Regulator_Mode)
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

}