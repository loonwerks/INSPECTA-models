#![no_std]

use core::panic::PanicInfo;
use log::{debug, error, info, trace};

mod config;
mod net;
// mod status;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    #[repr(C)]
    pub struct BaseSwSizedEthernetMessageImpl {
        pub message: BaseSwRawEthernetMessageImpl,
        pub size: u16,
    }
    extern "C" {
        pub fn get_EthernetFramesRxIn(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }
}

use bindings::{
    get_EthernetFramesRxIn, get_EthernetFramesTxIn, put_EthernetFramesRxOut,
    put_EthernetFramesTxOut, BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

use net::{
    Address, Arp, ArpOp, EtherType, EthernetRepr, IpProtocol, Ipv4Address, Ipv4Repr, TcpRepr,
};

// use status::TransmitStatus;

fn filter(tcp: &TcpRepr) -> bool {
    !config::firewall::tcp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == tcp.dst_port)
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    config::log::LOGGER.set().unwrap();
    info!("Init");
}

fn firewall_rx(frame: &mut BaseSwRawEthernetMessageImpl) {
    // RX path
    loop {
        let new_data = unsafe { get_EthernetFramesRxIn(frame) };
        if !new_data {
            break;
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

                    match arp.op {
                        ArpOp::Request => {
                            if arp.dest_protocol_addr.0 == config::net::IPV4_ADDR {
                                debug!("ARP requested MY info");
                                let arp_reply = Arp {
                                    htype: arp.htype,
                                    ptype: arp.ptype,
                                    hsize: arp.hsize,
                                    psize: arp.psize,
                                    op: ArpOp::Reply,
                                    src_addr: Address(config::net::MAC_ADDR),
                                    src_protocol_addr: Ipv4Address(config::net::IPV4_ADDR),
                                    dest_addr: arp.src_addr,
                                    dest_protocol_addr: arp.src_protocol_addr,
                                };
                                let header_reply = EthernetRepr {
                                    src_addr: Address(config::net::MAC_ADDR),
                                    dst_addr: header.src_addr,
                                    ethertype: EtherType::Arp,
                                };
                                // Not enough stack space for a new packet
                                // let mut reply_packet: BaseSwRawEthernetMessageImpl =
                                //     [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
                                header_reply.emit(&mut frame[0..EthernetRepr::SIZE]);
                                arp_reply.emit(
                                    &mut frame[EthernetRepr::SIZE..EthernetRepr::SIZE + Arp::SIZE],
                                );

                                let mut out = BaseSwSizedEthernetMessageImpl {
                                    size: 64,
                                    message: *frame,
                                };
                                unsafe { put_EthernetFramesTxOut(&mut out) };
                            }
                        }
                        ArpOp::Reply => {
                            unsafe { put_EthernetFramesRxOut(frame) };
                        }
                        ArpOp::Unknown(_) => (),
                    }
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

                            if filter(&tcp) {
                                info!("TCP packet filtered out");
                            } else {
                                info!("Good TCP Packet. Send it along");
                                unsafe { put_EthernetFramesRxOut(frame) };
                            }
                        }
                        // Throw away any packet that isn't TCP
                        _ => info!("Not a TCP packet. ({:?}) Throw it away.", ip.protocol),
                    }
                }
                // Throw away any packet that isn't IPv4 or Arp
                _ => info!("Not an IPv4 or Arp packet. Throw it away."),
            }
        }
    }
}

fn firewall_tx(frame: &mut BaseSwRawEthernetMessageImpl) {
    loop {
        let new_data = unsafe { get_EthernetFramesTxIn(frame) };
        if !new_data {
            break;
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
            unsafe { put_EthernetFramesTxOut(&mut out as *mut BaseSwSizedEthernetMessageImpl) };
        }
    }
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    firewall_rx(&mut frame);
    firewall_tx(&mut frame);
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
