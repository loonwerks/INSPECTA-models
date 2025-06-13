// #Sireum #Logika

package firewall.SW

import org.sireum._
import firewall._
import org.sireum.U16._
import firewall.SW.RawEthernetMessage.F7373A
import org.sireum.U8._
import firewall.SW.u16Array.I0F3536

// This file will not be overwritten so is safe to edit
object Firewall_Impl_Firewall_Firewall {

  def initialise(api: Firewall_Impl_Initialization_Api): Unit = {
    Contract(
      Modifies(api)
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesRxOut(SW.RawEthernetMessage.example())
    api.put_EthernetFramesTxOut(SW.RawEthernetMessage.example())
  }

  def timeTriggered(api: Firewall_Impl_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        api.EthernetFramesRxOut.isEmpty,
        api.EthernetFramesTxOut.isEmpty
        // END COMPUTE REQUIRES timeTriggered
      ),
      Modifies(api),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee rx
        (api.EthernetFramesRxIn.nonEmpty ___>:
           (api.EthernetFramesRxOut.nonEmpty ___>:
             Firewall_Impl_Firewall_Firewall.should_allow_inbound_frame_rx(api.EthernetFramesRxIn.get, T) &&
               api.EthernetFramesRxIn.get == api.EthernetFramesRxOut.get) &&
             (api.EthernetFramesRxOut.isEmpty __>: Firewall_Impl_Firewall_Firewall.should_allow_inbound_frame_rx(api.EthernetFramesRxIn.get, F))) &&
          (!(api.EthernetFramesRxIn.nonEmpty) __>: api.EthernetFramesRxOut.isEmpty),
        // guarantee tx
        (api.EthernetFramesTxIn.nonEmpty ___>:
           (api.EthernetFramesTxOut.nonEmpty ___>:
             Firewall_Impl_Firewall_Firewall.should_allow_outbound_frame_tx(api.EthernetFramesTxIn.get, T) &&
               api.EthernetFramesTxIn.get == api.EthernetFramesTxOut.get) &&
             (api.EthernetFramesTxOut.isEmpty __>: Firewall_Impl_Firewall_Firewall.should_allow_outbound_frame_tx(api.EthernetFramesTxIn.get, F))) &&
          (!(api.EthernetFramesTxIn.nonEmpty) __>: api.EthernetFramesTxOut.isEmpty)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // setOptions("Logika", """--par --par-branch --timeout 3 --par-branch-pred-num 3 --par-branch-pred-complexity 16 --background disabled""")
    // full verification --> 45s
    api.get_EthernetFramesRxIn() match {
      case Some(rxIn) =>
        if (should_allow_inbound_frame_rx(rxIn, T)) {
          api.put_EthernetFramesRxOut(rxIn)
        }
      case _ =>
    }

    api.get_EthernetFramesTxIn() match {
      case Some(txIn) =>
        if (should_allow_outbound_frame_tx(txIn, T)) {
          api.put_EthernetFramesTxOut(txIn)
        }
      case _ =>
    }
  }

  def finalise(api: Firewall_Impl_Operational_Api): Unit = { }

  // BEGIN FUNCTIONS
  @strictpure def TCP_ALLOWED_PORTS(): SW.u16Array = SW.u16Array(IS[SW.u16Array.I, U16](u16"5760", u16"0", u16"0", u16"0"))

  @strictpure def UDP_ALLOWED_PORTS(): SW.u16Array = SW.u16Array(IS[SW.u16Array.I, U16](u16"68", u16"0", u16"0", u16"0"))

  @strictpure def two_bytes_to_u16(byte0: Base_Types.Unsigned_8, byte1: Base_Types.Unsigned_8): Base_Types.Unsigned_16 = conversions.U8.toU16(byte0) * u16"256" + conversions.U8.toU16(byte1)

  @strictpure def frame_is_wellformed_eth2(frame: SW.RawEthernetMessage): Base_Types.Boolean = if (!(frame.value(F7373A(12)) >= u8"6" &&
        frame.value(F7373A(13)) >= u8"0")) F else T

  @strictpure def frame_has_ipv4(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) __>:
    (if (!(frame.value(F7373A(12)) == u8"8" &&
      frame.value(F7373A(13)) == u8"0")) F else T)

  @strictpure def frame_has_ipv4_tcp(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) __>:
    (if (frame.value(F7373A(23)) != u8"6") F else T)

  @strictpure def frame_has_ipv4_udp(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) __>:
    (if (!(frame.value(F7373A(23)) == u8"17")) F else T)

  @strictpure def frame_has_ipv4_tcp_on_allowed_port(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
    frame_has_ipv4_tcp(frame) __>:
    TCP_ALLOWED_PORTS().value(I0F3536(0)) == two_bytes_to_u16(frame.value(F7373A(36)), frame.value(F7373A(37)))

  @strictpure def frame_has_ipv4_tcp_on_allowed_port_quant(frame: SW.RawEthernetMessage): Base_Types.Boolean = ∃(0 until TCP_ALLOWED_PORTS().value.size)(i => TCP_ALLOWED_PORTS().value(I0F3536(i)) == two_bytes_to_u16(frame.value(F7373A(36)), frame.value(F7373A(37))))

  @strictpure def frame_has_ipv4_udp_on_allowed_port(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
    frame_has_ipv4_udp(frame) __>:
    UDP_ALLOWED_PORTS().value(I0F3536(0)) == two_bytes_to_u16(frame.value(F7373A(36)), frame.value(F7373A(37)))

  @strictpure def frame_has_ipv4_udp_on_allowed_port_quant(frame: SW.RawEthernetMessage): Base_Types.Boolean = ∃(0 until UDP_ALLOWED_PORTS().value.size)(i => UDP_ALLOWED_PORTS().value(I0F3536(i)) == two_bytes_to_u16(frame.value(F7373A(36)), frame.value(F7373A(37))))

  @strictpure def frame_has_ipv6(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) __>:
    (if (!(frame.value(F7373A(12)) == u8"134" &&
      frame.value(F7373A(13)) == u8"221")) F else T)

  @strictpure def frame_has_arp(frame: SW.RawEthernetMessage): Base_Types.Boolean = frame_is_wellformed_eth2(frame) __>:
    (if (!(frame.value(F7373A(12)) == u8"8" &&
      frame.value(F7373A(13)) == u8"6")) F else T)

  @strictpure def hlr_1_1(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (!(frame_is_wellformed_eth2(frame))) should_allow == F else T

  @strictpure def hlr_1_2(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv6(frame)) should_allow == F else T

  @strictpure def hlr_1_3(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
        !(frame_has_ipv4_tcp(frame) || frame_has_ipv4_udp(frame))) should_allow == F else T

  @strictpure def hlr_1_4(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
        frame_has_ipv4_tcp(frame) &&
        !(frame_has_ipv4_tcp_on_allowed_port(frame))) should_allow == F else T

  @strictpure def hlr_1_5(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
        frame_has_ipv4_udp(frame) &&
        !(frame_has_ipv4_udp_on_allowed_port(frame))) should_allow == F else T

  @strictpure def hlr_1_6(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_arp(frame)) should_allow == T else T

  @strictpure def hlr_1_7(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
        frame_has_ipv4_tcp(frame) &&
        frame_has_ipv4_tcp_on_allowed_port(frame)) should_allow == T else T

  @strictpure def hlr_1_8(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame) &&
        frame_has_ipv4_udp(frame) &&
        frame_has_ipv4_udp_on_allowed_port(frame)) should_allow == T else T

  @strictpure def should_allow_inbound_frame_rx(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = hlr_1_1(frame, should_allow) && hlr_1_2(frame, should_allow) &&
    hlr_1_3(frame, should_allow) &&
    hlr_1_4(frame, should_allow) &&
    hlr_1_5(frame, should_allow) &&
    hlr_1_6(frame, should_allow) &&
    hlr_1_7(frame, should_allow) &&
    hlr_1_8(frame, should_allow)

  @strictpure def hlr_2_1(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (!(frame_is_wellformed_eth2(frame))) should_allow == F else T

  @strictpure def hlr_2_2(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv6(frame)) should_allow == F else T

  @strictpure def hlr_2_3(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_arp(frame)) should_allow == T else T

  @strictpure def hlr_2_4(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = if (frame_is_wellformed_eth2(frame) && frame_has_ipv4(frame)) should_allow == T else T

  @strictpure def should_allow_outbound_frame_tx(frame: SW.RawEthernetMessage, should_allow: Base_Types.Boolean): Base_Types.Boolean = hlr_2_1(frame, should_allow) && hlr_2_2(frame, should_allow) &&
    hlr_2_3(frame, should_allow) &&
    hlr_2_4(frame, should_allow)
  // END FUNCTIONS

}
