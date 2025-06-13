// #Sireum
// @formatter:off

// This file is auto-generated from RawEthernetMessage.scala, u16Array.scala, Base_Types.scala, ArduPilot_Impl_ArduPilot_ArduPilot_Containers.scala, Firewall_Impl_Firewall_Firewall_Containers.scala, LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Containers.scala, Container.scala, DataContent.scala, Aux_Types.scala

package firewall

import org.sireum._
import org.sireum.Json.Printer._

object JSON {

  object Printer {

    @pure def printSWRawEthernetMessageI(o: SW.RawEthernetMessage.I): ST = {
      return printNumber(o.toZ.string)
    }

    @pure def printSWRawEthernetMessage(o: SW.RawEthernetMessage): ST = {
      return printObject(ISZ(
        ("type", st""""SW.RawEthernetMessage""""),
        ("value", printIS(T, o.value.map(printU8 _)))
      ))
    }

    @pure def printSWRawEthernetMessage_Payload(o: SW.RawEthernetMessage_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""SW.RawEthernetMessage_Payload""""),
        ("value", printSWRawEthernetMessage(o.value))
      ))
    }

    @pure def printSWu16ArrayI(o: SW.u16Array.I): ST = {
      return printNumber(o.toZ.string)
    }

    @pure def printSWu16Array(o: SW.u16Array): ST = {
      return printObject(ISZ(
        ("type", st""""SW.u16Array""""),
        ("value", printIS(T, o.value.map(printU16 _)))
      ))
    }

    @pure def printSWu16Array_Payload(o: SW.u16Array_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""SW.u16Array_Payload""""),
        ("value", printSWu16Array(o.value))
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

    @pure def printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container): ST = {
      o match {
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(o)
      }
    }

    @pure def printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container): ST = {
      o match {
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(o)
      }
    }

    @pure def printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWFirewall_Impl_Firewall_Firewall_PreState_Container(o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container): ST = {
      o match {
        case o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P => return printSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS => return printSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(o)
      }
    }

    @pure def printSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P""""),
        ("api_EthernetFramesRxIn", printOption(F, o.api_EthernetFramesRxIn, printSWRawEthernetMessage _)),
        ("api_EthernetFramesTxIn", printOption(F, o.api_EthernetFramesTxIn, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS""""),
        ("api_EthernetFramesRxIn", printOption(F, o.api_EthernetFramesRxIn, printSWRawEthernetMessage _)),
        ("api_EthernetFramesTxIn", printOption(F, o.api_EthernetFramesTxIn, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWFirewall_Impl_Firewall_Firewall_PostState_Container(o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container): ST = {
      o match {
        case o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P => return printSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS => return printSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(o)
      }
    }

    @pure def printSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P""""),
        ("api_EthernetFramesRxOut", printOption(F, o.api_EthernetFramesRxOut, printSWRawEthernetMessage _)),
        ("api_EthernetFramesTxOut", printOption(F, o.api_EthernetFramesTxOut, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS""""),
        ("api_EthernetFramesRxOut", printOption(F, o.api_EthernetFramesRxOut, printSWRawEthernetMessage _)),
        ("api_EthernetFramesTxOut", printOption(F, o.api_EthernetFramesTxOut, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container): ST = {
      o match {
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
      }
    }

    @pure def printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container): ST = {
      o match {
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
      }
    }

    @pure def printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWRawEthernetMessage _))
      ))
    }

    @pure def printutilContainer(o: util.Container): ST = {
      o match {
        case o: util.EmptyContainer => return printutilEmptyContainer(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P => return printSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS => return printSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P => return printSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS => return printSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
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
        case o: SW.u16Array_Payload => return printSWu16Array_Payload(o)
        case o: util.EmptyContainer => return printutilEmptyContainer(o)
        case o: SW.RawEthernetMessage_Payload => return printSWRawEthernetMessage_Payload(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P => return printSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS => return printSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P => return printSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS => return printSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS => return printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => return printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
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

    def parseSWRawEthernetMessageI(): SW.RawEthernetMessage.I = {
      val i = parser.offset
      val s = parser.parseNumber()
      SW.RawEthernetMessage.I(s) match {
        case Some(n) => return n
        case _ =>
          parser.parseException(i, s"Expected a SW.RawEthernetMessage.I, but '$s' found.")
          return SW.RawEthernetMessage.I.Min
      }
    }

    def parseISSWRawEthernetMessageI[T](f: () => T): IS[SW.RawEthernetMessage.I, T] = {
      if (!parser.parseArrayBegin()) {
        return IS()
      }
      var e = f()
      var r = IS[SW.RawEthernetMessage.I, T](e)
      var continue = parser.parseArrayNext()
      while (continue) {
        e = f()
        r = r :+ e
        continue = parser.parseArrayNext()
      }
      return r
    }

    def parseSWRawEthernetMessage(): SW.RawEthernetMessage = {
      val r = parseSWRawEthernetMessageT(F)
      return r
    }

    def parseSWRawEthernetMessageT(typeParsed: B): SW.RawEthernetMessage = {
      if (!typeParsed) {
        parser.parseObjectType("SW.RawEthernetMessage")
      }
      parser.parseObjectKey("value")
      val value = parseISSWRawEthernetMessageI(parser.parseU8 _)
      parser.parseObjectNext()
      return SW.RawEthernetMessage(value)
    }

    def parseSWRawEthernetMessage_Payload(): SW.RawEthernetMessage_Payload = {
      val r = parseSWRawEthernetMessage_PayloadT(F)
      return r
    }

    def parseSWRawEthernetMessage_PayloadT(typeParsed: B): SW.RawEthernetMessage_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("SW.RawEthernetMessage_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseSWRawEthernetMessage()
      parser.parseObjectNext()
      return SW.RawEthernetMessage_Payload(value)
    }

    def parseSWu16ArrayI(): SW.u16Array.I = {
      val i = parser.offset
      val s = parser.parseNumber()
      SW.u16Array.I(s) match {
        case Some(n) => return n
        case _ =>
          parser.parseException(i, s"Expected a SW.u16Array.I, but '$s' found.")
          return SW.u16Array.I.Min
      }
    }

    def parseISSWu16ArrayI[T](f: () => T): IS[SW.u16Array.I, T] = {
      if (!parser.parseArrayBegin()) {
        return IS()
      }
      var e = f()
      var r = IS[SW.u16Array.I, T](e)
      var continue = parser.parseArrayNext()
      while (continue) {
        e = f()
        r = r :+ e
        continue = parser.parseArrayNext()
      }
      return r
    }

    def parseSWu16Array(): SW.u16Array = {
      val r = parseSWu16ArrayT(F)
      return r
    }

    def parseSWu16ArrayT(typeParsed: B): SW.u16Array = {
      if (!typeParsed) {
        parser.parseObjectType("SW.u16Array")
      }
      parser.parseObjectKey("value")
      val value = parseISSWu16ArrayI(parser.parseU16 _)
      parser.parseObjectNext()
      return SW.u16Array(value)
    }

    def parseSWu16Array_Payload(): SW.u16Array_Payload = {
      val r = parseSWu16Array_PayloadT(F)
      return r
    }

    def parseSWu16Array_PayloadT(typeParsed: B): SW.u16Array_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("SW.u16Array_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseSWu16Array()
      parser.parseObjectNext()
      return SW.u16Array_Payload(value)
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

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container(): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS"))
      t.native match {
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case _ => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
      }
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P = {
      val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PT(F)
      return r
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PT(typeParsed: B): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(api_EthernetFramesRx)
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS = {
      val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PST(F)
      return r
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PST(typeParsed: B): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(api_EthernetFramesRx)
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container(): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS"))
      t.native match {
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case _ => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
      }
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P = {
      val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PT(F)
      return r
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PT(typeParsed: B): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(api_EthernetFramesTx)
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS = {
      val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PST(F)
      return r
    }

    def parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PST(typeParsed: B): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(api_EthernetFramesTx)
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PreState_Container(): SW.Firewall_Impl_Firewall_Firewall_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P", "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS"))
      t.native match {
        case "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P" => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS" => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PST(T); return r
        case _ => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PST(T); return r
      }
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(): SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P = {
      val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PT(F)
      return r
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PT(typeParsed: B): SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRxIn")
      val api_EthernetFramesRxIn = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxIn")
      val api_EthernetFramesTxIn = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(): SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS = {
      val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PST(F)
      return r
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PST(typeParsed: B): SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRxIn")
      val api_EthernetFramesRxIn = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxIn")
      val api_EthernetFramesTxIn = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PostState_Container(): SW.Firewall_Impl_Firewall_Firewall_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P", "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS"))
      t.native match {
        case "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P" => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS" => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PST(T); return r
        case _ => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PST(T); return r
      }
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(): SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P = {
      val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PT(F)
      return r
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PT(typeParsed: B): SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRxOut")
      val api_EthernetFramesRxOut = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxOut")
      val api_EthernetFramesTxOut = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(): SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS = {
      val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PST(F)
      return r
    }

    def parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PST(typeParsed: B): SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRxOut")
      val api_EthernetFramesRxOut = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxOut")
      val api_EthernetFramesTxOut = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS"))
      t.native match {
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
      }
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(typeParsed: B): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(api_EthernetFramesTx)
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(typeParsed: B): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(api_EthernetFramesTx)
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS"))
      t.native match {
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
      }
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(typeParsed: B): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(api_EthernetFramesRx)
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(typeParsed: B): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWRawEthernetMessage _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(api_EthernetFramesRx)
    }

    def parseutilContainer(): util.Container = {
      val t = parser.parseObjectTypes(ISZ("util.EmptyContainer", "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P", "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS", "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P", "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS"))
      t.native match {
        case "util.EmptyContainer" => val r = parseutilEmptyContainerT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P" => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS" => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PST(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P" => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS" => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
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
      val t = parser.parseObjectTypes(ISZ("art.Empty", "Base_Types.Boolean_Payload", "Base_Types.Integer_Payload", "Base_Types.Integer_8_Payload", "Base_Types.Integer_16_Payload", "Base_Types.Integer_32_Payload", "Base_Types.Integer_64_Payload", "Base_Types.Unsigned_8_Payload", "Base_Types.Unsigned_16_Payload", "Base_Types.Unsigned_32_Payload", "Base_Types.Unsigned_64_Payload", "Base_Types.Float_Payload", "Base_Types.Float_32_Payload", "Base_Types.Float_64_Payload", "Base_Types.Character_Payload", "Base_Types.String_Payload", "Base_Types.Bits_Payload", "SW.u16Array_Payload", "util.EmptyContainer", "SW.RawEthernetMessage_Payload", "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P", "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS", "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P", "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P", "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P", "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS"))
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
        case "SW.u16Array_Payload" => val r = parseSWu16Array_PayloadT(T); return r
        case "util.EmptyContainer" => val r = parseutilEmptyContainerT(T); return r
        case "SW.RawEthernetMessage_Payload" => val r = parseSWRawEthernetMessage_PayloadT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P" => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS" => val r = parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PST(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P" => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PT(T); return r
        case "SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS" => val r = parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS" => val r = parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
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

  def fromSWRawEthernetMessage(o: SW.RawEthernetMessage, isCompact: B): String = {
    val st = Printer.printSWRawEthernetMessage(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWRawEthernetMessage(s: String): Either[SW.RawEthernetMessage, Json.ErrorMsg] = {
    def fSWRawEthernetMessage(parser: Parser): SW.RawEthernetMessage = {
      val r = parser.parseSWRawEthernetMessage()
      return r
    }
    val r = to(s, fSWRawEthernetMessage _)
    return r
  }

  def fromSWRawEthernetMessage_Payload(o: SW.RawEthernetMessage_Payload, isCompact: B): String = {
    val st = Printer.printSWRawEthernetMessage_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWRawEthernetMessage_Payload(s: String): Either[SW.RawEthernetMessage_Payload, Json.ErrorMsg] = {
    def fSWRawEthernetMessage_Payload(parser: Parser): SW.RawEthernetMessage_Payload = {
      val r = parser.parseSWRawEthernetMessage_Payload()
      return r
    }
    val r = to(s, fSWRawEthernetMessage_Payload _)
    return r
  }

  def fromSWu16Array(o: SW.u16Array, isCompact: B): String = {
    val st = Printer.printSWu16Array(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWu16Array(s: String): Either[SW.u16Array, Json.ErrorMsg] = {
    def fSWu16Array(parser: Parser): SW.u16Array = {
      val r = parser.parseSWu16Array()
      return r
    }
    val r = to(s, fSWu16Array _)
    return r
  }

  def fromSWu16Array_Payload(o: SW.u16Array_Payload, isCompact: B): String = {
    val st = Printer.printSWu16Array_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWu16Array_Payload(s: String): Either[SW.u16Array_Payload, Json.ErrorMsg] = {
    def fSWu16Array_Payload(parser: Parser): SW.u16Array_Payload = {
      val r = parser.parseSWu16Array_Payload()
      return r
    }
    val r = to(s, fSWu16Array_Payload _)
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

  def fromSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container(s: String): Either[SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container(parser: Parser): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container = {
      val r = parser.parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container _)
    return r
  }

  def fromSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(s: String): Either[SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P(parser: Parser): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P = {
      val r = parser.parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_P _)
    return r
  }

  def fromSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(s: String): Either[SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS(parser: Parser): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS = {
      val r = parser.parseSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_ArduPilot_ArduPilot_PreState_Container_PS _)
    return r
  }

  def fromSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container(s: String): Either[SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container(parser: Parser): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container = {
      val r = parser.parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container _)
    return r
  }

  def fromSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(s: String): Either[SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P(parser: Parser): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P = {
      val r = parser.parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_P _)
    return r
  }

  def fromSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(o: SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(s: String): Either[SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS(parser: Parser): SW.ArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS = {
      val r = parser.parseSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_ArduPilot_ArduPilot_PostState_Container_PS _)
    return r
  }

  def fromSWFirewall_Impl_Firewall_Firewall_PreState_Container(o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_Firewall_Firewall_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_Firewall_Firewall_PreState_Container(s: String): Either[SW.Firewall_Impl_Firewall_Firewall_PreState_Container, Json.ErrorMsg] = {
    def fSWFirewall_Impl_Firewall_Firewall_PreState_Container(parser: Parser): SW.Firewall_Impl_Firewall_Firewall_PreState_Container = {
      val r = parser.parseSWFirewall_Impl_Firewall_Firewall_PreState_Container()
      return r
    }
    val r = to(s, fSWFirewall_Impl_Firewall_Firewall_PreState_Container _)
    return r
  }

  def fromSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(s: String): Either[SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P, Json.ErrorMsg] = {
    def fSWFirewall_Impl_Firewall_Firewall_PreState_Container_P(parser: Parser): SW.Firewall_Impl_Firewall_Firewall_PreState_Container_P = {
      val r = parser.parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_P()
      return r
    }
    val r = to(s, fSWFirewall_Impl_Firewall_Firewall_PreState_Container_P _)
    return r
  }

  def fromSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(o: SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(s: String): Either[SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS, Json.ErrorMsg] = {
    def fSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS(parser: Parser): SW.Firewall_Impl_Firewall_Firewall_PreState_Container_PS = {
      val r = parser.parseSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS()
      return r
    }
    val r = to(s, fSWFirewall_Impl_Firewall_Firewall_PreState_Container_PS _)
    return r
  }

  def fromSWFirewall_Impl_Firewall_Firewall_PostState_Container(o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_Firewall_Firewall_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_Firewall_Firewall_PostState_Container(s: String): Either[SW.Firewall_Impl_Firewall_Firewall_PostState_Container, Json.ErrorMsg] = {
    def fSWFirewall_Impl_Firewall_Firewall_PostState_Container(parser: Parser): SW.Firewall_Impl_Firewall_Firewall_PostState_Container = {
      val r = parser.parseSWFirewall_Impl_Firewall_Firewall_PostState_Container()
      return r
    }
    val r = to(s, fSWFirewall_Impl_Firewall_Firewall_PostState_Container _)
    return r
  }

  def fromSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(s: String): Either[SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P, Json.ErrorMsg] = {
    def fSWFirewall_Impl_Firewall_Firewall_PostState_Container_P(parser: Parser): SW.Firewall_Impl_Firewall_Firewall_PostState_Container_P = {
      val r = parser.parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_P()
      return r
    }
    val r = to(s, fSWFirewall_Impl_Firewall_Firewall_PostState_Container_P _)
    return r
  }

  def fromSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(o: SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(s: String): Either[SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS, Json.ErrorMsg] = {
    def fSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS(parser: Parser): SW.Firewall_Impl_Firewall_Firewall_PostState_Container_PS = {
      val r = parser.parseSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS()
      return r
    }
    val r = to(s, fSWFirewall_Impl_Firewall_Firewall_PostState_Container_PS _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(s: String): Either[SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(parser: Parser): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(s: String): Either[SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(parser: Parser): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(s: String): Either[SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(parser: Parser): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(s: String): Either[SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(parser: Parser): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(s: String): Either[SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(parser: Parser): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(s: String): Either[SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(parser: Parser): SW.LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _)
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