// #Sireum

package isolette.Regulate

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Heat_Source_i_thermostat_rt_mhs_mhs {

  // BEGIN STATE VARS
  var lastCmd: Isolette_Data_Model.On_Off.Type = Isolette_Data_Model.On_Off.byOrdinal(0).get
  // END STATE VARS

  def initialise(api: Manage_Heat_Source_i_Initialization_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee initlastCmd
        lastCmd == Isolette_Data_Model.On_Off.Off,
        // guarantee REQ_MHS_1
        //   If the Regulator Mode is INIT, the Heat Control shall be
        //   set to Off
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        api.heat_control == Isolette_Data_Model.On_Off.Off
        // END INITIALIZES ENSURES
      )
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_heat_control(Isolette_Data_Model.On_Off.byOrdinal(0).get)
  }

  def timeTriggered(api: Manage_Heat_Source_i_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume lower_is_lower_temp
        api.lower_desired_temp.degrees <= api.upper_desired_temp.degrees
        // END COMPUTE REQUIRES timeTriggered
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee lastCmd
        //   Set lastCmd to value of output Cmd port
        lastCmd == api.heat_control,
        // case REQ_MHS_1
        //   If the Regulator Mode is INIT, the Heat Control shall be
        //   set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) -->: (api.heat_control == Isolette_Data_Model.On_Off.Off),
        // case REQ_MHS_2
        //   If the Regulator Mode is NORMAL and the Current Temperature is less than
        //   the Lower Desired Temperature, the Heat Control shall be set to On.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &
           api.current_tempWstatus.degrees < api.lower_desired_temp.degrees) -->: (api.heat_control == Isolette_Data_Model.On_Off.Onn),
        // case REQ_MHS_3
        //   If the Regulator Mode is NORMAL and the Current Temperature is greater than
        //   the Upper Desired Temperature, the Heat Control shall be set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &
           api.current_tempWstatus.degrees > api.upper_desired_temp.degrees) -->: (api.heat_control == Isolette_Data_Model.On_Off.Off),
        // case REQ_MHS_4
        //   If the Regulator Mode is NORMAL and the Current
        //   Temperature is greater than or equal to the Lower Desired Temperature
        //   and less than or equal to the Upper Desired Temperature, the value of
        //   the Heat Control shall not be changed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode &
           (api.current_tempWstatus.degrees >= api.lower_desired_temp.degrees &
             api.current_tempWstatus.degrees <= api.upper_desired_temp.degrees)) -->: (api.heat_control == In(lastCmd)),
        // case REQ_MHS_5
        //   If the Regulator Mode is FAILED, the Heat Control shall be
        //   set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=111 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode) -->: (api.heat_control == Isolette_Data_Model.On_Off.Off)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_current_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_current_tempWstatus()
    api.logInfo(s"Received on data port current_tempWstatus: ${apiUsage_current_tempWstatus}")
    val apiUsage_lower_desired_temp: Option[Isolette_Data_Model.Temp_i] = api.get_lower_desired_temp()
    api.logInfo(s"Received on data port lower_desired_temp: ${apiUsage_lower_desired_temp}")
    val apiUsage_upper_desired_temp: Option[Isolette_Data_Model.Temp_i] = api.get_upper_desired_temp()
    api.logInfo(s"Received on data port upper_desired_temp: ${apiUsage_upper_desired_temp}")
    val apiUsage_regulator_mode: Option[Isolette_Data_Model.Regulator_Mode.Type] = api.get_regulator_mode()
    api.logInfo(s"Received on data port regulator_mode: ${apiUsage_regulator_mode}")
  }

  def finalise(api: Manage_Heat_Source_i_Operational_Api): Unit = { }
}
