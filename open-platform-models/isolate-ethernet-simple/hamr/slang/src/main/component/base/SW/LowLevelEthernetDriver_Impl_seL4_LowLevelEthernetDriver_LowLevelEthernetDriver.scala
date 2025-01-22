// #Sireum

package base.SW

import org.sireum._
import base._

// This file will not be overwritten so is safe to edit
object LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {

  def initialise(api: LowLevelEthernetDriver_Impl_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesRx(SW.StructuredEthernetMessage_i.example())
  }

  def timeTriggered(api: LowLevelEthernetDriver_Impl_Operational_Api): Unit = {
    // example api usage

    val apiUsage_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i] = api.get_EthernetFramesTx()
    api.logInfo(s"Received on event data port EthernetFramesTx: ${apiUsage_EthernetFramesTx}")
  }

  def finalise(api: LowLevelEthernetDriver_Impl_Operational_Api): Unit = { }
}
