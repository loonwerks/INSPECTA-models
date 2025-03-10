// #Sireum #Logika

package firewall.SW

import org.sireum._
import firewall._

// For Robby
//  - it would be good if the Ensures clauses for multiple returns indicated the line number of the return being addressed
// This file will not be overwritten so is safe to edit
object Firewall_Impl_Firewall_Firewall {

  // These types abstract a byte array
  type ByteField = Z  // abstracts (simplifies) 1 or more bytes that constitute a logical field in the frame
  type ByteArray = ISZ[ByteField] // abstracts a frame

  // ============================================
  //   Property Specification
  // ============================================
  // Concept:
  //   These predicates form the auditable specification of firewall concepts

  // ---------------------------
  //   Byte-field predicates
  // --------------------------

  // --- address fields ---

  // 6-byte addresses are simulated using a single ByteField.
  // Thus, for the "empty address" property", the Rust check to check if all
  // 6 bytes are 0, becomes a check to see if the single ByteField is equal to 0
  @strictpure def is_empty_address(slice: ByteField): B = (slice == 0)

  // --- protocol code ---

  // byte-field-level predicates specifying protocol encoding
  //  (a slice of 2 bytes is currently simulated using 1 Slang ByteField (Z) value)
  //
  //  Ipv4 = 0x0800 = 2048
  //  Arp = 0x0806 = 2054
  //  Ipv6 = 0x86DD = 34525
  @strictpure def is_Ipv4_slice(slice: ByteField): B = (slice == 2048)
  @strictpure def is_Arp_slice(slice: ByteField): B = (slice == 2054)
  @strictpure def is_Ipv6_slice(slice: ByteField): B = (slice == 34525)

  @strictpure def is_UnknownProtocol_slice(slice: ByteField): B =
    !(is_Ipv4_slice(slice) | is_Arp_slice(slice) | is_Ipv6_slice(slice))


  // ---------------------------
  //   Frame predicates
  // --------------------------

  // In this mock up, we represent bytes as integers, and we collapse multiple bytes
  // representing a single logical field into an integer
  //
  //   Actual Byte array (using Rust slice notation)      Slang mock-up
  //    0..6  - destination address                        0
  //    6..12 - source address                             1
  //    12..14 - protocol code                             2

  // --- address fields ---
  @strictpure def is_empty_dest_address(frame: ByteArray): B =
    (frame.size >= 1 && is_empty_address(frame(0)))

  //  --- protocol code ---
  // frame-level predicates specifying protocol encoding, assuming protocol encoding occures
  //  at index position 2
  //  In the Rust implementation, we would write something like the following...
  // @strictpure def is_Ipv4_frame(frame: ByteArray): B = frame.size >= 14 && is_Ipv4_slice(frame(12..14))

  @strictpure def is_Ipv4_frame(frame: ByteArray): B = frame.size >= 3 && is_Ipv4_slice(frame(2))
  @strictpure def is_Arp_frame(frame: ByteArray): B = frame.size >= 3 && is_Arp_slice(frame(2))
  @strictpure def is_Ipv6_frame(frame: ByteArray): B = frame.size >= 3 && is_Ipv6_slice(frame(2))

  // Note: The notion of "malformed" as mentioned in the HLRs was a bit unclear to me.
  //  It seemed to at least include the notion of the frame protocol being unknown OR
  //  destination address being empty.  So I have used those conditions for the mockup.
  @strictpure def is_unknown_protocol_frame(frame: ByteArray): B =
    frame.size >= 3 && is_UnknownProtocol_slice(frame(2))
  @strictpure def is_malformed_frame(frame: ByteArray): B =
    (is_unknown_protocol_frame(frame) | is_empty_dest_address(frame))

  //-----------------------------------
  // HLR Predicates
  //-----------------------------------

  // Express drop / copy helper predicates that provide a more natural formalization of
  // natural language requirements.

  @strictpure def policy_COPY(b: B): B = (b == T)
  @strictpure def policy_DROP(b: B): B = (b == F)

  @strictpure def msg_to_policy(msgOpt: Option[SW.RawEthernetMessage]): B
    = (!(msgOpt.isEmpty))

  // We need to decide in what form the formalization of the HLRs would be presented to an
  // auditor.  For now I assume that we encode HLR's as predicates on frames that exit
  // the firewall
  //
  // HLR 01: The firewall shall drop any malformed frame.
  //  i.e., no malformed frame shall pass the firewall
  //
  // The semantics is not exactly the same, because the notion of "drop" might also imply
  //  (in fuller implementations) the notion of logging a dropped frame, etc.
  // However, these correspond to what should be enforced in the contract of "can_send_frame".
  // These can also show up as pre-conditions to the entrance of the VM and other consumers of
  // firewall-processed data.

  @strictpure def HLR_01(frame: ByteArray,firewall_action: B): B
    = (is_malformed_frame(frame) ->: policy_DROP(firewall_action))
  @strictpure def HLR_01_inv(frame: ByteArray): B = (!is_malformed_frame(frame))

  // HLR 02: The firewall shall drop any frame that is type ipv6.
  //   i.e., no ipv6 frame shall pass the firewall

  @strictpure def HLR_02(frame: ByteArray,firewall_action: B): B =
    (is_Ipv6_frame(frame) ->: policy_DROP(firewall_action))
  @strictpure def HLR_02_inv(frame: ByteArray): B = (!is_Ipv6_frame(frame))

  // HLR 06: If the firewall gets an ARP packet frame from RxIn,
  // the firewall shall copy that frame to RxOut.

  @strictpure def HLR_06(frame: ByteArray,firewall_action: B): B =
    (is_Arp_frame(frame) ->: policy_COPY(firewall_action))

  @strictpure def HLR_06_fix1(frame: ByteArray,firewall_action: B): B =
    // Robby - I am surprised by the way this parsed
   // (!is_empty_dest_address(frame) & is_Arp_frame(frame) ->: policy_COPY(firewall_action))
    ((!is_empty_dest_address(frame) & is_Arp_frame(frame)) ->: policy_COPY(firewall_action))

  //====================================================================
  //  F r a m e    P a r s e    T r e e s
  //====================================================================

  // In the Rust code, Rust enums are used to define structures representing
  // frame "parse trees", i.e., simple abstractions of byte-array encoded information
  // represented using easy-to-manipulate structures.
  //
  // Fire-wall rules are enforced
  // via checks on the frame parse trees.
  //
  // Therefore, a key verification task is to prove that "abstract" properties that hold
  // on frame parse trees have a "concrete" analogue that holds for the frame byte array.

  //----------------------------------------------
  // Address
  //----------------------------------------------

  // Original Rust definition
  //   pub struct Address(pub [u8; 6]);

  @datatype class Address(bytes: ByteField)

  // The following definition models the Rust function
  //  Address::from_bytes
  //
  // pub fn from_bytes(data: &[u8]) -> Address {
  //        let mut bytes = [0; 6];
  //        bytes.copy_from_slice(data);
  //        Address(bytes)
  //    }

  // Note that the contract is forming a relationship between the
  // "concrete" byte array representation of an address which was passed as an argument,
  // and the parse tree "abstract" representation of an address.
  // In this case, the relationship
  // is trivial (an equality) because of the bytes of the address are just copied
  // directly into the parse tree (this is what happens in the original Rust code).

  @strictpure def Address_REP(bytes: ByteField, address: Address) : B =
    (bytes == address.bytes)

  def Address_from_bytes(bytes: ByteField): Address = {
    Contract(
      Ensures(
        Address_REP(bytes, Res[Address])
      )
    )
    return Address(bytes)
  }

  //    pub fn is_empty(&self) -> bool {
  //        self.0.iter().filter(|x| **x != 0).count() == 0
  //    }
  // **Note**: in the Rust code, is it possible to simplify the expression above
  //   and just check each byte to see if it is zero?  That would make the code more readable
  //   (as suitable for verification)
  def Address_is_empty(address: Address): B = {
    Contract(
      Ensures(
        Res[B] == is_empty_address(address.bytes))
    )
    return (address.bytes == 0)
  }


  //----------------------------------------------
  // EtherType
  //----------------------------------------------

  // Original Rust
  // pub enum EtherType {
  //    Ipv4 = 0x0800,
  //    Arp = 0x0806,
  //    Ipv6 = 0x86DD,
  //    Unknown(u16),
  // }

  // The following Slang idiom using Slang @datatype is equivalent in principle
  // to the Rust enum above.
  @datatype trait EtherType
  @datatype class Ipv4() extends EtherType {}
  @datatype class Arp() extends EtherType {}
  @datatype class Ipv6() extends EtherType {}
  @datatype class Unknown(other: Z) extends EtherType {}

  // The presentation invariant for this structure relates the
  //  byte representation to the parse tree representation.
  @strictpure def EtherType_REP(bytes: ByteField, etherType: EtherType) : B =
    ( is_Ipv4_slice(bytes) == (etherType === Ipv4()) &
      is_Arp_slice(bytes) == (etherType === Arp()) &
      is_Ipv6_slice(bytes) == (etherType === Ipv6()) &
      is_UnknownProtocol_slice(bytes) == (etherType === Unknown(bytes))
      )


  // The following definition models the Rust function
  //  EtherType::from_bytes
  //   pub fn from_bytes(bytes: &[u8]) -> Self {
  //        let mut data = [0u8; 2];
  //        data.copy_from_slice(bytes);
  //        let raw = u16::from_be_bytes(data);
  //        EtherType::from(raw)
  //    }

  // Here again, the contract relates the "concrete" byte array representation
  // (passed as an argument) to the abstract parse tree representation using
  // the enum idiom.  Intuitively, the contract specifies "if and only if" (bi-implication)
  // relationships between the representations:
  //   the byte array satisfies the is_Ipv4_slice() predicate
  //     iff
  //   the parse tree enum value is Ipv4
  def EtherType_from_bytes(byteField: ByteField): EtherType = {
  Contract(
    Ensures(EtherType_REP(byteField,Res[EtherType])
    )
  )
    byteField match {
      case 2048 => return Ipv4();
      case 2054 => return Arp();
      case 34525 => return Ipv6();
      case _ => return Unknown(byteField);
    }
  }


//----------------------------------------------
// EthernetRep
//----------------------------------------------

  // pub struct EthernetRepr {
  //    pub src_addr: Address,
  //    pub dst_addr: Address,
  //    pub ethertype: EtherType,
  //}

  @datatype class EthernetRepr(
                                src_addr: Address,
                                dst_addr: Address,
                                etherType: EtherType) {}

  @strictpure def EthernetRepr_REP(frame: ByteArray, ethernetRepr: EthernetRepr) : B =
    (frame.size >= 3 &&  // must guard the size for other Logika predicates to be well-formed
                         // (i.e., avoid indexing errors.  Not sure how Verus handles this.
                         // Note && is short-circuit evaluation, so if the guard is false, the
                         // entire expression is false without encountering out of range issues.
      (Address_REP(frame(0),ethernetRepr.dst_addr) &
       Address_REP(frame(1),ethernetRepr.src_addr) &
       EtherType_REP(frame(2),ethernetRepr.etherType)))

  // pub fn is_wellformed(&self) -> bool {
  //        if let EtherType::Unknown(_) = self.ethertype {
  //            return false;
  //        }
  //        true
  //    }

  // The Slang version is tweaked slightly to only have one "return"
  //  (which Logika tends to do better with)

  // This function is a "check" on the parse tree representation.
  // The contract simply specifies the functionality of the check
  // in logical form.

  // This spec method is convenient, but not strictly necessary
  @strictpure def EthernetRepr_is_wellformed_spec(ethernetRepr: EthernetRepr): B = (
       ethernetRepr.etherType == Ipv4() |
       ethernetRepr.etherType == Ipv6() |
       ethernetRepr.etherType == Arp())

  @pure def EthernetRepr_is_wellformed(ethernetRepr: EthernetRepr): B = {
    Contract(
      Ensures(Res[B] == EthernetRepr_is_wellformed_spec(ethernetRepr))
    )
    val wf:B = (ethernetRepr.etherType match {
      case Unknown(_) => F
      case _ => T
    })
    return wf
  }

  // pub fn parse(frame: &[u8]) -> Option<EthernetRepr> {
  //        let dst_addr = Address::from_bytes(&frame[0..6]);
  //        let src_addr = Address::from_bytes(&frame[6..12]);
  //        let ethertype = EtherType::from_bytes(&frame[12..14]);
  //        let e = EthernetRepr {
  //            src_addr,
  //            dst_addr,
  //            ethertype,
  //        };
  //        if e.is_wellformed() {
  //            Some(e)
  //        } else {
  //            None
  //        }
  //    }

  def EthernetRepr_parse(frame: ByteArray): Option[EthernetRepr] = {
    Contract(
      Requires(frame.size >= 3),
      Ensures(
        Res[Option[EthernetRepr]].isEmpty ->: is_unknown_protocol_frame(frame),
        !Res[Option[EthernetRepr]].isEmpty -->: EthernetRepr_is_wellformed_spec(Res[Option[EthernetRepr]].get),
        !Res[Option[EthernetRepr]].isEmpty -->:  // short-circuit implies to guard against applying "get" to Option None()
          EthernetRepr_REP(frame,Res[Option[EthernetRepr]].get)
      )
    )
    val dst_addr = Address_from_bytes(frame(0)) // model frame[0..6]
    // Debugging (proof)
    Deduce( |- (Address_REP(frame(0),dst_addr)))

    val src_addr = Address_from_bytes(frame(1)) // model frame[6..12]
    // Debugging (proof)
    Deduce( |- (Address_REP(frame(1),src_addr)))

    // Verification notes:
    //   the contract of the following called method establishes a relationship between the
    //   ethertype structured representation and the byte array
    val ethertype = EtherType_from_bytes(frame(2)) // model frame[12..14]
    // Debugging (proof)
    Deduce( |- (EtherType_REP(frame(2),ethertype)))

    val e = EthernetRepr(src_addr, dst_addr, ethertype)
    // Debugging (proof)
    Deduce( |- (EthernetRepr_REP(frame, e)))

    // Verification notes:
    //   the contract of "is_wellformed" below establishes conditions on the structured
    //   representation.  Given the constraints coming from the EtherType_frome_bytes above,
    //   Logika is able to reason "transitively" (e.g., via equality contraints)
    //   to establish the corresponding well-formedness property on the byte array as required
    //   by the post-condition
    //
    //  Note that !EthernetRepr_is_wellformed does not correspond to "malformed frame"
    //  malformed frame also includes the notion of an empty destination address.
    if (EthernetRepr_is_wellformed(e)) {
      return Some(e)
    } else {
      return None()
    }
  }


  //  pub fn is_empty(&self) -> bool {
  //        self.dst_addr.is_empty()
  //    }
  def EthernetRepr_is_empty(ethernetRepr: EthernetRepr): B = {
    Contract(
      Ensures(Res[B] == is_empty_address(ethernetRepr.dst_addr.bytes))
    )
    return Address_is_empty(ethernetRepr.dst_addr)
  }

  // fn can_send_frame(frame: &mut [u8]) -> bool {
  //    let Some(packet) = EthFrame::parse(frame) else {
  //        info!("Malformed packet. Throw it away.");
  //        return false;
  //    };
  //
  //    match packet.eth_type {
  //        PacketType::Arp(_) => true,
  //        PacketType::Ipv4(ip) => match ip.protocol {
  //            Ipv4ProtoPacket::Tcp(tcp) => {
  //                let allowed = tcp_port_allowed(&tcp);
  //                if !allowed {
  //                    info!("TCP packet filtered out");
  //                }
  //                allowed
  //            }
  //            Ipv4ProtoPacket::Udp(udp) => {
  //                let allowed = udp_port_allowed(&udp);
  //                if !allowed {
  //                    info!("UDP packet filtered out");
  //                }
  //                allowed
  //            }
  //            Ipv4ProtoPacket::TxOnly => {
  //                info!(
  //                    "Not a TCP or UDP packet. ({:?}) Throw it away.",
  //                    ip.header.protocol
  //                );
  //                false
  //            }
  //        },
  //        PacketType::Ipv6 => {
  //            info!("Not an IPv4 or Arp packet. Throw it away.");
  //            false
  //        }
  //    }
  //}


  def can_send_frame(frame: ByteArray): B = {
    Contract(
      Requires(frame.size >= 3),
      Ensures(HLR_01(frame, Res[B]),
              HLR_02(frame, Res[B]),
              HLR_06_fix1(frame, Res[B]))
    )

    val ethernetReprOpt = EthernetRepr_parse(frame)
    if (ethernetReprOpt.isEmpty) {
      // Malformed packet. Throw it away
      // Deduce( |- (!is_Arp_frame(frame)))
      // Deduce( |- (!(!is_empty_dest_address(frame) & is_Arp_frame(frame))))
      // Deduce( |- (!is_empty_dest_address(frame) & is_Arp_frame(frame) ->: F))
      // Deduce( |- ((!is_empty_dest_address(frame) & is_Arp_frame(frame)) ->: F))
      // Deduce( |- (HLR_06_fix1(frame,false)))
      return false
    }

    val ethernetRepr = ethernetReprOpt.get
    if (EthernetRepr_is_empty(ethernetRepr)) {
      // Unknown dest address.  Throw it away
      return false
    }

    // Deduce( |- (EtherType_REP(frame(2),ethernetRepr.etherType)))
    // Deduce( |- (ethernetRepr.etherType == Ipv4() | ethernetRepr.etherType == Arp() | ethernetRepr.etherType == Ipv6()))
    ethernetRepr.etherType match {
      case Ipv4() => {return true}
      case Arp() => {return true}
      case Ipv6() => {
        // "Not an IPv4 or Arp packet. Throw it away."
        return false
      }
      case Unknown(_) => {
        // This case is already handled by EthernetRepr_parse,
        // but the Scala compiler doesn't understand that.
        // So we need to add this case here to avoid a compiler error.
        return false
      }
    }
  }



  //=================================================
  // I n i t i a l i s e     E n t r y     P o i n t
  //=================================================
  def initialise(api: Firewall_Impl_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesRxOut(SW.RawEthernetMessage.example())
  }


  def timeTriggered(api: Firewall_Impl_Operational_Api): Unit = {
    Contract(
      Requires(
        // this should be specified by the HAMR proof infrastructure
        api.EthernetFramesRxOut.isEmpty,
        // simplifying assumption for J. Hatcliff demonstration
        !api.EthernetFramesRxIn.isEmpty -->: (api.EthernetFramesRxIn.get.value.size >= 3)),
      Modifies(api),
      Ensures(api.EthernetFramesRxIn.isEmpty -->: api.EthernetFramesRxOut.isEmpty,
              !api.EthernetFramesRxIn.isEmpty -->:
                (HLR_01(api.EthernetFramesRxIn.get.value, msg_to_policy(api.EthernetFramesRxOut))),
              !api.EthernetFramesRxIn.isEmpty -->:
                (HLR_02(api.EthernetFramesRxIn.get.value, msg_to_policy(api.EthernetFramesRxOut))),
              !api.EthernetFramesRxIn.isEmpty -->:
                (HLR_06_fix1(api.EthernetFramesRxIn.get.value, msg_to_policy(api.EthernetFramesRxOut))))
    )
    // check in port for presence of message
    val in_messageOpt: Option[SW.RawEthernetMessage] = api.get_EthernetFramesRxIn()
    in_messageOpt match {
      // If no message, do nothing
      case None() =>
        api.logDebug("No input message; thus no output message")
      // If some message, take appropriate action (send, drop) based on rules
      case Some(in_message) => {
        val firewall_action: B = can_send_frame(in_message.value)
        if (firewall_action) {
          api.logDebug(s"PASS frame: ${in_message.value}")
          api.put_EthernetFramesRxOut(in_message)
        } else {
          api.logDebug(s"DROP frame: ${in_message.value}")
        }
      }
    }
  }

  def finalise(api: Firewall_Impl_Operational_Api): Unit = { }
}
