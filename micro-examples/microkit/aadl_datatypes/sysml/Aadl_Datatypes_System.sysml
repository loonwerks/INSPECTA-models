//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_mcs
package Aadl_Datatypes_System {

    private import HAMR::*;
    private import Base_Types::*;
    private import Aadl_Datatypes::*;

    ///////////////////////////////////////////////////////////////////////
    //
    //   T h r e a d s
    //
    ///////////////////////////////////////////////////////////////////////

    part def ProducerThr_i :> Periodic_Thread {
        // Base types
        out port myBoolean   : EventDataPort { out :>> type : Boolean; }
        out port myCharacter : EventDataPort { out :>> type : Character; }
        out port myString    : EventDataPort { out :>> type : String; }
        // Signed integers
        out port myInt8      : EventDataPort { out :>> type : Integer_8; }
        out port myInt16     : EventDataPort { out :>> type : Integer_16; }
        out port myInt32     : EventDataPort { out :>> type : Integer_32; }
        out port myInt64     : EventDataPort { out :>> type : Integer_64; }
        // Unsigned integers
        out port myUInt8     : EventDataPort { out :>> type : Unsigned_8; }
        out port myUInt16    : EventDataPort { out :>> type : Unsigned_16; }
        out port myUInt32    : EventDataPort { out :>> type : Unsigned_32; }
        out port myUInt64    : EventDataPort { out :>> type : Unsigned_64; }
        // Floating point
        out port myFloat32   : EventDataPort { out :>> type : Float_32; }
        out port myFloat64   : EventDataPort { out :>> type : Float_64; }
        // Structured types
        out port myEnum      : EventDataPort { out :>> type : MyEnum; }
        out port myStruct    : EventDataPort { out :>> type : MyStruct_i; }
        out port myArray1    : EventDataPort { out :>> type : MyArrayOneDim; }
    }

    part def ConsumerThr_i :> Periodic_Thread {
        // Base types
        in port myBoolean   : EventDataPort { in :>> type : Boolean; }
        in port myCharacter : EventDataPort { in :>> type : Character; }
        in port myString    : EventDataPort { in :>> type : String; }
        // Signed integers
        in port myInt8      : EventDataPort { in :>> type : Integer_8; }
        in port myInt16     : EventDataPort { in :>> type : Integer_16; }
        in port myInt32     : EventDataPort { in :>> type : Integer_32; }
        in port myInt64     : EventDataPort { in :>> type : Integer_64; }
        // Unsigned integers
        in port myUInt8     : EventDataPort { in :>> type : Unsigned_8; }
        in port myUInt16    : EventDataPort { in :>> type : Unsigned_16; }
        in port myUInt32    : EventDataPort { in :>> type : Unsigned_32; }
        in port myUInt64    : EventDataPort { in :>> type : Unsigned_64; }
        // Floating point
        in port myFloat32   : EventDataPort { in :>> type : Float_32; }
        in port myFloat64   : EventDataPort { in :>> type : Float_64; }
        // Structured types
        in port myEnum      : EventDataPort { in :>> type : MyEnum; }
        in port myStruct    : EventDataPort { in :>> type : MyStruct_i; }
        in port myArray1    : EventDataPort { in :>> type : MyArrayOneDim; }

        attribute :>> Implementation_Language = Rust;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   P r o c e s s e s
    //
    ///////////////////////////////////////////////////////////////////////

    part def ProducerProc_i :> Process {
        // Base types
        out port myBoolean   : EventDataPort { out :>> type : Boolean; }
        out port myCharacter : EventDataPort { out :>> type : Character; }
        out port myString    : EventDataPort { out :>> type : String; }
        // Signed integers
        out port myInt8      : EventDataPort { out :>> type : Integer_8; }
        out port myInt16     : EventDataPort { out :>> type : Integer_16; }
        out port myInt32     : EventDataPort { out :>> type : Integer_32; }
        out port myInt64     : EventDataPort { out :>> type : Integer_64; }
        // Unsigned integers
        out port myUInt8     : EventDataPort { out :>> type : Unsigned_8; }
        out port myUInt16    : EventDataPort { out :>> type : Unsigned_16; }
        out port myUInt32    : EventDataPort { out :>> type : Unsigned_32; }
        out port myUInt64    : EventDataPort { out :>> type : Unsigned_64; }
        // Floating point
        out port myFloat32   : EventDataPort { out :>> type : Float_32; }
        out port myFloat64   : EventDataPort { out :>> type : Float_64; }
        // Structured types
        out port myEnum      : EventDataPort { out :>> type : MyEnum; }
        out port myStruct    : EventDataPort { out :>> type : MyStruct_i; }
        out port myArray1    : EventDataPort { out :>> type : MyArrayOneDim; }

        part producer: ProducerThr_i;

        connection myBooleanConn   : PortConnection connect producer.myBoolean   to myBoolean;
        connection myCharacterConn : PortConnection connect producer.myCharacter to myCharacter;
        connection myStringConn    : PortConnection connect producer.myString    to myString;
        connection myInt8Conn      : PortConnection connect producer.myInt8      to myInt8;
        connection myInt16Conn     : PortConnection connect producer.myInt16     to myInt16;
        connection myInt32Conn     : PortConnection connect producer.myInt32     to myInt32;
        connection myInt64Conn     : PortConnection connect producer.myInt64     to myInt64;
        connection myUInt8Conn     : PortConnection connect producer.myUInt8     to myUInt8;
        connection myUInt16Conn    : PortConnection connect producer.myUInt16    to myUInt16;
        connection myUInt32Conn    : PortConnection connect producer.myUInt32    to myUInt32;
        connection myUInt64Conn    : PortConnection connect producer.myUInt64    to myUInt64;
        connection myFloat32Conn   : PortConnection connect producer.myFloat32   to myFloat32;
        connection myFloat64Conn   : PortConnection connect producer.myFloat64   to myFloat64;
        connection myEnumConn      : PortConnection connect producer.myEnum      to myEnum;
        connection myStructConn    : PortConnection connect producer.myStruct    to myStruct;
        connection myArray1Conn    : PortConnection connect producer.myArray1    to myArray1;

        attribute :>> Domain = 2;
    }

    part def ConsumerProc_i :> Process {
        // Base types
        in port myBoolean   : EventDataPort { in :>> type : Boolean; }
        in port myCharacter : EventDataPort { in :>> type : Character; }
        in port myString    : EventDataPort { in :>> type : String; }
        // Signed integers
        in port myInt8      : EventDataPort { in :>> type : Integer_8; }
        in port myInt16     : EventDataPort { in :>> type : Integer_16; }
        in port myInt32     : EventDataPort { in :>> type : Integer_32; }
        in port myInt64     : EventDataPort { in :>> type : Integer_64; }
        // Unsigned integers
        in port myUInt8     : EventDataPort { in :>> type : Unsigned_8; }
        in port myUInt16    : EventDataPort { in :>> type : Unsigned_16; }
        in port myUInt32    : EventDataPort { in :>> type : Unsigned_32; }
        in port myUInt64    : EventDataPort { in :>> type : Unsigned_64; }
        // Floating point
        in port myFloat32   : EventDataPort { in :>> type : Float_32; }
        in port myFloat64   : EventDataPort { in :>> type : Float_64; }
        // Structured types
        in port myEnum      : EventDataPort { in :>> type : MyEnum; }
        in port myStruct    : EventDataPort { in :>> type : MyStruct_i; }
        in port myArray1    : EventDataPort { in :>> type : MyArrayOneDim; }

        part consumer: ConsumerThr_i;

        connection myBooleanConn   : PortConnection connect myBoolean   to consumer.myBoolean;
        connection myCharacterConn : PortConnection connect myCharacter to consumer.myCharacter;
        connection myStringConn    : PortConnection connect myString    to consumer.myString;
        connection myInt8Conn      : PortConnection connect myInt8      to consumer.myInt8;
        connection myInt16Conn     : PortConnection connect myInt16     to consumer.myInt16;
        connection myInt32Conn     : PortConnection connect myInt32     to consumer.myInt32;
        connection myInt64Conn     : PortConnection connect myInt64     to consumer.myInt64;
        connection myUInt8Conn     : PortConnection connect myUInt8     to consumer.myUInt8;
        connection myUInt16Conn    : PortConnection connect myUInt16    to consumer.myUInt16;
        connection myUInt32Conn    : PortConnection connect myUInt32    to consumer.myUInt32;
        connection myUInt64Conn    : PortConnection connect myUInt64    to consumer.myUInt64;
        connection myFloat32Conn   : PortConnection connect myFloat32   to consumer.myFloat32;
        connection myFloat64Conn   : PortConnection connect myFloat64   to consumer.myFloat64;
        connection myEnumConn      : PortConnection connect myEnum      to consumer.myEnum;
        connection myStructConn    : PortConnection connect myStruct    to consumer.myStruct;
        connection myArray1Conn    : PortConnection connect myArray1    to consumer.myArray1;

        attribute :>> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   S y s t e m
    //
    ///////////////////////////////////////////////////////////////////////

    part def Proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 1[ms];
    }

    part def Sys_i :> System {
        part proc     : Proc_impl;
        part producer : ProducerProc_i;
        part consumer : ConsumerProc_i;

        connection myBooleanConn   : PortConnection connect producer.myBoolean   to consumer.myBoolean;
        connection myCharacterConn : PortConnection connect producer.myCharacter to consumer.myCharacter;
        connection myStringConn    : PortConnection connect producer.myString    to consumer.myString;
        connection myInt8Conn      : PortConnection connect producer.myInt8      to consumer.myInt8;
        connection myInt16Conn     : PortConnection connect producer.myInt16     to consumer.myInt16;
        connection myInt32Conn     : PortConnection connect producer.myInt32     to consumer.myInt32;
        connection myInt64Conn     : PortConnection connect producer.myInt64     to consumer.myInt64;
        connection myUInt8Conn     : PortConnection connect producer.myUInt8     to consumer.myUInt8;
        connection myUInt16Conn    : PortConnection connect producer.myUInt16    to consumer.myUInt16;
        connection myUInt32Conn    : PortConnection connect producer.myUInt32    to consumer.myUInt32;
        connection myUInt64Conn    : PortConnection connect producer.myUInt64    to consumer.myUInt64;
        connection myFloat32Conn   : PortConnection connect producer.myFloat32   to consumer.myFloat32;
        connection myFloat64Conn   : PortConnection connect producer.myFloat64   to consumer.myFloat64;
        connection myEnumConn      : PortConnection connect producer.myEnum      to consumer.myEnum;
        connection myStructConn    : PortConnection connect producer.myStruct    to consumer.myStruct;
        connection myArray1Conn    : PortConnection connect producer.myArray1    to consumer.myArray1;

        allocation pb0 : Deployment_Properties::Actual_Processor_Binding
            allocate producer to proc;

        allocation pb1 : Deployment_Properties::Actual_Processor_Binding
            allocate consumer to proc;

        attribute :>> Scheduling = MCS;
    }

    part def Periodic_Thread :> Thread {
        attribute :>> Period = 1000[ms];
        attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
    }
}
