// NOTE: need to run tests sequentially to prevent race conditions
//       on the app and the testing apis which are static
use serial_test::serial;

use data::*;
use crate::bridge::thermostat_rt_mhs_mhs_GUMBOX as GUMBOX;
use crate::test::util::*;
use data::Isolette_Data_Model::*;

#[test]
#[serial]
fn test_initialization_REQ_MHS_1_container() {
    // [InvokeEntryPoint]: invoke the entry point test method
    crate::thermostat_rt_mhs_mhs_initialize();

    // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
    let heat_control = test_apis::get_heat_control();

    let lastCmd = test_apis::get_lastCmd();

    // [CheckPost]: invoke the oracle function
    assert!(GUMBOX::initialize_IEP_Post(
    heat_control,
    lastCmd));

    // example of manual testing
    assert!(heat_control == On_Off::Off);
    assert!(lastCmd == heat_control);
}

#[test]
#[serial]
fn test_compute_REQ_MHS_2_container() {
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let container = test_apis::PreStateContainer_wGSV {
    api_current_tempWstatus: TempWstatus_i {
        degrees: 96,
        status: ValueStatus::Valid,
    },
    api_lower_desired_temp: Temp_i { degrees: 97 },
    api_upper_desired_temp: Temp_i { degrees: 101 },
    api_regulator_mode: Regulator_Mode::Normal_Regulator_Mode,
    In_lastCmd: On_Off::Off
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
    _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_compute_REQ_MHS_3_container() {
    // initialize the app
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let container = test_apis::PreStateContainer_wGSV {
    api_current_tempWstatus: TempWstatus_i {
        degrees: 102,
        status: ValueStatus::Valid,
    },
    api_lower_desired_temp: Temp_i { degrees: 97 },
    api_upper_desired_temp: Temp_i { degrees: 101 },
    api_regulator_mode: Regulator_Mode::Normal_Regulator_Mode,
    In_lastCmd: On_Off::Onn
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
    _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_compute_REQ_MHS_4_container() {
    // initialize the app
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let container = test_apis::PreStateContainer_wGSV {
    api_current_tempWstatus: TempWstatus_i {
        degrees: 98,
        status: ValueStatus::Valid,
    },
    api_lower_desired_temp: Temp_i { degrees: 97 },
    api_upper_desired_temp: Temp_i { degrees: 101 },
    api_regulator_mode: Regulator_Mode::Normal_Regulator_Mode,
    In_lastCmd: On_Off::Onn
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
    _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_compute_REQ_MHS_5_container() {
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let container = test_apis::PreStateContainer_wGSV {
    api_current_tempWstatus: TempWstatus_i {
        degrees: 98,
        status: ValueStatus::Valid,
    },
    api_lower_desired_temp: Temp_i { degrees: 97 },
    api_upper_desired_temp: Temp_i { degrees: 101 },
    api_regulator_mode: Regulator_Mode::Failed_Regulator_Mode,
    In_lastCmd: On_Off::Onn
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
    _ => { assert!(false); }
    }
}