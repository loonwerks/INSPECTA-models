// #Sireum
package isolette.Operator_Interface

import org.sireum._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait Operator_Interface_Thread_i_operator_interface_oip_oit_Injection_Provider {
  def pre_receiveInput(): Unit
}

object Operator_Interface_Thread_i_operator_interface_oip_oit_Injection_Service {

  var providers: MSZ[Operator_Interface_Thread_i_operator_interface_oip_oit_Injection_Provider] = MSZ()

  def register(provider: Operator_Interface_Thread_i_operator_interface_oip_oit_Injection_Provider): Unit = {
    providers = providers :+ provider
  }

  def pre_receiveInput(): Unit = {
    for (provider <- providers) {
      provider.pre_receiveInput()
    }
  }
}