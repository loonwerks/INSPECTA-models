# Aadl_Datatypes_System::Sys.i

## AADL Architecture
|System: [Aadl_Datatypes_System::Sys.i]()|
|:--|

|Thread: Aadl_Datatypes_System::ProducerThr.i |
|:--|
|Type: [ProducerThr](../../aadl/Aadl_Datatypes_System.aadl#L279)<br>Implementation: [ProducerThr.i](../../aadl/Aadl_Datatypes_System.aadl#L320)|
|Periodic |

|Thread: Aadl_Datatypes_System::ConsumerThr.i |
|:--|
|Type: [ConsumerThr](../../aadl/Aadl_Datatypes_System.aadl#L323)<br>Implementation: [ConsumerThr.i](../../aadl/Aadl_Datatypes_System.aadl#L375)<br>GUMBO: [Subclause](../../aadl/Aadl_Datatypes_System.aadl#L364)|
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
        <td>Base_Types::Boolean</td><td><a title='C Interface: Lines 28-32' href='components/producer_producer/src/producer_producer.c#L28'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/producer_producer/src/producer_producer.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 17-21' href='microkit.system#L17'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L287'>myCharacter</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='C Interface: Lines 34-38' href='components/producer_producer/src/producer_producer.c#L34'>C Interface</a> → <a title='C Shared Memory Variable: Line 10' href='components/producer_producer/src/producer_producer.c#L10'>C var_addr</a> → <a title='Memory Map: Lines 22-26' href='microkit.system#L22'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L288'>myString</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='C Interface: Lines 40-44' href='components/producer_producer/src/producer_producer.c#L40'>C Interface</a> → <a title='C Shared Memory Variable: Line 11' href='components/producer_producer/src/producer_producer.c#L11'>C var_addr</a> → <a title='Memory Map: Lines 27-31' href='microkit.system#L27'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L292'>myInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface: Lines 46-50' href='components/producer_producer/src/producer_producer.c#L46'>C Interface</a> → <a title='C Shared Memory Variable: Line 12' href='components/producer_producer/src/producer_producer.c#L12'>C var_addr</a> → <a title='Memory Map: Lines 32-36' href='microkit.system#L32'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L293'>myInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='C Interface: Lines 52-56' href='components/producer_producer/src/producer_producer.c#L52'>C Interface</a> → <a title='C Shared Memory Variable: Line 13' href='components/producer_producer/src/producer_producer.c#L13'>C var_addr</a> → <a title='Memory Map: Lines 37-41' href='microkit.system#L37'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L294'>myInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='C Interface: Lines 58-62' href='components/producer_producer/src/producer_producer.c#L58'>C Interface</a> → <a title='C Shared Memory Variable: Line 14' href='components/producer_producer/src/producer_producer.c#L14'>C var_addr</a> → <a title='Memory Map: Lines 42-46' href='microkit.system#L42'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L295'>myInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='C Interface: Lines 64-68' href='components/producer_producer/src/producer_producer.c#L64'>C Interface</a> → <a title='C Shared Memory Variable: Line 15' href='components/producer_producer/src/producer_producer.c#L15'>C var_addr</a> → <a title='Memory Map: Lines 47-51' href='microkit.system#L47'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L299'>myUInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='C Interface: Lines 70-74' href='components/producer_producer/src/producer_producer.c#L70'>C Interface</a> → <a title='C Shared Memory Variable: Line 16' href='components/producer_producer/src/producer_producer.c#L16'>C var_addr</a> → <a title='Memory Map: Lines 52-56' href='microkit.system#L52'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L300'>myUInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='C Interface: Lines 76-80' href='components/producer_producer/src/producer_producer.c#L76'>C Interface</a> → <a title='C Shared Memory Variable: Line 17' href='components/producer_producer/src/producer_producer.c#L17'>C var_addr</a> → <a title='Memory Map: Lines 57-61' href='microkit.system#L57'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L301'>myUInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='C Interface: Lines 82-86' href='components/producer_producer/src/producer_producer.c#L82'>C Interface</a> → <a title='C Shared Memory Variable: Line 18' href='components/producer_producer/src/producer_producer.c#L18'>C var_addr</a> → <a title='Memory Map: Lines 62-66' href='microkit.system#L62'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L302'>myUInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='C Interface: Lines 88-92' href='components/producer_producer/src/producer_producer.c#L88'>C Interface</a> → <a title='C Shared Memory Variable: Line 19' href='components/producer_producer/src/producer_producer.c#L19'>C var_addr</a> → <a title='Memory Map: Lines 67-71' href='microkit.system#L67'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L306'>myFloat32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='C Interface: Lines 94-98' href='components/producer_producer/src/producer_producer.c#L94'>C Interface</a> → <a title='C Shared Memory Variable: Line 20' href='components/producer_producer/src/producer_producer.c#L20'>C var_addr</a> → <a title='Memory Map: Lines 72-76' href='microkit.system#L72'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L307'>myFloat64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='C Interface: Lines 100-104' href='components/producer_producer/src/producer_producer.c#L100'>C Interface</a> → <a title='C Shared Memory Variable: Line 21' href='components/producer_producer/src/producer_producer.c#L21'>C var_addr</a> → <a title='Memory Map: Lines 77-81' href='microkit.system#L77'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L311'>myEnum</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='C Interface: Lines 106-110' href='components/producer_producer/src/producer_producer.c#L106'>C Interface</a> → <a title='C Shared Memory Variable: Line 22' href='components/producer_producer/src/producer_producer.c#L22'>C var_addr</a> → <a title='Memory Map: Lines 82-86' href='microkit.system#L82'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L312'>myStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='C Interface: Lines 112-116' href='components/producer_producer/src/producer_producer.c#L112'>C Interface</a> → <a title='C Shared Memory Variable: Line 23' href='components/producer_producer/src/producer_producer.c#L23'>C var_addr</a> → <a title='Memory Map: Lines 87-91' href='microkit.system#L87'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L313'>myArray1</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='C Interface: Lines 118-122' href='components/producer_producer/src/producer_producer.c#L118'>C Interface</a> → <a title='C Shared Memory Variable: Line 24' href='components/producer_producer/src/producer_producer.c#L24'>C var_addr</a> → <a title='Memory Map: Lines 92-96' href='microkit.system#L92'>Memory Map</a></td></tr>
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
        <td>Base_Types::Boolean</td><td><a title='Memory Map: Lines 110-114' href='microkit.system#L110'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/consumer_consumer/src/consumer_consumer.c#L9'>C var_addr</a> → <a title='C Interface: Lines 52-55' href='components/consumer_consumer/src/consumer_consumer.c#L52'>C Interface</a> → <a title='C Extern: Line 14' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 32-42' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L32'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 15-22' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L15'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 202-223' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L202'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L331'>myCharacter</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='Memory Map: Lines 115-119' href='microkit.system#L115'>Memory Map</a> → <a title='C Shared Memory Variable: Line 11' href='components/consumer_consumer/src/consumer_consumer.c#L11'>C var_addr</a> → <a title='C Interface: Lines 65-68' href='components/consumer_consumer/src/consumer_consumer.c#L65'>C Interface</a> → <a title='C Extern: Line 15' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='Rust/C Interface: Lines 44-54' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L44'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 25-32' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L25'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 224-245' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L224'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L332'>myString</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='Memory Map: Lines 120-124' href='microkit.system#L120'>Memory Map</a> → <a title='C Shared Memory Variable: Line 13' href='components/consumer_consumer/src/consumer_consumer.c#L13'>C var_addr</a> → <a title='C Interface: Lines 78-81' href='components/consumer_consumer/src/consumer_consumer.c#L78'>C Interface</a> → <a title='C Extern: Line 16' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L16'>C Extern</a> → <a title='Rust/C Interface: Lines 56-66' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L56'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 35-42' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L35'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 246-267' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L246'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L336'>myInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map: Lines 125-129' href='microkit.system#L125'>Memory Map</a> → <a title='C Shared Memory Variable: Line 15' href='components/consumer_consumer/src/consumer_consumer.c#L15'>C var_addr</a> → <a title='C Interface: Lines 91-94' href='components/consumer_consumer/src/consumer_consumer.c#L91'>C Interface</a> → <a title='C Extern: Line 17' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L17'>C Extern</a> → <a title='Rust/C Interface: Lines 68-78' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L68'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 45-52' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L45'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 268-289' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L268'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L337'>myInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='Memory Map: Lines 130-134' href='microkit.system#L130'>Memory Map</a> → <a title='C Shared Memory Variable: Line 17' href='components/consumer_consumer/src/consumer_consumer.c#L17'>C var_addr</a> → <a title='C Interface: Lines 104-107' href='components/consumer_consumer/src/consumer_consumer.c#L104'>C Interface</a> → <a title='C Extern: Line 18' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L18'>C Extern</a> → <a title='Rust/C Interface: Lines 80-90' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L80'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 55-62' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L55'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 290-311' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L290'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L338'>myInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map: Lines 135-139' href='microkit.system#L135'>Memory Map</a> → <a title='C Shared Memory Variable: Line 19' href='components/consumer_consumer/src/consumer_consumer.c#L19'>C var_addr</a> → <a title='C Interface: Lines 117-120' href='components/consumer_consumer/src/consumer_consumer.c#L117'>C Interface</a> → <a title='C Extern: Line 19' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L19'>C Extern</a> → <a title='Rust/C Interface: Lines 92-102' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L92'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 65-72' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L65'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 312-333' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L312'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L339'>myInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='Memory Map: Lines 140-144' href='microkit.system#L140'>Memory Map</a> → <a title='C Shared Memory Variable: Line 21' href='components/consumer_consumer/src/consumer_consumer.c#L21'>C var_addr</a> → <a title='C Interface: Lines 130-133' href='components/consumer_consumer/src/consumer_consumer.c#L130'>C Interface</a> → <a title='C Extern: Line 20' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L20'>C Extern</a> → <a title='Rust/C Interface: Lines 104-114' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L104'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 75-82' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L75'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 334-355' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L334'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L343'>myUInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='Memory Map: Lines 145-149' href='microkit.system#L145'>Memory Map</a> → <a title='C Shared Memory Variable: Line 23' href='components/consumer_consumer/src/consumer_consumer.c#L23'>C var_addr</a> → <a title='C Interface: Lines 143-146' href='components/consumer_consumer/src/consumer_consumer.c#L143'>C Interface</a> → <a title='C Extern: Line 21' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L21'>C Extern</a> → <a title='Rust/C Interface: Lines 116-126' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L116'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 85-92' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L85'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 356-377' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L356'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L344'>myUInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='Memory Map: Lines 150-154' href='microkit.system#L150'>Memory Map</a> → <a title='C Shared Memory Variable: Line 25' href='components/consumer_consumer/src/consumer_consumer.c#L25'>C var_addr</a> → <a title='C Interface: Lines 156-159' href='components/consumer_consumer/src/consumer_consumer.c#L156'>C Interface</a> → <a title='C Extern: Line 22' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L22'>C Extern</a> → <a title='Rust/C Interface: Lines 128-138' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L128'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 95-102' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L95'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 378-399' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L378'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L345'>myUInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='Memory Map: Lines 155-159' href='microkit.system#L155'>Memory Map</a> → <a title='C Shared Memory Variable: Line 27' href='components/consumer_consumer/src/consumer_consumer.c#L27'>C var_addr</a> → <a title='C Interface: Lines 169-172' href='components/consumer_consumer/src/consumer_consumer.c#L169'>C Interface</a> → <a title='C Extern: Line 23' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L23'>C Extern</a> → <a title='Rust/C Interface: Lines 140-150' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L140'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 105-112' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L105'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 400-421' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L400'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L346'>myUInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='Memory Map: Lines 160-164' href='microkit.system#L160'>Memory Map</a> → <a title='C Shared Memory Variable: Line 29' href='components/consumer_consumer/src/consumer_consumer.c#L29'>C var_addr</a> → <a title='C Interface: Lines 182-185' href='components/consumer_consumer/src/consumer_consumer.c#L182'>C Interface</a> → <a title='C Extern: Line 24' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L24'>C Extern</a> → <a title='Rust/C Interface: Lines 152-162' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L152'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 115-122' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L115'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 422-443' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L422'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L350'>myFloat32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='Memory Map: Lines 165-169' href='microkit.system#L165'>Memory Map</a> → <a title='C Shared Memory Variable: Line 31' href='components/consumer_consumer/src/consumer_consumer.c#L31'>C var_addr</a> → <a title='C Interface: Lines 195-198' href='components/consumer_consumer/src/consumer_consumer.c#L195'>C Interface</a> → <a title='C Extern: Line 25' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L25'>C Extern</a> → <a title='Rust/C Interface: Lines 164-174' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L164'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 125-132' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L125'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 444-465' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L444'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L351'>myFloat64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='Memory Map: Lines 170-174' href='microkit.system#L170'>Memory Map</a> → <a title='C Shared Memory Variable: Line 33' href='components/consumer_consumer/src/consumer_consumer.c#L33'>C var_addr</a> → <a title='C Interface: Lines 208-211' href='components/consumer_consumer/src/consumer_consumer.c#L208'>C Interface</a> → <a title='C Extern: Line 26' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L26'>C Extern</a> → <a title='Rust/C Interface: Lines 176-186' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L176'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 135-142' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L135'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 466-487' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L466'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L355'>myEnum</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='Memory Map: Lines 175-179' href='microkit.system#L175'>Memory Map</a> → <a title='C Shared Memory Variable: Line 35' href='components/consumer_consumer/src/consumer_consumer.c#L35'>C var_addr</a> → <a title='C Interface: Lines 221-224' href='components/consumer_consumer/src/consumer_consumer.c#L221'>C Interface</a> → <a title='C Extern: Line 27' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L27'>C Extern</a> → <a title='Rust/C Interface: Lines 188-198' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L188'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 145-152' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L145'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 488-509' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L488'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L356'>myStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='Memory Map: Lines 180-184' href='microkit.system#L180'>Memory Map</a> → <a title='C Shared Memory Variable: Line 37' href='components/consumer_consumer/src/consumer_consumer.c#L37'>C var_addr</a> → <a title='C Interface: Lines 234-237' href='components/consumer_consumer/src/consumer_consumer.c#L234'>C Interface</a> → <a title='C Extern: Line 28' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L28'>C Extern</a> → <a title='Rust/C Interface: Lines 200-210' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L200'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 155-162' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L155'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 510-531' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L510'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L357'>myArray1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='Memory Map: Lines 185-189' href='microkit.system#L185'>Memory Map</a> → <a title='C Shared Memory Variable: Line 39' href='components/consumer_consumer/src/consumer_consumer.c#L39'>C var_addr</a> → <a title='C Interface: Lines 247-250' href='components/consumer_consumer/src/consumer_consumer.c#L247'>C Interface</a> → <a title='C Extern: Line 29' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L29'>C Extern</a> → <a title='Rust/C Interface: Lines 212-222' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L212'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 165-172' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L165'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 532-553' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L532'>Rust/Verus API</a></td></tr>
    </table>

