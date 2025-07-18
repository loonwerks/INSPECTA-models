// #Sireum

package firewall.SW

import org.sireum._
import firewall._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun
object Firewall_Impl_Firewall_Firewall_GumboX {
  /** Compute Entrypoint Contract
    *
    * guarantee rx_HasEvent
    * @param api_EthernetFramesRxIn incoming event data port
    * @param api_EthernetFramesRxOut outgoing event data port
    */
  @strictpure def compute_spec_rx_HasEvent_guarantee(
      api_EthernetFramesRxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesRxOut: Option[SW.RawEthernetMessage]): B =
    api_EthernetFramesRxIn.nonEmpty ___>:
      (api_EthernetFramesRxOut.nonEmpty ___>:
        Firewall_Impl_Firewall_Firewall.should_allow_inbound_frame_rx(api_EthernetFramesRxIn.get, T) &&
          api_EthernetFramesRxIn.get == api_EthernetFramesRxOut.get) &&
        (api_EthernetFramesRxOut.isEmpty __>: Firewall_Impl_Firewall_Firewall.should_allow_inbound_frame_rx(api_EthernetFramesRxIn.get, F))

  /** Compute Entrypoint Contract
    *
    * guarantee rx_NoSend
    * @param api_EthernetFramesRxIn incoming event data port
    * @param api_EthernetFramesRxOut outgoing event data port
    */
  @strictpure def compute_spec_rx_NoSend_guarantee(
      api_EthernetFramesRxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesRxOut: Option[SW.RawEthernetMessage]): B =
    !(api_EthernetFramesRxIn.nonEmpty) __>: api_EthernetFramesRxOut.isEmpty

  /** Compute Entrypoint Contract
    *
    * guarantee tx_HasEvent
    * @param api_EthernetFramesTxIn incoming event data port
    * @param api_EthernetFramesTxOut outgoing event data port
    */
  @strictpure def compute_spec_tx_HasEvent_guarantee(
      api_EthernetFramesTxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]): B =
    api_EthernetFramesTxIn.nonEmpty ___>:
      (api_EthernetFramesTxOut.nonEmpty ___>:
        Firewall_Impl_Firewall_Firewall.should_allow_outbound_frame_tx(api_EthernetFramesTxIn.get, T) &&
          api_EthernetFramesTxIn.get == api_EthernetFramesTxOut.get) &&
        (api_EthernetFramesTxOut.isEmpty __>: Firewall_Impl_Firewall_Firewall.should_allow_outbound_frame_tx(api_EthernetFramesTxIn.get, F))

  /** Compute Entrypoint Contract
    *
    * guarantee tx_NoSend
    * @param api_EthernetFramesTxIn incoming event data port
    * @param api_EthernetFramesTxOut outgoing event data port
    */
  @strictpure def compute_spec_tx_NoSend_guarantee(
      api_EthernetFramesTxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]): B =
    !(api_EthernetFramesTxIn.nonEmpty) __>: api_EthernetFramesTxOut.isEmpty

  /** CEP-T-Guar: Top-level guarantee contracts for Firewall's compute entrypoint
    *
    * @param api_EthernetFramesRxIn incoming event data port
    * @param api_EthernetFramesTxIn incoming event data port
    * @param api_EthernetFramesRxOut outgoing event data port
    * @param api_EthernetFramesTxOut outgoing event data port
    */
  @strictpure def compute_CEP_T_Guar (
      api_EthernetFramesRxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesTxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesRxOut: Option[SW.RawEthernetMessage],
      api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]): B =
    compute_spec_rx_HasEvent_guarantee(api_EthernetFramesRxIn, api_EthernetFramesRxOut) &
    compute_spec_rx_NoSend_guarantee(api_EthernetFramesRxIn, api_EthernetFramesRxOut) &
    compute_spec_tx_HasEvent_guarantee(api_EthernetFramesTxIn, api_EthernetFramesTxOut) &
    compute_spec_tx_NoSend_guarantee(api_EthernetFramesTxIn, api_EthernetFramesTxOut)

  /** CEP-Post: Compute Entrypoint Post-Condition for Firewall
    *
    * @param api_EthernetFramesRxIn incoming event data port
    * @param api_EthernetFramesTxIn incoming event data port
    * @param api_EthernetFramesRxOut outgoing event data port
    * @param api_EthernetFramesTxOut outgoing event data port
    */
  @strictpure def compute_CEP_Post (
      api_EthernetFramesRxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesTxIn: Option[SW.RawEthernetMessage],
      api_EthernetFramesRxOut: Option[SW.RawEthernetMessage],
      api_EthernetFramesTxOut: Option[SW.RawEthernetMessage]): B =
    (// CEP-Guar: guarantee clauses of Firewall's compute entrypoint
     compute_CEP_T_Guar (api_EthernetFramesRxIn, api_EthernetFramesTxIn, api_EthernetFramesRxOut, api_EthernetFramesTxOut))

  /** CEP-Post: Compute Entrypoint Post-Condition for Firewall via containers
    *
    * @param pre Container holding the values of incoming ports and the pre-state values of state variables
    * @param post Container holding the values of outgoing ports and the post-state values of state variables
    */
  @strictpure def compute_CEP_Post_Container(
      pre: Firewall_Impl_Firewall_Firewall_PreState_Container_PS,
      post: Firewall_Impl_Firewall_Firewall_PostState_Container_PS): B =
    compute_CEP_Post(
      api_EthernetFramesRxIn = pre.api_EthernetFramesRxIn,
      api_EthernetFramesTxIn = pre.api_EthernetFramesTxIn,
      api_EthernetFramesRxOut = post.api_EthernetFramesRxOut,
      api_EthernetFramesTxOut = post.api_EthernetFramesTxOut)
}
