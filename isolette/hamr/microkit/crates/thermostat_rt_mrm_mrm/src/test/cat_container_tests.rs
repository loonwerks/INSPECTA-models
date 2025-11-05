use data::*;
use serial_test::serial;
use crate::bridge::thermostat_rt_mrm_mrm_GUMBOX as GUMBOX;
use crate::test::util::*;
use data::Isolette_Data_Model::*;

#[test]
#[serial]
fn test_REQ_MRM_Maintain_Normal_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Normal_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Valid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_2_init_to_normal_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Init_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Valid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_3_normal_to_failed_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Normal_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Invalid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_4_init_to_failed_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Init_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Valid
        },
        api_interface_failure: Failure_Flag_i { flag: true },
        api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_Maintain_Failed_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Failed_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Valid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_3_normal_to_failed_multiple_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Normal_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Invalid
        },
        api_interface_failure: Failure_Flag_i { flag: true },
        api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_3_normal_to_failed_internal_failure_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Normal_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Valid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_4_init_to_failed_internal_failure_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Init_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Valid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_3_normal_to_failed_invalid_and_interface_failure_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Normal_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Invalid
        },
        api_interface_failure: Failure_Flag_i { flag: true },
        api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_4_init_to_failed_invalid_and_internal_failure_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Init_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Invalid
        },
        api_interface_failure: Failure_Flag_i { flag: false },
        api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MRM_Maintain_Failed_invalid_status_container() {
    crate::thermostat_rt_mrm_mrm_initialize();

    let container = test_apis::PreStateContainer_wGSV {
        In_lastRegulatorMode: Regulator_Mode::Failed_Regulator_Mode,
        api_current_tempWstatus: TempWstatus_i {
            degrees: 96,
            status: ValueStatus::Invalid
        },
        api_interface_failure: Failure_Flag_i { flag: true },
        api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_apis::testComputeCBwGSV_container(container) {
    cb_apis::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}