// #Sireum

package base.SW

import org.sireum._
import art._
import base.SystemTestSuiteSlang.runtimeMonitorStream
import base._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object ArduPilot_Impl_seL4_ArduPilot_ArduPilot_SystemTestAPI {
  /** helper method to set the values of all incoming ports
    * @param api_EthernetFramesRx incoming event data port
    */
  def put_concrete_inputs(api_EthernetFramesRx: Option[SW.StructuredEthernetMessage_i]): Unit = {
    put_EthernetFramesRx(api_EthernetFramesRx)
  }

  // setter for incoming event data port
  def put_EthernetFramesRx(value: Option[SW.StructuredEthernetMessage_i]): Unit = {
    value match {
      case Some(v) => Art.insertInInfrastructurePort(Arch.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot.operational_api.EthernetFramesRx_Id, SW.StructuredEthernetMessage_i_Payload(v))
      case _ =>
    }
  }

  def fetchContainer(): base.SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS = {
    if (runtimeMonitorStream.contains(Arch.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot.id)) {
      val (_, postContainer_) = runtimeMonitorStream.get(Arch.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot.id).get
      return postContainer_.asInstanceOf[base.SW.ArduPilot_Impl_seL4_ArduPilot_ArduPilot_PostState_Container_PS]
    }
    else {
      assert(F, s"No post state recorded for ${Arch.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot.name}")
      halt(s"No post state recorded for ${Arch.ZCU102_Impl_Instance_seL4_ArduPilot_ArduPilot.name}")
    }
  }

  def check_concrete_outputs(api_EthernetFramesTx: Option[SW.StructuredEthernetMessage_i]): Unit = {
    var failureReasons: ISZ[ST] = ISZ()

    val actual_EthernetFramesTx = get_api_EthernetFramesTx()
    if (api_EthernetFramesTx != actual_EthernetFramesTx) {
      failureReasons = failureReasons :+ st"'EthernetFramesTx' did not match expected.  Expected: $api_EthernetFramesTx, Actual: $actual_EthernetFramesTx"
    }

    assert(failureReasons.isEmpty, st"${(failureReasons, "\n")}".render)
  }

  def get_api_EthernetFramesTx(): Option[SW.StructuredEthernetMessage_i] = {
    return fetchContainer().api_EthernetFramesTx
  }
}