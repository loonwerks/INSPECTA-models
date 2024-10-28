#![no_std]

use core::cell::OnceCell;
use core::panic::PanicInfo;
use log::{debug, error, info, trace};
// use sel4::debug_println;
use sel4_driver_interfaces::HandleInterrupt;
use sel4_microkit_base::memory_region_symbol;
use smoltcp::{
    phy::{self, Device, RxToken, TxToken},
    time::Instant,
};

use eth_driver_core::{DmaDef, Driver};

mod config;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    extern "C" {
        pub fn putEthernetFramesRx(value: *mut u8);
    }
    extern "C" {
        pub fn getEthernetFramesTx(value: *mut u8);
    }
}
use bindings::{
    getEthernetFramesTx, putEthernetFramesRx, BaseSwRawEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

// static mut VALUE: OnceCell<BaseSwRawEthernetMessageImpl> = OnceCell::new();
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
    // let gem_addr = memory_region_symbol!(gem_register_block: *mut ()).as_ptr();
    // info!("LowLevel_Driver: gem_addr: {gem_addr:p}");
    // let dma_paddr = memory_region_symbol!(net_driver_dma_paddr: *mut ()).as_ptr();
    // info!("LowLevel_Driver: dma_paddr: {dma_paddr:p}");
    // let dma_vaddr = memory_region_symbol!(net_driver_dma_vaddr: *mut ()).as_ptr();
    // info!("LowLevel_Driver: dma_vaddr: {dma_vaddr:p}");
    // let mut val = [0u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    // val[3] = DEFAULT_RX;
    // unsafe {
    //     let _ = VALUE.set(val);
    // };
}

#[no_mangle]
pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
    info!("timeTriggered");
    let drv = unsafe { DEVICE.get_mut().unwrap() };

    let mut tmp: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    // let mut notify_rx = false;

    if let Some((rx_tok, _tx_tok)) = drv.receive(Instant::ZERO) {
        rx_tok.consume(|rx_buf| {
            unsafe { putEthernetFramesRx(rx_buf.as_mut_ptr()) };
        });

        // notify_rx = true;
    } else {
        unsafe { putEthernetFramesRx(tmp.as_mut_ptr()) };
    }

    // if notify_rx {
    //     drv.rx_ring_buffers.notify();
    // }

    // let mut notify_tx = false;

    // if let Some(tx_tok) = drv.transmit(Instant::ZERO) {
    //     tx_tok.consume(BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE, |tx_buf| {
    //         unsafe { getEthernetFramesTx(tx_buf.as_mut_ptr()) };
    //         // tx_buf.copy_from_slice(&tmp);
    //     });
    // };
    unsafe { getEthernetFramesTx(tmp.as_mut_ptr()) };
    if tmp[0..6].iter().filter(|x| **x != 0).count() > 0 {
        debug!("TX Packet: {:?}", &tmp[0..64]);
        if let Some(tx_tok) = drv.transmit(Instant::ZERO) {
            trace!("Valid tx token");
            // TODO: We care about the actual size of the packet here. Should have another data port from the firewall to know the size.
            tx_tok.consume(64, |tx_buf| {
                tx_buf.copy_from_slice(&tmp[0..64]);
                trace!("Copied from tmp to tx_buf");
            });
        };
    }

    // if notify_tx {
    //     drv.tx_ring_buffers.notify();
    // }

    drv.handle_interrupt();
    // drv.device_channel.irq_ack().unwrap();

    // {
    //     let mut tx: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    //     unsafe { getEthernetFramesTx(tx.as_mut_ptr()) };
    //     let (int_bytes, _rest) = tx.split_at(core::mem::size_of::<u32>());
    //     let val = u32::from_be_bytes(int_bytes.try_into().unwrap());
    //     info!("TX Received: {val}");
    // }
    // {
    //     let ptr = unsafe { VALUE.get_mut().unwrap() };
    //     let val = ptr.get_mut(3).unwrap();
    //     let new_val = if *val > 0 { *val - 1 } else { DEFAULT_RX };
    //     *val = new_val;
    //     let ptr = unsafe { VALUE.get_mut().unwrap() };
    //     unsafe { putEthernetFramesRx(ptr.as_mut_ptr()) };
    //     info!("RX Sent {new_val}");
    // }
}

// Need a Panic handler in a no_std environment
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}
