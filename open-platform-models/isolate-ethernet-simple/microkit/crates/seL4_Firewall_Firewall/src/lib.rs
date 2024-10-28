#![no_std]

use core::panic::PanicInfo;
use log::{debug, error, info, trace, warn};

mod config;
mod net;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    extern "C" {
        pub fn getEthernetFramesRxIn(value: *mut u8);
        pub fn getEthernetFramesTxIn(value: *mut u8);
        pub fn putEthernetFramesRxOut(value: *mut u8);
        pub fn putEthernetFramesTxOut(value: *mut u8);
    }
}
use bindings::{
    getEthernetFramesRxIn, getEthernetFramesTxIn, putEthernetFramesRxOut, putEthernetFramesTxOut,
    BaseSwRawEthernetMessageImpl, BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

use net::{
    Address, Arp, ArpOp, EtherType, EthernetRepr, IpProtocol, Ipv4Address, Ipv4Repr, TcpRepr,
};

fn filter(tcp: &TcpRepr) -> bool {
    let allowed_ports = [5760u16];
    !allowed_ports.iter().any(|x| *x == tcp.dst_port)
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    config::log::LOGGER.set().unwrap();
    info!("Init");
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_timeTriggered() {
    let mut tx_transmitted = false;
    let mut rx_transmitted = false;

    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    // RX path
    {
        unsafe { getEthernetFramesRxIn(frame.as_mut_ptr()) };

        let header = EthernetRepr::parse(&frame);
        // TODO: Check for malformed Header

        if !header.is_empty() {
            debug!("Dest Mac: {:?}", header.dst_addr);
            debug!("Source Mac: {:?}", header.src_addr);
            debug!("EtherType: {:?}", header.ethertype);
            match header.ethertype {
                EtherType::Arp => {
                    let arp = Arp::parse(&frame[EthernetRepr::SIZE..]);
                    // TODO: Check for malformed ARP

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
                        arp_reply
                            .emit(&mut frame[EthernetRepr::SIZE..EthernetRepr::SIZE + Arp::SIZE]);

                        unsafe { putEthernetFramesTxOut(frame.as_mut_ptr()) };
                        tx_transmitted = true;
                    }
                }
                EtherType::Ipv4 => {
                    trace!("PACKET:\n {:?}", &frame[0..64]);
                    let ip = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]);
                    // TODO: Check that IPv4 Packet is not malformed

                    match ip.protocol {
                        IpProtocol::Tcp => {
                            info!("TCP Packet, keep parsing");
                            let tcp = TcpRepr::parse(&frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..]);
                            // TODO: Check that IPv4 Packet is not malformed

                            if filter(&tcp) {
                                info!("TCP packet filtered out");
                            } else {
                                unsafe { putEthernetFramesRxOut(frame.as_mut_ptr()) };
                                rx_transmitted = true;
                            }
                        }
                        // Throw away any packet that isn't IPv4
                        _ => {
                            info!("Not a TCP packet, throw away")
                        }
                    }
                }
                // Throw away any packet that isn't IPv4 or Arp
                _ => {
                    info!("Not an IPv4 or Arp packet, throw away")
                }
            }
        }
    }

    // TX Path
    // {
    //     unsafe { getEthernetFramesTxIn(frame.as_mut_ptr()) };
    //     let header = EthernetRepr::parse(&frame);
    //     // TODO: Check for malformed Header

    //     if !header.is_empty() {
    //         // TODO: Need to do any filtering this direction?

    //         unsafe { putEthernetFramesTxOut(frame.as_mut_ptr()) };
    //         tx_transmitted = true;
    //     }
    // }

    if !(rx_transmitted && tx_transmitted) {
        frame.fill(0);
        if !rx_transmitted {
            unsafe { putEthernetFramesRxOut(frame.as_mut_ptr()) };
        }
        if !tx_transmitted {
            unsafe { putEthernetFramesTxOut(frame.as_mut_ptr()) };
        }
    }
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
