# data_1_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [data_1_prod_2_cons::top.impl]()|
|:--|

|Thread: data_1_prod_2_cons::producer_t.p |
|:--|
|Type: [producer_t](../../aadl/data_1_prod_2_cons.aadl#L12-L16)<br>Implementation: [producer_t.p](../../aadl/data_1_prod_2_cons.aadl#L17-L23)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L41-L47)<br>Implementation: [consumer_t.p](../../aadl/data_1_prod_2_cons.aadl#L56-L61)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L41-L47)<br>Implementation: [consumer_t.s](../../aadl/data_1_prod_2_cons.aadl#L74-L78)|
|Sporadic : 100 ms|


## Rust Code


### Behavior Code
#### producer: data_1_prod_2_cons::producer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L15-L15'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L19-L23'>Memory Map</a></td></tr>
    </table>


#### consumer: data_1_prod_2_cons::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L44-L44'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L37-L41'>Memory Map</a></td></tr>
    </table>


#### consumer: data_1_prod_2_cons::consumer_t.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L44-L44'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L55-L59'>Memory Map</a></td></tr>
    </table>

