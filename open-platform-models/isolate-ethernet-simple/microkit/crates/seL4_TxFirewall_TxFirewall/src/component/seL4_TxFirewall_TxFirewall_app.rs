#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_TxFirewall_TxFirewall_api::*;
use crate::data::*;
// #[cfg(feature = "sel4")]
// #[allow(unused_imports)]
use log::{debug, error, info, trace, warn};

use firewall_core::{Arp, EthFrame, EthernetRepr, PacketType};

mod config;

const NUM_MSGS: usize = 4;

fn eth_get<API: seL4_TxFirewall_TxFirewall_Get_Api>(
    idx: usize,
    api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
) -> Option<SW::RawEthernetMessage_Impl> {
    match idx {
        0 => api.get_EthernetFramesTxIn0(),
        1 => api.get_EthernetFramesTxIn1(),
        2 => api.get_EthernetFramesTxIn2(),
        3 => api.get_EthernetFramesTxIn3(),
        _ => None,
    }
}

fn can_send_frame(frame: &mut [u8]) -> Option<u16> {
    let Some(packet) = EthFrame::parse(frame) else {
        info!("Malformed packet. Throw it away.");
        return None;
    };

    let size = match packet.eth_type {
        PacketType::Arp(_) => {
            let size = 64u16;
            // TODO: Do we need this now that linux is constructing it?
            frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
            size
        }
        PacketType::Ipv4(ip) => ip.header.length + EthernetRepr::SIZE as u16,
        PacketType::Ipv6 => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            return None;
        }
    };

    Some(size)
}

pub struct seL4_TxFirewall_TxFirewall {
    idx: usize,
}

impl seL4_TxFirewall_TxFirewall {
    pub const fn new() -> Self {
        Self { idx: 0 }
    }

    fn idx_increment(&mut self) {
        self.idx = (self.idx + 1) % NUM_MSGS;
    }

    fn eth_put<API: seL4_TxFirewall_TxFirewall_Put_Api>(
        &mut self,
        tx_buf: SW::SizedEthernetMessage_Impl,
        api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
    ) {
        match self.idx {
            0 => api.put_EthernetFramesTxOut0(tx_buf),
            1 => api.put_EthernetFramesTxOut1(tx_buf),
            2 => api.put_EthernetFramesTxOut2(tx_buf),
            3 => api.put_EthernetFramesTxOut3(tx_buf),
            _ => (),
        }
        self.idx_increment();
    }

    fn firewall<API: seL4_TxFirewall_TxFirewall_Full_Api>(
        &mut self,
        api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
    ) {
        for i in 0..NUM_MSGS {
            if let Some(mut frame) = eth_get(i, api) {
                if let Some(size) = can_send_frame(&mut frame) {
                    let out = SW::SizedEthernetMessage_Impl {
                        size,
                        message: frame,
                    };
                    self.eth_put(out, api);
                }
            }
        }
    }

    pub fn initialize<API: seL4_TxFirewall_TxFirewall_Put_Api>(
        &mut self,
        api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_TxFirewall_TxFirewall_Full_Api>(
        &mut self,
        api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        trace!("compute entrypoint invoked");
        self.firewall(api);
    }

    pub fn notify(&mut self, channel: microkit_channel) {
        // this method is called when the monitor does not handle the passed in channel
        {
            #[cfg(feature = "sel4")]
            warn!("Unexpected channel {}", channel)
        }
    }
}

#[test]
fn init_test() {
    let state = seL4_TxFirewall_TxFirewall::new();
    assert_eq!(state.idx, 0);
}

#[test]
fn increment_tests() {
    let mut state = seL4_TxFirewall_TxFirewall::new();
    assert_eq!(state.idx, 0);
    state.idx_increment();
    assert_eq!(state.idx, 1);
    state.idx_increment();
    assert_eq!(state.idx, 2);
    state.idx_increment();
    assert_eq!(state.idx, 3);
    state.idx_increment();
    assert_eq!(state.idx, 0);
    state.idx_increment();
    assert_eq!(state.idx, 1);
}

#[cfg(test)]
mod can_send_tx_frame_tests {
    use super::*;

    #[test]
    fn valid_arp() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 64);
    }

    #[test]
    fn disallowed_ipv6() {
        let mut frame = [0u8; 128];
        // IPv6 Frame
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xdd,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn valid_ipv4() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x20, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 46);

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x33, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x1, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 65);
    }
}

// #[cfg(test)]
// mod tx_ethernet_frames_tests {
//     use super::*;

//     mod get_in {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut tx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let res = eth_get(0, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(2, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(3, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(0, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut tx_buf);
//             assert!(res);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut tx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let _ = eth_get(4, &mut tx_buf);
//         }
//     }

//     mod put_out {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut state = State::new();
//             let message: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let mut tx_buf: SW::SizedEthernetMessage_Impl =
//                 SW::SizedEthernetMessage_Impl { message, size: 20 };

//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 1);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 2);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 3);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 1);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut state = State::new();
//             state.idx = 4;
//             let message: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let mut tx_buf: SW::SizedEthernetMessage_Impl =
//                 SW::SizedEthernetMessage_Impl { message, size: 20 };

//             eth_put(&mut state, &mut tx_buf);
//         }
//     }
// }

// #[cfg(test)]
// mod bindings {
//     use super::*;
//     pub fn get_EthernetFramesTxIn0(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn1(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn2(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn3(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn EthernetFramesTxIn0_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesTxIn1_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesTxIn2_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesTxIn3_is_empty() -> bool {
//         false
//     }
//     pub fn put_EthernetFramesTxOut0(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesTxOut1(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesTxOut2(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesTxOut3(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub struct EthChannelGet {
//         pub get: fn(*mut SW::RawEthernetMessage_Impl) -> bool,
//         pub empty: fn() -> bool,
//     }
//     pub struct EthChannelPut {
//         pub put: fn(*mut SW::SizedEthernetMessage_Impl) -> bool,
//     }
// }
