// #Sireum

package base.SW

import org.sireum._
import art._
import base._

@sig trait Firewall_Impl_Api {
  def id: Art.BridgeId
  def EthernetFramesRxIn_Id : Art.PortId
  def EthernetFramesTxIn_Id : Art.PortId
  def EthernetFramesRxOut_Id : Art.PortId
  def EthernetFramesTxOut_Id : Art.PortId

  // Logika spec var representing port state for outgoing event data port
  @spec var EthernetFramesRxOut: Option[SW.StructuredEthernetMessage_i] = $

  def put_EthernetFramesRxOut(value : SW.StructuredEthernetMessage_i) : Unit = {
    Contract(
      Modifies(EthernetFramesRxOut),
      Ensures(
        EthernetFramesRxOut == Some(value)
      )
    )
    Spec {
      EthernetFramesRxOut = Some(value)
    }

    Art.putValue(EthernetFramesRxOut_Id, SW.StructuredEthernetMessage_i_Payload(value))
  }

  // Logika spec var representing port state for outgoing event data port
  @spec var EthernetFramesTxOut: Option[SW.StructuredEthernetMessage_i] = $

  def put_EthernetFramesTxOut(value : SW.StructuredEthernetMessage_i) : Unit = {
    Contract(
      Modifies(EthernetFramesTxOut),
      Ensures(
        EthernetFramesTxOut == Some(value)
      )
    )
    Spec {
      EthernetFramesTxOut = Some(value)
    }

    Art.putValue(EthernetFramesTxOut_Id, SW.StructuredEthernetMessage_i_Payload(value))
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
  val EthernetFramesTxIn_Id : Art.PortId,
  val EthernetFramesRxOut_Id : Art.PortId,
  val EthernetFramesTxOut_Id : Art.PortId) extends Firewall_Impl_Api

@datatype class Firewall_Impl_Operational_Api (
  val id: Art.BridgeId,
  val EthernetFramesRxIn_Id : Art.PortId,
  val EthernetFramesTxIn_Id : Art.PortId,
  val EthernetFramesRxOut_Id : Art.PortId,
  val EthernetFramesTxOut_Id : Art.PortId) extends Firewall_Impl_Api {

  // Logika spec var representing port state for incoming event data port
  @spec var EthernetFramesRxIn: Option[SW.StructuredEthernetMessage_i] = $

  def get_EthernetFramesRxIn() : Option[SW.StructuredEthernetMessage_i] = {
    Contract(
      Ensures(
        Res == EthernetFramesRxIn
      )
    )
    val value : Option[SW.StructuredEthernetMessage_i] = Art.getValue(EthernetFramesRxIn_Id) match {
      case Some(SW.StructuredEthernetMessage_i_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port EthernetFramesRxIn.  Expecting 'SW.StructuredEthernetMessage_i_Payload' but received ${v}")
        None[SW.StructuredEthernetMessage_i]()
      case _ => None[SW.StructuredEthernetMessage_i]()
    }
    return value
  }

  // Logika spec var representing port state for incoming event data port
  @spec var EthernetFramesTxIn: Option[SW.StructuredEthernetMessage_i] = $

  def get_EthernetFramesTxIn() : Option[SW.StructuredEthernetMessage_i] = {
    Contract(
      Ensures(
        Res == EthernetFramesTxIn
      )
    )
    val value : Option[SW.StructuredEthernetMessage_i] = Art.getValue(EthernetFramesTxIn_Id) match {
      case Some(SW.StructuredEthernetMessage_i_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port EthernetFramesTxIn.  Expecting 'SW.StructuredEthernetMessage_i_Payload' but received ${v}")
        None[SW.StructuredEthernetMessage_i]()
      case _ => None[SW.StructuredEthernetMessage_i]()
    }
    return value
  }
}
