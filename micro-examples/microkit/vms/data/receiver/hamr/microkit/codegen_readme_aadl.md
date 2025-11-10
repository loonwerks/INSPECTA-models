# vmR_data::top.impl

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [vmR_data::top.impl]()|
|:--|

|Thread: vmR_data::producer_t.p |
|:--|
|Type: [producer_t](../../aadl/vmR_data.aadl#L12)<br>Implementation: [producer_t.p](../../aadl/vmR_data.aadl#L17)|
|Periodic : 100 ms|

|Thread: vmR_data::consumer_t.p |
|:--|
|Type: [consumer_t](../../aadl/vmR_data.aadl#L41)<br>Implementation: [consumer_t.p](../../aadl/vmR_data.aadl#L56)|
|Periodic : 100 ms|


## Rust Code


### Behavior Code
#### producer: vmR_data::producer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/vmR_data.aadl#L15'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface: Lines 13-17' href='components/producer_p_p_producer/src/producer_p_p_producer.c#L13'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_p_p_producer/src/producer_p_p_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 11-15' href='microkit.system#L11'>Memory Map</a></td></tr>
    </table>


#### consumer: vmR_data::consumer_t.p

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/vmR_data.aadl#L44'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 34-38' href='microkit.system#L34'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 16-25' href='components/consumer_p_p_consumer/src/consumer_p_p_consumer.c#L16'>C Interface</a></td></tr>
    </table>

