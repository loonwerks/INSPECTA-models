# event_data_2_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [event_data_2_prod_2_cons::top.impl]()|
|:--|

|Thread: event_data_2_prod_2_cons::producer_t.p1 |
|:--|
|Type: [producer_t](../../aadl/event_data_2_prod_2_cons.aadl#L17)<br>Implementation: [producer_t.p1](../../aadl/event_data_2_prod_2_cons.aadl#L24)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::producer_t.p1 |
|:--|
|Type: [producer_t](../../aadl/event_data_2_prod_2_cons.aadl#L17)<br>Implementation: [producer_t.p1](../../aadl/event_data_2_prod_2_cons.aadl#L24)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/event_data_2_prod_2_cons.aadl#L62)<br>Implementation: [consumer_t.p](../../aadl/event_data_2_prod_2_cons.aadl#L78)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/event_data_2_prod_2_cons.aadl#L62)<br>Implementation: [consumer_t.s](../../aadl/event_data_2_prod_2_cons.aadl#L97)|
|Sporadic : 100 ms|


## Rust Code


### Behavior Code
#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L20'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface: Lines 13-17' href='components/producer_p_p1_producer/src/producer_p_p1_producer.c#L13'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_p_p1_producer/src/producer_p_p1_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 11-15' href='microkit.system#L11'>Memory Map</a></td></tr>
    </table>


#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L20'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface: Lines 13-17' href='components/producer_p_p2_producer/src/producer_p_p2_producer.c#L13'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_p_p2_producer/src/producer_p_p2_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 29-33' href='microkit.system#L29'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_2_prod_2_cons::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L65'>read_port1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 47-51' href='microkit.system#L47'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 24-27' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L24'>C Interface</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L66'>read_port2</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 52-56' href='microkit.system#L52'>Memory Map</a> → <a title='C Shared Memory Variable: Line 11' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L11'>C var_addr</a> → <a title='C Interface: Lines 37-40' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L37'>C Interface</a></td></tr>
    </table>


#### consumer: event_data_2_prod_2_cons::consumer_t.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L65'>read_port1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 70-74' href='microkit.system#L70'>Memory Map</a> → <a title='C Shared Memory Variable: Line 10' href='components/consumer_p_s_consumer/src/consumer_p_s_consumer.c#L10'>C var_addr</a> → <a title='C Interface: Lines 25-28' href='components/consumer_p_s_consumer/src/consumer_p_s_consumer.c#L25'>C Interface</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L66'>read_port2</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 75-79' href='microkit.system#L75'>Memory Map</a> → <a title='C Shared Memory Variable: Line 12' href='components/consumer_p_s_consumer/src/consumer_p_s_consumer.c#L12'>C var_addr</a> → <a title='C Interface: Lines 38-41' href='components/consumer_p_s_consumer/src/consumer_p_s_consumer.c#L38'>C Interface</a></td></tr>
    </table>

