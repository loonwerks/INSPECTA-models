// #Sireum

package isolette.Monitor

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf {

  def initialise(api: Detect_Monitor_Failure_i_Initialization_Api): Unit = {
    api.put_internal_failure(Isolette_Data_Model.Failure_Flag_i(F))
  }

  def timeTriggered(api: Detect_Monitor_Failure_i_Operational_Api): Unit = {
  }

  def finalise(api: Detect_Monitor_Failure_i_Operational_Api): Unit = { }
}
