# Gumbo_Structs_Arrays::Sys.i

## AADL Architecture
|System: [Gumbo_Structs_Arrays::Sys.i]()|
|:--|

|Thread: Gumbo_Structs_Arrays::ProducerThr.i |
|:--|
|Type: [ProducerThr](../../aadl/Gumbo_Structs_Arrays.aadl#L157)<br>Implementation: [ProducerThr.i](../../aadl/Gumbo_Structs_Arrays.aadl#L166)|
|Periodic |

|Thread: Gumbo_Structs_Arrays::ConsumerThr.i |
|:--|
|Type: [ConsumerThr](../../aadl/Gumbo_Structs_Arrays.aadl#L169)<br>Implementation: [ConsumerThr.i](../../aadl/Gumbo_Structs_Arrays.aadl#L281)<br>GUMBO: [Subclause](../../aadl/Gumbo_Structs_Arrays.aadl#L179)|
|Periodic |


## Rust Code


### Behavior Code
#### producer: Gumbo_Structs_Arrays::ProducerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/producer_producer/src/component/producer_producer_app.rs#L16)

    TimeTriggered: [Rust](crates/producer_producer/src/component/producer_producer_app.rs#L23)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L160'>myStructArray</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Gumbo_Structs_Arrays::MyStructArray.i</td><td><a title='Rust/Verus API: Lines 41-50' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L41'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 12-17' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L12'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 18-23' href='crates/producer_producer/src/bridge/extern_c_api.rs#L18'>Rust/C Interface</a> → <a title='C Extern: Line 14' href='crates/producer_producer/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='C Interface: Lines 14-18' href='components/producer_producer/src/producer_producer.c#L14'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_producer/src/producer_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 11-15' href='microkit.system#L11'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L161'>MyArrayStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Gumbo_Structs_Arrays::MyArrayStruct</td><td><a title='Rust/Verus API: Lines 51-60' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L51'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 20-25' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L20'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 25-30' href='crates/producer_producer/src/bridge/extern_c_api.rs#L25'>Rust/C Interface</a> → <a title='C Extern: Line 15' href='crates/producer_producer/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='C Interface: Lines 20-24' href='components/producer_producer/src/producer_producer.c#L20'>C Interface</a> → <a title='C Shared Memory Variable: Line 10' href='components/producer_producer/src/producer_producer.c#L10'>C var_addr</a> → <a title='Memory Map: Lines 16-20' href='microkit.system#L16'>Memory Map</a></td></tr>
    </table>


#### consumer: Gumbo_Structs_Arrays::ConsumerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L19)

    TimeTriggered: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L26)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L172'>myStructArray</a></td>
        <td>In</td><td>Event Data</td>
        <td>Gumbo_Structs_Arrays::MyStructArray.i</td><td><a title='Memory Map: Lines 34-38' href='microkit.system#L34'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_consumer/src/consumer_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 26-29' href='components/consumer_consumer/src/consumer_consumer.c#L26'>C Interface</a> → <a title='C Extern: Line 14' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 19-29' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L19'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 15-22' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L15'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 59-67' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L59'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L173'>MyArrayStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Gumbo_Structs_Arrays::MyArrayStruct</td><td><a title='Memory Map: Lines 39-43' href='microkit.system#L39'>Memory Map</a> → <a title='C Shared Memory Variable: Line 11' href='components/consumer_consumer/src/consumer_consumer.c#L11'>C var_addr</a> → <a title='C Interface: Lines 39-42' href='components/consumer_consumer/src/consumer_consumer.c#L39'>C Interface</a> → <a title='C Extern: Line 15' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='Rust/C Interface: Lines 31-41' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L31'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 25-32' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L25'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 68-76' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L68'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L174'>MyArrayInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Gumbo_Structs_Arrays::MyArrayInt32</td><td><a title='Memory Map: Lines 44-48' href='microkit.system#L44'>Memory Map</a> → <a title='C Shared Memory Variable: Line 13' href='components/consumer_consumer/src/consumer_consumer.c#L13'>C var_addr</a> → <a title='C Interface: Lines 52-55' href='components/consumer_consumer/src/consumer_consumer.c#L52'>C Interface</a> → <a title='C Extern: Line 16' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L16'>C Extern</a> → <a title='Rust/C Interface: Lines 43-53' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L43'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 35-42' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L35'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 77-85' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L77'>Rust/Verus API</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume isSorted_StructArray</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L239>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L56>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L130>GUMBOX</a></td>
    </tr>
    <tr><td>assume atLeastOneZero_StructArray</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L243>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L58>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L142>GUMBOX</a></td>
    </tr>
    <tr><td>assume isSorted_ArrayInt32</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L247>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L60>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L154>GUMBOX</a></td>
    </tr>
    <tr><td>assume atLeastOneZero_ArrayInt32</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L251>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L62>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L166>GUMBOX</a></td>
    </tr>
    <tr><td>assume isSorted_ArrayStruct</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L255>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L64>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L180>GUMBOX</a></td>
    </tr>
    <tr><td>assume atLeastOneZero_ArrayStruct</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L260>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L72>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L196>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee conversions</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L264>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L77>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L246>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>convertB</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L186>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L134>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L17>GUMBOX</a></td>
    </tr>
    <tr><td>convertS8</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L196>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L146>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L29>GUMBOX</a></td>
    </tr>
    <tr><td>convertS16</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L200>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L158>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L41>GUMBOX</a></td>
    </tr>
    <tr><td>convertS32</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L204>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L170>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L53>GUMBOX</a></td>
    </tr>
    <tr><td>convertS64</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L208>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L182>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L65>GUMBOX</a></td>
    </tr>
    <tr><td>convertU8</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L212>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L194>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L77>GUMBOX</a></td>
    </tr>
    <tr><td>convertU16</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L216>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L206>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L89>GUMBOX</a></td>
    </tr>
    <tr><td>convertU32</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L220>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L218>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L101>GUMBOX</a></td>
    </tr>
    <tr><td>convertU64</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L224>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L230>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L113>GUMBOX</a></td>
    </tr></table>

