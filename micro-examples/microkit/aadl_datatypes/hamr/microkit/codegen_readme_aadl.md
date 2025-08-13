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
        <td>Base_Types::Boolean</td><td><a title='Memory Map' href='microkit.system#L15-L19'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L287-L287'>myCharacter</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='Memory Map' href='microkit.system#L20-L24'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L288-L288'>myString</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='Memory Map' href='microkit.system#L25-L29'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L292-L292'>myInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L30-L34'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L293-L293'>myInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='Memory Map' href='microkit.system#L35-L39'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L294-L294'>myInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L40-L44'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L295-L295'>myInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='Memory Map' href='microkit.system#L45-L49'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L299-L299'>myUInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='Memory Map' href='microkit.system#L50-L54'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L300-L300'>myUInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='Memory Map' href='microkit.system#L55-L59'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L301-L301'>myUInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='Memory Map' href='microkit.system#L60-L64'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L302-L302'>myUInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='Memory Map' href='microkit.system#L65-L69'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L306-L306'>myFloat32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='Memory Map' href='microkit.system#L70-L74'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L307-L307'>myFloat64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='Memory Map' href='microkit.system#L75-L79'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L311-L311'>myEnum</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='Memory Map' href='microkit.system#L80-L84'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L312-L312'>myStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='Memory Map' href='microkit.system#L85-L89'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L313-L313'>myArray1</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='Memory Map' href='microkit.system#L90-L94'>Memory Map</a></td></tr>
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
        <td>Base_Types::Boolean</td><td><a title='Memory Map' href='microkit.system#L102-L106'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L32-L42'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L15-L22'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L202-L223'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L331-L331'>myCharacter</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a title='Memory Map' href='microkit.system#L107-L111'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L44-L54'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L25-L32'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L224-L245'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L332-L332'>myString</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a title='Memory Map' href='microkit.system#L112-L116'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L16-L16'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L56-L66'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L35-L42'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L246-L267'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L336-L336'>myInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a title='Memory Map' href='microkit.system#L117-L121'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L17-L17'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L68-L78'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L45-L52'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L268-L289'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L337-L337'>myInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a title='Memory Map' href='microkit.system#L122-L126'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L18-L18'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L80-L90'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L55-L62'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L290-L311'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L338-L338'>myInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a title='Memory Map' href='microkit.system#L127-L131'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L19-L19'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L92-L102'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L65-L72'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L312-L333'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L339-L339'>myInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a title='Memory Map' href='microkit.system#L132-L136'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L20-L20'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L104-L114'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L75-L82'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L334-L355'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L343-L343'>myUInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a title='Memory Map' href='microkit.system#L137-L141'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L21-L21'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L116-L126'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L85-L92'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L356-L377'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L344-L344'>myUInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a title='Memory Map' href='microkit.system#L142-L146'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L22-L22'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L128-L138'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L95-L102'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L378-L399'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L345-L345'>myUInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a title='Memory Map' href='microkit.system#L147-L151'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L23-L23'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L140-L150'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L105-L112'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L400-L421'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L346-L346'>myUInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a title='Memory Map' href='microkit.system#L152-L156'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L24-L24'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L152-L162'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L115-L122'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L422-L443'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L350-L350'>myFloat32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a title='Memory Map' href='microkit.system#L157-L161'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L25-L25'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L164-L174'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L125-L132'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L444-L465'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L351-L351'>myFloat64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a title='Memory Map' href='microkit.system#L162-L166'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L26-L26'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L176-L186'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L135-L142'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L466-L487'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L355-L355'>myEnum</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a title='Memory Map' href='microkit.system#L167-L171'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L27-L27'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L188-L198'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L145-L152'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L488-L509'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L356-L356'>myStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a title='Memory Map' href='microkit.system#L172-L176'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L28-L28'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L200-L210'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L155-L162'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L510-L531'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/Aadl_Datatypes_System.aadl#L357-L357'>myArray1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a title='Memory Map' href='microkit.system#L177-L181'>Memory Map</a> -> <a title='C Extern' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L29-L29'>C Extern</a> -> <a title='Rust/C Interface' href='crates/consumer_consumer/src/bridge/extern_c_api.rs#L212-L222'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L165-L172'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L532-L553'>Rust/Verus API</a></td></tr>
    </table>

