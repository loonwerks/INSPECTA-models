//
// Copyright 2024, DornerWorks
//
// SPDX-License-Identifier: BSD-2-Clause
//

// pub mod net {
//     pub const MAC_ADDR: [u8; 6] = [0x00, 0x0A, 0x35, 0x03, 0x78, 0xA1];
// }

pub mod tcp {
    pub const ALLOWED_PORTS: [u16; 1] = [5760u16];
}

pub mod udp {
    const NUM_UDP_PORTS: usize = 1;
    pub const ALLOWED_PORTS: [u16; NUM_UDP_PORTS] = [68u16];
}
