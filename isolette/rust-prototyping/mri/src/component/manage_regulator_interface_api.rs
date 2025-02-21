#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use vstd::prelude::*;

use crate::data::*;

verus! {

pub trait Manage_Regulator_Interface_Api {}

pub trait Manage_Regulator_Interface_Put_Api: Manage_Regulator_Interface_Api {
    #[verifier::external_body]
    fn put_upper_desired_temp_unverified(&mut self, data: isolette_Isolette_Data_Model_Temp_i) {
        super::extern_api::unsafe_put_upper_desired_temp(&data);
    }

    #[verifier::external_body]
    fn put_lower_desired_temp_unverified(&mut self, data: isolette_Isolette_Data_Model_Temp_i) {
        super::extern_api::unsafe_put_lower_desired_temp(&data);
    }

    #[verifier::external_body]
    fn put_displayed_temp_unverified(&mut self, data: isolette_Isolette_Data_Model_Temp_i) {
        super::extern_api::unsafe_put_displayed_temp(&data);
    }

    #[verifier::external_body]
    fn put_regulator_status_unverified(&mut self, data: isolette_Isolette_Data_Model_Status_Type) {
        super::extern_api::unsafe_put_regulator_status(&data);
    }

    #[verifier::external_body]
    fn put_interface_failure_unverified(&mut self, data: isolette_Isolette_Data_Model_Failure_Flag_i) {
        super::extern_api::unsafe_put_interface_failure(&data);
    }
}

pub trait Manage_Regulator_Interface_Get_Api: Manage_Regulator_Interface_Api {
    
  #[verifier::external_body]
  fn get_regulator_mode_unverified(&self, _data: &Ghost<isolette_Isolette_Data_Model_Regulator_Mode_Type>) -> 
    (res: isolette_Isolette_Data_Model_Regulator_Mode_Type) 
        ensures res == _data@ {
    return super::extern_api::unsafe_get_regulator_mode();
  }

  #[verifier::external_body]
  fn get_lower_desired_tempWstatus_unverified(&self, _data: &Ghost<isolette_Isolette_Data_Model_TempWstatus_i>) -> 
    (res: isolette_Isolette_Data_Model_TempWstatus_i)
        ensures res.degrees == _data@.degrees && res.status == _data@.status {
    return super::extern_api::unsafe_get_lower_desired_tempWstatus();
  }

  #[verifier::external_body]
  fn get_upper_desired_tempWstatus_unverified(&self, _data: &Ghost<isolette_Isolette_Data_Model_TempWstatus_i>) ->
     (res: isolette_Isolette_Data_Model_TempWstatus_i)
        ensures res.degrees == _data@.degrees && res.status == _data@.status {
    return super::extern_api::unsafe_get_upper_desired_tempWstatus();
  }

  #[verifier::external_body]
  fn get_current_tempWstatus_unverified(&self, _data: &Ghost<isolette_Isolette_Data_Model_TempWstatus_i>) -> 
    (res: isolette_Isolette_Data_Model_TempWstatus_i) 
        ensures res.degrees == _data@.degrees && res.status == _data@.status {
    return super::extern_api::unsafe_get_current_tempWstatus();
  }

}


pub trait Manage_Regulator_Interface_Full_Api: Manage_Regulator_Interface_Get_Api + Manage_Regulator_Interface_Put_Api {}

pub struct Manage_Regulator_Interface_Application_Api<API: Manage_Regulator_Interface_Api> {
    pub api: API,

    pub ghost upper_desired_temp: isolette_Isolette_Data_Model_Temp_i,
    pub ghost lower_desired_temp: isolette_Isolette_Data_Model_Temp_i,
    pub ghost displayed_temp: isolette_Isolette_Data_Model_Temp_i,
    pub ghost regulator_status: isolette_Isolette_Data_Model_Status_Type,
    pub ghost interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i,

    pub ghost regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
    pub ghost lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
    pub ghost upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
    pub ghost current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i
}

impl <API: Manage_Regulator_Interface_Api> Manage_Regulator_Interface_Application_Api<API> {
    pub fn new(api: API) -> Self {
        Self {
            api,

            // must also init the ghost vars (i'm guessing these get compiled away).
            // verus complains "cannot call function xx with mode exec" for the defaults
            /*
            upper_desired_temp: isolette_Isolette_Data_Model_Temp_i::default(),
            lower_desired_temp: isolette_Isolette_Data_Model_Temp_i::default(),
            displayed_temp: isolette_Isolette_Data_Model_Temp_i::default(),
            regulator_status: isolette_Isolette_Data_Model_Status_Type::default(),
            interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i::default(),

            regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type::default(),
            lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i::default(),
            upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i::default(),
            current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i::default()
            */
            upper_desired_temp: isolette_Isolette_Data_Model_Temp_i { degrees: 0 },
            lower_desired_temp: isolette_Isolette_Data_Model_Temp_i { degrees: 0 },
            displayed_temp: isolette_Isolette_Data_Model_Temp_i { degrees: 0 },
            regulator_status: isolette_Isolette_Data_Model_Status_Type::Init_Status,
            interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i { flag: false },

            regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode,
            lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i { degrees: 0, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid },
            upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i { degrees: 0, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid },
            current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i { degrees: 0, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid }
        }
    }
}

impl<API: Manage_Regulator_Interface_Put_Api> Manage_Regulator_Interface_Application_Api<API> {

    pub fn put_upper_desired_temp(&mut self, data: isolette_Isolette_Data_Model_Temp_i) 
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

    pub fn put_lower_desired_temp(&mut self, data: isolette_Isolette_Data_Model_Temp_i) 
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

    pub fn put_displayed_temp(&mut self, data: isolette_Isolette_Data_Model_Temp_i) 
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

    pub fn put_regulator_status(&mut self, data: isolette_Isolette_Data_Model_Status_Type) 
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

    pub fn put_interface_failure(&mut self, data: isolette_Isolette_Data_Model_Failure_Flag_i) 
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
    
    pub fn get_regulator_mode(&mut self) -> (res: isolette_Isolette_Data_Model_Regulator_Mode_Type) 
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
      let data: isolette_Isolette_Data_Model_Regulator_Mode_Type = self.api.get_regulator_mode_unverified(&Ghost(self.regulator_mode));
      return data;
    }   

    pub fn get_lower_desired_tempWstatus(&mut self) -> (res: isolette_Isolette_Data_Model_TempWstatus_i) 
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
      let data: isolette_Isolette_Data_Model_TempWstatus_i = self.api.get_lower_desired_tempWstatus_unverified(&Ghost(self.lower_desired_tempWstatus));
      return data;
    }  

    pub fn get_upper_desired_tempWstatus(&mut self) -> (res: isolette_Isolette_Data_Model_TempWstatus_i) 
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
      let data: isolette_Isolette_Data_Model_TempWstatus_i = self.api.get_upper_desired_tempWstatus_unverified(&Ghost(self.upper_desired_tempWstatus));
      return data;
    }     

    pub fn get_current_tempWstatus(&mut self) -> (res: isolette_Isolette_Data_Model_TempWstatus_i) 
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
      let data: isolette_Isolette_Data_Model_TempWstatus_i = self.api.get_current_tempWstatus_unverified(&Ghost(self.current_tempWstatus));
      return data;
    }               
}


pub struct MRI_Initialization_Api;
impl Manage_Regulator_Interface_Api for MRI_Initialization_Api {}
impl Manage_Regulator_Interface_Put_Api for MRI_Initialization_Api {}

pub struct MRI_Compute_Api;
impl Manage_Regulator_Interface_Api for MRI_Compute_Api {}
impl Manage_Regulator_Interface_Put_Api for MRI_Compute_Api {}
impl Manage_Regulator_Interface_Get_Api for MRI_Compute_Api {}
impl Manage_Regulator_Interface_Full_Api for MRI_Compute_Api {}

}