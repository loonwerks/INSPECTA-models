// #Sireum

package firewall.SW

import org.sireum._
import firewall._

// This file will not be overwritten so is safe to edit
object LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver {

  def initialise(api: LowLevelEthernetDriver_Impl_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesRx(SW.RawEthernetMessage.example())
  }

  def timeTriggered(api: LowLevelEthernetDriver_Impl_Operational_Api): Unit = {
    // example api usage


  }

  def finalise(api: LowLevelEthernetDriver_Impl_Operational_Api): Unit = { }
}
