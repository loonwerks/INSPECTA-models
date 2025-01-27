// #Sireum

package base.SW

import org.sireum._
import base._

// This file will not be overwritten so is safe to edit
object ArduPilot_Impl_seL4_ArduPilot_ArduPilot {

  def initialise(api: ArduPilot_Impl_Initialization_Api): Unit = {
    // event data ports so nothing to initialize
  }

  def timeTriggered(api: ArduPilot_Impl_Operational_Api): Unit = {
    // example api usage

    val apiUsage_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = api.get_EthernetFramesRx()
    api.logInfo(s"Received on event data port EthernetFramesRx: ${apiUsage_EthernetFramesRx}")
  }

  def finalise(api: ArduPilot_Impl_Operational_Api): Unit = { }
}
