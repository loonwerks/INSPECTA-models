# test_data_port_periodic_three_domains::top.impl

## AADL Architecture
![arch.png](../../aadl/diagrams/arch.png)
|System: [test_data_port_periodic_three_domains::top.impl]()|
|:--|

|Thread: test_data_port_periodic_three_domains::T1.i |
|:--|
|Type: [T1](../../aadl/test_data_port_periodic_three_domains.aadl#L14-L22)<br>Implementation: [T1.i](../../aadl/test_data_port_periodic_three_domains.aadl#L23-L25)|
|Periodic : 100 ms|

|Thread: test_data_port_periodic_three_domains::T2.i |
|:--|
|Type: [T2](../../aadl/test_data_port_periodic_three_domains.aadl#L47-L56)<br>Implementation: [T2.i](../../aadl/test_data_port_periodic_three_domains.aadl#L57-L59)|
|Periodic : 100 ms|

|Thread: test_data_port_periodic_three_domains::T3.i |
|:--|
|Type: [T3](../../aadl/test_data_port_periodic_three_domains.aadl#L84-L92)<br>Implementation: [T3.i](../../aadl/test_data_port_periodic_three_domains.aadl#L93-L95)|
|Periodic : 100 ms|


## Rust Code


### Behavior Code
#### t1: test_data_port_periodic_three_domains::T1.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L17-L17'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L17-L21'>Memory Map</a></td></tr>
    </table>


#### t2: test_data_port_periodic_three_domains::T2.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L50-L50'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L29-L33'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L51-L51'>write_port</a></td>
        <td>Out</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L34-L38'>Memory Map</a></td></tr>
    </table>


#### t3: test_data_port_periodic_three_domains::T3.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/test_data_port_periodic_three_domains.aadl#L87-L87'>read_port</a></td>
        <td>In</td><td>Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L46-L50'>Memory Map</a></td></tr>
    </table>

