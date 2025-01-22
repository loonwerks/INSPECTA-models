// #Sireum

package isolette.Operator_Interface

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Operator_Interface_Thread_i_operator_interface_oip_oit {

  def initialise(api: Operator_Interface_Thread_i_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_lower_desired_tempWstatus(Isolette_Data_Model.TempWstatus_i.example())
    api.put_upper_desired_tempWstatus(Isolette_Data_Model.TempWstatus_i.example())
    api.put_lower_alarm_tempWstatus(Isolette_Data_Model.TempWstatus_i.example())
    api.put_upper_alarm_tempWstatus(Isolette_Data_Model.TempWstatus_i.example())
  }

  def timeTriggered(api: Operator_Interface_Thread_i_Operational_Api): Unit = {
    // example api usage

    val apiUsage_regulator_status: Option[Isolette_Data_Model.Status.Type] = api.get_regulator_status()
    api.logInfo(s"Received on data port regulator_status: ${apiUsage_regulator_status}")
    val apiUsage_monitor_status: Option[Isolette_Data_Model.Status.Type] = api.get_monitor_status()
    api.logInfo(s"Received on data port monitor_status: ${apiUsage_monitor_status}")
    val apiUsage_display_temperature: Option[Isolette_Data_Model.Temp_i] = api.get_display_temperature()
    api.logInfo(s"Received on data port display_temperature: ${apiUsage_display_temperature}")
    val apiUsage_alarm_control: Option[Isolette_Data_Model.On_Off.Type] = api.get_alarm_control()
    api.logInfo(s"Received on data port alarm_control: ${apiUsage_alarm_control}")
  }

  def finalise(api: Operator_Interface_Thread_i_Operational_Api): Unit = { }
}
