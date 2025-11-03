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
        // assume isSorted_StructArray
        old(api).myStructArray.is_some() ==> forall|i:int| 0 <= i < old(api).myStructArray.unwrap().fieldArray.len() - 1 ==> old(api).myStructArray.unwrap().fieldArray[i].fieldSInt32 <= old(api).myStructArray.unwrap().fieldArray[i + 1].fieldSInt32,
        // assume atLeastOneZero_StructArray
        old(api).myStructArray.is_some() ==> exists|i:int| 0 <= i < old(api).myStructArray.unwrap().fieldArray.len() - 1 && old(api).myStructArray.unwrap().fieldArray[i].fieldSInt32 == 0,
        // assume isSorted_ArrayInt32
        old(api).MyArrayInt32.is_some() ==> forall|i:int| 0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 ==> old(api).MyArrayInt32.unwrap()[i] <= old(api).MyArrayInt32.unwrap()[i + 1],
        // assume atLeastOneZero_ArrayInt32
        old(api).MyArrayInt32.is_some() ==> exists|i:int| 0 <= i < old(api).MyArrayInt32.unwrap().len() - 1 && old(api).MyArrayInt32.unwrap()[i] == 0,
        // assume isSorted_ArrayStruct
        old(api).MyArrayStruct.is_some() ==> forall|i:int| 0 <= i < old(api).MyArrayStruct.unwrap().len() ==> old(api).MyArrayStruct.unwrap()[i].fieldSInt32 <= old(api).MyArrayStruct.unwrap()[i + 1].fieldSInt32,
        // assume atLeastOneZero_ArrayStruct
        old(api).MyArrayStruct.is_some() ==> exists|i:int| 0 <= i < old(api).MyArrayStruct.unwrap().len() && old(api).MyArrayStruct.unwrap()[i].fieldSInt32 == 0,
        // END MARKER TIME TRIGGERED REQUIRES

    {
      log_info("compute entrypoint invoked");
      assert(api.MyArrayInt32.unwrap()[0] <= api.MyArrayInt32.unwrap()[1]);
      assert(api.MyArrayInt32.unwrap()[0] <= api.MyArrayInt32.unwrap()[0 + 1 as int]);
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

}
