// NOTE: need to run tests sequentially to prevent race conditions
//       on the app and the testing apis which are static
use serial_test::serial;

use data::*;
use crate::bridge::thermostat_rt_mhs_mhs_GUMBOX as GUMBOX;
use crate::test::util::*;
const failOnUnsatPrecondition: bool = false;

use data::Isolette_Data_Model::*;

#[test]
#[serial]
fn test_initialization_REQ_MHS_1() {
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
fn test_compute_REQ_MHS_2() {
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let current_tempWstatus = TempWstatus_i {
        degrees: 96,
        status: ValueStatus::Valid,
    };
    let lower_desired_temp = Temp_i { degrees: 97 };
    let upper_desired_temp = Temp_i { degrees: 101 };
    let regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
    let Old_lastCmd: On_Off = On_Off::Off;

    // [CheckPre]: check/filter based on pre-condition.
    if (!GUMBOX::compute_CEP_Pre(
        Old_lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp)) { 
            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }           
    } else {
        // [PutInPorts]: put values on the input ports
        test_apis::put_current_tempWstatus(current_tempWstatus);
        test_apis::put_lower_desired_temp(lower_desired_temp);
        test_apis::put_upper_desired_temp(upper_desired_temp);
        test_apis::put_regulator_mode(regulator_mode);

        // [SetInStateVars]: set the pre-state values of state variables
        test_apis::put_lastCmd(Old_lastCmd);

        // [InvokeEntryPoint]: invoke the entry point test method
        crate::thermostat_rt_mhs_mhs_timeTriggered();

        // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
        let api_heat_control = test_apis::get_heat_control();
        let lastCmd = test_apis::get_lastCmd();

        // [CheckPost]: invoke the oracle function
        assert!(GUMBOX::compute_CEP_Post(
        Old_lastCmd,
        lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp,
        api_heat_control));

            // example of manual testing
        assert!(api_heat_control == On_Off::Onn);
        assert!(lastCmd == api_heat_control);
    }
}


#[test]
#[serial]
fn test_compute_REQ_MHS_3() {
    // initialize the app
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let current_tempWstatus = TempWstatus_i { degrees: 102, status: ValueStatus::Valid };
    let lower_desired_temp = Temp_i { degrees: 97 };
    let upper_desired_temp = Temp_i { degrees: 101 };
    let regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
    let Old_lastCmd: On_Off = On_Off::Onn;

    // [CheckPre]: check/filter based on pre-condition.
    if (!GUMBOX::compute_CEP_Pre(
        Old_lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp)) { 
            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }           
    } else {
        // [PutInPorts]: put values on the input ports
        test_apis::put_current_tempWstatus(current_tempWstatus);
        test_apis::put_lower_desired_temp(lower_desired_temp);
        test_apis::put_upper_desired_temp(upper_desired_temp);
        test_apis::put_regulator_mode(regulator_mode);

        // [SetInStateVars]: set the pre-state values of state variables
        test_apis::put_lastCmd(Old_lastCmd);

        // [InvokeEntryPoint]: invoke the entry point test method
        crate::thermostat_rt_mhs_mhs_timeTriggered();

        // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
        let api_heat_control = test_apis::get_heat_control();
        let lastCmd = test_apis::get_lastCmd();

        // [CheckPost]: invoke the oracle function
        assert!(GUMBOX::compute_CEP_Post(
        Old_lastCmd,
        lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp,
        api_heat_control));

            // example of manual testing
        assert!(api_heat_control == On_Off::Off);
        assert!(lastCmd == api_heat_control);
    }
}

#[test]
#[serial]
fn test_compute_REQ_MHS_4() {
    // initialize the app
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let current_tempWstatus = TempWstatus_i { degrees: 98, status: ValueStatus::Valid };
    let lower_desired_temp = Temp_i { degrees: 97 };
    let upper_desired_temp = Temp_i { degrees: 101 };
    let regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
    let Old_lastCmd: On_Off = On_Off::Onn;

    // [CheckPre]: check/filter based on pre-condition.
    if (!GUMBOX::compute_CEP_Pre(
        Old_lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp)) { 
            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }           
    } else {
        // [PutInPorts]: put values on the input ports
        test_apis::put_current_tempWstatus(current_tempWstatus);
        test_apis::put_lower_desired_temp(lower_desired_temp);
        test_apis::put_upper_desired_temp(upper_desired_temp);
        test_apis::put_regulator_mode(regulator_mode);


        // [SetInStateVars]: set the pre-state values of state variables
        test_apis::put_lastCmd(Old_lastCmd);

        // [InvokeEntryPoint]: invoke the entry point test method
        crate::thermostat_rt_mhs_mhs_timeTriggered();

        // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
        //let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
        let api_heat_control =  test_apis::get_heat_control();
        let lastCmd = test_apis::get_lastCmd();

        // [CheckPost]: invoke the oracle function
        assert!(GUMBOX::compute_CEP_Post(
        Old_lastCmd,
        lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp,
        api_heat_control));

            // example of manual testing
        assert!(api_heat_control == Old_lastCmd);
        assert!(lastCmd == api_heat_control);
}
}

#[test]
#[serial]
fn test_compute_REQ_MHS_5() {
    crate::thermostat_rt_mhs_mhs_initialize();

    // generate values for the incoming ports and state variables
    let current_tempWstatus = TempWstatus_i { degrees: 98, status: ValueStatus::Valid };
    let lower_desired_temp = Temp_i { degrees: 97 };
    let upper_desired_temp = Temp_i { degrees: 101 };
    let regulator_mode = Regulator_Mode::Failed_Regulator_Mode;
    let Old_lastCmd: On_Off = On_Off::Onn;

    // [CheckPre]: check/filter based on pre-condition.
    if (!GUMBOX::compute_CEP_Pre(
        Old_lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp)) { 
            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }           
    } else {
        // [PutInPorts]: put values on the input ports
        test_apis::put_current_tempWstatus(current_tempWstatus);
        test_apis::put_lower_desired_temp(lower_desired_temp);
        test_apis::put_upper_desired_temp(upper_desired_temp);
        test_apis::put_regulator_mode(regulator_mode);

        // [SetInStateVars]: set the pre-state values of state variables
        test_apis::put_lastCmd(Old_lastCmd);

        // [InvokeEntryPoint]: invoke the entry point test method
        crate::thermostat_rt_mhs_mhs_timeTriggered();

        // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
        let api_heat_control =  test_apis::get_heat_control();
        let lastCmd = test_apis::get_lastCmd();

        // [CheckPost]: invoke the oracle function
        assert!(GUMBOX::compute_CEP_Post(
        Old_lastCmd,
        lastCmd,
        current_tempWstatus,
        lower_desired_temp,
        regulator_mode,
        upper_desired_temp,
        api_heat_control));

            // example of manual testing
        assert!(api_heat_control == On_Off::Off);
        assert!(lastCmd == api_heat_control);
    }
}