# event_data_port_port_queues::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [event_data_port_port_queues::top.impl]()|
|:--|

|Thread: event_data_port_port_queues::producer.p |
|:--|
|Type: [producer](../../aadl/event_data_port_queues.aadl#L6-L14)<br>Implementation: [producer.p](../../aadl/event_data_port_queues.aadl#L15-L17)|
|Periodic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_1.s |
|:--|
|Type: [consumer_queue_1](../../aadl/event_data_port_queues.aadl#L42-L49)<br>Implementation: [consumer_queue_1.s](../../aadl/event_data_port_queues.aadl#L50-L52)|
|Sporadic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_2.s |
|:--|
|Type: [consumer_queue_2](../../aadl/event_data_port_queues.aadl#L64-L73)<br>Implementation: [consumer_queue_2.s](../../aadl/event_data_port_queues.aadl#L74-L76)|
|Sporadic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_5.s |
|:--|
|Type: [consumer_queue_5](../../aadl/event_data_port_queues.aadl#L88-L97)<br>Implementation: [consumer_queue_5.s](../../aadl/event_data_port_queues.aadl#L98-L100)|
|Sporadic : 100 ms|


## Rust Code

[Microkit System Description](microkit.system)

### Behavior Code
#### producer: event_data_port_port_queues::producer.p

 - **Entry Points**



#### consumer: event_data_port_port_queues::consumer_queue_1.s

 - **Entry Points**



#### consumer: event_data_port_port_queues::consumer_queue_2.s

 - **Entry Points**



#### consumer: event_data_port_port_queues::consumer_queue_5.s

 - **Entry Points**


