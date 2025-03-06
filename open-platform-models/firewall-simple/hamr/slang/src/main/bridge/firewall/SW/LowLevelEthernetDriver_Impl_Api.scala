// #Sireum

package firewall.SW

import org.sireum._
import art._
import firewall._

@sig trait LowLevelEthernetDriver_Impl_Api {
  def id: Art.BridgeId
  def EthernetFramesRx_Id : Art.PortId

  // Logika spec var representing port state for outgoing event data port
  @spec var EthernetFramesRx: Option[SW.RawEthernetMessage] = $

  def put_EthernetFramesRx(value : SW.RawEthernetMessage) : Unit = {
    Contract(
      Modifies(EthernetFramesRx),
      Ensures(
        EthernetFramesRx == Some(value)
      )
    )
    Spec {
      EthernetFramesRx = Some(value)
    }

    Art.putValue(EthernetFramesRx_Id, SW.RawEthernetMessage_Payload(value))
  }

  def logInfo(msg: String): Unit = {
    Art.logInfo(id, msg)
  }

  def logDebug(msg: String): Unit = {
    Art.logDebug(id, msg)
  }

  def logError(msg: String): Unit = {
    Art.logError(id, msg)
  }
}

@datatype class LowLevelEthernetDriver_Impl_Initialization_Api (
  val id: Art.BridgeId,
  val EthernetFramesRx_Id : Art.PortId) extends LowLevelEthernetDriver_Impl_Api

@datatype class LowLevelEthernetDriver_Impl_Operational_Api (
  val id: Art.BridgeId,
  val EthernetFramesRx_Id : Art.PortId) extends LowLevelEthernetDriver_Impl_Api {

}
