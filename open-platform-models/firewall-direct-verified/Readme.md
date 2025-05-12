# Firewall Direct Verified 

## Acknowledgement: 

The artifacts in the Firewall Direct Verified folder
were developed by Jacob Bengel as part of the CS 855 High-Assurance Systems
course at Kansas State University.

## Overview / Purpose

This codebase is inspired by the original Dornerworks firewall code
base and high-level requirements (HLRs).   

It provides the following contributions to the effort of developing a
verified firewall component for the INSPECTA Open Platform system.
- It provides a candidate formalization in Verus spec functions of the HLR
  (natural language requirements).   It's likely the case that we
  would use these as the basis of providing the model-level GUMBO
  specifications for the firewall component.
- It provides executable counterparts for each of the Verus spec
  functions that can be used in testing.  

It is provides a *simplified* version of the firewall implementation,
and thus does not completely solve the task of "verifying the Dornerworks
firewall component".   However, it is likely that the *specifications*
above can be used directly for the top-level contracts for
verification of the Dorneworks firewall code.

The version in this repo is "simplified" (compared to the Dornerworks
code) as follows.  Overall, the firewall component (for both the
simplified version and the Dornerworks original) decides whether or
not an ethernet message represented as a byte array should be passed
through the firewall (copied) or dropped. 
The Dornerworks code is based on a general purpose
ethernet message library.  That library can parse a byte-array into a
Rust struct/enum-based data structure that is conceptually easier 
to work with.  It provides functions that
check the values of the *data structure* representation of the
message.  Firewall rules (realizing the HLRs) are implemented
using these data structure checking functions.   If the data structure
satisfies the policy checks, then the original byte array message is
passed through the firewall.   Verification of the Dornerworks code 
is complicated by the fact that the checks on the message are
implemented in terms of the data structure version of the message, 
but the component interface in stated in terms of byte arrays.
Thus, to obtain an interface specification in terms of byte arrays, 
the verification effort needs to define auxilary specifications
that establish a correspondence between the byte array representation
of a message and the data structure representation of the message.

To avoid the need to develop the "correspondence specifications"
between the data structure and byte array representations, the
"simplified" firewall implements checks directly in terms of the byte
array representation.   This intermediate step in the research plan (we eventually
want to verify the original Dorneworks code) 
enables us to develop and vet the Verus contracts and companion 
executable specs for the Dornerworks HLRs, while considering a more
simple firewall implementation.

## Usage

The commands below are given from the firewall-direct-verified
folder...

### Compiling

```cargo build```

### Running Tests

```cargo test```

### Verus Verification

```verus src/main.rs```


