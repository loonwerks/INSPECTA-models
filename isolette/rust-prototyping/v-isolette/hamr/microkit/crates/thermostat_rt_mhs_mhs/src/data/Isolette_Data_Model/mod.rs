include!("Failure_Flag_i.rs");
include!("Monitor_Mode.rs");
include!("On_Off.rs");
include!("Physical_Temp_i.rs");
include!("Regulator_Mode.rs");
include!("Status.rs");
include!("Temp_i.rs");
include!("TempWstatus_i.rs");
include!("ValueStatus.rs");

/* 
NOTE: the include!("xx.rs") inlines the file contents directly so a module for
xx is not created.  An alternative would be to do

pub mod Failure_Flag_i.rs;
pub use Failure_Flag_i::*;

however this would create a module out of Failure_Flag_i.rs so to refer to the datatype
it contains you'd have to do something like "use crate::types::Failure_Flag_i::Failure_Flag_i"
in lib.rs. A workaround is to rename the file, e.g. Failure_Flag_i_struct.rs but leave the
file contents unchanged.  Then in this file do

pub mod Failure_Flag_i_struct;
pub use Failure_Flag_i_struct::*;

then Failure_Flag_i would be imported in lib.rs via "use crate::types::Isolette_Data_Model::*"
*/
