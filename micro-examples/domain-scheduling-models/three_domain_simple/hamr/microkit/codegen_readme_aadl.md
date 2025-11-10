# test_data_port_periodic_three_domains::top.impl

## AADL Architecture
![arch.png](../../aadl/diagrams/arch.png)
|System: [test_data_port_periodic_three_domains::top.impl]()|
|:--|

|Thread: test_data_port_periodic_three_domains::T1.i |
|:--|
|Type: [T1](../../aadl/test_data_port_periodic_three_domains.aadl#L14)<br>Implementation: [T1.i](../../aadl/test_data_port_periodic_three_domains.aadl#L23)|
|Periodic : 100 ms|

|Thread: test_data_port_periodic_three_domains::T2.i |
|:--|
|Type: [T2](../../aadl/test_data_port_periodic_three_domains.aadl#L47)<br>Implementation: [T2.i](../../aadl/test_data_port_periodic_three_domains.aadl#L57)|
|Periodic : 100 ms|

|Thread: test_data_port_periodic_three_domains::T3.i |
|:--|
|Type: [T3](../../aadl/test_data_port_periodic_three_domains.aadl#L84)<br>Implementation: [T3.i](../../aadl/test_data_port_periodic_three_domains.aadl#L93)|
|Periodic : 100 ms|


## Rust Code


### Behavior Code
#### t1: test_data_port_periodic_three_domains::T1.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L17'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='C Interface: Lines 13-17' href='components/p1_t1/src/p1_t1.c#L13'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/p1_t1/src/p1_t1.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 11-15' href='microkit.system#L11'>Memory Map</a></td></tr>
    </table>


#### t2: test_data_port_periodic_three_domains::T2.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L50'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map: Lines 29-33' href='microkit.system#L29'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/p2_t2/src/p2_t2.c#L9'>C var_addr</a> → <a title='C Interface: Lines 17-26' href='components/p2_t2/src/p2_t2.c#L17'>C Interface</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L51'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='C Interface: Lines 28-32' href='components/p2_t2/src/p2_t2.c#L28'>C Interface</a> → <a title='C Shared Memory Variable: Line 11' href='components/p2_t2/src/p2_t2.c#L11'>C var_addr</a> → <a title='Memory Map: Lines 34-38' href='microkit.system#L34'>Memory Map</a></td></tr>
    </table>


#### t3: test_data_port_periodic_three_domains::T3.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L87'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map: Lines 52-56' href='microkit.system#L52'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/p3_t3/src/p3_t3.c#L9'>C var_addr</a> → <a title='C Interface: Lines 16-25' href='components/p3_t3/src/p3_t3.c#L16'>C Interface</a></td></tr>
    </table>

