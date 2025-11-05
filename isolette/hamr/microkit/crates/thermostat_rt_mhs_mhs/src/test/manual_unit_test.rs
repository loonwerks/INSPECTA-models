// NOTE: need to run tests sequentially to prevent race conditions
//       on the app and the testing apis which are static
use serial_test::serial;

use data::*;
use crate::test::util::*;

use data::Isolette_Data_Model::*;

#[test]
#[serial]
fn test_compute_REQ_MHS_2() {
// [InvokeEntryPoint]: invoke the entry point test method
crate::thermostat_rt_mhs_mhs_initialize();

// generate values for the incoming data ports
let regulator_mode: Regulator_Mode = Regulator_Mode::Normal_Regulator_Mode;
let lower_desired_temp = Temp_i { degrees: 97 };
let upper_desired_temp = Temp_i { degrees: 101 };
let current_tempWstatus = TempWstatus_i {
    degrees: 96,
    status: ValueStatus::Valid,
};

// [PutInPorts]: put values on the input ports
test_apis::put_current_tempWstatus(current_tempWstatus);
test_apis::put_lower_desired_temp(lower_desired_temp);
test_apis::put_upper_desired_temp(upper_desired_temp);
test_apis::put_regulator_mode(regulator_mode);

// invoke the entry point test method
crate::thermostat_rt_mhs_mhs_timeTriggered();

let api_heat_control = test_apis::get_heat_control();
let lastCmd = test_apis::get_lastCmd();

assert!(api_heat_control == On_Off::Onn);
assert!(lastCmd == On_Off::Onn);
}