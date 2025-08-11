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
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L283-L283'>myBoolean</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Boolean</td><td><a href='microkit.system#L15-L19'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L287-L287'>myCharacter</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a href='microkit.system#L20-L24'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L288-L288'>myString</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a href='microkit.system#L25-L29'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L292-L292'>myInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a href='microkit.system#L30-L34'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L293-L293'>myInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a href='microkit.system#L35-L39'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L294-L294'>myInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a href='microkit.system#L40-L44'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L295-L295'>myInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a href='microkit.system#L45-L49'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L299-L299'>myUInt8</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a href='microkit.system#L50-L54'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L300-L300'>myUInt16</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a href='microkit.system#L55-L59'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L301-L301'>myUInt32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a href='microkit.system#L60-L64'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L302-L302'>myUInt64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a href='microkit.system#L65-L69'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L306-L306'>myFloat32</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a href='microkit.system#L70-L74'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L307-L307'>myFloat64</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a href='microkit.system#L75-L79'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L311-L311'>myEnum</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a href='microkit.system#L80-L84'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L312-L312'>myStruct</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a href='microkit.system#L85-L89'>Memory Map</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L313-L313'>myArray1</a></td>
        <td>Out</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a href='microkit.system#L90-L94'>Memory Map</a></td></tr>
    </table>


#### consumer: Aadl_Datatypes_System::ConsumerThr.i

 - **Entry Points**


    Initialize: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L26-L34)

    TimeTriggered: [Rust](crates/consumer_consumer/src/component/consumer_consumer_app.rs#L36-L51)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>myBoolean</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L327-L327>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L202-L223>Rust API</a></td>
     <tr><td>myCharacter</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L331-L331>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L224-L245>Rust API</a></td>
     <tr><td>myString</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L332-L332>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L246-L267>Rust API</a></td>
     <tr><td>myInt8</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L336-L336>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L268-L289>Rust API</a></td>
     <tr><td>myInt16</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L337-L337>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L290-L311>Rust API</a></td>
     <tr><td>myInt32</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L338-L338>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L312-L333>Rust API</a></td>
     <tr><td>myInt64</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L339-L339>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L334-L355>Rust API</a></td>
     <tr><td>myUInt8</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L343-L343>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L356-L377>Rust API</a></td>
     <tr><td>myUInt16</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L344-L344>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L378-L399>Rust API</a></td>
     <tr><td>myUInt32</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L345-L345>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L400-L421>Rust API</a></td>
     <tr><td>myUInt64</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L346-L346>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L422-L443>Rust API</a></td>
     <tr><td>myFloat32</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L350-L350>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L444-L465>Rust API</a></td>
     <tr><td>myFloat64</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L351-L351>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L466-L487>Rust API</a></td>
     <tr><td>myEnum</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L355-L355>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L488-L509>Rust API</a></td>
     <tr><td>myStruct</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L356-L356>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L510-L531>Rust API</a></td>
     <tr><td>myArray1</td>
     <td><a href=../../aadl/Aadl_Datatypes_System.aadl#L357-L357>Model</a></td>
     <td><a href=crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L532-L553>Rust API</a></td></table>


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L327-L327'>myBoolean</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Boolean</td><td><a href='microkit.system#L102-L106'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L202-L223'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L331-L331'>myCharacter</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Character</td><td><a href='microkit.system#L107-L111'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L224-L245'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L332-L332'>myString</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::String</td><td><a href='microkit.system#L112-L116'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L246-L267'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L336-L336'>myInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_8</td><td><a href='microkit.system#L117-L121'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L268-L289'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L337-L337'>myInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_16</td><td><a href='microkit.system#L122-L126'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L290-L311'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L338-L338'>myInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_32</td><td><a href='microkit.system#L127-L131'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L312-L333'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L339-L339'>myInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Integer_64</td><td><a href='microkit.system#L132-L136'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L334-L355'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L343-L343'>myUInt8</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_8</td><td><a href='microkit.system#L137-L141'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L356-L377'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L344-L344'>myUInt16</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_16</td><td><a href='microkit.system#L142-L146'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L378-L399'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L345-L345'>myUInt32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_32</td><td><a href='microkit.system#L147-L151'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L400-L421'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L346-L346'>myUInt64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Unsigned_64</td><td><a href='microkit.system#L152-L156'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L422-L443'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L350-L350'>myFloat32</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_32</td><td><a href='microkit.system#L157-L161'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L444-L465'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L351-L351'>myFloat64</a></td>
        <td>In</td><td>Event Data</td>
        <td>Base_Types::Float_64</td><td><a href='microkit.system#L162-L166'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L466-L487'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L355-L355'>myEnum</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyEnum</td><td><a href='microkit.system#L167-L171'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L488-L509'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L356-L356'>myStruct</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyStruct.i</td><td><a href='microkit.system#L172-L176'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L510-L531'>Rust API</a></td></tr>
    <tr><td><a href='../../aadl/Aadl_Datatypes_System.aadl#L357-L357'>myArray1</a></td>
        <td>In</td><td>Event Data</td>
        <td>Aadl_Datatypes::MyArrayOneDim</td><td><a href='microkit.system#L177-L181'>Memory Map</a> -> <a href='crates/consumer_consumer/src/bridge/consumer_consumer_api.rs#L532-L553'>Rust API</a></td></tr>
    </table>

