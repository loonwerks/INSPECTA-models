// #Sireum

package isolette.Regulate

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Regulator_Interface_i_thermostat_rt_mri_mri {

  // BEGIN FUNCTIONS
  @strictpure def ROUND(num: Base_Types.Float_32): Base_Types.Float_32 = num
  // END FUNCTIONS

  def initialise(api: Manage_Regulator_Interface_i_Initialization_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee RegulatorStatusIsInitiallyInit
        api.regulator_status == Isolette_Data_Model.Status.Init_Status
        // END INITIALIZES ENSURES
      )
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_upper_desired_temp(Isolette_Data_Model.Temp_i.example())
    api.put_lower_desired_temp(Isolette_Data_Model.Temp_i.example())
    api.put_displayed_temp(Isolette_Data_Model.Temp_i.example())
    api.put_regulator_status(Isolette_Data_Model.Status.byOrdinal(0).get)
    api.put_interface_failure(Isolette_Data_Model.Failure_Flag_i.example())
  }

  def timeTriggered(api: Manage_Regulator_Interface_i_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume lower_is_not_higher_than_upper
        api.lower_desired_tempWstatus.degrees <= api.upper_desired_tempWstatus.degrees
        // END COMPUTE REQUIRES timeTriggered
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MRI_1
        //   If the Regulator Mode is INIT,
        //   the Regulator Status shall be set to Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) -->: (api.regulator_status == Isolette_Data_Model.Status.Init_Status),
        // case REQ_MRI_2
        //   If the Regulator Mode is NORMAL,
        //   the Regulator Status shall be set to On
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) -->: (api.regulator_status == Isolette_Data_Model.Status.On_Status),
        // case REQ_MRI_3
        //   If the Regulator Mode is FAILED,
        //   the Regulator Status shall be set to Failed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode) -->: (api.regulator_status == Isolette_Data_Model.Status.Failed_Status),
        // case REQ_MRI_4
        //   If the Regulator Mode is NORMAL, the
        //   Display Temperature shall be set to the value of the
        //   Current Temperature rounded to the nearest integer.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) -->: (api.displayed_temp.degrees == Manage_Regulator_Interface_i_thermostat_rt_mri_mri.ROUND(api.current_tempWstatus.degrees)),
        // case REQ_MRI_5
        //   If the Regulator Mode is not NORMAL,
        //   the value of the Display Temperature is UNSPECIFIED.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) -->: (T),
        // case REQ_MRI_6
        //   If the Status attribute of the Lower Desired Temperature
        //   or the Upper Desired Temperature is Invalid,
        //   the Regulator Interface Failure shall be set to True.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (api.upper_desired_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid |
           api.upper_desired_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid) -->: (api.interface_failure.flag),
        // case REQ_MRI_7
        //   If the Status attribute of the Lower Desired Temperature
        //   and the Upper Desired Temperature is Valid,
        //   the Regulator Interface Failure shall be set to False.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) -->: (api.interface_failure.flag == !(api.upper_desired_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid &
           api.lower_desired_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid)),
        // case REQ_MRI_8
        //   If the Regulator Interface Failure is False
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) -->: (!(api.interface_failure.flag) ->: (api.lower_desired_temp.degrees == api.lower_desired_tempWstatus.degrees &
           api.upper_desired_temp.degrees == api.upper_desired_tempWstatus.degrees)),
        // case REQ_MRI_9
        //   If the Regulator Interface Failure is True,
        //   the Desired Range is UNSPECIFIED.
        //   the Desired Range shall be set to the Desired Temperature Range.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) -->: (T)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_upper_desired_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_upper_desired_tempWstatus()
    api.logInfo(s"Received on data port upper_desired_tempWstatus: ${apiUsage_upper_desired_tempWstatus}")
    val apiUsage_lower_desired_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_lower_desired_tempWstatus()
    api.logInfo(s"Received on data port lower_desired_tempWstatus: ${apiUsage_lower_desired_tempWstatus}")
    val apiUsage_current_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_current_tempWstatus()
    api.logInfo(s"Received on data port current_tempWstatus: ${apiUsage_current_tempWstatus}")
    val apiUsage_regulator_mode: Option[Isolette_Data_Model.Regulator_Mode.Type] = api.get_regulator_mode()
    api.logInfo(s"Received on data port regulator_mode: ${apiUsage_regulator_mode}")
  }

  def finalise(api: Manage_Regulator_Interface_i_Operational_Api): Unit = { }
}
