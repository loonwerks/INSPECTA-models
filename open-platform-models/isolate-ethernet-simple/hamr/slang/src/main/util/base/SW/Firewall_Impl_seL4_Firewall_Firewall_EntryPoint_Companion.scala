// #Sireum

package base.SW

import org.sireum._
import art._
import base._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object Firewall_Impl_seL4_Firewall_Firewall_EntryPoint_Companion {

  var preStateContainer_wL: Option[Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS] = None()

  def pre_initialise(): Unit = {
    // assume/require contracts cannot refer to incoming ports or
    // state variables so nothing to do here
  }

  def post_initialise(): Unit = {
    // block the component while its post-state values are retrieved
    val postStateContainer_wL =
      Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(
        api_EthernetFramesRxOut = 
          if (Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesRxOut_Id).nonEmpty)
            Some(Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesRxOut_Id).get.asInstanceOf[SW.StructuredEthernetMessage_i_Payload].value)
          else None(),
        api_EthernetFramesTxOut = 
          if (Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesTxOut_Id).nonEmpty)
            Some(Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesTxOut_Id).get.asInstanceOf[SW.StructuredEthernetMessage_i_Payload].value)
          else None())

    // the rest can now be performed via a different thread
    base.runtimemonitor.RuntimeMonitor.observeInitialisePostState(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.id, base.runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_Firewall_Firewall_postInit, postStateContainer_wL)
  }

  def pre_compute(): Unit = {
    // block the component while its pre-state values are retrieved
    preStateContainer_wL = Some(
      Firewall_Impl_seL4_Firewall_Firewall_PreState_Container_PS(
        api_EthernetFramesRxIn = 
          if (Art.observeInPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesRxIn_Id).nonEmpty)
            Some(Art.observeInPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesRxIn_Id).get.asInstanceOf[SW.StructuredEthernetMessage_i_Payload].value)
          else None(), 
        api_EthernetFramesTxIn = 
          if (Art.observeInPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesTxIn_Id).nonEmpty)
            Some(Art.observeInPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesTxIn_Id).get.asInstanceOf[SW.StructuredEthernetMessage_i_Payload].value)
          else None()))

    // the rest can now be performed via a different thread
    base.runtimemonitor.RuntimeMonitor.observeComputePreState(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.id, base.runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_Firewall_Firewall_preCompute, preStateContainer_wL.asInstanceOf[Option[art.DataContent]])
  }

  def post_compute(): Unit = {
    // block the component while its post-state values are retrieved
    val postStateContainer_wL =
      Firewall_Impl_seL4_Firewall_Firewall_PostState_Container_PS(
        api_EthernetFramesRxOut = 
          if (Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesRxOut_Id).nonEmpty)
            Some(Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesRxOut_Id).get.asInstanceOf[SW.StructuredEthernetMessage_i_Payload].value)
          else None(),
        api_EthernetFramesTxOut = 
          if (Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesTxOut_Id).nonEmpty)
            Some(Art.observeOutPortVariable(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.operational_api.EthernetFramesTxOut_Id).get.asInstanceOf[SW.StructuredEthernetMessage_i_Payload].value)
          else None())

    // the rest can now be performed via a different thread
    base.runtimemonitor.RuntimeMonitor.observeComputePrePostState(Arch.ZCU102_Impl_Instance_seL4_Firewall_Firewall.id, base.runtimemonitor.ObservationKind.ZCU102_Impl_Instance_seL4_Firewall_Firewall_postCompute, preStateContainer_wL.asInstanceOf[Option[art.DataContent]], postStateContainer_wL)
  }
}