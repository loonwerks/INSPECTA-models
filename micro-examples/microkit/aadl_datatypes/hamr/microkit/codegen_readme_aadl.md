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

[Microkit System Description](microkit.system)

### Behavior Code
#### producer: Aadl_Datatypes_System::ProducerThr.i

 - **Entry Points**



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


