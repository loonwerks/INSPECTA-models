#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(unused_parens)]

use crate::types::sb_aadl_types::*;

  /** Initialize Entrypoint Contract
    *
    * guarantee RegulatorStatusIsInitiallyInit
    * @param api_regulator_status outgoing data port
    */
    fn initialize_RegulatorStatusIsInitiallyInit (
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type) -> bool {
      api_regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status
    }
  
    /** IEP-Guar: Initialize Entrypoint Contracts for mri
      *
      * @param api_displayed_temp outgoing data port
      * @param api_interface_failure outgoing data port
      * @param api_lower_desired_temp outgoing data port
      * @param api_regulator_status outgoing data port
      * @param api_upper_desired_temp outgoing data port
      */
    fn initialize_IEP_Guar (
        api_displayed_temp: isolette_Isolette_Data_Model_Temp_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i,
        api_lower_desired_temp: isolette_Isolette_Data_Model_Temp_i,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type,
        api_upper_desired_temp: isolette_Isolette_Data_Model_Temp_i) -> bool {
      initialize_RegulatorStatusIsInitiallyInit(api_regulator_status)
    }
  
    /** IEP-Post: Initialize Entrypoint Post-Condition
      *
      * @param api_displayed_temp outgoing data port
      * @param api_interface_failure outgoing data port
      * @param api_lower_desired_temp outgoing data port
      * @param api_regulator_status outgoing data port
      * @param api_upper_desired_temp outgoing data port
      */
    pub fn inititialize_IEP_Post (
        api_displayed_temp: isolette_Isolette_Data_Model_Temp_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i,
        api_lower_desired_temp: isolette_Isolette_Data_Model_Temp_i,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type,
        api_upper_desired_temp: isolette_Isolette_Data_Model_Temp_i) -> bool {
      // IEP-Guar: Initialize Entrypoint contract for mri
      initialize_IEP_Guar(api_displayed_temp, api_interface_failure, api_lower_desired_temp, api_regulator_status, api_upper_desired_temp) 
    }
  
    /** Compute Entrypoint Contract
      *
      * assumes lower_is_not_higher_than_upper
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      */
    fn compute_spec_lower_is_not_higher_than_upper_assume(
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i) -> bool {
      api_lower_desired_tempWstatus.degrees <= api_upper_desired_tempWstatus.degrees
    }
  
    /** CEP-T-Assm: Top-level assume contracts for mri's compute entrypoint
      *
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      */
    fn compute_CEP_T_Assm (
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i) -> bool {
      compute_spec_lower_is_not_higher_than_upper_assume(api_lower_desired_tempWstatus, api_upper_desired_tempWstatus)
    }
  
    /** CEP-Pre: Compute Entrypoint Pre-Condition for mri
      *
      * @param api_current_tempWstatus incoming data port
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_regulator_mode incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      */
    pub fn compute_CEP_Pre (
        api_current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i) -> bool {
      (// CEP-Assm: assume clauses of mri's compute entrypoint
       compute_CEP_T_Assm (api_lower_desired_tempWstatus, api_upper_desired_tempWstatus))
    }

    /** guarantee REQ_MRI_1
      *   If the Regulator Mode is INIT,
      *   the Regulator Status shall be set to Init.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
      * @param api_regulator_mode incoming data port
      * @param api_regulator_status outgoing data port
      */
    fn compute_case_REQ_MRI_1(
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type) -> bool {
      !(api_regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode) ||
        (api_regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status)
    }
  
    /** guarantee REQ_MRI_2
      *   If the Regulator Mode is NORMAL,
      *   the Regulator Status shall be set to On
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
      * @param api_regulator_mode incoming data port
      * @param api_regulator_status outgoing data port
      */
    fn compute_case_REQ_MRI_2(
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type) -> bool {
      !(api_regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode) || 
        (api_regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status)
    }
  
    /** guarantee REQ_MRI_3
      *   If the Regulator Mode is FAILED,
      *   the Regulator Status shall be set to Failed.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
      * @param api_regulator_mode incoming data port
      * @param api_regulator_status outgoing data port
      */
    fn compute_case_REQ_MRI_3(
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type) -> bool {
      !(api_regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Failed_Regulator_Mode) ||
        (api_regulator_status == isolette_Isolette_Data_Model_Status_Type::Failed_Status)
    }
  
    /** guarantee REQ_MRI_4
      *   If the Regulator Mode is NORMAL, the
      *   Display Temperature shall be set to the value of the
      *   Current Temperature rounded to the nearest integer.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
      * @param api_current_tempWstatus incoming data port
      * @param api_regulator_mode incoming data port
      * @param api_displayed_temp outgoing data port
      */
    fn compute_case_REQ_MRI_4(
        api_current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_displayed_temp: isolette_Isolette_Data_Model_Temp_i) -> bool {
      !(api_regulator_mode == isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode) ||
        (api_displayed_temp.degrees == api_current_tempWstatus.degrees)
    }
  
    /** guarantee REQ_MRI_5
      *   If the Regulator Mode is not NORMAL,
      *   the value of the Display Temperature is UNSPECIFIED.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
      */
    fn compute_case_REQ_MRI_5(
        )-> bool {
      !(true) ||
        (true)
    }
  
    /** guarantee REQ_MRI_6
      *   If the Status attribute of the Lower Desired Temperature
      *   or the Upper Desired Temperature is Invalid,
      *   the Regulator Interface Failure shall be set to True.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
      * @param api_upper_desired_tempWstatus incoming data port
      * @param api_interface_failure outgoing data port
      */
    fn compute_case_REQ_MRI_6(
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i) -> bool {
      !((api_upper_desired_tempWstatus.status != isolette_Isolette_Data_Model_ValueStatus_Type::Valid ||
         api_upper_desired_tempWstatus.status != isolette_Isolette_Data_Model_ValueStatus_Type::Valid)) ||
        (api_interface_failure.flag)
    }
  
    /** guarantee REQ_MRI_7
      *   If the Status attribute of the Lower Desired Temperature
      *   and the Upper Desired Temperature is Valid,
      *   the Regulator Interface Failure shall be set to False.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      * @param api_interface_failure outgoing data port
      */
    fn compute_case_REQ_MRI_7(
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i) -> bool {
      !(true) ||
        (api_interface_failure.flag == !((api_upper_desired_tempWstatus.status == isolette_Isolette_Data_Model_ValueStatus_Type::Valid) &
            (api_lower_desired_tempWstatus.status == isolette_Isolette_Data_Model_ValueStatus_Type::Valid)))
    }
  
    /** guarantee REQ_MRI_8
      *   If the Regulator Interface Failure is False
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      * @param api_interface_failure outgoing data port
      * @param api_lower_desired_temp outgoing data port
      * @param api_upper_desired_temp outgoing data port
      */
    fn compute_case_REQ_MRI_8(
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i,
        api_lower_desired_temp: isolette_Isolette_Data_Model_Temp_i,
        api_upper_desired_temp: isolette_Isolette_Data_Model_Temp_i) -> bool {
      !(true) ||
        (!(!(api_interface_failure.flag)) || 
            ((api_lower_desired_temp.degrees == api_lower_desired_tempWstatus.degrees) &
             (api_upper_desired_temp.degrees == api_upper_desired_tempWstatus.degrees)))
    }
  
    /** guarantee REQ_MRI_9
      *   If the Regulator Interface Failure is True,
      *   the Desired Range is UNSPECIFIED.
      *   the Desired Range shall be set to the Desired Temperature Range.
      *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
      */
    fn compute_case_REQ_MRI_9(
        )-> bool {
      !(true) ||
        (true)
    }
  
    /** CEP-T-Case: Top-Level case contracts for mri's compute entrypoint
      *
      * @param api_current_tempWstatus incoming data port
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_regulator_mode incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      * @param api_displayed_temp outgoing data port
      * @param api_interface_failure outgoing data port
      * @param api_lower_desired_temp outgoing data port
      * @param api_regulator_status outgoing data port
      * @param api_upper_desired_temp outgoing data port
      */
    fn compute_CEP_T_Case (
        api_current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_displayed_temp: isolette_Isolette_Data_Model_Temp_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i,
        api_lower_desired_temp: isolette_Isolette_Data_Model_Temp_i,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type,
        api_upper_desired_temp: isolette_Isolette_Data_Model_Temp_i) -> bool {
      compute_case_REQ_MRI_1(api_regulator_mode, api_regulator_status) &
      compute_case_REQ_MRI_2(api_regulator_mode, api_regulator_status) &
      compute_case_REQ_MRI_3(api_regulator_mode, api_regulator_status) &
      compute_case_REQ_MRI_4(api_current_tempWstatus, api_regulator_mode, api_displayed_temp) &
      compute_case_REQ_MRI_5() &
      compute_case_REQ_MRI_6(api_upper_desired_tempWstatus, api_interface_failure) &
      compute_case_REQ_MRI_7(api_lower_desired_tempWstatus, api_upper_desired_tempWstatus, api_interface_failure) &
      compute_case_REQ_MRI_8(api_lower_desired_tempWstatus, api_upper_desired_tempWstatus, api_interface_failure, api_lower_desired_temp, api_upper_desired_temp) &
      compute_case_REQ_MRI_9()
    }
  
    /** CEP-Post: Compute Entrypoint Post-Condition for mri
      *
      * @param api_current_tempWstatus incoming data port
      * @param api_lower_desired_tempWstatus incoming data port
      * @param api_regulator_mode incoming data port
      * @param api_upper_desired_tempWstatus incoming data port
      * @param api_displayed_temp outgoing data port
      * @param api_interface_failure outgoing data port
      * @param api_lower_desired_temp outgoing data port
      * @param api_regulator_status outgoing data port
      * @param api_upper_desired_temp outgoing data port
      */
    pub fn compute_CEP_Post (
        api_current_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_lower_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_regulator_mode: isolette_Isolette_Data_Model_Regulator_Mode_Type,
        api_upper_desired_tempWstatus: isolette_Isolette_Data_Model_TempWstatus_i,
        api_displayed_temp: isolette_Isolette_Data_Model_Temp_i,
        api_interface_failure: isolette_Isolette_Data_Model_Failure_Flag_i,
        api_lower_desired_temp: isolette_Isolette_Data_Model_Temp_i,
        api_regulator_status: isolette_Isolette_Data_Model_Status_Type,
        api_upper_desired_temp: isolette_Isolette_Data_Model_Temp_i) -> bool {
      (// CEP-T-Case: case clauses of mri's compute entrypoint
       compute_CEP_T_Case (api_current_tempWstatus, api_lower_desired_tempWstatus, api_regulator_mode, api_upper_desired_tempWstatus, api_displayed_temp, api_interface_failure, api_lower_desired_temp, api_regulator_status, api_upper_desired_temp))
    }
