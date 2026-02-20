// #Sireum
// @formatter:off

// This file is auto-generated from InternetProtocol.scala, FrameProtocol.scala, ARP_Type.scala, RawEthernetMessage.scala, StructuredEthernetMessage_i.scala, Base_Types.scala, ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Containers.scala, Firewall_Impl_seL4_Firewall_Firewall_Containers.scala, LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Containers.scala, ObservationKind.scala, Container.scala, DataContent.scala, Aux_Types.scala

package base

import org.sireum._
import org.sireum.Json.Printer._

object JSON {

  object Printer {

    @pure def printSWInternetProtocolType(o: SW.InternetProtocol.Type): ST = {
      val value: String = o match {
        case SW.InternetProtocol.IPV4 => "IPV4"
        case SW.InternetProtocol.IPV6 => "IPV6"
      }
      return printObject(ISZ(
        ("type", printString("SW.InternetProtocol")),
        ("value", printString(value))
      ))
    }

    @pure def printSWInternetProtocol_Payload(o: SW.InternetProtocol_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""SW.InternetProtocol_Payload""""),
        ("value", printSWInternetProtocolType(o.value))
      ))
    }

    @pure def printSWFrameProtocolType(o: SW.FrameProtocol.Type): ST = {
      val value: String = o match {
        case SW.FrameProtocol.TCP => "TCP"
        case SW.FrameProtocol.ARP => "ARP"
      }
      return printObject(ISZ(
        ("type", printString("SW.FrameProtocol")),
        ("value", printString(value))
      ))
    }

    @pure def printSWFrameProtocol_Payload(o: SW.FrameProtocol_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""SW.FrameProtocol_Payload""""),
        ("value", printSWFrameProtocolType(o.value))
      ))
    }

    @pure def printSWARP_TypeType(o: SW.ARP_Type.Type): ST = {
      val value: String = o match {
        case SW.ARP_Type.REQUEST => "REQUEST"
        case SW.ARP_Type.REPLY => "REPLY"
        case SW.ARP_Type.NA => "NA"
      }
      return printObject(ISZ(
        ("type", printString("SW.ARP_Type")),
        ("value", printString(value))
      ))
    }

    @pure def printSWARP_Type_Payload(o: SW.ARP_Type_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ARP_Type_Payload""""),
        ("value", printSWARP_TypeType(o.value))
      ))
    }

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

    @pure def printSWStructuredEthernetMessage_i(o: SW.StructuredEthernetMessage_i): ST = {
      return printObject(ISZ(
        ("type", st""""SW.StructuredEthernetMessage_i""""),
        ("malformedFrame", printB(o.malformedFrame)),
        ("internetProtocol", printSWInternetProtocolType(o.internetProtocol)),
        ("frameProtocol", printSWFrameProtocolType(o.frameProtocol)),
        ("portIsWhitelisted", printB(o.portIsWhitelisted)),
        ("arpType", printSWARP_TypeType(o.arpType)),
        ("rawMessage", printSWRawEthernetMessage(o.rawMessage))
      ))
    }

    @pure def printSWStructuredEthernetMessage_i_Payload(o: SW.StructuredEthernetMessage_i_Payload): ST = {
      return printObject(ISZ(
        ("type", st""""SW.StructuredEthernetMessage_i_Payload""""),
        ("value", printSWStructuredEthernetMessage_i(o.value))
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

    @pure def printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container): ST = {
      o match {
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
      }
    }

    @pure def printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container): ST = {
      o match {
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
      }
    }

    @pure def printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container): ST = {
      o match {
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => return printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => return printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
      }
    }

    @pure def printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P""""),
        ("api_EthernetFramesRxIn", printOption(F, o.api_EthernetFramesRxIn, printSWStructuredEthernetMessage_i _)),
        ("api_EthernetFramesTxIn", printOption(F, o.api_EthernetFramesTxIn, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS""""),
        ("api_EthernetFramesRxIn", printOption(F, o.api_EthernetFramesRxIn, printSWStructuredEthernetMessage_i _)),
        ("api_EthernetFramesTxIn", printOption(F, o.api_EthernetFramesTxIn, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container): ST = {
      o match {
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => return printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => return printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
      }
    }

    @pure def printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P""""),
        ("api_EthernetFramesRxOut", printOption(F, o.api_EthernetFramesRxOut, printSWStructuredEthernetMessage_i _)),
        ("api_EthernetFramesTxOut", printOption(F, o.api_EthernetFramesTxOut, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS""""),
        ("api_EthernetFramesRxOut", printOption(F, o.api_EthernetFramesRxOut, printSWStructuredEthernetMessage_i _)),
        ("api_EthernetFramesTxOut", printOption(F, o.api_EthernetFramesTxOut, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container): ST = {
      o match {
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
      }
    }

    @pure def printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS""""),
        ("api_EthernetFramesTx", printOption(F, o.api_EthernetFramesTx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container): ST = {
      o match {
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
      }
    }

    @pure def printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS): ST = {
      return printObject(ISZ(
        ("type", st""""SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS""""),
        ("api_EthernetFramesRx", printOption(F, o.api_EthernetFramesRx, printSWStructuredEthernetMessage_i _))
      ))
    }

    @pure def printruntimemonitorObservationKindType(o: runtimemonitor.ObservationKind.Type): ST = {
      val value: String = o match {
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot_postInit => "ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot_postInit"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot_preCompute => "ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot_preCompute"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot_postCompute => "ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot_postCompute"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_Firewall_Firewall_postInit => "ZCU102_Impl_Instance_seL4_Firewall_Firewall_postInit"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_Firewall_Firewall_preCompute => "ZCU102_Impl_Instance_seL4_Firewall_Firewall_preCompute"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_Firewall_Firewall_postCompute => "ZCU102_Impl_Instance_seL4_Firewall_Firewall_postCompute"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_postInit => "ZCU102_Impl_Instance_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_postInit"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_preCompute => "ZCU102_Impl_Instance_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_preCompute"
        case runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_postCompute => "ZCU102_Impl_Instance_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_postCompute"
      }
      return printObject(ISZ(
        ("type", printString("runtimemonitor.ObservationKind")),
        ("value", printString(value))
      ))
    }

    @pure def printutilContainer(o: util.Container): ST = {
      o match {
        case o: util.EmptyContainer => return printutilEmptyContainer(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => return printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => return printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => return printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => return printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
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
        case o: SW.ARP_Type_Payload => return printSWARP_Type_Payload(o)
        case o: util.EmptyContainer => return printutilEmptyContainer(o)
        case o: SW.FrameProtocol_Payload => return printSWFrameProtocol_Payload(o)
        case o: SW.InternetProtocol_Payload => return printSWInternetProtocol_Payload(o)
        case o: SW.RawEthernetMessage_Payload => return printSWRawEthernetMessage_Payload(o)
        case o: SW.StructuredEthernetMessage_i_Payload => return printSWStructuredEthernetMessage_i_Payload(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => return printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => return printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => return printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => return printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => return printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => return printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
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

    def parseSWInternetProtocolType(): SW.InternetProtocol.Type = {
      val r = parseSWInternetProtocolT(F)
      return r
    }

    def parseSWInternetProtocolT(typeParsed: B): SW.InternetProtocol.Type = {
      if (!typeParsed) {
        parser.parseObjectType("SW.InternetProtocol")
      }
      parser.parseObjectKey("value")
      var i = parser.offset
      val s = parser.parseString()
      parser.parseObjectNext()
      SW.InternetProtocol.byName(s) match {
        case Some(r) => return r
        case _ =>
          parser.parseException(i, s"Invalid element name '$s' for SW.InternetProtocol.")
          return SW.InternetProtocol.byOrdinal(0).get
      }
    }

    def parseSWInternetProtocol_Payload(): SW.InternetProtocol_Payload = {
      val r = parseSWInternetProtocol_PayloadT(F)
      return r
    }

    def parseSWInternetProtocol_PayloadT(typeParsed: B): SW.InternetProtocol_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("SW.InternetProtocol_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseSWInternetProtocolType()
      parser.parseObjectNext()
      return SW.InternetProtocol_Payload(value)
    }

    def parseSWFrameProtocolType(): SW.FrameProtocol.Type = {
      val r = parseSWFrameProtocolT(F)
      return r
    }

    def parseSWFrameProtocolT(typeParsed: B): SW.FrameProtocol.Type = {
      if (!typeParsed) {
        parser.parseObjectType("SW.FrameProtocol")
      }
      parser.parseObjectKey("value")
      var i = parser.offset
      val s = parser.parseString()
      parser.parseObjectNext()
      SW.FrameProtocol.byName(s) match {
        case Some(r) => return r
        case _ =>
          parser.parseException(i, s"Invalid element name '$s' for SW.FrameProtocol.")
          return SW.FrameProtocol.byOrdinal(0).get
      }
    }

    def parseSWFrameProtocol_Payload(): SW.FrameProtocol_Payload = {
      val r = parseSWFrameProtocol_PayloadT(F)
      return r
    }

    def parseSWFrameProtocol_PayloadT(typeParsed: B): SW.FrameProtocol_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("SW.FrameProtocol_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseSWFrameProtocolType()
      parser.parseObjectNext()
      return SW.FrameProtocol_Payload(value)
    }

    def parseSWARP_TypeType(): SW.ARP_Type.Type = {
      val r = parseSWARP_TypeT(F)
      return r
    }

    def parseSWARP_TypeT(typeParsed: B): SW.ARP_Type.Type = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ARP_Type")
      }
      parser.parseObjectKey("value")
      var i = parser.offset
      val s = parser.parseString()
      parser.parseObjectNext()
      SW.ARP_Type.byName(s) match {
        case Some(r) => return r
        case _ =>
          parser.parseException(i, s"Invalid element name '$s' for SW.ARP_Type.")
          return SW.ARP_Type.byOrdinal(0).get
      }
    }

    def parseSWARP_Type_Payload(): SW.ARP_Type_Payload = {
      val r = parseSWARP_Type_PayloadT(F)
      return r
    }

    def parseSWARP_Type_PayloadT(typeParsed: B): SW.ARP_Type_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ARP_Type_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseSWARP_TypeType()
      parser.parseObjectNext()
      return SW.ARP_Type_Payload(value)
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

    def parseSWStructuredEthernetMessage_i(): SW.StructuredEthernetMessage_i = {
      val r = parseSWStructuredEthernetMessage_iT(F)
      return r
    }

    def parseSWStructuredEthernetMessage_iT(typeParsed: B): SW.StructuredEthernetMessage_i = {
      if (!typeParsed) {
        parser.parseObjectType("SW.StructuredEthernetMessage_i")
      }
      parser.parseObjectKey("malformedFrame")
      val malformedFrame = parser.parseB()
      parser.parseObjectNext()
      parser.parseObjectKey("internetProtocol")
      val internetProtocol = parseSWInternetProtocolType()
      parser.parseObjectNext()
      parser.parseObjectKey("frameProtocol")
      val frameProtocol = parseSWFrameProtocolType()
      parser.parseObjectNext()
      parser.parseObjectKey("portIsWhitelisted")
      val portIsWhitelisted = parser.parseB()
      parser.parseObjectNext()
      parser.parseObjectKey("arpType")
      val arpType = parseSWARP_TypeType()
      parser.parseObjectNext()
      parser.parseObjectKey("rawMessage")
      val rawMessage = parseSWRawEthernetMessage()
      parser.parseObjectNext()
      return SW.StructuredEthernetMessage_i(malformedFrame, internetProtocol, frameProtocol, portIsWhitelisted, arpType, rawMessage)
    }

    def parseSWStructuredEthernetMessage_i_Payload(): SW.StructuredEthernetMessage_i_Payload = {
      val r = parseSWStructuredEthernetMessage_i_PayloadT(F)
      return r
    }

    def parseSWStructuredEthernetMessage_i_PayloadT(typeParsed: B): SW.StructuredEthernetMessage_i_Payload = {
      if (!typeParsed) {
        parser.parseObjectType("SW.StructuredEthernetMessage_i_Payload")
      }
      parser.parseObjectKey("value")
      val value = parseSWStructuredEthernetMessage_i()
      parser.parseObjectNext()
      return SW.StructuredEthernetMessage_i_Payload(value)
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

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS"))
      t.native match {
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case _ => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
      }
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
      val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(F)
      return r
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(api_EthernetFramesRx)
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
      val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(F)
      return r
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(api_EthernetFramesRx)
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS"))
      t.native match {
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case _ => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
      }
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
      val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(F)
      return r
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(api_EthernetFramesTx)
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
      val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(F)
      return r
    }

    def parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(api_EthernetFramesTx)
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P", "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS"))
      t.native match {
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
        case _ => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
      }
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
      val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(F)
      return r
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRxIn")
      val api_EthernetFramesRxIn = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxIn")
      val api_EthernetFramesTxIn = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
      val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(F)
      return r
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRxIn")
      val api_EthernetFramesRxIn = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxIn")
      val api_EthernetFramesTxIn = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P", "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS"))
      t.native match {
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
        case _ => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
      }
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
      val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(F)
      return r
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRxOut")
      val api_EthernetFramesRxOut = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxOut")
      val api_EthernetFramesTxOut = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
      val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(F)
      return r
    }

    def parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRxOut")
      val api_EthernetFramesRxOut = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      parser.parseObjectKey("api_EthernetFramesTxOut")
      val api_EthernetFramesTxOut = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS"))
      t.native match {
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
      }
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(api_EthernetFramesTx)
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesTx")
      val api_EthernetFramesTx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(api_EthernetFramesTx)
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
      val t = parser.parseObjectTypes(ISZ("SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS"))
      t.native match {
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
      }
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(api_EthernetFramesRx)
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(F)
      return r
    }

    def parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      if (!typeParsed) {
        parser.parseObjectType("SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS")
      }
      parser.parseObjectKey("api_EthernetFramesRx")
      val api_EthernetFramesRx = parser.parseOption(parseSWStructuredEthernetMessage_i _)
      parser.parseObjectNext()
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(api_EthernetFramesRx)
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
      val t = parser.parseObjectTypes(ISZ("util.EmptyContainer", "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P", "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS", "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P", "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS"))
      t.native match {
        case "util.EmptyContainer" => val r = parseutilEmptyContainerT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
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
      val t = parser.parseObjectTypes(ISZ("art.Empty", "Base_Types.Boolean_Payload", "Base_Types.Integer_Payload", "Base_Types.Integer_8_Payload", "Base_Types.Integer_16_Payload", "Base_Types.Integer_32_Payload", "Base_Types.Integer_64_Payload", "Base_Types.Unsigned_8_Payload", "Base_Types.Unsigned_16_Payload", "Base_Types.Unsigned_32_Payload", "Base_Types.Unsigned_64_Payload", "Base_Types.Float_Payload", "Base_Types.Float_32_Payload", "Base_Types.Float_64_Payload", "Base_Types.Character_Payload", "Base_Types.String_Payload", "Base_Types.Bits_Payload", "SW.ARP_Type_Payload", "util.EmptyContainer", "SW.FrameProtocol_Payload", "SW.InternetProtocol_Payload", "SW.RawEthernetMessage_Payload", "SW.StructuredEthernetMessage_i_Payload", "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P", "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS", "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P", "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P", "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P", "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS"))
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
        case "SW.ARP_Type_Payload" => val r = parseSWARP_Type_PayloadT(T); return r
        case "util.EmptyContainer" => val r = parseutilEmptyContainerT(T); return r
        case "SW.FrameProtocol_Payload" => val r = parseSWFrameProtocol_PayloadT(T); return r
        case "SW.InternetProtocol_Payload" => val r = parseSWInternetProtocol_PayloadT(T); return r
        case "SW.RawEthernetMessage_Payload" => val r = parseSWRawEthernetMessage_PayloadT(T); return r
        case "SW.StructuredEthernetMessage_i_Payload" => val r = parseSWStructuredEthernetMessage_i_PayloadT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(T); return r
        case "SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS" => val r = parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case "SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS" => val r = parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case "SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS" => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ => val r = parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
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

  def fromSWInternetProtocol_Payload(o: SW.InternetProtocol_Payload, isCompact: B): String = {
    val st = Printer.printSWInternetProtocol_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWInternetProtocol_Payload(s: String): Either[SW.InternetProtocol_Payload, Json.ErrorMsg] = {
    def fSWInternetProtocol_Payload(parser: Parser): SW.InternetProtocol_Payload = {
      val r = parser.parseSWInternetProtocol_Payload()
      return r
    }
    val r = to(s, fSWInternetProtocol_Payload _)
    return r
  }

  def fromSWFrameProtocol_Payload(o: SW.FrameProtocol_Payload, isCompact: B): String = {
    val st = Printer.printSWFrameProtocol_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFrameProtocol_Payload(s: String): Either[SW.FrameProtocol_Payload, Json.ErrorMsg] = {
    def fSWFrameProtocol_Payload(parser: Parser): SW.FrameProtocol_Payload = {
      val r = parser.parseSWFrameProtocol_Payload()
      return r
    }
    val r = to(s, fSWFrameProtocol_Payload _)
    return r
  }

  def fromSWARP_Type_Payload(o: SW.ARP_Type_Payload, isCompact: B): String = {
    val st = Printer.printSWARP_Type_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWARP_Type_Payload(s: String): Either[SW.ARP_Type_Payload, Json.ErrorMsg] = {
    def fSWARP_Type_Payload(parser: Parser): SW.ARP_Type_Payload = {
      val r = parser.parseSWARP_Type_Payload()
      return r
    }
    val r = to(s, fSWARP_Type_Payload _)
    return r
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

  def fromSWStructuredEthernetMessage_i(o: SW.StructuredEthernetMessage_i, isCompact: B): String = {
    val st = Printer.printSWStructuredEthernetMessage_i(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWStructuredEthernetMessage_i(s: String): Either[SW.StructuredEthernetMessage_i, Json.ErrorMsg] = {
    def fSWStructuredEthernetMessage_i(parser: Parser): SW.StructuredEthernetMessage_i = {
      val r = parser.parseSWStructuredEthernetMessage_i()
      return r
    }
    val r = to(s, fSWStructuredEthernetMessage_i _)
    return r
  }

  def fromSWStructuredEthernetMessage_i_Payload(o: SW.StructuredEthernetMessage_i_Payload, isCompact: B): String = {
    val st = Printer.printSWStructuredEthernetMessage_i_Payload(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWStructuredEthernetMessage_i_Payload(s: String): Either[SW.StructuredEthernetMessage_i_Payload, Json.ErrorMsg] = {
    def fSWStructuredEthernetMessage_i_Payload(parser: Parser): SW.StructuredEthernetMessage_i_Payload = {
      val r = parser.parseSWStructuredEthernetMessage_i_Payload()
      return r
    }
    val r = to(s, fSWStructuredEthernetMessage_i_Payload _)
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

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(s: String): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(parser: Parser): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = {
      val r = parser.parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(s: String): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(parser: Parser): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
      val r = parser.parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(s: String): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(parser: Parser): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
      val r = parser.parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(s: String): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(parser: Parser): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = {
      val r = parser.parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(s: String): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(parser: Parser): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
      val r = parser.parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(s: String): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS, Json.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(parser: Parser): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
      val r = parser.parseSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS()
      return r
    }
    val r = to(s, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(s: String): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container, Json.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(parser: Parser): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container = {
      val r = parser.parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container()
      return r
    }
    val r = to(s, fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(s: String): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P, Json.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(parser: Parser): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
      val r = parser.parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P()
      return r
    }
    val r = to(s, fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(s: String): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS, Json.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(parser: Parser): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
      val r = parser.parseSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS()
      return r
    }
    val r = to(s, fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(s: String): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container, Json.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(parser: Parser): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container = {
      val r = parser.parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container()
      return r
    }
    val r = to(s, fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(s: String): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P, Json.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(parser: Parser): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
      val r = parser.parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P()
      return r
    }
    val r = to(s, fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(s: String): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS, Json.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(parser: Parser): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
      val r = parser.parseSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS()
      return r
    }
    val r = to(s, fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(s: String): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(parser: Parser): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(s: String): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(parser: Parser): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(s: String): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(parser: Parser): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(s: String): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(parser: Parser): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(s: String): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(parser: Parser): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS, isCompact: B): String = {
    val st = Printer.printSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
    if (isCompact) {
      return st.renderCompact
    } else {
      return st.render
    }
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(s: String): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS, Json.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(parser: Parser): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      val r = parser.parseSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS()
      return r
    }
    val r = to(s, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _)
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