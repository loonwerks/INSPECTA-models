// #Sireum
// @formatter:off

// This file is auto-generated from MyEnum.scala, MyStruct2_i.scala, MyArrayInt32.scala, MyArrayStruct.scala, MyStructArray_i.scala, Base_Types.scala, GUMBO__Library.scala, ProducerThr_i_producer_producer_Containers.scala, ConsumerThr_i_consumer_consumer_Containers.scala, ObservationKind.scala, Container.scala, DataContent.scala, Aux_Types.scala

package base

import org.sireum._
import org.sireum.Json.Printer._

object JSON {

  object Printer {

    @pure def printGumbo_Structs_ArraysMyEnumType(o: Gumbo_Structs_Arrays.MyEnum.Type): ST = {
      val value: String = o match {
        case Gumbo_Structs_Arrays.MyEnum.On => "On"
        case Gumbo_Structs_Arrays.MyEnum.Off => "Off"
      }
      return printObject(ISZ(
        ("type", printString("Gumbo_Structs_Arrays.MyEnum")),
        ("value", printString(value))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyEnum_Payload(o: Gumbo_Structs_Arrays.MyEnum_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyEnum_Payload""""),
        ("value", printGumbo_Structs_ArraysMyEnumType(o.value))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyStruct2_i(o: Gumbo_Structs_Arrays.MyStruct2_i): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyStruct2_i""""),
        ("fieldSInt32", printS32(o.fieldSInt32))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyStruct2_i_Payload(o: Gumbo_Structs_Arrays.MyStruct2_i_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyStruct2_i_Payload""""),
        ("value", printGumbo_Structs_ArraysMyStruct2_i(o.value))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyArrayInt32I(o: Gumbo_Structs_Arrays.MyArrayInt32.I): ST = {
      return printNumber(o.toZ.string)
    }

    @pure def printGumbo_Structs_ArraysMyArrayInt32(o: Gumbo_Structs_Arrays.MyArrayInt32): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyArrayInt32""""),
        ("value", printIS(T, o.value.map(printS32 _)))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyArrayInt32_Payload(o: Gumbo_Structs_Arrays.MyArrayInt32_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyArrayInt32_Payload""""),
        ("value", printGumbo_Structs_ArraysMyArrayInt32(o.value))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyArrayStructI(o: Gumbo_Structs_Arrays.MyArrayStruct.I): ST = {
      return printNumber(o.toZ.string)
    }

    @pure def printGumbo_Structs_ArraysMyArrayStruct(o: Gumbo_Structs_Arrays.MyArrayStruct): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyArrayStruct""""),
        ("value", printIS(F, o.value.map(printGumbo_Structs_ArraysMyStruct2_i _)))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyArrayStruct_Payload(o: Gumbo_Structs_Arrays.MyArrayStruct_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyArrayStruct_Payload""""),
        ("value", printGumbo_Structs_ArraysMyArrayStruct(o.value))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyStructArray_i(o: Gumbo_Structs_Arrays.MyStructArray_i): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyStructArray_i""""),
        ("fieldInt64", printS64(o.fieldInt64)),
        ("fieldEnum", printGumbo_Structs_ArraysMyEnumType(o.fieldEnum)),
        ("fieldRec", printGumbo_Structs_ArraysMyStruct2_i(o.fieldRec)),
        ("fieldArray", printGumbo_Structs_ArraysMyArrayStruct(o.fieldArray))
      ))
    }

    @pure def printGumbo_Structs_ArraysMyStructArray_i_Payload(o: Gumbo_Structs_Arrays.MyStructArray_i_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.MyStructArray_i_Payload""""),
        ("value", printGumbo_Structs_ArraysMyStructArray_i(o.value))
      ))
    }

    @pure def printBase_TypesBoolean_Payload(o: Base_Types.Boolean_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Boolean_Payload""""),
        ("value", printB(o.value))
      ))
    }

    @pure def printBase_TypesInteger_Payload(o: Base_Types.Integer_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Integer_Payload""""),
        ("value", printZ(o.value))
      ))
    }

    @pure def printBase_TypesInteger_8_Payload(o: Base_Types.Integer_8_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Integer_8_Payload""""),
        ("value", printS8(o.value))
      ))
    }

    @pure def printBase_TypesInteger_16_Payload(o: Base_Types.Integer_16_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Integer_16_Payload""""),
        ("value", printS16(o.value))
      ))
    }

    @pure def printBase_TypesInteger_32_Payload(o: Base_Types.Integer_32_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Integer_32_Payload""""),
        ("value", printS32(o.value))
      ))
    }

    @pure def printBase_TypesInteger_64_Payload(o: Base_Types.Integer_64_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Integer_64_Payload""""),
        ("value", printS64(o.value))
      ))
    }

    @pure def printBase_TypesUnsigned_8_Payload(o: Base_Types.Unsigned_8_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Unsigned_8_Payload""""),
        ("value", printU8(o.value))
      ))
    }

    @pure def printBase_TypesUnsigned_16_Payload(o: Base_Types.Unsigned_16_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Unsigned_16_Payload""""),
        ("value", printU16(o.value))
      ))
    }

    @pure def printBase_TypesUnsigned_32_Payload(o: Base_Types.Unsigned_32_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Unsigned_32_Payload""""),
        ("value", printU32(o.value))
      ))
    }

    @pure def printBase_TypesUnsigned_64_Payload(o: Base_Types.Unsigned_64_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Unsigned_64_Payload""""),
        ("value", printU64(o.value))
      ))
    }

    @pure def printBase_TypesFloat_Payload(o: Base_Types.Float_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Float_Payload""""),
        ("value", printR(o.value))
      ))
    }

    @pure def printBase_TypesFloat_32_Payload(o: Base_Types.Float_32_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Float_32_Payload""""),
        ("value", printF32(o.value))
      ))
    }

    @pure def printBase_TypesFloat_64_Payload(o: Base_Types.Float_64_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Float_64_Payload""""),
        ("value", printF64(o.value))
      ))
    }

    @pure def printBase_TypesCharacter_Payload(o: Base_Types.Character_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Character_Payload""""),
        ("value", printC(o.value))
      ))
    }

    @pure def printBase_TypesString_Payload(o: Base_Types.String_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.String_Payload""""),
        ("value", printString(o.value))
      ))
    }

    @pure def printBase_TypesBits_Payload(o: Base_Types.Bits_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""Base_Types.Bits_Payload""""),
        ("value", printISZ(T, o.value, printB _))
      ))
    }

    @pure def printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container): ST = {
      o match {
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
      }
    }

    @pure def printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P"""")
      ))
    }

    @pure def printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS"""")
      ))
    }

    @pure def printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container): ST = {
      o match {
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
      }
    }

    @pure def printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P""""),
        ("api_MyArrayStruct", printOption(F, o.api_MyArrayStruct, printGumbo_Structs_ArraysMyArrayStruct _)),
        ("api_myStructArray", printOption(F, o.api_myStructArray, printGumbo_Structs_ArraysMyStructArray_i _))
      ))
    }

    @pure def printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS""""),
        ("api_MyArrayStruct", printOption(F, o.api_MyArrayStruct, printGumbo_Structs_ArraysMyArrayStruct _)),
        ("api_myStructArray", printOption(F, o.api_myStructArray, printGumbo_Structs_ArraysMyStructArray_i _))
      ))
    }

    @pure def printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container): ST = {
      o match {
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
      }
    }

    @pure def printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P""""),
        ("api_MyArrayInt32", printOption(F, o.api_MyArrayInt32, printGumbo_Structs_ArraysMyArrayInt32 _)),
        ("api_MyArrayStruct", printOption(F, o.api_MyArrayStruct, printGumbo_Structs_ArraysMyArrayStruct _)),
        ("api_myStructArray", printOption(F, o.api_myStructArray, printGumbo_Structs_ArraysMyStructArray_i _))
      ))
    }

    @pure def printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS""""),
        ("api_MyArrayInt32", printOption(F, o.api_MyArrayInt32, printGumbo_Structs_ArraysMyArrayInt32 _)),
        ("api_MyArrayStruct", printOption(F, o.api_MyArrayStruct, printGumbo_Structs_ArraysMyArrayStruct _)),
        ("api_myStructArray", printOption(F, o.api_myStructArray, printGumbo_Structs_ArraysMyStructArray_i _))
      ))
    }

    @pure def printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container): ST = {
      o match {
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
      }
    }

    @pure def printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P"""")
      ))
    }

    @pure def printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS"""")
      ))
    }

    @pure def printruntimemonitorObservationKindType(o: runtimemonitor.ObservationKind.Type): ST = {
      val value: String = o match {
        case runtimemonitor.ObservationKind.Sys_i_Instance_producer_producer_postInit => "Sys_i_Instance_producer_producer_postInit"
        case runtimemonitor.ObservationKind.Sys_i_Instance_producer_producer_preCompute => "Sys_i_Instance_producer_producer_preCompute"
        case runtimemonitor.ObservationKind.Sys_i_Instance_producer_producer_postCompute => "Sys_i_Instance_producer_producer_postCompute"
        case runtimemonitor.ObservationKind.Sys_i_Instance_consumer_consumer_postInit => "Sys_i_Instance_consumer_consumer_postInit"
        case runtimemonitor.ObservationKind.Sys_i_Instance_consumer_consumer_preCompute => "Sys_i_Instance_consumer_consumer_preCompute"
        case runtimemonitor.ObservationKind.Sys_i_Instance_consumer_consumer_postCompute => "Sys_i_Instance_consumer_consumer_postCompute"
      }
      return printObject(ISZ(
        ("type", printString("runtimemonitor.ObservationKind")),
        ("value", printString(value))
      ))
    }

    @pure def printutilContainer(o: util.Container): ST = {
      o match {
        case o: util.EmptyContainer => return printutilEmptyContainer(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
      }
    }

    @pure def printutilEmptyContainer(o: util.EmptyContainer): ST = {
      return printObject(ISZ(
        ("type", st""""util.EmptyContainer"""")
      ))
    }

    @pure def print_artDataContent(o: art.DataContent): ST = {
      o match {
        case o: art.Empty => return print_artEmpty(o)
        case o: Base_Types.Boolean_Payload => return printBase_TypesBoolean_Payload(o)
        case o: Base_Types.Integer_Payload => return printBase_TypesInteger_Payload(o)
        case o: Base_Types.Integer_8_Payload => return printBase_TypesInteger_8_Payload(o)
        case o: Base_Types.Integer_16_Payload => return printBase_TypesInteger_16_Payload(o)
        case o: Base_Types.Integer_32_Payload => return printBase_TypesInteger_32_Payload(o)
        case o: Base_Types.Integer_64_Payload => return printBase_TypesInteger_64_Payload(o)
        case o: Base_Types.Unsigned_8_Payload => return printBase_TypesUnsigned_8_Payload(o)
        case o: Base_Types.Unsigned_16_Payload => return printBase_TypesUnsigned_16_Payload(o)
        case o: Base_Types.Unsigned_32_Payload => return printBase_TypesUnsigned_32_Payload(o)
        case o: Base_Types.Unsigned_64_Payload => return printBase_TypesUnsigned_64_Payload(o)
        case o: Base_Types.Float_Payload => return printBase_TypesFloat_Payload(o)
        case o: Base_Types.Float_32_Payload => return printBase_TypesFloat_32_Payload(o)
        case o: Base_Types.Float_64_Payload => return printBase_TypesFloat_64_Payload(o)
        case o: Base_Types.Character_Payload => return printBase_TypesCharacter_Payload(o)
        case o: Base_Types.String_Payload => return printBase_TypesString_Payload(o)
        case o: Base_Types.Bits_Payload => return printBase_TypesBits_Payload(o)
        case o: util.EmptyContainer => return printutilEmptyContainer(o)
        case o: Gumbo_Structs_Arrays.MyEnum_Payload => return printGumbo_Structs_ArraysMyEnum_Payload(o)
        case o: Gumbo_Structs_Arrays.MyStruct2_i_Payload => return printGumbo_Structs_ArraysMyStruct2_i_Payload(o)
        case o: Gumbo_Structs_Arrays.MyArrayInt32_Payload => return printGumbo_Structs_ArraysMyArrayInt32_Payload(o)
        case o: Gumbo_Structs_Arrays.MyArrayStruct_Payload => return printGumbo_Structs_ArraysMyArrayStruct_Payload(o)
        case o: Gumbo_Structs_Arrays.MyStructArray_i_Payload => return printGumbo_Structs_ArraysMyStructArray_i_Payload(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
        case o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS => return printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
        case o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS => return printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
      }
    }

    @pure def print_artEmpty(o: art.Empty): ST = {
      return printObject(ISZ(
        ("type", st""""art.Empty"""")
      ))
    }

  }

  @record class Parser(val input: String) {
    val parser: Json.Parser = Json.Parser.create(input)

    def errorOpt: Option[Json.ErrorMsg] = {
      return parser.errorOpt
    }

    def parseGumbo_Structs_ArraysMyEnumType(): Gumbo_Structs_Arrays.MyEnum.Type = {
      val r = parseGumbo_Structs_ArraysMyEnumT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyEnumT(typeParsed: B): Gumbo_Structs_Arrays.MyEnum.Type = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyEnum")
      }
      parser.parseObjectKey("value")
      var i = parser.offset
      val s = parser.parseString()
      parser.parseObjectNext()
      Gumbo_Structs_Arrays.MyEnum.byName(s) match {
        case Some(r) => return r
        case _ =>
          parser.parseException(i, s"Invalid element name '$s' for Gumbo_Structs_Arrays.MyEnum.")
          return Gumbo_Structs_Arrays.MyEnum.byOrdinal(0).get
      }
    }

    def parseGumbo_Structs_ArraysMyEnum_Payload(): Gumbo_Structs_Arrays.MyEnum_Payload = {
      val r = parseGumbo_Structs_ArraysMyEnum_PayloadT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyEnum_PayloadT(typeParsed: B): Gumbo_Structs_Arrays.MyEnum_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyEnum_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseGumbo_Structs_ArraysMyEnumType()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyEnum_Payload(value)
    }

    def parseGumbo_Structs_ArraysMyStruct2_i(): Gumbo_Structs_Arrays.MyStruct2_i = {
      val r = parseGumbo_Structs_ArraysMyStruct2_iT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyStruct2_iT(typeParsed: B): Gumbo_Structs_Arrays.MyStruct2_i = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyStruct2_i")
      }
      parser.parseObjectKey("fieldSInt32")
      val fieldSInt32 = parser.parseS32()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyStruct2_i(fieldSInt32)
    }

    def parseGumbo_Structs_ArraysMyStruct2_i_Payload(): Gumbo_Structs_Arrays.MyStruct2_i_Payload = {
      val r = parseGumbo_Structs_ArraysMyStruct2_i_PayloadT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyStruct2_i_PayloadT(typeParsed: B): Gumbo_Structs_Arrays.MyStruct2_i_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyStruct2_i_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseGumbo_Structs_ArraysMyStruct2_i()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyStruct2_i_Payload(value)
    }

    def parseGumbo_Structs_ArraysMyArrayInt32I(): Gumbo_Structs_Arrays.MyArrayInt32.I = {
      val i = parser.offset
      val s = parser.parseNumber()
      Gumbo_Structs_Arrays.MyArrayInt32.I(s) match {
        case Some(n) => return n
        case _ =>
          parser.parseException(i, s"Expected a Gumbo_Structs_Arrays.MyArrayInt32.I, but '$s' found.")
          return Gumbo_Structs_Arrays.MyArrayInt32.I.Min
      }
    }

    def parseISGumbo_Structs_ArraysMyArrayInt32I[T](f: () => T): IS[Gumbo_Structs_Arrays.MyArrayInt32.I, T] = {
      if (!parser.parseArrayBegin()) {
        return IS()
      }
      var e = f()
      var r = IS[Gumbo_Structs_Arrays.MyArrayInt32.I, T](e)
      var continue = parser.parseArrayNext()
      while (continue) {
        e = f()
        r = r :+ e
        continue = parser.parseArrayNext()
      }
      return r
    }

    def parseGumbo_Structs_ArraysMyArrayInt32(): Gumbo_Structs_Arrays.MyArrayInt32 = {
      val r = parseGumbo_Structs_ArraysMyArrayInt32T(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyArrayInt32T(typeParsed: B): Gumbo_Structs_Arrays.MyArrayInt32 = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyArrayInt32")
      }
      parser.parseObjectKey("value")
      val value = parseISGumbo_Structs_ArraysMyArrayInt32I(parser.parseS32 _)
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyArrayInt32(value)
    }

    def parseGumbo_Structs_ArraysMyArrayInt32_Payload(): Gumbo_Structs_Arrays.MyArrayInt32_Payload = {
      val r = parseGumbo_Structs_ArraysMyArrayInt32_PayloadT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyArrayInt32_PayloadT(typeParsed: B): Gumbo_Structs_Arrays.MyArrayInt32_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyArrayInt32_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseGumbo_Structs_ArraysMyArrayInt32()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyArrayInt32_Payload(value)
    }

    def parseGumbo_Structs_ArraysMyArrayStructI(): Gumbo_Structs_Arrays.MyArrayStruct.I = {
      val i = parser.offset
      val s = parser.parseNumber()
      Gumbo_Structs_Arrays.MyArrayStruct.I(s) match {
        case Some(n) => return n
        case _ =>
          parser.parseException(i, s"Expected a Gumbo_Structs_Arrays.MyArrayStruct.I, but '$s' found.")
          return Gumbo_Structs_Arrays.MyArrayStruct.I.Min
      }
    }

    def parseISGumbo_Structs_ArraysMyArrayStructI[T](f: () => T): IS[Gumbo_Structs_Arrays.MyArrayStruct.I, T] = {
      if (!parser.parseArrayBegin()) {
        return IS()
      }
      var e = f()
      var r = IS[Gumbo_Structs_Arrays.MyArrayStruct.I, T](e)
      var continue = parser.parseArrayNext()
      while (continue) {
        e = f()
        r = r :+ e
        continue = parser.parseArrayNext()
      }
      return r
    }

    def parseGumbo_Structs_ArraysMyArrayStruct(): Gumbo_Structs_Arrays.MyArrayStruct = {
      val r = parseGumbo_Structs_ArraysMyArrayStructT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyArrayStructT(typeParsed: B): Gumbo_Structs_Arrays.MyArrayStruct = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyArrayStruct")
      }
      parser.parseObjectKey("value")
      val value = parseISGumbo_Structs_ArraysMyArrayStructI(parseGumbo_Structs_ArraysMyStruct2_i _)
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyArrayStruct(value)
    }

    def parseGumbo_Structs_ArraysMyArrayStruct_Payload(): Gumbo_Structs_Arrays.MyArrayStruct_Payload = {
      val r = parseGumbo_Structs_ArraysMyArrayStruct_PayloadT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyArrayStruct_PayloadT(typeParsed: B): Gumbo_Structs_Arrays.MyArrayStruct_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyArrayStruct_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseGumbo_Structs_ArraysMyArrayStruct()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyArrayStruct_Payload(value)
    }

    def parseGumbo_Structs_ArraysMyStructArray_i(): Gumbo_Structs_Arrays.MyStructArray_i = {
      val r = parseGumbo_Structs_ArraysMyStructArray_iT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyStructArray_iT(typeParsed: B): Gumbo_Structs_Arrays.MyStructArray_i = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyStructArray_i")
      }
      parser.parseObjectKey("fieldInt64")
      val fieldInt64 = parser.parseS64()
      parser.parseObjectNext()
      parser.parseObjectKey("fieldEnum")
      val fieldEnum = parseGumbo_Structs_ArraysMyEnumType()
      parser.parseObjectNext()
      parser.parseObjectKey("fieldRec")
      val fieldRec = parseGumbo_Structs_ArraysMyStruct2_i()
      parser.parseObjectNext()
      parser.parseObjectKey("fieldArray")
      val fieldArray = parseGumbo_Structs_ArraysMyArrayStruct()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyStructArray_i(fieldInt64, fieldEnum, fieldRec, fieldArray)
    }

    def parseGumbo_Structs_ArraysMyStructArray_i_Payload(): Gumbo_Structs_Arrays.MyStructArray_i_Payload = {
      val r = parseGumbo_Structs_ArraysMyStructArray_i_PayloadT(F)
      return r
    }

    def parseGumbo_Structs_ArraysMyStructArray_i_PayloadT(typeParsed: B): Gumbo_Structs_Arrays.MyStructArray_i_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.MyStructArray_i_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseGumbo_Structs_ArraysMyStructArray_i()
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.MyStructArray_i_Payload(value)
    }

    def parseBase_TypesBoolean_Payload(): Base_Types.Boolean_Payload = {
      val r = parseBase_TypesBoolean_PayloadT(F)
      return r
    }

    def parseBase_TypesBoolean_PayloadT(typeParsed: B): Base_Types.Boolean_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Boolean_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseB()
      parser.parseObjectNext()
      return Base_Types.Boolean_Payload(value)
    }

    def parseBase_TypesInteger_Payload(): Base_Types.Integer_Payload = {
      val r = parseBase_TypesInteger_PayloadT(F)
      return r
    }

    def parseBase_TypesInteger_PayloadT(typeParsed: B): Base_Types.Integer_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Integer_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseZ()
      parser.parseObjectNext()
      return Base_Types.Integer_Payload(value)
    }

    def parseBase_TypesInteger_8_Payload(): Base_Types.Integer_8_Payload = {
      val r = parseBase_TypesInteger_8_PayloadT(F)
      return r
    }

    def parseBase_TypesInteger_8_PayloadT(typeParsed: B): Base_Types.Integer_8_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Integer_8_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseS8()
      parser.parseObjectNext()
      return Base_Types.Integer_8_Payload(value)
    }

    def parseBase_TypesInteger_16_Payload(): Base_Types.Integer_16_Payload = {
      val r = parseBase_TypesInteger_16_PayloadT(F)
      return r
    }

    def parseBase_TypesInteger_16_PayloadT(typeParsed: B): Base_Types.Integer_16_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Integer_16_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseS16()
      parser.parseObjectNext()
      return Base_Types.Integer_16_Payload(value)
    }

    def parseBase_TypesInteger_32_Payload(): Base_Types.Integer_32_Payload = {
      val r = parseBase_TypesInteger_32_PayloadT(F)
      return r
    }

    def parseBase_TypesInteger_32_PayloadT(typeParsed: B): Base_Types.Integer_32_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Integer_32_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseS32()
      parser.parseObjectNext()
      return Base_Types.Integer_32_Payload(value)
    }

    def parseBase_TypesInteger_64_Payload(): Base_Types.Integer_64_Payload = {
      val r = parseBase_TypesInteger_64_PayloadT(F)
      return r
    }

    def parseBase_TypesInteger_64_PayloadT(typeParsed: B): Base_Types.Integer_64_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Integer_64_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseS64()
      parser.parseObjectNext()
      return Base_Types.Integer_64_Payload(value)
    }

    def parseBase_TypesUnsigned_8_Payload(): Base_Types.Unsigned_8_Payload = {
      val r = parseBase_TypesUnsigned_8_PayloadT(F)
      return r
    }

    def parseBase_TypesUnsigned_8_PayloadT(typeParsed: B): Base_Types.Unsigned_8_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Unsigned_8_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseU8()
      parser.parseObjectNext()
      return Base_Types.Unsigned_8_Payload(value)
    }

    def parseBase_TypesUnsigned_16_Payload(): Base_Types.Unsigned_16_Payload = {
      val r = parseBase_TypesUnsigned_16_PayloadT(F)
      return r
    }

    def parseBase_TypesUnsigned_16_PayloadT(typeParsed: B): Base_Types.Unsigned_16_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Unsigned_16_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseU16()
      parser.parseObjectNext()
      return Base_Types.Unsigned_16_Payload(value)
    }

    def parseBase_TypesUnsigned_32_Payload(): Base_Types.Unsigned_32_Payload = {
      val r = parseBase_TypesUnsigned_32_PayloadT(F)
      return r
    }

    def parseBase_TypesUnsigned_32_PayloadT(typeParsed: B): Base_Types.Unsigned_32_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Unsigned_32_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseU32()
      parser.parseObjectNext()
      return Base_Types.Unsigned_32_Payload(value)
    }

    def parseBase_TypesUnsigned_64_Payload(): Base_Types.Unsigned_64_Payload = {
      val r = parseBase_TypesUnsigned_64_PayloadT(F)
      return r
    }

    def parseBase_TypesUnsigned_64_PayloadT(typeParsed: B): Base_Types.Unsigned_64_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Unsigned_64_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseU64()
      parser.parseObjectNext()
      return Base_Types.Unsigned_64_Payload(value)
    }

    def parseBase_TypesFloat_Payload(): Base_Types.Float_Payload = {
      val r = parseBase_TypesFloat_PayloadT(F)
      return r
    }

    def parseBase_TypesFloat_PayloadT(typeParsed: B): Base_Types.Float_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Float_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseR()
      parser.parseObjectNext()
      return Base_Types.Float_Payload(value)
    }

    def parseBase_TypesFloat_32_Payload(): Base_Types.Float_32_Payload = {
      val r = parseBase_TypesFloat_32_PayloadT(F)
      return r
    }

    def parseBase_TypesFloat_32_PayloadT(typeParsed: B): Base_Types.Float_32_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Float_32_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseF32()
      parser.parseObjectNext()
      return Base_Types.Float_32_Payload(value)
    }

    def parseBase_TypesFloat_64_Payload(): Base_Types.Float_64_Payload = {
      val r = parseBase_TypesFloat_64_PayloadT(F)
      return r
    }

    def parseBase_TypesFloat_64_PayloadT(typeParsed: B): Base_Types.Float_64_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Float_64_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseF64()
      parser.parseObjectNext()
      return Base_Types.Float_64_Payload(value)
    }

    def parseBase_TypesCharacter_Payload(): Base_Types.Character_Payload = {
      val r = parseBase_TypesCharacter_PayloadT(F)
      return r
    }

    def parseBase_TypesCharacter_PayloadT(typeParsed: B): Base_Types.Character_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Character_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseC()
      parser.parseObjectNext()
      return Base_Types.Character_Payload(value)
    }

    def parseBase_TypesString_Payload(): Base_Types.String_Payload = {
      val r = parseBase_TypesString_PayloadT(F)
      return r
    }

    def parseBase_TypesString_PayloadT(typeParsed: B): Base_Types.String_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.String_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseString()
      parser.parseObjectNext()
      return Base_Types.String_Payload(value)
    }

    def parseBase_TypesBits_Payload(): Base_Types.Bits_Payload = {
      val r = parseBase_TypesBits_PayloadT(F)
      return r
    }

    def parseBase_TypesBits_PayloadT(typeParsed: B): Base_Types.Bits_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("Base_Types.Bits_Payload")
      }
      parser.parseObjectKey("value")
      val value = parser.parseISZ(parser.parseB _)
      parser.parseObjectNext()
      return Base_Types.Bits_Payload(value)
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS"))
      t.native match {
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
        case _ => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
      }
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P = {
      val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(F)
      return r
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(typeParsed: B): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P")
      }
      return Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P()
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS = {
      val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(F)
      return r
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(typeParsed: B): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS")
      }
      return Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS()
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS"))
      t.native match {
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
        case _ => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
      }
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P = {
      val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(F)
      return r
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(typeParsed: B): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P")
      }
      parser.parseObjectKey("api_MyArrayStruct")
      val api_MyArrayStruct = parser.parseOption(parseGumbo_Structs_ArraysMyArrayStruct _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_myStructArray")
      val api_myStructArray = parser.parseOption(parseGumbo_Structs_ArraysMyStructArray_i _)
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P(api_MyArrayStruct, api_myStructArray)
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS = {
      val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(F)
      return r
    }

    def parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(typeParsed: B): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS")
      }
      parser.parseObjectKey("api_MyArrayStruct")
      val api_MyArrayStruct = parser.parseOption(parseGumbo_Structs_ArraysMyArrayStruct _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_myStructArray")
      val api_myStructArray = parser.parseOption(parseGumbo_Structs_ArraysMyStructArray_i _)
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS(api_MyArrayStruct, api_myStructArray)
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS"))
      t.native match {
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
        case _ => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
      }
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P = {
      val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(F)
      return r
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(typeParsed: B): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P")
      }
      parser.parseObjectKey("api_MyArrayInt32")
      val api_MyArrayInt32 = parser.parseOption(parseGumbo_Structs_ArraysMyArrayInt32 _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_MyArrayStruct")
      val api_MyArrayStruct = parser.parseOption(parseGumbo_Structs_ArraysMyArrayStruct _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_myStructArray")
      val api_myStructArray = parser.parseOption(parseGumbo_Structs_ArraysMyStructArray_i _)
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P(api_MyArrayInt32, api_MyArrayStruct, api_myStructArray)
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS = {
      val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(F)
      return r
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(typeParsed: B): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS")
      }
      parser.parseObjectKey("api_MyArrayInt32")
      val api_MyArrayInt32 = parser.parseOption(parseGumbo_Structs_ArraysMyArrayInt32 _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_MyArrayStruct")
      val api_MyArrayStruct = parser.parseOption(parseGumbo_Structs_ArraysMyArrayStruct _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_myStructArray")
      val api_myStructArray = parser.parseOption(parseGumbo_Structs_ArraysMyStructArray_i _)
      parser.parseObjectNext()
      return Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS(api_MyArrayInt32, api_MyArrayStruct, api_myStructArray)
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS"))
      t.native match {
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
        case _ => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
      }
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P = {
      val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(F)
      return r
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(typeParsed: B): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P")
      }
      return Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P()
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS = {
      val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(F)
      return r
    }

    def parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(typeParsed: B): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS")
      }
      return Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS()
    }

    def parseruntimemonitorObservationKindType(): runtimemonitor.ObservationKind.Type = {
      val r = parseruntimemonitorObservationKindT(F)
      return r
    }

    def parseruntimemonitorObservationKindT(typeParsed: B): runtimemonitor.ObservationKind.Type = {
      if (!typeParsed) {
        parser.parseObjectType("runtimemonitor.ObservationKind")
      }
      parser.parseObjectKey("value")
      var i = parser.offset
      val s = parser.parseString()
      parser.parseObjectNext()
      runtimemonitor.ObservationKind.byName(s) match {
        case Some(r) => return r
        case _ =>
          parser.parseException(i, s"Invalid element name '$s' for runtimemonitor.ObservationKind.")
          return runtimemonitor.ObservationKind.byOrdinal(0).get
      }
    }

    def parseutilContainer(): util.Container = {
      val t = parser.parseObjectTypes(ISZ("util.EmptyContainer", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS"))
      t.native match {
        case "util.EmptyContainer" => val r = parseutilEmptyContainerT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
        case _ => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
      }
    }

    def parseutilEmptyContainer(): util.EmptyContainer = {
      val r = parseutilEmptyContainerT(F)
      return r
    }

    def parseutilEmptyContainerT(typeParsed: B): util.EmptyContainer = {
      if (!typeParsed) {
        parser.parseObjectType("util.EmptyContainer")
      }
      return util.EmptyContainer()
    }

    def parse_artDataContent(): art.DataContent = {
      val t = parser.parseObjectTypes(ISZ("art.Empty", "Base_Types.Boolean_Payload", "Base_Types.Integer_Payload", "Base_Types.Integer_8_Payload", "Base_Types.Integer_16_Payload", "Base_Types.Integer_32_Payload", "Base_Types.Integer_64_Payload", "Base_Types.Unsigned_8_Payload", "Base_Types.Unsigned_16_Payload", "Base_Types.Unsigned_32_Payload", "Base_Types.Unsigned_64_Payload", "Base_Types.Float_Payload", "Base_Types.Float_32_Payload", "Base_Types.Float_64_Payload", "Base_Types.Character_Payload", "Base_Types.String_Payload", "Base_Types.Bits_Payload", "util.EmptyContainer", "Gumbo_Structs_Arrays.MyEnum_Payload", "Gumbo_Structs_Arrays.MyStruct2_i_Payload", "Gumbo_Structs_Arrays.MyArrayInt32_Payload", "Gumbo_Structs_Arrays.MyArrayStruct_Payload", "Gumbo_Structs_Arrays.MyStructArray_i_Payload", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS", "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P", "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS"))
      t.native match {
        case "art.Empty" => val r = parse_artEmptyT(T); return r
        case "Base_Types.Boolean_Payload" => val r = parseBase_TypesBoolean_PayloadT(T); return r
        case "Base_Types.Integer_Payload" => val r = parseBase_TypesInteger_PayloadT(T); return r
        case "Base_Types.Integer_8_Payload" => val r = parseBase_TypesInteger_8_PayloadT(T); return r
        case "Base_Types.Integer_16_Payload" => val r = parseBase_TypesInteger_16_PayloadT(T); return r
        case "Base_Types.Integer_32_Payload" => val r = parseBase_TypesInteger_32_PayloadT(T); return r
        case "Base_Types.Integer_64_Payload" => val r = parseBase_TypesInteger_64_PayloadT(T); return r
        case "Base_Types.Unsigned_8_Payload" => val r = parseBase_TypesUnsigned_8_PayloadT(T); return r
        case "Base_Types.Unsigned_16_Payload" => val r = parseBase_TypesUnsigned_16_PayloadT(T); return r
        case "Base_Types.Unsigned_32_Payload" => val r = parseBase_TypesUnsigned_32_PayloadT(T); return r
        case "Base_Types.Unsigned_64_Payload" => val r = parseBase_TypesUnsigned_64_PayloadT(T); return r
        case "Base_Types.Float_Payload" => val r = parseBase_TypesFloat_PayloadT(T); return r
        case "Base_Types.Float_32_Payload" => val r = parseBase_TypesFloat_32_PayloadT(T); return r
        case "Base_Types.Float_64_Payload" => val r = parseBase_TypesFloat_64_PayloadT(T); return r
        case "Base_Types.Character_Payload" => val r = parseBase_TypesCharacter_PayloadT(T); return r
        case "Base_Types.String_Payload" => val r = parseBase_TypesString_PayloadT(T); return r
        case "Base_Types.Bits_Payload" => val r = parseBase_TypesBits_PayloadT(T); return r
        case "util.EmptyContainer" => val r = parseutilEmptyContainerT(T); return r
        case "Gumbo_Structs_Arrays.MyEnum_Payload" => val r = parseGumbo_Structs_ArraysMyEnum_PayloadT(T); return r
        case "Gumbo_Structs_Arrays.MyStruct2_i_Payload" => val r = parseGumbo_Structs_ArraysMyStruct2_i_PayloadT(T); return r
        case "Gumbo_Structs_Arrays.MyArrayInt32_Payload" => val r = parseGumbo_Structs_ArraysMyArrayInt32_PayloadT(T); return r
        case "Gumbo_Structs_Arrays.MyArrayStruct_Payload" => val r = parseGumbo_Structs_ArraysMyArrayStruct_PayloadT(T); return r
        case "Gumbo_Structs_Arrays.MyStructArray_i_Payload" => val r = parseGumbo_Structs_ArraysMyStructArray_i_PayloadT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PST(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PST(T); return r
        case "Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS" => val r = parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PST(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PT(T); return r
        case "Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS" => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
        case _ => val r = parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PST(T); return r
      }
    }

    def parse_artEmpty(): art.Empty = {
      val r = parse_artEmptyT(F)
      return r
    }

    def parse_artEmptyT(typeParsed: B): art.Empty = {
      if (!typeParsed) {
        parser.parseObjectType("art.Empty")
      }
      return art.Empty()
    }

    def eof(): B = {
      val r = parser.eof()
      return r
    }

  }

  def to[T](s: String, f: Parser => T): Either[T, Json.ErrorMsg] = {
    val parser = Parser(s)
    val r = f(parser)
    parser.eof()
    parser.errorOpt match {
      case Some(e) => return Either.Right(e)
      case _ => return Either.Left(r)
    }
  }

  def fromGumbo_Structs_ArraysMyEnum_Payload(o: Gumbo_Structs_Arrays.MyEnum_Payload, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyEnum_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyEnum_Payload(s: String): Either[Gumbo_Structs_Arrays.MyEnum_Payload, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyEnum_Payload(parser: Parser): Gumbo_Structs_Arrays.MyEnum_Payload = {
      val r = parser.parseGumbo_Structs_ArraysMyEnum_Payload()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyEnum_Payload _)
    return r
  }

  def fromGumbo_Structs_ArraysMyStruct2_i(o: Gumbo_Structs_Arrays.MyStruct2_i, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyStruct2_i(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyStruct2_i(s: String): Either[Gumbo_Structs_Arrays.MyStruct2_i, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyStruct2_i(parser: Parser): Gumbo_Structs_Arrays.MyStruct2_i = {
      val r = parser.parseGumbo_Structs_ArraysMyStruct2_i()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyStruct2_i _)
    return r
  }

  def fromGumbo_Structs_ArraysMyStruct2_i_Payload(o: Gumbo_Structs_Arrays.MyStruct2_i_Payload, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyStruct2_i_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyStruct2_i_Payload(s: String): Either[Gumbo_Structs_Arrays.MyStruct2_i_Payload, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyStruct2_i_Payload(parser: Parser): Gumbo_Structs_Arrays.MyStruct2_i_Payload = {
      val r = parser.parseGumbo_Structs_ArraysMyStruct2_i_Payload()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyStruct2_i_Payload _)
    return r
  }

  def fromGumbo_Structs_ArraysMyArrayInt32(o: Gumbo_Structs_Arrays.MyArrayInt32, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyArrayInt32(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyArrayInt32(s: String): Either[Gumbo_Structs_Arrays.MyArrayInt32, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyArrayInt32(parser: Parser): Gumbo_Structs_Arrays.MyArrayInt32 = {
      val r = parser.parseGumbo_Structs_ArraysMyArrayInt32()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyArrayInt32 _)
    return r
  }

  def fromGumbo_Structs_ArraysMyArrayInt32_Payload(o: Gumbo_Structs_Arrays.MyArrayInt32_Payload, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyArrayInt32_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyArrayInt32_Payload(s: String): Either[Gumbo_Structs_Arrays.MyArrayInt32_Payload, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyArrayInt32_Payload(parser: Parser): Gumbo_Structs_Arrays.MyArrayInt32_Payload = {
      val r = parser.parseGumbo_Structs_ArraysMyArrayInt32_Payload()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyArrayInt32_Payload _)
    return r
  }

  def fromGumbo_Structs_ArraysMyArrayStruct(o: Gumbo_Structs_Arrays.MyArrayStruct, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyArrayStruct(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyArrayStruct(s: String): Either[Gumbo_Structs_Arrays.MyArrayStruct, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyArrayStruct(parser: Parser): Gumbo_Structs_Arrays.MyArrayStruct = {
      val r = parser.parseGumbo_Structs_ArraysMyArrayStruct()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyArrayStruct _)
    return r
  }

  def fromGumbo_Structs_ArraysMyArrayStruct_Payload(o: Gumbo_Structs_Arrays.MyArrayStruct_Payload, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyArrayStruct_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyArrayStruct_Payload(s: String): Either[Gumbo_Structs_Arrays.MyArrayStruct_Payload, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyArrayStruct_Payload(parser: Parser): Gumbo_Structs_Arrays.MyArrayStruct_Payload = {
      val r = parser.parseGumbo_Structs_ArraysMyArrayStruct_Payload()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyArrayStruct_Payload _)
    return r
  }

  def fromGumbo_Structs_ArraysMyStructArray_i(o: Gumbo_Structs_Arrays.MyStructArray_i, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyStructArray_i(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyStructArray_i(s: String): Either[Gumbo_Structs_Arrays.MyStructArray_i, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyStructArray_i(parser: Parser): Gumbo_Structs_Arrays.MyStructArray_i = {
      val r = parser.parseGumbo_Structs_ArraysMyStructArray_i()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyStructArray_i _)
    return r
  }

  def fromGumbo_Structs_ArraysMyStructArray_i_Payload(o: Gumbo_Structs_Arrays.MyStructArray_i_Payload, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysMyStructArray_i_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysMyStructArray_i_Payload(s: String): Either[Gumbo_Structs_Arrays.MyStructArray_i_Payload, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysMyStructArray_i_Payload(parser: Parser): Gumbo_Structs_Arrays.MyStructArray_i_Payload = {
      val r = parser.parseGumbo_Structs_ArraysMyStructArray_i_Payload()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysMyStructArray_i_Payload _)
    return r
  }

  def fromBase_TypesBoolean_Payload(o: Base_Types.Boolean_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesBoolean_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesBoolean_Payload(s: String): Either[Base_Types.Boolean_Payload, Json.ErrorMsg] = {
    def fBase_TypesBoolean_Payload(parser: Parser): Base_Types.Boolean_Payload = {
      val r = parser.parseBase_TypesBoolean_Payload()
      return r
    }
    val r = to(s, fBase_TypesBoolean_Payload _)
    return r
  }

  def fromBase_TypesInteger_Payload(o: Base_Types.Integer_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesInteger_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesInteger_Payload(s: String): Either[Base_Types.Integer_Payload, Json.ErrorMsg] = {
    def fBase_TypesInteger_Payload(parser: Parser): Base_Types.Integer_Payload = {
      val r = parser.parseBase_TypesInteger_Payload()
      return r
    }
    val r = to(s, fBase_TypesInteger_Payload _)
    return r
  }

  def fromBase_TypesInteger_8_Payload(o: Base_Types.Integer_8_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesInteger_8_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesInteger_8_Payload(s: String): Either[Base_Types.Integer_8_Payload, Json.ErrorMsg] = {
    def fBase_TypesInteger_8_Payload(parser: Parser): Base_Types.Integer_8_Payload = {
      val r = parser.parseBase_TypesInteger_8_Payload()
      return r
    }
    val r = to(s, fBase_TypesInteger_8_Payload _)
    return r
  }

  def fromBase_TypesInteger_16_Payload(o: Base_Types.Integer_16_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesInteger_16_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesInteger_16_Payload(s: String): Either[Base_Types.Integer_16_Payload, Json.ErrorMsg] = {
    def fBase_TypesInteger_16_Payload(parser: Parser): Base_Types.Integer_16_Payload = {
      val r = parser.parseBase_TypesInteger_16_Payload()
      return r
    }
    val r = to(s, fBase_TypesInteger_16_Payload _)
    return r
  }

  def fromBase_TypesInteger_32_Payload(o: Base_Types.Integer_32_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesInteger_32_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesInteger_32_Payload(s: String): Either[Base_Types.Integer_32_Payload, Json.ErrorMsg] = {
    def fBase_TypesInteger_32_Payload(parser: Parser): Base_Types.Integer_32_Payload = {
      val r = parser.parseBase_TypesInteger_32_Payload()
      return r
    }
    val r = to(s, fBase_TypesInteger_32_Payload _)
    return r
  }

  def fromBase_TypesInteger_64_Payload(o: Base_Types.Integer_64_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesInteger_64_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesInteger_64_Payload(s: String): Either[Base_Types.Integer_64_Payload, Json.ErrorMsg] = {
    def fBase_TypesInteger_64_Payload(parser: Parser): Base_Types.Integer_64_Payload = {
      val r = parser.parseBase_TypesInteger_64_Payload()
      return r
    }
    val r = to(s, fBase_TypesInteger_64_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_8_Payload(o: Base_Types.Unsigned_8_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesUnsigned_8_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesUnsigned_8_Payload(s: String): Either[Base_Types.Unsigned_8_Payload, Json.ErrorMsg] = {
    def fBase_TypesUnsigned_8_Payload(parser: Parser): Base_Types.Unsigned_8_Payload = {
      val r = parser.parseBase_TypesUnsigned_8_Payload()
      return r
    }
    val r = to(s, fBase_TypesUnsigned_8_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_16_Payload(o: Base_Types.Unsigned_16_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesUnsigned_16_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesUnsigned_16_Payload(s: String): Either[Base_Types.Unsigned_16_Payload, Json.ErrorMsg] = {
    def fBase_TypesUnsigned_16_Payload(parser: Parser): Base_Types.Unsigned_16_Payload = {
      val r = parser.parseBase_TypesUnsigned_16_Payload()
      return r
    }
    val r = to(s, fBase_TypesUnsigned_16_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_32_Payload(o: Base_Types.Unsigned_32_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesUnsigned_32_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesUnsigned_32_Payload(s: String): Either[Base_Types.Unsigned_32_Payload, Json.ErrorMsg] = {
    def fBase_TypesUnsigned_32_Payload(parser: Parser): Base_Types.Unsigned_32_Payload = {
      val r = parser.parseBase_TypesUnsigned_32_Payload()
      return r
    }
    val r = to(s, fBase_TypesUnsigned_32_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_64_Payload(o: Base_Types.Unsigned_64_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesUnsigned_64_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesUnsigned_64_Payload(s: String): Either[Base_Types.Unsigned_64_Payload, Json.ErrorMsg] = {
    def fBase_TypesUnsigned_64_Payload(parser: Parser): Base_Types.Unsigned_64_Payload = {
      val r = parser.parseBase_TypesUnsigned_64_Payload()
      return r
    }
    val r = to(s, fBase_TypesUnsigned_64_Payload _)
    return r
  }

  def fromBase_TypesFloat_Payload(o: Base_Types.Float_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesFloat_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesFloat_Payload(s: String): Either[Base_Types.Float_Payload, Json.ErrorMsg] = {
    def fBase_TypesFloat_Payload(parser: Parser): Base_Types.Float_Payload = {
      val r = parser.parseBase_TypesFloat_Payload()
      return r
    }
    val r = to(s, fBase_TypesFloat_Payload _)
    return r
  }

  def fromBase_TypesFloat_32_Payload(o: Base_Types.Float_32_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesFloat_32_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesFloat_32_Payload(s: String): Either[Base_Types.Float_32_Payload, Json.ErrorMsg] = {
    def fBase_TypesFloat_32_Payload(parser: Parser): Base_Types.Float_32_Payload = {
      val r = parser.parseBase_TypesFloat_32_Payload()
      return r
    }
    val r = to(s, fBase_TypesFloat_32_Payload _)
    return r
  }

  def fromBase_TypesFloat_64_Payload(o: Base_Types.Float_64_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesFloat_64_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesFloat_64_Payload(s: String): Either[Base_Types.Float_64_Payload, Json.ErrorMsg] = {
    def fBase_TypesFloat_64_Payload(parser: Parser): Base_Types.Float_64_Payload = {
      val r = parser.parseBase_TypesFloat_64_Payload()
      return r
    }
    val r = to(s, fBase_TypesFloat_64_Payload _)
    return r
  }

  def fromBase_TypesCharacter_Payload(o: Base_Types.Character_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesCharacter_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesCharacter_Payload(s: String): Either[Base_Types.Character_Payload, Json.ErrorMsg] = {
    def fBase_TypesCharacter_Payload(parser: Parser): Base_Types.Character_Payload = {
      val r = parser.parseBase_TypesCharacter_Payload()
      return r
    }
    val r = to(s, fBase_TypesCharacter_Payload _)
    return r
  }

  def fromBase_TypesString_Payload(o: Base_Types.String_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesString_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesString_Payload(s: String): Either[Base_Types.String_Payload, Json.ErrorMsg] = {
    def fBase_TypesString_Payload(parser: Parser): Base_Types.String_Payload = {
      val r = parser.parseBase_TypesString_Payload()
      return r
    }
    val r = to(s, fBase_TypesString_Payload _)
    return r
  }

  def fromBase_TypesBits_Payload(o: Base_Types.Bits_Payload, isCompact: B): String = {
    val st = Printer.printBase_TypesBits_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toBase_TypesBits_Payload(s: String): Either[Base_Types.Bits_Payload, Json.ErrorMsg] = {
    def fBase_TypesBits_Payload(parser: Parser): Base_Types.Bits_Payload = {
      val r = parser.parseBase_TypesBits_Payload()
      return r
    }
    val r = to(s, fBase_TypesBits_Payload _)
    return r
  }

  def fromGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(s: String): Either[Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container(parser: Parser): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container = {
      val r = parser.parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container _)
    return r
  }

  def fromGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(s: String): Either[Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P(parser: Parser): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_P = {
      val r = parser.parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_P _)
    return r
  }

  def fromGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(s: String): Either[Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS(parser: Parser): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PreState_Container_PS = {
      val r = parser.parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysProducerThr_i_producer_producer_PreState_Container_PS _)
    return r
  }

  def fromGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(s: String): Either[Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container(parser: Parser): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container = {
      val r = parser.parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container _)
    return r
  }

  def fromGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(s: String): Either[Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P(parser: Parser): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_P = {
      val r = parser.parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_P _)
    return r
  }

  def fromGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o: Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(s: String): Either[Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS(parser: Parser): Gumbo_Structs_Arrays.ProducerThr_i_producer_producer_PostState_Container_PS = {
      val r = parser.parseGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysProducerThr_i_producer_producer_PostState_Container_PS _)
    return r
  }

  def fromGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(s: String): Either[Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container(parser: Parser): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container = {
      val r = parser.parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container _)
    return r
  }

  def fromGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(s: String): Either[Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P(parser: Parser): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_P = {
      val r = parser.parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_P _)
    return r
  }

  def fromGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(s: String): Either[Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS(parser: Parser): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PreState_Container_PS = {
      val r = parser.parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PreState_Container_PS _)
    return r
  }

  def fromGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(s: String): Either[Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container(parser: Parser): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container = {
      val r = parser.parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container _)
    return r
  }

  def fromGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(s: String): Either[Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P(parser: Parser): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_P = {
      val r = parser.parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_P _)
    return r
  }

  def fromGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o: Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(s: String): Either[Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS, Json.ErrorMsg] = {
    def fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS(parser: Parser): Gumbo_Structs_Arrays.ConsumerThr_i_consumer_consumer_PostState_Container_PS = {
      val r = parser.parseGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS()
      return r
    }
    val r = to(s, fGumbo_Structs_ArraysConsumerThr_i_consumer_consumer_PostState_Container_PS _)
    return r
  }

  def fromutilContainer(o: util.Container, isCompact: B): String = {
    val st = Printer.printutilContainer(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toutilContainer(s: String): Either[util.Container, Json.ErrorMsg] = {
    def futilContainer(parser: Parser): util.Container = {
      val r = parser.parseutilContainer()
      return r
    }
    val r = to(s, futilContainer _)
    return r
  }

  def fromutilEmptyContainer(o: util.EmptyContainer, isCompact: B): String = {
    val st = Printer.printutilEmptyContainer(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toutilEmptyContainer(s: String): Either[util.EmptyContainer, Json.ErrorMsg] = {
    def futilEmptyContainer(parser: Parser): util.EmptyContainer = {
      val r = parser.parseutilEmptyContainer()
      return r
    }
    val r = to(s, futilEmptyContainer _)
    return r
  }

  def from_artDataContent(o: art.DataContent, isCompact: B): String = {
    val st = Printer.print_artDataContent(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def to_artDataContent(s: String): Either[art.DataContent, Json.ErrorMsg] = {
    def f_artDataContent(parser: Parser): art.DataContent = {
      val r = parser.parse_artDataContent()
      return r
    }
    val r = to(s, f_artDataContent _)
    return r
  }

  def from_artEmpty(o: art.Empty, isCompact: B): String = {
    val st = Printer.print_artEmpty(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def to_artEmpty(s: String): Either[art.Empty, Json.ErrorMsg] = {
    def f_artEmpty(parser: Parser): art.Empty = {
      val r = parser.parse_artEmpty()
      return r
    }
    val r = to(s, f_artEmpty _)
    return r
  }

}