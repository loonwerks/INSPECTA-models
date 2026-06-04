// This file will not be overwritten if HAMR codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::test::util::*;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::domain_monitor_process_domain_monitor_thread_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::domain_monitor_process_domain_monitor_thread_initialize();

    // populate incoming data ports
    test_apis::put_thermostat_rt_mri_mri_displayed_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_thermostat_rt_mri_mri_regulator_status(Isolette_Data_Model::Status::default());
    test_apis::put_thermostat_rt_mhs_mhs_heat_control(Isolette_Data_Model::On_Off::default());
    test_apis::put_thermostat_mt_mmi_mmi_monitor_status(Isolette_Data_Model::Status::default());
    test_apis::put_thermostat_mt_ma_ma_alarm_control(Isolette_Data_Model::On_Off::default());
    test_apis::put_operator_interface_oip_oit_lower_desired_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_operator_interface_oip_oit_upper_desired_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_operator_interface_oip_oit_lower_alarm_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_operator_interface_oip_oit_upper_alarm_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_temperature_sensor_cpi_thermostat_current_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_thermostat_rt_mri_mri_upper_desired_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_thermostat_rt_mri_mri_lower_desired_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_thermostat_rt_mri_mri_interface_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_thermostat_rt_mrm_mrm_regulator_mode(Isolette_Data_Model::Regulator_Mode::default());
    test_apis::put_thermostat_rt_drf_drf_internal_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_thermostat_mt_mmi_mmi_upper_alarm_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_thermostat_mt_mmi_mmi_lower_alarm_temp(Isolette_Data_Model::Temp_i::default());
    test_apis::put_thermostat_mt_mmi_mmi_interface_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_thermostat_mt_mmm_mmm_monitor_mode(Isolette_Data_Model::Monitor_Mode::default());
    test_apis::put_thermostat_mt_dmf_dmf_internal_failure(Isolette_Data_Model::Failure_Flag_i::default());

    crate::domain_monitor_process_domain_monitor_thread_timeTriggered();
  }
}
