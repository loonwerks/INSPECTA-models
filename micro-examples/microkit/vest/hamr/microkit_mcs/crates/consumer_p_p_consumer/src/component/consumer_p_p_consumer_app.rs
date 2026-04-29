// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::consumer_p_p_consumer_api::*;
use vstd::prelude::*;
use vest_ex::*;

extern crate alloc;

// Vest's parse functions require the alloc crate to be linked (Vec<u8> appears
// in the combinator type parameters). On the seL4 microkit target there is no
// standard library, so we provide a global allocator backed by a fixed-size
// static heap using sel4-dlmalloc. This is gated behind cfg(not(test)) because
// test builds link against std which supplies its own system allocator.
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

    // Registers this allocator as the global allocator for the alloc crate.
    // In a no_std environment there is no default allocator, so Rust requires
    // exactly one #[global_allocator] static to service all heap allocations
    // (e.g. Vec, Box, String).
    #[global_allocator]
    static GLOBAL_ALLOCATOR: StaticDlmalloc<RawOneShotMutex> = StaticDlmalloc::new(HEAP.bounds());
}

verus! {

  pub struct consumer_p_p_consumer {
    // BEGIN MARKER STATE VARS
    pub dummy: vest_1prod_1cons::ArrayOfByte,
    // END MARKER STATE VARS
  }

  impl consumer_p_p_consumer {
    pub fn new() -> Self
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        dummy: [0; vest_1prod_1cons::vest_1prod_1cons_ArrayOfByte_DIM_0],
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: consumer_p_p_consumer_Put_Api> (
      &mut self,
      api: &mut consumer_p_p_consumer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: consumer_p_p_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_p_p_consumer_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee g1
        uif(),
        // END MARKER TIME TRIGGERED ENSURES
    {
      let port_value = api.get_read_port();
      match parse_triple(&port_value) {
        Ok((_, triple)) => {
          log_triple(&triple);
        },
        Err(_) => {
          log_info("failed to parse triple from read_port");
        },
      }
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

  /// Logs the parsed Triple fields (a, b tag bytes, and c).
  #[verifier::external_body]
  pub fn log_triple(triple: &Triple) {
    log::info!("received Triple {{ a: {}, b: [{:#04x}, {:#04x}], c: {} }}",
      triple.a, triple.b[0], triple.b[1], triple.c);
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

  // BEGIN MARKER GUMBO METHODS
  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  /// 
  ///   pub open spec fn uif__developer_verus() -> (res: bool) { ... }
  /// 
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn uif() -> bool
  {
    uif__developer_verus()
  }
  // END MARKER GUMBO METHODS

  pub open spec fn uif__developer_verus() -> (res: bool) { 
    true
   }

  pub exec fn uif__developer_gumbox() -> (res: bool) { 
    true
  }
}
