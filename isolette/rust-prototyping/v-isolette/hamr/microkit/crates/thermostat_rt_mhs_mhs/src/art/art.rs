//! Data declarations

use vstd::prelude::*;

verus! {
// ****
pub struct BridgeId(u32);

impl BridgeId {


    pub fn new(value: u32) -> Self
        
    {
        
        Self(value)
    }
}

impl Default for BridgeId {
    fn default() -> Self {
        Self(0)
    }
}

// ****
pub struct PortId(u32);

impl PortId {


    pub fn new(value: u32) -> Self
        
    {
        
        Self(value)
    }
}

impl Default for PortId {
    fn default() -> Self {
        Self(0)
    }
}

// ****
pub struct ConnectionId(u32);

impl ConnectionId {



    pub fn new(value: u32) -> Self
       
    {
        
        Self(value)
    }
}

impl Default for ConnectionId {
    fn default() -> Self {
        Self(0)
    }
}

pub trait DataContent {}//: Any {}

// mod native {
//     use crate::art::{PortId, DataContent};
//     pub fn putValue(portId: PortId, data: Box<dyn DataContent>) {
//         todo!()
//     }
//     pub fn getValue(portId: PortId) -> Option<Box<dyn DataContent>> {
//         todo!()
//     }
//     pub fn getAnyValue(portId: PortId) -> Option<Box<dyn std::any::Any>> {
//         todo!()
//     }
// }

// pub fn putValue(portId: PortId, data: Box<dyn DataContent>) {
//     native::putValue(portId, data)
// }

// pub fn getValue(portId: PortId) -> Option<Box<dyn DataContent>> {
//     return native::getValue(portId)
// }

// pub fn getAnyValue(portId: PortId) -> Option<Box<dyn Any>> {
//     return native::getAnyValue(portId)
// }

// pub fn logError(bridgeId: BridgeId, s: &str) {
//     todo!()
// }
}