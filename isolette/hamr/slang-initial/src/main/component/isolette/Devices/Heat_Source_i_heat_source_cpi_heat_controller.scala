// #Sireum

package isolette.Devices

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Heat_Source_i_heat_source_cpi_heat_controller {

  def initialise(api: Heat_Source_i_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_heat_out(Isolette_Environment.Heat.byOrdinal(0).get)
  }

  def timeTriggered(api: Heat_Source_i_Operational_Api): Unit = {
    // example api usage

    val apiUsage_heat_control: Option[Isolette_Data_Model.On_Off.Type] = api.get_heat_control()
    api.logInfo(s"Received on data port heat_control: ${apiUsage_heat_control}")
  }

  def finalise(api: Heat_Source_i_Operational_Api): Unit = { }
}
