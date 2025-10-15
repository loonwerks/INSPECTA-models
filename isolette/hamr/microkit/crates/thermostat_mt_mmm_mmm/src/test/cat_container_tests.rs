use serial_test::serial;
use data::*;
use crate::bridge::thermostat_mt_mmm_mmm_GUMBOX as GUMBOX;
use crate::test::util::test_apis as test_api;
use crate::test::util::cb_apis as cb_api;
use data::Isolette_Data_Model::*; 

#[test]
#[serial]
fn test_REQ_MMM_2_Transition_Init_to_Normal_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Init_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Valid 
    },
    api_interface_failure: Failure_Flag_i { flag: false },
    api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_invalid_current_temp_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Invalid 
    },
    api_interface_failure: Failure_Flag_i { flag: false },
    api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_interface_failure_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Valid 
    },
    api_interface_failure: Failure_Flag_i { flag: true },
    api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_internal_failure_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Valid 
    },
    api_interface_failure: Failure_Flag_i { flag: false },
    api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_invalid_current_temp_interface_failure_container() { 
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Invalid 
    },
    api_interface_failure: Failure_Flag_i { flag: true },
    api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_invalid_current_temp_internal_failure_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Invalid 
    },
    api_interface_failure: Failure_Flag_i { flag: false },
    api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_interface_failure_internal_failure_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Valid 
    },
    api_interface_failure: Failure_Flag_i { flag: true },
    api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_invalid_current_temp_interface_failure_internal_failure_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Invalid 
    },
    api_interface_failure: Failure_Flag_i { flag: true },
    api_internal_failure: Failure_Flag_i { flag: true }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}

#[test]
#[serial]
fn test_REQ_MMM_3_Maintain_Normal_container() {
    crate::thermostat_mt_mmm_mmm_initialize();

    let container = test_api::PreStateContainer_wGSV {
    In_lastMonitorMode: Monitor_Mode::Normal_Monitor_Mode,
    api_current_tempWstatus: TempWstatus_i { 
        degrees: 96, 
        status: ValueStatus::Valid 
    },
    api_interface_failure: Failure_Flag_i { flag: false },
    api_internal_failure: Failure_Flag_i { flag: false }
    };

    match cb_api::testComputeCBwGSV_container(container) {
    cb_api::HarnessResult::Passed => {}
        _ => { assert!(false); }
    }
}