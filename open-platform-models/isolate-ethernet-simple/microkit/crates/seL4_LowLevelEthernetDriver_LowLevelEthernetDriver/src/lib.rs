#![no_std]

use core::cell::OnceCell;
use core::panic::PanicInfo;
use log::{debug, error, info, trace};
use sel4_driver_interfaces::HandleInterrupt;
use sel4_microkit_base::memory_region_symbol;
use smoltcp::{
    phy::{Device, RxToken, TxToken},
    time::Instant,
};

use eth_driver_core::{DmaDef, Driver};

mod config;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    #[repr(C)]
    pub struct BaseSwSizedEthernetMessageImpl {
        pub message: BaseSwRawEthernetMessageImpl,
        pub size: u16,
    }
    extern "C" {
        pub fn put_EthernetFramesRx(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTx(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }
}
use bindings::{
    get_EthernetFramesTx, put_EthernetFramesRx, BaseSwRawEthernetMessageImpl,
    BaseSwSizedEthernetMessageImpl, BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

static mut DEVICE: OnceCell<Driver> = OnceCell::new();

#[no_mangle]
pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize() {
    config::log::LOGGER.set().unwrap();
    info!("Init");
    let mut dev = {
        let dma = DmaDef {
            vaddr: memory_region_symbol!(net_driver_dma_vaddr: *mut ()),
            paddr: memory_region_symbol!(net_driver_dma_paddr: *mut ()),
            size: config::sizes::DRIVER_DMA,
        };
        Driver::new(
            memory_region_symbol!(gem_register_block: *mut ()).as_ptr(),
            dma,
        )
    };
    dev.handle_interrupt();
    info!("Acked driver IRQ");

    unsafe {
        let _ = DEVICE.set(dev);
    };
}

#[no_mangle]
pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
    info!("timeTriggered");
    let drv = unsafe { DEVICE.get_mut().unwrap() };

    let tmp: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    while let Some((rx_tok, _tx_tok)) = drv.receive(Instant::ZERO) {
        rx_tok.consume(|rx_buf| {
            debug!("RX Packet: {:?}", &rx_buf[0..64]);
            unsafe {
                put_EthernetFramesRx(rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl)
            };
        });
    }

    let mut sz_pkt = BaseSwSizedEthernetMessageImpl {
        size: 0,
        message: tmp,
    };

    let new_data = unsafe { get_EthernetFramesTx(&mut sz_pkt) };
    if new_data {
        let size = sz_pkt.size as usize;
        if size > 0 {
            debug!("TX Packet: {:?}", &sz_pkt.message[0..size]);
            if let Some(tx_tok) = drv.transmit(Instant::ZERO) {
                trace!("Valid tx token");
                tx_tok.consume(size, |tx_buf| {
                    tx_buf.copy_from_slice(&sz_pkt.message[0..size]);
                    trace!("Copied from tmp to tx_buf");
                });
            };
        }
    }

    drv.handle_interrupt();
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
