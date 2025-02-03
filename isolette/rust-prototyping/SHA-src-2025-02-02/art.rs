use vstd::prelude::*;
use vstd::invariant::*;

use std::any::Any;

verus! {

// ****
pub struct BridgeId(u32);

impl BridgeId {

    #[verifier::type_invariant]
    spec fn type_inv(self) -> bool {
        0 <= self.0 && self.0 <= 10
    }

    pub fn new(value: u32) -> Self
        requires 0 <= value && value <= 10
    {
        
        Self(value)
    }
}

// ****
pub struct PortId(u32);

impl PortId {

    #[verifier::type_invariant]
    spec fn type_inv(self) -> bool {
        0 <= self.0 && self.0 <= 48
    }

    pub fn new(value: u32) -> Self
        requires 0 <= value && value <= 48
    {
        
        Self(value)
    }
}

// ****
pub struct ConnectionId(u32);

impl ConnectionId {

    #[verifier::type_invariant]
    spec fn type_inv(self) -> bool {
        0 <= self.0 && self.0 <= 26
    }

    pub fn new(value: u32) -> Self
        requires 0 <= value && value <= 26
    {
        
        Self(value)
    }
}

pub trait DataContent: Any {}

mod native {
    use crate::art::{PortId, DataContent};
    pub fn putValue(portId: PortId, data: Box<dyn DataContent>) {
        todo!()
    }
    pub fn getValue(portId: PortId) -> Option<Box<dyn DataContent>> {
        todo!()
    }
    pub fn getAnyValue(portId: PortId) -> Option<Box<dyn std::any::Any>> {
        todo!()
    }
}

pub fn putValue(portId: PortId, data: Box<dyn DataContent>) {
    native::putValue(portId, data)
}

pub fn getValue(portId: PortId) -> Option<Box<dyn DataContent>> {
    return native::getValue(portId)
}

pub fn getAnyValue(portId: PortId) -> Option<Box<dyn Any>> {
    return native::getAnyValue(portId)
}

pub fn logError(bridgeId: BridgeId, s: &str) {
    todo!()
}

}