#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use vstd::prelude::*;
use crate::data::Isolette_Data_Model::*;
use super::extern_c_api as extern_api;

verus! {

pub trait Manage_Regulator_Interface_Api {}

pub trait Manage_Regulator_Interface_Put_Api: Manage_Regulator_Interface_Api {
    #[verifier::external_body]
    fn put_upper_desired_temp_unverified(&mut self, data: Temp_i) {
        extern_api::unsafe_put_upper_desired_temp(&data);
    }

    #[verifier::external_body]
    fn put_lower_desired_temp_unverified(&mut self, data: Temp_i) {
        extern_api::unsafe_put_lower_desired_temp(&data);
    }

    #[verifier::external_body]
    fn put_displayed_temp_unverified(&mut self, data: Temp_i) {
        extern_api::unsafe_put_displayed_temp(&data);
    }

    #[verifier::external_body]
    fn put_regulator_status_unverified(&mut self, data: Status) {
        extern_api::unsafe_put_regulator_status(&data);
    }

    #[verifier::external_body]
    fn put_interface_failure_unverified(&mut self, data: Failure_Flag_i) {
        extern_api::unsafe_put_interface_failure(&data);
    }
}

pub trait Manage_Regulator_Interface_Get_Api: Manage_Regulator_Interface_Api {
    
  #[verifier::external_body]
  fn get_regulator_mode_unverified(&self, _data: &Ghost<Regulator_Mode>) -> 
    (res: Regulator_Mode) 
        ensures res == _data@ {
    return extern_api::unsafe_get_regulator_mode();
  }

  #[verifier::external_body]
  fn get_lower_desired_tempWstatus_unverified(&self, _data: &Ghost<TempWstatus_i>) -> 
    (res: TempWstatus_i)
        ensures res.degrees == _data@.degrees && res.status == _data@.status {
    return extern_api::unsafe_get_lower_desired_tempWstatus();
  }

  #[verifier::external_body]
  fn get_upper_desired_tempWstatus_unverified(&self, _data: &Ghost<TempWstatus_i>) ->
     (res: TempWstatus_i)
        ensures res.degrees == _data@.degrees && res.status == _data@.status {
    return extern_api::unsafe_get_upper_desired_tempWstatus();
  }

  #[verifier::external_body]
  fn get_current_tempWstatus_unverified(&self, _data: &Ghost<TempWstatus_i>) -> 
    (res: TempWstatus_i) 
        ensures res.degrees == _data@.degrees && res.status == _data@.status {
    return extern_api::unsafe_get_current_tempWstatus();
  }

}


pub trait Manage_Regulator_Interface_Full_Api: Manage_Regulator_Interface_Get_Api + Manage_Regulator_Interface_Put_Api {}

pub struct Manage_Regulator_Interface_Application_Api<API: Manage_Regulator_Interface_Api> {
    pub api: API,

    pub ghost upper_desired_temp: Temp_i,
    pub ghost lower_desired_temp: Temp_i,
    pub ghost displayed_temp: Temp_i,
    pub ghost regulator_status: Status,
    pub ghost interface_failure: Failure_Flag_i,

    pub ghost regulator_mode: Regulator_Mode,
    pub ghost lower_desired_tempWstatus: TempWstatus_i,
    pub ghost upper_desired_tempWstatus: TempWstatus_i,
    pub ghost current_tempWstatus: TempWstatus_i
}

impl <API: Manage_Regulator_Interface_Api> Manage_Regulator_Interface_Application_Api<API> {
    pub fn new(api: API) -> Self {
        Self {
            api,

            // must also init the ghost vars (i'm guessing these get compiled away).
            // verus complains "cannot call function xx with mode exec" for the defaults
            /*
            upper_desired_temp: Temp_i::default(),
            lower_desired_temp: Temp_i::default(),
            displayed_temp: Temp_i::default(),
            regulator_status: Status::default(),
            interface_failure: Failure_Flag_i::default(),

            regulator_mode: Regulator_Mode::default(),
            lower_desired_tempWstatus: TempWstatus_i::default(),
            upper_desired_tempWstatus: TempWstatus_i::default(),
            current_tempWstatus: TempWstatus_i::default()
            */
            upper_desired_temp: Temp_i { degrees: 0 },
            lower_desired_temp: Temp_i { degrees: 0 },
            displayed_temp: Temp_i { degrees: 0 },
            regulator_status: Status::Init_Status,
            interface_failure: Failure_Flag_i { flag: false },

            regulator_mode: Regulator_Mode::Init_Regulator_Mode,
            lower_desired_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
            upper_desired_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
            current_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid }
        }
    }
}

impl<API: Manage_Regulator_Interface_Put_Api> Manage_Regulator_Interface_Application_Api<API> {

    pub fn put_upper_desired_temp(&mut self, data: Temp_i) 
        ensures 
            self.upper_desired_temp == data,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,

            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      self.api.put_upper_desired_temp_unverified(data);
      self.upper_desired_temp = data;
    }

    pub fn put_lower_desired_temp(&mut self, data: Temp_i) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            self.lower_desired_temp == data,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,

            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      self.api.put_lower_desired_temp_unverified(data);
      self.lower_desired_temp = data;
    }

    pub fn put_displayed_temp(&mut self, data: Temp_i) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            self.displayed_temp == data,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,
  
            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      self.api.put_displayed_temp_unverified(data);
      self.displayed_temp = data;
    }

    pub fn put_regulator_status(&mut self, data: Status) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            self.regulator_status == data,
            old(self).interface_failure == self.interface_failure,
  
            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      self.api.put_regulator_status_unverified(data);
      self.regulator_status = data;
    }

    pub fn put_interface_failure(&mut self, data: Failure_Flag_i) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            self.interface_failure == data,
  
            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      self.api.put_interface_failure_unverified(data);
      self.interface_failure = data;
    }   
           
}

impl<API: Manage_Regulator_Interface_Get_Api> Manage_Regulator_Interface_Application_Api<API> {
    
    pub fn get_regulator_mode(&mut self) -> (res: Regulator_Mode) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,

            res == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      let data: Regulator_Mode = self.api.get_regulator_mode_unverified(&Ghost(self.regulator_mode));
      return data;
    }   

    pub fn get_lower_desired_tempWstatus(&mut self) -> (res: TempWstatus_i) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,

            old(self).regulator_mode == self.regulator_mode,
            res == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      let data: TempWstatus_i = self.api.get_lower_desired_tempWstatus_unverified(&Ghost(self.lower_desired_tempWstatus));
      return data;
    }  

    pub fn get_upper_desired_tempWstatus(&mut self) -> (res: TempWstatus_i) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,

            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            res == self.upper_desired_tempWstatus,
            old(self).current_tempWstatus == self.current_tempWstatus {
      let data: TempWstatus_i = self.api.get_upper_desired_tempWstatus_unverified(&Ghost(self.upper_desired_tempWstatus));
      return data;
    }     

    pub fn get_current_tempWstatus(&mut self) -> (res: TempWstatus_i) 
        ensures 
            old(self).upper_desired_temp == self.upper_desired_temp,
            old(self).lower_desired_temp == self.lower_desired_temp,
            old(self).displayed_temp == self.displayed_temp,
            old(self).regulator_status == self.regulator_status,
            old(self).interface_failure == self.interface_failure,

            old(self).regulator_mode == self.regulator_mode,
            old(self).lower_desired_tempWstatus == self.lower_desired_tempWstatus,
            old(self).upper_desired_tempWstatus == self.upper_desired_tempWstatus,
            res == self.current_tempWstatus {
      let data: TempWstatus_i = self.api.get_current_tempWstatus_unverified(&Ghost(self.current_tempWstatus));
      return data;
    }               
}


pub struct MRI_Initialization_Api;
impl Manage_Regulator_Interface_Api for MRI_Initialization_Api {}
impl Manage_Regulator_Interface_Put_Api for MRI_Initialization_Api {}

pub const fn init_api() -> Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api> {
    return Manage_Regulator_Interface_Application_Api {
        api: MRI_Initialization_Api {},
        upper_desired_temp: Temp_i { degrees: 0 },
        lower_desired_temp: Temp_i { degrees: 0 },
        displayed_temp: Temp_i { degrees: 0 },
        regulator_status: Status::Init_Status,
        interface_failure: Failure_Flag_i { flag: false },
    
        regulator_mode: Regulator_Mode::Init_Regulator_Mode,
        lower_desired_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
        upper_desired_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
        current_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid }
    }
}

pub struct MRI_Compute_Api;
impl Manage_Regulator_Interface_Api for MRI_Compute_Api {}
impl Manage_Regulator_Interface_Put_Api for MRI_Compute_Api {}
impl Manage_Regulator_Interface_Get_Api for MRI_Compute_Api {}
impl Manage_Regulator_Interface_Full_Api for MRI_Compute_Api {}

pub const fn compute_api() -> Manage_Regulator_Interface_Application_Api<MRI_Compute_Api> {
    return Manage_Regulator_Interface_Application_Api {
        api: MRI_Compute_Api {},
        upper_desired_temp: Temp_i { degrees: 0 },
        lower_desired_temp: Temp_i { degrees: 0 },
        displayed_temp: Temp_i { degrees: 0 },
        regulator_status: Status::Init_Status,
        interface_failure: Failure_Flag_i { flag: false },
    
        regulator_mode: Regulator_Mode::Init_Regulator_Mode,
        lower_desired_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
        upper_desired_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid },
        current_tempWstatus: TempWstatus_i { degrees: 0, status: ValueStatus::Valid }
    }
}
}