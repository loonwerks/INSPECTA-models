// #Sireum

package isolette.Monitor

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Alarm_i_thermostat_mt_ma_ma {

  // BEGIN STATE VARS
  var lastCmd: Isolette_Data_Model.On_Off.Type = Isolette_Data_Model.On_Off.byOrdinal(0).get
  // END STATE VARS

  // BEGIN FUNCTIONS
  @strictpure def timeout_condition_satisfied(): Base_Types.Boolean = T
  // END FUNCTIONS

  def initialise(api: Manage_Alarm_i_Initialization_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee REQ_MA_1
        //   If the Monitor Mode is INIT, the Alarm Control shall be set
        //   to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        api.alarm_control == Isolette_Data_Model.On_Off.Off &
          lastCmd == Isolette_Data_Model.On_Off.Off
        // END INITIALIZES ENSURES
      )
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_alarm_control(Isolette_Data_Model.On_Off.byOrdinal(0).get)
  }

  def timeTriggered(api: Manage_Alarm_i_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume Figure_A_7
        //   This is not explicitly stated in the requirements, but a reasonable
        //   assumption is that the lower alarm must be at least 1.0f less than
        //   the upper alarm in order to account for the 0.5f tolerance
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        api.upper_alarm_temp.degrees - api.lower_alarm_temp.degrees >= 1.0f,
        // assume Table_A_12_LowerAlarmTemp
        //   Range [96..101]
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 
        96.0f <= api.lower_alarm_temp.degrees &&
          api.lower_alarm_temp.degrees <= 101.0f,
        // assume Table_A_12_UpperAlarmTemp
        //   Range [97..102]
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 
        97.0f <= api.upper_alarm_temp.degrees &&
          api.upper_alarm_temp.degrees <= 102.0f
        // END COMPUTE REQUIRES timeTriggered
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MA_1
        //   If the Monitor Mode is INIT, the Alarm Control shall be set
        //   to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) -->: (api.alarm_control == Isolette_Data_Model.On_Off.Off &
          lastCmd == Isolette_Data_Model.On_Off.Off),
        // case REQ_MA_2
        //   If the Monitor Mode is NORMAL and the Current Temperature is
        //   less than the Lower Alarm Temperature or greater than the Upper Alarm
        //   Temperature, the Alarm Control shall be set to On.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode &
           (api.current_tempWstatus.degrees < api.lower_alarm_temp.degrees ||
             api.current_tempWstatus.degrees > api.upper_alarm_temp.degrees)) -->: (api.alarm_control == Isolette_Data_Model.On_Off.Onn &
          lastCmd == Isolette_Data_Model.On_Off.Onn),
        // case REQ_MA_3
        //   If the Monitor Mode is NORMAL and the Current Temperature
        //   is greater than or equal to the Lower Alarm Temperature and less than
        //   the Lower Alarm Temperature +0.5 degrees, or the Current Temperature is
        //   greater than the Upper Alarm Temperature -0.5 degrees and less than or equal
        //   to the Upper Alarm Temperature, the value of the Alarm Control shall
        //   not be changed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode &
           (api.current_tempWstatus.degrees >= api.lower_alarm_temp.degrees &&
             api.current_tempWstatus.degrees < api.lower_alarm_temp.degrees + 0.5f ||
             api.current_tempWstatus.degrees > api.upper_alarm_temp.degrees - 0.5f &&
               api.current_tempWstatus.degrees <= api.upper_alarm_temp.degrees)) -->: (api.alarm_control == In(lastCmd) &
          lastCmd == In(lastCmd)),
        // case REQ_MA_4
        //   If the Monitor Mode is NORMAL and the value of the Current
        //   Temperature is greater than or equal to the Lower Alarm Temperature
        //   +0.5 degrees and less than or equal to the Upper Alarm Temperature
        //   -0.5 degrees, the Alarm Control shall be set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode &
           (api.current_tempWstatus.degrees >= api.lower_alarm_temp.degrees + 0.5f &
             api.current_tempWstatus.degrees <= api.upper_alarm_temp.degrees - 0.5f)) -->: (api.alarm_control == Isolette_Data_Model.On_Off.Off &
          lastCmd == Isolette_Data_Model.On_Off.Off),
        // case REQ_MA_5
        //   If the Monitor Mode is FAILED, the Alarm Control shall be
        //   set to On.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=116 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode) -->: (api.alarm_control == Isolette_Data_Model.On_Off.Onn &
          lastCmd == Isolette_Data_Model.On_Off.Onn)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_current_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_current_tempWstatus()
    api.logInfo(s"Received on data port current_tempWstatus: ${apiUsage_current_tempWstatus}")
    val apiUsage_lower_alarm_temp: Option[Isolette_Data_Model.Temp_i] = api.get_lower_alarm_temp()
    api.logInfo(s"Received on data port lower_alarm_temp: ${apiUsage_lower_alarm_temp}")
    val apiUsage_upper_alarm_temp: Option[Isolette_Data_Model.Temp_i] = api.get_upper_alarm_temp()
    api.logInfo(s"Received on data port upper_alarm_temp: ${apiUsage_upper_alarm_temp}")
    val apiUsage_monitor_mode: Option[Isolette_Data_Model.Monitor_Mode.Type] = api.get_monitor_mode()
    api.logInfo(s"Received on data port monitor_mode: ${apiUsage_monitor_mode}")
  }

  def finalise(api: Manage_Alarm_i_Operational_Api): Unit = { }
}
