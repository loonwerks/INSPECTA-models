#![no_std]

use core::cell::OnceCell;
use core::panic::PanicInfo;
use log::{debug, error, info, trace, warn};
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
        pub fn put_EthernetFramesRx0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRx1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRx2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRx3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTx0(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTx1(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTx2(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTx3(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }
}
use bindings::{
    get_EthernetFramesTx0, get_EthernetFramesTx1, get_EthernetFramesTx2, get_EthernetFramesTx3,
    put_EthernetFramesRx0, put_EthernetFramesRx1, put_EthernetFramesRx2, put_EthernetFramesRx3,
    BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    drv: Driver,
    rx_idx: u8,
}

fn put_EthernetFramesRx(idx: &mut u8, rx_buf: &mut [u8]) {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    // let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    match idx {
        0 => unsafe { put_EthernetFramesRx0(value) },
        1 => unsafe { put_EthernetFramesRx1(value) },
        2 => unsafe { put_EthernetFramesRx2(value) },
        3 => unsafe { put_EthernetFramesRx3(value) },
        _ => false,
    };
    *idx = (*idx + 1) % 4;
}

fn get_EthernetFramesTx(idx: u8, tx_buf: &mut BaseSwSizedEthernetMessageImpl) -> bool {
    let value = tx_buf;
    match idx {
        0 => unsafe { get_EthernetFramesTx0(value) },
        1 => unsafe { get_EthernetFramesTx1(value) },
        2 => unsafe { get_EthernetFramesTx2(value) },
        3 => unsafe { get_EthernetFramesTx3(value) },
        _ => false,
    }
}

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

    let state = State {
        drv: dev,
        rx_idx: 0,
    };

    unsafe {
        let _ = STATE.set(state);
    };
}

#[no_mangle]
pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
    info!("timeTriggered");
    let state = unsafe { STATE.get_mut().unwrap() };
    // let drv = unsafe { DEVICE.get_mut().unwrap() };

    let tmp: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    while let Some((rx_tok, _tx_tok)) = state.drv.receive(Instant::ZERO) {
        rx_tok.consume(|rx_buf| {
            debug!("RX Packet: {:?}", &rx_buf[0..64]);
            put_EthernetFramesRx(&mut state.rx_idx, rx_buf);
        });
    }

    let mut sz_pkt = BaseSwSizedEthernetMessageImpl {
        size: 0,
        message: tmp,
    };

    for i in 0u8..4u8 {
        if get_EthernetFramesTx(i, &mut sz_pkt) {
            let size = sz_pkt.size as usize;
            if size > 0 {
                // warn!("TX Packet: {:0>2X?}", &sz_pkt.message[0..size]);
                debug!("TX Packet");
                if let Some(tx_tok) = state.drv.transmit(Instant::ZERO) {
                    trace!("Valid tx token");
                    tx_tok.consume(size, |tx_buf| {
                        tx_buf.copy_from_slice(&sz_pkt.message[0..size]);
                        trace!("Copied from tmp to tx_buf");
                    });
                };
            }
        }
    }

    state.drv.handle_interrupt();
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
