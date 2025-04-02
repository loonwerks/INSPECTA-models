# Firewall-Simple -- Prototyping Verus Verification of Open Platform Firewall Component

This document contains notes prototyping testing and Verus verification of the INSPECTA open platform firewall component.

The strategy that we are following includes:
- coding up a simplified version of the firewall in Slang and showing how Logika verification and testing applies
- incrementally working on (starting from a simplified version) of the DornerWorks firewall libraries, applying Verus and testing following the approach illustrated for Slang

We eventually want to have GUMBO contracts, with auto-generated Verus contracts and testing infrastructure for the firewall.  But HAMR is not yet ready to support that.

The two-pronged strategy above can proceed independently of HAMR/GUMBO because 
- the lower level methods of the firewall that are not on the component interface need to 
  be verified anyway
- the eventual contracts that will show up in GUMBO can be mocked up directly in Slang/Verus.  

## Verus prototyping

Current prototyping of Verus verification is being carried out in [firewall_simple-verus.rs](firewall_simple-verus.rs).  This contains excerpts of the DornerWorks core libraries.


### Next Steps

Next steps in the Verus prototyping include:
- (HLR Specification) Define additional predicates (Verus spec functions as well as corresponding executable predicates) to represent the 
  firewall HLRs following the pattern illustrated in Slang https://github.com/loonwerks/INSPECTA-models/blob/05602d3afbabe4c9eb91b156cb3d4eecf78bbaf8/open-platform-models/firewall-simple/hamr/slang/src/main/component/firewall/SW/Firewall_Impl_Firewall_Firewall.scala#L84-L131
  
- (Verus Reasoning for Bitwise Ops) The firewall libraries perform bitwise operations (e.g., in the EtherType implementation) to convert between byte array entries and more abstract values stored in the parsed firewall message structure (e.g., u16 values).   We are currently stuck on the verification of the EtherType::from_bytes method which works on a byte array (https://github.com/loonwerks/INSPECTA-models/blob/05602d3afbabe4c9eb91b156cb3d4eecf78bbaf8/open-platform-models/firewall-simple/firewall-simple-verus-prototyping/firewall_simple-verus.rs#L261-L275) because it does some bit operations to convert a two-byte slice of a byte array to a u16.  Then it calls the helper method EtherType::from(raw) to produce an EtherType value from the u16.  So we need to prove the correctness of the helper method, and then prove a lemma stating a relationship between u16 and [u8] (2 bytes length), and then use that lemma and the proof of EtherType::from(raw) to establish the correctness of EtherType::from_bytes.  Verus documentation for bit vector operations is here: https://verus-lang.github.io/verus/guide/bitvec.html   I recommend playing around with some tiny micro-examples to illustrate how these special Verus annotations work.  Then apply the learned concepts into the firewall example.

- (Build and prove simplified version of can_send_frame).  If you look in the "core" crate of the 
  firewall code, https://github.com/loonwerks/INSPECTA-models/blob/main/isolette/rust-prototyping/firewall-grok-documentation/core/src/net.rs , you will see a succession of methods that need to be specified with contracts (including "_REP" specs as illustrated in Slang) proved correct, including
  - EthernetRepr::parse 
  - EthernetRepr::is_empty
  - EthernetRepr::is_wellformed
  There are many others that need to be proved correct, but I recommend that after the EthernetRepr methods, we mock up a simple can_send_frame method in Rust with some contracts representing the HLRs (following my example in Slang - https://github.com/loonwerks/INSPECTA-models/blob/05602d3afbabe4c9eb91b156cb3d4eecf78bbaf8/open-platform-models/firewall-simple/hamr/slang/src/main/component/firewall/SW/Firewall_Impl_Firewall_Firewall.scala#L424-L465).   Verifying this simplified can_send_frame function will illustrate the application of all the most important concepts that we will need to verify the firewall library, i.e., we will show that application of message parsing methods can be used to achieve some selected HLRs of the firewall component.

- Following the above demonstration on a simplified can_send_frame, we can continue applying the same approach to the rest of the firewall library.  Note that not all of the firewall methods are necessary for the Dornerworks can_send_frame method (https://github.com/loonwerks/INSPECTA-models/blob/05602d3afbabe4c9eb91b156cb3d4eecf78bbaf8/isolette/rust-prototyping/firewall-grok-documentation/rx/src/lib.rs#L171-L207)





