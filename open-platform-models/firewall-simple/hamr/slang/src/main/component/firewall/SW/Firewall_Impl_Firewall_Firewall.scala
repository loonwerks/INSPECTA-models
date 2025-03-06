// #Sireum #Logika

package firewall.SW

import org.sireum._
import firewall._

// This file will not be overwritten so is safe to edit
object Firewall_Impl_Firewall_Firewall {

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

  // byte-field-level predicates specifying protocol encoding
  //  (a slice of 2 bytes is currently simulated using 1 Slang ByteField (Z) value)
  //
  //  Ipv4 = 0x0800 = 2048
  //  Arp = 0x0806 = 2054
  //  Ipv6 = 0x86DD = 34525
  @strictpure def is_Ipv4_slice(slice: ByteField): B = (slice == 2048)
  @strictpure def is_Arp_slice(slice: ByteField): B = (slice == 2054)
  @strictpure def is_Ipv6_slice(slice: ByteField): B = (slice == 34525)

  @strictpure def is_empty_address(slice: ByteField): B = (slice == 0)

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


  // frame-level predicates specifying protocol encoding
  //  Simplifying assumption: protocol encoding occurs at index position 2
  @strictpure def is_Ipv4_frame(frame: ByteArray): B = frame.size >= 3 && is_Ipv4_slice(frame(2))
  //  In the Rust implementation, we would write something like the following...
  // @strictpure def is_Ipv4_frame(frame: ByteArray): B = frame.size >= 14 && is_Ipv4_slice(frame(12..14))
  @strictpure def is_Arp_frame(frame: ByteArray): B = frame.size >= 3 && is_Arp_slice(frame(2))
  @strictpure def is_Ipv6_frame(frame: ByteArray): B = frame.size >= 3 && is_Ipv6_slice(frame(2))

  @strictpure def is_well_formed_frame(frame: ByteArray): B =
    (is_Ipv4_frame(frame) | is_Ipv6_frame(frame) | is_Arp_frame(frame))
  @strictpure def is_malformed_frame(frame: ByteArray): B =
    (!is_well_formed_frame(frame))

  @strictpure def is_empty_dest_address(frame: ByteArray): B =
    (frame.size >= 1 & is_empty_address(frame(0)))

  //----------------------------------------------
  // Address
  //----------------------------------------------
  
  @datatype class Address(bytes: ByteField)

  // The following definition models the Rust function
  //  Address::from_bytes
  //
  // pub fn from_bytes(data: &[u8]) -> Address {
  //        let mut bytes = [0; 6];
  //        bytes.copy_from_slice(data);
  //        Address(bytes)
  //    }

  def Address_from_bytes(bytes: ByteField): Address = {
    Contract(
      Ensures(
        Res[Address].bytes == bytes
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


  // Original Rust
  // pub enum EtherType {
  //    Ipv4 = 0x0800,
  //    Arp = 0x0806,
  //    Ipv6 = 0x86DD,
  //    Unknown(u16),
  // }

  //----------------------------------------------
  // EtherType
  //----------------------------------------------

  @datatype trait EtherType
  @datatype class Ipv4() extends EtherType {}
  @datatype class Arp() extends EtherType {}
  @datatype class Ipv6() extends EtherType {}
  @datatype class Unknown(other: Z) extends EtherType {}

  // The following definition models the Rust function
  //  EtherType::from_bytes
  //   pub fn from_bytes(bytes: &[u8]) -> Self {
  //        let mut data = [0u8; 2];
  //        data.copy_from_slice(bytes);
  //        let raw = u16::from_be_bytes(data);
  //        EtherType::from(raw)
  //    }
  def EtherType_from_bytes(byteField: ByteField): EtherType = {
  Contract(
    Ensures(is_Ipv4_slice(byteField) == (Res[EtherType] == Ipv4()),
            is_Arp_slice(byteField) == (Res[EtherType] === Arp()),
            is_Ipv6_slice(byteField) == (Res[EtherType] === Ipv6())
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

  // pub fn is_wellformed(&self) -> bool {
  //        if let EtherType::Unknown(_) = self.ethertype {
  //            return false;
  //        }
  //        true
  //    }

  // The Slang version is tweaked slightly to only have one "return"
  //  (which Logika tends to do better with)

  def EthernetRepr_is_wellformed(ethernetRepr: EthernetRepr): B = {
    Contract(
      Ensures(Res[B] == (
        ethernetRepr.etherType == Ipv4() |
        ethernetRepr.etherType == Ipv6() |
        ethernetRepr.etherType == Arp())
      )
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
      Ensures(!(Res[Option[EthernetRepr]].isEmpty) ->: is_well_formed_frame(frame),
              Res[Option[EthernetRepr]].isEmpty ->: is_malformed_frame(frame)
      )
    )
    val dst_addr = Address_from_bytes(frame(0)) // model frame[0..6]
    val src_addr = Address_from_bytes(frame(1)) // model frame[6..12]
    // Verification notes:
    //   the contract of the following method establishes a relationship between the
    //   ethertype structured representation and the byte array
    val ethertype = EtherType_from_bytes(frame(2)) // model frame[12..14]
    val e = EthernetRepr(src_addr, dst_addr, ethertype)
    // Verification notes:
    //   the contract of "is_wellformed" below establishes conditions on the structured
    //   representation.  Given the constraints coming from the EtherType_frome_bytes above,
    //   Logika is able to reason "transitively" (e.g., via equality contraints)
    //   to establish the corresponding well-formedness property on the byte array as required
    //   by the post-condition
    if (EthernetRepr_is_wellformed(e)) {
      return Some(e)
    } else {
      return None()
    }
  }

  def can_send_frame(frame: ByteArray): B = {
    Contract(
      Requires(frame.size == 4),
      Ensures(Res[B] == (is_Ipv4_frame(frame) | is_Arp_frame(frame)))
    )
    val etherType: EtherType = EtherType_from_bytes(frame(0))

    var allowed: B = F;

    etherType match {
      case Ipv4() => { allowed = T; }
      case Arp() =>  { allowed = T; }
      case _ => { allowed = F; }
    }

    return allowed;
  }



  def can_send_frame_SIMPL(frame: ByteArray): B = {
    Contract(
      Requires(frame.size == 4),
      Ensures(Res[B] == (is_Ipv4_frame(frame) | is_Arp_frame(frame)))
    )
    val etherType: EtherType = EtherType_from_bytes(frame(0))

    var allowed: B = F;

    etherType match {
      case Ipv4() => { allowed = T; }
      case Arp() =>  { allowed = T; }
      case _ => { allowed = F; }
    }

    return allowed;
  }

  def can_send_frame_SIMPL_DEBUG(frame: ISZ[Z]): B = {
    Contract(
      Requires(frame.size == 4),
      Ensures(Res[B] == (is_Ipv4_frame(frame) | is_Arp_frame(frame)))
    )
    val etherType: EtherType = EtherType_from_bytes(frame(0))

    var allowed: B = F;

    etherType match {
      case Ipv4() => {
        allowed = T;
        Deduce ( |- (allowed == is_Ipv4_frame(frame)) )
      }
      case Arp() => {
        allowed = T;
        Deduce ( |- (is_Arp_frame(frame)) )
      }
      case _ => {
        allowed = F;
        Deduce ( |- (!is_Arp_frame(frame) & !is_Ipv4_frame(frame)) )
      }
    }

    return allowed;
  }

  def initialise(api: Firewall_Impl_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_EthernetFramesRxOut(SW.RawEthernetMessage.example())
  }

  def timeTriggered(api: Firewall_Impl_Operational_Api): Unit = {
    val in_message: SW.RawEthernetMessage = api.get_EthernetFramesRxIn().get
    // "frame" simulates a fixed length byte array using
    //    ISZ (unbounded sequence, instead of fixed length)
    //    Z (unbounded integer, instead of byte)
    val in_frame: ISZ[Z] = in_message.value
    // TEMP HACK: force frame size to be 4, simulating a byte array of size 4
    assert(in_frame.size == 4)
    api.logInfo(s"Input frame: ${in_frame}")



  }

  def finalise(api: Firewall_Impl_Operational_Api): Unit = { }
}
