package firewall.SW

import org.sireum._
import firewall.SW._
import firewall.SW.Firewall_Impl_Firewall_Firewall._

// This file will not be overwritten so is safe to edit
class Firewall_Impl_Firewall_Firewall_Test extends Firewall_Impl_Firewall_Firewall_ScalaTest {

  val frame_empty_dest_addr: ByteArray = ISZ(0,0,2048)
  val frame_ipv6: ByteArray = ISZ(1,2,34525)
  val frame_ipv4: ByteArray = ISZ(3,4,2048)

  def firewall_combined_policies(inMsg: RawEthernetMessage, outMsgOpt: Option[RawEthernetMessage]) : B = {
    val policies_hold: B =
      (HLR_01(inMsg.value, msg_to_policy(outMsgOpt))
      && HLR_02(inMsg.value, msg_to_policy(outMsgOpt))
      && HLR_06_fix1(inMsg.value, msg_to_policy(outMsgOpt)))

    val copy_correct: B =
      (if (!outMsgOpt.isEmpty) (inMsg.value == outMsgOpt.get.value) else T)
    return (policies_hold && copy_correct)
  }

  test("test - hard code expected result") {
    assert (!can_send_frame(frame_empty_dest_addr))
  }

  test("test - HLR 01 - drop empty address") {
    val firewall_action = can_send_frame(frame_empty_dest_addr)
    println(s"Empty address [Yes] -- Can send: ${firewall_action}")
    assert (HLR_01(frame_empty_dest_addr,firewall_action))
  }

  test("test - HLR 01 - allow non-empty address") {
    val firewall_action = can_send_frame(frame_ipv4)
    println(s"Empty address [No] -- Can send: ${firewall_action}")
    assert (HLR_01(frame_ipv4,firewall_action))
  }

  test("test - HLR 02 - drop ipv6") {
    val firewall_action = can_send_frame(frame_ipv6)
    println(s"IPv6 [Yes] -- Can send: ${firewall_action}")
    assert (HLR_02(frame_ipv6,firewall_action))
  }

  test("test - HLR 02 - allow !ipv6") {
    val firewall_action = can_send_frame(frame_ipv4)
    println(s"IPv6 [No] -- Can send: ${firewall_action}")
    assert (HLR_02(frame_ipv4,firewall_action))
  }


//  test("Example Unit Test for Initialise Entry Point"){
//    // Initialise Entry Point doesn't read input port values, so just proceed with
//    // launching the entry point code
//    testInitialise()
//    // use get_XXX methods and check_concrete_output() from test/util/../YYY_TestApi
//    // retrieve values from output ports and check against expected results
//  }
//

//test ("Component: frame_empty_dest_addr"){
//  //  configure input test value
//  val inMsg = RawEthernetMessage(frame_empty_dest_addr)
//  put_EthernetFramesRxIn(inMsg)
//
//  testCompute()
//
//  val optRxOutMsg = get_EthernetFramesRxOut()
//  println(optRxOutMsg)
//  assert (firewall_combined_policies(inMsg,optRxOutMsg))
//  }

  test ("Component: ipv4"){
    //  configure input test value
    val inMsg = RawEthernetMessage(frame_ipv4)
    put_EthernetFramesRxIn(inMsg)

    testCompute()

    val optRxOutMsg = get_EthernetFramesRxOut()
    println(optRxOutMsg)
    assert (firewall_combined_policies(inMsg,optRxOutMsg))
  }

}
