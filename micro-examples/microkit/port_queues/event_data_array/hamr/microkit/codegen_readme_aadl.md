# event_data_port_port_queues::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [event_data_port_port_queues::top.impl]()|
|:--|

|Thread: event_data_port_port_queues::producer.p |
|:--|
|Type: [producer](../../aadl/event_data_port_queues.aadl#L27-L35)<br>Implementation: [producer.p](../../aadl/event_data_port_queues.aadl#L36-L38)|
|Periodic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_1.s |
|:--|
|Type: [consumer_queue_1](../../aadl/event_data_port_queues.aadl#L63-L70)<br>Implementation: [consumer_queue_1.s](../../aadl/event_data_port_queues.aadl#L71-L73)|
|Sporadic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_2.s |
|:--|
|Type: [consumer_queue_2](../../aadl/event_data_port_queues.aadl#L85-L94)<br>Implementation: [consumer_queue_2.s](../../aadl/event_data_port_queues.aadl#L95-L97)|
|Sporadic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_5.s |
|:--|
|Type: [consumer_queue_5](../../aadl/event_data_port_queues.aadl#L109-L118)<br>Implementation: [consumer_queue_5.s](../../aadl/event_data_port_queues.aadl#L119-L121)|
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


