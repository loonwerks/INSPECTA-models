// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::producer_p_p_producer_api::*;
use vstd::prelude::*;
use vest_ex::*;
use vest_lib::properties::SecureSpecCombinator;

extern crate alloc;
use alloc::vec::Vec;

// Vest's serialization API requires Vec (heap allocation). On the seL4 microkit
// target there is no standard library, so we provide a global allocator backed
// by a fixed-size static heap using sel4-dlmalloc. This is gated behind
// cfg(not(test)) because test builds link against std which supplies its own
// system allocator -- overriding it with a small static heap would cause
// out-of-memory failures in the test harness.
//
// HEAP_SIZE must be at least 64KB. The underlying dlmalloc allocator rounds its
// first memory request up to DEFAULT_GRANULARITY (64 * 1024). A heap smaller
// than that will fail on the first allocation even if the actual request is only
// a few bytes.
#[cfg(not(test))]
mod heap_allocator {
    use one_shot_mutex::sync::RawOneShotMutex;
    use sel4_dlmalloc::{StaticDlmalloc, StaticHeap};

    const HEAP_SIZE: usize = 64 * 1024;
    static HEAP: StaticHeap<HEAP_SIZE> = StaticHeap::new();

    #[global_allocator]
    static GLOBAL_ALLOCATOR: StaticDlmalloc<RawOneShotMutex> = StaticDlmalloc::new(HEAP.bounds());
}

verus! {

  pub struct producer_p_p_producer {
    pub a: u8,
    pub c: u64,
    pub dispatch_count: u8,
    // BEGIN MARKER STATE VARS
    pub dummy: vest_1prod_1cons::ArrayOfByte,
    // END MARKER STATE VARS
  }

  impl producer_p_p_producer {
    pub fn new() -> Self
    {
      Self {
        a: 0,
        c: 0xFFFF_FFFF_FFFF_FFFFu64,
        dispatch_count: 0,
        // BEGIN MARKER STATE VAR INIT
        dummy: [0; vest_1prod_1cons::vest_1prod_1cons_ArrayOfByte_DIM_0],
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: producer_p_p_producer_Put_Api> (
      &mut self,
      api: &mut producer_p_p_producer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");

      self.dispatch_count = wrapping_add_u8(self.dispatch_count, 1);
      let mut payload = serialize_and_build_payload(self.a, self.c);
      if self.dispatch_count % 3 == 0 {
        payload.set(1, payload[1] ^ 0xFFu8);
      }
      api.put_write_port(payload);
    }

    pub fn timeTriggered<API: producer_p_p_producer_Full_Api> (
      &mut self,
      api: &mut producer_p_p_producer_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      self.a = wrapping_add_u8(self.a, 1);
      self.c = wrapping_sub_u64(self.c, 1);
      self.dispatch_count = wrapping_add_u8(self.dispatch_count, 1);

      let mut payload = serialize_and_build_payload(self.a, self.c);
      if self.dispatch_count % 3 == 0 {
        payload.set(1, payload[1] ^ 0xFFu8);
      }
      api.put_write_port(payload);
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel)
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          log_warn_channel(channel)
        }
      }
    }
  }

  /// Serializes a Triple with the given a and c values into an ArrayOfByte
  /// port payload ready for put_write_port. The payload could be constructed
  /// manually (a is 1 byte, the tag is 2 constant bytes [0x54, 0x33], and c
  /// is 8 bytes little-endian) since what matters is the consumer's use of the
  /// generated Vest parse methods. We use the generated serialize method here
  /// for illustrative purposes.
  #[verifier::external_body]
  pub fn serialize_and_build_payload(a: u8, c: u64) -> (payload: vest_1prod_1cons::ArrayOfByte) {
    let tag = [0x54u8, 0x33u8];
    let trip = Triple { a, b: &tag, c };
    let trip_len = triple_len(&trip);
    let mut bytes = Vec::with_capacity(trip_len);
    bytes.resize(trip_len, 0u8);
    let mut payload: vest_1prod_1cons::ArrayOfByte = [0u8; vest_1prod_1cons::vest_1prod_1cons_ArrayOfByte_DIM_0];
    if let Ok(_) = serialize_triple(&trip, &mut bytes, 0) {
      let copy_len = core::cmp::min(bytes.len(), payload.len());
      payload[..copy_len].copy_from_slice(&bytes[..copy_len]);
    }
    payload
  }

  /// Verus requires integer arithmetic to be proven free of overflow, so
  /// Rust's native wrapping_add is not directly usable inside verified code.
  /// This wrapper is marked external_body to bypass that check and delegate
  /// to the underlying modular (wrapping) arithmetic.
  #[verifier::external_body]
  pub fn wrapping_add_u8(a: u8, b: u8) -> u8 {
    a.wrapping_add(b)
  }

  /// Same rationale as wrapping_add_u8: Verus does not support wrapping
  /// subtraction natively, so we wrap Rust's wrapping_sub behind an
  /// external_body boundary.
  #[verifier::external_body]
  pub fn wrapping_sub_u64(a: u64, b: u64) -> u64 {
    a.wrapping_sub(b)
  }

  #[verifier::external_body]
  pub fn log_info(msg: &str)
  {
    log::info!("{0}", msg);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32)
  {
    log::warn!("Unexpected channel: {0}", channel);
  }

  // PLACEHOLDER MARKER GUMBO METHODS

}
