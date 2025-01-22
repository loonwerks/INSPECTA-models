// #Sireum
// @formatter:off

// This file is auto-generated from InternetProtocol.scala, FrameProtocol.scala, ARP_Type.scala, RawEthernetMessage.scala, StructuredEthernetMessage_i.scala, Base_Types.scala, GUMBO__Library.scala, ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Containers.scala, Firewall_Impl_seL4_Firewall_Firewall_Containers.scala, LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Containers.scala, ObservationKind.scala, Container.scala, DataContent.scala, Aux_Types.scala

package base

import org.sireum._

object MsgPack {

  object Constants {

    val SWInternetProtocol_Payload: Z = -32

    val SWFrameProtocol_Payload: Z = -31

    val SWARP_Type_Payload: Z = -30

    val SWRawEthernetMessage: Z = -29

    val SWRawEthernetMessage_Payload: Z = -28

    val SWStructuredEthernetMessage_i: Z = -27

    val SWStructuredEthernetMessage_i_Payload: Z = -26

    val Base_TypesBoolean_Payload: Z = -25

    val Base_TypesInteger_Payload: Z = -24

    val Base_TypesInteger_8_Payload: Z = -23

    val Base_TypesInteger_16_Payload: Z = -22

    val Base_TypesInteger_32_Payload: Z = -21

    val Base_TypesInteger_64_Payload: Z = -20

    val Base_TypesUnsigned_8_Payload: Z = -19

    val Base_TypesUnsigned_16_Payload: Z = -18

    val Base_TypesUnsigned_32_Payload: Z = -17

    val Base_TypesUnsigned_64_Payload: Z = -16

    val Base_TypesFloat_Payload: Z = -15

    val Base_TypesFloat_32_Payload: Z = -14

    val Base_TypesFloat_64_Payload: Z = -13

    val Base_TypesCharacter_Payload: Z = -12

    val Base_TypesString_Payload: Z = -11

    val Base_TypesBits_Payload: Z = -10

    val SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P: Z = -9

    val SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS: Z = -8

    val SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P: Z = -7

    val SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS: Z = -6

    val SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P: Z = -5

    val SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS: Z = -4

    val SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P: Z = -3

    val SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS: Z = -2

    val SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P: Z = -1

    val SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS: Z = 0

    val SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P: Z = 1

    val SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS: Z = 2

    val utilEmptyContainer: Z = 3

    val _artEmpty: Z = 4

  }

  object Writer {

    @record class Default(val writer: MessagePack.Writer.Impl) extends Writer

  }

  @msig trait Writer {

    def writer: MessagePack.Writer

    def writeSWInternetProtocolType(o: SW.InternetProtocol.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeSWInternetProtocol_Payload(o: SW.InternetProtocol_Payload): Unit = {
      writer.writeZ(Constants.SWInternetProtocol_Payload)
      writeSWInternetProtocolType(o.value)
    }

    def writeSWFrameProtocolType(o: SW.FrameProtocol.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeSWFrameProtocol_Payload(o: SW.FrameProtocol_Payload): Unit = {
      writer.writeZ(Constants.SWFrameProtocol_Payload)
      writeSWFrameProtocolType(o.value)
    }

    def writeSWARP_TypeType(o: SW.ARP_Type.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeSWARP_Type_Payload(o: SW.ARP_Type_Payload): Unit = {
      writer.writeZ(Constants.SWARP_Type_Payload)
      writeSWARP_TypeType(o.value)
    }

    def writeSWRawEthernetMessageI(o: SW.RawEthernetMessage.I): Unit = {
      writer.writeZ(o.toZ)
    }

    def writeISSWRawEthernetMessageI[E](s: IS[SW.RawEthernetMessage.I, E], f: E => Unit) : Unit = {
      writer.writeArrayHeader(s.size)
      for (e <- s) {
        f(e)
      }
    }

    def writeSWRawEthernetMessage(o: SW.RawEthernetMessage): Unit = {
      writer.writeZ(Constants.SWRawEthernetMessage)
      writeISSWRawEthernetMessageI(o.value, writer.writeU8 _)
    }

    def writeSWRawEthernetMessage_Payload(o: SW.RawEthernetMessage_Payload): Unit = {
      writer.writeZ(Constants.SWRawEthernetMessage_Payload)
      writeSWRawEthernetMessage(o.value)
    }

    def writeSWStructuredEthernetMessage_i(o: SW.StructuredEthernetMessage_i): Unit = {
      writer.writeZ(Constants.SWStructuredEthernetMessage_i)
      writer.writeB(o.malformedFrame)
      writeSWInternetProtocolType(o.internetProtocol)
      writeSWFrameProtocolType(o.frameProtocol)
      writer.writeB(o.portIsWhitelisted)
      writeSWARP_TypeType(o.arpType)
      writeSWRawEthernetMessage(o.rawMessage)
    }

    def writeSWStructuredEthernetMessage_i_Payload(o: SW.StructuredEthernetMessage_i_Payload): Unit = {
      writer.writeZ(Constants.SWStructuredEthernetMessage_i_Payload)
      writeSWStructuredEthernetMessage_i(o.value)
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

    def writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container): Unit = {
      o match {
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
      }
    }

    def writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P): Unit = {
      writer.writeZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P)
      writer.writeOption(o.api_EthernetFramesRx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS)
      writer.writeOption(o.api_EthernetFramesRx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container): Unit = {
      o match {
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
      }
    }

    def writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P): Unit = {
      writer.writeZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P)
      writer.writeOption(o.api_EthernetFramesTx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS)
      writer.writeOption(o.api_EthernetFramesTx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container): Unit = {
      o match {
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
      }
    }

    def writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P): Unit = {
      writer.writeZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P)
      writer.writeOption(o.api_EthernetFramesRxIn, writeSWStructuredEthernetMessage_i _)
      writer.writeOption(o.api_EthernetFramesTxIn, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS)
      writer.writeOption(o.api_EthernetFramesRxIn, writeSWStructuredEthernetMessage_i _)
      writer.writeOption(o.api_EthernetFramesTxIn, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container): Unit = {
      o match {
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
      }
    }

    def writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P): Unit = {
      writer.writeZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P)
      writer.writeOption(o.api_EthernetFramesRxOut, writeSWStructuredEthernetMessage_i _)
      writer.writeOption(o.api_EthernetFramesTxOut, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS)
      writer.writeOption(o.api_EthernetFramesRxOut, writeSWStructuredEthernetMessage_i _)
      writer.writeOption(o.api_EthernetFramesTxOut, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container): Unit = {
      o match {
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
      }
    }

    def writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P): Unit = {
      writer.writeZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P)
      writer.writeOption(o.api_EthernetFramesTx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS)
      writer.writeOption(o.api_EthernetFramesTx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container): Unit = {
      o match {
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
      }
    }

    def writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P): Unit = {
      writer.writeZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P)
      writer.writeOption(o.api_EthernetFramesRx, writeSWStructuredEthernetMessage_i _)
    }

    def writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS)
      writer.writeOption(o.api_EthernetFramesRx, writeSWStructuredEthernetMessage_i _)
    }

    def writeruntimemonitorObservationKindType(o: runtimemonitor.ObservationKind.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeutilContainer(o: util.Container): Unit = {
      o match {
        case o: util.EmptyContainer => writeutilEmptyContainer(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
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
        case o: SW.ARP_Type_Payload => writeSWARP_Type_Payload(o)
        case o: util.EmptyContainer => writeutilEmptyContainer(o)
        case o: SW.FrameProtocol_Payload => writeSWFrameProtocol_Payload(o)
        case o: SW.InternetProtocol_Payload => writeSWInternetProtocol_Payload(o)
        case o: SW.RawEthernetMessage_Payload => writeSWRawEthernetMessage_Payload(o)
        case o: SW.StructuredEthernetMessage_i_Payload => writeSWStructuredEthernetMessage_i_Payload(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
        case o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
        case o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
        case o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
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

    def readSWInternetProtocolType(): SW.InternetProtocol.Type = {
      val r = reader.readZ()
      return SW.InternetProtocol.byOrdinal(r).get
    }

    def readSWInternetProtocol_Payload(): SW.InternetProtocol_Payload = {
      val r = readSWInternetProtocol_PayloadT(F)
      return r
    }

    def readSWInternetProtocol_PayloadT(typeParsed: B): SW.InternetProtocol_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWInternetProtocol_Payload)
      }
      val value = readSWInternetProtocolType()
      return SW.InternetProtocol_Payload(value)
    }

    def readSWFrameProtocolType(): SW.FrameProtocol.Type = {
      val r = reader.readZ()
      return SW.FrameProtocol.byOrdinal(r).get
    }

    def readSWFrameProtocol_Payload(): SW.FrameProtocol_Payload = {
      val r = readSWFrameProtocol_PayloadT(F)
      return r
    }

    def readSWFrameProtocol_PayloadT(typeParsed: B): SW.FrameProtocol_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWFrameProtocol_Payload)
      }
      val value = readSWFrameProtocolType()
      return SW.FrameProtocol_Payload(value)
    }

    def readSWARP_TypeType(): SW.ARP_Type.Type = {
      val r = reader.readZ()
      return SW.ARP_Type.byOrdinal(r).get
    }

    def readSWARP_Type_Payload(): SW.ARP_Type_Payload = {
      val r = readSWARP_Type_PayloadT(F)
      return r
    }

    def readSWARP_Type_PayloadT(typeParsed: B): SW.ARP_Type_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWARP_Type_Payload)
      }
      val value = readSWARP_TypeType()
      return SW.ARP_Type_Payload(value)
    }

    def readSWRawEthernetMessageI(): SW.RawEthernetMessage.I = {
      val n = reader.readZ()
      return SW.RawEthernetMessage.I.fromZ(n)
    }

    def readISSWRawEthernetMessageI[E](f: () => E): IS[SW.RawEthernetMessage.I, E] = {
      val size = reader.readArrayHeader()
      var r = IS[SW.RawEthernetMessage.I, E]()
      var i = 0
      while (i < size) {
        val o = f()
        r = r :+ o
        i = i + 1
      }
      return r
    }

    def readSWRawEthernetMessage(): SW.RawEthernetMessage = {
      val r = readSWRawEthernetMessageT(F)
      return r
    }

    def readSWRawEthernetMessageT(typeParsed: B): SW.RawEthernetMessage = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWRawEthernetMessage)
      }
      val value = readISSWRawEthernetMessageI(reader.readU8 _)
      return SW.RawEthernetMessage(value)
    }

    def readSWRawEthernetMessage_Payload(): SW.RawEthernetMessage_Payload = {
      val r = readSWRawEthernetMessage_PayloadT(F)
      return r
    }

    def readSWRawEthernetMessage_PayloadT(typeParsed: B): SW.RawEthernetMessage_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWRawEthernetMessage_Payload)
      }
      val value = readSWRawEthernetMessage()
      return SW.RawEthernetMessage_Payload(value)
    }

    def readSWStructuredEthernetMessage_i(): SW.StructuredEthernetMessage_i = {
      val r = readSWStructuredEthernetMessage_iT(F)
      return r
    }

    def readSWStructuredEthernetMessage_iT(typeParsed: B): SW.StructuredEthernetMessage_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWStructuredEthernetMessage_i)
      }
      val malformedFrame = reader.readB()
      val internetProtocol = readSWInternetProtocolType()
      val frameProtocol = readSWFrameProtocolType()
      val portIsWhitelisted = reader.readB()
      val arpType = readSWARP_TypeType()
      val rawMessage = readSWRawEthernetMessage()
      return SW.StructuredEthernetMessage_i(malformedFrame, internetProtocol, frameProtocol, portIsWhitelisted, arpType, rawMessage)
    }

    def readSWStructuredEthernetMessage_i_Payload(): SW.StructuredEthernetMessage_i_Payload = {
      val r = readSWStructuredEthernetMessage_i_PayloadT(F)
      return r
    }

    def readSWStructuredEthernetMessage_i_PayloadT(typeParsed: B): SW.StructuredEthernetMessage_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWStructuredEthernetMessage_i_Payload)
      }
      val value = readSWStructuredEthernetMessage_i()
      return SW.StructuredEthernetMessage_i_Payload(value)
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

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container.")
          val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T)
          return r
      }
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
      val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(F)
      return r
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P)
      }
      val api_EthernetFramesRx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(api_EthernetFramesRx)
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
      val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(F)
      return r
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS)
      }
      val api_EthernetFramesRx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(api_EthernetFramesRx)
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container.")
          val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T)
          return r
      }
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
      val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(F)
      return r
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P)
      }
      val api_EthernetFramesTx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(api_EthernetFramesTx)
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
      val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(F)
      return r
    }

    def readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(typeParsed: B): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS)
      }
      val api_EthernetFramesTx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(api_EthernetFramesTx)
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container.")
          val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T)
          return r
      }
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
      val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(F)
      return r
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P)
      }
      val api_EthernetFramesRxIn = reader.readOption(readSWStructuredEthernetMessage_i _)
      val api_EthernetFramesTxIn = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
      val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(F)
      return r
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS)
      }
      val api_EthernetFramesRxIn = reader.readOption(readSWStructuredEthernetMessage_i _)
      val api_EthernetFramesTxIn = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(api_EthernetFramesRxIn, api_EthernetFramesTxIn)
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container.")
          val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T)
          return r
      }
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
      val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(F)
      return r
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P)
      }
      val api_EthernetFramesRxOut = reader.readOption(readSWStructuredEthernetMessage_i _)
      val api_EthernetFramesTxOut = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
      val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(F)
      return r
    }

    def readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(typeParsed: B): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS)
      }
      val api_EthernetFramesRxOut = reader.readOption(readSWStructuredEthernetMessage_i _)
      val api_EthernetFramesTxOut = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(api_EthernetFramesRxOut, api_EthernetFramesTxOut)
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container.")
          val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T)
          return r
      }
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(F)
      return r
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P)
      }
      val api_EthernetFramesTx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(api_EthernetFramesTx)
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(F)
      return r
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS)
      }
      val api_EthernetFramesTx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(api_EthernetFramesTx)
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container.")
          val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T)
          return r
      }
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(F)
      return r
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P)
      }
      val api_EthernetFramesRx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(api_EthernetFramesRx)
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(F)
      return r
    }

    def readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(typeParsed: B): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS)
      }
      val api_EthernetFramesRx = reader.readOption(readSWStructuredEthernetMessage_i _)
      return SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(api_EthernetFramesRx)
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
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of util.Container.")
          val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T)
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
        case Constants.SWARP_Type_Payload => val r = readSWARP_Type_PayloadT(T); return r
        case Constants.utilEmptyContainer => val r = readutilEmptyContainerT(T); return r
        case Constants.SWFrameProtocol_Payload => val r = readSWFrameProtocol_PayloadT(T); return r
        case Constants.SWInternetProtocol_Payload => val r = readSWInternetProtocol_PayloadT(T); return r
        case Constants.SWRawEthernetMessage_Payload => val r = readSWRawEthernetMessage_PayloadT(T); return r
        case Constants.SWStructuredEthernetMessage_i_Payload => val r = readSWStructuredEthernetMessage_i_PayloadT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PST(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PT(T); return r
        case Constants.SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS => val r = readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PST(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PT(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PST(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PT(T); return r
        case Constants.SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS => val r = readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PST(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PT(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PST(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PT(T); return r
        case Constants.SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS => val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of art.DataContent.")
          val r = readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PST(T)
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

  def fromSWInternetProtocol_Payload(o: SW.InternetProtocol_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWInternetProtocol_Payload(o)
    return w.result
  }

  def toSWInternetProtocol_Payload(data: ISZ[U8]): Either[SW.InternetProtocol_Payload, MessagePack.ErrorMsg] = {
    def fSWInternetProtocol_Payload(reader: Reader): SW.InternetProtocol_Payload = {
      val r = reader.readSWInternetProtocol_Payload()
      return r
    }
    val r = to(data, fSWInternetProtocol_Payload _)
    return r
  }

  def fromSWFrameProtocol_Payload(o: SW.FrameProtocol_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFrameProtocol_Payload(o)
    return w.result
  }

  def toSWFrameProtocol_Payload(data: ISZ[U8]): Either[SW.FrameProtocol_Payload, MessagePack.ErrorMsg] = {
    def fSWFrameProtocol_Payload(reader: Reader): SW.FrameProtocol_Payload = {
      val r = reader.readSWFrameProtocol_Payload()
      return r
    }
    val r = to(data, fSWFrameProtocol_Payload _)
    return r
  }

  def fromSWARP_Type_Payload(o: SW.ARP_Type_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWARP_Type_Payload(o)
    return w.result
  }

  def toSWARP_Type_Payload(data: ISZ[U8]): Either[SW.ARP_Type_Payload, MessagePack.ErrorMsg] = {
    def fSWARP_Type_Payload(reader: Reader): SW.ARP_Type_Payload = {
      val r = reader.readSWARP_Type_Payload()
      return r
    }
    val r = to(data, fSWARP_Type_Payload _)
    return r
  }

  def fromSWRawEthernetMessage(o: SW.RawEthernetMessage, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWRawEthernetMessage(o)
    return w.result
  }

  def toSWRawEthernetMessage(data: ISZ[U8]): Either[SW.RawEthernetMessage, MessagePack.ErrorMsg] = {
    def fSWRawEthernetMessage(reader: Reader): SW.RawEthernetMessage = {
      val r = reader.readSWRawEthernetMessage()
      return r
    }
    val r = to(data, fSWRawEthernetMessage _)
    return r
  }

  def fromSWRawEthernetMessage_Payload(o: SW.RawEthernetMessage_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWRawEthernetMessage_Payload(o)
    return w.result
  }

  def toSWRawEthernetMessage_Payload(data: ISZ[U8]): Either[SW.RawEthernetMessage_Payload, MessagePack.ErrorMsg] = {
    def fSWRawEthernetMessage_Payload(reader: Reader): SW.RawEthernetMessage_Payload = {
      val r = reader.readSWRawEthernetMessage_Payload()
      return r
    }
    val r = to(data, fSWRawEthernetMessage_Payload _)
    return r
  }

  def fromSWStructuredEthernetMessage_i(o: SW.StructuredEthernetMessage_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWStructuredEthernetMessage_i(o)
    return w.result
  }

  def toSWStructuredEthernetMessage_i(data: ISZ[U8]): Either[SW.StructuredEthernetMessage_i, MessagePack.ErrorMsg] = {
    def fSWStructuredEthernetMessage_i(reader: Reader): SW.StructuredEthernetMessage_i = {
      val r = reader.readSWStructuredEthernetMessage_i()
      return r
    }
    val r = to(data, fSWStructuredEthernetMessage_i _)
    return r
  }

  def fromSWStructuredEthernetMessage_i_Payload(o: SW.StructuredEthernetMessage_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWStructuredEthernetMessage_i_Payload(o)
    return w.result
  }

  def toSWStructuredEthernetMessage_i_Payload(data: ISZ[U8]): Either[SW.StructuredEthernetMessage_i_Payload, MessagePack.ErrorMsg] = {
    def fSWStructuredEthernetMessage_i_Payload(reader: Reader): SW.StructuredEthernetMessage_i_Payload = {
      val r = reader.readSWStructuredEthernetMessage_i_Payload()
      return r
    }
    val r = to(data, fSWStructuredEthernetMessage_i_Payload _)
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

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(o)
    return w.result
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(data: ISZ[U8]): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container, MessagePack.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container(reader: Reader): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container = {
      val r = reader.readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container()
      return r
    }
    val r = to(data, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(o)
    return w.result
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(data: ISZ[U8]): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P(reader: Reader): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P = {
      val r = reader.readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P()
      return r
    }
    val r = to(data, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(o)
    return w.result
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(data: ISZ[U8]): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS(reader: Reader): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS = {
      val r = reader.readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS()
      return r
    }
    val r = to(data, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(o)
    return w.result
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(data: ISZ[U8]): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container, MessagePack.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container(reader: Reader): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container = {
      val r = reader.readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container()
      return r
    }
    val r = to(data, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(o)
    return w.result
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(data: ISZ[U8]): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P(reader: Reader): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P = {
      val r = reader.readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P()
      return r
    }
    val r = to(data, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P _)
    return r
  }

  def fromSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o: SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(o)
    return w.result
  }

  def toSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(data: ISZ[U8]): Either[SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS(reader: Reader): SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
      val r = reader.readSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS()
      return r
    }
    val r = to(data, fSWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(o)
    return w.result
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(data: ISZ[U8]): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container, MessagePack.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container(reader: Reader): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container = {
      val r = reader.readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container()
      return r
    }
    val r = to(data, fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(o)
    return w.result
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(data: ISZ[U8]): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P(reader: Reader): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_P = {
      val r = reader.readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P()
      return r
    }
    val r = to(data, fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(o)
    return w.result
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(data: ISZ[U8]): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(reader: Reader): SW.Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS = {
      val r = reader.readSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS()
      return r
    }
    val r = to(data, fSWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(o)
    return w.result
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(data: ISZ[U8]): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container, MessagePack.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container(reader: Reader): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container = {
      val r = reader.readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container()
      return r
    }
    val r = to(data, fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(o)
    return w.result
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(data: ISZ[U8]): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P(reader: Reader): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_P = {
      val r = reader.readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P()
      return r
    }
    val r = to(data, fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P _)
    return r
  }

  def fromSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o: SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(o)
    return w.result
  }

  def toSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(data: ISZ[U8]): Either[SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(reader: Reader): SW.Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS = {
      val r = reader.readSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS()
      return r
    }
    val r = to(data, fSWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(o)
    return w.result
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(data: ISZ[U8]): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container, MessagePack.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container(reader: Reader): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container = {
      val r = reader.readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container()
      return r
    }
    val r = to(data, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(o)
    return w.result
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(data: ISZ[U8]): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P(reader: Reader): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
      val r = reader.readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P()
      return r
    }
    val r = to(data, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(o)
    return w.result
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(data: ISZ[U8]): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS(reader: Reader): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
      val r = reader.readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS()
      return r
    }
    val r = to(data, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(o)
    return w.result
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(data: ISZ[U8]): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container, MessagePack.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container(reader: Reader): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container = {
      val r = reader.readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container()
      return r
    }
    val r = to(data, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(o)
    return w.result
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(data: ISZ[U8]): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P(reader: Reader): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P = {
      val r = reader.readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P()
      return r
    }
    val r = to(data, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P _)
    return r
  }

  def fromSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o: SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(o)
    return w.result
  }

  def toSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(data: ISZ[U8]): Either[SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS(reader: Reader): SW.LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS = {
      val r = reader.readSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS()
      return r
    }
    val r = to(data, fSWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS _)
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