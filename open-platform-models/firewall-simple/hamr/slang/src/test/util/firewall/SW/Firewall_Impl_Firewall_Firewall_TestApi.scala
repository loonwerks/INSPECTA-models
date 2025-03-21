// #Sireum

package firewall.SW

import org.sireum._
import art.Art
import firewall._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun
@msig trait Firewall_Impl_Firewall_Firewall_TestApi {

  def BeforeEntrypoint(): Unit = {
    Art.initTest(Arch.seL4_Impl_Instance_Firewall_Firewall)
  }

  def AfterEntrypoint(): Unit = {
    Art.finalizeTest(Arch.seL4_Impl_Instance_Firewall_Firewall)
  }

  def testCompute(): Unit = {
    Art.manuallyClearOutput()
    Art.testCompute(Arch.seL4_Impl_Instance_Firewall_Firewall)
  }

  def testInitialise(): Unit = {
    Art.manuallyClearOutput()
    Art.testInitialise(Arch.seL4_Impl_Instance_Firewall_Firewall)
  }

  /** helper function to set the values of all input ports.
   * @param EthernetFramesRxIn payloads for event data port EthernetFramesRxIn.
   *   ART currently supports single element event data queues so
   *   only the last element of EthernetFramesRxIn will be used
   */
  def put_concrete_inputs(EthernetFramesRxIn : ISZ[SW.RawEthernetMessage]): Unit = {
    for(v <- EthernetFramesRxIn){
      put_EthernetFramesRxIn(v)
    }
  }


  /** helper function to check Firewall_Impl_Firewall_Firewall's
   * output ports.  Use named arguments to check subsets of the output ports.
   * @param EthernetFramesRxOut method that will be called with the payloads to be sent
   *        on the outgoing event data port 'EthernetFramesRxOut'.
   */
  def check_concrete_output(EthernetFramesRxOut: ISZ[SW.RawEthernetMessage] => B): Unit = {
    var testFailures: ISZ[ST] = ISZ()

    var EthernetFramesRxOutValue: ISZ[SW.RawEthernetMessage] = ISZ()
    // TODO: event data port getter should return all of the events/payloads
    //       received on event data ports when queue sizes > 1 support is added
    //       to ART
    if(get_EthernetFramesRxOut().nonEmpty) { EthernetFramesRxOutValue = EthernetFramesRxOutValue :+ get_EthernetFramesRxOut().get }
    if(!EthernetFramesRxOut(EthernetFramesRxOutValue)) {
      testFailures = testFailures :+ st"'EthernetFramesRxOut' did not match expected: received ${EthernetFramesRxOutValue.size} events with the following payloads ${EthernetFramesRxOutValue}"
    }

    assert(testFailures.isEmpty, st"${(testFailures, "\n")}".render)
  }


  // setter for in EventDataPort
  def put_EthernetFramesRxIn(value : SW.RawEthernetMessage): Unit = {
    Art.insertInInfrastructurePort(Arch.seL4_Impl_Instance_Firewall_Firewall.operational_api.EthernetFramesRxIn_Id, SW.RawEthernetMessage_Payload(value))
  }

  // getter for out EventDataPort
  def get_EthernetFramesRxOut(): Option[SW.RawEthernetMessage] = {
    val value: Option[SW.RawEthernetMessage] = get_EthernetFramesRxOut_payload() match {
      case Some(SW.RawEthernetMessage_Payload(v)) => Some(v)
      case Some(v) => halt(s"Unexpected payload on port EthernetFramesRxOut.  Expecting 'SW.RawEthernetMessage_Payload' but received ${v}")
      case _ => None[SW.RawEthernetMessage]()
    }
    return value
  }

  // payload getter for out EventDataPort
  def get_EthernetFramesRxOut_payload(): Option[SW.RawEthernetMessage_Payload] = {
    return Art.observeOutInfrastructurePort(Arch.seL4_Impl_Instance_Firewall_Firewall.initialization_api.EthernetFramesRxOut_Id).asInstanceOf[Option[SW.RawEthernetMessage_Payload]]
  }

}
