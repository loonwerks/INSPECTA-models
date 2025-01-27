package base.SW

import base.GumboXUtil.GumboXResult
import base.SW.Firewall_Impl_seL4_Firewall_Firewall_UnitTestConfiguration_Util._
import base.util.{Container, UnitTestConfigurationBatch}
import org.sireum.Random.Impl.Xoshiro256
import org.sireum._
import base.RandomLib

// This file will not be overwritten so is safe to edit

class Firewall_Impl_seL4_Firewall_Firewall_GumboX_Manual extends Firewall_Impl_seL4_Firewall_Firewall_GumboX_TestHarness_ScalaTest {
  def freshRandomLib: RandomLib = {
    RandomLib(Random.Gen64Impl(Xoshiro256.create))
  }

  override def verbose: B = F

  test("Violate assumption 'onlyOneInEvent'") {

    val rxIn = freshRandomLib.nextSWStructuredEthernetMessage_i()
    val txIn = freshRandomLib.nextSWStructuredEthernetMessage_i()

    testComputeCB(api_EthernetFramesRxIn = Some(rxIn), api_EthernetFramesTxIn = Some(txIn)) match {
      case GumboXResult.Pre_Condition_Unsat =>
        println("Precondition violated")
      case x => assert (F, s"Wasn't expecting: $x")
    }
  }

  test ("Violate datatype invariant 'relateFrameProtocolToArpType'") {
    val rxIn = freshRandomLib.nextSWStructuredEthernetMessage_i()(
      frameProtocol = FrameProtocol.TCP, arpType = ARP_Type.REPLY)

    testComputeCB(api_EthernetFramesRxIn = Some(rxIn), api_EthernetFramesTxIn = None()) match {
      case GumboXResult.Pre_Condition_Unsat =>
        println("Precondition violated")
      case x => assert (F, s"Wasn't expecting: $x")
    }
  }

  test(" RC_INSPECTA_00-HLR-6: Allow RxIn + IPV4 + TCP + port whitelisted") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV4,
      frameProtocol = FrameProtocol.TCP,
      portIsWhitelisted = T,
      arpType = ARP_Type.NA,

      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    testComputeCB(api_EthernetFramesRxIn = Some(msg), api_EthernetFramesTxIn = None()) match {
      case GumboXResult.Post_Condition_Pass =>
        println("Post-condition held")
      case x => assert (F, s"Wasn't expecting $x")
    }
  }
}
