#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api::*;
use data::SW::BufferDesc_Impl;
use data::*;
use hamr_utils::{dequeue, empty_buf_queue, enqueue, QueuePair, QUEUE_SIZE};
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{debug, error, info, trace, warn};

use crate::microkit_channel;
// use crate::SW;

use sel4_driver_interfaces::HandleInterrupt;
use sel4_microkit_base::memory_region_symbol;

use eth_driver_core::{DmaDef, Driver};

mod config;

extern "C" {
    static RxData_queue_1: *const SW::EthernetMessages;
    static TxData_queue_1: *const SW::EthernetMessages;
}

use core::arch::asm;

// This comes from microkit SDK. Can I use without redefining here?
const CONFIG_L1_CACHE_LINE_SIZE_BITS: u64 = 6;
const ROUND: u64 = 1 << CONFIG_L1_CACHE_LINE_SIZE_BITS;

pub fn cache_clean_and_maybe_invalidate(start: u64, end: u64, invalidate: bool) {
    // If the end address is not on a cache line boundary, we want to perform
    // the cache operation on that cache line as well.
    let extra = if end % ROUND == 0 { 0 } else { 1 };

    let end_rounded = ROUND * (end / ROUND + extra);

    let begin = start >> CONFIG_L1_CACHE_LINE_SIZE_BITS;
    let last = end_rounded >> CONFIG_L1_CACHE_LINE_SIZE_BITS;

    for i in begin..last {
        let vaddr = i << CONFIG_L1_CACHE_LINE_SIZE_BITS;
        if invalidate {
            unsafe {
                asm!("dc civac, {r}",
                 r = in(reg) vaddr);
            };
        } else {
            unsafe {
                asm!("dc cvac, {r}",
                 r = in(reg) vaddr);
            };
        }
    }
    unsafe { asm!("dsb sy") };
}

pub struct seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
    rx: QueuePair,
    tx: QueuePair,
    rx_vaddr: u64,
    tx_vaddr: u64,
    drv: Driver,
}

impl seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
    pub fn new() -> Self {
        let dev = {
            let dma = DmaDef {
                vaddr: memory_region_symbol!(net_driver_dma_vaddr: *mut ()),
                paddr: memory_region_symbol!(net_driver_dma_paddr: *mut ()),
                size: config::sizes::DRIVER_DMA,
            };
            Driver::new(
                memory_region_symbol!(gem_register_block: *mut ()).as_ptr(),
                dma,
                memory_region_symbol!(RxData_queue_1_paddr: *mut ()).as_ptr(),
                memory_region_symbol!(TxData_queue_1_paddr: *mut ()).as_ptr(),
            )
        };
        Self {
            rx: QueuePair {
                avail: empty_buf_queue(),
                free: empty_buf_queue(),
            },
            tx: QueuePair {
                avail: empty_buf_queue(),
                free: empty_buf_queue(),
            },
            // rx_vaddr: memory_region_symbol!(RxData_queue_1: *mut ()).addr().get() as u64,
            // tx_vaddr: memory_region_symbol!(TxData_queue_1: *mut ()).addr().get() as u64,
            rx_vaddr: unsafe { RxData_queue_1.addr() as u64 },
            tx_vaddr: unsafe { TxData_queue_1.addr() as u64 },
            drv: dev,
        }
    }

    pub fn tx_free_init(&mut self) {
        for i in 0..QUEUE_SIZE {
            let buffer = SW::BufferDesc_Impl {
                index: i as u16,
                length: SW::SW_RawEthernetMessage_DIM_0 as u16,
            };
            self.tx_free_enqueue(buffer);
        }
    }

    pub fn tx_avail_dequeue(&mut self) -> Option<SW::BufferDesc_Impl> {
        dequeue(&self.tx.avail, &mut self.tx.free)
    }

    pub fn rx_avail_enqueue(&mut self, buffer: SW::BufferDesc_Impl) -> bool {
        enqueue(&mut self.rx.avail, &self.rx.free, buffer)
    }

    pub fn rx_free_dequeue(&mut self) -> Option<SW::BufferDesc_Impl> {
        dequeue(&self.rx.free, &mut self.rx.avail)
    }

    pub fn tx_free_enqueue(&mut self, buffer: SW::BufferDesc_Impl) -> bool {
        enqueue(&mut self.tx.free, &self.tx.avail, buffer)
    }

    pub fn calculate_buff_start_end(
        &self,
        data_start: u64,
        buffer: &SW::BufferDesc_Impl,
    ) -> (u64, u64) {
        let start =
            data_start + (buffer.index as u64 * SW::SW_RawEthernetMessage_DIM_0 as u64) as u64;
        let end = start + buffer.length as u64;
        (start, end)
    }

    pub fn calculate_tx_buff_start_end(&self, buffer: &SW::BufferDesc_Impl) -> (u64, u64) {
        self.calculate_buff_start_end(self.tx_vaddr, buffer)
    }

    pub fn calculate_rx_buff_start_end(&self, buffer: &SW::BufferDesc_Impl) -> (u64, u64) {
        self.calculate_buff_start_end(self.rx_vaddr, buffer)
    }

    pub fn initialize<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api>(
        &mut self,
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("initialize entrypoint invoked");
        self.tx_free_init();
        api.put_TxQueueFree(self.tx.free);
        self.drv.handle_interrupt();
        info!("Acked driver IRQ");
    }

    pub fn timeTriggered<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Full_Api>(
        &mut self,
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        trace!("compute entrypoint invoked");

        // Update state based on inputs
        if let Some(free) = api.get_RxQueueFree() {
            self.rx.free = free;
        }
        if let Some(avail) = api.get_TxQueueAvail() {
            self.tx.avail = avail;
        }

        let mut wrote_tx_free = false;
        let mut wrote_rx_avail = false;

        loop {
            match self.rx_free_dequeue() {
                Some(buffer) => {
                    self.drv.rx_mark_done(buffer.index.into());
                    wrote_rx_avail = true;
                    // info!("Mark done {}", buffer.index);
                }
                None => break,
            }
        }

        // loop {
        for i in 0..QUEUE_SIZE {
            match self.drv.receive() {
                Some(index) => {
                    let buffer = BufferDesc_Impl {
                        index,
                        length: SW::SW_RawEthernetMessage_DIM_0 as u16,
                    };
                    let (start, end) = self.calculate_rx_buff_start_end(&buffer);
                    cache_clean_and_maybe_invalidate(start, end, true);
                    self.rx_avail_enqueue(buffer);
                    wrote_rx_avail = true;
                    // info!("Use {}", buffer.index);
                }
                None => break,
            }
        }

        loop {
            match self.tx_avail_dequeue() {
                Some(buffer) => {
                    // info!("Transmit buffer {}", buffer.index);
                    let (start, end) = self.calculate_tx_buff_start_end(&buffer);
                    cache_clean_and_maybe_invalidate(start, end, false);
                    self.drv.transmit(buffer.index.into(), buffer.length.into());
                    // Should we do this somewhere else?
                    self.tx_free_enqueue(buffer);
                    wrote_tx_free = true;
                    // api.put_TxQueueFree(self.tx.free);
                }
                None => break,
            }
        }

        if wrote_rx_avail {
            api.put_RxQueueAvail(self.rx.avail);
        }
        if wrote_tx_free {
            api.put_TxQueueFree(self.tx.free);
        }

        // TODO: Do this earlier?
        self.drv.handle_interrupt();
    }

    pub fn notify(&mut self, channel: microkit_channel) {
        // this method is called when the monitor does not handle the passed in channel
        match channel {
            _ => {
                #[cfg(feature = "sel4")]
                warn!("Unexpected channel {}", channel)
            }
        }
    }
}
