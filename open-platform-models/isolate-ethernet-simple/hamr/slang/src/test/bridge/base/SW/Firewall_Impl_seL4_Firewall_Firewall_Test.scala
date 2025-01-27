package base.SW

import base.RandomLib
import org.sireum._
import base.SW._
import org.sireum.Random.Impl.Xoshiro256

// This file will not be overwritten so is safe to edit
class Firewall_Impl_seL4_Firewall_Firewall_Test extends Firewall_Impl_seL4_Firewall_Firewall_ScalaTest {

  def freshRandomLib: RandomLib = {
    RandomLib(Random.Gen64Impl(Xoshiro256.create))
  }
/*
  test("Example Unit Test for Initialise Entry Point"){
    // Initialise Entry Point doesn't read input port values, so just proceed with
    // launching the entry point code
    testInitialise()
    // use get_XXX methods and check_concrete_output() from test/util/../YYY_TestApi
    // retrieve values from output ports and check against expected results
  }

  test("Example Unit Test for Compute Entry Point"){
    // use put_XXX methods from test/util/../YYY_TestApi to seed input ports with values
    testCompute()
    // use get_XXX methods and check_concrete_output() from test/util/../YYY_TestApi
    // retrieve values from output ports and check against expected results
  }
*/
  test("RC_INSPECTA_00-HLR-6: Allow RxIn + IPV4 + TCP + port whitelisted") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV4,
      frameProtocol = FrameProtocol.TCP,
      portIsWhitelisted = T,
      arpType = ARP_Type.NA,

      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesRxIn(msg)

    testCompute()

    get_EthernetFramesRxOut() match {
      case Some(payload) => assert (payload == msg)
      case _ => assert (F)
    }

    assert (get_EthernetFramesTxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-4: Drop RxIn + IPV4 + TCP + port not whitelisted") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV4,
      frameProtocol = FrameProtocol.TCP,
      portIsWhitelisted = F,
      arpType = ARP_Type.NA,

      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesRxIn(msg)

    testCompute()

    assert (get_EthernetFramesRxOut().isEmpty)

    assert (get_EthernetFramesTxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-5: RxIn + IPV4 + ARP Request ~> ARP Reply") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV4,
      frameProtocol = FrameProtocol.ARP,
      arpType = ARP_Type.REQUEST,

      portIsWhitelisted = freshRandomLib.nextB(),
      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesRxIn(msg)

    testCompute()

    get_EthernetFramesTxOut() match {
      case Some(payload) => assert (payload.arpType == ARP_Type.REPLY, "Expected arp reply on TxOut")
      case _ => assert(F)
    }

    assert (get_EthernetFramesRxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-5: Drop RxIn + IPV4 + ARP Reply") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV4,
      frameProtocol = FrameProtocol.ARP,
      arpType = ARP_Type.REPLY,

      portIsWhitelisted = freshRandomLib.nextB(),
      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesRxIn(msg)

    testCompute()

    assert (get_EthernetFramesTxOut().isEmpty)

    assert (get_EthernetFramesRxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-2: Drop RxIn + IPV6") {
    val arpType = freshRandomLib.nextSWARP_TypeType()
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV6,

      frameProtocol = if (arpType == ARP_Type.NA) FrameProtocol.TCP else FrameProtocol.ARP,
      arpType = arpType,
      portIsWhitelisted = freshRandomLib.nextB(),
      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesRxIn(msg)

    testCompute()

    assert (get_EthernetFramesTxOut().isEmpty)

    assert (get_EthernetFramesRxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-7: Allow TxIn + IPV4") {
    val arpType = freshRandomLib.nextSWARP_TypeType()
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV4,

      frameProtocol = if (arpType == ARP_Type.NA) FrameProtocol.TCP else FrameProtocol.ARP,
      arpType = arpType,
      portIsWhitelisted = freshRandomLib.nextB(),
      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesTxIn(msg)

    testCompute()

    get_EthernetFramesTxOut() match {
      case Some(payload) => assert(payload == msg)
      case _ => assert (F)
    }

    assert (get_EthernetFramesRxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-7: Allow TxIn + IPV6 + ARP") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV6,
      frameProtocol = FrameProtocol.ARP,
      arpType = ARP_Type.REPLY,

      portIsWhitelisted = freshRandomLib.nextB(),
      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesTxIn(msg)

    testCompute()

    get_EthernetFramesTxOut() match {
      case Some(payload) => assert(payload == msg)
      case _ => assert (F)
    }

    assert (get_EthernetFramesRxOut().isEmpty)
  }

  test("RC_INSPECTA_00-HLR-7: Drop TxIn + IPV6 + TCP") {
    val msg = StructuredEthernetMessage_i(
      internetProtocol = InternetProtocol.IPV6,
      frameProtocol = FrameProtocol.TCP,
      arpType = ARP_Type.NA,

      portIsWhitelisted = freshRandomLib.nextB(),
      malformedFrame = freshRandomLib.nextB(),
      rawMessage = freshRandomLib.nextSWRawEthernetMessage())

    put_EthernetFramesTxIn(msg)

    testCompute()

    assert (get_EthernetFramesTxOut().isEmpty)

    assert (get_EthernetFramesRxOut().isEmpty)
  }
}
