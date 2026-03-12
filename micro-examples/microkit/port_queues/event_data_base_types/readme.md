# AADL Event Data Port Queue Sizes — Base Type Payload

This micro-example demonstrates how **queue size** is specified on AADL
**event data ports** that carry a **base type** payload, and how HAMR codegen
realizes those sizes in the generated C API.  The example contains one periodic
producer and three sporadic consumers — each consumer declares a different
queue size (1, 2, and 5) on its input port.  The payload type is
`Base_Types::Integer_8`, which maps to C's standard `int8_t`.  Two model
representations and two corresponding sets of generated code are included.
All consumer components are implemented in C; HAMR currently only supports
queue sizes of 1 for Rust components that contain GUMBO contracts.

 Table of Contents
  * [Models](#models)
    * [AADL Model](#aadl-model)
    * [SysML Model](#sysml-model)
  * [AADL Event Data Port Semantics](#aadl-event-data-port-semantics)
  * [Specifying Queue Size](#specifying-queue-size)
    * [In AADL](#in-aadl)
    * [In SysML](#in-sysml)
  * [How the Generated API Realizes Event Data Port Semantics](#how-the-generated-api-realizes-event-data-port-semantics)
    * [Payload Type](#payload-type)
    * [Sender API — `put_write_port()`](#sender-api--put_write_port)
    * [Receiver API — `get_read_port()` and `get_read_port_poll()`](#receiver-api--get_read_port-and-get_read_port_poll)
    * [Queue Size in the Generated Code](#queue-size-in-the-generated-code)
    * [Sporadic Consumer](#sporadic-consumer)

---

## Models

### Arch
![Arch](aadl/diagrams/arch.svg)

---

### AADL Metrics
| | |
|--|--|
|Threads|4|
|Ports|4|
|Connections|3|

---

### AADL Model

The primary model is written in AADL and lives under [`aadl/`](aadl/).  It
describes one periodic **producer** thread (`producer_p_p_producer`) with one
output event data port carrying a `Base_Types::Integer_8` payload, and three
sporadic **consumer** threads that each receive from the producer with
different input queue sizes:

- `consumer_p_s1_consumer` — default queue size (1)
- `consumer_p_s2_consumer` — `Queue_Size => 2`
- `consumer_p_s5_consumer` — `Queue_Size => 5`

When no explicit scheduling property is specified, the default scheduling
strategy for the HAMR Microkit platform is **seL4 domain scheduling**, which
statically partitions execution time across the threads.  HAMR codegen
targeting the **Microkit** platform produces C code in
[`hamr/microkit/`](hamr/microkit/).

### SysML Model

The SysML model in
[`sysml/event_data_base_types_port_queues.sysml`](sysml/event_data_base_types_port_queues.sysml)
was **derived/converted from the AADL model**.  The structure (threads, port
types, connections, and queue sizes) is identical to the AADL model, but
expressed in SysML v2 syntax using the
[santoslab AADL SysML libraries](https://github.com/santoslab/sysml-aadl-libraries).
The key modeling difference is the explicit scheduling strategy: the SysML
model sets `attribute :>> Scheduling = MCS` to use **MCS (Mixed-Criticality
Scheduling) user-land scheduling**.  HAMR codegen targeting the **Microkit**
platform produces C code in [`hamr/microkit_mcs/`](hamr/microkit_mcs/).

For installation, codegen, and simulation instructions see:
- [aadl_readme.md](aadl_readme.md) — AADL model, seL4 domain scheduling, generated code in `hamr/microkit/`
- [sysml_readme.md](sysml_readme.md) — SysML model, MCS user-land scheduling, generated code in `hamr/microkit_mcs/`

---

## AADL Event Data Port Semantics

An AADL **event data port** combines event port queueing semantics with a
typed payload.  Its semantics differ from data ports and event-only ports in
the following key ways:

- **Typed payload.**  Each event carries a value of the declared data type.
  The receiver obtains a copy of that value when it dequeues the event.

- **Queued, not latest-value.**  Unlike a data port (which holds only the most
  recently written value), events accumulate up to the declared queue size.
  Each `put_write_port()` call appends one event; each successful
  `get_read_port()` call removes exactly one event from the front of the queue.

- **Consumed once.**  A successful dequeue removes the event.  The value is
  not available on the next read.

- **Queue overflow.**  When the queue is full and a new event arrives, the
  oldest unread event is overwritten and lost.  `*numDropped` reflects how many
  events were lost in this way since the previous dequeue.

- **Broadcast to independent per-receiver queues.**  The single
  `put_write_port()` call enqueues into a separate shared-memory queue for
  each connected receiver.  Each receiver's queue is sized to its own declared
  `Queue_Size` and is drained independently — one receiver's consumption rate
  does not affect any other.

- **Dispatch for sporadic threads.**  An event data port arrival is a valid
  trigger for sporadic thread dispatch.  The generated `notified()` handler
  checks `read_port_is_empty()` before calling the user-supplied handler, and
  the user handler drains the queue with a `while` loop.

---

## Specifying Queue Size

### In AADL

Queue size is declared as a property on the port feature.  Omitting
`Queue_Size` gives the AADL default of 1.

```aadl
thread consumer_queue_1
    features
        read_port: in event data port Base_Types::Integer_8;
    properties
        Dispatch_Protocol => Sporadic;
        Compute_Execution_Time => 100ms .. 100ms;
end consumer_queue_1;

thread consumer_queue_2
    features
        read_port: in event data port Base_Types::Integer_8 {
            Queue_Size => 2;
        };
    properties
        Dispatch_Protocol => Sporadic;
        Compute_Execution_Time => 100ms .. 100ms;
end consumer_queue_2;

thread consumer_queue_5
    features
        read_port: in event data port Base_Types::Integer_8 {
            Queue_Size => 5;
        };
    properties
        Dispatch_Protocol => Sporadic;
        Compute_Execution_Time => 100ms .. 100ms;
end consumer_queue_5;
```

### In SysML

Queue size is declared as an attribute inside the `EventDataPort` block.
Omitting `Queue_Size` gives the default of 1.

```sysml
part def consumer_queue_1_t_s :> Sporadic_Thread {
    port read_port : EventDataPort { in :>> type : Base_Types::Integer_8; }
}

part def consumer_queue_2_t_s :> Sporadic_Thread {
    port read_port : EventDataPort {
        in :>> type : Base_Types::Integer_8;
        attribute :>> Queue_Size = 2;
    }
}

part def consumer_queue_5_t_s :> Sporadic_Thread {
    port read_port : EventDataPort {
        in :>> type : Base_Types::Integer_8;
        attribute :>> Queue_Size = 5;
    }
}
```

---

## How the Generated API Realizes Event Data Port Semantics

### Payload Type

`Base_Types::Integer_8` maps to C's standard `int8_t`.  Because `int8_t` is
a built-in C type, HAMR does not generate a typedef for it —
[`types/include/sb_aadl_types.h`](hamr/microkit/types/include/sb_aadl_types.h)
contains no type declarations for this example.  The `int8_t` type is used
directly in the generated queue headers and component APIs.  This contrasts
with user-defined AADL data types (structs, arrays), for which HAMR generates
corresponding C typedefs in `sb_aadl_types.h`.

---

### Sender API — `put_write_port()`

Generated in the **non-user-editable**
[`producer_p_p_producer.c`](hamr/microkit/components/producer_p_p_producer/src/producer_p_p_producer.c):

```c
bool put_write_port(const int8_t *data) {
  sb_queue_int8_t_1_enqueue((sb_queue_int8_t_1_t *) write_port_queue_1, (int8_t *) data);

  sb_queue_int8_t_2_enqueue((sb_queue_int8_t_2_t *) write_port_queue_2, (int8_t *) data);

  sb_queue_int8_t_5_enqueue((sb_queue_int8_t_5_t *) write_port_queue_5, (int8_t *) data);

  return true;
}
```

A single `put_write_port()` call enqueues the `int8_t` value into all three
connected receiver queues independently.  Each enqueue always succeeds and
never blocks — there is no back-pressure from any receiver.  The three queue
variables (`write_port_queue_1`, `write_port_queue_2`, `write_port_queue_5`)
are separate regions of shared memory, one per receiver, each sized to its
receiver's declared `Queue_Size`.

---

### Receiver API — `get_read_port()` and `get_read_port_poll()`

Generated in the **non-user-editable** consumer `.c` files.  The shape is
identical for all three consumers; only the queue type suffix differs.  Shown
here for `consumer_p_s1_consumer` (queue size 1) from
[`consumer_p_s1_consumer.c`](hamr/microkit/components/consumer_p_s1_consumer/src/consumer_p_s1_consumer.c):

```c
bool read_port_is_empty(void) {
  return sb_queue_int8_t_1_is_empty(&read_port_recv_queue);
}

bool get_read_port_poll(sb_event_counter_t *numDropped, int8_t *data) {
  return sb_queue_int8_t_1_dequeue((sb_queue_int8_t_1_Recv_t *) &read_port_recv_queue, numDropped, data);
}

bool get_read_port(int8_t *data) {
  sb_event_counter_t numDropped;
  return get_read_port_poll (&numDropped, data);
}
```

`dequeue` compares the receiver's `numRecv` counter to the sender's `numSent`
counter.  If `numSent > numRecv` it copies the next enqueued `int8_t` into
`*data`, increments `numRecv`, and returns `true`.  If `numSent == numRecv`
the queue is empty and returns `false` without touching `*data`.

`*numDropped` reports how many events were overwritten and lost since the previous dequeue — events will be lost if the senders send more events than the receiver's queue can hold before it is dispatched.  `get_read_port()` silently discards this value; `get_read_port_poll()` exposes it for callers that need to detect missed events.

Consumers with queue sizes 2 and 5 use the `_2` and `_5` suffixed queue types
respectively; the API shape is identical.

---

### Queue Size in the Generated Code

For base type payloads, HAMR names the generated queue types after the C
primitive type rather than an AADL data type name.  A distinct queue type is
generated for each unique `Queue_Size` value used in the model.  Because one
slot is always held as the next-to-write (dirty) slot, the allocated ring
buffer has `Queue_Size + 1` slots:

| AADL `Queue_Size` | Generated type suffix | Ring buffer `_SIZE` |
|---|---|---|
| 1 (default) | `int8_t_1` | 2 |
| 2 | `int8_t_2` | 3 |
| 5 | `int8_t_5` | 6 |

From the generated queue headers:

```c
// sb_queue_int8_t_1.h
#define SB_QUEUE_INT8_T_1_SIZE 2

// sb_queue_int8_t_2.h
#define SB_QUEUE_INT8_T_2_SIZE 3

// sb_queue_int8_t_5.h
#define SB_QUEUE_INT8_T_5_SIZE 6
```

Each queue struct holds an `_Atomic sb_event_counter_t numSent` (written by
the sender) and a ring buffer of `_SIZE` elements:

```c
typedef struct sb_queue_int8_t_1 {
  _Atomic sb_event_counter_t numSent;
  int8_t elt[SB_QUEUE_INT8_T_1_SIZE];
} sb_queue_int8_t_1_t;
```

Each receiver maintains its own `numRecv` counter in a private
`sb_queue_int8_t_1_Recv_t` struct that it owns (not shared memory).

---

### Sporadic Consumer

The generated `notified()` in all three consumer `.c` files follows the same
pattern — shown here for
[`consumer_p_s1_consumer.c`](hamr/microkit/components/consumer_p_s1_consumer/src/consumer_p_s1_consumer.c):

```c
void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      if (!read_port_is_empty()) {
        handle_read_port();
      }
      break;
    default:
      consumer_p_s1_consumer_notify(channel);
  }
}
```

The `!read_port_is_empty()` guard ensures `handle_read_port()` is only called
when at least one event is present.  The user-supplied `handle_read_port()`
implementation in
[`consumer_p_s1_consumer_user.c`](hamr/microkit/components/consumer_p_s1_consumer/src/consumer_p_s1_consumer_user.c)
drains the entire queue in a `while` loop:

```c
void handle_read_port(void) {
    int32_t received = 0;
    int8_t value;

    // keep dequeuing until no more things can be had
    while(get_read_port(&value)) {
      printf("%s: received value {%d}\n", microkit_name, value);
      received++;
    }

    printf("%s: %i events received\n", microkit_name, received);
}
```

Each consumer drains all queued events on every dispatch, so the number of
`received value` lines per dispatch reflects how many events accumulated in
that consumer's queue since its last dispatch.
