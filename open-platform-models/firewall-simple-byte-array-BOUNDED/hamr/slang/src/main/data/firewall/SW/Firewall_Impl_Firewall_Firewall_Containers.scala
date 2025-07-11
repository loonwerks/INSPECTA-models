// #Sireum

package firewall.SW

import org.sireum._
import firewall._
import firewall.util.Container

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

// containers for the pre and post state values of ports and state variables

@sig trait Firewall_Impl_Firewall_Firewall_PreState_Container extends Container {
  def api_EthernetFramesRxIn: Option[SW.RawEthernetMessage]
  def api_EthernetFramesTxIn: Option[SW.RawEthernetMessage]
}

// container for incoming ports
@datatype class Firewall_Impl_Firewall_Firewall_PreState_Container_P (
  val api_EthernetFramesRxIn: Option[SW.RawEthernetMessage],
  val api_EthernetFramesTxIn: Option[SW.RawEthernetMessage]) extends Firewall_Impl_Firewall_Firewall_PreState_Container

// container for incoming ports and state variables
@datatype class Firewall_Impl_Firewall_Firewall_PreState_Container_PS (
  val api_EthernetFramesRxIn: Option[SW.RawEthernetMessage],
  val api_EthernetFramesTxIn: Option[SW.RawEthernetMessage]) extends Firewall_Impl_Firewall_Firewall_PreState_Container

@sig trait Firewall_Impl_Firewall_Firewall_PostState_Container extends Container {
  def api_EthernetFramesRxOut: Option[SW.RawEthernetMessage]
  def api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]
}

// container for outgoing ports
@datatype class Firewall_Impl_Firewall_Firewall_PostState_Container_P (
  val api_EthernetFramesRxOut: Option[SW.RawEthernetMessage],
  val api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]) extends Firewall_Impl_Firewall_Firewall_PostState_Container

// container for outgoing ports and state variables
@datatype class Firewall_Impl_Firewall_Firewall_PostState_Container_PS (
  val api_EthernetFramesRxOut: Option[SW.RawEthernetMessage],
  val api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]) extends Firewall_Impl_Firewall_Firewall_PostState_Container
