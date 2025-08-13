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


### Behavior Code
#### producer: event_data_port_port_queues::producer.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L30-L30'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>event_data_port_port_queues::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L19-L23'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_port_port_queues::consumer_queue_1.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L66-L66'>read_port</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_port_port_queues::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L41-L45'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_port_port_queues::consumer_queue_2.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L88-L90'>read_port</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_port_port_queues::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L53-L57'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_port_port_queues::consumer_queue_5.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L112-L114'>read_port</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_port_port_queues::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L65-L69'>Memory Map</a></td></tr>
    </table>

