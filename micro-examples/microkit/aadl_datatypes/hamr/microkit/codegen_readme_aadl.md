# Aadl_Datatypes_System::Sys.i

## AADL Architecture
|System: [Aadl_Datatypes_System::Sys.i]()|
|:--|

|Thread: Aadl_Datatypes_System::ProducerThr.i |
|:--|
|Type: [ProducerThr](../../aadl/Aadl_Datatypes_System.aadl#L279-L319)<br>Implementation: [ProducerThr.i](../../aadl/Aadl_Datatypes_System.aadl#L320-L322)|
|Periodic |

|Thread: Aadl_Datatypes_System::ConsumerThr.i |
|:--|
|Type: [ConsumerThr](../../aadl/Aadl_Datatypes_System.aadl#L323-L363)<br>Implementation: [ConsumerThr.i](../../aadl/Aadl_Datatypes_System.aadl#L364-L366)|
|Periodic |


## Rust Code


### Behavior Code
#### producer: Aadl_Datatypes_System::ProducerThr.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L283-L283'>myBoolean</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Boolean</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L28-L32'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L9-L9'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L17-L21'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L287-L287'>myCharacter</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L34-L38'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L10-L10'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L22-L26'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L288-L288'>myString</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L40-L44'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L11-L11'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L27-L31'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L292-L292'>myInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L46-L50'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L12-L12'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L32-L36'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L293-L293'>myInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L52-L56'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L13-L13'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L37-L41'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L294-L294'>myInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L58-L62'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L14-L14'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L42-L46'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L295-L295'>myInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L64-L68'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L15-L15'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L47-L51'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L299-L299'>myUInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L70-L74'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L16-L16'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L52-L56'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L300-L300'>myUInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L76-L80'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L17-L17'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L57-L61'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L301-L301'>myUInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L82-L86'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L18-L18'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L62-L66'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L302-L302'>myUInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L88-L92'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L19-L19'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L67-L71'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L306-L306'>myFloat32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L94-L98'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L20-L20'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L72-L76'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L307-L307'>myFloat64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L100-L104'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L21-L21'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L77-L81'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L311-L311'>myEnum</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L106-L110'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L22-L22'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L82-L86'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L312-L312'>myStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L112-L116'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L23-L23'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L87-L91'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L313-L313'>myArray1</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='C Interface' href='components/producer_producer/src/producer_producer.c#L118-L122'>C Interface</a> → <a title='C Shared Memory Variable' href='components/producer_producer/src/producer_producer.c#L24-L24'>C var_addr</a> → <a title='Memory Map' href='microkit.system#L92-L96'>Memory Map</a></td></tr>
    </table>


#### consumer: Aadl_Datatypes_System::ConsumerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L20-L28)

    TimeTriggered: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L30-L45)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L327-L327'>myBoolean</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Boolean</td><td><a title='Memory Map' href='microkit.system#L110-L114'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L9-L9'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L52-L55'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L32-L42'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L15-L22'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L202-L223'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L331-L331'>myCharacter</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='Memory Map' href='microkit.system#L115-L119'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L11-L11'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L65-L68'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L44-L54'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L25-L32'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L224-L245'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L332-L332'>myString</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='Memory Map' href='microkit.system#L120-L124'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L13-L13'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L78-L81'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L16-L16'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L56-L66'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L35-L42'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L246-L267'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L336-L336'>myInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L125-L129'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L15-L15'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L91-L94'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L17-L17'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L68-L78'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L45-L52'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L268-L289'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L337-L337'>myInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='Memory Map' href='microkit.system#L130-L134'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L17-L17'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L104-L107'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L18-L18'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L80-L90'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L55-L62'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L290-L311'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L338-L338'>myInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L135-L139'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L19-L19'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L117-L120'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L19-L19'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L92-L102'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L65-L72'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L312-L333'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L339-L339'>myInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='Memory Map' href='microkit.system#L140-L144'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L21-L21'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L130-L133'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L20-L20'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L104-L114'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L75-L82'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L334-L355'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L343-L343'>myUInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='Memory Map' href='microkit.system#L145-L149'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L23-L23'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L143-L146'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L21-L21'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L116-L126'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L85-L92'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L356-L377'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L344-L344'>myUInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='Memory Map' href='microkit.system#L150-L154'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L25-L25'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L156-L159'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L22-L22'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L128-L138'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L95-L102'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L378-L399'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L345-L345'>myUInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='Memory Map' href='microkit.system#L155-L159'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L27-L27'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L169-L172'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L23-L23'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L140-L150'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L105-L112'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L400-L421'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L346-L346'>myUInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='Memory Map' href='microkit.system#L160-L164'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L29-L29'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L182-L185'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L24-L24'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L152-L162'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L115-L122'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L422-L443'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L350-L350'>myFloat32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='Memory Map' href='microkit.system#L165-L169'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L31-L31'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L195-L198'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L25-L25'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L164-L174'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L125-L132'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L444-L465'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L351-L351'>myFloat64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='Memory Map' href='microkit.system#L170-L174'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L33-L33'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L208-L211'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L26-L26'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L176-L186'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L135-L142'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L466-L487'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L355-L355'>myEnum</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='Memory Map' href='microkit.system#L175-L179'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L35-L35'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L221-L224'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L27-L27'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L188-L198'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L145-L152'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L488-L509'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L356-L356'>myStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='Memory Map' href='microkit.system#L180-L184'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L37-L37'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L234-L237'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L28-L28'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L200-L210'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L155-L162'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L510-L531'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L357-L357'>myArray1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='Memory Map' href='microkit.system#L185-L189'>Memory Map</a> → <a title='C Shared Memory Variable' href='components/consumer_consumer/src/consumer_consumer.c#L39-L39'>C var_addr</a> → <a title='C Interface' href='components/consumer_consumer/src/consumer_consumer.c#L247-L250'>C Interface</a> → <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L29-L29'>C Extern</a> → <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L212-L222'>Rust/C Interface</a> → <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L165-L172'>Unverified Rust Interface</a> → <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L532-L553'>Rust/Verus API</a></td></tr>
    </table>

