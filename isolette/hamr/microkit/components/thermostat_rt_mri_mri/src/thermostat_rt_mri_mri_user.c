#include "thermostat_rt_mri_mri.h"

// This file will not be overwritten if codegen is rerun

const float DEFAULT_LOWER_DESIRED_TEMPERATURE = 97;
const float DEFAULT_UPPER_DESIRED_TEMPERATURE = 99;
const float DEFAULT_DISPLAYED_TEMPERATURE = 98;
const isolette_Isolette_Data_Model_Status_Type DEFAULT_REGULATOR_STATUS = Init_Status;
const bool DEFAULT_REGULATOR_INTERFACE_FAILURE_FLAG = false;

void thermostat_rt_mri_mri_initialize(void) {
  printf("%s: thermostat_rt_mri_mri_initialize invoked\n", microkit_name);

  // set initial upper desired temp
  const isolette_Isolette_Data_Model_Temp_i upper = { DEFAULT_UPPER_DESIRED_TEMPERATURE };
  put_upper_desired_temp(&upper);

  // set initial lower desired temp
  const isolette_Isolette_Data_Model_Temp_i lower = { DEFAULT_LOWER_DESIRED_TEMPERATURE };
  put_lower_desired_temp(&lower);
  
  // set initial displayed temp
  const isolette_Isolette_Data_Model_Temp_i displayedTemp = { DEFAULT_DISPLAYED_TEMPERATURE };
  put_displayed_temp(&displayedTemp);
  
  // set initial regulator status
  put_regulator_status(&DEFAULT_REGULATOR_STATUS);
  
  // set initial regulator interface failure flag
  const isolette_Isolette_Data_Model_Failure_Flag_i interface_failure = { DEFAULT_REGULATOR_INTERFACE_FAILURE_FLAG };
  put_interface_failure(&interface_failure);
}

void thermostat_rt_mri_mri_timeTriggered(void) {
  //printf("%s: thermostat_rt_mri_mri_timeTriggered invoked\n", microkit_name);

  // -------------- Get values of input ports ------------------
  
  isolette_Isolette_Data_Model_TempWstatus_i lower;
  get_lower_desired_tempWstatus(&lower);

  isolette_Isolette_Data_Model_TempWstatus_i upper;
  get_upper_desired_tempWstatus(&upper);

  isolette_Isolette_Data_Model_Regulator_Mode_Type regulator_mode;
  get_regulator_mode(&regulator_mode);

  isolette_Isolette_Data_Model_TempWstatus_i currentTemp;
  get_current_tempWstatus(&currentTemp);

  // =============================================
  //  Set values for Regulator Status (Table A-6)
  //  http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=101
  // =============================================  

  isolette_Isolette_Data_Model_Status_Type regulator_status = Init_Status;

  // =============================================
  //  Set values for Regulator Status
  // =============================================

  // Note that this code computes a function that is independent of the other
  // aspects of this thread.  This should be reflected in flow declarations.
  switch (regulator_mode) {
    case Init_Regulator_Mode:
      // REQ-MRI-1
      regulator_status = Init_Status;
      break;
    case Normal_Regulator_Mode:
      // REQ-MRI-2
      regulator_status = On_Status;
      break;
    case Failed_Regulator_Mode:
      // REQ-MRI-3
      regulator_status = Failed_Status;
      break;
  }

  put_regulator_status((const isolette_Isolette_Data_Model_Status_Type *) &regulator_status);

  // =============================================
  //  Set values for Display Temperature (Table A-6)
  // =============================================

  // Latency: < Max Operator Response Time
  // Tolerance: +/- 0.6 degrees F
  //

  isolette_Isolette_Data_Model_Temp_i display_temperature = { 0 };

  switch (regulator_mode) {
    case Normal_Regulator_Mode:
      // REQ-MRI-4
      display_temperature.degrees = currentTemp.degrees;
      break;

    // INIT, FAILED Modes

    // REQ-MRI
    //
    // leave the current temperature set to the default temperature value above.
    // This fulfills the requirement since the value when the Regulator Mode is not NORMAL
    // is unspecified.      

    case Init_Regulator_Mode:
      // do nothing
      break;
    case Failed_Regulator_Mode:
      // do nothing
      break;
  }

  put_displayed_temp(&display_temperature);

  // =============================================
  //  Set values for Regulator Interface Failure internal variable
  // =============================================

  // The interface_failure status defaults to TRUE (i.e., failing), which is the safe modality.  

  bool interface_failure = true;

  // Extract the value status from both the upper and lower alarm range
  isolette_Isolette_Data_Model_ValueStatus_Type upper_desired_temp_status = upper.status;
  isolette_Isolette_Data_Model_ValueStatus_Type lower_desired_temp_status = lower.status;

  // Set the Monitor Interface Failure value based on the status values of the
  //   upper and lower temperature
  if (!(upper_desired_temp_status == Valid) ||
     !(lower_desired_temp_status == Valid)) {
    // REQ-MRI-6
    interface_failure = true;
  } else {
    // REQ-MRI-7
    interface_failure = false;
  }

  // create the appropriately typed value to send on the output port and set the port value
  isolette_Isolette_Data_Model_Failure_Flag_i interface_failure_flag = { interface_failure };
  put_interface_failure(&interface_failure_flag);

  // =============================================
  //  Set values for Desired Range internal variable
  // =============================================

  if (!interface_failure) {
    // REQ-MRI-8
    const isolette_Isolette_Data_Model_Temp_i lowerDesiredTemp = { lower.degrees };
    const isolette_Isolette_Data_Model_Temp_i upperDesiredTemp = { upper.degrees };
    put_lower_desired_temp(&lowerDesiredTemp);
    put_upper_desired_temp(&upperDesiredTemp);
  } else {
    // REQ-MRI-9
    const isolette_Isolette_Data_Model_Temp_i lowerDesiredTemp = { 0 };
    const isolette_Isolette_Data_Model_Temp_i upperDesiredTemp = { 0 };
    put_lower_desired_temp(&lowerDesiredTemp);
    put_upper_desired_temp(&upperDesiredTemp);
  }  
}

void thermostat_rt_mri_mri_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
