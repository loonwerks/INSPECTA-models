// #Sireum

package base.Gumbo_Structs_Arrays

import org.sireum._
import base._
import org.sireum.S8._
import org.sireum.S16._
import org.sireum.S32._
import org.sireum.S64._
import org.sireum.U8._
import org.sireum.U16._
import org.sireum.U32._
import org.sireum.U64._
import base.Gumbo_Structs_Arrays.MyArrayInt32.I05910D
import base.Gumbo_Structs_Arrays.MyArrayStruct.I9442F9

// This file will not be overwritten so is safe to edit
object ConsumerThr_i_consumer_consumer {

  // BEGIN FUNCTIONS
  @strictpure def convertB(v: Base_Types.Boolean): Base_Types.Boolean = conversions.B.toS8(v) == s8"1" &&
    conversions.B.toS16(v) == s16"1" &&
    conversions.B.toS32(v) == s32"1" &&
    conversions.B.toS64(v) == s64"1" &&
    conversions.B.toU8(v) == u8"1" &&
    conversions.B.toU16(v) == u16"1" &&
    conversions.B.toU32(v) == u32"1" &&
    conversions.B.toU64(v) == u64"1"

  @strictpure def convertS8(v: Base_Types.Integer_8): Base_Types.Boolean = conversions.S8.toS8(v) == s8"1" &&
    conversions.S8.toS16(v) == s16"1" &&
    conversions.S8.toS32(v) == s32"1" &&
    conversions.S8.toS64(v) == s64"1" &&
    conversions.S8.toU8(v) == u8"1" &&
    conversions.S8.toU16(v) == u16"1" &&
    conversions.S8.toU32(v) == u32"1" &&
    conversions.S8.toU64(v) == u64"1"

  @strictpure def convertS16(v: Base_Types.Integer_16): Base_Types.Boolean = conversions.S16.toS8(v) == s8"1" &&
    conversions.S16.toS16(v) == s16"1" &&
    conversions.S16.toS32(v) == s32"1" &&
    conversions.S16.toS64(v) == s64"1" &&
    conversions.S16.toU8(v) == u8"1" &&
    conversions.S16.toU16(v) == u16"1" &&
    conversions.S16.toU32(v) == u32"1" &&
    conversions.S16.toU64(v) == u64"1"

  @strictpure def convertS32(v: Base_Types.Integer_32): Base_Types.Boolean = conversions.S32.toS8(v) == s8"1" &&
    conversions.S32.toS16(v) == s16"1" &&
    conversions.S32.toS32(v) == s32"1" &&
    conversions.S32.toS64(v) == s64"1" &&
    conversions.S32.toU8(v) == u8"1" &&
    conversions.S32.toU16(v) == u16"1" &&
    conversions.S32.toU32(v) == u32"1" &&
    conversions.S32.toU64(v) == u64"1"

  @strictpure def convertS64(v: Base_Types.Integer_64): Base_Types.Boolean = conversions.S64.toS8(v) == s8"1" &&
    conversions.S64.toS16(v) == s16"1" &&
    conversions.S64.toS32(v) == s32"1" &&
    conversions.S64.toS64(v) == s64"1" &&
    conversions.S64.toU8(v) == u8"1" &&
    conversions.S64.toU16(v) == u16"1" &&
    conversions.S64.toU32(v) == u32"1" &&
    conversions.S64.toU64(v) == u64"1"

  @strictpure def convertU8(v: Base_Types.Unsigned_8): Base_Types.Boolean = conversions.U8.toS8(v) == s8"1" &&
    conversions.U8.toS16(v) == s16"1" &&
    conversions.U8.toS32(v) == s32"1" &&
    conversions.U8.toS64(v) == s64"1" &&
    conversions.U8.toU8(v) == u8"1" &&
    conversions.U8.toU16(v) == u16"1" &&
    conversions.U8.toU32(v) == u32"1" &&
    conversions.U8.toU64(v) == u64"1"

  @strictpure def convertU16(v: Base_Types.Unsigned_16): Base_Types.Boolean = conversions.U16.toS8(v) == s8"1" &&
    conversions.U16.toS16(v) == s16"1" &&
    conversions.U16.toS32(v) == s32"1" &&
    conversions.U16.toS64(v) == s64"1" &&
    conversions.U16.toU8(v) == u8"1" &&
    conversions.U16.toU16(v) == u16"1" &&
    conversions.U16.toU32(v) == u32"1" &&
    conversions.U16.toU64(v) == u64"1"

  @strictpure def convertU32(v: Base_Types.Unsigned_32): Base_Types.Boolean = conversions.U32.toS8(v) == s8"1" &&
    conversions.U32.toS16(v) == s16"1" &&
    conversions.U32.toS32(v) == s32"1" &&
    conversions.U32.toS64(v) == s64"1" &&
    conversions.U32.toU8(v) == u8"1" &&
    conversions.U32.toU16(v) == u16"1" &&
    conversions.U32.toU32(v) == u32"1" &&
    conversions.U32.toU64(v) == u64"1"

  @strictpure def convertU64(v: Base_Types.Unsigned_64): Base_Types.Boolean = conversions.U64.toS8(v) == s8"1" &&
    conversions.U64.toS16(v) == s16"1" &&
    conversions.U64.toS32(v) == s32"1" &&
    conversions.U64.toS64(v) == s64"1" &&
    conversions.U64.toU8(v) == u8"1" &&
    conversions.U64.toU16(v) == u16"1" &&
    conversions.U64.toU32(v) == u32"1" &&
    conversions.U64.toU64(v) == u64"1"

  @strictpure def add(a: Base_Types.Integer_32, b: Base_Types.Integer_32): Base_Types.Integer_32 = a + b

  @strictpure def addMinAndMax(a: Base_Types.Integer_32, b: Base_Types.Integer_32, c: Base_Types.Integer_32): Base_Types.Integer_32 = if (a < b) if (b < c) a + c else a + b else if (a < c) b + c else b + a

  @strictpure def test(x: Base_Types.Integer_32): Base_Types.Boolean = T

  @strictpure def abs(x: Base_Types.Integer_32): Base_Types.Integer_32 = if (x < s32"0") -x else x + s32"0"

  @strictpure def square(a: Base_Types.Integer_64): Base_Types.Integer_64 = a * a
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
        // assume atLeastOneZero_ArrayInt32
        api.MyArrayInt32.nonEmpty ___>: ∃(0 until api.MyArrayInt32.get.value.size)(i => api.MyArrayInt32.get.value(I05910D(i)) == s32"0"),
        // assume isSorted_ArrayInt32
        api.MyArrayInt32.nonEmpty ___>: ∀(0 to api.MyArrayInt32.get.value.size - 2)(i => api.MyArrayInt32.get.value(I05910D(i)) <= api.MyArrayInt32.get.value(I05910D(i + 1))),
        // assume atLeastOneZero_StructArray
        api.myStructArray.nonEmpty ___>: ∃(0 until api.myStructArray.get.fieldArray.value.size)(i => api.myStructArray.get.fieldArray.value(I9442F9(i)).fieldSInt32 == s32"0"),
        // assume isSorted_StructArray
        api.myStructArray.nonEmpty ___>: ∀(0 until api.myStructArray.get.fieldArray.value.size - 1)(i => api.myStructArray.get.fieldArray.value(I9442F9(i)).fieldSInt32 <= api.myStructArray.get.fieldArray.value(I9442F9(i + 1)).fieldSInt32),
        // assume atLeastOneZero_ArrayStruct
        api.MyArrayStruct.nonEmpty ___>: ∃(0 until api.MyArrayStruct.get.value.size)(i => api.MyArrayStruct.get.value(I9442F9(i)).fieldSInt32 == s32"0"),
        // assume isSorted_ArrayStruct
        //   Demonstrate that the trigger will be attached to the *first indexed use* of the quantified variable 
        //   inside an expression, not merely the first textual occurrence of the quantifier variable.
        api.MyArrayStruct.nonEmpty ___>: ∀(0 to api.MyArrayStruct.get.value.size - 2)(i => if (i >= 0) api.MyArrayStruct.get.value(I9442F9(i)).fieldSInt32 <= api.MyArrayStruct.get.value(I9442F9(i + 1)).fieldSInt32 else T),
        // assume assume_valid_velocity
        api.myStructArray.nonEmpty ___>:
          ConsumerThr_i_consumer_consumer.square(api.myStructArray.get.fieldInt64) + ConsumerThr_i_consumer_consumer.square(api.myStructArray.get.fieldInt64) <= ConsumerThr_i_consumer_consumer.square(GL.GUMBO__Library.MAX_SPEED())
        // END COMPUTE REQUIRES timeTriggered
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee conversions
        //   Exercise all base conversions
        ConsumerThr_i_consumer_consumer.convertB(T) && ConsumerThr_i_consumer_consumer.convertS8(s8"1") &&
          ConsumerThr_i_consumer_consumer.convertS16(s16"1") &&
          ConsumerThr_i_consumer_consumer.convertS32(s32"1") &&
          ConsumerThr_i_consumer_consumer.convertS64(s64"1") &&
          ConsumerThr_i_consumer_consumer.convertU8(u8"1") &&
          ConsumerThr_i_consumer_consumer.convertU16(u16"1") &&
          ConsumerThr_i_consumer_consumer.convertU32(u32"1") &&
          ConsumerThr_i_consumer_consumer.convertU64(u64"1"),
        // guarantee guarantee_valid_velocity
        api.myStructArray.nonEmpty ___>:
          ConsumerThr_i_consumer_consumer.square(api.myStructArray.get.fieldInt64) + ConsumerThr_i_consumer_consumer.square(api.myStructArray.get.fieldInt64) <= ConsumerThr_i_consumer_consumer.square(GL.GUMBO__Library.MAX_SPEED()),
        // guarantee all_zero
        (api.MyArrayInt32.nonEmpty ___>: ∀(0 until api.MyArrayInt32.get.value.size)(i => ConsumerThr_i_consumer_consumer.test(api.MyArrayInt32.get.value(I05910D(i))) & T)) __>:
          T
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_myStructArray: Option[Gumbo_Structs_Arrays.MyStructArray_i] = api.get_myStructArray()
    api.logInfo(s"Received on event data port myStructArray: ${apiUsage_myStructArray}")
    val apiUsage_MyArrayStruct: Option[Gumbo_Structs_Arrays.MyArrayStruct] = api.get_MyArrayStruct()
    api.logInfo(s"Received on event data port MyArrayStruct: ${apiUsage_MyArrayStruct}")
    val apiUsage_MyArrayInt32: Option[Gumbo_Structs_Arrays.MyArrayInt32] = api.get_MyArrayInt32()
    api.logInfo(s"Received on event data port MyArrayInt32: ${apiUsage_MyArrayInt32}")
  }

  def finalise(api: ConsumerThr_i_Operational_Api): Unit = { }
}
