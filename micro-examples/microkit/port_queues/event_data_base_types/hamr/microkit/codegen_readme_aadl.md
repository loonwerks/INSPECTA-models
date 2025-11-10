# event_data_port_port_queues::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [event_data_port_port_queues::top.impl]()|
|:--|

|Thread: event_data_port_port_queues::producer.p |
|:--|
|Type: [producer](../../aadl/event_data_port_queues.aadl#L6)<br>Implementation: [producer.p](../../aadl/event_data_port_queues.aadl#L15)|
|Periodic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_1.s |
|:--|
|Type: [consumer_queue_1](../../aadl/event_data_port_queues.aadl#L42)<br>Implementation: [consumer_queue_1.s](../../aadl/event_data_port_queues.aadl#L50)|
|Sporadic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_2.s |
|:--|
|Type: [consumer_queue_2](../../aadl/event_data_port_queues.aadl#L64)<br>Implementation: [consumer_queue_2.s](../../aadl/event_data_port_queues.aadl#L74)|
|Sporadic : 100 ms|

|Thread: event_data_port_port_queues::consumer_queue_5.s |
|:--|
|Type: [consumer_queue_5](../../aadl/event_data_port_queues.aadl#L88)<br>Implementation: [consumer_queue_5.s](../../aadl/event_data_port_queues.aadl#L98)|
|Sporadic : 100 ms|


## Rust Code


### Behavior Code
#### producer: event_data_port_port_queues::producer.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L9'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface: Lines 15-23' href='components/producer_p_p_producer/src/producer_p_p_producer.c#L15'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_p_p_producer/src/producer_p_p_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 11-15' href='microkit.system#L11'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_port_port_queues::consumer_queue_1.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L45'>read_port</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 39-43' href='microkit.system#L39'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_s1_consumer/src/consumer_p_s1_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 22-25' href='components/consumer_p_s1_consumer/src/consumer_p_s1_consumer.c#L22'>C Interface</a></td></tr>
    </table>


#### consumer: event_data_port_port_queues::consumer_queue_2.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L67'>read_port</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 57-61' href='microkit.system#L57'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_s2_consumer/src/consumer_p_s2_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 22-25' href='components/consumer_p_s2_consumer/src/consumer_p_s2_consumer.c#L22'>C Interface</a></td></tr>
    </table>


#### consumer: event_data_port_port_queues::consumer_queue_5.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_port_queues.aadl#L91'>read_port</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 75-79' href='microkit.system#L75'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_s5_consumer/src/consumer_p_s5_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 22-25' href='components/consumer_p_s5_consumer/src/consumer_p_s5_consumer.c#L22'>C Interface</a></td></tr>
    </table>

