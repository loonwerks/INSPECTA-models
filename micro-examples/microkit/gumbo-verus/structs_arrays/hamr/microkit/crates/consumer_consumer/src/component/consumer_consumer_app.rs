// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;
use vstd::prelude::*;

verus! {

  pub struct consumer_consumer {
  }

  impl consumer_consumer {
    pub fn new() -> Self
    {
      Self {
      }
    }

    pub fn initialize<API: consumer_consumer_Put_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      requires
        // assume isSortedInt32
        // original HAMR version -- Verus reports 
        //   "error: Could not automatically infer triggers for this quantifier.  Use #[trigger] annotations to manually mark trigger terms instead."
        //forall|i:int|
        //  0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 ==> old(api).MyArrayInt32.unwrap()[i] <= old(api).MyArrayInt32.unwrap()[i + 1],

        // HAMR could add the trigger to the first use of the quantifier in an array indexing expression as follows. This allows Verus to prove
        // both the assertions in the body hold
        //forall|i:int|
        //  0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 ==> #[trigger] old(api).MyArrayInt32.unwrap()[i] <= old(api).MyArrayInt32.unwrap()[i + 1],

        // However, what if the user wrote the quantified expression as follows. If HAMR places the trigger on the first indexing expression then Verus
        // will report that the first assertion does not hold. If that is commented out then Verus will be able to prove that the second assertion holds,
        // presumably due to "0 + 1" matching "i + 1" where i is 0
        //forall|i:int|
        //  0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 ==> #[trigger] old(api).MyArrayInt32.unwrap()[i + 1] >= old(api).MyArrayInt32.unwrap()[i],  

        // This is just showing that adding the trigger to the 'simpler' pattern allows Verus to prove both assertions hold
        //forall|i:int|
        //  0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 ==> old(api).MyArrayInt32.unwrap()[i + 1] >= #[trigger] old(api).MyArrayInt32.unwrap()[i],            

        // Adding trigger to both indexing expressions results in Verus reporting
        //   "error: variable `i` in trigger cannot appear in both arithmetic and non-arithmetic positions"
        //forall|i:int|
        //  0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 ==> #[trigger] old(api).MyArrayInt32.unwrap()[i] <= #[trigger] old(api).MyArrayInt32.unwrap()[i + 1],
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume atLeastOneZero_ArrayInt32
        old(api).MyArrayInt32.is_some() ==> exists|i:int| 0 <= i < old(api).MyArrayInt32.unwrap().len() && #[trigger] old(api).MyArrayInt32.unwrap()[i] == 0,
        // assume isSorted_ArrayInt32
        old(api).MyArrayInt32.is_some() ==> forall|i:int| 0 <= i <= old(api).MyArrayInt32.unwrap().len() - 2 ==> #[trigger] old(api).MyArrayInt32.unwrap()[i] <= old(api).MyArrayInt32.unwrap()[i + 1],
        // assume atLeastOneZero_StructArray
        old(api).myStructArray.is_some() ==> exists|i:int| 0 <= i < old(api).myStructArray.unwrap().fieldArray.len() && #[trigger] old(api).myStructArray.unwrap().fieldArray[i].fieldSInt32 == 0,
        // assume isSorted_StructArray
        old(api).myStructArray.is_some() ==> forall|i:int| 0 <= i < old(api).myStructArray.unwrap().fieldArray.len() ==> #[trigger] old(api).myStructArray.unwrap().fieldArray[i].fieldSInt32 <= old(api).myStructArray.unwrap().fieldArray[i + 1].fieldSInt32,
        // assume atLeastOneZero_ArrayStruct
        old(api).MyArrayStruct.is_some() ==> exists|i:int| 0 <= i < old(api).MyArrayStruct.unwrap().len() && #[trigger] old(api).MyArrayStruct.unwrap()[i].fieldSInt32 == 0,
        // assume isSorted_ArrayStruct
        //   Demonstrate that the trigger will be attached to the *first indexed use* of the quantified variable 
        //   inside an expression, not merely the first textual occurrence of the quantifier variable.
        old(api).MyArrayStruct.is_some() ==> forall|i:int| -(1) <= i <= old(api).MyArrayStruct.unwrap().len() - 2 ==> if (i >= 0) {
          #[trigger] old(api).MyArrayStruct.unwrap()[i].fieldSInt32 <= old(api).MyArrayStruct.unwrap()[i + 1].fieldSInt32
        } else {
          true
        },
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee conversions
        //   Exercise all base conversions
        convertB(true) && convertS8(1i8) &&
          convertS16(1i16) &&
          convertS32(1i32) &&
          convertS64(1i64) &&
          convertU8(1u8) &&
          convertU16(1u16) &&
          convertU32(1u32) &&
          convertU64(1u64),
        // END MARKER TIME TRIGGERED ENSURES

    {
      log_info("compute entrypoint invoked");

      if let Some(v) = api.get_myStructArray() {
        assert (v.fieldArray[0].fieldSInt32 <= v.fieldArray[1].fieldSInt32);
        assert (v.fieldArray[0].fieldSInt32 <= v.fieldArray[0 + 1 as int].fieldSInt32);
      }

      if let Some(v) = api.get_MyArrayInt32() {
        assert(v[0] <= v[1]);
        assert(v[0] <= v[0 + 1 as int]);
      }

      if let Some(v) = api.get_MyArrayStruct() {
        assert(v[0].fieldSInt32 <= v[1].fieldSInt32);
        assert(v[0].fieldSInt32 <= v[0 + 1 as int].fieldSInt32);
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
  pub open spec fn convertB(v: bool) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertS8(v: i8) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertS16(v: i16) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertS32(v: i32) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertS64(v: i64) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertU8(v: u8) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertU16(v: u16) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertU32(v: u32) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn convertU64(v: u64) -> bool
  {
    (((v) as i8) == 1i8) &&
      (((v) as i16) == 1i16) &&
      (((v) as i32) == 1i32) &&
      (((v) as i64) == 1i64) &&
      (((v) as u8) == 1u8) &&
      (((v) as u16) == 1u16) &&
      (((v) as u32) == 1u32) &&
      (((v) as u64) == 1u64)
  }

  pub open spec fn add(
    a: i32,
    b: i32) -> i32
  {
    (a + b) as i32
  }

  pub open spec fn addMinAndMax(
    a: i32,
    b: i32,
    c: i32) -> i32
  {
    (if (a < b) {
      if (b < c) {
        a + c
      } else {
        a + b
      }
    } else {
      if (a < c) {
        b + c
      } else {
        b + a
      }
    }) as i32
  }
  // END MARKER GUMBO METHODS
}
