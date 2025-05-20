# GUMBO System Verification Prototyping - Research Plan

## Folder for System Verification Artifacts

**Concept:** We will likely need a dedicated location for system verification artifacts.  Perhaps these
would overlap with system testing -- I'm not sure.  We could put a `system-verification` or a `system` folder
in the HAMR project in src/main or src/test

## System Verification File/Script

**Concept:** We will likely want to have a separate script file associated with a property or "property family" 
(a notion we would need to invent in GUMBO system property language).  These files would need to include
the artifacts below, many of which should be updated/regenerated as model information changes.

## Inport Data Type Defintitions

**Concept:** All data types reference in the model in GUMBO contracts will need to be imported.

## Develop HAMR code generation for component contracts

**Concept:** Compositional views each thread component as a "black box", relying on the GUMBO contract to 
summarize the behavior of the component.   Generate a contract-only representation of each thread.  We will
need representations for both init contracts and compute contracts.  For now, only consider periodic components with data ports
to avoid the complexities of the event handler contracts in the sporadic components.  Both the init and compute
contracts will need to reference notions of state associated with ports and GUMBO-declared thread local variables
(see separate sections on those below).

## Port Representation

**Concept:** The state of each port for each component instance 
needs to be reflected in the verification scripts.  I imagine that this
will eventually need to include both the infrastructure port state and the application port state concepts
from the HAMR formal semantics.   If we start with periodic components with data ports only, each port can be 
represented as a Slang global variable with the appropriate type.   Perhaps we want to group
these notations of state into separate name spaces for each component instance. 

## Thread Local State Representation

**Concept:** The state of each port needs to be reflected in the verification scripts.  I imagine that this
will eventually need to include both the infrastructure port state and the application port state concepts
from the HAMR formal semantics.   If we start with periodic components with data ports only, each port can be
represented as a Slang global variable with the appropriate type.   As above, we may want to group the thread
local state with the port state in a name space for the particular component instance.  This would correspond
to the HAMR formal semantics in which a thread state has fields for the local thread state as well as the port
states for the component instance.

## Environment (non-connected (sub)system boundary ports)

**Concept:** We want to be able to support verification at the subsystem level, and thus we will have ports 
on the subsystem boundaries.  We need to be able to state assumptions and make assertions about values flowing 
through the ports.  Coming up with an appropriate notion of "boundary" or "virtual" port state is complicated
by the fact that these ports don't have a specific physical manifestation in the AADL semantics because they
are tunneled through when doing a model instantiation.  Thus they are virtual in some sense.  We also encounter
the same conceptual challeng in the system testing.  This issue is one that we will need to more fully explore
in the HAMR formal semantics.  

**Steps**:
- For now, represent each boundary port as a global variable, perhaps in a "context" name space.
- It may be simpler to represent the input and output state of each global GUMBO declared thread variable as
distinct global variables (I believe that is what is done in the prototyping).

## Communication Representation

**Concept**: The actions of the AADL communication substrate must be reflected in the system verification script.
This will ultimately be quite complicated (especially with event-like ports where the full queueing semantics,
including possible queue overflows must be addressed.  In the complete version of the system verification, we will likely want
the realization to be specialized to different simpler versions based on proofs about the system architecture itself.
For example, if there is no fan-in for an input port, and if the single sender will only be sending one message between
each sender/receive activation, the full semantics of the queue can be simplified to a single cell, assuming that we also 
have no malicious message insertion into the substrate as well as no message loss (which ultimately, should also be 
taken as axioms).

The representation of communication must also be tied to the scheduling for communication implemented in the HAMR run-time.
We currently assume that communication obeys AADL's "Immediate" communication property.  Thus after each component is 
"executed" in the verification script, it communication steps would be executed.

**Steps**:
- For now, treat only data ports (which have no fan in).   Represent the propagation of information from a sender
to a receiver via a Slang function that copies (via assignment) values from the global variable representing the 
output port the global variable representing the input port.  Note that the propagation to/from context variables
must also be handled.

## Execution Trace

**Concept:** We will ultimately want to represent arbitrary sequences of system steps in a script of 
actions realizing "component execution", "communication step", (maybe "infrastructure step").

**Steps**:
- For now, generate a script corresponding to single hyperperiod (or a projection of thread actions from the hyperperiod,
if only a subsystem is considered).

## Properties

*To be done*.










