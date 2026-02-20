// #Sireum
// @formatter:off

// This file is auto-generated from MyEnum.scala, MyStruct2_i.scala, MyArrayInt32.scala, MyArrayStruct.scala, MyStructArray_i.scala, Base_Types.scala, ProducerThr_i_producer_producer_Containers.scala, ConsumerThr_i_consumer_consumer_Containers.scala, ObservationKind.scala, Container.scala, DataContent.scala, Aux_Types.scala

package sysml

import org.sireum._

object MsgPack {

  object Constants {

    val Gubmo_Structs_ArraysMyEnum_Payload: Z = -32

    val Gubmo_Structs_ArraysMyStruct2_i: Z = -31

    val Gubmo_Structs_ArraysMyStruct2_i_Payload: Z = -30

    val Gubmo_Structs_ArraysMyArrayInt32: Z = -29

    val Gubmo_Structs_ArraysMyArrayInt32_Payload: Z = -28

    val Gubmo_Structs_ArraysMyArrayStruct: Z = -27

    val Gubmo_Structs_ArraysMyArrayStruct_Payload: Z = -26

    val Gubmo_Structs_ArraysMyStructArray_i: Z = -25

    val Gubmo_Structs_ArraysMyStructArray_i_Payload: Z = -24

    val Base_TypesBoolean_Payload: Z = -23

    val Base_TypesInteger_Payload: Z = -22

    val Base_TypesInteger_8_Payload: Z = -21

    val Base_TypesInteger_16_Payload: Z = -20

    val Base_TypesInteger_32_Payload: Z = -19

    val Base_TypesInteger_64_Payload: Z = -18

    val Base_TypesUnsigned_8_Payload: Z = -17

    val Base_TypesUnsigned_16_Payload: Z = -16

    val Base_TypesUnsigned_32_Payload: Z = -15

    val Base_TypesUnsigned_64_Payload: Z = -14

    val Base_TypesFloat_Payload: Z = -13

    val Base_TypesFloat_32_Payload: Z = -12

    val Base_TypesFloat_64_Payload: Z = -11

    val Base_TypesCharacter_Payload: Z = -10

    val Base_TypesString_Payload: Z = -9

    val Base_TypesBits_Payload: Z = -8

    val Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P: Z = -7

    val Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS: Z = -6

    val Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P: Z = -5

    val Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS: Z = -4

    val Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P: Z = -3

    val Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS: Z = -2

    val Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P: Z = -1

    val Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS: Z = 0

    val utilEmptyContainer: Z = 1

    val _artEmpty: Z = 2

  }

  object Writer {

    @record class Default(val writer: MessagePack.Writer.Impl) extends Writer

  }

  @msig trait Writer {

    def writer: MessagePack.Writer

    def writeGubmo_Structs_ArraysMyEnumType(o: Gubmo_Structs_Arrays.MyEnum.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeGubmo_Structs_ArraysMyEnum_Payload(o: Gubmo_Structs_Arrays.MyEnum_Payload): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyEnum_Payload)
      writeGubmo_Structs_ArraysMyEnumType(o.value)
    }

    def writeGubmo_Structs_ArraysMyStruct2_i(o: Gubmo_Structs_Arrays.MyStruct2_i): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyStruct2_i)
      writer.writeS32(o.fieldSInt32)
    }

    def writeGubmo_Structs_ArraysMyStruct2_i_Payload(o: Gubmo_Structs_Arrays.MyStruct2_i_Payload): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyStruct2_i_Payload)
      writeGubmo_Structs_ArraysMyStruct2_i(o.value)
    }

    def writeGubmo_Structs_ArraysMyArrayInt32I(o: Gubmo_Structs_Arrays.MyArrayInt32.I): Unit = {
      writer.writeZ(o.toZ)
    }

    def writeISGubmo_Structs_ArraysMyArrayInt32I[E](s: IS[Gubmo_Structs_Arrays.MyArrayInt32.I, E], f: E => Unit) : Unit = {
      writer.writeArrayHeader(s.size)
      for (e <- s) {
        f(e)
      }
    }

    def writeGubmo_Structs_ArraysMyArrayInt32(o: Gubmo_Structs_Arrays.MyArrayInt32): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyArrayInt32)
      writeISGubmo_Structs_ArraysMyArrayInt32I(o.value, writer.writeS32 _)
    }

    def writeGubmo_Structs_ArraysMyArrayInt32_Payload(o: Gubmo_Structs_Arrays.MyArrayInt32_Payload): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyArrayInt32_Payload)
      writeGubmo_Structs_ArraysMyArrayInt32(o.value)
    }

    def writeGubmo_Structs_ArraysMyArrayStructI(o: Gubmo_Structs_Arrays.MyArrayStruct.I): Unit = {
      writer.writeZ(o.toZ)
    }

    def writeISGubmo_Structs_ArraysMyArrayStructI[E](s: IS[Gubmo_Structs_Arrays.MyArrayStruct.I, E], f: E => Unit) : Unit = {
      writer.writeArrayHeader(s.size)
      for (e <- s) {
        f(e)
      }
    }

    def writeGubmo_Structs_ArraysMyArrayStruct(o: Gubmo_Structs_Arrays.MyArrayStruct): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyArrayStruct)
      writeISGubmo_Structs_ArraysMyArrayStructI(o.value, writeGubmo_Structs_ArraysMyStruct2_i _)
    }

    def writeGubmo_Structs_ArraysMyArrayStruct_Payload(o: Gubmo_Structs_Arrays.MyArrayStruct_Payload): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyArrayStruct_Payload)
      writeGubmo_Structs_ArraysMyArrayStruct(o.value)
    }

    def writeGubmo_Structs_ArraysMyStructArray_i(o: Gubmo_Structs_Arrays.MyStructArray_i): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyStructArray_i)
      writer.writeS64(o.fieldInt64)
      writeGubmo_Structs_ArraysMyStruct2_i(o.fieldRec)
      writeGubmo_Structs_ArraysMyArrayStruct(o.fieldArray)
      writeGubmo_Structs_ArraysMyEnumType(o.fieldEnum)
    }

    def writeGubmo_Structs_ArraysMyStructArray_i_Payload(o: Gubmo_Structs_Arrays.MyStructArray_i_Payload): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysMyStructArray_i_Payload)
      writeGubmo_Structs_ArraysMyStructArray_i(o.value)
    }

    def writeBase_TypesBoolean_Payload(o: Base_Types.Boolean_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesBoolean_Payload)
      writer.writeB(o.value)
    }

    def writeBase_TypesInteger_Payload(o: Base_Types.Integer_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_Payload)
      writer.writeZ(o.value)
    }

    def writeBase_TypesInteger_8_Payload(o: Base_Types.Integer_8_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_8_Payload)
      writer.writeS8(o.value)
    }

    def writeBase_TypesInteger_16_Payload(o: Base_Types.Integer_16_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_16_Payload)
      writer.writeS16(o.value)
    }

    def writeBase_TypesInteger_32_Payload(o: Base_Types.Integer_32_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_32_Payload)
      writer.writeS32(o.value)
    }

    def writeBase_TypesInteger_64_Payload(o: Base_Types.Integer_64_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_64_Payload)
      writer.writeS64(o.value)
    }

    def writeBase_TypesUnsigned_8_Payload(o: Base_Types.Unsigned_8_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_8_Payload)
      writer.writeU8(o.value)
    }

    def writeBase_TypesUnsigned_16_Payload(o: Base_Types.Unsigned_16_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_16_Payload)
      writer.writeU16(o.value)
    }

    def writeBase_TypesUnsigned_32_Payload(o: Base_Types.Unsigned_32_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_32_Payload)
      writer.writeU32(o.value)
    }

    def writeBase_TypesUnsigned_64_Payload(o: Base_Types.Unsigned_64_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_64_Payload)
      writer.writeU64(o.value)
    }

    def writeBase_TypesFloat_Payload(o: Base_Types.Float_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesFloat_Payload)
      writer.writeR(o.value)
    }

    def writeBase_TypesFloat_32_Payload(o: Base_Types.Float_32_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesFloat_32_Payload)
      writer.writeF32(o.value)
    }

    def writeBase_TypesFloat_64_Payload(o: Base_Types.Float_64_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesFloat_64_Payload)
      writer.writeF64(o.value)
    }

    def writeBase_TypesCharacter_Payload(o: Base_Types.Character_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesCharacter_Payload)
      writer.writeC(o.value)
    }

    def writeBase_TypesString_Payload(o: Base_Types.String_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesString_Payload)
      writer.writeString(o.value)
    }

    def writeBase_TypesBits_Payload(o: Base_Types.Bits_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesBits_Payload)
      writer.writeISZ(o.value, writer.writeB _)
    }

    def writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container): Unit = {
      o match {
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
      }
    }

    def writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P)
    }

    def writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS)
    }

    def writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container): Unit = {
      o match {
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
      }
    }

    def writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P)
      writer.writeOption(o.api_MyArrayStruct, writeGubmo_Structs_ArraysMyArrayStruct _)
      writer.writeOption(o.api_myStructArray, writeGubmo_Structs_ArraysMyStructArray_i _)
    }

    def writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS)
      writer.writeOption(o.api_MyArrayStruct, writeGubmo_Structs_ArraysMyArrayStruct _)
      writer.writeOption(o.api_myStructArray, writeGubmo_Structs_ArraysMyStructArray_i _)
    }

    def writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container): Unit = {
      o match {
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
      }
    }

    def writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P)
      writer.writeOption(o.api_myArrayInt32_EventDataPort, writeGubmo_Structs_ArraysMyArrayInt32 _)
      writer.writeOption(o.api_myArrayStruct_EventDataPort, writeGubmo_Structs_ArraysMyArrayStruct _)
      writer.writeOption(o.api_myStructArray_EventDataPort, writeGubmo_Structs_ArraysMyStructArray_i _)
      writeGubmo_Structs_ArraysMyArrayInt32(o.api_myArrayInt32_DataPort)
      writeGubmo_Structs_ArraysMyArrayStruct(o.api_myArrayStruct_DataPort)
      writeGubmo_Structs_ArraysMyStructArray_i(o.api_myStructArray_DataPort)
    }

    def writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS)
      writeGubmo_Structs_ArraysMyArrayInt32(o.In_myArrayInt32_StateVar)
      writeGubmo_Structs_ArraysMyArrayStruct(o.In_myArrayStruct_StateVar)
      writeGubmo_Structs_ArraysMyStructArray_i(o.In_myStructArray_StateVar)
      writer.writeOption(o.api_myArrayInt32_EventDataPort, writeGubmo_Structs_ArraysMyArrayInt32 _)
      writer.writeOption(o.api_myArrayStruct_EventDataPort, writeGubmo_Structs_ArraysMyArrayStruct _)
      writer.writeOption(o.api_myStructArray_EventDataPort, writeGubmo_Structs_ArraysMyStructArray_i _)
      writeGubmo_Structs_ArraysMyArrayInt32(o.api_myArrayInt32_DataPort)
      writeGubmo_Structs_ArraysMyArrayStruct(o.api_myArrayStruct_DataPort)
      writeGubmo_Structs_ArraysMyStructArray_i(o.api_myStructArray_DataPort)
    }

    def writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container): Unit = {
      o match {
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
      }
    }

    def writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P)
    }

    def writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS)
      writeGubmo_Structs_ArraysMyArrayInt32(o.myArrayInt32_StateVar)
      writeGubmo_Structs_ArraysMyArrayStruct(o.myArrayStruct_StateVar)
      writeGubmo_Structs_ArraysMyStructArray_i(o.myStructArray_StateVar)
    }

    def writeruntimemonitorObservationKindType(o: runtimemonitor.ObservationKind.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeutilContainer(o: util.Container): Unit = {
      o match {
        case o: util.EmptyContainer => writeutilEmptyContainer(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
      }
    }

    def writeutilEmptyContainer(o: util.EmptyContainer): Unit = {
      writer.writeZ(Constants.utilEmptyContainer)
    }

    def write_artDataContent(o: art.DataContent): Unit = {
      o match {
        case o: art.Empty => write_artEmpty(o)
        case o: Base_Types.Boolean_Payload => writeBase_TypesBoolean_Payload(o)
        case o: Base_Types.Integer_Payload => writeBase_TypesInteger_Payload(o)
        case o: Base_Types.Integer_8_Payload => writeBase_TypesInteger_8_Payload(o)
        case o: Base_Types.Integer_16_Payload => writeBase_TypesInteger_16_Payload(o)
        case o: Base_Types.Integer_32_Payload => writeBase_TypesInteger_32_Payload(o)
        case o: Base_Types.Integer_64_Payload => writeBase_TypesInteger_64_Payload(o)
        case o: Base_Types.Unsigned_8_Payload => writeBase_TypesUnsigned_8_Payload(o)
        case o: Base_Types.Unsigned_16_Payload => writeBase_TypesUnsigned_16_Payload(o)
        case o: Base_Types.Unsigned_32_Payload => writeBase_TypesUnsigned_32_Payload(o)
        case o: Base_Types.Unsigned_64_Payload => writeBase_TypesUnsigned_64_Payload(o)
        case o: Base_Types.Float_Payload => writeBase_TypesFloat_Payload(o)
        case o: Base_Types.Float_32_Payload => writeBase_TypesFloat_32_Payload(o)
        case o: Base_Types.Float_64_Payload => writeBase_TypesFloat_64_Payload(o)
        case o: Base_Types.Character_Payload => writeBase_TypesCharacter_Payload(o)
        case o: Base_Types.String_Payload => writeBase_TypesString_Payload(o)
        case o: Base_Types.Bits_Payload => writeBase_TypesBits_Payload(o)
        case o: util.EmptyContainer => writeutilEmptyContainer(o)
        case o: Gubmo_Structs_Arrays.MyEnum_Payload => writeGubmo_Structs_ArraysMyEnum_Payload(o)
        case o: Gubmo_Structs_Arrays.MyStruct2_i_Payload => writeGubmo_Structs_ArraysMyStruct2_i_Payload(o)
        case o: Gubmo_Structs_Arrays.MyArrayInt32_Payload => writeGubmo_Structs_ArraysMyArrayInt32_Payload(o)
        case o: Gubmo_Structs_Arrays.MyArrayStruct_Payload => writeGubmo_Structs_ArraysMyArrayStruct_Payload(o)
        case o: Gubmo_Structs_Arrays.MyStructArray_i_Payload => writeGubmo_Structs_ArraysMyStructArray_i_Payload(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS => writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
        case o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS => writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
      }
    }

    def write_artEmpty(o: art.Empty): Unit = {
      writer.writeZ(Constants._artEmpty)
    }

    def result: ISZ[U8] = {
      return writer.result
    }

  }

  object Reader {

    @record class Default(val reader: MessagePack.Reader.Impl) extends Reader {
      def errorOpt: Option[MessagePack.ErrorMsg] = {
        return reader.errorOpt
      }
    }

  }

  @msig trait Reader {

    def reader: MessagePack.Reader

    def readGubmo_Structs_ArraysMyEnumType(): Gubmo_Structs_Arrays.MyEnum.Type = {
      val r = reader.readZ()
      return Gubmo_Structs_Arrays.MyEnum.byOrdinal(r).get
    }

    def readGubmo_Structs_ArraysMyEnum_Payload(): Gubmo_Structs_Arrays.MyEnum_Payload = {
      val r = readGubmo_Structs_ArraysMyEnum_PayloadT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyEnum_PayloadT(typeParsed: B): Gubmo_Structs_Arrays.MyEnum_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyEnum_Payload)
      }
      val value = readGubmo_Structs_ArraysMyEnumType()
      return Gubmo_Structs_Arrays.MyEnum_Payload(value)
    }

    def readGubmo_Structs_ArraysMyStruct2_i(): Gubmo_Structs_Arrays.MyStruct2_i = {
      val r = readGubmo_Structs_ArraysMyStruct2_iT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyStruct2_iT(typeParsed: B): Gubmo_Structs_Arrays.MyStruct2_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyStruct2_i)
      }
      val fieldSInt32 = reader.readS32()
      return Gubmo_Structs_Arrays.MyStruct2_i(fieldSInt32)
    }

    def readGubmo_Structs_ArraysMyStruct2_i_Payload(): Gubmo_Structs_Arrays.MyStruct2_i_Payload = {
      val r = readGubmo_Structs_ArraysMyStruct2_i_PayloadT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyStruct2_i_PayloadT(typeParsed: B): Gubmo_Structs_Arrays.MyStruct2_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyStruct2_i_Payload)
      }
      val value = readGubmo_Structs_ArraysMyStruct2_i()
      return Gubmo_Structs_Arrays.MyStruct2_i_Payload(value)
    }

    def readGubmo_Structs_ArraysMyArrayInt32I(): Gubmo_Structs_Arrays.MyArrayInt32.I = {
      val n = reader.readZ()
      return Gubmo_Structs_Arrays.MyArrayInt32.I.fromZ(n)
    }

    def readISGubmo_Structs_ArraysMyArrayInt32I[E](f: () => E): IS[Gubmo_Structs_Arrays.MyArrayInt32.I, E] = {
      val size = reader.readArrayHeader()
      var r = IS[Gubmo_Structs_Arrays.MyArrayInt32.I, E]()
      var i = 0
      while (i < size) {
        val o = f()
        r = r :+ o
        i = i + 1
      }
      return r
    }

    def readGubmo_Structs_ArraysMyArrayInt32(): Gubmo_Structs_Arrays.MyArrayInt32 = {
      val r = readGubmo_Structs_ArraysMyArrayInt32T(F)
      return r
    }

    def readGubmo_Structs_ArraysMyArrayInt32T(typeParsed: B): Gubmo_Structs_Arrays.MyArrayInt32 = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyArrayInt32)
      }
      val value = readISGubmo_Structs_ArraysMyArrayInt32I(reader.readS32 _)
      return Gubmo_Structs_Arrays.MyArrayInt32(value)
    }

    def readGubmo_Structs_ArraysMyArrayInt32_Payload(): Gubmo_Structs_Arrays.MyArrayInt32_Payload = {
      val r = readGubmo_Structs_ArraysMyArrayInt32_PayloadT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyArrayInt32_PayloadT(typeParsed: B): Gubmo_Structs_Arrays.MyArrayInt32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyArrayInt32_Payload)
      }
      val value = readGubmo_Structs_ArraysMyArrayInt32()
      return Gubmo_Structs_Arrays.MyArrayInt32_Payload(value)
    }

    def readGubmo_Structs_ArraysMyArrayStructI(): Gubmo_Structs_Arrays.MyArrayStruct.I = {
      val n = reader.readZ()
      return Gubmo_Structs_Arrays.MyArrayStruct.I.fromZ(n)
    }

    def readISGubmo_Structs_ArraysMyArrayStructI[E](f: () => E): IS[Gubmo_Structs_Arrays.MyArrayStruct.I, E] = {
      val size = reader.readArrayHeader()
      var r = IS[Gubmo_Structs_Arrays.MyArrayStruct.I, E]()
      var i = 0
      while (i < size) {
        val o = f()
        r = r :+ o
        i = i + 1
      }
      return r
    }

    def readGubmo_Structs_ArraysMyArrayStruct(): Gubmo_Structs_Arrays.MyArrayStruct = {
      val r = readGubmo_Structs_ArraysMyArrayStructT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyArrayStructT(typeParsed: B): Gubmo_Structs_Arrays.MyArrayStruct = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyArrayStruct)
      }
      val value = readISGubmo_Structs_ArraysMyArrayStructI(readGubmo_Structs_ArraysMyStruct2_i _)
      return Gubmo_Structs_Arrays.MyArrayStruct(value)
    }

    def readGubmo_Structs_ArraysMyArrayStruct_Payload(): Gubmo_Structs_Arrays.MyArrayStruct_Payload = {
      val r = readGubmo_Structs_ArraysMyArrayStruct_PayloadT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyArrayStruct_PayloadT(typeParsed: B): Gubmo_Structs_Arrays.MyArrayStruct_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyArrayStruct_Payload)
      }
      val value = readGubmo_Structs_ArraysMyArrayStruct()
      return Gubmo_Structs_Arrays.MyArrayStruct_Payload(value)
    }

    def readGubmo_Structs_ArraysMyStructArray_i(): Gubmo_Structs_Arrays.MyStructArray_i = {
      val r = readGubmo_Structs_ArraysMyStructArray_iT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyStructArray_iT(typeParsed: B): Gubmo_Structs_Arrays.MyStructArray_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyStructArray_i)
      }
      val fieldInt64 = reader.readS64()
      val fieldRec = readGubmo_Structs_ArraysMyStruct2_i()
      val fieldArray = readGubmo_Structs_ArraysMyArrayStruct()
      val fieldEnum = readGubmo_Structs_ArraysMyEnumType()
      return Gubmo_Structs_Arrays.MyStructArray_i(fieldInt64, fieldRec, fieldArray, fieldEnum)
    }

    def readGubmo_Structs_ArraysMyStructArray_i_Payload(): Gubmo_Structs_Arrays.MyStructArray_i_Payload = {
      val r = readGubmo_Structs_ArraysMyStructArray_i_PayloadT(F)
      return r
    }

    def readGubmo_Structs_ArraysMyStructArray_i_PayloadT(typeParsed: B): Gubmo_Structs_Arrays.MyStructArray_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysMyStructArray_i_Payload)
      }
      val value = readGubmo_Structs_ArraysMyStructArray_i()
      return Gubmo_Structs_Arrays.MyStructArray_i_Payload(value)
    }

    def readBase_TypesBoolean_Payload(): Base_Types.Boolean_Payload = {
      val r = readBase_TypesBoolean_PayloadT(F)
      return r
    }

    def readBase_TypesBoolean_PayloadT(typeParsed: B): Base_Types.Boolean_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesBoolean_Payload)
      }
      val value = reader.readB()
      return Base_Types.Boolean_Payload(value)
    }

    def readBase_TypesInteger_Payload(): Base_Types.Integer_Payload = {
      val r = readBase_TypesInteger_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_PayloadT(typeParsed: B): Base_Types.Integer_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_Payload)
      }
      val value = reader.readZ()
      return Base_Types.Integer_Payload(value)
    }

    def readBase_TypesInteger_8_Payload(): Base_Types.Integer_8_Payload = {
      val r = readBase_TypesInteger_8_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_8_PayloadT(typeParsed: B): Base_Types.Integer_8_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_8_Payload)
      }
      val value = reader.readS8()
      return Base_Types.Integer_8_Payload(value)
    }

    def readBase_TypesInteger_16_Payload(): Base_Types.Integer_16_Payload = {
      val r = readBase_TypesInteger_16_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_16_PayloadT(typeParsed: B): Base_Types.Integer_16_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_16_Payload)
      }
      val value = reader.readS16()
      return Base_Types.Integer_16_Payload(value)
    }

    def readBase_TypesInteger_32_Payload(): Base_Types.Integer_32_Payload = {
      val r = readBase_TypesInteger_32_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_32_PayloadT(typeParsed: B): Base_Types.Integer_32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_32_Payload)
      }
      val value = reader.readS32()
      return Base_Types.Integer_32_Payload(value)
    }

    def readBase_TypesInteger_64_Payload(): Base_Types.Integer_64_Payload = {
      val r = readBase_TypesInteger_64_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_64_PayloadT(typeParsed: B): Base_Types.Integer_64_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_64_Payload)
      }
      val value = reader.readS64()
      return Base_Types.Integer_64_Payload(value)
    }

    def readBase_TypesUnsigned_8_Payload(): Base_Types.Unsigned_8_Payload = {
      val r = readBase_TypesUnsigned_8_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_8_PayloadT(typeParsed: B): Base_Types.Unsigned_8_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_8_Payload)
      }
      val value = reader.readU8()
      return Base_Types.Unsigned_8_Payload(value)
    }

    def readBase_TypesUnsigned_16_Payload(): Base_Types.Unsigned_16_Payload = {
      val r = readBase_TypesUnsigned_16_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_16_PayloadT(typeParsed: B): Base_Types.Unsigned_16_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_16_Payload)
      }
      val value = reader.readU16()
      return Base_Types.Unsigned_16_Payload(value)
    }

    def readBase_TypesUnsigned_32_Payload(): Base_Types.Unsigned_32_Payload = {
      val r = readBase_TypesUnsigned_32_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_32_PayloadT(typeParsed: B): Base_Types.Unsigned_32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_32_Payload)
      }
      val value = reader.readU32()
      return Base_Types.Unsigned_32_Payload(value)
    }

    def readBase_TypesUnsigned_64_Payload(): Base_Types.Unsigned_64_Payload = {
      val r = readBase_TypesUnsigned_64_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_64_PayloadT(typeParsed: B): Base_Types.Unsigned_64_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_64_Payload)
      }
      val value = reader.readU64()
      return Base_Types.Unsigned_64_Payload(value)
    }

    def readBase_TypesFloat_Payload(): Base_Types.Float_Payload = {
      val r = readBase_TypesFloat_PayloadT(F)
      return r
    }

    def readBase_TypesFloat_PayloadT(typeParsed: B): Base_Types.Float_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesFloat_Payload)
      }
      val value = reader.readR()
      return Base_Types.Float_Payload(value)
    }

    def readBase_TypesFloat_32_Payload(): Base_Types.Float_32_Payload = {
      val r = readBase_TypesFloat_32_PayloadT(F)
      return r
    }

    def readBase_TypesFloat_32_PayloadT(typeParsed: B): Base_Types.Float_32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesFloat_32_Payload)
      }
      val value = reader.readF32()
      return Base_Types.Float_32_Payload(value)
    }

    def readBase_TypesFloat_64_Payload(): Base_Types.Float_64_Payload = {
      val r = readBase_TypesFloat_64_PayloadT(F)
      return r
    }

    def readBase_TypesFloat_64_PayloadT(typeParsed: B): Base_Types.Float_64_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesFloat_64_Payload)
      }
      val value = reader.readF64()
      return Base_Types.Float_64_Payload(value)
    }

    def readBase_TypesCharacter_Payload(): Base_Types.Character_Payload = {
      val r = readBase_TypesCharacter_PayloadT(F)
      return r
    }

    def readBase_TypesCharacter_PayloadT(typeParsed: B): Base_Types.Character_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesCharacter_Payload)
      }
      val value = reader.readC()
      return Base_Types.Character_Payload(value)
    }

    def readBase_TypesString_Payload(): Base_Types.String_Payload = {
      val r = readBase_TypesString_PayloadT(F)
      return r
    }

    def readBase_TypesString_PayloadT(typeParsed: B): Base_Types.String_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesString_Payload)
      }
      val value = reader.readString()
      return Base_Types.String_Payload(value)
    }

    def readBase_TypesBits_Payload(): Base_Types.Bits_Payload = {
      val r = readBase_TypesBits_PayloadT(F)
      return r
    }

    def readBase_TypesBits_PayloadT(typeParsed: B): Base_Types.Bits_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesBits_Payload)
      }
      val value = reader.readISZ(reader.readB _)
      return Base_Types.Bits_Payload(value)
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container.")
          val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T)
          return r
      }
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P = {
      val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(F)
      return r
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(typeParsed: B): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P)
      }
      return Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P()
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS = {
      val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(F)
      return r
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(typeParsed: B): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS)
      }
      return Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS()
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container.")
          val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T)
          return r
      }
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P = {
      val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(F)
      return r
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(typeParsed: B): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P)
      }
      val api_MyArrayStruct = reader.readOption(readGubmo_Structs_ArraysMyArrayStruct _)
      val api_myStructArray = reader.readOption(readGubmo_Structs_ArraysMyStructArray_i _)
      return Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P(api_MyArrayStruct, api_myStructArray)
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS = {
      val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(F)
      return r
    }

    def readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(typeParsed: B): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS)
      }
      val api_MyArrayStruct = reader.readOption(readGubmo_Structs_ArraysMyArrayStruct _)
      val api_myStructArray = reader.readOption(readGubmo_Structs_ArraysMyStructArray_i _)
      return Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS(api_MyArrayStruct, api_myStructArray)
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container.")
          val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T)
          return r
      }
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P = {
      val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(F)
      return r
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(typeParsed: B): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P)
      }
      val api_myArrayInt32_EventDataPort = reader.readOption(readGubmo_Structs_ArraysMyArrayInt32 _)
      val api_myArrayStruct_EventDataPort = reader.readOption(readGubmo_Structs_ArraysMyArrayStruct _)
      val api_myStructArray_EventDataPort = reader.readOption(readGubmo_Structs_ArraysMyStructArray_i _)
      val api_myArrayInt32_DataPort = readGubmo_Structs_ArraysMyArrayInt32()
      val api_myArrayStruct_DataPort = readGubmo_Structs_ArraysMyArrayStruct()
      val api_myStructArray_DataPort = readGubmo_Structs_ArraysMyStructArray_i()
      return Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P(api_myArrayInt32_EventDataPort, api_myArrayStruct_EventDataPort, api_myStructArray_EventDataPort, api_myArrayInt32_DataPort, api_myArrayStruct_DataPort, api_myStructArray_DataPort)
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS = {
      val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(F)
      return r
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(typeParsed: B): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS)
      }
      val In_myArrayInt32_StateVar = readGubmo_Structs_ArraysMyArrayInt32()
      val In_myArrayStruct_StateVar = readGubmo_Structs_ArraysMyArrayStruct()
      val In_myStructArray_StateVar = readGubmo_Structs_ArraysMyStructArray_i()
      val api_myArrayInt32_EventDataPort = reader.readOption(readGubmo_Structs_ArraysMyArrayInt32 _)
      val api_myArrayStruct_EventDataPort = reader.readOption(readGubmo_Structs_ArraysMyArrayStruct _)
      val api_myStructArray_EventDataPort = reader.readOption(readGubmo_Structs_ArraysMyStructArray_i _)
      val api_myArrayInt32_DataPort = readGubmo_Structs_ArraysMyArrayInt32()
      val api_myArrayStruct_DataPort = readGubmo_Structs_ArraysMyArrayStruct()
      val api_myStructArray_DataPort = readGubmo_Structs_ArraysMyStructArray_i()
      return Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS(In_myArrayInt32_StateVar, In_myArrayStruct_StateVar, In_myStructArray_StateVar, api_myArrayInt32_EventDataPort, api_myArrayStruct_EventDataPort, api_myStructArray_EventDataPort, api_myArrayInt32_DataPort, api_myArrayStruct_DataPort, api_myStructArray_DataPort)
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container.")
          val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T)
          return r
      }
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P = {
      val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(F)
      return r
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(typeParsed: B): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P)
      }
      return Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P()
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS = {
      val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(F)
      return r
    }

    def readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(typeParsed: B): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS)
      }
      val myArrayInt32_StateVar = readGubmo_Structs_ArraysMyArrayInt32()
      val myArrayStruct_StateVar = readGubmo_Structs_ArraysMyArrayStruct()
      val myStructArray_StateVar = readGubmo_Structs_ArraysMyStructArray_i()
      return Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS(myArrayInt32_StateVar, myArrayStruct_StateVar, myStructArray_StateVar)
    }

    def readruntimemonitorObservationKindType(): runtimemonitor.ObservationKind.Type = {
      val r = reader.readZ()
      return runtimemonitor.ObservationKind.byOrdinal(r).get
    }

    def readutilContainer(): util.Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.utilEmptyContainer => val r = readutilEmptyContainerT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of util.Container.")
          val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T)
          return r
      }
    }

    def readutilEmptyContainer(): util.EmptyContainer = {
      val r = readutilEmptyContainerT(F)
      return r
    }

    def readutilEmptyContainerT(typeParsed: B): util.EmptyContainer = {
      if (!typeParsed) {
        reader.expectZ(Constants.utilEmptyContainer)
      }
      return util.EmptyContainer()
    }

    def read_artDataContent(): art.DataContent = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants._artEmpty => val r = read_artEmptyT(T); return r
        case Constants.Base_TypesBoolean_Payload => val r = readBase_TypesBoolean_PayloadT(T); return r
        case Constants.Base_TypesInteger_Payload => val r = readBase_TypesInteger_PayloadT(T); return r
        case Constants.Base_TypesInteger_8_Payload => val r = readBase_TypesInteger_8_PayloadT(T); return r
        case Constants.Base_TypesInteger_16_Payload => val r = readBase_TypesInteger_16_PayloadT(T); return r
        case Constants.Base_TypesInteger_32_Payload => val r = readBase_TypesInteger_32_PayloadT(T); return r
        case Constants.Base_TypesInteger_64_Payload => val r = readBase_TypesInteger_64_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_8_Payload => val r = readBase_TypesUnsigned_8_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_16_Payload => val r = readBase_TypesUnsigned_16_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_32_Payload => val r = readBase_TypesUnsigned_32_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_64_Payload => val r = readBase_TypesUnsigned_64_PayloadT(T); return r
        case Constants.Base_TypesFloat_Payload => val r = readBase_TypesFloat_PayloadT(T); return r
        case Constants.Base_TypesFloat_32_Payload => val r = readBase_TypesFloat_32_PayloadT(T); return r
        case Constants.Base_TypesFloat_64_Payload => val r = readBase_TypesFloat_64_PayloadT(T); return r
        case Constants.Base_TypesCharacter_Payload => val r = readBase_TypesCharacter_PayloadT(T); return r
        case Constants.Base_TypesString_Payload => val r = readBase_TypesString_PayloadT(T); return r
        case Constants.Base_TypesBits_Payload => val r = readBase_TypesBits_PayloadT(T); return r
        case Constants.utilEmptyContainer => val r = readutilEmptyContainerT(T); return r
        case Constants.Gubmo_Structs_ArraysMyEnum_Payload => val r = readGubmo_Structs_ArraysMyEnum_PayloadT(T); return r
        case Constants.Gubmo_Structs_ArraysMyStruct2_i_Payload => val r = readGubmo_Structs_ArraysMyStruct2_i_PayloadT(T); return r
        case Constants.Gubmo_Structs_ArraysMyArrayInt32_Payload => val r = readGubmo_Structs_ArraysMyArrayInt32_PayloadT(T); return r
        case Constants.Gubmo_Structs_ArraysMyArrayStruct_Payload => val r = readGubmo_Structs_ArraysMyArrayStruct_PayloadT(T); return r
        case Constants.Gubmo_Structs_ArraysMyStructArray_i_Payload => val r = readGubmo_Structs_ArraysMyStructArray_i_PayloadT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS => val r = readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(T); return r
        case Constants.Gubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS => val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of art.DataContent.")
          val r = readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T)
          return r
      }
    }

    def read_artEmpty(): art.Empty = {
      val r = read_artEmptyT(F)
      return r
    }

    def read_artEmptyT(typeParsed: B): art.Empty = {
      if (!typeParsed) {
        reader.expectZ(Constants._artEmpty)
      }
      return art.Empty()
    }

  }

  def to[T](data: ISZ[U8], f: Reader => T): Either[T, MessagePack.ErrorMsg] = {
    val rd = Reader.Default(MessagePack.reader(data))
    rd.reader.init()
    val r = f(rd)
    rd.errorOpt match {
      case Some(e) => return Either.Right(e)
      case _ => return Either.Left(r)
    }
  }

  def fromGubmo_Structs_ArraysMyEnum_Payload(o: Gubmo_Structs_Arrays.MyEnum_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyEnum_Payload(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyEnum_Payload(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyEnum_Payload, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyEnum_Payload(reader: Reader): Gubmo_Structs_Arrays.MyEnum_Payload = {
      val r = reader.readGubmo_Structs_ArraysMyEnum_Payload()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyEnum_Payload _)
    return r
  }

  def fromGubmo_Structs_ArraysMyStruct2_i(o: Gubmo_Structs_Arrays.MyStruct2_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyStruct2_i(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyStruct2_i(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyStruct2_i, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyStruct2_i(reader: Reader): Gubmo_Structs_Arrays.MyStruct2_i = {
      val r = reader.readGubmo_Structs_ArraysMyStruct2_i()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyStruct2_i _)
    return r
  }

  def fromGubmo_Structs_ArraysMyStruct2_i_Payload(o: Gubmo_Structs_Arrays.MyStruct2_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyStruct2_i_Payload(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyStruct2_i_Payload(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyStruct2_i_Payload, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyStruct2_i_Payload(reader: Reader): Gubmo_Structs_Arrays.MyStruct2_i_Payload = {
      val r = reader.readGubmo_Structs_ArraysMyStruct2_i_Payload()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyStruct2_i_Payload _)
    return r
  }

  def fromGubmo_Structs_ArraysMyArrayInt32(o: Gubmo_Structs_Arrays.MyArrayInt32, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyArrayInt32(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyArrayInt32(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyArrayInt32, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyArrayInt32(reader: Reader): Gubmo_Structs_Arrays.MyArrayInt32 = {
      val r = reader.readGubmo_Structs_ArraysMyArrayInt32()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyArrayInt32 _)
    return r
  }

  def fromGubmo_Structs_ArraysMyArrayInt32_Payload(o: Gubmo_Structs_Arrays.MyArrayInt32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyArrayInt32_Payload(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyArrayInt32_Payload(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyArrayInt32_Payload, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyArrayInt32_Payload(reader: Reader): Gubmo_Structs_Arrays.MyArrayInt32_Payload = {
      val r = reader.readGubmo_Structs_ArraysMyArrayInt32_Payload()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyArrayInt32_Payload _)
    return r
  }

  def fromGubmo_Structs_ArraysMyArrayStruct(o: Gubmo_Structs_Arrays.MyArrayStruct, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyArrayStruct(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyArrayStruct(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyArrayStruct, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyArrayStruct(reader: Reader): Gubmo_Structs_Arrays.MyArrayStruct = {
      val r = reader.readGubmo_Structs_ArraysMyArrayStruct()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyArrayStruct _)
    return r
  }

  def fromGubmo_Structs_ArraysMyArrayStruct_Payload(o: Gubmo_Structs_Arrays.MyArrayStruct_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyArrayStruct_Payload(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyArrayStruct_Payload(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyArrayStruct_Payload, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyArrayStruct_Payload(reader: Reader): Gubmo_Structs_Arrays.MyArrayStruct_Payload = {
      val r = reader.readGubmo_Structs_ArraysMyArrayStruct_Payload()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyArrayStruct_Payload _)
    return r
  }

  def fromGubmo_Structs_ArraysMyStructArray_i(o: Gubmo_Structs_Arrays.MyStructArray_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyStructArray_i(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyStructArray_i(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyStructArray_i, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyStructArray_i(reader: Reader): Gubmo_Structs_Arrays.MyStructArray_i = {
      val r = reader.readGubmo_Structs_ArraysMyStructArray_i()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyStructArray_i _)
    return r
  }

  def fromGubmo_Structs_ArraysMyStructArray_i_Payload(o: Gubmo_Structs_Arrays.MyStructArray_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysMyStructArray_i_Payload(o)
    return w.result
  }

  def toGubmo_Structs_ArraysMyStructArray_i_Payload(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.MyStructArray_i_Payload, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysMyStructArray_i_Payload(reader: Reader): Gubmo_Structs_Arrays.MyStructArray_i_Payload = {
      val r = reader.readGubmo_Structs_ArraysMyStructArray_i_Payload()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysMyStructArray_i_Payload _)
    return r
  }

  def fromBase_TypesBoolean_Payload(o: Base_Types.Boolean_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesBoolean_Payload(o)
    return w.result
  }

  def toBase_TypesBoolean_Payload(data: ISZ[U8]): Either[Base_Types.Boolean_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesBoolean_Payload(reader: Reader): Base_Types.Boolean_Payload = {
      val r = reader.readBase_TypesBoolean_Payload()
      return r
    }
    val r = to(data, fBase_TypesBoolean_Payload _)
    return r
  }

  def fromBase_TypesInteger_Payload(o: Base_Types.Integer_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_Payload(data: ISZ[U8]): Either[Base_Types.Integer_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_Payload(reader: Reader): Base_Types.Integer_Payload = {
      val r = reader.readBase_TypesInteger_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_Payload _)
    return r
  }

  def fromBase_TypesInteger_8_Payload(o: Base_Types.Integer_8_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_8_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_8_Payload(data: ISZ[U8]): Either[Base_Types.Integer_8_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_8_Payload(reader: Reader): Base_Types.Integer_8_Payload = {
      val r = reader.readBase_TypesInteger_8_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_8_Payload _)
    return r
  }

  def fromBase_TypesInteger_16_Payload(o: Base_Types.Integer_16_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_16_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_16_Payload(data: ISZ[U8]): Either[Base_Types.Integer_16_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_16_Payload(reader: Reader): Base_Types.Integer_16_Payload = {
      val r = reader.readBase_TypesInteger_16_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_16_Payload _)
    return r
  }

  def fromBase_TypesInteger_32_Payload(o: Base_Types.Integer_32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_32_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_32_Payload(data: ISZ[U8]): Either[Base_Types.Integer_32_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_32_Payload(reader: Reader): Base_Types.Integer_32_Payload = {
      val r = reader.readBase_TypesInteger_32_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_32_Payload _)
    return r
  }

  def fromBase_TypesInteger_64_Payload(o: Base_Types.Integer_64_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_64_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_64_Payload(data: ISZ[U8]): Either[Base_Types.Integer_64_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_64_Payload(reader: Reader): Base_Types.Integer_64_Payload = {
      val r = reader.readBase_TypesInteger_64_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_64_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_8_Payload(o: Base_Types.Unsigned_8_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_8_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_8_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_8_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_8_Payload(reader: Reader): Base_Types.Unsigned_8_Payload = {
      val r = reader.readBase_TypesUnsigned_8_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_8_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_16_Payload(o: Base_Types.Unsigned_16_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_16_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_16_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_16_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_16_Payload(reader: Reader): Base_Types.Unsigned_16_Payload = {
      val r = reader.readBase_TypesUnsigned_16_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_16_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_32_Payload(o: Base_Types.Unsigned_32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_32_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_32_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_32_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_32_Payload(reader: Reader): Base_Types.Unsigned_32_Payload = {
      val r = reader.readBase_TypesUnsigned_32_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_32_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_64_Payload(o: Base_Types.Unsigned_64_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_64_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_64_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_64_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_64_Payload(reader: Reader): Base_Types.Unsigned_64_Payload = {
      val r = reader.readBase_TypesUnsigned_64_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_64_Payload _)
    return r
  }

  def fromBase_TypesFloat_Payload(o: Base_Types.Float_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesFloat_Payload(o)
    return w.result
  }

  def toBase_TypesFloat_Payload(data: ISZ[U8]): Either[Base_Types.Float_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesFloat_Payload(reader: Reader): Base_Types.Float_Payload = {
      val r = reader.readBase_TypesFloat_Payload()
      return r
    }
    val r = to(data, fBase_TypesFloat_Payload _)
    return r
  }

  def fromBase_TypesFloat_32_Payload(o: Base_Types.Float_32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesFloat_32_Payload(o)
    return w.result
  }

  def toBase_TypesFloat_32_Payload(data: ISZ[U8]): Either[Base_Types.Float_32_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesFloat_32_Payload(reader: Reader): Base_Types.Float_32_Payload = {
      val r = reader.readBase_TypesFloat_32_Payload()
      return r
    }
    val r = to(data, fBase_TypesFloat_32_Payload _)
    return r
  }

  def fromBase_TypesFloat_64_Payload(o: Base_Types.Float_64_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesFloat_64_Payload(o)
    return w.result
  }

  def toBase_TypesFloat_64_Payload(data: ISZ[U8]): Either[Base_Types.Float_64_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesFloat_64_Payload(reader: Reader): Base_Types.Float_64_Payload = {
      val r = reader.readBase_TypesFloat_64_Payload()
      return r
    }
    val r = to(data, fBase_TypesFloat_64_Payload _)
    return r
  }

  def fromBase_TypesCharacter_Payload(o: Base_Types.Character_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesCharacter_Payload(o)
    return w.result
  }

  def toBase_TypesCharacter_Payload(data: ISZ[U8]): Either[Base_Types.Character_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesCharacter_Payload(reader: Reader): Base_Types.Character_Payload = {
      val r = reader.readBase_TypesCharacter_Payload()
      return r
    }
    val r = to(data, fBase_TypesCharacter_Payload _)
    return r
  }

  def fromBase_TypesString_Payload(o: Base_Types.String_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesString_Payload(o)
    return w.result
  }

  def toBase_TypesString_Payload(data: ISZ[U8]): Either[Base_Types.String_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesString_Payload(reader: Reader): Base_Types.String_Payload = {
      val r = reader.readBase_TypesString_Payload()
      return r
    }
    val r = to(data, fBase_TypesString_Payload _)
    return r
  }

  def fromBase_TypesBits_Payload(o: Base_Types.Bits_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesBits_Payload(o)
    return w.result
  }

  def toBase_TypesBits_Payload(data: ISZ[U8]): Either[Base_Types.Bits_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesBits_Payload(reader: Reader): Base_Types.Bits_Payload = {
      val r = reader.readBase_TypesBits_Payload()
      return r
    }
    val r = to(data, fBase_TypesBits_Payload _)
    return r
  }

  def fromGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(o)
    return w.result
  }

  def toGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(reader: Reader): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container = {
      val r = reader.readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container _)
    return r
  }

  def fromGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
    return w.result
  }

  def toGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(reader: Reader): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P = {
      val r = reader.readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P _)
    return r
  }

  def fromGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
    return w.result
  }

  def toGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(reader: Reader): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS = {
      val r = reader.readGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS _)
    return r
  }

  def fromGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(o)
    return w.result
  }

  def toGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(reader: Reader): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container = {
      val r = reader.readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container _)
    return r
  }

  def fromGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
    return w.result
  }

  def toGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(reader: Reader): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P = {
      val r = reader.readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P _)
    return r
  }

  def fromGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o: Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
    return w.result
  }

  def toGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(reader: Reader): Gubmo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS = {
      val r = reader.readGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS _)
    return r
  }

  def fromGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(o)
    return w.result
  }

  def toGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(reader: Reader): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container = {
      val r = reader.readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container _)
    return r
  }

  def fromGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
    return w.result
  }

  def toGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(reader: Reader): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P = {
      val r = reader.readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P _)
    return r
  }

  def fromGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
    return w.result
  }

  def toGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(reader: Reader): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS = {
      val r = reader.readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS _)
    return r
  }

  def fromGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(o)
    return w.result
  }

  def toGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(reader: Reader): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container = {
      val r = reader.readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container _)
    return r
  }

  def fromGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
    return w.result
  }

  def toGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(reader: Reader): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P = {
      val r = reader.readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P _)
    return r
  }

  def fromGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o: Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
    return w.result
  }

  def toGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(data: ISZ[U8]): Either[Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(reader: Reader): Gubmo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS = {
      val r = reader.readGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS()
      return r
    }
    val r = to(data, fGubmo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS _)
    return r
  }

  def fromutilContainer(o: util.Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeutilContainer(o)
    return w.result
  }

  def toutilContainer(data: ISZ[U8]): Either[util.Container, MessagePack.ErrorMsg] = {
    def futilContainer(reader: Reader): util.Container = {
      val r = reader.readutilContainer()
      return r
    }
    val r = to(data, futilContainer _)
    return r
  }

  def fromutilEmptyContainer(o: util.EmptyContainer, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeutilEmptyContainer(o)
    return w.result
  }

  def toutilEmptyContainer(data: ISZ[U8]): Either[util.EmptyContainer, MessagePack.ErrorMsg] = {
    def futilEmptyContainer(reader: Reader): util.EmptyContainer = {
      val r = reader.readutilEmptyContainer()
      return r
    }
    val r = to(data, futilEmptyContainer _)
    return r
  }

  def from_artDataContent(o: art.DataContent, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.write_artDataContent(o)
    return w.result
  }

  def to_artDataContent(data: ISZ[U8]): Either[art.DataContent, MessagePack.ErrorMsg] = {
    def f_artDataContent(reader: Reader): art.DataContent = {
      val r = reader.read_artDataContent()
      return r
    }
    val r = to(data, f_artDataContent _)
    return r
  }

  def from_artEmpty(o: art.Empty, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.write_artEmpty(o)
    return w.result
  }

  def to_artEmpty(data: ISZ[U8]): Either[art.Empty, MessagePack.ErrorMsg] = {
    def f_artEmpty(reader: Reader): art.Empty = {
      val r = reader.read_artEmpty()
      return r
    }
    val r = to(data, f_artEmpty _)
    return r
  }

}