// #Sireum

package base.SW

import org.sireum._
import art._
import base._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@sig trait ArduPilot_Impl_Api {
  def id: Art.BridgeId
  def EthernetFramesRx_Id : Art.PortId
  def EthernetFramesTx_Id : Art.PortId

  // Logika spec var representing port state for outgoing event data port
  @spec var EthernetFramesTx: Option[SW.StructuredEthernetMessage_i] = $

  def put_EthernetFramesTx(value : SW.StructuredEthernetMessage_i) : Unit = {
    Contract(
      Modifies(EthernetFramesTx),
      Ensures(
        EthernetFramesTx == Some(value)
      )
    )
    Spec {
      EthernetFramesTx = Some(value)
    }

    Art.putValue(EthernetFramesTx_Id, SW.StructuredEthernetMessage_i_Payload(value))
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

@datatype class ArduPilot_Impl_Initialization_Api (
  val id: Art.BridgeId,
  val EthernetFramesRx_Id : Art.PortId,
  val EthernetFramesTx_Id : Art.PortId) extends ArduPilot_Impl_Api

@datatype class ArduPilot_Impl_Operational_Api (
  val id: Art.BridgeId,
  val EthernetFramesRx_Id : Art.PortId,
  val EthernetFramesTx_Id : Art.PortId) extends ArduPilot_Impl_Api {

  // Logika spec var representing port state for incoming event data port
  @spec var EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = $

  def get_EthernetFramesRx() : Option[SW.StructuredEthernetMessage_i] = {
    Contract(
      Ensures(
        Res == EthernetFramesRx
      )
    )
    val value : Option[SW.StructuredEthernetMessage_i] = Art.getValue(EthernetFramesRx_Id) match {
      case Some(SW.StructuredEthernetMessage_i_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port EthernetFramesRx.  Expecting 'SW.StructuredEthernetMessage_i_Payload' but received ${v}")
        None[SW.StructuredEthernetMessage_i]()
      case _ => None[SW.StructuredEthernetMessage_i]()
    }
    return value
  }
}
