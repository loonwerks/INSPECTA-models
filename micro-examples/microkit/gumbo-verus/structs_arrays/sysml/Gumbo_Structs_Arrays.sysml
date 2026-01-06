//@ HAMR: --platform Microkit --output-dir ../hamr/microkit_X
package Gubmo_Structs_Arrays {
    
    private import HAMR::*;

    enum def MyEnum {
        enum On;
        enum Off;
    }
    
    part def MyStructArray_i :> Data {
        part fieldInt64: Base_Types::Integer_64;
        attribute fieldEnum: MyEnum;
        part fieldRec: MyStruct2_i;
        part fieldArray: MyArrayStruct;
    }

    part def MyStruct2_i :> Data {
        part fieldInt32: Base_Types::Integer_32;
    }

    part def MyArrayStruct :> Array {
        part :>> Base_Type : MyStruct2_i;

        attribute :>> Dimensions = 10;
        
        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 40[byte];
        
        //attribute :>> value : Collections::Array { 
        //    :>> dimensions = 10; 
        //    :>> elements : MyStruct2_i
        //}
    }

    part def MyArrayInt32 :> Array {
        part :>> Base_Type : Base_Types::Integer_32;

        attribute :>> Dimensions = 10;
        
        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 40[byte];

        //attribute :>> value : Collections::Array { 
        //    :>> dimensions = 10; 
        //    :>> elements : Base_Types::Integer_32;
        //}
    }

    part def Sys_i :> System {
        part producer: ProducerProc_i;
        part consumer: ConsumerProc_i;

        part proc: Proc;

        connection myStructConn: PortConnection
            connect producer.myStructArray to consumer.myStructArray;

        connection MyArrayStruct1Conn: PortConnection
            connect producer.MyArrayStruct to consumer.MyArrayStruct;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate producer to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate consumer to proc;
    }

    part def Proc :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 1[ms];
    }

    part def ProducerProc_i :> Process {
        out port myStructArray: EventDataPort { :>> type : MyStructArray_i; }
        out port MyArrayStruct: EventDataPort { :>> type : MyArrayStruct; }

        part producer: ProducerThr_i;

        connection myStructConn: PortConnection
            connect producer.myStructArray to myStructArray;

        connection MyArrayStruct1Conn: PortConnection
            connect producer.MyArrayStruct to MyArrayStruct;

        attribute :>> Domain = 2;
    }
    
    part def ConsumerProc_i :> Process {
        in port myStructArray: EventDataPort { :>> type : MyStructArray_i; }
        in port MyArrayStruct: EventDataPort { :>> type : MyArrayStruct; }

        part consumer: ConsumerThr_i;

        connection myStructConn: PortConnection 
            connect myStructArray to consumer.myStructArray;

        connection MyArrayStruct1Conn: PortConnection
            connect MyArrayStruct to consumer.MyArrayStruct;
        
        attribute :>> Domain = 3;
    }

    part def ProducerThr_i :> Periodic_Rust_Thread {
        out port myStructArray: EventDataPort { :>> type : MyStructArray_i; }
        out port MyArrayStruct: EventDataPort { :>> type : MyArrayStruct; }
    }

    part def ConsumerThr_i :> Periodic_Rust_Thread {
        in port myStructArray: EventDataPort { :>> type : MyStructArray_i; }
        in port MyArrayStruct: EventDataPort { :>> type : MyArrayStruct; }
        in port MyArrayInt32: EventDataPort { :>> type : MyArrayInt32; }

        language "GUMBO" /*{
            compute
                assume s:
                    MyArrayInt32[0] >= MyArrayInt32[2];
                //assume s2:
                //    MyArrayInt32.size() > 0;
                //assume s3:
                //   forall i in 0..MyArrayInt32.size() - 1 | a[i] <= a[i + 1]);
                //assume s4:
                //    forall i in 0..MyArrayInt32# - 1 | a[i] <= a[i + 1]);
        }*/
    }

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
        attribute :>> Implementation_Language = Implementation_Languages::Rust;
    }
}