// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::seL4_Firewall_Firewall_api::*;
use vstd::prelude::*;

verus! {

  pub struct seL4_Firewall_Firewall {
  }

  impl seL4_Firewall_Firewall {
    pub fn new() -> Self 
    {
      Self {
      }
    }

    pub fn initialize<API: seL4_Firewall_Firewall_Put_Api>(
      &mut self,
      api: &mut seL4_Firewall_Firewall_Application_Api<API>) 
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_Firewall_Firewall_Full_Api>(
      &mut self,
      api: &mut seL4_Firewall_Firewall_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).EthernetFramesRxOut.is_none(),
        old(api).EthernetFramesTxOut.is_none(),
        // assume onlyOneInEvent
        //   Allow at most one incoming event per dispatch
        (!(old(api).EthernetFramesRxIn.is_some()) && !(old(api).EthernetFramesTxIn.is_some())) ||
          old(api).EthernetFramesRxIn.is_some() ^ old(api).EthernetFramesTxIn.is_some(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee RC_INSPECTA_00_HLR_2
        //   1.2 firewall: drop ipv6 frames (RC_INSPECTA_00-HLR-2) The firewall shall drop any frame that is type ipv6.
        (api.EthernetFramesRxIn.is_some() ==>
          (api.EthernetFramesRxIn.unwrap().internetProtocol == SW::InternetProtocol::IPV6)) ==>
          api.EthernetFramesRxOut.is_none(),
        // guarantee RC_INSPECTA_00_HLR_4
        //   1.4 firewall: drop RxIn ipv4 tcp frames with unexpected ports (RC_INSPECTA_00-HLR-4) The firewall shall
        //   drop any frame from RxIn that is an Ipv4 frame whose protocol is TCP and whose port is not defined in the port whitelist.
        (api.EthernetFramesRxIn.is_some() ==>
          (((api.EthernetFramesRxIn.unwrap().internetProtocol == SW::InternetProtocol::IPV6) &&
            (api.EthernetFramesRxIn.unwrap().frameProtocol == SW::FrameProtocol::TCP)) &&
            !(api.EthernetFramesRxIn.unwrap().portIsWhitelisted))) ==>
          api.EthernetFramesRxOut.is_none() ||
            (api.EthernetFramesRxOut.unwrap() != api.EthernetFramesRxIn.unwrap()),
        // guarantee RC_INSPECTA_00_HLR_5
        //   1.5 firewall: reply to RxIn arp requests (RC_INSPECTA_00-HLR-5) If the firewall gets an Arp request frame from RxIn,
        //   the firewall shall send an Arp reply frame to TxOut.
        (api.EthernetFramesRxIn.is_some() ==>
          (isIPV4(api.EthernetFramesRxIn.unwrap()) && isARP(api.EthernetFramesRxIn.unwrap()))) ==>
          api.EthernetFramesTxOut.is_some() && isARP_Reply(api.EthernetFramesTxOut.unwrap()),
        // guarantee RC_INSPECTA_00_HLR_6
        //   1.6 firewall: copy through allowed tcp port packets (RC_INSPECTA_00-HLR-6) The firewall shall copy any frame from RxIn 
        //   that is an Ipv4 frame with the TCP protocol and whose port is defined in the port whitelist to RxOut.
        (api.EthernetFramesRxIn.is_some() ==>
          (((api.EthernetFramesRxIn.unwrap().internetProtocol == SW::InternetProtocol::IPV4) &&
            (api.EthernetFramesRxIn.unwrap().frameProtocol == SW::FrameProtocol::TCP)) &&
            api.EthernetFramesRxIn.unwrap().portIsWhitelisted)) ==>
          api.EthernetFramesRxOut.is_some() &&
            (api.EthernetFramesRxIn.unwrap() == api.EthernetFramesRxOut.unwrap()),
        // guarantee RC_INSPECTA_00_HLR_7
        //   1.7 firewall: copy out tx arp and ipv4 frames (RC_INSPECTA_00-HLR-7) The firewall shall copy any frame from TxIn that
        //   is an Ipv4 or Arp frame to TxOut.
        (api.EthernetFramesTxIn.is_some() ==>
          ((api.EthernetFramesTxIn.unwrap().internetProtocol == SW::InternetProtocol::IPV4) ||
            (api.EthernetFramesTxIn.unwrap().frameProtocol == SW::FrameProtocol::ARP))) ==>
          api.EthernetFramesTxOut.is_some() &&
            (api.EthernetFramesTxIn.unwrap() == api.EthernetFramesTxOut.unwrap()),
        // END MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel) 
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          log_warn_channel(channel)
        }
      }
    }
  }

  #[verifier::external_body]
  pub exec fn log_info(message: &str) {
    log::info!("{}", message);
  }

  #[verifier::external_body]
  pub exec fn log_warn_channel(channel: u32) {
    log::warn!("Unexpected channel {}", channel);
  }

  // BEGIN MARKER GUMBO METHODS
  pub open spec fn isMalformedFrame(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.malformedFrame
  }

  pub open spec fn getInternetProtocol(v: SW::StructuredEthernetMessage_i) -> SW::InternetProtocol
  {
    v.internetProtocol
  }

  pub open spec fn isIPV4(v: SW::StructuredEthernetMessage_i) -> bool
  {
    getInternetProtocol(v) == SW::InternetProtocol::IPV4
  }

  pub open spec fn isIPV6(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.internetProtocol == SW::InternetProtocol::IPV6
  }

  pub open spec fn getFrameProtocol(v: SW::StructuredEthernetMessage_i) -> SW::FrameProtocol
  {
    v.frameProtocol
  }

  pub open spec fn isTCP(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.frameProtocol == SW::FrameProtocol::TCP
  }

  pub open spec fn isARP(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.frameProtocol == SW::FrameProtocol::ARP
  }

  pub open spec fn isPortWhitelisted(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.portIsWhitelisted
  }

  pub open spec fn getARP_Type(v: SW::StructuredEthernetMessage_i) -> SW::ARP_Type
  {
    v.arpType
  }

  pub open spec fn isARP_Request(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.arpType == SW::ARP_Type::REQUEST
  }

  pub open spec fn isARP_Reply(v: SW::StructuredEthernetMessage_i) -> bool
  {
    v.arpType == SW::ARP_Type::REPLY
  }
  // END MARKER GUMBO METHODS
}
