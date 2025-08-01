// #Sireum #Logika

package isolette.Regulate

import org.sireum._
import isolette._
import org.sireum.S32._

// This file will not be overwritten so is safe to edit
object Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm {

  // BEGIN STATE VARS
  var lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type = Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode
  // END STATE VARS

  def initialise(api: Manage_Regulator_Mode_i_Initialization_Api): Unit = {
    Contract(
      Modifies(
        api,
        lastRegulatorMode
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee REQ_MRM_1
        //   The initial mode of the regular is INIT
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode
        // END INITIALIZES ENSURES
      )
    )

    Manage_Regulator_Mode__InjectionProvider.init()

    lastRegulatorMode = Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode
    api.put_regulator_mode(lastRegulatorMode)
  }

  def timeTriggered(api: Manage_Regulator_Mode_i_Operational_Api): Unit = {
    Contract(
      Modifies(lastRegulatorMode, api),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MRM_2
        //   'transition from Init to Normal'
        //   If the current regulator mode is Init, then
        //   the regulator mode is set to NORMAL iff the regulator status is valid (see Table A-10), i.e.,
        //     if NOT (Regulator Interface Failure OR Regulator Internal Failure)
        //        AND Current Temperature.Status = Valid
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) ___>: (!(api.interface_failure.flag || api.internal_failure.flag) &&
          api.current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid ___>:
          api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode),
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
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) ___>: (!(api.interface_failure.flag || api.internal_failure.flag) &&
          api.current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid ___>:
          api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode),
        // case REQ_MRM_3
        //   'transition for NORMAL to FAILED'
        //   If the current regulator mode is Normal, then
        //   the regulator mode is set to Failed iff
        //   the regulator status is false, i.e.,
        //      if  (Regulator Interface Failure OR Regulator Internal Failure)
        //          OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) ___>: ((api.interface_failure.flag || api.internal_failure.flag) &&
          api.current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid ___>:
          api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode),
        // case REQ_MRM_4
        //   'transition from INIT to FAILED' 
        //   If the current regulator mode is Init, then
        //   the regulator mode and lastRegulatorMode state value is set to Failed iff
        //   the regulator status is false, i.e.,
        //          if  (Regulator Interface Failure OR Regulator Internal Failure)
        //          OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) ___>: ((api.interface_failure.flag || api.internal_failure.flag) &&
          api.current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid ___>:
          api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
            lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode),
        // case REQ_MRM_MaintainFailed
        //   'maintaining FAIL, FAIL to FAIL'
        //   If the current regulator mode is Failed, then
        //   the regulator mode remains in the Failed state and the LastRegulator mode remains Failed.REQ-MRM-Maintain-Failed
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
        (In(lastRegulatorMode) == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode) ___>: (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode &&
          lastRegulatorMode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode)
        // END COMPUTE ENSURES timeTriggered
      )
    )

    // -------------- Get values of input ports ------------------

    val current_tempWstatus: Isolette_Data_Model.TempWstatus_i =
      api.get_current_tempWstatus().get

    val current_temperature_status: Isolette_Data_Model.ValueStatus.Type = current_tempWstatus.status

    val interface_failure: Isolette_Data_Model.Failure_Flag_i =
      api.get_interface_failure().get

    val internal_failure: Isolette_Data_Model.Failure_Flag_i =
      api.get_internal_failure().get


    // determine regulator status as specified in FAA REMH Table A-10
    //  regulator_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
    //                          AND Current Temperature.Status = Valid
    val regulator_status: B =
      (!(interface_failure.flag || internal_failure.flag)
        && (current_temperature_status == Isolette_Data_Model.ValueStatus.Valid))

    lastRegulatorMode match {

      // Transitions from INIT Mode
      case Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode =>
        if (regulator_status) {
          // REQ-MRM-2
          lastRegulatorMode = Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode
        } else {
          // REQ-MRM-3
          lastRegulatorMode = Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode
        }

      // otherwise we stay in Init mode

      // Transitions from NORMAL Mode
      case Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode =>
        if (!regulator_status) {
          // REQ-MRM-4
          lastRegulatorMode = Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode
        }

      // Transitions from FAILED Mode (do nothing -- system must be rebooted)
      case Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode =>
      // do nothing
    }

    api.put_regulator_mode(lastRegulatorMode)

    //api.logInfo(s"Sent on regulator_mode: $lastRegulatorMode")
  }

  def finalise(api: Manage_Regulator_Mode_i_Operational_Api): Unit = { }
}

@ext object Manage_Regulator_Mode__InjectionProvider {
  def init(): Unit = $
}