// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_MavlinkFirewall_MavlinkFirewall_api::*;
use data::{
    SW::{SW_EthIpUdpHeaders_DIM_0, SW_RawEthernetMessage_DIM_0, SW_UdpPayload_DIM_0},
    *,
};

use vstd::prelude::*;
use vstd::slice::slice_subrange;

verus! {

pub struct seL4_MavlinkFirewall_MavlinkFirewall {}

  fn raw_eth_from_udp_frame(value: SW::UdpFrame_Impl) -> (r: SW::RawEthernetMessage)
        ensures
            mav_input_eq_output(value, r),
            // value.headers@ =~= r@.subrange(0, SW_EthIpUdpHeaders_DIM_0 as int),
            // value.payload@ =~= r@.subrange(SW_EthIpUdpHeaders_DIM_0 as int, SW_RawEthernetMessage_DIM_0 as int),
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

fn ex_two_bytes_to_u16(
  byte0: u8,
  byte1: u8) -> (r: u16)
  ensures
    r == two_bytes_to_u16(byte0, byte1)
{
  (((byte1) as u16) * 256u16 + ((byte0) as u16)) as u16
}

fn ex_three_bytes_to_u32(
  byte0: u8,
  byte1: u8,
  byte2: u8,
) -> (r: u32)
  ensures
    r == three_bytes_to_u32(byte0, byte1, byte2)
{
  ((((byte2) as u32) * 65536u32 + ((byte1) as u32) * 256u32 + ((byte0) as u32))) as u32
}


fn can_send(udp_frame: SW::UdpFrame_Impl) -> (r: bool)
  ensures
    msg_is_blacklisted(udp_frame.payload) == (r == false)
{
  let msg = &udp_frame.payload;
  match msg[0] {
    // Mavlink v2
    0xFD => match ex_three_bytes_to_u32(msg[7], msg[8], msg[9]) {
        75 => ex_two_bytes_to_u16(msg[37], msg[38]) != 42650,
        76 => ex_two_bytes_to_u16(msg[38], msg[39]) != 42650,
        _ => true,
    },
    // Mavlink v1
    0xFE => match msg[5] {
        75 => ex_two_bytes_to_u16(msg[33], msg[34]) != 42650,
        76 => ex_two_bytes_to_u16(msg[34], msg[35]) != 42650,
        _ => true,
    }
    _ => true,
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
        api.In0.is_some() && msg_is_mav_cmd_flash_bootloader(api.In0.unwrap().payload) ==>
          api.Out0.is_none(),
        // guarantee hlr_21_mav0_no_input
        !(api.In0.is_some()) ==> api.Out0.is_none(),
        // guarantee hlr_22_mav0_allow
        api.In0.is_some() && !(msg_is_blacklisted(api.In0.unwrap().payload)) ==>
          api.Out0.is_some() && mav_input_eq_output(api.In0.unwrap(), api.Out0.unwrap()),
        // guarantee hlr_19_mav1_drop_mav_cmd_flash_bootloader
        api.In1.is_some() && msg_is_mav_cmd_flash_bootloader(api.In1.unwrap().payload) ==>
          api.Out1.is_none(),
        // guarantee hlr_21_mav1_no_input
        !(api.In1.is_some()) ==> api.Out1.is_none(),
        // guarantee hlr_22_mav1_allow
        api.In1.is_some() && !(msg_is_blacklisted(api.In1.unwrap().payload)) ==>
          api.Out1.is_some() && mav_input_eq_output(api.In1.unwrap(), api.Out1.unwrap()),
        // guarantee hlr_19_mav2_drop_mav_cmd_flash_bootloader
        api.In2.is_some() && msg_is_mav_cmd_flash_bootloader(api.In2.unwrap().payload) ==>
          api.Out2.is_none(),
        // guarantee hlr_21_mav2_no_input
        !(api.In2.is_some()) ==> api.Out2.is_none(),
        // guarantee hlr_22_mav2_allow
        api.In2.is_some() && !(msg_is_blacklisted(api.In2.unwrap().payload)) ==>
          api.Out2.is_some() && mav_input_eq_output(api.In2.unwrap(), api.Out2.unwrap()),
        // guarantee hlr_19_mav3_drop_mav_cmd_flash_bootloader
        api.In3.is_some() && msg_is_mav_cmd_flash_bootloader(api.In3.unwrap().payload) ==>
          api.Out3.is_none(),
        // guarantee hlr_21_mav3_no_input
        !(api.In3.is_some()) ==> api.Out3.is_none(),
        // guarantee hlr_22_mav3_allow
        api.In3.is_some() && !(msg_is_blacklisted(api.In3.unwrap().payload)) ==>
          api.Out3.is_some() && mav_input_eq_output(api.In3.unwrap(), api.Out3.unwrap()),
        // END MARKER TIME TRIGGERED ENSURES
    {
        log_trace("compute entrypoint invoked");
        if let Some(udp_frame) = api.get_In0() {
            if can_send(udp_frame) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out0(output);
            } else {
              log_info("Dropped blacklisted mavlink message");
            }
        }

        if let Some(udp_frame) = api.get_In1() {
            if can_send(udp_frame) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out1(output);
            } else {
              log_info("Dropped blacklisted mavlink message");
            }
        }

        if let Some(udp_frame) = api.get_In2() {
            if can_send(udp_frame) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out2(output);
            } else {
              log_info("Dropped blacklisted mavlink message");
            }
        }

        if let Some(udp_frame) = api.get_In3() {
            if can_send(udp_frame) {
                let output = raw_eth_from_udp_frame(udp_frame);
                api.put_Out3(output);
            } else {
              log_info("Dropped blacklisted mavlink message");
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
  pub open spec fn three_bytes_to_u32(
    byte0: u8,
    byte1: u8,
    byte2: u8) -> u32
  {
    (((byte2) as u32) * 65536u32 + ((byte1) as u32) * 256u32 + ((byte0) as u32)) as u32
  }

  pub open spec fn two_bytes_to_u16(
    byte0: u8,
    byte1: u8) -> u16
  {
    (((byte1) as u16) * 256u16 + ((byte0) as u16)) as u16
  }

  pub open spec fn msg_v1_is_command_int(msg: SW::UdpPayload) -> bool
  {
    msg[5] == 75u8
  }

  pub open spec fn command_int_msg_v1_is_bootloader_flash(msg: SW::UdpPayload) -> bool
  {
    two_bytes_to_u16(msg[33], msg[34]) == 42650u16
  }

  pub open spec fn msg_v1_is_command_long(msg: SW::UdpPayload) -> bool
  {
    msg[5] == 76u8
  }

  pub open spec fn command_long_msg_v1_is_bootloader_flash(msg: SW::UdpPayload) -> bool
  {
    two_bytes_to_u16(msg[34], msg[35]) == 42650u16
  }

  pub open spec fn msg_is_mavlinkv1(msg: SW::UdpPayload) -> bool
  {
    msg[0] == 0xFEu8
  }

  pub open spec fn msg_v2_is_command_int(msg: SW::UdpPayload) -> bool
  {
    three_bytes_to_u32(msg[7], msg[8], msg[9]) == 75u32
  }

  pub open spec fn command_int_msg_v2_is_bootloader_flash(msg: SW::UdpPayload) -> bool
  {
    two_bytes_to_u16(msg[37], msg[38]) == 42650u16
  }

  pub open spec fn msg_v2_is_command_long(msg: SW::UdpPayload) -> bool
  {
    three_bytes_to_u32(msg[7], msg[8], msg[9]) == 76u32
  }

  pub open spec fn command_long_msg_v2_is_bootloader_flash(msg: SW::UdpPayload) -> bool
  {
    two_bytes_to_u16(msg[38], msg[39]) == 42650u16
  }

  pub open spec fn msg_is_mavlinkv2(msg: SW::UdpPayload) -> bool
  {
    msg[0] == 0xFDu8
  }

  pub open spec fn msg_is_mav_v2_cmd_flash_bootloader(msg: SW::UdpPayload) -> bool
  {
    msg_is_mavlinkv2(msg) &&
      (msg_v2_is_command_int(msg) && command_int_msg_v2_is_bootloader_flash(msg) ||
        msg_v2_is_command_long(msg) && command_long_msg_v2_is_bootloader_flash(msg))
  }

  pub open spec fn msg_is_mav_v1_cmd_flash_bootloader(msg: SW::UdpPayload) -> bool
  {
    msg_is_mavlinkv1(msg) &&
      (msg_v1_is_command_int(msg) && command_int_msg_v1_is_bootloader_flash(msg) ||
        msg_v1_is_command_long(msg) && command_long_msg_v1_is_bootloader_flash(msg))
  }

  pub open spec fn msg_is_mav_cmd_flash_bootloader(msg: SW::UdpPayload) -> bool
  {
    msg_is_mav_v2_cmd_flash_bootloader(msg) || msg_is_mav_v1_cmd_flash_bootloader(msg)
  }

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
