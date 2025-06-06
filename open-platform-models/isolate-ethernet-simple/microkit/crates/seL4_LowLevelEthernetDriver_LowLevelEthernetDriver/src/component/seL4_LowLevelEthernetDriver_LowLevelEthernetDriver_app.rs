#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api::*;
use data::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{debug, error, info, trace, warn};

use crate::microkit_channel;
use crate::SW;

use sel4_driver_interfaces::HandleInterrupt;
use sel4_microkit_base::memory_region_symbol;
use smoltcp::{
    phy::{Device, RxToken, TxToken},
    time::Instant,
};

use eth_driver_core::{DmaDef, Driver};

mod config;

const NUM_MSGS: usize = 4;

fn get_tx<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Get_Api>(
    idx: usize,
    api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
) -> Option<SW::SizedEthernetMessage_Impl> {
    match idx {
        0 => api.get_EthernetFramesTx0(),
        1 => api.get_EthernetFramesTx1(),
        2 => api.get_EthernetFramesTx2(),
        3 => api.get_EthernetFramesTx3(),
        _ => None,
    }
}

fn put_rx<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api>(
    idx: &mut usize,
    rx_buf: &[u8],
    api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
) {
    let value: SW::RawEthernetMessage = rx_buf[0..SW::SW_RawEthernetMessage_DIM_0]
        .try_into()
        .unwrap();
    match idx {
        0 => api.put_EthernetFramesRx0(value),
        1 => api.put_EthernetFramesRx1(value),
        2 => api.put_EthernetFramesRx2(value),
        3 => api.put_EthernetFramesRx3(value),
        _ => (),
    }
    *idx = (*idx + 1) % NUM_MSGS;
}

pub struct seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
    drv: Driver,
    rx_idx: usize,
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
            )
        };
        Self {
            drv: dev,
            rx_idx: 0,
        }
    }

    pub fn initialize<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api>(
        &mut self,
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("initialize entrypoint invoked");
        self.drv.handle_interrupt();
        info!("Acked driver IRQ");
    }

    pub fn timeTriggered<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Full_Api>(
        &mut self,
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        trace!("compute entrypoint invoked");
        let tmp: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

        while let Some((rx_tok, _tx_tok)) = self.drv.receive(Instant::ZERO) {
            rx_tok.consume(|rx_buf| {
                debug!("RX Packet: {:?}", &rx_buf[0..64]);
                put_rx(&mut self.rx_idx, rx_buf, api);
            });
        }

        for i in 0..NUM_MSGS {
            if let Some(sz_pkt) = get_tx(i, api) {
                let size = sz_pkt.size as usize;
                if size > 0 {
                    // warn!("TX Packet: {:0>2X?}", &sz_pkt.message[0..size]);
                    debug!("TX Packet");
                    if let Some(tx_tok) = self.drv.transmit(Instant::ZERO) {
                        trace!("Valid tx token");
                        tx_tok.consume(size, |tx_buf| {
                            tx_buf.copy_from_slice(&sz_pkt.message[0..size]);
                            trace!("Copied from tmp to tx_buf");
                        });
                    };
                }
            }
        }

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
