#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

use crate::types::sb_aadl_types::*;

pub trait Manage_Regulator_Interface_Api {}

pub trait Manage_Regulator_Interface_Put_Api: Manage_Regulator_Interface_Api {
    
    fn put_upper_desired_temp_unverified(&mut self, data: isolette_Isolette_Data_Model_Temp_i) {
        super::extern_api::unsafe_put_upper_desired_temp(&data);
    }

    
    fn put_lower_desired_temp_unverified(&mut self, data: isolette_Isolette_Data_Model_Temp_i) {
        super::extern_api::unsafe_put_lower_desired_temp(&data);
    }

    
    fn put_displayed_temp_unverified(&mut self, data: isolette_Isolette_Data_Model_Temp_i) {
        super::extern_api::unsafe_put_displayed_temp(&data);
    }

    
    fn put_regulator_status_unverified(&mut self, data: isolette_Isolette_Data_Model_Status_Type) {
        super::extern_api::unsafe_put_regulator_status(&data);
    }

    
    fn put_interface_failure_unverified(&mut self, data: isolette_Isolette_Data_Model_Failure_Flag_i) {
        super::extern_api::unsafe_put_interface_failure(&data);
    }
}

pub trait Manage_Regulator_Interface_Get_Api: Manage_Regulator_Interface_Api {
    
  
  fn get_regulator_mode_unverified(&self) -> 
    (isolette_Isolette_Data_Model_Regulator_Mode_Type) 
        {
    return super::extern_api::unsafe_get_regulator_mode();
  }

  
  fn get_lower_desired_tempWstatus_unverified(&self) -> 
    ( isolette_Isolette_Data_Model_TempWstatus_i)
        {
    return super::extern_api::unsafe_get_lower_desired_tempWstatus();
  }

  
  fn get_upper_desired_tempWstatus_unverified(&self) ->
     ( isolette_Isolette_Data_Model_TempWstatus_i)
        {
    return super::extern_api::unsafe_get_upper_desired_tempWstatus();
  }

  
  fn get_current_tempWstatus_unverified(&self) -> 
    ( isolette_Isolette_Data_Model_TempWstatus_i) 
        {
    return super::extern_api::unsafe_get_current_tempWstatus();
  }

}


pub trait Manage_Regulator_Interface_Full_Api: Manage_Regulator_Interface_Get_Api + Manage_Regulator_Interface_Put_Api {}

pub struct Manage_Regulator_Interface_Application_Api<API: Manage_Regulator_Interface_Api> {
    pub api: API,

}

impl <API: Manage_Regulator_Interface_Api> Manage_Regulator_Interface_Application_Api<API> {
    pub fn new(api: API) -> Self {
        Self {
            api,
        }
    }
}

impl<API: Manage_Regulator_Interface_Put_Api> Manage_Regulator_Interface_Application_Api<API> {

    pub fn put_upper_desired_temp(&mut self, data: isolette_Isolette_Data_Model_Temp_i) 
        {
      self.api.put_upper_desired_temp_unverified(data);
      
    }

    pub fn put_lower_desired_temp(&mut self, data: isolette_Isolette_Data_Model_Temp_i) 
        {
      self.api.put_lower_desired_temp_unverified(data);
      
    }

    pub fn put_displayed_temp(&mut self, data: isolette_Isolette_Data_Model_Temp_i) 
        {
      self.api.put_displayed_temp_unverified(data);
      
    }

    pub fn put_regulator_status(&mut self, data: isolette_Isolette_Data_Model_Status_Type) 
        {
      self.api.put_regulator_status_unverified(data);
      
    }

    pub fn put_interface_failure(&mut self, data: isolette_Isolette_Data_Model_Failure_Flag_i) 
        {
      self.api.put_interface_failure_unverified(data);
      
    }   
           
}

impl<API: Manage_Regulator_Interface_Get_Api> Manage_Regulator_Interface_Application_Api<API> {
    
    pub fn get_regulator_mode(&mut self) -> ( isolette_Isolette_Data_Model_Regulator_Mode_Type) 
       {
      let data: isolette_Isolette_Data_Model_Regulator_Mode_Type = self.api.get_regulator_mode_unverified();
      return data;
    }   

    pub fn get_lower_desired_tempWstatus(&mut self) -> ( isolette_Isolette_Data_Model_TempWstatus_i) 
       {
      let data: isolette_Isolette_Data_Model_TempWstatus_i = self.api.get_lower_desired_tempWstatus_unverified();
      return data;
    }  

    pub fn get_upper_desired_tempWstatus(&mut self) -> ( isolette_Isolette_Data_Model_TempWstatus_i) 
        {
      let data: isolette_Isolette_Data_Model_TempWstatus_i = self.api.get_upper_desired_tempWstatus_unverified();
      return data;
    }     

    pub fn get_current_tempWstatus(&mut self) -> ( isolette_Isolette_Data_Model_TempWstatus_i) 
        {
      let data: isolette_Isolette_Data_Model_TempWstatus_i = self.api.get_current_tempWstatus_unverified();
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

