// This file will not be overwritten if codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::test::util::*;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::monitor_process_monitor_thread_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::monitor_process_monitor_thread_initialize();

    // populate incoming data ports
    test_apis::put_mri_mri_displayed_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_mri_mri_regulator_status(Isolette_Data_Model::Status::default());
    test_apis::put_mhs_mhs_heat_control(Isolette_Data_Model::On_Off::default());
    test_apis::put_mmi_mmi_monitor_status(Isolette_Data_Model::Status::default());
    test_apis::put_ma_ma_alarm_control(Isolette_Data_Model::On_Off::default());
    test_apis::put_oip_oit_lower_desired_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_oip_oit_upper_desired_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_oip_oit_lower_alarm_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_oip_oit_upper_alarm_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_cpi_thermostat_current_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_mri_mri_upper_desired_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_mri_mri_lower_desired_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_mri_mri_interface_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_mrm_mrm_regulator_mode(Isolette_Data_Model::Regulator_Mode::default());
    test_apis::put_drf_drf_internal_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_mmi_mmi_upper_alarm_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_mmi_mmi_lower_alarm_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_mmi_mmi_interface_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_mmm_mmm_monitor_mode(Isolette_Data_Model::Monitor_Mode::default());
    test_apis::put_dmf_dmf_internal_failure(Isolette_Data_Model::Failure_Flag_i::default());

    crate::monitor_process_monitor_thread_timeTriggered();
  }
}

mod GUMBOX_tests {
  use serial_test::serial;
  use proptest::prelude::*;

  use crate::test::util::*;
  use crate::testInitializeCB_macro;
  use crate::testComputeCB_macro;

  // number of valid (i.e., non-rejected) test cases that must be executed for the compute method.
  const numValidComputeTestCases: u32 = 100;

  // how many total test cases (valid + rejected) that may be attempted.
  //   0 means all inputs must satisfy the precondition (if present),
  //   5 means at most 5 rejected inputs are allowed per valid test case
  const computeRejectRatio: u32 = 5;

  const verbosity: u32 = 2;

  testInitializeCB_macro! {
    prop_testInitializeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    }
  }

  testComputeCB_macro! {
    prop_testComputeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    },
    // strategies for generating each component input
    api_cpi_thermostat_current_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_dmf_dmf_internal_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_drf_drf_internal_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_ma_ma_alarm_control: generators::Isolette_Data_Model_On_Off_strategy_default(),
    api_mhs_mhs_heat_control: generators::Isolette_Data_Model_On_Off_strategy_default(),
    api_mmi_mmi_interface_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_mmi_mmi_lower_alarm_temp: generators::Isolette_Data_Model_Temp_i_strategy_default(),
    api_mmi_mmi_monitor_status: generators::Isolette_Data_Model_Status_strategy_default(),
    api_mmi_mmi_upper_alarm_temp: generators::Isolette_Data_Model_Temp_i_strategy_default(),
    api_mmm_mmm_monitor_mode: generators::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_mri_mri_displayed_temp: generators::Isolette_Data_Model_Temp_i_strategy_default(),
    api_mri_mri_interface_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_mri_mri_lower_desired_temp: generators::Isolette_Data_Model_Temp_i_strategy_default(),
    api_mri_mri_regulator_status: generators::Isolette_Data_Model_Status_strategy_default(),
    api_mri_mri_upper_desired_temp: generators::Isolette_Data_Model_Temp_i_strategy_default(),
    api_mrm_mrm_regulator_mode: generators::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_oip_oit_lower_alarm_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_oip_oit_lower_desired_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_oip_oit_upper_alarm_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_oip_oit_upper_desired_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default()
  }
}
