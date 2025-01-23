#![no_std]
pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
#[repr(C)]
pub struct BaseSwSizedEthernetMessageImpl {
    pub message: BaseSwRawEthernetMessageImpl,
    pub size: u16,
}
