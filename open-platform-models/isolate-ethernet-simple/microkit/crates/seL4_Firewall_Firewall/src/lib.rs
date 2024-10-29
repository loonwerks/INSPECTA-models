#![no_std]

use core::panic::PanicInfo;
use log::{debug, error, info, trace};

mod config;
mod net;
mod status;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    #[repr(C)]
    pub struct BaseSwSizedEthernetMessageImpl {
        pub message: BaseSwRawEthernetMessageImpl,
        pub size: u16,
    }
    extern "C" {
        pub fn getEthernetFramesRxIn(value: *mut u8);
        pub fn getEthernetFramesTxIn(value: *mut u8);
        pub fn putEthernetFramesRxOut(value: *mut u8);
        pub fn putEthernetFramesTxOut(value: *mut BaseSwSizedEthernetMessageImpl);
    }
}
use bindings::{
    getEthernetFramesRxIn, getEthernetFramesTxIn, putEthernetFramesRxOut, putEthernetFramesTxOut,
    BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

use net::{
    Address, Arp, ArpOp, EtherType, EthernetRepr, IpProtocol, Ipv4Address, Ipv4Repr, TcpRepr,
};

use status::TransmitStatus;

fn filter(tcp: &TcpRepr) -> bool {
    let allowed_ports = [5760u16];
    !allowed_ports.iter().any(|x| *x == tcp.dst_port)
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    config::log::LOGGER.set().unwrap();
    info!("Init");
}

fn firewall_rx(frame: &mut BaseSwRawEthernetMessageImpl) -> TransmitStatus {
    // RX path
    let mut status = TransmitStatus::new();
    unsafe { getEthernetFramesRxIn(frame.as_mut_ptr()) };

    let Some(header) = EthernetRepr::parse(frame) else {
        return status;
    };

    if !header.is_empty() {
        debug!("Dest Mac: {:?}", header.dst_addr);
        debug!("Source Mac: {:?}", header.src_addr);
        debug!("EtherType: {:?}", header.ethertype);
        match header.ethertype {
            EtherType::Arp => {
                let Some(arp) = Arp::parse(&frame[EthernetRepr::SIZE..]) else {
                    info!("Malformed ARP Packet. Throw it away.");
                    return status;
                };

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
                    arp_reply.emit(&mut frame[EthernetRepr::SIZE..EthernetRepr::SIZE + Arp::SIZE]);

                    let mut out = BaseSwSizedEthernetMessageImpl {
                        size: 64,
                        message: *frame,
                    };
                    unsafe {
                        putEthernetFramesTxOut(&mut out as *mut BaseSwSizedEthernetMessageImpl)
                    };
                    // unsafe { putEthernetFramesTxOut(frame.as_mut_ptr()) };
                    // unsafe { putTxFrameSize(64) };
                    status.tx = true;
                }
            }
            EtherType::Ipv4 => {
                trace!("PACKET:\n {:?}", &frame[0..64]);
                let Some(ip) = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]) else {
                    info!("Malformed IPv4 Packet. Throw it away.");
                    return status;
                };
                // TODO: Check that IPv4 Packet is not malformed

                match ip.protocol {
                    IpProtocol::Tcp => {
                        let tcp = TcpRepr::parse(&frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..]);
                        // TODO: Check that IPv4 Packet is not malformed

                        if filter(&tcp) {
                            info!("TCP packet filtered out");
                        } else {
                            info!("Good TCP Packet. Send it along");
                            unsafe { putEthernetFramesRxOut(frame.as_mut_ptr()) };
                            status.rx = true;
                        }
                    }
                    // Throw away any packet that isn't IPv4
                    _ => {
                        info!("Not a TCP packet. Throw it away.")
                    }
                }
            }
            // Throw away any packet that isn't IPv4 or Arp
            _ => {
                info!("Not an IPv4 or Arp packet. Throw it away.")
            }
        }
    }
    status
}

fn firewall_tx(frame: &mut BaseSwRawEthernetMessageImpl) -> TransmitStatus {
    let mut status = TransmitStatus::new();
    unsafe { getEthernetFramesTxIn(frame.as_mut_ptr()) };
    let Some(header) = EthernetRepr::parse(frame) else {
        return status;
    };

    if !header.is_empty() {
        // TODO: Need to do any filtering this direction?

        let size = match header.ethertype {
            EtherType::Arp => 64,
            EtherType::Ipv4 => {
                let Some(ip) = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]) else {
                    info!("Malformed IPv4 Packet. Throw it away.");
                    return status;
                };
                ip.length + EthernetRepr::SIZE as u16
            }
            _ => EthernetRepr::SIZE as u16,
        };
        let mut out = BaseSwSizedEthernetMessageImpl {
            size,
            message: *frame,
        };
        unsafe { putEthernetFramesTxOut(&mut out as *mut BaseSwSizedEthernetMessageImpl) };
        status.tx = true;
    }
    status
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_timeTriggered() {
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    let mut status = firewall_rx(&mut frame);
    status |= firewall_tx(&mut frame);

    if status.any_untransmitted() {
        frame.fill(0);
        if !status.rx {
            unsafe { putEthernetFramesRxOut(frame.as_mut_ptr()) };
        }
        if !status.tx {
            let mut out = BaseSwSizedEthernetMessageImpl {
                size: 0,
                message: frame,
            };
            unsafe { putEthernetFramesTxOut(&mut out as *mut BaseSwSizedEthernetMessageImpl) };
        }
    }
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
