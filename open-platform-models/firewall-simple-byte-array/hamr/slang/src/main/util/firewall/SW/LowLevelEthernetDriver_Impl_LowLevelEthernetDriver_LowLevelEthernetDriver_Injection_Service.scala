// #Sireum
package firewall.SW

import org.sireum._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Injection_Provider {
  def pre_receiveInput(): Unit
}

object LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Injection_Service {

  var providers: MSZ[LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Injection_Provider] = MSZ()

  def register(provider: LowLevelEthernetDriver_Impl_LowLevelEthernetDriver_LowLevelEthernetDriver_Injection_Provider): Unit = {
    providers = providers :+ provider
  }

  def pre_receiveInput(): Unit = {
    for (provider <- providers) {
      provider.pre_receiveInput()
    }
  }
}