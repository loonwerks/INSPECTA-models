// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_MavlinkFirewall_MavlinkFirewall_api::*;
use data::{
    SW::{SW_EthIpUdpHeaders_DIM_0, SW_RawEthernetMessage_DIM_0, SW_UdpPayload_DIM_0},
    *,
};

use vstd::prelude::*;

pub struct seL4_MavlinkFirewall_MavlinkFirewall {}

fn raw_eth_from_udp_frame(value: SW::UdpFrame_Impl) -> SW::RawEthernetMessage {
    let mut frame = [0u8; SW_RawEthernetMessage_DIM_0];

    let mut i = 0;
    while i < SW_EthIpUdpHeaders_DIM_0 {
        frame.set(i, value.headers[i]);
        i += 1;
    }

    i = 0;
    while i < SW_UdpPayload_DIM_0 {
        frame.set(i + SW_EthIpUdpHeaders_DIM_0, value.payload[i]);
        i += 1;
    }
    frame
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

    pub fn timeTriggered<API: seL4_MavlinkFirewall_MavlinkFirewall_Full_Api>(
        &mut self,
        api: &mut seL4_MavlinkFirewall_MavlinkFirewall_Application_Api<API>,
    ) {
        log_trace("compute entrypoint invoked");
        if let Some(udp_frame) = api.get_In0() {
            let output = raw_eth_from_udp_frame(udp_frame);
            api.put_Out0(output);
        }

        if let Some(udp_frame) = api.get_In1() {
            let output = raw_eth_from_udp_frame(udp_frame);
            api.put_Out1(output);
        }

        if let Some(udp_frame) = api.get_In2() {
            let output = raw_eth_from_udp_frame(udp_frame);
            api.put_Out2(output);
        }

        if let Some(udp_frame) = api.get_In3() {
            let output = raw_eth_from_udp_frame(udp_frame);
            api.put_Out3(output);
        }
    }

    pub fn notify(&mut self, channel: microkit_channel) {
        // this method is called when the monitor does not handle the passed in channel
        match channel {
            _ => log_warn_channel(channel),
        }
    }
}

pub fn log_info(msg: &str) {
    log::info!("{0}", msg);
}

pub fn log_trace(msg: &str) {
    log::trace!("{0}", msg);
}

pub fn log_warn_channel(channel: u32) {
    log::warn!("Unexpected channel: {0}", channel);
}
