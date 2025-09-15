# event_data_2_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [event_data_2_prod_2_cons::top.impl]()|
|:--|

|Thread: event_data_2_prod_2_cons::producer_t.p1 |
|:--|
|Type: [producer_t](../../aadl/event_data_2_prod_2_cons.aadl#L38-L42)<br>Implementation: [producer_t.p1](../../aadl/event_data_2_prod_2_cons.aadl#L45-L51)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::producer_t.p1 |
|:--|
|Type: [producer_t](../../aadl/event_data_2_prod_2_cons.aadl#L38-L42)<br>Implementation: [producer_t.p1](../../aadl/event_data_2_prod_2_cons.aadl#L45-L51)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/event_data_2_prod_2_cons.aadl#L83-L90)<br>Implementation: [consumer_t.p](../../aadl/event_data_2_prod_2_cons.aadl#L99-L104)|
|Periodic : 100 ms|

|Thread: event_data_2_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/event_data_2_prod_2_cons.aadl#L83-L90)<br>Implementation: [consumer_t.s](../../aadl/event_data_2_prod_2_cons.aadl#L118-L122)|
|Sporadic : 100 ms|


## Rust Code


### Behavior Code
#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L41-L41'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>event_data_2_prod_2_cons::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L21-L25'>Memory Map</a></td></tr>
    </table>


#### producer: event_data_2_prod_2_cons::producer_t.p1

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L41-L41'>write_port</a></td>
        <td>Out</td><td>Event Data</td>
        <td>event_data_2_prod_2_cons::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L39-L43'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_2_prod_2_cons::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L86-L86'>read_port1</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_2_prod_2_cons::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L57-L61'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L87-L87'>read_port2</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_2_prod_2_cons::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L62-L66'>Memory Map</a></td></tr>
    </table>


#### consumer: event_data_2_prod_2_cons::consumer_t.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L86-L86'>read_port1</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_2_prod_2_cons::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L80-L84'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/event_data_2_prod_2_cons.aadl#L87-L87'>read_port2</a></td>
        <td>In</td><td>Event Data</td>
        <td>event_data_2_prod_2_cons::ArrayOfStruct</td><td><a title='Memory Map' href='microkit.system#L85-L89'>Memory Map</a></td></tr>
    </table>

