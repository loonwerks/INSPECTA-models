// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;
use vstd::prelude::*;

verus! {

  pub struct consumer_consumer {
    // BEGIN MARKER STATE VARS
    pub myArrayInt32_StateVar: Gubmo_Structs_Arrays::MyArrayInt32,
    pub myArrayStruct_StateVar: Gubmo_Structs_Arrays::MyArrayStruct,
    pub myStructArray_StateVar: Gubmo_Structs_Arrays::MyStructArray_i,
    // END MARKER STATE VARS
  }

  impl consumer_consumer {
    pub fn new() -> Self
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        myArrayInt32_StateVar: [0; Gubmo_Structs_Arrays::Gubmo_Structs_Arrays_MyArrayInt32_DIM_0],
        myArrayStruct_StateVar: [Gubmo_Structs_Arrays::MyStruct2_i::default(); Gubmo_Structs_Arrays::Gubmo_Structs_Arrays_MyArrayStruct_DIM_0],
        myStructArray_StateVar: Gubmo_Structs_Arrays::MyStructArray_i::default(),
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: consumer_consumer_Put_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume isSorted_MyArrayInt32_StateVar_Assume
        forall|i:int| 0 <= i <= (old(self).myArrayInt32_StateVar).len() - 2 ==> #[trigger] old(self).myArrayInt32_StateVar[i] <= old(self).myArrayInt32_StateVar[i + 1],
        // assume isSorted_MyArrayStruct_StateVar_Assume
        forall|i:int| 0 <= i <= (old(self).myArrayStruct_StateVar).len() - 2 ==> #[trigger] old(self).myArrayStruct_StateVar[i].fieldSInt32 <= old(self).myArrayStruct_StateVar[i + 1].fieldSInt32,
        // assume isSorted_MyStructArray_StateVar_Assume
        forall|i:int| 0 <= i <= (old(self).myStructArray_StateVar).fieldArray.len() - 2 ==> #[trigger] (old(self).myStructArray_StateVar).fieldArray[i].fieldSInt32 <= (old(self).myStructArray_StateVar).fieldArray[i + 1].fieldSInt32,
        // assume isSorted_MyArrayInt32_Function_Assume
        //   Ensure operations on an array returned by a function work as expected
        forall|i:int| 0 <= i <= myArrayInt32_FunctionReturn(old(self).myArrayInt32_StateVar).len() - 2 ==> #[trigger] myArrayInt32_FunctionReturn(old(self).myArrayInt32_StateVar)[i] <= myArrayInt32_FunctionReturn(old(self).myArrayInt32_StateVar)[i + 1],
        // assume myArrayInt32_FunctionParam_Assume
        //   ensure functions can operate on arrays
        myArrayInt32_FunctionParam(old(self).myArrayInt32_StateVar),
        // assume specFunctionAssumeTest
        subclauseSpecFunction_Assume(old(api).c_myArrayInt32_DataPort) &&
          (GumboLib::normalLibraryFunction_spec(old(api).c_myArrayInt32_DataPort) && GumboLib::librarySpecFunction_Assume_spec(old(api).c_myArrayInt32_DataPort)),
        // assume isSorted_MyArrayStruct_Function_Assume
        //   Ensure operations on an array returned by a function work as expected
        forall|i:int| 0 <= i <= myArrayStruct_FunctionReturn(old(self).myArrayStruct_StateVar).len() - 2 ==> #[trigger] myArrayStruct_FunctionReturn(old(self).myArrayStruct_StateVar)[i].fieldSInt32 <= myArrayStruct_FunctionReturn(old(self).myArrayStruct_StateVar)[i + 1].fieldSInt32,
        // assume myArrayStruct_FunctionParam_Assume
        //   ensure functions can operate on arrays
        myArrayStruct_FunctionParam(old(self).myArrayStruct_StateVar),
        // assume isSorted_MyStructArray_i_Function_Assume
        //   Ensure operations on an array returned by a function work as expected
        forall|i:int| 0 <= i <= myStructArray_i_FunctionReturn(old(self).myStructArray_StateVar).fieldArray.len() - 2 ==> #[trigger] myStructArray_i_FunctionReturn(old(self).myStructArray_StateVar).fieldArray[i].fieldSInt32 <= myStructArray_i_FunctionReturn(old(self).myStructArray_StateVar).fieldArray[i + 1].fieldSInt32,
        // assume myStructArray_i_FunctionParam_Assume
        //   ensure functions can operate on arrays
        myStructArray_i_FunctionParam(old(self).myStructArray_StateVar),
        // assume atLeastOneZero_MyArrayInt32_DataPort_Assume
        exists|i:int| 0 <= i <= old(api).c_myArrayInt32_DataPort.len() - 1 && #[trigger] old(api).c_myArrayInt32_DataPort[i] == 0i32,
        // assume isSorted_MyArrayInt32_DataPort_Assume
        forall|i:int| 0 <= i <= old(api).c_myArrayInt32_DataPort.len() - 2 ==> #[trigger] old(api).c_myArrayInt32_DataPort[i] <= old(api).c_myArrayInt32_DataPort[i + 1],
        // assume isSorted_MyArrayStruct_DataPort_Assume
        forall|i:int| 0 <= i <= old(api).c_myArrayStruct_DataPort.len() - 2 ==> #[trigger] old(api).c_myArrayStruct_DataPort[i].fieldSInt32 <= old(api).c_myArrayStruct_DataPort[i + 1].fieldSInt32,
        // assume isSorted_MyStructArray_DataPort_Assume
        forall|i:int| 0 <= i <= old(api).c_myStructArray_DataPort.fieldArray.len() - 2 ==> #[trigger] old(api).c_myStructArray_DataPort.fieldArray[i].fieldSInt32 <= old(api).c_myStructArray_DataPort.fieldArray[i + 1].fieldSInt32,
        // assume isSorted_MyArrayInt32_EventDataPort_Assume
        old(api).c_myArrayInt32_EventDataPort.is_some() ==> forall|i:int| 0 <= i <= old(api).c_myArrayInt32_EventDataPort.unwrap().len() - 2 ==> #[trigger] old(api).c_myArrayInt32_EventDataPort.unwrap()[i] <= old(api).c_myArrayInt32_EventDataPort.unwrap()[i + 1],
        // assume isSorted_MyArrayStruct_EventDataPort_Assume
        old(api).c_myArrayStruct_EventDataPort.is_some() ==> forall|i:int| 0 <= i <= old(api).c_myArrayStruct_EventDataPort.unwrap().len() - 2 ==> #[trigger] old(api).c_myArrayStruct_EventDataPort.unwrap()[i].fieldSInt32 <= old(api).c_myArrayStruct_EventDataPort.unwrap()[i + 1].fieldSInt32,
        // assume isSorted_MyStructArray_EventDataPort_Assume
        old(api).c_myStructArray_EventDataPort.is_some() ==> forall|i:int| 0 <= i <= old(api).c_myStructArray_EventDataPort.unwrap().fieldArray.len() - 2 ==> #[trigger] old(api).c_myStructArray_EventDataPort.unwrap().fieldArray[i].fieldSInt32 <= old(api).c_myStructArray_EventDataPort.unwrap().fieldArray[i + 1].fieldSInt32,
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee noChange_MyArrayInt32_StateVar_Guarantee
        old(self).myArrayInt32_StateVar[0] == self.myArrayInt32_StateVar[0],
        // guarantee noChange_MyArrayStruct_StateVar_Guarantee
        old(self).myArrayStruct_StateVar[0].fieldSInt32 == self.myArrayStruct_StateVar[0].fieldSInt32,
        // guarantee isSorted_MyArrayInt32_StateVar_Guarantee
        forall|i:int| 0 <= i <= self.myArrayInt32_StateVar.len() - 2 ==> #[trigger] old(self).myArrayInt32_StateVar[i] <= self.myArrayInt32_StateVar[i + 1],
        // guarantee isSorted_MyArrayStruct_StateVar_Guarantee
        forall|i:int| 0 <= i <= self.myArrayStruct_StateVar.len() - 2 ==> #[trigger] old(self).myArrayStruct_StateVar[i].fieldSInt32 <= self.myArrayStruct_StateVar[i + 1].fieldSInt32,
        // guarantee isSorted_MyStructArray_StateVar_Guarantee
        forall|i:int| 0 <= i <= self.myStructArray_StateVar.fieldArray.len() - 2 ==> #[trigger] (old(self).myStructArray_StateVar).fieldArray[i].fieldSInt32 <= self.myStructArray_StateVar.fieldArray[i + 1].fieldSInt32,
        // guarantee isSorted_MyArrayInt32_Function_Guarantee
        //   Ensure operations on an array returned by a function work as expected
        forall|i:int| 0 <= i <= myArrayInt32_FunctionReturn(self.myArrayInt32_StateVar).len() - 2 ==> #[trigger] myArrayInt32_FunctionReturn(old(self).myArrayInt32_StateVar)[i] <= myArrayInt32_FunctionReturn(self.myArrayInt32_StateVar)[i + 1],
        // guarantee myArrayInt32_FunctionParam_Guarantee
        //   ensure functions can operate on arrays
        myArrayInt32_FunctionParam(old(self).myArrayInt32_StateVar),
        // guarantee librarySpecFunctionTest
        subclauseSpecFunction_Assume(api.c_myArrayInt32_DataPort) &&
          (subclauseSpecFunction_Guarantee(api.c_myArrayInt32_DataPort) &&
            (GumboLib::normalLibraryFunction_spec(api.c_myArrayInt32_DataPort) && GumboLib::librarySpecFunction_Guarantee_spec(api.c_myArrayInt32_DataPort))),
        // guarantee isSorted_MyArrayStruct_Function_Guarantee
        //   Ensure operations on an array returned by a function work as expected
        forall|i:int| 0 <= i <= myArrayStruct_FunctionReturn(self.myArrayStruct_StateVar).len() - 2 ==> #[trigger] myArrayStruct_FunctionReturn(old(self).myArrayStruct_StateVar)[i].fieldSInt32 <= myArrayStruct_FunctionReturn(self.myArrayStruct_StateVar)[i + 1].fieldSInt32,
        // guarantee myArrayStruct_FunctionParam_Guarantee
        //   ensure functions can operate on arrays
        myArrayStruct_FunctionParam(old(self).myArrayStruct_StateVar),
        // guarantee isSorted_MyStructArray_i_Function_Guarantee
        //   Ensure operations on an array returned by a function work as expected
        forall|i:int| 0 <= i <= myStructArray_i_FunctionReturn(self.myStructArray_StateVar).fieldArray.len() - 2 ==> #[trigger] myStructArray_i_FunctionReturn(old(self).myStructArray_StateVar).fieldArray[i].fieldSInt32 <= myStructArray_i_FunctionReturn(self.myStructArray_StateVar).fieldArray[i + 1].fieldSInt32,
        // guarantee myStructArray_i_FunctionParam_Guarantee
        //   ensure functions can operate on arrays
        myStructArray_i_FunctionParam(old(self).myStructArray_StateVar),
        // guarantee atLeastOneZero_MyArrayInt32_DataPort_Guarantee
        exists|i:int| 0 <= i <= api.c_myArrayInt32_DataPort.len() - 1 && #[trigger] api.c_myArrayInt32_DataPort[i] == 0i32,
        // guarantee isSorted_MyArrayInt32_DataPort_Guarantee
        forall|i:int| 0 <= i <= api.c_myArrayInt32_DataPort.len() - 2 ==> #[trigger] api.c_myArrayInt32_DataPort[i] <= api.c_myArrayInt32_DataPort[i + 1],
        // guarantee isSorted_MyArrayStruct_DataPort_Guarantee
        forall|i:int| 0 <= i <= api.c_myArrayStruct_DataPort.len() - 2 ==> #[trigger] api.c_myArrayStruct_DataPort[i].fieldSInt32 <= api.c_myArrayStruct_DataPort[i + 1].fieldSInt32,
        // guarantee isSorted_MyStructArray_DataPort_Guarantee
        forall|i:int| 0 <= i <= api.c_myStructArray_DataPort.fieldArray.len() - 2 ==> #[trigger] api.c_myStructArray_DataPort.fieldArray[i].fieldSInt32 <= api.c_myStructArray_DataPort.fieldArray[i + 1].fieldSInt32,
        // guarantee isSorted_MyArrayInt32_EventDataPort_Guarantee
        api.c_myArrayInt32_EventDataPort.is_some() ==> forall|i:int| 0 <= i <= api.c_myArrayInt32_EventDataPort.unwrap().len() - 2 ==> #[trigger] api.c_myArrayInt32_EventDataPort.unwrap()[i] <= api.c_myArrayInt32_EventDataPort.unwrap()[i + 1],
        // guarantee isSorted_MyArrayStruct_EventDataPort_Guarantee
        api.c_myArrayStruct_EventDataPort.is_some() ==> forall|i:int| 0 <= i <= api.c_myArrayStruct_EventDataPort.unwrap().len() - 2 ==> #[trigger] api.c_myArrayStruct_EventDataPort.unwrap()[i].fieldSInt32 <= api.c_myArrayStruct_EventDataPort.unwrap()[i + 1].fieldSInt32,
        // guarantee isSorted_MyStructArray_EventDataPort_Guarantee
        api.c_myStructArray_EventDataPort.is_some() ==> forall|i:int| 0 <= i <= api.c_myStructArray_EventDataPort.unwrap().fieldArray.len() - 2 ==> #[trigger] api.c_myStructArray_EventDataPort.unwrap().fieldArray[i].fieldSInt32 <= api.c_myStructArray_EventDataPort.unwrap().fieldArray[i + 1].fieldSInt32,
        // END MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");
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
  pub open spec fn myArrayInt32_FunctionReturn(v: Gubmo_Structs_Arrays::MyArrayInt32) -> Gubmo_Structs_Arrays::MyArrayInt32
  {
    v
  }

  pub open spec fn myArrayInt32_FunctionParam(v: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
  {
    exists|i:int| 0 <= i <= v.len() - 1 && #[trigger] v[i] == 0i32
  }

  pub open spec fn myArrayStruct_FunctionReturn(v: Gubmo_Structs_Arrays::MyArrayStruct) -> Gubmo_Structs_Arrays::MyArrayStruct
  {
    v
  }

  pub open spec fn myArrayStruct_FunctionParam(v: Gubmo_Structs_Arrays::MyArrayStruct) -> bool
  {
    exists|i:int| 0 <= i <= v.len() - 1 && #[trigger] v[i].fieldSInt32 == 0i32
  }

  pub open spec fn myStructArray_i_FunctionReturn(v: Gubmo_Structs_Arrays::MyStructArray_i) -> Gubmo_Structs_Arrays::MyStructArray_i
  {
    v
  }

  pub open spec fn myStructArray_i_FunctionParam(v: Gubmo_Structs_Arrays::MyStructArray_i) -> bool
  {
    exists|i:int| 0 <= i <= v.fieldArray.len() - 1 && #[trigger] v.fieldArray[i].fieldSInt32 == 0i32
  }

  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  /// 
  ///   pub open spec fn subclauseSpecFunction_Assume__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool) { ... }
  /// 
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn subclauseSpecFunction_Assume(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
  {
    subclauseSpecFunction_Assume__developer_verus(a)
  }

  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  /// 
  ///   pub open spec fn subclauseSpecFunction_Guarantee__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool) { ... }
  /// 
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn subclauseSpecFunction_Guarantee(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
  {
    subclauseSpecFunction_Guarantee__developer_verus(a)
  }
  // END MARKER GUMBO METHODS

  /// Developer-supplied Verus realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined as long as it remains a pure Verus `spec fn`.
  pub open spec fn subclauseSpecFunction_Assume__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow Verus to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

  /// Developer-supplied Verus realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined as long as it remains a pure Verus `spec fn`.
  pub open spec fn subclauseSpecFunction_Guarantee__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow Verus to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

  /// Developer-supplied GUMBOX realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined.
  pub exec fn subclauseSpecFunction_Assume__developer_gumbox(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
    ensures
      res == subclauseSpecFunction_Assume__developer_verus(a),
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow GUMBOX to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

  /// Developer-supplied GUMBOX realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined.
  pub exec fn subclauseSpecFunction_Guarantee__developer_gumbox(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
    ensures
      res == subclauseSpecFunction_Guarantee__developer_verus(a),
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow GUMBOX to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

}
