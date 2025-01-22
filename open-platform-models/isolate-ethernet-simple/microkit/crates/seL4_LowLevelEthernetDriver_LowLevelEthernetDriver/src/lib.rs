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

const NUM_MSGS: usize = 4;

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
        pub fn EthernetFramesTx0_is_empty() -> bool;
        pub fn EthernetFramesTx1_is_empty() -> bool;
        pub fn EthernetFramesTx2_is_empty() -> bool;
        pub fn EthernetFramesTx3_is_empty() -> bool;
    }
    pub struct EthFrameGetter {
        pub get: unsafe extern "C" fn(*mut BaseSwSizedEthernetMessageImpl) -> bool,
        pub empty: unsafe extern "C" fn() -> bool,
    }
    pub struct EthFrameRawPutter {
        pub put: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
    }
}
use bindings::*;

pub const FRAMES_RX_FUNCS: [EthFrameRawPutter; NUM_MSGS] = [
    EthFrameRawPutter {
        put: put_EthernetFramesRx0,
    },
    EthFrameRawPutter {
        put: put_EthernetFramesRx1,
    },
    EthFrameRawPutter {
        put: put_EthernetFramesRx2,
    },
    EthFrameRawPutter {
        put: put_EthernetFramesRx3,
    },
];
pub const FRAMES_TX_FUNCS: [EthFrameGetter; NUM_MSGS] = [
    EthFrameGetter {
        get: get_EthernetFramesTx0,
        empty: EthernetFramesTx0_is_empty,
    },
    EthFrameGetter {
        get: get_EthernetFramesTx1,
        empty: EthernetFramesTx1_is_empty,
    },
    EthFrameGetter {
        get: get_EthernetFramesTx2,
        empty: EthernetFramesTx2_is_empty,
    },
    EthFrameGetter {
        get: get_EthernetFramesTx3,
        empty: EthernetFramesTx3_is_empty,
    },
];

static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    drv: Driver,
    rx_idx: usize,
}

fn put_rx(idx: &mut usize, rx_buf: &mut [u8]) {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    let channel = &FRAMES_RX_FUNCS[*idx];
    unsafe {
        (channel.put)(value);
    };
    *idx = (*idx + 1) % NUM_MSGS;
}

fn get_tx(idx: usize, tx_buf: &mut BaseSwSizedEthernetMessageImpl) -> bool {
    let value = tx_buf;
    let channel = &FRAMES_TX_FUNCS[idx];
    unsafe {
        if !(channel.empty)() {
            (channel.get)(value);
            true
        } else {
            false
        }
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

    let tmp: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    while let Some((rx_tok, _tx_tok)) = state.drv.receive(Instant::ZERO) {
        rx_tok.consume(|rx_buf| {
            debug!("RX Packet: {:?}", &rx_buf[0..64]);
            put_rx(&mut state.rx_idx, rx_buf);
        });
    }

    let mut sz_pkt = BaseSwSizedEthernetMessageImpl {
        size: 0,
        message: tmp,
    };

    for i in 0..NUM_MSGS {
        if get_tx(i, &mut sz_pkt) {
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
