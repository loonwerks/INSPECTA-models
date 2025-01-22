// #Sireum

package isolette.Monitor

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi {

  // BEGIN STATE VARS
  var lastCmd: Isolette_Data_Model.On_Off.Type = Isolette_Data_Model.On_Off.byOrdinal(0).get
  // END STATE VARS

  // BEGIN FUNCTIONS
  @strictpure def timeout_condition_satisfied(): Base_Types.Boolean = T
  // END FUNCTIONS

  def initialise(api: Manage_Monitor_Interface_i_Initialization_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee monitorStatusInitiallyInit
        api.monitor_status == Isolette_Data_Model.Status.Init_Status
        // END INITIALIZES ENSURES
      )
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_upper_alarm_temp(Isolette_Data_Model.Temp_i.example())
    api.put_lower_alarm_temp(Isolette_Data_Model.Temp_i.example())
    api.put_monitor_status(Isolette_Data_Model.Status.byOrdinal(0).get)
    api.put_interface_failure(Isolette_Data_Model.Failure_Flag_i.example())
  }

  def timeTriggered(api: Manage_Monitor_Interface_i_Operational_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MMI_1
        //   If the Manage Monitor Interface mode is INIT,
        //   the Monitor Status shall be set to Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) -->: (api.monitor_status == Isolette_Data_Model.Status.Init_Status),
        // case REQ_MMI_2
        //   If the Manage Monitor Interface mode is NORMAL,
        //   the Monitor Status shall be set to On
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode) -->: (api.monitor_status == Isolette_Data_Model.Status.On_Status),
        // case REQ_MMI_3
        //   If the Manage Monitor Interface mode is FAILED,
        //   the Monitor Status shall be set to Failed.
        //   Latency: < Max Operator Response Time
        //   Tolerance: N/A
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode) -->: (api.monitor_status == Isolette_Data_Model.Status.Failed_Status),
        // case REQ_MMI_4
        //   If the Status attribute of the Lower Alarm Temperature
        //   or the Upper Alarm Temperature is Invalid,
        //   the Monitor Interface Failure shall be set to True
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.lower_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Invalid |
           api.upper_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Invalid) -->: (api.interface_failure.flag),
        // case REQ_MMI_5
        //   If the Status attribute of the Lower Alarm Temperature
        //   and the Upper Alarm Temperature is Valid,
        //   the Monitor Interface Failure shall be set to False
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.lower_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid &
           api.upper_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid) -->: (!(api.interface_failure.flag)),
        // case REQ_MMI_6
        //   If the Monitor Interface Failure is False,
        //   the Alarm Range variable shall be set to the Desired Temperature Range
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (T) -->: (!(api.interface_failure.flag) -->:
          (api.lower_alarm_temp.degrees == api.lower_alarm_tempWstatus.degrees &
            api.upper_alarm_temp.degrees == api.upper_alarm_tempWstatus.degrees)),
        // case REQ_MMI_7
        //   If the Monitor Interface Failure is True,
        //   the Alarm Range variable is UNSPECIFIED
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (T) -->: (api.interface_failure.flag -->: T)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_upper_alarm_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_upper_alarm_tempWstatus()
    api.logInfo(s"Received on data port upper_alarm_tempWstatus: ${apiUsage_upper_alarm_tempWstatus}")
    val apiUsage_lower_alarm_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_lower_alarm_tempWstatus()
    api.logInfo(s"Received on data port lower_alarm_tempWstatus: ${apiUsage_lower_alarm_tempWstatus}")
    val apiUsage_current_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_current_tempWstatus()
    api.logInfo(s"Received on data port current_tempWstatus: ${apiUsage_current_tempWstatus}")
    val apiUsage_monitor_mode: Option[Isolette_Data_Model.Monitor_Mode.Type] = api.get_monitor_mode()
    api.logInfo(s"Received on data port monitor_mode: ${apiUsage_monitor_mode}")
  }

  def finalise(api: Manage_Monitor_Interface_i_Operational_Api): Unit = { }
}
