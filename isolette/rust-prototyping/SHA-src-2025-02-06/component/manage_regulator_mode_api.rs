use vstd::prelude::*;
use vstd::invariant::*;

use crate::{art, data};

//  AADL Component Type info for Manage Regulator Mode
//
// -- ==== INPUTS ========	 (see Figure A.3)
// -- ("Current Temperature (Status)" - current temperature from temp sensor
// current_tempWstatus: in data port Isolette_Data_Model::TempWstatus.i;
// -- ("Regulator Interface Failure") - status of operator interface interactions
// interface_failure: in data port Isolette_Data_Model::Failure_Flag.i;
// -- ("Regulator Internal Failure") - internal status of regulator
// internal_failure: in data port Isolette_Data_Model::Failure_Flag.i;
//
// -- ==== OUTPUTS ========  (see Figure A.3)
// -- ("Regulator Mode") - mode of regulator (Init, Normal, Failed)
// regulator_mode: out data port Isolette_Data_Model::Regulator_Mode;

verus! {

pub trait Manage_Regulator_Mode_i_Api {}

// Declare API for input ports (Get_Api)
pub trait Manage_Regulator_Mode_i_Put_Api: Manage_Regulator_Mode_i_Api {
    #[verifier::external_body]
    fn put_regulator_mode_unverified(&mut self, value: data::Regulator_Mode) {
        // To Stefan: I don't understand how this works.  There isn't an unsafe_put method in the super trait
        super::extern_api::unsafe_put_regulator_mode(&value);
    }
}

// Declare API for output ports (Put_Api)
pub trait Manage_Regulator_Mode_i_Get_Api: Manage_Regulator_Mode_i_Api {
    #[verifier::external_body]
    fn get_current_tempWstatus_unverified(&self) -> data::TempWstatus_i {
        super::extern_api::unsafe_get_current_tempWstatus()
    }
    #[verifier::external_body]
    fn get_interface_failure_unverified(&self) -> (res: data::Failure_Flag_i)
    {
        super::extern_api::unsafe_get_interface_failure()
    }
    fn get_internal_failure_unverified(&self) -> (res: data::Failure_Flag_i)
    {
        super::extern_api::unsafe_get_internal_failure()
    }
}

pub trait Manage_Regulator_Mode_i_Full_Api: Manage_Regulator_Mode_i_Get_Api + Manage_Regulator_Mode_i_Put_Api {}

// -----------------------------------------------------------------
//   API State Structure 
// -----------------------------------------------------------------

// To Stefan: 
//    Does Rust/Verus allow the ghost variables to appear in the Api trait?
//    I am assuming NO since traits cannot be stateful in general in Rust.
pub struct Manage_Regulator_Mode_i_Application_Api<API: Manage_Regulator_Mode_i_Api> {
    pub api: API,
    // input ports
    // To Stefan: what does the varname below have a different color than the others?
    pub ghost current_tempWstatus: data::TempWstatus_i,
    pub ghost interface_failure: data::Failure_Flag_i,
    pub ghost internal_failure: data::Failure_Flag_i,
    // output ports
    pub ghost regulator_mode: data::Regulator_Mode  
}

// -----------------------------------------------------------------
//   output ports - put methods
// -----------------------------------------------------------------

// To Stefan: 
//   - how do the contracts on the trait methods integrate with contracts on the implementation methods?
//     Are the contracts on implementations visible to the callers?
//   - shouldn't we mark this method as #[verifier::external_body]
// To John: I need to remind myself how these struct/impl declarations work
impl<API: Manage_Heat_Source_i_Put_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn put_regulator_mode(&mut self, value : data::Regulator)
        ensures self.regulator_mode == value // the port ghost variable is set to the put method parameter
        // we need frame conditions for all 
        //  input port ghost variables
        //  output port ghost variables OTHER THAN THE ONE BEING UPDATED by put
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).interface_failure == self.interface_failure
        && old(self).internal_failure == self.internal_failure
    {
        self.api.put_regulator_mode_unverified(value);
        // ghost variable manipulation - this may not be strictly needed if method is marked 
        //  as external body.  However, it may be useful for documenting the intended semantics.
        self.regulator_mode = value 
    }
}

// -----------------------------------------------------------------
//   input ports - get methods
// -----------------------------------------------------------------

impl<API: Manage_Heat_Source_i_Get_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn get_current_tempWstatus(&mut self) -> (res: data::TempWstatus_i)
        ensures res == self.current_tempWstatus
        // we need frame conditions for all ports since the get_ method is only doing a read
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).interface_failure == self.interface_failure
        && old(self).internal_failure == self.internal_failure
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_current_tempWstatus_unverified();
        data
    }

    pub fn get_interface_failure(&mut self) -> (res: data::Failure_Flag_i)
        ensures res == self.interface_failure
        // we need frame conditions for all ports since the get_ method is only doing a read
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).interface_failure == self.interface_failure
        && old(self).internal_failure == self.internal_failure
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_interface_failure_unverified();
        data
    }

    pub fn get_internal_failure(&mut self) -> (res: data::Failure_Flag_i)
        ensures res == self.internal_failure
        // we need frame conditions for all ports since the get_ method is only doing a read        
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).interface_failure == self.interface_failure
        && old(self).internal_failure == self.internal_failure
        && old(self).regulator_mode == self.regulator_mode
    {
        let data = self.api.get_internal_failure_unverified();
        data
    }
}

}