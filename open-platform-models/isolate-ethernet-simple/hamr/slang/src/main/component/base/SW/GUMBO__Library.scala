// #Sireum

package base.SW

import org.sireum._
import base._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object GUMBO__Library {
  @strictpure def isMalformedFrame(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.malformedFrame

  @strictpure def getInternetProtocol(v: SW.StructuredEthernetMessage_i): SW.InternetProtocol.Type = v.internetProtocol

  @strictpure def isIPV4(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = SW.GUMBO__Library.getInternetProtocol(v) == SW.InternetProtocol.IPV4

  @strictpure def isIPV6(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.internetProtocol == SW.InternetProtocol.IPV6

  @strictpure def getFrameProtocol(v: SW.StructuredEthernetMessage_i): SW.FrameProtocol.Type = v.frameProtocol

  @strictpure def isTCP(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.frameProtocol == SW.FrameProtocol.TCP

  @strictpure def isARP(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.frameProtocol == SW.FrameProtocol.ARP

  @strictpure def isPortWhitelisted(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.portIsWhitelisted

  @strictpure def getARP_Type(v: SW.StructuredEthernetMessage_i): SW.ARP_Type.Type = v.arpType

  @strictpure def isARP_Request(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.arpType == SW.ARP_Type.REQUEST

  @strictpure def isARP_Reply(v: SW.StructuredEthernetMessage_i): Base_Types.Boolean = v.arpType == SW.ARP_Type.REPLY
}