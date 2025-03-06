This document contains notes on potential GUMBO contracts for the INSPECTA isolate-ethernet-simple model.
The suggestions are basic on natural language requirements from Robbie VanVossen (DornerWorks).

Currently, the natural language requirements are for the firewall component, which
has the following AADL component type:

```aadl
thread Firewall
 features
  EthernetFramesRxIn: in data port RawEthernetMessage.Impl;
  EthernetFramesTxIn: in data port RawEthernetMessage.Impl;
  EthernetFramesRxOut: out data port RawEthernetMessage.Impl;
  EthernetFramesTxOut: out data port RawEthernetMessage.Impl;
end Firewall;
```

Input and output data items have the following type
```aadl
data RawEthernetMessage
  properties
	Data_Model::Data_Representation => Array;
	Data_Model::Base_Type => (classifier (Base_Types::Unsigned_8));
	Data_Model::Dimension => (1600);
	HAMR::Bit_Codec_Max_Size => 1600 Bytes;
end RawEthernetMessage;

data implementation RawEthernetMessage.Impl
end RawEthernetMessage.Impl;
```

# Overall Assessment

## Properties/Predicates on Data

Many of the requirements are phrased in terms of properties of the incoming ethernet message.
These include:
- malformed frame
- frame type (=,!=) ipv6, ipv4
- frame protocol (=,!=) TCP, Arp
- port from frame is in whitelist
- is Arp request
- is Arp reply


## Constraints on Input / Output

Many of the requirements are "conditions for pass through" (e.g., from RxIn to RxOut), 
with complementary "conditions for drop".

These have the forms
- if input port data value has property P, then the message is passed through to the output
- if input port data value has property P, then the message is dropped (i.e., the output is empty/null for that dispatch)

For these to be realizable, we need have the notion of 
  - a message with value v is present on an output port
  - a message does not appear on the output port

## 1.1 Malformed frames dropped

1.1 firewall: drop malformed frame (RC_INSPECTA_00-HLR-1))) 
The firewall shall drop any malformed frame.

Questions:
* what are the input / output ports here?

## 1.2 drop ipv6 frames

1.2 firewall: drop ipv6 frames (RC_INSPECTA_00-HLR-2))) 
The firewall shall drop any frame that is type ipv6.

## 1.3 drop ipv4 non TCP

1.3 firewall: drop RxIn ipv4 non-tcp frames (RC_INSPECTA_00-HLR-3)))
The firewall shall drop any frame from RxIn that is an Ipv4 frame whose protocol is
not TCP.

## 1.4 drop inpv4 tcp frames with unexpected ports

1.4 firewall: drop RxIn ipv4 tcp frames with unexpected ports (RC_INSPECTA_00-HLR-
4)))
The firewall shall drop any frame from RxIn that is an Ipv4 frame whose protocol is
TCP and whose port is not defined in the port whitelist.

## 1.5 reply to RxIn Arp requests

1.5 firewall: reply to RxIn arp requests (RC_INSPECTA_00-HLR-5)))
If the firewall gets an Arp request frame from RxIn, the firewall shall send an Arp reply frame to TxOut.

## 1.6 copy through allowed tcp port packets

1.6 firewall: copy through allowed tcp port packets (RC_INSPECTA_00-HLR-6))) The firewall shall copy any frame from RxIn that is an Ipv4 frame with the TCP
protocol and whose port is defined in the port whitelist to RxOut.

## copy out tx arp and ipv4 frames

1.7 firewall: copy out tx arp and ipv4 frames (RC_INSPECTA_00-HLR-7)))
The firewall shall copy any frame from TxIn that is an Ipv4 or Arp frame to TxOut.

# Suggested Solution

For the issue expressing "presence" or "absence" of value on an output port, we either need to 
- have a notion of "null value" to express the concept of "no message" on a data port, or 
- change the model to use event data ports (and then the absence of a message can be represented as an empty queue).
For this, I suggest that the model be refactored to use event data ports.

For the issue of expressing the relevant properties above, we have the challeng that the properties likely cannot be directly
expressed as GUMBO / AGREE expressions because that would require
1. partially exposing the message structure so that relevant fields like (type, i.e., ipv6, ipv4), protocol (e.g., tcp) 
can have predicates defined on them.
2. defining the properties referenced in the requirements as GUMBO helper predicates

Alternative, we can develop abstract predicates in GUMBO that correspond to abstract predicates or executable side-effect free boolean functions at the source code level that walk over a message structure.

## Suggested next steps

- refactor the model to use event data ports
- define GUMBO helper predicates to represent *abstractly* the properties
- state the requirements as GUMBO contracts

The above will give a model that can be shown in a presentation for the PI meeting.

Note that if we want to expose part of the message packet as a GUMBO data type, we likely need (i.e., 
if we don't want to extend GUMBO with radically new concepts) a separate
component that sits in front of the component decodes the raw byte data structure into 
structured AADL data type.  This first component would implement the guard "is malformed frame".
If a message passes through the guard, it would be guaranteed to be well-formed (i.e., not malformed).

To investigate the broader framework for verifying that component implementation code conforms to the
contract, we need to prototype the firewall component in Slang (and perhaps also rust), and 
play around with the contract languages.  For this component we should write some manual HAMR unit tests.

As a guiding principle, I suggest that we also always keep in mind how we would automatically
generate tests/fuzzing from the model-level component specification.









