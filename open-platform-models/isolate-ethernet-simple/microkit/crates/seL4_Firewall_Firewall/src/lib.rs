#![no_std]

use core::cell::OnceCell;
use core::panic::PanicInfo;
use log::{debug, error, info, trace};

mod config;
mod net;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    #[repr(C)]
    pub struct BaseSwSizedEthernetMessageImpl {
        pub message: BaseSwRawEthernetMessageImpl,
        pub size: u16,
    }
    extern "C" {
        pub fn get_EthernetFramesRxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut0(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut1(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut2(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut3(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }
}

use bindings::{
    get_EthernetFramesRxIn0, get_EthernetFramesRxIn1, get_EthernetFramesRxIn2,
    get_EthernetFramesRxIn3, get_EthernetFramesTxIn0, get_EthernetFramesTxIn1,
    get_EthernetFramesTxIn2, get_EthernetFramesTxIn3, put_EthernetFramesRxOut0,
    put_EthernetFramesRxOut1, put_EthernetFramesRxOut2, put_EthernetFramesRxOut3,
    put_EthernetFramesTxOut0, put_EthernetFramesTxOut1, put_EthernetFramesTxOut2,
    put_EthernetFramesTxOut3, BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

use net::{
    Address, Arp, ArpOp, EtherType, EthernetRepr, IpProtocol, Ipv4Address, Ipv4Repr, TcpRepr,
    UdpRepr,
};

static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    rx_idx: u8,
    tx_idx: u8,
}

fn filter_udp(udp: &UdpRepr) -> bool {
    !config::firewall::udp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == udp.dst_port)
}

fn filter_tcp(tcp: &TcpRepr) -> bool {
    !config::firewall::tcp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == tcp.dst_port)
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    config::log::LOGGER.set().unwrap();
    info!("Init");
    let state = State {
        rx_idx: 0,
        tx_idx: 0,
    };

    unsafe {
        let _ = STATE.set(state);
    };
}

fn get_EthernetFramesRxIn(idx: u8, rx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    match idx {
        0 => unsafe { get_EthernetFramesRxIn0(value) },
        1 => unsafe { get_EthernetFramesRxIn1(value) },
        2 => unsafe { get_EthernetFramesRxIn2(value) },
        3 => unsafe { get_EthernetFramesRxIn3(value) },
        _ => false,
    }
}

fn put_EthernetFramesRxOut(state: &mut State, rx_buf: &mut BaseSwRawEthernetMessageImpl) {
    let value = rx_buf as *mut BaseSwRawEthernetMessageImpl;
    match state.rx_idx {
        0 => unsafe { put_EthernetFramesRxOut0(value) },
        1 => unsafe { put_EthernetFramesRxOut1(value) },
        2 => unsafe { put_EthernetFramesRxOut2(value) },
        3 => unsafe { put_EthernetFramesRxOut3(value) },
        _ => false,
    };
    state.rx_idx = (state.rx_idx + 1) % 4;
}

fn firewall_rx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    // RX path
    for i in 0u8..4u8 {
        let new_data = get_EthernetFramesRxIn(i, frame);
        if !new_data {
            continue;
        }

        let Some(header) = EthernetRepr::parse(frame) else {
            continue;
        };

        // TODO: Do we still need this? Probably not because queuing tells us whether we have new data. However, is an all zero dest mac address a malformed packet?
        if !header.is_empty() {
            debug!("Dest Mac: {:?}", header.dst_addr);
            debug!("Source Mac: {:?}", header.src_addr);
            debug!("EtherType: {:?}", header.ethertype);
            match header.ethertype {
                EtherType::Arp => {
                    let Some(arp) = Arp::parse(&frame[EthernetRepr::SIZE..]) else {
                        info!("Malformed ARP Packet. Throw it away.");
                        continue;
                    };

                    trace!("Good ARP Packet. Send it along");
                    put_EthernetFramesRxOut(state, frame);

                    //     match arp.op {
                    //         ArpOp::Request => {
                    //             if arp.dest_protocol_addr.0 == config::net::IPV4_ADDR {
                    //                 debug!("ARP requested MY info");
                    //                 let arp_reply = Arp {
                    //                     htype: arp.htype,
                    //                     ptype: arp.ptype,
                    //                     hsize: arp.hsize,
                    //                     psize: arp.psize,
                    //                     op: ArpOp::Reply,
                    //                     src_addr: Address(config::net::MAC_ADDR),
                    //                     src_protocol_addr: Ipv4Address(config::net::IPV4_ADDR),
                    //                     dest_addr: arp.src_addr,
                    //                     dest_protocol_addr: arp.src_protocol_addr,
                    //                 };
                    //                 let header_reply = EthernetRepr {
                    //                     src_addr: Address(config::net::MAC_ADDR),
                    //                     dst_addr: header.src_addr,
                    //                     ethertype: EtherType::Arp,
                    //                 };
                    //                 // Not enough stack space for a new packet
                    //                 // let mut reply_packet: BaseSwRawEthernetMessageImpl =
                    //                 //     [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
                    //                 header_reply.emit(&mut frame[0..EthernetRepr::SIZE]);
                    //                 arp_reply.emit(
                    //                     &mut frame[EthernetRepr::SIZE..EthernetRepr::SIZE + Arp::SIZE],
                    //                 );

                    //                 let mut out = BaseSwSizedEthernetMessageImpl {
                    //                     size: 64,
                    //                     message: *frame,
                    //                 };
                    //                 put_EthernetFramesTxOut(state, &mut out);
                    //             }
                    //         }
                    //         ArpOp::Reply => {
                    //             put_EthernetFramesRxOut(state, frame);
                    //         }
                    //         ArpOp::Unknown(_) => (),
                    //     }
                }
                EtherType::Ipv4 => {
                    trace!("PACKET:\n {:?}", &frame[0..64]);
                    let Some(ip) = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]) else {
                        info!("Malformed IPv4 Packet. Throw it away.");
                        continue;
                    };
                    // TODO: Check that the entire IPv4 Packet is not malformed

                    match ip.protocol {
                        IpProtocol::Tcp => {
                            let tcp = TcpRepr::parse(&frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..]);
                            // TODO: Check that TCP Packet is not malformed

                            if filter_tcp(&tcp) {
                                info!("TCP packet filtered out");
                            } else {
                                trace!("Good TCP Packet. Send it along");
                                put_EthernetFramesRxOut(state, frame);
                            }
                        }
                        IpProtocol::Udp => {
                            let udp = UdpRepr::parse(&frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..]);
                            // TODO: Check that UDP Packet is not malformed

                            if filter_udp(&udp) {
                                info!("UDP packet filtered out");
                            } else {
                                trace!("Good UDP Packet. Send it along");
                                put_EthernetFramesRxOut(state, frame);
                            }
                        }
                        // Throw away any packet that isn't TCP or UDP
                        _ => info!(
                            "Not a TCP or UDP packet. ({:?}) Throw it away.",
                            ip.protocol
                        ),
                    }
                }
                // Throw away any packet that isn't IPv4 or Arp
                _ => info!("Not an IPv4 or Arp packet. Throw it away."),
            }
        }
    }
}

fn get_EthernetFramesTxIn(idx: u8, tx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = tx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    match idx {
        0 => unsafe { get_EthernetFramesTxIn0(value) },
        1 => unsafe { get_EthernetFramesTxIn1(value) },
        2 => unsafe { get_EthernetFramesTxIn2(value) },
        3 => unsafe { get_EthernetFramesTxIn3(value) },
        _ => false,
    }
}

fn put_EthernetFramesTxOut(state: &mut State, tx_buf: &mut BaseSwSizedEthernetMessageImpl) {
    let value = tx_buf as *mut BaseSwSizedEthernetMessageImpl;
    match state.tx_idx {
        0 => unsafe { put_EthernetFramesTxOut0(value) },
        1 => unsafe { put_EthernetFramesTxOut1(value) },
        2 => unsafe { put_EthernetFramesTxOut2(value) },
        3 => unsafe { put_EthernetFramesTxOut3(value) },
        _ => false,
    };
    state.tx_idx = (state.tx_idx + 1) % 4;
}

fn firewall_tx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    for i in 0u8..4u8 {
        let new_data = get_EthernetFramesTxIn(i, frame);
        if !new_data {
            continue;
        }

        let Some(header) = EthernetRepr::parse(frame) else {
            continue;
        };

        if !header.is_empty() {
            // TODO: Need to do any filtering this direction?

            let size = match header.ethertype {
                EtherType::Arp => {
                    let size = 64u16;
                    frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
                    size
                }
                EtherType::Ipv4 => {
                    let Some(ip) = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]) else {
                        info!("Malformed IPv4 Packet. Throw it away.");
                        continue;
                    };
                    ip.length + EthernetRepr::SIZE as u16
                }
                // _ => EthernetRepr::SIZE as u16,
                _ => {
                    info!("Not an IPv4 or Arp packet. Throw it away.");
                    continue;
                }
            };
            let mut out = BaseSwSizedEthernetMessageImpl {
                size,
                message: *frame,
            };
            put_EthernetFramesTxOut(state, &mut out);
        }
    }
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    let state = unsafe { STATE.get_mut().unwrap() };

    firewall_rx(state, &mut frame);
    firewall_tx(state, &mut frame);
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
