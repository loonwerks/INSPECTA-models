#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api::*;
use data::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{debug, error, info, trace, warn};

use sel4_driver_interfaces::HandleInterrupt;
use sel4_microkit_base::memory_region_symbol;
use smoltcp::{
    phy::{Device, RxToken, TxToken},
    time::Instant,
};

use eth_driver_core::{DmaDef, Driver};

// mod config;

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

pub struct seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
    drv: Driver,
    rx_idx: usize,
}

impl seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
    pub const fn new() -> Self {
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

        Self {
            drv: dev,
            rx_idx: 0,
        }
    }

    fn put_rx<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api>(
        &mut self,
        rx_buf: &mut [u8],
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
        match self.rx_idx {
            0 => api.put_EthernetFramesRx0(value),
            1 => api.put_EthernetFramesRx1(value),
            2 => api.put_EthernetFramesRx2(value),
            3 => api.put_EthernetFramesRx3(value),
            _ => (),
        }
        self.rx_idx = (self.rx_idx + 1) % NUM_MSGS;
    }

    pub fn initialize<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api>(
        &mut self,
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("initialize entrypoint invoked");
        self.drv.handle_interrupt();
        info!("Acked driver IRQ");
        // TODO: Do something with the api?
    }

    pub fn timeTriggered<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Full_Api>(
        &mut self,
        api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("compute entrypoint invoked");
        let tmp: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

        while let Some((rx_tok, _tx_tok)) = self.drv.receive(Instant::ZERO) {
            rx_tok.consume(|rx_buf| {
                debug!("RX Packet: {:?}", &rx_buf[0..64]);
                self.put_rx(rx_buf, api);
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
