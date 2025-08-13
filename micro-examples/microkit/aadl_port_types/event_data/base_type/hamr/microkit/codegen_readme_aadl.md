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


### Behavior Code
#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L20-L20'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L19-L23'>Memory Map</a></td></tr>
    </table>


#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L20-L20'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L31-L35'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_2_prod_2_cons::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L65-L65'>read_port1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L43-L47'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L66-L66'>read_port2</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L48-L52'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_2_prod_2_cons::consumer_t.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L65-L65'>read_port1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L60-L64'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L66-L66'>read_port2</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L65-L69'>Memory Map</a></td></tr>
    </table>

