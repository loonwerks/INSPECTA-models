## Commentary on Proofcraft Domain Starter Proposal

This file contains informal notes from John Hatcliff on the Proofcraft Domain Starter proposal
(see [here](domain-starter.md) for Proofcraft's very nice write-up of the details).

The attractive features of the Proofcraft proposal are:
- the current pacer concepts (which is an extra PD, and fairly complicated collection of 
microkit notifications) are removed.  The general desired functionality of "starting 
code at the beginning of each scheduling domain" is achieved using priorities and MCS time budgets 
instead of all the additional Pacer artifacts.
- time efficiency is improved (the pacer PD is removed from the domain schedule)
- the strategy can be applied "as is" - no additions to the kernel are needed.

Potential issues are:
- There are some new constraints on the allowable time slots in the domain schedule -- if domain schedule 
is used that doesn't satisfy these constraints, the approach doesn't work correctly.
- For future work, we had considered using our existing Monitor PD concept for error recovery.  Will
the new approach enable a similar error recovery approach?  I think yes, but I am not sure yet
(also, we never wrote out the requirements for the desired error recovery -- the whole concept 
was somewhat vague).

As we think about if/how to adopt the proposal, some of the key questions are:

- can we live with the restrictions on the domain time slots in the domain schedule?  My intuition is "yes".  I think all of our current examples would live within the restriction, and it doesn't seem
too restrictive for future systems.  It is a slightly increased burden on the developer to have
to reason about satisfying the restrictions.  However, I had already suggested some future work on
a modeling IDE integrated timing analysis to reasoning about scheduling, latencies, and throughput.
To this timing tool concept, one could add a simple checking function to ensure that the new constraints
on the domain schedule are satisfied.

- can we easily adapt our existing code microkit PD code structure to the proposed "starter thread" architecture?  Again, I think the answer is "yes" -- primarily because our existing approach with the 
monitor PD for each application thread (i.e., an infrastructure entity that kicks off the execution
of the application code) is similar in structure to the "starter thread" concept (the starter
thread is also an infrastructure entity that kicks of the execution of the application code for 
a particular AADL thread component).  Moreover, following the Proofcraft proposal seems like it 
would dramatically simplify the microkit-layer infrastructure code needed for scheduling (removing the 
pacer and the tangled web of notifications necessary to make the pacer work).  This would also
improve efficiency since the pacer and its time slot could be removed from the scheduler.


I think the issues regarding the domain scheduling constraints are spelled out very clearly in the Proofcraft commentary.  So I'll just provide some additional commentary regarding the code and PD structuring.

First, recall that in our existing approach (based on the [three-domain-simple example](https://github.com/loonwerks/INSPECTA-models/tree/main/micro-examples/domain-scheduling-models/three_domain_simple)), there are three C files generated for each AADL thread component.  These files are associated with two PDs: a monitor PD and a child PD holding the application code (organized as HAMR/AADL standard entry points) and an infrastructure wrapper.

My intuition is that we would simply replace the existing MON concept with the simpler starter thread
from the Proofcraft proposal.

Here's a quick summary of the three (per AADL thread) files in our existing approach.
- a monitor (MON) PD that receives a notification from the pacer, notifies infrastructure wrapper of the application code, and then notifies the pacer back.  As an example, see the [p2_t2](https://github.com/loonwerks/INSPECTA-models/blob/main/micro-examples/domain-scheduling-models/three_domain_simple/hamr/microkit_initial/components/p2_t2/src/p2_t2_MON.c).  In the Proofcraft proposal, I believe the MON PD would be replaced by the "Starter Thread" PD (more details and tradeoffs are discussed below).
- an infrastructure wrapper - this wrapper contains port infrastructure, as well as a handler for the MON notification.  Upon notification, it calls the user/application compute entry point.  As an example, see [pt_t2.c](https://github.com/loonwerks/INSPECTA-models/blob/main/micro-examples/domain-scheduling-models/three_domain_simple/hamr/microkit_initial/components/p2_t2/src/p2_t2.c).  I believe that this code WOULD NOT CHANGE for the Proofcraft proposal.  The notification received would come from the starter thread (instead of the monitor).
- application/user code - this contains HAMR/AADL entry point methods.  As an example, see [pt_t2.c](https://github.com/loonwerks/INSPECTA-models/blob/main/micro-examples/domain-scheduling-models/three_domain_simple/hamr/microkit_initial/components/p2_t2/src/p2_t2_user.c) I believe that this code WOULD NOT CHANGE for the Proofcraft proposal.


## Monitor Code (Monitor PD) vs Starter Thread Code

Here is the existing monitor code structure from [p2_t2](https://github.com/loonwerks/INSPECTA-models/blob/main/micro-examples/domain-scheduling-models/three_domain_simple/hamr/microkit_initial/components/p2_t2/src/p2_t2_MON.c)


```c
#define PORT_PACER 59

#define PORT_TO_CHILD 58

void init(void) {
  microkit_notify(PORT_PACER);
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_PACER:
      // notify child
      microkit_notify(PORT_TO_CHILD);

      // send response back to pacer
      microkit_notify(PORT_PACER);
      break;
  }
}
```

And then here is the starter thread structure from [starter.c](domain-starter/starter.c)
```c
void notify_loop() {
    while (true) {
        microkit_notify(0);
        seL4_Yield();
    }
}

void notified(microkit_channel ch) {
    /* unreachable */
}

void init() {
    notify_loop();
}
```

I think the overall abstract computation structure is the same:
- at the start of the associated scheduling domain, a notification is sent to the application code,
- then execution yields, until the start of the next scheduling domain

In the code generation, we would simply replace the monitor code with the starter thread code.


## PD Allocation and Domain Schedule

Currently, for each AADL thread component, a "monitor" microkit PD is declared, together with a child protection domain to hold the application code (+ infrastructure wrapper).  For [example](https://github.com/loonwerks/INSPECTA-models/blob/24fb95b9c5f0df9146870ab8f41e3eb8a776ca24/micro-examples/domain-scheduling-models/three_domain_simple/hamr/microkit_initial/microkit.system#L25C3-L40C23), ...

```xml
<protection_domain name="p2_t2_MON" domain="domain_3">
    <program_image path="p2_t2_MON.elf" />
    <protection_domain name="p2_t2" domain="domain_3" id="1">
      <program_image path="p2_t2.elf" />
      <map mr="top_impl_Instance_p1_t1_write_port_1_Memory_Region"
           vaddr="0x10_000_000"
           perms="r"
           setvar_vaddr="read_port_queue_1"
      />
      <map mr="top_impl_Instance_p2_t2_write_port_1_Memory_Region"
           vaddr="0x10_001_000"
           perms="rw"
           setvar_vaddr="write_port_queue_1"
      />
    </protection_domain>
</protection_domain>
```


Illustration of current domain schedule approach from [three-domain-simple example](https://github.com/loonwerks/INSPECTA-models/blob/24fb95b9c5f0df9146870ab8f41e3eb8a776ca24/micro-examples/domain-scheduling-models/three_domain_simple/hamr/microkit_initial/microkit.system#L3C3-L11C21)


```xml
<domain_schedule>
    <domain name="domain_1" length="10" />
    <domain name="domain_2" length="100" />
    <domain name="domain_1" length="10" />
    <domain name="domain_3" length="100" />
    <domain name="domain_1" length="10" />
    <domain name="domain_4" length="100" />
    <domain name="domain_0" length="670" />
</domain_schedule>
```

In the Proofcraft proposal, for the protection domains per AADL thread component, we would have something like this (see [domains.system](domain-starter/domains.system))...

```xml
<protection_domain name="s2" domain="2" priority="254" period="5_500" budget="50">
        <program_image path="starter.elf" />
    </protection_domain>

<protection_domain name="t2" domain="2" priority="64">
    <program_image path="worker-2.elf" />
</protection_domain>

<channel>
    <end pd="s2" id="0" />
    <end pd="t2" id="0" />
</channel>
```    

This is similar to the current approach in that there are two PDs for each AADL thread component and they each belong to the same scheduling domain.  It's different in that the PDs aren't nested.  

You can observe the new priority, period, and budget fields, which are needed to make the new scheduling approach work.

I'm not sure what the differences would be with..
 - having nested PDs (which are also associated to the same scheduling domain) (as in the current approach)
 - having non-nested PDs (which are associated to the same scheduling domain) (as in the proposed approach)

In the Proofcraft proposal, we would have something like below for the domain schedule (see [domains.system](domain-starter/domains.system))...

```xml
<domain_schedule>
   <domain name="1" length="20" />
   <domain name="2" length="5" />
   <domain name="3" length="30" />
</domain_schedule>
```

Compared to the existing approach..
 - the scheduling of the pacer after each application PD is eliminated
 - it looks like the specification of the period for the major frame happens through the MCS related annotations vs. having to design a specific padding domain to achieve any slack necessary to achieve the target major frame???

