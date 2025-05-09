// #Sireum

package firewall

import org.sireum._
import art._
import art.PortMode._
import art.DispatchPropertyProtocol._
import art.Art.BridgeId._
import art.Art.PortId._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object Arch {
  val seL4_Impl_Instance_ArduPilot_ArduPilot : firewall.SW.ArduPilot_Impl_ArduPilot_ArduPilot_Bridge = {
    val EthernetFramesRx = Port[SW.RawEthernetMessage] (id = portId"0", name = "seL4_Impl_Instance_ArduPilot_ArduPilot_EthernetFramesRx", mode = EventIn)

    firewall.SW.ArduPilot_Impl_ArduPilot_ArduPilot_Bridge(
      id = bridgeId"0",
      name = "seL4_Impl_Instance_ArduPilot_ArduPilot",
      dispatchProtocol = Periodic(period = 1000),
      dispatchTriggers = None(),

      EthernetFramesRx = EthernetFramesRx
    )
  }
  val seL4_Impl_Instance_Firewall_Firewall : firewall.SW.Firewall_Impl_Firewall_Firewall_Bridge = {
    val EthernetFramesRxIn = Port[SW.RawEthernetMessage] (id = portId"1", name = "seL4_Impl_Instance_Firewall_Firewall_EthernetFramesRxIn", mode = EventIn)
    val EthernetFramesRxOut = Port[SW.RawEthernetMessage] (id = portId"2", name = "seL4_Impl_Instance_Firewall_Firewall_EthernetFramesRxOut", mode = EventOut)

    firewall.SW.Firewall_Impl_Firewall_Firewall_Bridge(
      id = bridgeId"1",
      name = "seL4_Impl_Instance_Firewall_Firewall",
      dispatchProtocol = Periodic(period = 1000),
      dispatchTriggers = None(),

      EthernetFramesRxIn = EthernetFramesRxIn,
      EthernetFramesRxOut = EthernetFramesRxOut
    )
  }
  val seL4_Impl_Instance_LowLevelEthernetDriver_LowLevelEthernetDriver : firewall.SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Bridge = {
    val EthernetFramesRx = Port[SW.RawEthernetMessage] (id = portId"3", name = "seL4_Impl_Instance_LowLevelEthernetDriver_LowLevelEthernetDriver_EthernetFramesRx", mode = EventOut)

    firewall.SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Bridge(
      id = bridgeId"2",
      name = "seL4_Impl_Instance_LowLevelEthernetDriver_LowLevelEthernetDriver",
      dispatchProtocol = Periodic(period = 1000),
      dispatchTriggers = None(),

      EthernetFramesRx = EthernetFramesRx
    )
  }

  val ad : ArchitectureDescription = {

    ArchitectureDescription(
      components = IS[Art.BridgeId, Bridge] (seL4_Impl_Instance_ArduPilot_ArduPilot, seL4_Impl_Instance_Firewall_Firewall, seL4_Impl_Instance_LowLevelEthernetDriver_LowLevelEthernetDriver),

      connections = IS[Art.ConnectionId, UConnection] (Connection(from = seL4_Impl_Instance_Firewall_Firewall.EthernetFramesRxOut, to = seL4_Impl_Instance_ArduPilot_ArduPilot.EthernetFramesRx),
                                                       Connection(from = seL4_Impl_Instance_LowLevelEthernetDriver_LowLevelEthernetDriver.EthernetFramesRx, to = seL4_Impl_Instance_Firewall_Firewall.EthernetFramesRxIn))
    )
  }
}
