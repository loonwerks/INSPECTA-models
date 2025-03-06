// #Sireum

package firewall.SW

import org.sireum._
import art._
import firewall._

@sig trait Firewall_Impl_Api {
  def id: Art.BridgeId
  def EthernetFramesRxIn_Id : Art.PortId
  def EthernetFramesRxOut_Id : Art.PortId

  // Logika spec var representing port state for outgoing event data port
  @spec var EthernetFramesRxOut: Option[SW.RawEthernetMessage] = $

  def put_EthernetFramesRxOut(value : SW.RawEthernetMessage) : Unit = {
    Contract(
      Modifies(EthernetFramesRxOut),
      Ensures(
        EthernetFramesRxOut == Some(value)
      )
    )
    Spec {
      EthernetFramesRxOut = Some(value)
    }

    Art.putValue(EthernetFramesRxOut_Id, SW.RawEthernetMessage_Payload(value))
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

@datatype class Firewall_Impl_Initialization_Api (
  val id: Art.BridgeId,
  val EthernetFramesRxIn_Id : Art.PortId,
  val EthernetFramesRxOut_Id : Art.PortId) extends Firewall_Impl_Api

@datatype class Firewall_Impl_Operational_Api (
  val id: Art.BridgeId,
  val EthernetFramesRxIn_Id : Art.PortId,
  val EthernetFramesRxOut_Id : Art.PortId) extends Firewall_Impl_Api {

  // Logika spec var representing port state for incoming event data port
  @spec var EthernetFramesRxIn: Option[SW.RawEthernetMessage] = $

  def get_EthernetFramesRxIn() : Option[SW.RawEthernetMessage] = {
    Contract(
      Ensures(
        Res == EthernetFramesRxIn
      )
    )
    val value : Option[SW.RawEthernetMessage] = Art.getValue(EthernetFramesRxIn_Id) match {
      case Some(SW.RawEthernetMessage_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port EthernetFramesRxIn.  Expecting 'SW.RawEthernetMessage_Payload' but received ${v}")
        None[SW.RawEthernetMessage]()
      case _ => None[SW.RawEthernetMessage]()
    }
    return value
  }
}
