#![no_std]

// use core::cell::OnceCell;
use core::panic::PanicInfo;
use log::{error, info};
// use sel4::debug_println;
// use sel4_driver_interfaces::HandleInterrupt;
// use sel4_microkit_base::memory_region_symbol;
// use smoltcp::{
//     phy::{self, Device, RxToken, TxToken},
//     time::Instant,
// };

mod config;

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

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    config::log::LOGGER.set().unwrap();
    info!("Init");
}

fn print_frame(pk: &BaseSwRawEthernetMessageImpl) {
    info!("PACKET:\n {:?}", &pk[..64]);
}

#[derive(Debug)]
pub struct Address(pub [u8; 6]);

impl Address {
    pub fn from_bytes(data: &[u8]) -> Address {
        let mut bytes = [0; 6];
        bytes.copy_from_slice(data);
        Address(bytes)
    }
}

#[derive(Debug)]
#[repr(u16)]
pub enum EtherType {
    Ipv4 = 0x0800,
    Arp = 0x0806,
    Ipv6 = 0x86DD,
    Unknown(u16),
}

impl From<u16> for EtherType {
    fn from(value: u16) -> Self {
        match value {
            0x0800 => EtherType::Ipv4,
            0x0806 => EtherType::Arp,
            0x86DD => EtherType::Ipv6,
            other => EtherType::Unknown(other),
        }
    }
}

#[derive(Debug)]
pub struct EthernetRepr {
    pub src_addr: Address,
    pub dst_addr: Address,
    pub ethertype: EtherType,
}

impl EthernetRepr {
    /// Parse an Ethernet II frame and return a high-level representation.
    pub fn parse(frame: &BaseSwRawEthernetMessageImpl) -> EthernetRepr {
        let dst_addr = Address::from_bytes(&frame[0..6]);
        let src_addr = Address::from_bytes(&frame[6..12]);
        let mut data = [0u8; 2];
        data.copy_from_slice(&frame[12..14]);
        let raw = u16::from_be_bytes(data);
        let ethertype = EtherType::from(raw);
        EthernetRepr {
            src_addr,
            dst_addr,
            ethertype,
        }
    }

    pub fn is_empty(&self) -> bool {
        self.src_addr.0.iter().filter(|x| **x != 0).count() == 0
    }
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_timeTriggered() {
    // info!("timeTriggered");

    let mut rx: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    unsafe { getEthernetFramesRxIn(rx.as_mut_ptr()) };

    let header = EthernetRepr::parse(&rx);
    if !header.is_empty() {
        info!("Dest Mac: {:?}", header.dst_addr);
        info!("Source Mac: {:?}", header.src_addr);
        info!("EtherType: {:?}", header.ethertype);
        info!("PACKET:\n {:?}", &rx[14..64]);
    }

    // TODO: Parse frameheader
    // Check if there is a mac address
    // if rx[0..6].iter().filter(|x| **x != 0).count() > 0 {
    //     print_frame(&rx);
    // }
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
