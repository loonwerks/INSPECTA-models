use data::*;
use serial_test::serial;
use crate::bridge::thermostat_rt_mri_mri_GUMBOX as GUMBOX;
use crate::test::util::test_apis as test_api;
use crate::test::util::cb_apis as cb_api;
use data::Isolette_Data_Model::*;

#[test]
#[serial]
fn test_compute_normal_container() {
    crate::thermostat_rt_mri_mri_initialize();

    let container = test_api::PreStateContainer {
    api_upper_desired_tempWstatus: TempWstatus_i {
        degrees: 101,
        status: ValueStatus::Valid
    },
    api_lower_desired_tempWstatus: TempWstatus_i {
        degrees: 98,
        status: ValueStatus::Valid
    },
    api_current_tempWstatus: TempWstatus_i {
        degrees: 99,
        status: ValueStatus::Valid
    },
    api_regulator_mode: Regulator_Mode::Normal_Regulator_Mode
    };

    match cb_api::testComputeCB_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_compute_interface_failure_container() {
    crate::thermostat_rt_mri_mri_initialize();

    let container = test_api::PreStateContainer {
    api_upper_desired_tempWstatus: TempWstatus_i {
        degrees: 101,
        status: ValueStatus::Valid
    },
    api_lower_desired_tempWstatus: TempWstatus_i {
        degrees: 98,
        status: ValueStatus::Invalid
    },
    api_current_tempWstatus: TempWstatus_i {
        degrees: 99,
        status: ValueStatus::Valid
    },
    api_regulator_mode: Regulator_Mode::Normal_Regulator_Mode
    };

    match cb_api::testComputeCB_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}