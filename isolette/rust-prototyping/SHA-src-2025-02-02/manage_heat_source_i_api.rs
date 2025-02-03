use vstd::prelude::*;
use vstd::invariant::*;

use std::any::Any;

use crate::{art, data};

verus! {

pub struct Manage_Heat_Source_i_Api { // use trait to restrict init code to put-access, op to get-put-access
    id: art::BridgeId,
    current_tempWstatus_Id: art::PortId,
    lower_desired_temp_Id: art::PortId,
    upper_desired_temp_Id: art::PortId,
    regulator_mode_Id: art::PortId,
    heat_control_Id: art::PortId
}

pub trait Manage_Heat_Source_i_Put_Api {
    fn put_current_tempWstatus(&mut self, value: data::TempWstatus_i);
    fn put_lower_desired_temp(&mut self, value: data::Temp_i);
    fn put_upper_desired_temp(&mut self, value: data::Temp_i);
    fn put_regulator_mode(&mut self, value: data::Regulator_Mode);
    fn put_heat_control(&mut self, value: data::OnOff);
}

impl Manage_Heat_Source_i_Put_Api for Manage_Heat_Source_i_Api {
    fn put_current_tempWstatus(&mut self, value: data::TempWstatus_i) {
        todo!()
    }
    fn put_lower_desired_temp(&mut self, value: data::Temp_i) {
        todo!()
    }
    fn put_upper_desired_temp(&mut self, value: data::Temp_i) {
        todo!()
    }
    fn put_regulator_mode(&mut self, value: data::Regulator_Mode) {
        todo!()
    }
    fn put_heat_control(&mut self, value: data::OnOff) {
        art::putValue(self.heat_control_Id, Box::new(value))
    }
}

pub trait Manage_Heat_Source_i_Get_Api {
    fn get_current_tempWstatus(&self) -> Option<data::TempWstatus_i>;
    fn get_lower_desired_temp(&self) -> Option<data::Temp_i>;
    fn get_upper_desired_temp(&self) -> Option<data::Temp_i>;
    fn get_regulator_mode(&self) -> Option<data::Regulator_Mode>;
    fn get_heat_control(&self) -> Option<data::OnOff>;
}

impl Manage_Heat_Source_i_Get_Api for Manage_Heat_Source_i_Api {
    fn get_current_tempWstatus(&self) -> Option<data::TempWstatus_i> {
        let value: Option<Box<dyn Any>> = art::getAnyValue(self.current_tempWstatus_Id);
        match value {
            Some(v) => {
                if let Some(x) = v.downcast_ref::<data::TempWstatus_i>() {
                    Some(x.clone())
                } else {
                    let s = format!("Unexpected payload on port current_tempWstatus.  Expecting 'Isolette_Data_Model.TempWstatus_i_Payload' but received {v:?}");
                    art::logError(self.id, &s);
                    None
                }
            },
            None => None
        }
    }
    fn get_lower_desired_temp(&self) -> Option<data::Temp_i> {
        todo!()
    }
    fn get_upper_desired_temp(&self) -> Option<data::Temp_i> {
        todo!()
    }
    fn get_regulator_mode(&self) -> Option<data::Regulator_Mode> {
        todo!()
    }
    fn get_heat_control(&self) -> Option<data::OnOff> {
        todo!()
    }
}

pub trait Manage_Heat_Source_i_Full_Api: Manage_Heat_Source_i_Put_Api + Manage_Heat_Source_i_Get_Api {}

pub struct Manage_Heat_Source_i_Initialization_Api<API: Manage_Heat_Source_i_Put_Api> {
    api: API,
    pub ghost heat_control: data::OnOff,
    pub ghost current_tempWstatus: data::TempWstatus_i
}

impl<API: Manage_Heat_Source_i_Put_Api> Manage_Heat_Source_i_Initialization_Api<API> {
    pub fn put_heat_control(&mut self, value : data::OnOff)
        ensures self.heat_control == value
    {
        self.heat_control = value;

        self.api.put_heat_control(value)
    }
}

pub struct Manage_Heat_Source_i_Operational_Api<API: Manage_Heat_Source_i_Full_Api> {
    api: API,
    pub ghost heat_control: data::OnOff,
    pub ghost current_tempWstatus: data::TempWstatus_i,
    pub ghost lower_desired_temp: data::Temp_i,
    pub ghost upper_desired_temp: data::Temp_i,
    pub ghost regulator_mode: data::Regulator_Mode
}

impl<API: Manage_Heat_Source_i_Full_Api> Manage_Heat_Source_i_Operational_Api<API> {
    pub fn put_heat_control(&mut self, value : data::OnOff)
        ensures self.heat_control == value
    {
        self.heat_control = value;

        self.api.put_heat_control(value)
    }

    pub fn get_current_tempWstatus(&self) -> (res: Option<data::TempWstatus_i>)
        ensures res = Some(self.current_tempWstatus)
    {
        self.api.get_current_tempWstatus()
    }

    pub fn get_lower_desired_temp(&self) -> (res: Option<data::Temp_i>)
    ensures res = Some(self.lower_desired_temp)
    {
        self.api.get_lower_desired_temp()
    }

    pub fn get_upper_desired_temp(&self) -> (res: Option<data::Temp_i>)
    ensures res = Some(self.lower_desired_temp)
    {
        self.api.get_upper_desired_temp()
    }

    pub fn get_regulator_mode(&self) -> (res: Option<data::Regulator_Mode>)
    ensures res = Some(self.regulator_mode)
    {
        self.api.get_regulator_mode()
    }

}

}