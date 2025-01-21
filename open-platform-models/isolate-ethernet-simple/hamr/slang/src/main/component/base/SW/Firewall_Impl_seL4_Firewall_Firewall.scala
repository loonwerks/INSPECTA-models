// #Sireum

package base.SW

import org.sireum._
import base._

// This file will not be overwritten so is safe to edit
object Firewall_Impl_seL4_Firewall_Firewall {

  def initialise(api: Firewall_Impl_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesRxOut(SW.StructuredEthernetMessage_i.example())
    api.put_EthernetFramesTxOut(SW.StructuredEthernetMessage_i.example())
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
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee RC_INSPECTA_00_HLR_2
        //   1.2 firewall: drop ipv6 frames (RC_INSPECTA_00-HLR-2))) The firewall shall drop any frame that is type ipv6.
        (api.EthernetFramesRxIn.nonEmpty &&
           SW.GUMBO__Library.getInternetProtocol(api.EthernetFramesRxIn.get) == SW.InternetProtocol.IPV6) ->: api.EthernetFramesRxOut.isEmpty &
          (api.EthernetFramesTxIn.nonEmpty && SW.GUMBO__Library.isIPV6(api.EthernetFramesTxIn.get)) ->: api.EthernetFramesTxOut.isEmpty,
        // guarantee RC_INSPECTA_00_HLR_4
        //   1.4 firewall: drop RxIn ipv4 tcp frames with unexpected ports (RC_INSPECTA_00-HLR- 4))) The firewall shall
        //   drop any frame from RxIn that is an Ipv4 frame whose protocol is TCP and whose port is not defined in the port whitelist.
        (api.EthernetFramesRxIn.nonEmpty &&
           (SW.GUMBO__Library.isIPV4(api.EthernetFramesRxIn.get) & SW.GUMBO__Library.isTCP(api.EthernetFramesRxIn.get) &
             SW.GUMBO__Library.isPortWhitelisted(api.EthernetFramesRxIn.get))) ->: api.EthernetFramesRxOut.isEmpty,
        // guarantee RC_INSPECTA_00_HLR_5
        //   1.5 firewall: reply to RxIn arp requests (RC_INSPECTA_00-HLR-5))) If the firewall gets an Arp request frame from RxIn,
        //   the firewall shall send an Arp reply frame to TxOut.
        (api.EthernetFramesRxIn.nonEmpty && SW.GUMBO__Library.isARP_Request(api.EthernetFramesRxIn.get)) ->: api.EthernetFramesTxOut.nonEmpty &
          SW.GUMBO__Library.isARP_Reply(api.EthernetFramesTxOut.get),
        // guarantee RC_INSPECTA_00_HLR_6
        //   1.6 firewall: copy through allowed tcp port packets (RC_INSPECTA_00-HLR-6))) The firewall shall copy any frame from RxIn 
        //   that is an Ipv4 frame with the TCP protocol and whose port is defined in the port whitelist to RxOut.
        (api.EthernetFramesRxIn.nonEmpty &&
           (SW.GUMBO__Library.isIPV4(api.EthernetFramesRxIn.get) & SW.GUMBO__Library.isTCP(api.EthernetFramesRxIn.get) &
             SW.GUMBO__Library.isPortWhitelisted(api.EthernetFramesRxIn.get))) ->: (api.EthernetFramesRxOut.nonEmpty &&
           api.EthernetFramesRxIn.get == api.EthernetFramesRxOut.get),
        // guarantee RC_INSPECTA_00_HLR_7
        //   1.7 firewall: copy out tx arp and ipv4 frames (RC_INSPECTA_00-HLR-7))) The firewall shall copy any frame from TxIn that
        //   is an Ipv4 or Arp frame to TxOut.
        (api.EthernetFramesTxIn.nonEmpty &&
           (SW.GUMBO__Library.isIPV4(api.EthernetFramesTxIn.get) | SW.GUMBO__Library.isARP(api.EthernetFramesRxIn.get))) ->: (api.EthernetFramesTxOut.nonEmpty &&
           api.EthernetFramesTxIn.get == api.EthernetFramesTxOut.get)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_EthernetFramesRxIn: Option[SW.StructuredEthernetMessage_i] = api.get_EthernetFramesRxIn()
    api.logInfo(s"Received on event data port EthernetFramesRxIn: ${apiUsage_EthernetFramesRxIn}")
    val apiUsage_EthernetFramesTxIn: Option[SW.StructuredEthernetMessage_i] = api.get_EthernetFramesTxIn()
    api.logInfo(s"Received on event data port EthernetFramesTxIn: ${apiUsage_EthernetFramesTxIn}")
  }

  def finalise(api: Firewall_Impl_Operational_Api): Unit = { }
}
