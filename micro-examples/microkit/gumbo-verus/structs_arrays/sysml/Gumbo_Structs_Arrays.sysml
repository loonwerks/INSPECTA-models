//@ HAMR: --platform Microkit --output-dir ../hamr/microkit_X
package Gubmo_Structs_Arrays {
    
    private import HAMR::*;
    private import SequenceFunctions::*;
    private import ControlFunctions::*;
    private import ScalarValues::*;

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
        part fieldSInt32: Base_Types::Integer_32;
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

        constraint def atLeastOneZero_ArrayInt32 {
            // Example SysMLv2 quantified expressions
            // example 1: https://github.com/Systems-Modeling/SysML-v2-Release/blob/834a3daf073b1886244833d9341677eb9733d1e4/sysml/src/training/33.%20Analysis/Analysis%20Case%20Definition%20Example.sysml#L59-L70
            // example 2: https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/v1%20Spec%20Examples/D.4.7.8%20Dynamics/HSUVDynamics.sysml#L29
            (true implies
                ((0 .. size(MyArrayInt32) - 1) -> exists { in i; MyArrayInt32#(i) == 0 })) ==
            // fully qualified version
            (true implies
                ((0 .. SequenceFunctions::size(MyArrayInt32) - 1) -> ControlFunctions::exists { in i; MyArrayInt32#(i) == 0 }))                 
        }

        language "GUMBO" /*{
            compute
                //assume simpleArrayIndexingExpressions:
                //    HasEventa(MyArrayInt32) implies MyArrayInt32#(0) >= MyArrayInt32#(1 + 1);
                //    //HasEvent(MyArrayInt32) implies MyArrayInt32[0] >= MyArrayInt32[1 + 1];


                //assume arraySizeOperation:
                //    HasEvent(MyArrayInt32) implies size(MyArrayInt32) > 0;

                //assume arraySizeOperation_FullyQualified:
                //    HasEvent(MyArrayInt32) implies SequenceFunctions::size(MyArrayInt32) > 0;
                

                assume atLeastOneZero_ArrayInt32:
                    HasEvent(MyArrayInt32) implies
                        (0 .. size(MyArrayInt32) - 1) -> exists { in i; MyArrayInt32#(i) == 0 };
                        
                assume atLeastOneZero_ArrayInt32_FullyQualified:
                    HasEvent(MyArrayInt32) implies
                        (0 .. SequenceFunctions::size(MyArrayInt32) - 1) -> 
                            ControlFunctions::exists { in i: Natural; MyArrayInt32#(i) == 0 };

                assume isSorted_ArrayInt32:
                    HasEvent(MyArrayInt32) implies
                        (0 .. size(MyArrayInt32) - 2) -> forAll { in i;
                            MyArrayInt32#(i) <= MyArrayInt32#(i + 1) };

                assume atLeastOneZero_ArrayStruct:
                    HasEvent(MyArrayStruct) implies 
                        (0 .. size(MyArrayStruct) - 1) -> exists { i; MyArrayStruct#(i).fieldSInt32 == 0 };


        }*/
    }

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
        attribute :>> Implementation_Language = Implementation_Languages::Rust;
    }
}