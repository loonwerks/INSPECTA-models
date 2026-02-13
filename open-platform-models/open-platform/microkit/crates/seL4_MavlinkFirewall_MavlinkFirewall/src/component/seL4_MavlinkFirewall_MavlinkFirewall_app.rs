// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_MavlinkFirewall_MavlinkFirewall_api::*;
use data::{
    SW::{SW_EthIpUdpHeaders_DIM_0, SW_RawEthernetMessage_DIM_0, SW_UdpPayload_DIM_0},
    *,
};

use mavlink_parser_vest::{
    parse_mavlink_msg, MavCmd, MavlinkMsg, MavlinkMsgMsg, MessageIdsV1, MessageIdsV2,
};
use mavlink_parser_vest::{spec_mavlink_msg, SpecMavlinkMsg, SpecMavlinkMsgMsg};
use vest_lib::properties::SpecCombinator;
use vest_lib::regular::uints::FromToBytes;
use vstd::prelude::*;
use vstd::slice::slice_subrange;

// Need an allocator for the vest lib
use one_shot_mutex::sync::RawOneShotMutex;
use sel4_dlmalloc::{StaticDlmalloc, StaticHeap};

extern crate alloc;

const HEAP_SIZE: usize = 16 * 1024;

static HEAP: StaticHeap<HEAP_SIZE> = StaticHeap::new();

#[global_allocator]
static GLOBAL_ALLOCATOR: StaticDlmalloc<RawOneShotMutex> = StaticDlmalloc::new(HEAP.bounds());
// Allocator END

verus! {

pub struct seL4_MavlinkFirewall_MavlinkFirewall {}

    // GUMBOX Uninterpreted functions
    pub fn msg_is_wellformed__developer_gumbox(payload: SW::UdpPayload) -> bool
    {
        parse_mavlink_msg(&payload).is_ok()
    }

    pub fn msg_is_mav_cmd_flash_bootloader__developer_gumbox(payload: SW::UdpPayload) -> bool
    {
        can_send(payload)
    }

    // Specification
    // Uninterpreted functions
    pub open spec fn msg_is_wellformed__developer_verus(payload: SW::UdpPayload) -> bool
    {
        spec_mavlink_msg().spec_parse(payload@).is_some()
    }

    pub open spec fn msg_is_mav_cmd_flash_bootloader__developer_verus(payload: SW::UdpPayload) -> bool
    {
        match spec_mavlink_msg().spec_parse(payload@) {
            Some((_, msg)) => spec_msg_is_flash_bootloader(msg),
            None => false,
        }
    }

    // Spec Helpers
    pub open spec fn spec_msg_is_flash_bootloader(msg: SpecMavlinkMsg) -> bool
    {
        spec_msg_v1_is_flash_bootloader(msg) || spec_msg_v2_is_flash_bootloader(msg)
    }

    pub open spec fn spec_msg_v1_is_flash_bootloader(msg: SpecMavlinkMsg) -> bool
    {
        msg.msg matches SpecMavlinkMsgMsg::MavLink1(mv1) &&
            (mv1.msgid == MessageIdsV1::SPEC_CommandInt || mv1.msgid == MessageIdsV1::SPEC_CommandLong) &&
            (spec_payload_get_cmd(mv1.payload) matches Some(cmd) && cmd == MavCmd::SPEC_FlashBootloader)
    }

    pub open spec fn spec_msg_v2_is_flash_bootloader(msg: SpecMavlinkMsg) -> bool
    {
        msg.msg matches SpecMavlinkMsgMsg::MavLink2(mv2) &&
            (mv2.msgid.spec_as_u32() == MessageIdsV2::SPEC_CommandInt || mv2.msgid.spec_as_u32() == MessageIdsV2::SPEC_CommandLong) &&
            (spec_payload_get_cmd(mv2.payload) matches Some(cmd) && cmd == MavCmd::SPEC_FlashBootloader)
    }

    pub open spec fn spec_payload_get_cmd(payload: Seq<u8>) -> Option<u16>
    {
        if payload.len() >= 30 {
            Some(u16::spec_from_le_bytes(payload.subrange(28,30)))
        } else {
            None
        }
    }

    // Exec Code
    fn raw_eth_from_udp_frame(value: SW::UdpFrame_Impl) -> (r: SW::RawEthernetMessage)
        ensures
            mav_input_eq_output(value, r),
     {
        let mut frame = [0u8; SW_RawEthernetMessage_DIM_0];

        let mut i = 0;
        while i < SW_RawEthernetMessage_DIM_0
            invariant
                0 <= i <= SW_RawEthernetMessage_DIM_0,
                forall |j: int| 0 <= j < i ==> {
                  if j < SW_EthIpUdpHeaders_DIM_0@ {
                    value.headers[j] == #[trigger] frame[j]
                  } else {
                    value.payload[j-SW_EthIpUdpHeaders_DIM_0@] == frame[j]
                  }
                },
            decreases
                SW_RawEthernetMessage_DIM_0 - i,

        {
          if i < SW_EthIpUdpHeaders_DIM_0 {
            frame.set(i, value.headers[i]);
          } else {
            frame.set(i, value.payload[i - SW_EthIpUdpHeaders_DIM_0]);
          }
            i += 1;
        }
        frame
    }

    fn can_send(payload: SW::UdpPayload) -> (r: bool)
        ensures
            (msg_is_wellformed(payload) && !msg_is_blacklisted(payload)) == (r == true)
    {
        match parse_mavlink_msg(&payload) {
            Ok((_, msg)) => !ex_msg_is_blacklisted(&msg),
            Err(_) => {
                log_info("Throw away malformed mavlink");
                false
            }
        }
    }

    fn ex_msg_is_blacklisted(msg: &MavlinkMsg) -> (r: bool)
        ensures
            r == spec_msg_is_flash_bootloader(msg@),
    {
        let res = msg_is_flash_bootloader(msg);
        if res {
            log_info("Throw away flash bootloader command");
        }
        res
    }

    fn msg_is_flash_bootloader(msg: &MavlinkMsg) -> (r: bool)
        ensures
             r == spec_msg_is_flash_bootloader(msg@)
    {
        let command = match &msg.msg {
            MavlinkMsgMsg::MavLink1(v1_msg) =>
                match v1_msg.msgid {
                    MessageIdsV1::CommandInt | MessageIdsV1::CommandLong =>
                    payload_get_cmd(v1_msg.payload),
                    _ => None,
                }
            MavlinkMsgMsg::MavLink2(v2_msg) => {
                let msgid = v2_msg.msgid.as_u32();
                match msgid {
                MessageIdsV2::CommandInt | MessageIdsV2::CommandLong =>
                    payload_get_cmd(v2_msg.payload),
                    _ => None
                }
            },
        };

        match command {
            Some(cmd) => cmd == MavCmd::FlashBootloader,
            None => false,
        }

    }

    /// Gets the command for a CommandInt or CommandLong payload
    ///
    /// Workaround for current vest deficiency
    fn payload_get_cmd(payload: &[u8]) -> (o: Option<u16>)
        ensures
            o == spec_payload_get_cmd(payload@),
    {
        if payload.len() >= 30 {
            Some(u16::ex_from_le_bytes(slice_subrange(payload, 28,30)))
        } else {
            None
        }
    }

impl seL4_MavlinkFirewall_MavlinkFirewall {
    pub fn new() -> Self {
        Self {}
    }

    pub fn initialize<API: seL4_MavlinkFirewall_MavlinkFirewall_Put_Api>(
        &mut self,
        api: &mut seL4_MavlinkFirewall_MavlinkFirewall_Application_Api<API>,
    ) {
        log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_MavlinkFirewall_MavlinkFirewall_Full_Api> (
      &mut self,
      api: &mut seL4_MavlinkFirewall_MavlinkFirewall_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).Out0.is_none(),
        old(api).Out1.is_none(),
        old(api).Out2.is_none(),
        old(api).Out3.is_none(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee hlr_19_mav0_drop_mav_cmd_flash_bootloader
        api.In0.is_some() && msg_is_wellformed(api.In0.unwrap().payload) &&
          msg_is_mav_cmd_flash_bootloader(api.In0.unwrap().payload) ==>
          api.Out0.is_none(),
        // guarantee hlr_20_mav0_drop_malformed_msg
        api.In0.is_some() && !(msg_is_wellformed(api.In0.unwrap().payload)) ==>
          api.Out0.is_none(),
        // guarantee hlr_21_mav0_no_input
        !(api.In0.is_some()) ==> api.Out0.is_none(),
        // guarantee hlr_22_mav0_allow
        api.In0.is_some() && msg_is_wellformed(api.In0.unwrap().payload) &&
          !(msg_is_blacklisted(api.In0.unwrap().payload)) ==>
          api.Out0.is_some() && mav_input_eq_output(api.In0.unwrap(), api.Out0.unwrap()),
        // guarantee hlr_19_mav1_drop_mav_cmd_flash_bootloader
        api.In1.is_some() && msg_is_wellformed(api.In1.unwrap().payload) &&
          msg_is_mav_cmd_flash_bootloader(api.In1.unwrap().payload) ==>
          api.Out1.is_none(),
        // guarantee hlr_20_mav1_drop_malformed_msg
        api.In1.is_some() && !(msg_is_wellformed(api.In1.unwrap().payload)) ==>
          api.Out1.is_none(),
        // guarantee hlr_21_mav1_no_input
        !(api.In1.is_some()) ==> api.Out1.is_none(),
        // guarantee hlr_22_mav1_allow
        api.In1.is_some() && msg_is_wellformed(api.In1.unwrap().payload) &&
          !(msg_is_blacklisted(api.In1.unwrap().payload)) ==>
          api.Out1.is_some() && mav_input_eq_output(api.In1.unwrap(), api.Out1.unwrap()),
        // guarantee hlr_19_mav2_drop_mav_cmd_flash_bootloader
        api.In2.is_some() && msg_is_wellformed(api.In2.unwrap().payload) &&
          msg_is_mav_cmd_flash_bootloader(api.In2.unwrap().payload) ==>
          api.Out2.is_none(),
        // guarantee hlr_20_mav2_drop_malformed_msg
        api.In2.is_some() && !(msg_is_wellformed(api.In2.unwrap().payload)) ==>
          api.Out2.is_none(),
        // guarantee hlr_21_mav2_no_input
        !(api.In2.is_some()) ==> api.Out2.is_none(),
        // guarantee hlr_22_mav2_allow
        api.In2.is_some() && msg_is_wellformed(api.In2.unwrap().payload) &&
          !(msg_is_blacklisted(api.In2.unwrap().payload)) ==>
          api.Out2.is_some() && mav_input_eq_output(api.In2.unwrap(), api.Out2.unwrap()),
        // guarantee hlr_19_mav3_drop_mav_cmd_flash_bootloader
        api.In3.is_some() && msg_is_wellformed(api.In3.unwrap().payload) &&
          msg_is_mav_cmd_flash_bootloader(api.In3.unwrap().payload) ==>
          api.Out3.is_none(),
        // guarantee hlr_20_mav3_drop_malformed_msg
        api.In3.is_some() && !(msg_is_wellformed(api.In3.unwrap().payload)) ==>
          api.Out3.is_none(),
        // guarantee hlr_21_mav3_no_input
        !(api.In3.is_some()) ==> api.Out3.is_none(),
        // guarantee hlr_22_mav3_allow
        api.In3.is_some() && msg_is_wellformed(api.In3.unwrap().payload) &&
          !(msg_is_blacklisted(api.In3.unwrap().payload)) ==>
          api.Out3.is_some() && mav_input_eq_output(api.In3.unwrap(), api.Out3.unwrap()),
        // END MARKER TIME TRIGGERED ENSURES
    {
        log_trace("compute entrypoint invoked");
        if let Some(udp_frame) = api.get_In0() {
            if can_send(udp_frame.payload) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out0(output);
            }
        }

        if let Some(udp_frame) = api.get_In1() {
            if can_send(udp_frame.payload) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out1(output);
            }
        }

        if let Some(udp_frame) = api.get_In2() {
            if can_send(udp_frame.payload) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out2(output);
            }
        }

        if let Some(udp_frame) = api.get_In3() {
            if can_send(udp_frame.payload) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out3(output);
            }
        }
    }

    pub fn notify(&mut self, channel: microkit_channel) {
        // this method is called when the monitor does not handle the passed in channel
        match channel {
            _ => log_warn_channel(channel),
        }
    }
}

// BEGIN MARKER GUMBO METHODS
  pub open spec fn mav_input_headers_eq_output(
    headers: SW::EthIpUdpHeaders,
    frame: SW::RawEthernetMessage) -> bool
  {
    forall|i:int| 0 <= i < headers.len() ==> #[trigger] headers[i] == frame[i]
  }

  pub open spec fn mav_input_payload_eq_output(
    payload: SW::UdpPayload,
    headers: SW::EthIpUdpHeaders,
    frame: SW::RawEthernetMessage) -> bool
  {
    forall|i:int| 0 <= i < payload.len() ==> #[trigger] frame[i + headers.len()] == payload[i]
  }

  pub open spec fn mav_input_eq_output(
    input: SW::UdpFrame_Impl,
    frame: SW::RawEthernetMessage) -> bool
  {
    mav_input_headers_eq_output(input.headers, frame) && mav_input_payload_eq_output(input.payload, input.headers, frame)
  }

  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  ///
  ///   pub open spec fn msg_is_wellformed__developer_verus(msg: SW::UdpPayload) -> (res: bool) { ... }
  ///
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn msg_is_wellformed(msg: SW::UdpPayload) -> bool
  {
    msg_is_wellformed__developer_verus(msg)
  }

  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  ///
  ///   pub open spec fn msg_is_mav_cmd_flash_bootloader__developer_verus(msg: SW::UdpPayload) -> (res: bool) { ... }
  ///
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn msg_is_mav_cmd_flash_bootloader(msg: SW::UdpPayload) -> bool
  {
    msg_is_mav_cmd_flash_bootloader__developer_verus(msg)
  }

  pub open spec fn msg_is_blacklisted(msg: SW::UdpPayload) -> bool
  {
    msg_is_mav_cmd_flash_bootloader(msg)
  }
  // END MARKER GUMBO METHODS

#[verifier::external_body]
pub fn log_info(msg: &str) {
    log::info!("{0}", msg);
}

#[verifier::external_body]
pub fn log_trace(msg: &str) {
    log::trace!("{0}", msg);
}

#[verifier::external_body]
pub fn log_warn_channel(channel: u32) {
    log::warn!("Unexpected channel: {0}", channel);
}
}
