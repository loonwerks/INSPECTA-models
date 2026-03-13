# AADL Port Types

These micro-examples illustrate how the three AADL port families — **data
ports**, **event ports**, and **event data ports** — are handled by HAMR for
the seL4 Microkit target.  For data and event data ports, three payload
varieties are demonstrated: a base type, a fixed-size array, and a user-defined
struct.  Each micro-example contains both an AADL model and a SysML model,
with generated C code for two scheduling variants.

## Data Ports

Data ports carry a latest-value payload: a writer always overwrites the
previous value, and a reader always sees the most recently written value with
no queuing.

| Micro-example | Description |
|---|---|
| [data/base\_type](data/base_type/) | Data ports carrying a `Base_Types::Integer_8` payload — the simplest base-type data port |
| [data/array](data/array/) | Data ports carrying an array-of-struct payload |
| [data/struct](data/struct/) | Data ports carrying a user-defined struct payload |

## Event Ports

Event ports carry no payload — they convey only the occurrence of an event.
They queue up to the declared queue size and trigger sporadic dispatch.

| Micro-example | Description |
|---|---|
| [event](event/) | Event ports with no payload, demonstrating both periodic and sporadic consumer patterns |

## Event Data Ports

Event data ports combine event queueing semantics with a typed payload: each
sent value is queued independently and consumed once.

| Micro-example | Description |
|---|---|
| [event\_data/base\_type](event_data/base_type/) | Event data ports carrying a `Base_Types::Integer_8` payload |
| [event\_data/array](event_data/array/) | Event data ports carrying an array-of-struct payload |
| [event\_data/struct](event_data/struct/) | Event data ports carrying a user-defined struct payload |
