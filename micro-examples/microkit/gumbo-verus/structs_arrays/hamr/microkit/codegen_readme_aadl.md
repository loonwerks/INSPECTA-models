# Aadl_Datatypes_System::Sys.i

## AADL Architecture
|System: [Aadl_Datatypes_System::Sys.i]()|
|:--|

|Thread: Aadl_Datatypes_System::ProducerThr.i |
|:--|
|Type: [ProducerThr](../../aadl/Gumbo_Structs_Arrays.aadl#L157)<br>Implementation: [ProducerThr.i](../../aadl/Gumbo_Structs_Arrays.aadl#L166)|
|Periodic |

|Thread: Aadl_Datatypes_System::ConsumerThr.i |
|:--|
|Type: [ConsumerThr](../../aadl/Gumbo_Structs_Arrays.aadl#L169)<br>Implementation: [ConsumerThr.i](../../aadl/Gumbo_Structs_Arrays.aadl#L196)<br>GUMBO: [Subclause](../../aadl/Gumbo_Structs_Arrays.aadl#L179)|
|Periodic |


## Rust Code


### Behavior Code
#### producer: Aadl_Datatypes_System::ProducerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/producer_producer/src/component/producer_producer_app.rs#L16)

    TimeTriggered: [Rust](crates/producer_producer/src/component/producer_producer_app.rs#L23)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L160'>myStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes_System::MyStruct.i</td><td><a title='Rust/Verus API: Lines 41-50' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L41'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 12-17' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L12'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 18-23' href='crates/producer_producer/src/bridge/extern_c_api.rs#L18'>Rust/C Interface</a> → <a title='C Extern: Line 14' href='crates/producer_producer/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='C Interface: Lines 14-18' href='components/producer_producer/src/producer_producer.c#L14'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_producer/src/producer_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 17-21' href='microkit.system#L17'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L161'>MyArrayStruct1</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes_System::MyArrayStruct</td><td><a title='Rust/Verus API: Lines 51-60' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L51'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 20-25' href='crates/producer_producer/src/bridge/producer_producer_api.rs#L20'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 25-30' href='crates/producer_producer/src/bridge/extern_c_api.rs#L25'>Rust/C Interface</a> → <a title='C Extern: Line 15' href='crates/producer_producer/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='C Interface: Lines 20-24' href='components/producer_producer/src/producer_producer.c#L20'>C Interface</a> → <a title='C Shared Memory Variable: Line 10' href='components/producer_producer/src/producer_producer.c#L10'>C var_addr</a> → <a title='Memory Map: Lines 22-26' href='microkit.system#L22'>Memory Map</a></td></tr>
    </table>


#### consumer: Aadl_Datatypes_System::ConsumerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L19)

    TimeTriggered: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L26)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L172'>myStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes_System::MyStruct.i</td><td><a title='Memory Map: Lines 40-44' href='microkit.system#L40'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_consumer/src/consumer_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 26-29' href='components/consumer_consumer/src/consumer_consumer.c#L26'>C Interface</a> → <a title='C Extern: Line 14' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 19-29' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L19'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 15-22' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L15'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 59-67' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L59'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L173'>MyArrayStruct1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes_System::MyArrayStruct</td><td><a title='Memory Map: Lines 45-49' href='microkit.system#L45'>Memory Map</a> → <a title='C Shared Memory Variable: Line 11' href='components/consumer_consumer/src/consumer_consumer.c#L11'>C var_addr</a> → <a title='C Interface: Lines 39-42' href='components/consumer_consumer/src/consumer_consumer.c#L39'>C Interface</a> → <a title='C Extern: Line 15' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='Rust/C Interface: Lines 31-41' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L31'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 25-32' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L25'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 68-76' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L68'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Gumbo_Structs_Arrays.aadl#L174'>MyArrayInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes_System::MyArrayInt32</td><td><a title='Memory Map: Lines 50-54' href='microkit.system#L50'>Memory Map</a> → <a title='C Shared Memory Variable: Line 13' href='components/consumer_consumer/src/consumer_consumer.c#L13'>C var_addr</a> → <a title='C Interface: Lines 52-55' href='components/consumer_consumer/src/consumer_consumer.c#L52'>C Interface</a> → <a title='C Extern: Line 16' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L16'>C Extern</a> → <a title='Rust/C Interface: Lines 43-53' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L43'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 35-42' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L35'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 77-85' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L77'>Rust/Verus API</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume isSortedInt32</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L189>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L30>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L22>GUMBOX</a></td>
    </tr>
    <tr><td>assume atLeastOneZero</td>
    <td><a href=../../aadl/Gumbo_Structs_Arrays.aadl#L192>GUMBO</a></td>
    <td><a href=crates/consumer_consumer/src/component/consumer_consumer_app.rs#L59>Verus</a></td>
    <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_GUMBOX.rs#L32>GUMBOX</a></td>
    </tr></table>

