# INSPECTA Micro-examples

*NOTE: The readme's are out-of-date and need to be updated for INSPECTA*

This repo supports prototyping of the HAMR to Microkit integration as part of the Collins Aerospace DARPA PROVERS project.  The HAMR Microkit integration is a 
migration of the seL4 HAMR backend from a CAmkES-based integration.  Although 
we expect to continue to support CAmkES, HAMR tool chain development plans 
to emphasize Microkit for PROVERS.

To support the CAmkES to Microkit migration, this repo provides a collection 
of "micro-examples" that illustrate patterns of AADL threading and communication.
Supporting these patterns will provide sufficient knowledge for supporting 
larger PROVERS examples.

The repo aims to provide a side-by-side comparison of HAMR-oriented CAmkES artifacts
and Microkit artifacts.  The CAmkES artifacts are based on a DARPA CASE deliverable
-- the [CASE tool-assessment-4](https://github.com/loonwerks/CASE/tree/master/TA5/tool-assessment-4) repository that only contains the micro-examples illustrating how the various AADL port kinds are handled and a single VM example. 

We aim for all the examples in this repo to be buildable and executable on QEMU.

## Producer-Consumer Illustrating AADL Port Kinds

- [Data Ports](basic/test_data_port_periodic_domains/) (CAmkES, preliminary Microkit)
- [Event Ports](basic/test_event_data_port_periodic_domains/) (CAmkES)
- [Event Data Ports](basic/test_event_data_port_periodic_domains/) (CAmkES)

## Virtual Machine

- [Native Producer, VM Consumer](vm/test_event_data_port_periodic_domains_VM//receiver_vm/) (CAmkES)

## Discussion

High-level requirements for Microkit to support the ARINC 653-like threading and communication emphasized by AADL are currently held separately by UNSW.   Below
we provide a summary of concepts based on what was implemented for CAmkES on CASE, 
along with a discussion of the current status of the Microkit prototype for the Data Ports example above.

### Communication

#### Basic Port Types

Inspired by ARINC 653, AADL contains both data ports (roughly corresponding to "sampling ports" of 653) and event data ports (roughly corresponding to the "queueing" 
ports of 653).  Event data ports implement queued messages with payloads.  AADL has 
a degenerate form of event data ports called event ports that support queued messages
with no payloads.   AADL provides several configuration parameters (model properties)
that determine the specific queuing policy implementation regarding overflow handling, 
dequeuing policies, etc.

For CASE, all the three AADL port types (data, event, event data) were based on 
a common CAmkES port pattern consisting of a 
* memory region holding the exchanged data
* notification indicate the presence of new data

The easiest of the ports to work with from an seL4 perspective are the data ports,
and we suggest to start the prototyping with these.
This allows us to focus on prototyping port channels as memory regions without worrying
about all the variations of AADL queueing policies for the moment.

*If we can get a prototype working using Microkit notification and memory regions, we can easily migrate from CAmkES to microkit, and then continuing refining to get a more elegant implementation from there.*

#### Prototyping for Data Ports

The results of the initial prototyping effort for the data ports microexample can be seen here
https://github.com/santoslab/case-tool-assessment-4/tree/main/basic/test_data_port_periodic_domains

For the CAmkES artifacts, please consult the "seL4_only" artifacts as these are the artifacts without the additional AADL shim code that implements the queuing semantics.

Note that in the [CAmkES for the producer](basic/test_data_port_periodic_domains/hamr/camkes-seL4_Only/components/producer_thread_i_producer_producer/producer_thread_i_producer_producer.camkes) one sees the declaration for
the int8 shared memory region (e.g., associated with an AADL data port of type int8)

```
dataport sp_int8_t_t sb_write_port;
```
and a corresponding port is found in the consumer.  The other "TickTock" ports 
are associated with the CASE scheduling approach using the domain scheduler
(we expect this strategy to be altered when moving to Microkit).

The [CAmkES assembly file](basic/test_data_port_periodic_domains/hamr/camkes-seL4_Only/top_impl_Instance.camkes) shows read/write configurations set to ensure a kernel enforced one-way 
communication from the producer to the consumer
```
configuration {
    producer_producer._domain = 2;
    consumer_consumer._domain = 3;
    producer_producer.sb_write_port_access = "W";
    consumer_consumer.sb_read_port_access = "R";
  }
```

Porting this concept over to microkit seems straightforward.  
See the [Microkit system file](basic/test_data_port_periodic_domains/microkit/dataport.system) that includes
a protection domain (PD) for both the Producer and Consumer with a memory region
shared between them (excerpts shown below).
```
<!-- 4KiB is smallest pages size for AArch64 -->
  <memory_region name="producer_to_consumer_dataport" size="4096" />

  <protection_domain name="producer" 
    priority="100"
    period="1000"
    budget="400"
  >
  
  <program_image path="producer.elf" />

  <map mr="producer_to_consumer_dataport"
    vaddr="0x4000000" perms="wr" setvar_vaddr="to_consumer_vaddr" />
  *** NOTE: I'm not sure why we set the perms above to "wr".  I expected
  *** just "w".  I will check with Jason on this.

  </protection_domain>
    
  <protection_domain name="consumer" 
    priority="99"
    period="1000"
    budget="400"
  >

  <program_image path="consumer.elf" />

  <map mr="producer_to_consumer_dataport"
    vaddr="0x4000000" perms="r" setvar_vaddr="from_producer_vaddr" />

  </protection_domain>

```

#### Prototyping for Static Scheduling

The HAMR approach to seL4 scheduling on CASE utilized the domain scheduler.  A
discussion of this can be found in this paper http://pub.santoslab.org/papers/hamr-seL4-belt-al-jsa-submission.pdf

Some key points: 
* AADL threads correspond more directly to real-time tasks than, e.g., Java threads
* When a thread is dispatched, the generated AADL infrastructure moves data from
"appropriate" (defined by a bunch of rules from the AADL standard) 
communication infrastructure ports into the view of the application code and then does not change during
the execution of the task application code for the current dispatch.
This is referred to in the AADL standard as "port freezing" and derives from 653 concepts.  
* As the thread executes it places data / messages its output ports.  When the task execution is complete,
the infrastructure releases the output port content to the communication infrastructure.
* Each thread has two entry points for the application code: (1) an Initialize Entry Point to provide initial
values for application variables, to provide initial values for output data ports (required), and to (optionally)
provide messages on output event ports, and (2) a Compute Entry Point corresponding to cyclic application behavior
(the entry point represents one cycle of functionality).

Overall, the pattern is 
```
  Initialize Entry Point
  SendOutputs // release output port values to communication infrastructure

  While (true)
    // wait for scheduling here...
    // ... when scheduled...
    ReceiveInputs // receive/freeze inputs from communication infrastructure
    ..do one cycle of application behavior..
    SendOutputs // release output port values to communication infrastructure
```

This achieves a receiveInput/compute/sendOutput pattern for the tasks.  
In a simplified view, each task runs to completion and finishes within its declared WCET (which seems similar to Microkit budget).

In AADL, there are two basic task types supported by HAMR: periodic (time-triggered) and sporadic (event port triggered).

We suggest to get the scheduling working for periodic components first.

On CASE, the basic idea is that the domain scheduler was used to enforce temporal partitioning.
See the [domain schedule](https://github.com/santoslab/case-tool-assessment-4/blob/main/basic/test_data_port_periodic_domains/hamr/camkes-seL4_Only/kernel/domain_schedule.c) used with the example above.   There was a 1-to-1 correspondence between an AADL thread component and a domain (and a CAmkES component).   An AADL process (roughly corresponding to a protected address space) was identified with a CAmkES component (seL4 "partition").  Thus, we had a model restriction that each AADL process could contain
a single thread.

This generally corresponds with the Microkit notion that each PD is single threaded, and for now we suggest identify the concept of an
AADL thread with a Microkit PD (so far, so good).

On CASE, a concept called  "pacing" used CAmkES notifications to coordinate the initiation of a thread's task dispatch with the beginning of 
a slot in the domain schedule.   In the CASE "self-pacing" approach, each thread consumed a "TickTock" notification to trigger
the task dispatch and emitted a "TickTock" notification at the end of a task execution to indicate that it was available for
dispatch when the slot occurred again in the domain schedule.   For example, see the TickTock notification declarations
in the Producer Task [CAmkES for the producer](basic/test_data_port_periodic_domains/hamr/camkes-seL4_Only/components/producer_thread_i_producer_producer/producer_thread_i_producer_producer.camkes)

An example of the C code providing skeleton of the abstract structure of the task pattern above can be seen in the 
[Producer Infrastructure Code run method](https://github.com/santoslab/case-tool-assessment-4/blob/47ba47ba8e35c4090453b361c1aedd9fa0a31a5e/basic/test_data_port_periodic_domains/hamr/camkes-seL4_Only/components/producer_thread_i_producer_producer/src/sb_producer_thread_i.c#L38).
This code is never seen by the application code.  The application code provides its own entry points which are 
invoke (indirectly) by the CAmkES-facing infrastructure code below.
This infrastructure code implemented the CAmkES entry points and used CAmkES level APIs to realize the AADL pattern above.
```
int run(void) { // CAmkES entry point
  {
    int64_t sb_dummy;
    sb_entrypoint_producer_thread_i_producer_producer_initializer(&sb_dummy); // trigger the AADL application Initialize Entry Point
  }
  sb_self_pacer_tick_emit(); // use a notification to trigger the scheduling of a thread again
  for(;;) {
    sb_self_pacer_tock_wait();  // wait on the domain scheduling of pace notification and a domain slot to unblock the AADL Compute Entry Point
    {
      int64_t sb_dummy = 0;
      // trigger the AADL application code compute entry point (along with beginning SendOutputs and ending ReceiveInputs)
      sb_entrypoint_period_producer_thread_i(&sb_dummy);
    }
    // use a notification to trigger the scheduling of a thread again
    sb_self_pacer_tick_emit();
  }
  return 0;
}

```

Notice that in the domain schedule, domain 0 was interleaved with each application thread and 
handled the propagation of notifications after each AADL thread representation,
ensuring that the next thread in the schedule was also available for computation.

```
const dschedule_t ksDomSchedule[] = { // (1 tick == 2ms)
    { .domain = 0, .length = 100 }, // all other seL4 threads, init, 200ms
    { .domain = 2, .length =   5 }, // source       10ms
    { .domain = 0, .length =  95 }, // domain0     190ms
    { .domain = 3, .length =   5 }, // destination  10ms
    { .domain = 0, .length = 295 }, // domain0     590ms    
```

We thought it would be possible to get the above structure working in Microkit, using self-notifications and
by using appropriate priorities, but we couldn't get it to work.  So instead we tried an alternate approach in which
we have a simple userland static scheduler component (PD) ping each of the components.  To avoid self-notifications, which didn't
quite work as we expected, we had to add an addition coordination component just to ping-pong notifications with the 
static scheduler component.

The current Microkit prototype is [here](https://github.com/santoslab/case-tool-assessment-4/tree/main/basic/test_data_port_periodic_domains/microkit).
We tried to set up a simple userland static scheduler that notified each PD to indicate the 
start of its "slot" in the schedule.  The following is excerpted from the 
[Microkit system file](basic/test_data_port_periodic_domains/microkit/dataport.system).

```
<!-- pings from periodic dispatcher to the other component -->
  <channel>
    <end pd="periodic_dispatcher" id="1" />
    <end pd="producer" id="1" />
  </channel>

  <channel>
    <end pd="periodic_dispatcher" id="2" />
    <end pd="consumer" id="2" />
  </channel>

  <channel>
    <end pd="periodic_dispatcher" id="3" />
    <end pd="end_of_schedule_ping" id="3" />
  </channel>

  <!-- ping from end_of_schedule_ping to the periodic dispatcher -->
  <channel>
    <end pd="end_of_schedule_ping" id="4" />
    <end pd="periodic_dispatcher" id="4" />
  </channel>
  ```

A [periodic_dispatcher](basic/test_data_port_periodic_domains/microkit/periodic_dispatcher.c) was coded
to notify each PD/slot in the "static schedule" with priorities declared in the system file for each PD
to enforce the (Producer;Consumer) ordering.

This seemed to give us the correct ordering, but executing on Qemu ran the threads "very quickly" and 
they did not seem to obey the declared "period" in the system file.

## Summary of Prototyping Issues

* We noticed in the Microkit documentation that channels were "bi-directional", enabling each end of the 
channel to notify each other.  It seems best for our purposes to have a uni-direction notification option,
which seems more appropriate for controlling one-way communication between threads (corresponding to one-way
AADL port communication).

* In general, we found the Microkit primitives similar to what we used before with CAmkES.  

* If we could simply figure out the appropriate pattern for a static userland scheduler component, 
it would relatively easy to port from the CASE code generation to Microkit.

* After the above is implemented, we can begin to discuss more in depth alignment with 653, port queuing, etc.




