
use vstd::prelude::*;

// To run Verus, go to ~/Dev/verus folder in terminal and type the following
//   ./verus ~/Dev/verus-examples/firewall_simp.rs

verus! {

//================================================================
//  S p e c i f i c a t i o n s
//
//  The following Verus spec functions and Rust executable specifications
//  would be automatically generated from GUMBO function definitions.
//================================================================    

// ---------------------------
//   Byte-field predicates
// --------------------------

// --- address fields ---

pub open spec fn is_empty_address(bytes: &[u8]) -> bool {
    bytes[0] == 0x00 && bytes[1] == 0x00
 && bytes[2] == 0x00 && bytes[3] == 0x00
 && bytes[4] == 0x00 && bytes[5] == 0x00
}

// --- protocol fields ---
//
//  @strictpure def is_Ipv4_slice(slice: ByteField): B = (slice == 2048)
//  @strictpure def is_Arp_slice(slice: ByteField): B = (slice == 2054)
//  @strictpure def is_Ipv6_slice(slice: ByteField): B = (slice == 34525)
//
//  @strictpure def is_UnknownProtocol_slice(slice: ByteField): B =
//    !(is_Ipv4_slice(slice) | is_Arp_slice(slice) | is_Ipv6_slice(slice)) 

// These functions would be defined, or at least their names would be visable at 
// the GUMBO level.
pub open spec fn is_Ipv4_slice(bytes: &[u8]) -> bool {
    // Verus doesn't seem to require that length is guarded within the spec function.
    // Perhaps because all spec functions are unfolded.
    // In GUMBO, I think we require that they are guarded by the length
    //  e.g., bytes.len() >= 2 && bytes[0] == 0x08 && bytes[1] == 0x00
    bytes.len() >= 2 && bytes[0] == 0x08 && bytes[1] == 0x00
}

pub open spec fn is_Arp_slice(bytes: &[u8]) -> bool {
    bytes.len() >= 2 && bytes[0] == 0x08 && bytes[1] == 0x06
}

pub open spec fn is_Ipv6_slice(bytes: &[u8]) -> bool {
    bytes.len() >= 2 && bytes[0] == 0x86 && bytes[1] == 0xDD
}

pub open spec fn is_UnknownProtocol_slice(bytes: &[u8]) -> bool {
  // do I need to guard this??  
  !(is_Ipv4_slice(bytes) || is_Arp_slice(bytes) || is_Ipv6_slice(bytes)) 
}

// ---------------------------
//   Frame predicates
// --------------------------

spec fn is_empty_dest_address(bytes: &[u8]) -> bool {
  bytes.len() >= 6 && is_empty_address(bytes)
}
  
// protocol tags is at bytes 12..13
spec fn is_Ipv4_frame(bytes: &[u8]) -> bool {
    // Verus is giving a type error (but it seems like type-correct Rust code)
    //   bytes.len() >= 14 && is_Ipv4_slice(&bytes[12..14])
    // Here is a re-write suggested by Grok
    bytes.len() >= 14 && is_Ipv4_slice(&[bytes[12], bytes[13]])
  }

spec fn is_Arp_frame(bytes: &[u8]) -> bool {
    bytes.len() >= 14 && is_Arp_slice(&[bytes[12], bytes[13]])
}

spec fn is_Ipv6_frame(bytes: &[u8]) -> bool {
    bytes.len() >= 14 && is_Ipv6_slice(&[bytes[12], bytes[13]])
}
    
// -------------------------------
//   GUMBOX/Rust Executable Predicates
//
//   The following definitions illustrate Rust executable specifications
//   generated from GUMBO specifications for use in testing.
//   Unlike Slang which allows spec functions to also be executable,
//   Verus specification functions are not executable.   So we need
//   to generate a Rust function, and then use Verus to prove the equivalence
//   to the companion Verus spec function.
//  
// -------------------------------

// GUMBOX/Rust function for is_Ipv4_slice

fn is_Ipv4_slice_exe(bytes: &[u8]) -> (result: bool) 
  // a contract is included in the generation that specifies that the 
  // executable function is equivalent to the companion spec function.
  ensures result == is_Ipv4_slice(bytes)
{
    bytes.len() >= 2 && bytes[0] == 0x08 && bytes[1] == 0x00
}

// GUMBOX/Rust function for is_Ipv4_exe

fn is_Ipv4_frame_exe(bytes: &[u8]) -> (result: bool) 
  ensures result == is_Ipv4_frame(bytes)
{
    (bytes.len() >= 14 && bytes[12] == 0x08 && bytes[13] == 0x00)
}

//================================================================
//  F i r e w a l l    U s e r - D e v e l o p e d    L i b r a r y
//
//  The code is excerpted from the original Dornerworks code 
//  but modified in some place to be processable by Verus.
//
//  Illustrations are given for how the library code can be verified
//  following the strategy originally carried out in the Slang 
//  mockup the library.  
//================================================================    


#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Address(pub [u8; 6]);

// Add the following representation specification to support verification
//  (patterned after the Slang approach).

pub open spec fn Address_REP(bytes: &[u8], address: Address) -> bool {
      bytes.len() >= 6
      && bytes[0] == address.0[0]
      && bytes[1] == address.0[1]
      && bytes[2] == address.0[2]
      && bytes[3] == address.0[3]      
      && bytes[4] == address.0[4]
      && bytes[5] == address.0[5] 
      // Note: is possible to state the above in a more compact way?
      //  Verus complains about the syntax below.  I didn't take time to figure it out.
      // bytes[0..6] == address.0[0..6]     
 }

impl Address {
    /// Creates an `Address` from a byte slice.
    ///
    /// # Arguments
    /// * `data` - A slice of at least 6 bytes representing a MAC address.
    ///
    /// # Panics
    /// Panics if `data` is shorter than 6 bytes.
    ///
    /// # Returns
    /// A new `Address` instance containing the first 6 bytes of `data`.

//   Original Dornerworks Rust    
//     -- needed to modify because Verus does not accept the "copy_from_slice" method.
//        We can define our own "copy_from_slice" method with an associated contract
//        (probably the preferrable solution), or use explicit assignment statements
//        to achieve the copy.
//
//    pub fn from_bytes(data: &[u8]) -> Address {
//        let mut bytes = [0; 6];
//        bytes.copy_from_slice(data);
//        Address(bytes)
//    }

    pub fn from_bytes(data: &[u8]) -> (address: Address)  // modify original code to get named return value
       requires data.len() >= 6,
       ensures Address_REP(data,address),
    {
       // Ensure the input slice has at least 6 bytes
       // if data.len() < 6 {
       //   // Verus limitation - panic not supported
       //   panic!("data slice must be at least 6 bytes long");
       // }
    
       // Manually copy the first 6 bytes into a new array
       let bytes = [
         data[0],
         data[1],
         data[2],
         data[3],
         data[4],
         data[5],
        ];
      Address(bytes)
    }

    /// Checks if the MAC address is all zeros.
    ///
    /// # Returns
    /// `true` if all bytes in the address are zero, `false` otherwise.
    // pub fn is_empty(&self) -> bool {
    //   self.0.iter().filter(|x| **x != 0).count() == 0
    // }
    // Verus limitation - complex iterators not supported.  Use a simplified version
    pub fn is_empty(&self) -> (res: bool)
      ensures res == is_empty_address(&self.0),
    {
        // Unroll the loop below to make it easier to verify
        // for i in 0..6 {
        //   if self.0[i] != 0 {
        //      return false;
        //   }
        // }
        // true
           self.0[0] == 0x00
        && self.0[1] == 0x00
        && self.0[2] == 0x00
        && self.0[3] == 0x00
        && self.0[4] == 0x00
        && self.0[5] == 0x00
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone)]
#[repr(u16)]
pub enum EtherType {
    Ipv4 = 0x0800,
    Arp = 0x0806,
    Ipv6 = 0x86DD,
    Unknown(u16),
}

// Representation specification for EtherType (for bytes)

pub open spec fn EtherType_REP(bytes: &[u8], etherType: EtherType) -> (res: bool) {
    bytes.len() >= 2
    // perhaps the following should be specified using a match on EtherType
 && is_Ipv4_slice(bytes) == (etherType == EtherType::Ipv4)
 && is_Arp_slice(bytes) == (etherType == EtherType::Arp)
 && is_Ipv6_slice(bytes) == (etherType == EtherType::Ipv6)
 && is_UnknownProtocol_slice(bytes) == {
   let raw = ((bytes[0] as u16) << 8) | (bytes[1] as u16);
   etherType == EtherType::Unknown(raw)
 }
}

impl EtherType {
    /// Parses an `EtherType` from a byte slice.
    ///
    /// # Arguments
    /// * `bytes` - A slice of at least 2 bytes representing the EtherType in big-endian order.
    ///
    /// # Panics
    /// Panics if `bytes` is shorter than 2 bytes.
    ///
    /// # Returns
    /// An `EtherType` variant corresponding to the parsed value, or `Unknown` if unrecognized.
    // pub fn from_bytes(bytes: &[u8]) -> Self {
    //    let mut data = [0u8; 2];
    //    // data.copy_from_slice(bytes);
    //    data = [bytes[0], bytes[1]];
    //    let raw = u16::from_be_bytes(data);
    //    EtherType::from(raw)
    //}
    // Verus limitation - refactor to avoid the from_be_bytes
    pub fn from_bytes(bytes: &[u8]) -> (res: Self)
      requires bytes.len() >= 2, 
    //  Omitting this because I don't have a contract yet for EtherType::from() because
    //  I can't figure out how to get Verus to do bitwise operations yet.
    //  ensures EtherType_REP(bytes,res)
    {
        // Remove defensive programming, since appropriate contract is supplied
        // if bytes.len() < 2 {
        // panic!("bytes slice must be at least 2 bytes long");
        // }
        let raw = ((bytes[0] as u16) << 8) | (bytes[1] as u16); 
        EtherType::from(raw)
    }
}

// The byte array parsing for EtherType is coded using a helper function
// that is defined in terms of u16.   So we need an associated representation
// specification in terms of u16.
//
// These specs could be defined in a number of ways, 
//  - as a relation between u16 and EtherType values as I have done below
//    using hard-code u16 values representing bit patterns for EtherTypes
//  - defined in terms of the lower level byte functions.
// 
// It's likely that clients to "From<u16> for EtherType" want a contract
// expressed in terms of u16, so I have defined the bodies of the specs
// using u16 values.
//
// But ultimately we
// need to show correspondence to the definitions which we propose to be 
// canonical (byte array based specs).
//
// So I subsequently set up lemmas establishing the correspondence between
// [u8, 2] and u16 predicates.
//
// These specs probably don't need to appear at the GUMBO level, since they
// don't pertain to data appearing on the component interfaces (byte arrays)

pub open spec fn is_Ipv4_u16(value: u16) -> bool {
    value == 0x0800
}

pub open spec fn is_Arp_u16(value: u16) -> bool {
    value == 0x0806
}

pub open spec fn is_Ipv6_u16(value: u16) -> bool {
    value == 0x86DD
}

pub open spec fn is_UnknownProtocol_u16(value: u16) -> bool {
  !(is_Ipv4_u16(value) || is_Arp_u16(value) || is_Ipv6_u16(value)) 
}

pub open spec fn EtherType_u16_REP(value: u16, etherType: EtherType) -> bool {
 // perhaps the following should be specified using a match on EtherType
    is_Ipv4_u16(value) == (etherType == EtherType::Ipv4)
 && is_Arp_u16(value) == (etherType == EtherType::Arp)
 && is_Ipv6_u16(value) == (etherType == EtherType::Ipv6)
 && is_UnknownProtocol_u16(value) == (etherType == EtherType::Unknown(value))
}

// **** Playground stuff - exploring how Verus handles bitwise operations
// 
// pub proof fn prove_shift_and_mask() {
//     let x: u16 = 0x1234;
//     assert((x >> 8) == 0x12);
//     assert((x & 0xFF) == 0x34);
// }

// pub proof fn lemma_prototype_bytes_u16(bytes: &[u8], value: u16)
//         requires 
//            bytes.len() >= 2,
//            value <= 0xFFFF,
//            // is_Ipv4_slice(bytes),
//            is_Ipv4_u16(value),
//            // value == ((bytes[0] as u16) << 8) | (bytes[1] as u16),
//            // bytes[0] == (value >> 8) as u8,
//            // bytes[1] == (value & 0xFF) as u8,
// //        ensures
// //           is_Ipv4_u16(value) == is_Ipv4_slice(bytes), 
//     {
//         assert(bytes.len() >= 2);
//         assert(value == 0x0800);
//         assert((value & 0xFF) == 0x00);
//         assert((value >> 8) == 0x08);
//         // assert(value == ((bytes[0] as u16) << 8) | (bytes[1] as u16));
//         // assert(bytes[0] == 0x08);
//         // assert(is_Ipv4_u16(value))
//     }


impl From<u16> for EtherType {
    /// Converts a 16-bit value into an `EtherType`.
    ///
    /// # Arguments
    /// * `value` - The 16-bit EtherType value.
    ///
    /// # Returns
    /// An `EtherType` variant matching the value, or `Unknown` if not predefined.
    fn from(value: u16) -> (res: Self) 
      ensures EtherType_u16_REP(value,res),
    {
        match value {
            0x0800 => EtherType::Ipv4,
            0x0806 => EtherType::Arp,
            0x86DD => EtherType::Ipv6,
            other => EtherType::Unknown(other),
        }
    }
}

impl From<EtherType> for u16 {
    /// Converts an `EtherType` into its 16-bit representation.
    ///
    /// # Arguments
    /// * `value` - The `EtherType` to convert.
    ///
    /// # Returns
    /// The 16-bit value of the `EtherType`.
    fn from(value: EtherType) -> Self {
        match value {
            EtherType::Ipv4 => 0x0800,
            EtherType::Arp => 0x0806,
            EtherType::Ipv6 => 0x86DD,
            EtherType::Unknown(other) => other,
        }
    }
}

/// Entry point of the program, demonstrating `Address::from_bytes`.
///
/// Creates an `Address` from a sample byte slice and prints it.
fn main() {
    // Example byte slice representing a MAC address (e.g., 00:1A:2B:3C:4D:5E)
    let mac_bytes = [0x00, 0x1A, 0x2B, 0x3C, 0x4D, 0x5E];
    let ipv4_tag = [0x08,0x00];
    let misc_tag = [0x08,0xAA];
    let address = Address::from_bytes(&mac_bytes);

    assert(!is_Ipv4_slice(&mac_bytes));
    assert(is_Ipv4_slice(&ipv4_tag));
    assert(is_UnknownProtocol_slice(&misc_tag));

    let ipv4_frame = [
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00,
        ];
    let ipv6_frame = [
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];

    assert(is_Ipv4_frame(&ipv4_frame));
    assert(!is_Ipv4_frame(&ipv6_frame));
    assert(is_Ipv6_frame(&ipv6_frame));


    // Verus limitation - Verus cannot handle println
    // Print the resulting Address to demonstrate the method
    // println!("Created MAC Address: {:?}", address);
    // println!("Is the address empty? {}", address.is_empty());
}

} // verus!


