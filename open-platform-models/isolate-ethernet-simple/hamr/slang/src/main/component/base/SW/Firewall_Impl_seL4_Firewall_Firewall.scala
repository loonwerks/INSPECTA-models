// #Sireum #Logika

package base.SW

import org.sireum._
import base.SW.GUMBO__Library.{getFrameProtocol, getInternetProtocol, isARP, isARP_Reply, isARP_Request, isIPV4, isIPV6, isPortWhitelisted, isTCP}
import base._

// This file will not be overwritten so is safe to edit
object Firewall_Impl_seL4_Firewall_Firewall {

  def initialise(api: Firewall_Impl_Initialization_Api): Unit = {
    // event data ports so nothing to initialize
  }

  def timeTriggered(api: Firewall_Impl_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        api.EthernetFramesRxOut.isEmpty,
        api.EthernetFramesTxOut.isEmpty,
        // assume onlyOneInEvent
        //   Allow at most one incoming event per dispatch
        !(api.EthernetFramesRxIn.nonEmpty) & !(api.EthernetFramesTxIn.nonEmpty) |
          (api.EthernetFramesRxIn.nonEmpty |^ api.EthernetFramesTxIn.nonEmpty)
        // END COMPUTE REQUIRES timeTriggered
      ),
      Modifies(api),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee RC_INSPECTA_00_HLR_2
        //   1.2 firewall: drop ipv6 frames (RC_INSPECTA_00-HLR-2) The firewall shall drop any frame that is type ipv6.
        api.EthernetFramesRxIn.nonEmpty ___>:
          api.EthernetFramesRxIn.get.internetProtocol == SW.InternetProtocol.IPV6 __>:
            api.EthernetFramesRxOut.isEmpty,
        // guarantee RC_INSPECTA_00_HLR_4
        //   1.4 firewall: drop RxIn ipv4 tcp frames with unexpected ports (RC_INSPECTA_00-HLR-4) The firewall shall
        //   drop any frame from RxIn that is an Ipv4 frame whose protocol is TCP and whose port is not defined in the port whitelist.
        api.EthernetFramesRxIn.nonEmpty ___>:
          api.EthernetFramesRxIn.get.internetProtocol == SW.InternetProtocol.IPV6 &
            api.EthernetFramesRxIn.get.frameProtocol == SW.FrameProtocol.TCP &
            !(api.EthernetFramesRxIn.get.portIsWhitelisted) __>:
            api.EthernetFramesRxOut.isEmpty ||
              api.EthernetFramesRxOut.get != api.EthernetFramesRxIn.get,
        // guarantee RC_INSPECTA_00_HLR_5
        //   1.5 firewall: reply to RxIn arp requests (RC_INSPECTA_00-HLR-5) If the firewall gets an Arp request frame from RxIn,
        //   the firewall shall send an Arp reply frame to TxOut.
        (api.EthernetFramesRxIn.nonEmpty ___>:
           api.EthernetFramesRxIn.get.internetProtocol == SW.InternetProtocol.IPV4 &
             api.EthernetFramesRxIn.get.frameProtocol == SW.FrameProtocol.ARP) __>:
          api.EthernetFramesTxOut.nonEmpty &&
            api.EthernetFramesTxOut.get.arpType == SW.ARP_Type.REPLY,
        // guarantee RC_INSPECTA_00_HLR_6
        //   1.6 firewall: copy through allowed tcp port packets (RC_INSPECTA_00-HLR-6) The firewall shall copy any frame from RxIn 
        //   that is an Ipv4 frame with the TCP protocol and whose port is defined in the port whitelist to RxOut.
        api.EthernetFramesRxIn.nonEmpty ___>:
          api.EthernetFramesRxIn.get.internetProtocol == SW.InternetProtocol.IPV4 &
            api.EthernetFramesRxIn.get.frameProtocol == SW.FrameProtocol.TCP &
            api.EthernetFramesRxIn.get.portIsWhitelisted __>:
            api.EthernetFramesRxOut.nonEmpty &&
              api.EthernetFramesRxIn.get == api.EthernetFramesRxOut.get,
        // guarantee RC_INSPECTA_00_HLR_7
        //   1.7 firewall: copy out tx arp and ipv4 frames (RC_INSPECTA_00-HLR-7) The firewall shall copy any frame from TxIn that
        //   is an Ipv4 or Arp frame to TxOut.
        api.EthernetFramesTxIn.nonEmpty ___>:
          api.EthernetFramesTxIn.get.internetProtocol == SW.InternetProtocol.IPV4 |
            api.EthernetFramesTxIn.get.frameProtocol == SW.FrameProtocol.ARP __>:
            api.EthernetFramesTxOut.nonEmpty &&
              api.EthernetFramesTxIn.get == api.EthernetFramesTxOut.get
        // END COMPUTE ENSURES timeTriggered
      )
    )
    api.get_EthernetFramesRxIn() match {
      case Some(payload) =>
        getInternetProtocol(payload) match {
          case InternetProtocol.IPV4 =>
            getFrameProtocol(payload) match {
              case FrameProtocol.TCP =>
                if (!isPortWhitelisted(payload)) {
                  // RC_INSPECTA_00-HLR-4
                  api.logInfo("Dropping RxIn IPV4 TCP message: port not whitelisted")
                } else {
                  // RC_INSPECTA_00-HLR-6
                  api.put_EthernetFramesRxOut(payload)
                }
              case FrameProtocol.ARP =>
                if (isARP_Request(payload)) {
                  // RC_INSPECTA_00-HLR-5
                  api.put_EthernetFramesTxOut(payload(arpType = ARP_Type.REPLY))
                } else {
                  api.logInfo("Dropping RxIn IPV4 non-ARP request message")
                }
            }
          case InternetProtocol.IPV6 =>
            // RC_INSPECTA_00-HLR-2
            api.logInfo("Dropping IPV6 RxIn message")
            Deduce(|-(api.EthernetFramesRxOut.isEmpty))
        }
      case _ =>
    }

    api.get_EthernetFramesTxIn() match {
      case Some(payload) =>
        getInternetProtocol(payload) match {
          case InternetProtocol.IPV4 =>
            // RC_INSPECTA_00-HLR-7
            api.put_EthernetFramesTxOut(payload)
          case InternetProtocol.IPV6 =>
            // RC_INSPECTA_00-HLR-7
            if (isARP(payload)) {
              api.put_EthernetFramesTxOut(payload)
            } else {
              api.logInfo("Dropping TxIn IPV6 non-ARP message")
            }
    }
      case _ =>
    }
  }

  def finalise(api: Firewall_Impl_Operational_Api): Unit = { }
}
