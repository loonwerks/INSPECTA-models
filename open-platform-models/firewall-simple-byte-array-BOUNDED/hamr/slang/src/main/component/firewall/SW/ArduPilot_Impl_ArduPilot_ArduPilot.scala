// #Sireum

package firewall.SW

import org.sireum._
import firewall._

// This file will not be overwritten so is safe to edit
object ArduPilot_Impl_ArduPilot_ArduPilot {

  def initialise(api: ArduPilot_Impl_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesTx(SW.RawEthernetMessage.example())
  }

  def timeTriggered(api: ArduPilot_Impl_Operational_Api): Unit = {
    // example api usage

    val apiUsage_EthernetFramesRx: Option[SW.RawEthernetMessage] = api.get_EthernetFramesRx()
    api.logInfo(s"Received on event data port EthernetFramesRx: ${apiUsage_EthernetFramesRx}")
  }

  def finalise(api: ArduPilot_Impl_Operational_Api): Unit = { }
}
