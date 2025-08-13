# data_1_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [data_1_prod_2_cons::top.impl]()|
|:--|

|Thread: data_1_prod_2_cons::producer_t.p |
|:--|
|Type: [producer_t](../../aadl/data_1_prod_2_cons.aadl#L33-L37)<br>Implementation: [producer_t.p](../../aadl/data_1_prod_2_cons.aadl#L38-L44)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L62-L68)<br>Implementation: [consumer_t.p](../../aadl/data_1_prod_2_cons.aadl#L77-L82)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L62-L68)<br>Implementation: [consumer_t.s](../../aadl/data_1_prod_2_cons.aadl#L95-L99)|
|Sporadic : 100 ms|


## Rust Code


### Behavior Code
#### producer: data_1_prod_2_cons::producer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L36-L36'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>data_1_prod_2_cons::struct.i</td><td><a title='Memory Map' href='microkit.system#L17-L21'>Memory Map</a></td></tr>
    </table>


#### consumer: data_1_prod_2_cons::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L65-L65'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>data_1_prod_2_cons::struct.i</td><td><a title='Memory Map' href='microkit.system#L29-L33'>Memory Map</a></td></tr>
    </table>


#### consumer: data_1_prod_2_cons::consumer_t.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L65-L65'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>data_1_prod_2_cons::struct.i</td><td><a title='Memory Map' href='microkit.system#L41-L45'>Memory Map</a></td></tr>
    </table>

