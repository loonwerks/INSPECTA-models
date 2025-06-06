// #Sireum

package isolette.Regulate

import org.sireum._
import isolette._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun
object Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_GumboX {
  /** Initialize Entrypoint Contract
    *
    * guarantee REQ_MRM_1
    *   The initial mode of the regular is INIT
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def initialize_REQ_MRM_1 (
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    api_regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode

  /** IEP-Guar: Initialize Entrypoint Contracts for mrm
    *
    * @param lastRegulatorMode post-state state variable
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def initialize_IEP_Guar (
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    initialize_REQ_MRM_1(api_regulator_mode)

  /** IEP-Post: Initialize Entrypoint Post-Condition
    *
    * @param lastRegulatorMode post-state state variable
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def inititialize_IEP_Post (
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (// IEP-Guar: Initialize Entrypoint contract for mrm
     initialize_IEP_Guar(lastRegulatorMode, api_regulator_mode))

  /** IEP-Post: Initialize Entrypoint Post-Condition via container
    *
    * @param post Container holding the value of incoming ports and the pre-state values of state variables
    */
  @strictpure def inititialize_IEP_Post_Container (post: Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS): B =
    inititialize_IEP_Post (
      lastRegulatorMode = post.lastRegulatorMode,
      api_regulator_mode = post.api_regulator_mode)

  /** CEP-Pre: Compute Entrypoint Pre-Condition for mrm
    *
    * @param In_lastRegulatorMode pre-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    */
  @strictpure def compute_CEP_Pre (
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i): B =
    (// D-Inv-Guard: Datatype invariants for the types associated with mrm's state variables and incoming ports
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_current_tempWstatus))

  /** CEP-Pre: Compute Entrypoint Pre-Condition for mrm via container
    *
    * @param pre Container holding the value of incoming ports and the pre-state values of state variables
    */
  @strictpure def compute_CEP_Pre_Container(pre: Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS): B =
    compute_CEP_Pre(
      In_lastRegulatorMode = pre.In_lastRegulatorMode,
      api_current_tempWstatus = pre.api_current_tempWstatus,
      api_interface_failure = pre.api_interface_failure,
      api_internal_failure = pre.api_internal_failure)

  /** guarantee REQ_MRM_2
    *   'transition from Init to Normal'
    *   If the current regulator mode is Init, then
    *   the regulator mode is set to NORMAL iff the regulator status is valid (see Table A-10), i.e.,
    *     if NOT (Regulator Interface Failure OR Regulator Internal Failure)
    *        AND Current Temperature.Status = Valid
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_case_REQ_MRM_2(
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (In_lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) ___>:
      (!(api_interface_failure.flag || api_internal_failure.flag) &&
         api_current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid ___>:
         api_regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &&
           lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode)

  /** guarantee REQ_MRM_Maintain_Normal
    *   'maintaining NORMAL, NORMAL to NORMAL'
    *   If the current regulator mode is Normal, then
    *   the regulator mode is stays normal iff
    *   the regulaor status is not false i.e.,
    *          if NOT(
    *              (Regulator Interface Failure OR Regulator Internal Failure)
    *              OR NOT(Current Temperature.Status = Valid)
    *          )
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_case_REQ_MRM_Maintain_Normal(
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (In_lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) ___>:
      (!(api_interface_failure.flag || api_internal_failure.flag) &&
         api_current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid ___>:
         api_regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &&
           lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode)

  /** guarantee REQ_MRM_3
    *   'transition for NORMAL to FAILED'
    *   If the current regulator mode is Normal, then
    *   the regulator mode is set to Failed iff
    *   the regulator status is false, i.e.,
    *      if  (Regulator Interface Failure OR Regulator Internal Failure)
    *          OR NOT(Current Temperature.Status = Valid)
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_case_REQ_MRM_3(
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (In_lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) ___>:
      ((api_interface_failure.flag || api_internal_failure.flag) &&
         api_current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid ___>:
         api_regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
           lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)

  /** guarantee REQ_MRM_4
    *   'transition from INIT to FAILED' 
    *   If the current regulator mode is Init, then
    *   the regulator mode and lastRegulatorMode state value is set to Failed iff
    *   the regulator status is false, i.e.,
    *          if  (Regulator Interface Failure OR Regulator Internal Failure)
    *          OR NOT(Current Temperature.Status = Valid)
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_case_REQ_MRM_4(
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (In_lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) ___>:
      ((api_interface_failure.flag || api_internal_failure.flag) &&
         api_current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid ___>:
         api_regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
           lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)

  /** guarantee REQ_MRM_MaintainFailed
    *   'maintaining FAIL, FAIL to FAIL'
    *   If the current regulator mode is Failed, then
    *   the regulator mode remains in the Failed state and the LastRegulator mode remains Failed.REQ-MRM-Maintain-Failed
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_case_REQ_MRM_MaintainFailed(
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (In_lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode) ___>:
      (api_regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
         lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)

  /** CEP-T-Case: Top-Level case contracts for mrm's compute entrypoint
    *
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_CEP_T_Case (
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    compute_case_REQ_MRM_2(In_lastRegulatorMode, lastRegulatorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure, api_regulator_mode) &
    compute_case_REQ_MRM_Maintain_Normal(In_lastRegulatorMode, lastRegulatorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure, api_regulator_mode) &
    compute_case_REQ_MRM_3(In_lastRegulatorMode, lastRegulatorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure, api_regulator_mode) &
    compute_case_REQ_MRM_4(In_lastRegulatorMode, lastRegulatorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure, api_regulator_mode) &
    compute_case_REQ_MRM_MaintainFailed(In_lastRegulatorMode, lastRegulatorMode, api_regulator_mode)

  /** CEP-Post: Compute Entrypoint Post-Condition for mrm
    *
    * @param In_lastRegulatorMode pre-state state variable
    * @param lastRegulatorMode post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    * @param api_regulator_mode outgoing data port
    */
  @strictpure def compute_CEP_Post (
      In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_internal_failure: Isolette_Data_Model.Failure_Flag_i,
      api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): B =
    (// D-Inv-Guard: Datatype invariants for the types associated with mrm's state variables and outgoing ports
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_current_tempWstatus) & 

     // CEP-T-Case: case clauses of mrm's compute entrypoint
     compute_CEP_T_Case (In_lastRegulatorMode, lastRegulatorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure, api_regulator_mode))

  /** CEP-Post: Compute Entrypoint Post-Condition for mrm via containers
    *
    * @param pre Container holding the values of incoming ports and the pre-state values of state variables
    * @param post Container holding the values of outgoing ports and the post-state values of state variables
    */
  @strictpure def compute_CEP_Post_Container(
      pre: Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS,
      post: Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS): B =
    compute_CEP_Post(
      In_lastRegulatorMode = pre.In_lastRegulatorMode,
      lastRegulatorMode = post.lastRegulatorMode,
      api_current_tempWstatus = pre.api_current_tempWstatus,
      api_interface_failure = pre.api_interface_failure,
      api_internal_failure = pre.api_internal_failure,
      api_regulator_mode = post.api_regulator_mode)
}
