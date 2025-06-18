#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::seL4_Firewall_Firewall_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
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
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
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
          old(api).EthernetFramesRxIn.is_some() ^ old(api).EthernetFramesTxIn.is_some()
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
          ((api.EthernetFramesRxIn.unwrap().internetProtocol == SW::InternetProtocol::IPV4) &&
            (api.EthernetFramesRxIn.unwrap().frameProtocol == SW::FrameProtocol::ARP))) ==>
          api.EthernetFramesTxOut.is_some() &&
            (api.EthernetFramesTxOut.unwrap().arpType == SW::ARP_Type::REPLY),
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
            (api.EthernetFramesTxIn.unwrap() == api.EthernetFramesTxOut.unwrap())
        // END MARKER TIME TRIGGERED ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("compute entrypoint invoked");
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel) 
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          #[cfg(feature = "sel4")]
          warn!("Unexpected channel {}", channel)
        }
      }
    }
  }

}
