// #Sireum

package isolette.Regulate

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm {

  // BEGIN STATE VARS
  var lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type = Isolette_Data_Model.Regulator_Mode.byOrdinal(0).get
  // END STATE VARS

  def initialise(api: Manage_Regulator_Mode_i_Initialization_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee REQ_MRM_1
        //   The initial mode of the regular is INIT
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode
        // END INITIALIZES ENSURES
      )
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_regulator_mode(Isolette_Data_Model.Regulator_Mode.byOrdinal(0).get)
  }

  def timeTriggered(api: Manage_Regulator_Mode_i_Operational_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MRM_2
        //   'transition from Init to Normal'
        //   If the current regulator mode is Init, then
        //   the regulator mode is set to NORMAL iff the regulator status is valid (see Table A-10), i.e.,
        //     if NOT (Regulator Interface Failure OR Regulator Internal Failure)
        //        AND Current Temperature.Status = Valid
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) -->: ((!(api.interface_failure.flag || api.internal_failure.flag) &&
           api.current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid) -->:
          (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode)),
        // case REQ_MRM_Maintain_Normal
        //   'maintaining NORMAL, NORMAL to NORMAL'
        //   If the current regulator mode is Normal, then
        //   the regulator mode is stays normal iff
        //   the regulaor status is not false i.e.,
        //          if NOT(
        //              (Regulator Interface Failure OR Regulator Internal Failure)
        //              OR NOT(Current Temperature.Status = Valid)
        //          )
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) -->: ((!(api.interface_failure.flag || api.internal_failure.flag) &&
           api.current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid) -->:
          (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode)),
        // case REQ_MRM_3
        //   'transition for NORMAL to FAILED'
        //   If the current regulator mode is Normal, then
        //   the regulator mode is set to Failed iff
        //   the regulator status is false, i.e.,
        //      if  (Regulator Interface Failure OR Regulator Internal Failure)
        //          OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) -->: (((api.interface_failure.flag || api.internal_failure.flag) &&
           api.current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid) -->:
          (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)),
        // case REQ_MRM_4
        //   'transition from INIT to FAILED' 
        //   If the current regulator mode is Init, then
        //   the regulator mode and lastRegulatorMode state value is set to Failed iff
        //   the regulator status is false, i.e.,
        //          if  (Regulator Interface Failure OR Regulator Internal Failure)
        //          OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) -->: (((api.interface_failure.flag || api.internal_failure.flag) &&
           api.current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid) -->:
          (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)),
        // case REQ_MRM_MaintainFailed
        //   'maintaining FAIL, FAIL to FAIL'
        //   If the current regulator mode is Failed, then
        //   the regulator mode remains in the Failed state and the LastRegulator mode remains Failed.REQ-MRM-Maintain-Failed
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode) -->: (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
          lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_current_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_current_tempWstatus()
    api.logInfo(s"Received on data port current_tempWstatus: ${apiUsage_current_tempWstatus}")
    val apiUsage_interface_failure: Option[Isolette_Data_Model.Failure_Flag_i] = api.get_interface_failure()
    api.logInfo(s"Received on data port interface_failure: ${apiUsage_interface_failure}")
    val apiUsage_internal_failure: Option[Isolette_Data_Model.Failure_Flag_i] = api.get_internal_failure()
    api.logInfo(s"Received on data port internal_failure: ${apiUsage_internal_failure}")
  }

  def finalise(api: Manage_Regulator_Mode_i_Operational_Api): Unit = { }
}
