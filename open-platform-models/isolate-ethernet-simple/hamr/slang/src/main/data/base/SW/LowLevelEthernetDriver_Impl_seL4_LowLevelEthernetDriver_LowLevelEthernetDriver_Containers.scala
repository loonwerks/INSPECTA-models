// #Sireum

package base.SW

import org.sireum._
import base._
import base.util.Container

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

// containers for the pre and post state values of ports and state variables

@sig trait LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container extends Container {
  def api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i]
}

// container for incoming ports
@datatype class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P (
  val api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i]) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container

// container for incoming ports and state variables
@datatype class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS (
  val api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i]) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container

@sig trait LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container extends Container {
  def api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i]
}

// container for outgoing ports
@datatype class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_P (
  val api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i]) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container

// container for outgoing ports and state variables
@datatype class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container_PS (
  val api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i]) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PostState_Container