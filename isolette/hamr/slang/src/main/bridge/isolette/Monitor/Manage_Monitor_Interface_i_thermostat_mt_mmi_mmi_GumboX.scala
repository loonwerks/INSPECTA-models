// #Sireum

package isolette.Monitor

import org.sireum._
import isolette._
import org.sireum.S32._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun
object Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_GumboX {
  /** I-Assm: Integration constraint on mmi's incoming data port upper_alarm_tempWstatus
    *
    * assume Table_A_12_UpperAlarmTemp
    *   Range [97..102]
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 
    */
  @strictpure def I_Assm_upper_alarm_tempWstatus(upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i): B =
    s32"97" <= upper_alarm_tempWstatus.degrees &&
      upper_alarm_tempWstatus.degrees <= s32"102"

  // I-Assm-Guard: Integration constraint on mmi's incoming data port upper_alarm_tempWstatus
  @strictpure def I_Assm_Guard_upper_alarm_tempWstatus(upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i): B =
    I_Assm_upper_alarm_tempWstatus(upper_alarm_tempWstatus)

  /** I-Assm: Integration constraint on mmi's incoming data port lower_alarm_tempWstatus
    *
    * assume Table_A_12_LowerAlarmTemp
    *   Range [96..101]
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 
    */
  @strictpure def I_Assm_lower_alarm_tempWstatus(lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i): B =
    s32"96" <= lower_alarm_tempWstatus.degrees &&
      lower_alarm_tempWstatus.degrees <= s32"101"

  // I-Assm-Guard: Integration constraint on mmi's incoming data port lower_alarm_tempWstatus
  @strictpure def I_Assm_Guard_lower_alarm_tempWstatus(lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i): B =
    I_Assm_lower_alarm_tempWstatus(lower_alarm_tempWstatus)

  /** Initialize Entrypoint Contract
    *
    * guarantee monitorStatusInitiallyInit
    * @param api_monitor_status outgoing data port
    */
  @strictpure def initialize_monitorStatusInitiallyInit (
      api_monitor_status: Isolette_Data_Model.Status.Type): B =
    api_monitor_status == Isolette_Data_Model.Status.Init_Status

  /** IEP-Guar: Initialize Entrypoint Contracts for mmi
    *
    * @param lastCmd post-state state variable
    * @param api_interface_failure outgoing data port
    * @param api_lower_alarm_temp outgoing data port
    * @param api_monitor_status outgoing data port
    * @param api_upper_alarm_temp outgoing data port
    */
  @strictpure def initialize_IEP_Guar (
      lastCmd: Isolette_Data_Model.On_Off.Type,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_lower_alarm_temp: Isolette_Data_Model.Temp_i,
      api_monitor_status: Isolette_Data_Model.Status.Type,
      api_upper_alarm_temp: Isolette_Data_Model.Temp_i): B =
    initialize_monitorStatusInitiallyInit(api_monitor_status)

  /** IEP-Post: Initialize Entrypoint Post-Condition
    *
    * @param lastCmd post-state state variable
    * @param api_interface_failure outgoing data port
    * @param api_lower_alarm_temp outgoing data port
    * @param api_monitor_status outgoing data port
    * @param api_upper_alarm_temp outgoing data port
    */
  @strictpure def inititialize_IEP_Post (
      lastCmd: Isolette_Data_Model.On_Off.Type,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_lower_alarm_temp: Isolette_Data_Model.Temp_i,
      api_monitor_status: Isolette_Data_Model.Status.Type,
      api_upper_alarm_temp: Isolette_Data_Model.Temp_i): B =
    (// D-Inv-Guard: Datatype invariants for the types associated with mmi's state variables and outgoing ports
     Isolette_Data_Model.Temp_i.D_Inv_Temp_i(api_lower_alarm_temp) &
     Isolette_Data_Model.Temp_i.D_Inv_Temp_i(api_upper_alarm_temp) & 

     // IEP-Guar: Initialize Entrypoint contract for mmi
     initialize_IEP_Guar(lastCmd, api_interface_failure, api_lower_alarm_temp, api_monitor_status, api_upper_alarm_temp))

  /** IEP-Post: Initialize Entrypoint Post-Condition via container
    *
    * @param post Container holding the value of incoming ports and the pre-state values of state variables
    */
  @strictpure def inititialize_IEP_Post_Container (post: Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS): B =
    inititialize_IEP_Post (
      lastCmd = post.lastCmd,
      api_interface_failure = post.api_interface_failure,
      api_lower_alarm_temp = post.api_lower_alarm_temp,
      api_monitor_status = post.api_monitor_status,
      api_upper_alarm_temp = post.api_upper_alarm_temp)

  /** CEP-Pre: Compute Entrypoint Pre-Condition for mmi
    *
    * @param In_lastCmd pre-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_lower_alarm_tempWstatus incoming data port
    * @param api_monitor_mode incoming data port
    * @param api_upper_alarm_tempWstatus incoming data port
    */
  @strictpure def compute_CEP_Pre (
      In_lastCmd: Isolette_Data_Model.On_Off.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_monitor_mode: Isolette_Data_Model.Monitor_Mode.Type,
      api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i): B =
    (// D-Inv-Guard: Datatype invariants for the types associated with mmi's state variables and incoming ports
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_current_tempWstatus) & 
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_lower_alarm_tempWstatus) & 
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_upper_alarm_tempWstatus) & 

     // I-Assm-Guard: Integration constraints for mmi's incoming ports
     I_Assm_Guard_upper_alarm_tempWstatus(api_upper_alarm_tempWstatus) & 
     I_Assm_Guard_lower_alarm_tempWstatus(api_lower_alarm_tempWstatus))

  /** CEP-Pre: Compute Entrypoint Pre-Condition for mmi via container
    *
    * @param pre Container holding the value of incoming ports and the pre-state values of state variables
    */
  @strictpure def compute_CEP_Pre_Container(pre: Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS): B =
    compute_CEP_Pre(
      In_lastCmd = pre.In_lastCmd,
      api_current_tempWstatus = pre.api_current_tempWstatus,
      api_lower_alarm_tempWstatus = pre.api_lower_alarm_tempWstatus,
      api_monitor_mode = pre.api_monitor_mode,
      api_upper_alarm_tempWstatus = pre.api_upper_alarm_tempWstatus)

  /** guarantee REQ_MMI_1
    *   If the Manage Monitor Interface mode is INIT,
    *   the Monitor Status shall be set to Init.
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_monitor_mode incoming data port
    * @param api_monitor_status outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_1(
      api_monitor_mode: Isolette_Data_Model.Monitor_Mode.Type,
      api_monitor_status: Isolette_Data_Model.Status.Type): B =
    (api_monitor_mode == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) ___>:
      (api_monitor_status == Isolette_Data_Model.Status.Init_Status)

  /** guarantee REQ_MMI_2
    *   If the Manage Monitor Interface mode is NORMAL,
    *   the Monitor Status shall be set to On
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_monitor_mode incoming data port
    * @param api_monitor_status outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_2(
      api_monitor_mode: Isolette_Data_Model.Monitor_Mode.Type,
      api_monitor_status: Isolette_Data_Model.Status.Type): B =
    (api_monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode) ___>:
      (api_monitor_status == Isolette_Data_Model.Status.On_Status)

  /** guarantee REQ_MMI_3
    *   If the Manage Monitor Interface mode is FAILED,
    *   the Monitor Status shall be set to Failed.
    *   Latency: < Max Operator Response Time
    *   Tolerance: N/A
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_monitor_mode incoming data port
    * @param api_monitor_status outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_3(
      api_monitor_mode: Isolette_Data_Model.Monitor_Mode.Type,
      api_monitor_status: Isolette_Data_Model.Status.Type): B =
    (api_monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode) ___>:
      (api_monitor_status == Isolette_Data_Model.Status.Failed_Status)

  /** guarantee REQ_MMI_4
    *   If the Status attribute of the Lower Alarm Temperature
    *   or the Upper Alarm Temperature is Invalid,
    *   the Monitor Interface Failure shall be set to True
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_lower_alarm_tempWstatus incoming data port
    * @param api_upper_alarm_tempWstatus incoming data port
    * @param api_interface_failure outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_4(
      api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i): B =
    (api_lower_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Invalid |
      api_upper_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Invalid) ___>:
      (api_interface_failure.flag)

  /** guarantee REQ_MMI_5
    *   If the Status attribute of the Lower Alarm Temperature
    *   and the Upper Alarm Temperature is Valid,
    *   the Monitor Interface Failure shall be set to False
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_lower_alarm_tempWstatus incoming data port
    * @param api_upper_alarm_tempWstatus incoming data port
    * @param api_interface_failure outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_5(
      api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i): B =
    (api_lower_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid &
      api_upper_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid) ___>:
      (!(api_interface_failure.flag))

  /** guarantee REQ_MMI_6
    *   If the Monitor Interface Failure is False,
    *   the Alarm Range variable shall be set to the Desired Temperature Range
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_lower_alarm_tempWstatus incoming data port
    * @param api_upper_alarm_tempWstatus incoming data port
    * @param api_interface_failure outgoing data port
    * @param api_lower_alarm_temp outgoing data port
    * @param api_upper_alarm_temp outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_6(
      api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_lower_alarm_temp: Isolette_Data_Model.Temp_i,
      api_upper_alarm_temp: Isolette_Data_Model.Temp_i): B =
    (T) ___>:
      (!(api_interface_failure.flag) ___>:
         api_lower_alarm_temp.degrees == api_lower_alarm_tempWstatus.degrees &
           api_upper_alarm_temp.degrees == api_upper_alarm_tempWstatus.degrees)

  /** guarantee REQ_MMI_7
    *   If the Monitor Interface Failure is True,
    *   the Alarm Range variable is UNSPECIFIED
    *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
    * @param api_interface_failure outgoing data port
    */
  @strictpure def compute_case_REQ_MMI_7(
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i): B =
    (T) ___>:
      (api_interface_failure.flag ___>: T)

  /** CEP-T-Case: Top-Level case contracts for mmi's compute entrypoint
    *
    * @param api_lower_alarm_tempWstatus incoming data port
    * @param api_monitor_mode incoming data port
    * @param api_upper_alarm_tempWstatus incoming data port
    * @param api_interface_failure outgoing data port
    * @param api_lower_alarm_temp outgoing data port
    * @param api_monitor_status outgoing data port
    * @param api_upper_alarm_temp outgoing data port
    */
  @strictpure def compute_CEP_T_Case (
      api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_monitor_mode: Isolette_Data_Model.Monitor_Mode.Type,
      api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_lower_alarm_temp: Isolette_Data_Model.Temp_i,
      api_monitor_status: Isolette_Data_Model.Status.Type,
      api_upper_alarm_temp: Isolette_Data_Model.Temp_i): B =
    compute_case_REQ_MMI_1(api_monitor_mode, api_monitor_status) &
    compute_case_REQ_MMI_2(api_monitor_mode, api_monitor_status) &
    compute_case_REQ_MMI_3(api_monitor_mode, api_monitor_status) &
    compute_case_REQ_MMI_4(api_lower_alarm_tempWstatus, api_upper_alarm_tempWstatus, api_interface_failure) &
    compute_case_REQ_MMI_5(api_lower_alarm_tempWstatus, api_upper_alarm_tempWstatus, api_interface_failure) &
    compute_case_REQ_MMI_6(api_lower_alarm_tempWstatus, api_upper_alarm_tempWstatus, api_interface_failure, api_lower_alarm_temp, api_upper_alarm_temp) &
    compute_case_REQ_MMI_7(api_interface_failure)

  /** CEP-Post: Compute Entrypoint Post-Condition for mmi
    *
    * @param In_lastCmd pre-state state variable
    * @param lastCmd post-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_lower_alarm_tempWstatus incoming data port
    * @param api_monitor_mode incoming data port
    * @param api_upper_alarm_tempWstatus incoming data port
    * @param api_interface_failure outgoing data port
    * @param api_lower_alarm_temp outgoing data port
    * @param api_monitor_status outgoing data port
    * @param api_upper_alarm_temp outgoing data port
    */
  @strictpure def compute_CEP_Post (
      In_lastCmd: Isolette_Data_Model.On_Off.Type,
      lastCmd: Isolette_Data_Model.On_Off.Type,
      api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_lower_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_monitor_mode: Isolette_Data_Model.Monitor_Mode.Type,
      api_upper_alarm_tempWstatus: Isolette_Data_Model.TempWstatus_i,
      api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
      api_lower_alarm_temp: Isolette_Data_Model.Temp_i,
      api_monitor_status: Isolette_Data_Model.Status.Type,
      api_upper_alarm_temp: Isolette_Data_Model.Temp_i): B =
    (// D-Inv-Guard: Datatype invariants for the types associated with mmi's state variables and outgoing ports
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_current_tempWstatus) & 
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_lower_alarm_tempWstatus) & 
     Isolette_Data_Model.TempWstatus_i.D_Inv_TempWstatus_i(api_upper_alarm_tempWstatus) & 
     Isolette_Data_Model.Temp_i.D_Inv_Temp_i(api_lower_alarm_temp) & 
     Isolette_Data_Model.Temp_i.D_Inv_Temp_i(api_upper_alarm_temp) & 

     // CEP-T-Case: case clauses of mmi's compute entrypoint
     compute_CEP_T_Case (api_lower_alarm_tempWstatus, api_monitor_mode, api_upper_alarm_tempWstatus, api_interface_failure, api_lower_alarm_temp, api_monitor_status, api_upper_alarm_temp))

  /** CEP-Post: Compute Entrypoint Post-Condition for mmi via containers
    *
    * @param pre Container holding the values of incoming ports and the pre-state values of state variables
    * @param post Container holding the values of outgoing ports and the post-state values of state variables
    */
  @strictpure def compute_CEP_Post_Container(
      pre: Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS,
      post: Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS): B =
    compute_CEP_Post(
      In_lastCmd = pre.In_lastCmd,
      lastCmd = post.lastCmd,
      api_current_tempWstatus = pre.api_current_tempWstatus,
      api_lower_alarm_tempWstatus = pre.api_lower_alarm_tempWstatus,
      api_monitor_mode = pre.api_monitor_mode,
      api_upper_alarm_tempWstatus = pre.api_upper_alarm_tempWstatus,
      api_interface_failure = post.api_interface_failure,
      api_lower_alarm_temp = post.api_lower_alarm_temp,
      api_monitor_status = post.api_monitor_status,
      api_upper_alarm_temp = post.api_upper_alarm_temp)
}
