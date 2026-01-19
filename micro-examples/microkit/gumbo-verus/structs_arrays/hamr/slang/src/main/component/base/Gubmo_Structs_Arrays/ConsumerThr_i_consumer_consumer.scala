// #Sireum

package base.Gubmo_Structs_Arrays

import org.sireum._
import base._
import base.Gubmo_Structs_Arrays.MyArrayInt32.FB4CC3
import base.Gubmo_Structs_Arrays.MyArrayStruct.I30F6A8
import org.sireum.S32._

// This file will not be overwritten so is safe to edit
object ConsumerThr_i_consumer_consumer {

  // BEGIN STATE VARS
  var myArrayInt32_StateVar: Gubmo_Structs_Arrays.MyArrayInt32 = Gubmo_Structs_Arrays.MyArrayInt32.example()

  var myArrayStruct_StateVar: Gubmo_Structs_Arrays.MyArrayStruct = Gubmo_Structs_Arrays.MyArrayStruct.example()

  var myStructArray_StateVar: Gubmo_Structs_Arrays.MyStructArray_i = Gubmo_Structs_Arrays.MyStructArray_i.example()
  // END STATE VARS

  // BEGIN FUNCTIONS
  @strictpure def myArrayInt32_FunctionReturn(v: Gubmo_Structs_Arrays.MyArrayInt32): Gubmo_Structs_Arrays.MyArrayInt32 = v

  @strictpure def myArrayInt32_FunctionParam(v: Gubmo_Structs_Arrays.MyArrayInt32): Base_Types.Boolean = ∃(0 to v.value.size - 1)(i => v.value(FB4CC3(i)) == s32"0")

  @strictpure def myArrayStruct_FunctionReturn(v: Gubmo_Structs_Arrays.MyArrayStruct): Gubmo_Structs_Arrays.MyArrayStruct = v

  @strictpure def myArrayStruct_FunctionParam(v: Gubmo_Structs_Arrays.MyArrayStruct): Base_Types.Boolean = ∃(0 to v.value.size - 1)(i => v.value(I30F6A8(i)).fieldSInt32 == s32"0")

  @strictpure def myStructArray_i_FunctionReturn(v: Gubmo_Structs_Arrays.MyStructArray_i): Gubmo_Structs_Arrays.MyStructArray_i = v

  @strictpure def myStructArray_i_FunctionParam(v: Gubmo_Structs_Arrays.MyStructArray_i): Base_Types.Boolean = ∃(0 to v.fieldArray.value.size - 1)(i => v.fieldArray.value(I30F6A8(i)).fieldSInt32 == s32"0")
  // END FUNCTIONS

  def initialise(api: ConsumerThr_i_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

  }

  def timeTriggered(api: ConsumerThr_i_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume isSorted_MyArrayInt32_StateVar_Assume
        ∀(0 to (In(myArrayInt32_StateVar)).value.size - 2)(i => (In(myArrayInt32_StateVar)).value(FB4CC3(i)) <= (In(myArrayInt32_StateVar)).value(FB4CC3(i + 1))),
        // assume isSorted_MyArrayStruct_StateVar_Assume
        ∀(0 to (In(myArrayStruct_StateVar)).value.size - 2)(i => (In(myArrayStruct_StateVar)).value(I30F6A8(i)).fieldSInt32 <= (In(myArrayStruct_StateVar)).value(I30F6A8(i + 1)).fieldSInt32),
        // assume isSorted_MyStructArray_StateVar_Assume
        ∀(0 to (In(myStructArray_StateVar)).fieldArray.value.size - 2)(i => (In(myStructArray_StateVar)).fieldArray.value(I30F6A8(i)).fieldSInt32 <= (In(myStructArray_StateVar)).fieldArray.value(I30F6A8(i + 1)).fieldSInt32),
        // assume isSorted_MyArrayInt32_Function_Assume
        //   Ensure operations on an array returned by a function work as expected
        ∀(0 to ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionReturn(In(myArrayInt32_StateVar)).value.size - 2)(i => ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionReturn(In(myArrayInt32_StateVar)).value(FB4CC3(i)) <= ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionReturn(In(myArrayInt32_StateVar)).value(FB4CC3(i + 1))),
        // assume myArrayInt32_FunctionParam_Assume
        //   ensure functions can operate on arrays
        ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionParam(In(myArrayInt32_StateVar)),
        // assume isSorted_MyArrayStruct_Function_Assume
        //   Ensure operations on an array returned by a function work as expected
        ∀(0 to ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionReturn(In(myArrayStruct_StateVar)).value.size - 2)(i => ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionReturn(In(myArrayStruct_StateVar)).value(I30F6A8(i)).fieldSInt32 <= ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionReturn(In(myArrayStruct_StateVar)).value(I30F6A8(i + 1)).fieldSInt32),
        // assume myArrayStruct_FunctionParam_Assume
        //   ensure functions can operate on arrays
        ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionParam(In(myArrayStruct_StateVar)),
        // assume isSorted_MyStructArray_i_Function_Assume
        //   Ensure operations on an array returned by a function work as expected
        ∀(0 to ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionReturn(In(myStructArray_StateVar)).fieldArray.value.size - 2)(i => ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionReturn(In(myStructArray_StateVar)).fieldArray.value(I30F6A8(i)).fieldSInt32 <= ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionReturn(In(myStructArray_StateVar)).fieldArray.value(I30F6A8(i + 1)).fieldSInt32),
        // assume myStructArray_i_FunctionParam_Assume
        //   ensure functions can operate on arrays
        ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionParam(In(myStructArray_StateVar)),
        // assume atLeastOneZero_MyArrayInt32_DataPort_Assume
        ∃(0 to api.myArrayInt32_DataPort.value.size - 1)(i => api.myArrayInt32_DataPort.value(FB4CC3(i)) == s32"0"),
        // assume isSorted_MyArrayInt32_DataPort_Assume
        ∀(0 to api.myArrayInt32_DataPort.value.size - 2)(i => api.myArrayInt32_DataPort.value(FB4CC3(i)) <= api.myArrayInt32_DataPort.value(FB4CC3(i + 1))),
        // assume isSorted_MyArrayStruct_DataPort_Assume
        ∀(0 to api.myArrayStruct_DataPort.value.size - 2)(i => api.myArrayStruct_DataPort.value(I30F6A8(i)).fieldSInt32 <= api.myArrayStruct_DataPort.value(I30F6A8(i + 1)).fieldSInt32),
        // assume isSorted_MyStructArray_DataPort_Assume
        ∀(0 to api.myStructArray_DataPort.fieldArray.value.size - 2)(i => api.myStructArray_DataPort.fieldArray.value(I30F6A8(i)).fieldSInt32 <= api.myStructArray_DataPort.fieldArray.value(I30F6A8(i + 1)).fieldSInt32),
        // assume isSorted_MyArrayInt32_EventDataPort_Assume
        api.myArrayInt32_EventDataPort.nonEmpty.value ___>: ∀(0 to api.myArrayInt32_EventDataPort.get.value.size - 2)(i => api.myArrayInt32_EventDataPort.get.value(FB4CC3(i)) <= api.myArrayInt32_EventDataPort.get.value(FB4CC3(i + 1))),
        // assume isSorted_MyArrayStruct_EventDataPort_Assume
        api.myArrayStruct_EventDataPort.nonEmpty.value ___>: ∀(0 to api.myArrayStruct_EventDataPort.get.value.size - 2)(i => api.myArrayStruct_EventDataPort.get.value(I30F6A8(i)).fieldSInt32 <= api.myArrayStruct_EventDataPort.get.value(I30F6A8(i + 1)).fieldSInt32),
        // assume isSorted_MyStructArray_EventDataPort_Assume
        api.myStructArray_EventDataPort.nonEmpty ___>: ∀(0 to api.myStructArray_EventDataPort.get.fieldArray.value.size - 2)(i => api.myStructArray_EventDataPort.get.fieldArray.value(I30F6A8(i)).fieldSInt32 <= api.myStructArray_EventDataPort.get.fieldArray.value(I30F6A8(i + 1)).fieldSInt32)
        // END COMPUTE REQUIRES timeTriggered
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee noChange_MyArrayInt32_StateVar_Guarantee
        (In(myArrayInt32_StateVar)).value(FB4CC3(0)) == myArrayInt32_StateVar.value(FB4CC3(0)),
        // guarantee noChange_MyArrayStruct_StateVar_Guarantee
        (In(myArrayStruct_StateVar)).value(I30F6A8(0)).fieldSInt32 == myArrayStruct_StateVar.value(I30F6A8(0)).fieldSInt32,
        // guarantee isSorted_MyArrayInt32_StateVar_Guarantee
        ∀(0 to myArrayInt32_StateVar.value.size - 2)(i => (In(myArrayInt32_StateVar)).value(FB4CC3(i)) <= myArrayInt32_StateVar.value(FB4CC3(i + 1))),
        // guarantee isSorted_MyArrayStruct_StateVar_Guarantee
        ∀(0 to myArrayStruct_StateVar.value.size - 2)(i => (In(myArrayStruct_StateVar)).value(I30F6A8(i)).fieldSInt32 <= myArrayStruct_StateVar.value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee isSorted_MyStructArray_StateVar_Guarantee
        ∀(0 to myStructArray_StateVar.fieldArray.value.size - 2)(i => (In(myStructArray_StateVar)).fieldArray.value(I30F6A8(i)).fieldSInt32 <= myStructArray_StateVar.fieldArray.value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee isSorted_MyArrayInt32_Function_Guarantee
        //   Ensure operations on an array returned by a function work as expected
        ∀(0 to ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionReturn(myArrayInt32_StateVar).value.size - 2)(i => ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionReturn(In(myArrayInt32_StateVar)).value(FB4CC3(i)) <= ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionReturn(myArrayInt32_StateVar).value(FB4CC3(i + 1))),
        // guarantee myArrayInt32_FunctionParam_Guarantee
        //   ensure functions can operate on arrays
        ConsumerThr_i_consumer_consumer.myArrayInt32_FunctionParam(In(myArrayInt32_StateVar)),
        // guarantee isSorted_MyArrayStruct_Function_Guarantee
        //   Ensure operations on an array returned by a function work as expected
        ∀(0 to ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionReturn(myArrayStruct_StateVar).value.size - 2)(i => ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionReturn(In(myArrayStruct_StateVar)).value(I30F6A8(i)).fieldSInt32 <= ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionReturn(myArrayStruct_StateVar).value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee myArrayStruct_FunctionParam_Guarantee
        //   ensure functions can operate on arrays
        ConsumerThr_i_consumer_consumer.myArrayStruct_FunctionParam(In(myArrayStruct_StateVar)),
        // guarantee isSorted_MyStructArray_i_Function_Guarantee
        //   Ensure operations on an array returned by a function work as expected
        ∀(0 to ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray.value.size - 2)(i => ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionReturn(In(myStructArray_StateVar)).fieldArray.value(I30F6A8(i)).fieldSInt32 <= ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray.value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee myStructArray_i_FunctionParam_Guarantee
        //   ensure functions can operate on arrays
        ConsumerThr_i_consumer_consumer.myStructArray_i_FunctionParam(In(myStructArray_StateVar)),
        // guarantee atLeastOneZero_MyArrayInt32_DataPort_Guarantee
        ∃(0 to api.myArrayInt32_DataPort.value.size - 1)(i => api.myArrayInt32_DataPort.value(FB4CC3(i)) == s32"0"),
        // guarantee isSorted_MyArrayInt32_DataPort_Guarantee
        ∀(0 to api.myArrayInt32_DataPort.value.size - 2)(i => api.myArrayInt32_DataPort.value(FB4CC3(i)) <= api.myArrayInt32_DataPort.value(FB4CC3(i + 1))),
        // guarantee isSorted_MyArrayStruct_DataPort_Guarantee
        ∀(0 to api.myArrayStruct_DataPort.value.size - 2)(i => api.myArrayStruct_DataPort.value(I30F6A8(i)).fieldSInt32 <= api.myArrayStruct_DataPort.value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee isSorted_MyStructArray_DataPort_Guarantee
        ∀(0 to api.myStructArray_DataPort.fieldArray.value.size - 2)(i => api.myStructArray_DataPort.fieldArray.value(I30F6A8(i)).fieldSInt32 <= api.myStructArray_DataPort.fieldArray.value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee isSorted_MyArrayInt32_EventDataPort_Guarantee
        api.myArrayInt32_EventDataPort.nonEmpty.value ___>: ∀(0 to api.myArrayInt32_EventDataPort.get.value.size - 2)(i => api.myArrayInt32_EventDataPort.get.value(FB4CC3(i)) <= api.myArrayInt32_EventDataPort.get.value(FB4CC3(i + 1))),
        // guarantee isSorted_MyArrayStruct_EventDataPort_Guarantee
        api.myArrayStruct_EventDataPort.nonEmpty.value ___>: ∀(0 to api.myArrayStruct_EventDataPort.get.value.size - 2)(i => api.myArrayStruct_EventDataPort.get.value(I30F6A8(i)).fieldSInt32 <= api.myArrayStruct_EventDataPort.get.value(I30F6A8(i + 1)).fieldSInt32),
        // guarantee isSorted_MyStructArray_EventDataPort_Guarantee
        api.myStructArray_EventDataPort.nonEmpty ___>: ∀(0 to api.myStructArray_EventDataPort.get.fieldArray.value.size - 2)(i => api.myStructArray_EventDataPort.get.fieldArray.value(I30F6A8(i)).fieldSInt32 <= api.myStructArray_EventDataPort.get.fieldArray.value(I30F6A8(i + 1)).fieldSInt32)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_myArrayInt32_DataPort: Option[Gubmo_Structs_Arrays.MyArrayInt32] = api.get_myArrayInt32_DataPort()
    api.logInfo(s"Received on data port myArrayInt32_DataPort: ${apiUsage_myArrayInt32_DataPort}")
    val apiUsage_myArrayStruct_DataPort: Option[Gubmo_Structs_Arrays.MyArrayStruct] = api.get_myArrayStruct_DataPort()
    api.logInfo(s"Received on data port myArrayStruct_DataPort: ${apiUsage_myArrayStruct_DataPort}")
    val apiUsage_myStructArray_DataPort: Option[Gubmo_Structs_Arrays.MyStructArray_i] = api.get_myStructArray_DataPort()
    api.logInfo(s"Received on data port myStructArray_DataPort: ${apiUsage_myStructArray_DataPort}")
    val apiUsage_myArrayInt32_EventDataPort: Option[Gubmo_Structs_Arrays.MyArrayInt32] = api.get_myArrayInt32_EventDataPort()
    api.logInfo(s"Received on event data port myArrayInt32_EventDataPort: ${apiUsage_myArrayInt32_EventDataPort}")
    val apiUsage_myArrayStruct_EventDataPort: Option[Gubmo_Structs_Arrays.MyArrayStruct] = api.get_myArrayStruct_EventDataPort()
    api.logInfo(s"Received on event data port myArrayStruct_EventDataPort: ${apiUsage_myArrayStruct_EventDataPort}")
    val apiUsage_myStructArray_EventDataPort: Option[Gubmo_Structs_Arrays.MyStructArray_i] = api.get_myStructArray_EventDataPort()
    api.logInfo(s"Received on event data port myStructArray_EventDataPort: ${apiUsage_myStructArray_EventDataPort}")
  }

  def finalise(api: ConsumerThr_i_Operational_Api): Unit = { }
}
