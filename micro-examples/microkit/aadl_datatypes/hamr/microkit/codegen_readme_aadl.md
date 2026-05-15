# Aadl_Datatypes_System::Sys.i

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [Aadl_Datatypes_System::Sys.i]()|
|:--|

|Thread: Aadl_Datatypes_System::ProducerThr.i |
|:--|
|Type: [ProducerThr](../../aadl/Aadl_Datatypes_System.aadl#L279)<br>Implementation: [ProducerThr.i](../../aadl/Aadl_Datatypes_System.aadl#L320)|
|Periodic |

|Thread: Aadl_Datatypes_System::ConsumerThr.i |
|:--|
|Type: [ConsumerThr](../../aadl/Aadl_Datatypes_System.aadl#L323)<br>Implementation: [ConsumerThr.i](../../aadl/Aadl_Datatypes_System.aadl#L365)|
|Periodic |


## Rust Code


### Behavior Code
#### producer: Aadl_Datatypes_System::ProducerThr.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L283'>myBoolean</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Boolean</td><td><a title='C Interface: Lines 28-32' href='components/producer_producer/src/producer_producer.c#L28'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_producer/src/producer_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 11-15' href='microkit.system#L11'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L287'>myCharacter</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='C Interface: Lines 34-38' href='components/producer_producer/src/producer_producer.c#L34'>C Interface</a> → <a title='C Shared Memory Variable: Line 10' href='components/producer_producer/src/producer_producer.c#L10'>C var_addr</a> → <a title='Memory Map: Lines 16-20' href='microkit.system#L16'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L288'>myString</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='C Interface: Lines 40-44' href='components/producer_producer/src/producer_producer.c#L40'>C Interface</a> → <a title='C Shared Memory Variable: Line 11' href='components/producer_producer/src/producer_producer.c#L11'>C var_addr</a> → <a title='Memory Map: Lines 21-25' href='microkit.system#L21'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L292'>myInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface: Lines 46-50' href='components/producer_producer/src/producer_producer.c#L46'>C Interface</a> → <a title='C Shared Memory Variable: Line 12' href='components/producer_producer/src/producer_producer.c#L12'>C var_addr</a> → <a title='Memory Map: Lines 26-30' href='microkit.system#L26'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L293'>myInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='C Interface: Lines 52-56' href='components/producer_producer/src/producer_producer.c#L52'>C Interface</a> → <a title='C Shared Memory Variable: Line 13' href='components/producer_producer/src/producer_producer.c#L13'>C var_addr</a> → <a title='Memory Map: Lines 31-35' href='microkit.system#L31'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L294'>myInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='C Interface: Lines 58-62' href='components/producer_producer/src/producer_producer.c#L58'>C Interface</a> → <a title='C Shared Memory Variable: Line 14' href='components/producer_producer/src/producer_producer.c#L14'>C var_addr</a> → <a title='Memory Map: Lines 36-40' href='microkit.system#L36'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L295'>myInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='C Interface: Lines 64-68' href='components/producer_producer/src/producer_producer.c#L64'>C Interface</a> → <a title='C Shared Memory Variable: Line 15' href='components/producer_producer/src/producer_producer.c#L15'>C var_addr</a> → <a title='Memory Map: Lines 41-45' href='microkit.system#L41'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L299'>myUInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='C Interface: Lines 70-74' href='components/producer_producer/src/producer_producer.c#L70'>C Interface</a> → <a title='C Shared Memory Variable: Line 16' href='components/producer_producer/src/producer_producer.c#L16'>C var_addr</a> → <a title='Memory Map: Lines 46-50' href='microkit.system#L46'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L300'>myUInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='C Interface: Lines 76-80' href='components/producer_producer/src/producer_producer.c#L76'>C Interface</a> → <a title='C Shared Memory Variable: Line 17' href='components/producer_producer/src/producer_producer.c#L17'>C var_addr</a> → <a title='Memory Map: Lines 51-55' href='microkit.system#L51'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L301'>myUInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='C Interface: Lines 82-86' href='components/producer_producer/src/producer_producer.c#L82'>C Interface</a> → <a title='C Shared Memory Variable: Line 18' href='components/producer_producer/src/producer_producer.c#L18'>C var_addr</a> → <a title='Memory Map: Lines 56-60' href='microkit.system#L56'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L302'>myUInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='C Interface: Lines 88-92' href='components/producer_producer/src/producer_producer.c#L88'>C Interface</a> → <a title='C Shared Memory Variable: Line 19' href='components/producer_producer/src/producer_producer.c#L19'>C var_addr</a> → <a title='Memory Map: Lines 61-65' href='microkit.system#L61'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L306'>myFloat32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='C Interface: Lines 94-98' href='components/producer_producer/src/producer_producer.c#L94'>C Interface</a> → <a title='C Shared Memory Variable: Line 20' href='components/producer_producer/src/producer_producer.c#L20'>C var_addr</a> → <a title='Memory Map: Lines 66-70' href='microkit.system#L66'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L307'>myFloat64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='C Interface: Lines 100-104' href='components/producer_producer/src/producer_producer.c#L100'>C Interface</a> → <a title='C Shared Memory Variable: Line 21' href='components/producer_producer/src/producer_producer.c#L21'>C var_addr</a> → <a title='Memory Map: Lines 71-75' href='microkit.system#L71'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L311'>myEnum</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='C Interface: Lines 106-110' href='components/producer_producer/src/producer_producer.c#L106'>C Interface</a> → <a title='C Shared Memory Variable: Line 22' href='components/producer_producer/src/producer_producer.c#L22'>C var_addr</a> → <a title='Memory Map: Lines 76-80' href='microkit.system#L76'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L312'>myStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='C Interface: Lines 112-116' href='components/producer_producer/src/producer_producer.c#L112'>C Interface</a> → <a title='C Shared Memory Variable: Line 23' href='components/producer_producer/src/producer_producer.c#L23'>C var_addr</a> → <a title='Memory Map: Lines 81-85' href='microkit.system#L81'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L313'>myArray1</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='C Interface: Lines 118-122' href='components/producer_producer/src/producer_producer.c#L118'>C Interface</a> → <a title='C Shared Memory Variable: Line 24' href='components/producer_producer/src/producer_producer.c#L24'>C var_addr</a> → <a title='Memory Map: Lines 86-90' href='microkit.system#L86'>Memory Map</a></td></tr>
    </table>


#### consumer: Aadl_Datatypes_System::ConsumerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L20)

    TimeTriggered: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L30)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L327'>myBoolean</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Boolean</td><td><a title='Memory Map: Lines 104-108' href='microkit.system#L104'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_consumer/src/consumer_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 52-55' href='components/consumer_consumer/src/consumer_consumer.c#L52'>C Interface</a> → <a title='C Extern: Line 14' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 32-42' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L32'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 16-23' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L16'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 203-224' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L203'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L331'>myCharacter</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='Memory Map: Lines 109-113' href='microkit.system#L109'>Memory Map</a> → <a title='C Shared Memory Variable: Line 11' href='components/consumer_consumer/src/consumer_consumer.c#L11'>C var_addr</a> → <a title='C Interface: Lines 65-68' href='components/consumer_consumer/src/consumer_consumer.c#L65'>C Interface</a> → <a title='C Extern: Line 15' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='Rust/C Interface: Lines 44-54' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L44'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 26-33' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L26'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 225-246' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L225'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L332'>myString</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='Memory Map: Lines 114-118' href='microkit.system#L114'>Memory Map</a> → <a title='C Shared Memory Variable: Line 13' href='components/consumer_consumer/src/consumer_consumer.c#L13'>C var_addr</a> → <a title='C Interface: Lines 78-81' href='components/consumer_consumer/src/consumer_consumer.c#L78'>C Interface</a> → <a title='C Extern: Line 16' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L16'>C Extern</a> → <a title='Rust/C Interface: Lines 56-66' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L56'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 36-43' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L36'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 247-268' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L247'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L336'>myInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 119-123' href='microkit.system#L119'>Memory Map</a> → <a title='C Shared Memory Variable: Line 15' href='components/consumer_consumer/src/consumer_consumer.c#L15'>C var_addr</a> → <a title='C Interface: Lines 91-94' href='components/consumer_consumer/src/consumer_consumer.c#L91'>C Interface</a> → <a title='C Extern: Line 17' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L17'>C Extern</a> → <a title='Rust/C Interface: Lines 68-78' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L68'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 46-53' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L46'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 269-290' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L269'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L337'>myInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='Memory Map: Lines 124-128' href='microkit.system#L124'>Memory Map</a> → <a title='C Shared Memory Variable: Line 17' href='components/consumer_consumer/src/consumer_consumer.c#L17'>C var_addr</a> → <a title='C Interface: Lines 104-107' href='components/consumer_consumer/src/consumer_consumer.c#L104'>C Interface</a> → <a title='C Extern: Line 18' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L18'>C Extern</a> → <a title='Rust/C Interface: Lines 80-90' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L80'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 56-63' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L56'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 291-312' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L291'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L338'>myInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map: Lines 129-133' href='microkit.system#L129'>Memory Map</a> → <a title='C Shared Memory Variable: Line 19' href='components/consumer_consumer/src/consumer_consumer.c#L19'>C var_addr</a> → <a title='C Interface: Lines 117-120' href='components/consumer_consumer/src/consumer_consumer.c#L117'>C Interface</a> → <a title='C Extern: Line 19' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L19'>C Extern</a> → <a title='Rust/C Interface: Lines 92-102' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L92'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 66-73' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L66'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 313-334' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L313'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L339'>myInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='Memory Map: Lines 134-138' href='microkit.system#L134'>Memory Map</a> → <a title='C Shared Memory Variable: Line 21' href='components/consumer_consumer/src/consumer_consumer.c#L21'>C var_addr</a> → <a title='C Interface: Lines 130-133' href='components/consumer_consumer/src/consumer_consumer.c#L130'>C Interface</a> → <a title='C Extern: Line 20' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L20'>C Extern</a> → <a title='Rust/C Interface: Lines 104-114' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L104'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 76-83' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L76'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 335-356' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L335'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L343'>myUInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='Memory Map: Lines 139-143' href='microkit.system#L139'>Memory Map</a> → <a title='C Shared Memory Variable: Line 23' href='components/consumer_consumer/src/consumer_consumer.c#L23'>C var_addr</a> → <a title='C Interface: Lines 143-146' href='components/consumer_consumer/src/consumer_consumer.c#L143'>C Interface</a> → <a title='C Extern: Line 21' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L21'>C Extern</a> → <a title='Rust/C Interface: Lines 116-126' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L116'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 86-93' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L86'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 357-378' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L357'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L344'>myUInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='Memory Map: Lines 144-148' href='microkit.system#L144'>Memory Map</a> → <a title='C Shared Memory Variable: Line 25' href='components/consumer_consumer/src/consumer_consumer.c#L25'>C var_addr</a> → <a title='C Interface: Lines 156-159' href='components/consumer_consumer/src/consumer_consumer.c#L156'>C Interface</a> → <a title='C Extern: Line 22' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L22'>C Extern</a> → <a title='Rust/C Interface: Lines 128-138' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L128'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 96-103' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L96'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 379-400' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L379'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L345'>myUInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='Memory Map: Lines 149-153' href='microkit.system#L149'>Memory Map</a> → <a title='C Shared Memory Variable: Line 27' href='components/consumer_consumer/src/consumer_consumer.c#L27'>C var_addr</a> → <a title='C Interface: Lines 169-172' href='components/consumer_consumer/src/consumer_consumer.c#L169'>C Interface</a> → <a title='C Extern: Line 23' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L23'>C Extern</a> → <a title='Rust/C Interface: Lines 140-150' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L140'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 106-113' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L106'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 401-422' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L401'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L346'>myUInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='Memory Map: Lines 154-158' href='microkit.system#L154'>Memory Map</a> → <a title='C Shared Memory Variable: Line 29' href='components/consumer_consumer/src/consumer_consumer.c#L29'>C var_addr</a> → <a title='C Interface: Lines 182-185' href='components/consumer_consumer/src/consumer_consumer.c#L182'>C Interface</a> → <a title='C Extern: Line 24' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L24'>C Extern</a> → <a title='Rust/C Interface: Lines 152-162' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L152'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 116-123' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L116'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 423-444' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L423'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L350'>myFloat32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='Memory Map: Lines 159-163' href='microkit.system#L159'>Memory Map</a> → <a title='C Shared Memory Variable: Line 31' href='components/consumer_consumer/src/consumer_consumer.c#L31'>C var_addr</a> → <a title='C Interface: Lines 195-198' href='components/consumer_consumer/src/consumer_consumer.c#L195'>C Interface</a> → <a title='C Extern: Line 25' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L25'>C Extern</a> → <a title='Rust/C Interface: Lines 164-174' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L164'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 126-133' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L126'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 445-466' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L445'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L351'>myFloat64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='Memory Map: Lines 164-168' href='microkit.system#L164'>Memory Map</a> → <a title='C Shared Memory Variable: Line 33' href='components/consumer_consumer/src/consumer_consumer.c#L33'>C var_addr</a> → <a title='C Interface: Lines 208-211' href='components/consumer_consumer/src/consumer_consumer.c#L208'>C Interface</a> → <a title='C Extern: Line 26' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L26'>C Extern</a> → <a title='Rust/C Interface: Lines 176-186' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L176'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 136-143' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L136'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 467-488' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L467'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L355'>myEnum</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='Memory Map: Lines 169-173' href='microkit.system#L169'>Memory Map</a> → <a title='C Shared Memory Variable: Line 35' href='components/consumer_consumer/src/consumer_consumer.c#L35'>C var_addr</a> → <a title='C Interface: Lines 221-224' href='components/consumer_consumer/src/consumer_consumer.c#L221'>C Interface</a> → <a title='C Extern: Line 27' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L27'>C Extern</a> → <a title='Rust/C Interface: Lines 188-198' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L188'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 146-153' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L146'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 489-510' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L489'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L356'>myStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='Memory Map: Lines 174-178' href='microkit.system#L174'>Memory Map</a> → <a title='C Shared Memory Variable: Line 37' href='components/consumer_consumer/src/consumer_consumer.c#L37'>C var_addr</a> → <a title='C Interface: Lines 234-237' href='components/consumer_consumer/src/consumer_consumer.c#L234'>C Interface</a> → <a title='C Extern: Line 28' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L28'>C Extern</a> → <a title='Rust/C Interface: Lines 200-210' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L200'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 156-163' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L156'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 511-532' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L511'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L357'>myArray1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='Memory Map: Lines 179-183' href='microkit.system#L179'>Memory Map</a> → <a title='C Shared Memory Variable: Line 39' href='components/consumer_consumer/src/consumer_consumer.c#L39'>C var_addr</a> → <a title='C Interface: Lines 247-250' href='components/consumer_consumer/src/consumer_consumer.c#L247'>C Interface</a> → <a title='C Extern: Line 29' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L29'>C Extern</a> → <a title='Rust/C Interface: Lines 212-222' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L212'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 166-173' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L166'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 533-554' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L533'>Rust/Verus API</a></td></tr>
    </table>

