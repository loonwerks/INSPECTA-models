#![allow(dead_code)]
#![allow(non_snake_case)]

// Do not edit this file as it will be overwritten if codegen is rerun

use types::*;

extern "C" {
  pub fn EthernetFramesTxIn_is_empty() -> bool;
  pub fn get_EthernetFramesTxIn_poll(num_dropped: *mut sb_event_counter_t, data: *mut base_SW_StructuredEthernetMessage_i);
  pub fn get_EthernetFramesTxIn(data: *mut base_SW_StructuredEthernetMessage_i) -> bool;
  pub fn put_EthernetFramesRxOut(data: *mut base_SW_StructuredEthernetMessage_i) -> bool;
  pub fn put_EthernetFramesTxOut(data: *mut base_SW_StructuredEthernetMessage_i) -> bool;
  pub fn EthernetFramesRxIn_is_empty() -> bool;
  pub fn get_EthernetFramesRxIn_poll(num_dropped: *mut sb_event_counter_t, data: *mut base_SW_StructuredEthernetMessage_i);
  pub fn get_EthernetFramesRxIn(data: *mut base_SW_StructuredEthernetMessage_i) -> bool;
}

pub fn unsafe_EthernetFramesTxIn_is_empty() -> bool {
  return unsafe { EthernetFramesTxIn_is_empty() };
}

pub fn unsafe_get_EthernetFramesTxIn_poll(num_dropped: *mut sb_event_counter_t, data: *mut base_SW_StructuredEthernetMessage_i) {
  return unsafe { get_EthernetFramesTxIn_poll(num_dropped, data) };
}

pub fn unsafe_get_EthernetFramesTxIn(data: *mut base_SW_StructuredEthernetMessage_i) -> bool {
  return unsafe { get_EthernetFramesTxIn(data) };
}

pub fn unsafe_put_EthernetFramesRxOut(data: *mut base_SW_StructuredEthernetMessage_i) -> bool {
  return unsafe { put_EthernetFramesRxOut(data) };
}

pub fn unsafe_put_EthernetFramesTxOut(data: *mut base_SW_StructuredEthernetMessage_i) -> bool {
  return unsafe { put_EthernetFramesTxOut(data) };
}

pub fn unsafe_EthernetFramesRxIn_is_empty() -> bool {
  return unsafe { EthernetFramesRxIn_is_empty() };
}

pub fn unsafe_get_EthernetFramesRxIn_poll(num_dropped: *mut sb_event_counter_t, data: *mut base_SW_StructuredEthernetMessage_i) {
  return unsafe { get_EthernetFramesRxIn_poll(num_dropped, data) };
}

pub fn unsafe_get_EthernetFramesRxIn(data: *mut base_SW_StructuredEthernetMessage_i) -> bool {
  return unsafe { get_EthernetFramesRxIn(data) };
}
