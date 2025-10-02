# data_1_prod_2_cons::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [data_1_prod_2_cons::top.impl]()|
|:--|

|Thread: data_1_prod_2_cons::producer_t.p |
|:--|
|Type: [producer_t](../../aadl/data_1_prod_2_cons.aadl#L33)<br>Implementation: [producer_t.p](../../aadl/data_1_prod_2_cons.aadl#L38)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L62)<br>Implementation: [consumer_t.p](../../aadl/data_1_prod_2_cons.aadl#L77)|
|Periodic : 100 ms|

|Thread: data_1_prod_2_cons::consumer_t.s |
|:--|
|Type: [consumer_t](../../aadl/data_1_prod_2_cons.aadl#L62)<br>Implementation: [consumer_t.s](../../aadl/data_1_prod_2_cons.aadl#L95)|
|Sporadic : 100 ms|


## Rust Code


### Behavior Code
#### producer: data_1_prod_2_cons::producer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L36'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>data_1_prod_2_cons::struct.i</td><td><a title='C Interface: Lines 13-17' href='components/producer_p_p_producer/src/producer_p_p_producer.c#L13'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_p_p_producer/src/producer_p_p_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 19-23' href='microkit.system#L19'>Memory Map</a></td></tr>
    </table>


#### consumer: data_1_prod_2_cons::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L65'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>data_1_prod_2_cons::struct.i</td><td><a title='Memory Map: Lines 37-41' href='microkit.system#L37'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 16-25' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L16'>C Interface</a></td></tr>
    </table>


#### consumer: data_1_prod_2_cons::consumer_t.s

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/data_1_prod_2_cons.aadl#L65'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>data_1_prod_2_cons::struct.i</td><td><a title='Memory Map: Lines 55-59' href='microkit.system#L55'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_s_consumer/src/consumer_p_s_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 16-25' href='components/consumer_p_s_consumer/src/consumer_p_s_consumer.c#L16'>C Interface</a></td></tr>
    </table>

