// #Sireum

package base

import org.sireum._
import org.sireum.Random.Gen64

/*
GENERATED FROM

InternetProtocol.scala

FrameProtocol.scala

ARP_Type.scala

RawEthernetMessage.scala

StructuredEthernetMessage_i.scala

Base_Types.scala

GUMBO__Library.scala

ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Containers.scala

Firewall_Impl_seL4_Firewall_Firewall_Containers.scala

LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Containers.scala

ObservationKind.scala

Container.scala

DataContent.scala

Aux_Types.scala

*/

@enum object _artDataContent_DataTypeId {
   "_artEmpty_Id"
   "Base_TypesBits_Payload_Id"
   "Base_TypesBoolean_Payload_Id"
   "Base_TypesCharacter_Payload_Id"
   "Base_TypesFloat_32_Payload_Id"
   "Base_TypesFloat_64_Payload_Id"
   "Base_TypesFloat_Payload_Id"
   "Base_TypesInteger_16_Payload_Id"
   "Base_TypesInteger_32_Payload_Id"
   "Base_TypesInteger_64_Payload_Id"
   "Base_TypesInteger_8_Payload_Id"
   "Base_TypesInteger_Payload_Id"
   "Base_TypesString_Payload_Id"
   "Base_TypesUnsigned_16_Payload_Id"
   "Base_TypesUnsigned_32_Payload_Id"
   "Base_TypesUnsigned_64_Payload_Id"
   "Base_TypesUnsigned_8_Payload_Id"
   "SWARP_Type_Payload_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id"
   "SWFrameProtocol_Payload_Id"
   "SWInternetProtocol_Payload_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id"
   "SWRawEthernetMessage_Payload_Id"
   "SWStructuredEthernetMessage_i_Payload_Id"
   "utilEmptyContainer_Id"
}

@enum object SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_DataTypeId {
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id"
}

@enum object SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_DataTypeId {
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id"
}

@enum object SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_DataTypeId {
   "SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id"
}

@enum object SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_DataTypeId {
   "SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id"
}

@enum object SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_DataTypeId {
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id"
}

@enum object SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_DataTypeId {
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id"
}

@enum object utilContainer_DataTypeId {
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_P_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_P_Id"
   "SWArduPilot_Impl_seL4_ArduPilot_ArduPilot_PreState_Container_PS_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_P_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_P_Id"
   "SWFirewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P_Id"
   "SWLowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS_Id"
   "utilEmptyContainer_Id"
}

