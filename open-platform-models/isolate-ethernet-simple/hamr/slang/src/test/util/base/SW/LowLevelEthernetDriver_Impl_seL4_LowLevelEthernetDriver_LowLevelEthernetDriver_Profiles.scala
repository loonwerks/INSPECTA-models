// #Sireum

package base.SW

import org.sireum._
import base.util.Profile
import base.util.EmptyContainer
import base.RandomLib

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

// Profile for initialise entrypoint
@msig trait LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_Trait extends Profile

@record class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile (
  val name: String,
) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_Trait {

  override def next: EmptyContainer = {
    return EmptyContainer()
  }
}

// Profile with generators for incoming ports
@msig trait LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_P_Trait extends Profile {
  def api_EthernetFramesTx: RandomLib // random lib for generating SW.StructuredEthernetMessage_i
}

@record class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_P(
  val name: String,
  var api_EthernetFramesTx: RandomLib // random lib for generating SW.StructuredEthernetMessage_i
  ) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_P_Trait {

  override def next: LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P = {
    return (LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_P (
      api_EthernetFramesTx = api_EthernetFramesTx.nextOptionSWStructuredEthernetMessage_i()))
  }
}

// Profile with generators for state variables and incoming ports
@msig trait LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_PS_Trait extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_P_Trait {
  def api_EthernetFramesTx: RandomLib // random lib for generating SW.StructuredEthernetMessage_i
}

@record class LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_PS(
  val name: String,
  var api_EthernetFramesTx: RandomLib // random lib for generating SW.StructuredEthernetMessage_i
  ) extends LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Profile_PS_Trait {

  override def next: LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS = {
    return (LowLevelEthernetDriver_Impl_seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_PreState_Container_PS (
      api_EthernetFramesTx = api_EthernetFramesTx.nextOptionSWStructuredEthernetMessage_i()))
  }
}
