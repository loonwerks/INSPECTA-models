// #Sireum

package base.SW

import org.sireum._
import base._
import org.sireum.Random.Impl.Xoshiro256

// This file will not be overwritten so is safe to edit
object ArduPilot_Impl_seL4_ArduPilot_ArduPilot {

  def freshRandomLib: RandomLib = {
    RandomLib(Random.Gen64Impl(Xoshiro256.create))
  }

  def initialise(api: ArduPilot_Impl_Initialization_Api): Unit = {
    // event data ports so nothing to initialize
  }

  def timeTriggered(api: ArduPilot_Impl_Operational_Api): Unit = {
    // example api usage

    val apiUsage_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i] = api.get_EthernetFramesRx()
    api.logInfo(s"Received on event data port EthernetFramesRx: ${apiUsage_EthernetFramesRx}")

    val txOut = freshRandomLib.nextSWStructuredEthernetMessage_i()(
      internetProtocol = InternetProtocol.IPV6,
      frameProtocol = FrameProtocol.ARP,
      arpType = ARP_Type.REQUEST)

    api.put_EthernetFramesTx(txOut)
  }

  def finalise(api: ArduPilot_Impl_Operational_Api): Unit = { }
}
