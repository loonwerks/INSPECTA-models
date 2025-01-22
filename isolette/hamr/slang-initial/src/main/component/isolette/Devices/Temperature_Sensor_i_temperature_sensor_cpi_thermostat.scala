// #Sireum

package isolette.Devices

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Temperature_Sensor_i_temperature_sensor_cpi_thermostat {

  def initialise(api: Temperature_Sensor_i_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_current_tempWstatus(Isolette_Data_Model.TempWstatus_i.example())
  }

  def timeTriggered(api: Temperature_Sensor_i_Operational_Api): Unit = {
    // example api usage

    val apiUsage_air: Option[Isolette_Data_Model.PhysicalTemp_i] = api.get_air()
    api.logInfo(s"Received on data port air: ${apiUsage_air}")
  }

  def finalise(api: Temperature_Sensor_i_Operational_Api): Unit = { }
}
