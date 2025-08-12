package GUMBO_Contracts {
  private import Isolette_Data_Model::*;
  private import Base_Types::*;

  package Manage_Regulator_Interface_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    calc def ROUND {
      in attribute num: Base_Types::Integer_32;
      num
    }
  
    requirement def InitializeContract {
      doc /*ID RegulatorStatusIsInitiallyInit*/
      require constraint { (regulator_status == Isolette_Data_Model::Status::Init_Status) }
  
    }
  
    doc /*======  C o m p u t e     C o n s t r a i n t s =====*/
    requirement def ComputeBase {
      doc /*ID lower_is_not_higher_than_upper*/
      assume constraint { (lower_desired_tempWstatus.degrees <= upper_desired_tempWstatus.degrees) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_1 specializes ComputeBase {
      doc /*If the Regulator Mode is INIT,
                                      |the Regulator Status shall be set to Init.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 */
      assume constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Init_Regulator_Mode) }
  
      require constraint { (regulator_status == Isolette_Data_Model::Status::Init_Status) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_2 specializes ComputeBase {
      doc /*If the Regulator Mode is NORMAL,
                                      |the Regulator Status shall be set to On
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 */
      assume constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode) }
  
      require constraint { (regulator_status == Isolette_Data_Model::Status::On_Status) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_3 specializes ComputeBase {
      doc /*If the Regulator Mode is FAILED,
                                      |the Regulator Status shall be set to Failed.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 */
      assume constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode) }
  
      require constraint { (regulator_status == Isolette_Data_Model::Status::Failed_Status) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_4 specializes ComputeBase {
      doc /*If the Regulator Mode is NORMAL, the
                                      |Display Temperature shall be set to the value of the
                                      |Current Temperature rounded to the nearest integer.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 */
      assume constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode) }
  
      require constraint { (displayed_temp.degrees == current_tempWstatus.degrees) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_5 specializes ComputeBase {
      doc /*If the Regulator Mode is not NORMAL,
                                      |the value of the Display Temperature is UNSPECIFIED.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 */
      assume constraint { true }
  
      require constraint { true }
  
    }
  
    requirement def ComputeCase_REQ_MRI_6 specializes ComputeBase {
      doc /*If the Status attribute of the Lower Desired Temperature
                                      |or the Upper Desired Temperature is Invalid,
                                      |the Regulator Interface Failure shall be set to True.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 */
      assume constraint { ((((upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)) or
        ((upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)))) }
  
      require constraint { interface_failure.flag }
  
    }
  
    requirement def ComputeCase_REQ_MRI_7 specializes ComputeBase {
      doc /*If the Status attribute of the Lower Desired Temperature
                                      |and the Upper Desired Temperature is Valid,
                                      |the Regulator Interface Failure shall be set to False.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 */
      assume constraint { true }
  
      require constraint { ((interface_failure.flag == ((not ((((upper_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid)) and
        ((lower_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid)))))))) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_8 specializes ComputeBase {
      doc /*If the Regulator Interface Failure is False,
                                     |the Desired Range shall be set to the Desired Temperature Range.
                                     |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 */
      assume constraint { true }
  
      require constraint { ((not interface_failure.flag) implies (((lower_desired_temp.degrees == lower_desired_tempWstatus.degrees)) and
        ((upper_desired_temp.degrees == upper_desired_tempWstatus.degrees)))) }
  
    }
  
    requirement def ComputeCase_REQ_MRI_9 specializes ComputeBase {
      doc /*If the Regulator Interface Failure is True,
                                      |the Desired Range is UNSPECIFIED.
                                      |the Desired Range shall be set to the Desired Temperature Range.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 */
      assume constraint { true }
  
      require constraint { true }
  
    }
  
  }

  package Manage_Regulator_Mode_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    attribute lastRegulatorMode: Isolette_Data_Model::Regulator_Mode;
  
    requirement def InitializeContract {
      doc /*ID REQ_MRM_1 The initial mode of the regular is INIT http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109*/
      require constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Init_Regulator_Mode) }
  
    }
  
    requirement def ComputeCase_REQ_MRM_2 {
      doc /*'transition from Init to Normal'
                                      |If the current regulator mode is Init, then
                                      |the regulator mode is set to NORMAL iff the regulator status is valid (see Table A-10), i.e.,
                                      |  if NOT (Regulator Interface Failure OR Regulator Internal Failure)
                                      |     AND Current Temperature.Status = Valid
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 */
      assume constraint { (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Init_Regulator_Mode) }
  
      require constraint { (((((not ((interface_failure.flag or
        internal_failure.flag)))) and
        (current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid))) implies (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode) and
        (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode)))) }
  
    }
  
    requirement def ComputeCase_REQ_MRM_Maintain_Normal {
      doc /*'maintaining NORMAL, NORMAL to NORMAL'
                                                  |If the current regulator mode is Normal, then
                                                  |the regulator mode is stays normal iff
                                                  |the regulaor status is not false i.e.,
                                                  |       if NOT(
                                                  |           (Regulator Interface Failure OR Regulator Internal Failure)
                                                  |           OR NOT(Current Temperature.Status = Valid)
                                                  |       )
                                                  |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 */
      assume constraint { (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode) }
  
      require constraint { (((((not ((interface_failure.flag or
        internal_failure.flag)))) and
        (current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid))) implies (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode) and
        (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode)))) }
  
    }
  
    requirement def ComputeCase_REQ_MRM_3 {
      doc /*'transition for NORMAL to FAILED'
                                      |If the current regulator mode is Normal, then
                                      |the regulator mode is set to Failed iff
                                      |the regulator status is false, i.e.,
                                      |   if  (Regulator Interface Failure OR Regulator Internal Failure)
                                      |       OR NOT(Current Temperature.Status = Valid)
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 */
      assume constraint { (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode) }
  
      require constraint { (((((interface_failure.flag or
        internal_failure.flag)) and
        (current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid))) implies (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode) and
        (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode)))) }
  
    }
  
    requirement def ComputeCase_REQ_MRM_4 {
      doc /*'transition from INIT to FAILED'
                                      |If the current regulator mode is Init, then
                                      |the regulator mode and lastRegulatorMode state value is set to Failed iff
                                      |the regulator status is false, i.e.,
                                      |       if  (Regulator Interface Failure OR Regulator Internal Failure)
                                      |       OR NOT(Current Temperature.Status = Valid)
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109*/
      assume constraint { (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Init_Regulator_Mode) }
  
      require constraint { (((((interface_failure.flag or
        internal_failure.flag)) and
        (current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid))) implies (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode) and
        (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode)))) }
  
    }
  
    requirement def ComputeCase_REQ_MRM_MaintainFailed {
      doc /*'maintaining FAIL, FAIL to FAIL'
                                                  |If the current regulator mode is Failed, then
                                                  |the regulator mode remains in the Failed state and the LastRegulator mode remains Failed.REQ-MRM-Maintain-Failed
                                                  |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109*/
      assume constraint { (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode) }
  
      require constraint { (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode) and
        (lastRegulatorMode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode))) }
  
    }
  
  }

  package Manage_Heat_Source_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    attribute lastCmd: Isolette_Data_Model::On_Off;
  
    requirement def InitializeContract {
      doc /*ID initlastCmd*/
      require constraint { (lastCmd == Isolette_Data_Model::On_Off.Off) }
  
      doc /*ID REQ_MHS_1 If the Regulator Mode is INIT, the Heat Control shall be set to Off. http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110*/
      require constraint { (heat_control == Isolette_Data_Model::On_Off.Off) }
  
    }
  
    doc /*======  C o m p u t e     C o n s t r a i n t s =====*/
    requirement def ComputeBase {
      doc /*ID lower_is_lower_temp*/
      assume constraint { (lower_desired_temp.degrees <= upper_desired_temp.degrees) }
  
      doc /*ID lastCmd Set lastCmd to value of output Cmd port*/
      require constraint { (lastCmd == heat_control) }
  
    }
  
    requirement def ComputeCase_REQ_MHS_1 specializes ComputeBase {
      doc /*If the Regulator Mode is INIT, the Heat Control shall be
                                      |set to Off.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 */
      assume constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Init_Regulator_Mode) }
  
      require constraint { (heat_control == Isolette_Data_Model::On_Off.Off) }
  
    }
  
    requirement def ComputeCase_REQ_MHS_2 specializes ComputeBase {
      doc /*If the Regulator Mode is NORMAL and the Current Temperature is less than
                                      |the Lower Desired Temperature, the Heat Control shall be set to On.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 */
      assume constraint { (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode)) and
        ((current_tempWstatus.degrees < lower_desired_temp.degrees))) }
  
      require constraint { (heat_control == Isolette_Data_Model::On_Off.Onn) }
  
    }
  
    requirement def ComputeCase_REQ_MHS_3 specializes ComputeBase {
      doc /*If the Regulator Mode is NORMAL and the Current Temperature is greater than
                                      |the Upper Desired Temperature, the Heat Control shall be set to Off.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 */
      assume constraint { (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode)) and
        ((current_tempWstatus.degrees > upper_desired_temp.degrees))) }
  
      require constraint { (heat_control == Isolette_Data_Model::On_Off.Off) }
  
    }
  
    requirement def ComputeCase_REQ_MHS_4 specializes ComputeBase {
      doc /*If the Regulator Mode is NORMAL and the Current
                                  |Temperature is greater than or equal to the Lower Desired Temperature
                                  |and less than or equal to the Upper Desired Temperature, the value of
                                  |the Heat Control shall not be changed.
                                  |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 */
      assume constraint { (((regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode)) and
        (((current_tempWstatus.degrees >= lower_desired_temp.degrees) and
        (current_tempWstatus.degrees <= upper_desired_temp.degrees)))) }
  
      require constraint { (heat_control == In(lastCmd)) }
  
    }
  
    requirement def ComputeCase_REQ_MHS_5 specializes ComputeBase {
      doc /*If the Regulator Mode is FAILED, the Heat Control shall be
                                      |set to Off.
                                      |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=111 */
      assume constraint { (regulator_mode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode) }
  
      require constraint { (heat_control == Isolette_Data_Model::On_Off.Off) }
  
    }
  
  }

}