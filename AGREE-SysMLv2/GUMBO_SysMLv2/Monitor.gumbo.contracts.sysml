package GUMBO_Contracts {
  private import Isolette_Data_Model::*;
  private import Base_Types::*;

  package Manage_Monitor_Interface_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    attribute lastCmd: Isolette_Data_Model::On_Off;
  
    calc def timeout_condition_satisfied {
      T
    }
  
    requirement def InitializeContract {
      doc /*ID monitorStatusInitiallyInit*/
      require constraint { (monitor_status == Isolette_Data_Model::Status::Init_Status) }
  
    }
  
    requirement def IntegrationContract {
      doc /*ID Table_A_12_LowerAlarmTemp Range [96..101] http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
      // typed literals: 96 [s32], 101 [s32]
      assume constraint { ((96 <= lower_alarm_tempWstatus.degrees) and
        (lower_alarm_tempWstatus.degrees <= 101)) }
  
      doc /*ID Table_A_12_UpperAlarmTemp Range [97..102] http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
      // typed literals: 97 [s32], 102 [s32]
      assume constraint { ((97 <= upper_alarm_tempWstatus.degrees) and
        (upper_alarm_tempWstatus.degrees <= 102)) }
  
    }
  
    requirement def ComputeCase_REQ_MMI_1 {
      doc /*If the Manage Monitor Interface mode is INIT,
  									|the Monitor Status shall be set to Init.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
  
      require constraint { (monitor_status == Isolette_Data_Model::Status::Init_Status) }
  
    }
  
    requirement def ComputeCase_REQ_MMI_2 {
      doc /*If the Manage Monitor Interface mode is NORMAL,
  									|the Monitor Status shall be set to On
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) }
  
      require constraint { (monitor_status == Isolette_Data_Model::Status::On_Status) }
  
    }
  
    requirement def ComputeCase_REQ_MMI_3 {
      doc /*If the Manage Monitor Interface mode is FAILED,
  									|the Monitor Status shall be set to Failed.
  									|Latency: < Max Operator Response Time
  									|Tolerance: N/A
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode) }
  
      require constraint { (monitor_status == Isolette_Data_Model::Status::Failed_Status) }
  
    }
  
    requirement def ComputeCase_REQ_MMI_4 {
      doc /*If the Status attribute of the Lower Alarm Temperature
  									|or the Upper Alarm Temperature is Invalid,
  									|the Monitor Interface Failure shall be set to True
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { ((lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Invalid) or
        (upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Invalid)) }
  
      require constraint { interface_failure.flag }
  
    }
  
    requirement def ComputeCase_REQ_MMI_5 {
      doc /*If the Status attribute of the Lower Alarm Temperature
  										|and the Upper Alarm Temperature is Valid,
  										|the Monitor Interface Failure shall be set to False
  										|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { ((lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid) and
        (upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid)) }
  
      require constraint { (not interface_failure.flag) }
  
    }
  
    requirement def ComputeCase_REQ_MMI_6 {
      doc /*If the Monitor Interface Failure is False,
  									|the Alarm Range variable shall be set to the Desired Temperature Range
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { true }
  
      require constraint { (((not interface_failure.flag)) implies (((lower_alarm_temp.degrees == lower_alarm_tempWstatus.degrees) and
        (upper_alarm_temp.degrees == upper_alarm_tempWstatus.degrees)))) }
  
    }
  
    requirement def ComputeCase_REQ_MMI_7 {
      doc /*If the Monitor Interface Failure is True,
  									|the Alarm Range variable is UNSPECIFIED
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
      assume constraint { true }
  
      require constraint { (interface_failure.flag implies true) }
  
    }
  
  }

  package Manage_Monitor_Mode_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    attribute lastMonitorMode: Isolette_Data_Model::Monitor_Mode;
  
    calc def timeout_condition_satisfied {
      F
    }
  
    requirement def InitializeContract {
      doc /*ID REQ_MMM_1 Upon the first dispatch of the thread, the monitor mode is Init. http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114*/
      require constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
  
    }
  
    requirement def ComputeCase_REQ_MMM_2 {
      doc /*If the current mode is Init, then
  									|the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
  									|if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
  									|AND Current Temperature.Status = Valid
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 */
      assume constraint { (lastMonitorMode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
  
      require constraint { (((((not ((interface_failure.flag or
        internal_failure.flag)))) and
        (current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid))) implies ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode))) }
  
    }
  
    requirement def ComputeCase_REQ_MMM_3 {
      doc /*If the current Monitor mode is Normal, then
  									|the Monitor mode is set to Failed iff
  									|the Monitor status is false, i.e.,
  									|if  (Monitor Interface Failure OR Monitor Internal Failure)
  									|OR NOT(Current Temperature.Status = Valid)
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 */
      assume constraint { (lastMonitorMode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) }
  
      require constraint { (((((interface_failure.flag or
        internal_failure.flag)) or
        (current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid))) implies ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode))) }
  
    }
  
    requirement def ComputeCase_REQ_MMM_4 {
      doc /*If the current mode is Init, then
  									|the mode is set to Failed iff the time during
  									|which the thread has been in Init mode exceeds the
  									|Monitor Init Timeout value.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 */
      assume constraint { (lastMonitorMode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
  
      require constraint { (false == ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode))) }
  
    }
  
  }

  package Manage_Alarm_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    attribute lastCmd: Isolette_Data_Model::On_Off;
  
    calc def timeout_condition_satisfied {
      T
    }
  
    requirement def InitializeContract {
      doc /*ID REQ_MA_1 If the Monitor Mode is INIT, the Alarm Control shall be set to Off. http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115*/
      require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Off) and
        (lastCmd == Isolette_Data_Model::On_Off.Off)) }
  
    }
  
    doc /*======  C o m p u t e     C o n s t r a i n t s =====*/
    requirement def ComputeBase {
      doc /*ID Figure_A_7 This is not explicitly stated in the requirements, but a reasonable assumption is that the lower alarm must be at least 1.0f less than the upper alarm in order to account for the 0.5f tolerance http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115*/
      // typed literals: 1 [s32]
      assume constraint { ((upper_alarm_temp.degrees - lower_alarm_temp.degrees) >= 1) }
  
      doc /*ID Table_A_12_LowerAlarmTemp Range [96..101] http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
      // typed literals: 96 [s32], 101 [s32]
      assume constraint { ((96 <= lower_alarm_temp.degrees) and
        (lower_alarm_temp.degrees <= 101)) }
  
      doc /*ID Table_A_12_UpperAlarmTemp Range [97..102] http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
      // typed literals: 97 [s32], 102 [s32]
      assume constraint { ((97 <= upper_alarm_temp.degrees) and
        (upper_alarm_temp.degrees <= 102)) }
  
    }
  
    requirement def ComputeCase_REQ_MA_1 specializes ComputeBase {
      doc /*If the Monitor Mode is INIT, the Alarm Control shall be set
  									|to Off.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
      assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
  
      require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Off) and
        (lastCmd == Isolette_Data_Model::On_Off.Off)) }
  
    }
  
    requirement def ComputeCase_REQ_MA_2 specializes ComputeBase {
      doc /*If the Monitor Mode is NORMAL and the Current Temperature is
  									|less than the Lower Alarm Temperature or greater than the Upper Alarm
  									|Temperature, the Alarm Control shall be set to On.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
      assume constraint { ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) and
        (((current_tempWstatus.degrees < lower_alarm_temp.degrees) or
        (current_tempWstatus.degrees > upper_alarm_temp.degrees)))) }
  
      require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Onn) and
        (lastCmd == Isolette_Data_Model::On_Off.Onn)) }
  
    }
  
    requirement def ComputeCase_REQ_MA_3 specializes ComputeBase {
      doc /*If the Monitor Mode is NORMAL and the Current Temperature
  									|is greater than or equal to the Lower Alarm Temperature and less than
  									|the Lower Alarm Temperature +0.5 degrees, or the Current Temperature is
  									|greater than the Upper Alarm Temperature -0.5 degrees and less than or equal
  									|to the Upper Alarm Temperature, the value of the Alarm Control shall
  									|not be changed.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
      // typed literals: 1 [s32], 1 [s32]
      assume constraint { ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) and
        (((((current_tempWstatus.degrees >= lower_alarm_temp.degrees) and
        (current_tempWstatus.degrees < (lower_alarm_temp.degrees + 1)))) or
        ((((current_tempWstatus.degrees > (upper_alarm_temp.degrees - 1))) and
        (current_tempWstatus.degrees <= upper_alarm_temp.degrees)))))) }
  
      require constraint { ((alarm_control == In(lastCmd)) and
        (lastCmd == In(lastCmd))) }
  
    }
  
    requirement def ComputeCase_REQ_MA_4 specializes ComputeBase {
      doc /*If the Monitor Mode is NORMAL and the value of the Current
  									|Temperature is greater than or equal to the Lower Alarm Temperature
  									|+0.5 degrees and less than or equal to the Upper Alarm Temperature
  									|-0.5 degrees, the Alarm Control shall be set to Off.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
      // typed literals: 1 [s32], 1 [s32]
      assume constraint { (((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) and
        (current_tempWstatus.degrees >= (lower_alarm_temp.degrees + 1))) and
        (current_tempWstatus.degrees <= (upper_alarm_temp.degrees - 1))) }
  
      require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Off) and
        (lastCmd == Isolette_Data_Model::On_Off.Off)) }
  
    }
  
    requirement def ComputeCase_REQ_MA_5 specializes ComputeBase {
      doc /*If the Monitor Mode is FAILED, the Alarm Control shall be
  									|set to On.
  									|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=116 */
      assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode) }
  
      require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Onn) and
        (lastCmd == Isolette_Data_Model::On_Off.Onn)) }
  
    }
  
  }

}