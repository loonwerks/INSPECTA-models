# event_data_2_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [event_data_2_prod_2_cons::top.impl]()|
|:--|

|Thread: event_data_2_prod_2_cons::producer_t.p1 |
|:--|
|Type: [producer_t](../../aadl/event_data_2_prod_2_cons.aadl#L17-L21)<br>Implementation: [producer_t.p1](../../aadl/event_data_2_prod_2_cons.aadl#L24-L30)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::producer_t.p1 |
|:--|
|Type: [producer_t](../../aadl/event_data_2_prod_2_cons.aadl#L17-L21)<br>Implementation: [producer_t.p1](../../aadl/event_data_2_prod_2_cons.aadl#L24-L30)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/event_data_2_prod_2_cons.aadl#L62-L69)<br>Implementation: [consumer_t.p](../../aadl/event_data_2_prod_2_cons.aadl#L78-L83)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/event_data_2_prod_2_cons.aadl#L62-L69)<br>Implementation: [consumer_t.s](../../aadl/event_data_2_prod_2_cons.aadl#L97-L101)|
|Sporadic : 100 ms|


## Rust Code

[Microkit System Description](microkit.system)

### Behavior Code
#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



#### consumer: event_data_2_prod_2_cons::consumer_t.p

 - **Entry Points**



#### consumer: event_data_2_prod_2_cons::consumer_t.s

 - **Entry Points**


