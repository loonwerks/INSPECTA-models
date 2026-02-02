//@ HAMR: --platform JVM --slang-output-dir ../hamr/slang_sysml
//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_sysml
package Gubmo_Structs_Arrays {
    
    private import HAMR::*;
    private import SequenceFunctions::*;
    private import ControlFunctions::*;
    private import ScalarValues::*;

    enum def MyEnum {
        enum On;
        enum Off;
    }
    

    part def MyArrayInt32 :> Array {
        part :>> Base_Type : Base_Types::Integer_32;

        attribute :>> Dimensions = 10;
        
        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 40[byte];
    }


    part def MyStruct2_i :> Struct {
        part fieldSInt32: Base_Types::Integer_32;
    }

    part def MyArrayStruct :> Array {
        part :>> Base_Type : MyStruct2_i;

        attribute :>> Dimensions = 10;
        
        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 40[byte];
    }
    

    part def MyStructArray_i :> Struct {
        part fieldInt64: Base_Types::Integer_64;
        attribute fieldEnum: MyEnum;
        part fieldRec: MyStruct2_i;
        part fieldArray: MyArrayStruct;
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
            connect myStructArray to consumer.myStructArray_EventDataPort;

        connection MyArrayStruct1Conn: PortConnection
            connect MyArrayStruct to consumer.myArrayStruct_EventDataPort;
        
        attribute :>> Domain = 3;
    }

    part def ProducerThr_i :> Periodic_Rust_Thread {
        out port myStructArray: EventDataPort { :>> type : MyStructArray_i; }
        out port MyArrayStruct: EventDataPort { :>> type : MyArrayStruct; }
    }

    part def ConsumerThr_i :> Periodic_Rust_Thread {
        
        in port myArrayInt32_DataPort: DataPort { :>> type : MyArrayInt32; }
        in port myArrayStruct_DataPort: DataPort { :>> type : MyArrayStruct; }
        in port myStructArray_DataPort: DataPort { :>> type : MyStructArray_i; }
        

        in port myArrayInt32_EventDataPort: EventDataPort { :>> type : MyArrayInt32; }
        in port myArrayStruct_EventDataPort: EventDataPort { :>> type : MyArrayStruct; }
        in port myStructArray_EventDataPort: EventDataPort { :>> type : MyStructArray_i; }
        
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

            state
                myArrayInt32_StateVar: MyArrayInt32;
                myArrayStruct_StateVar: MyArrayStruct;
                myStructArray_StateVar: MyStructArray_i;

            functions

                def myArrayInt32_FunctionReturn(v: MyArrayInt32): MyArrayInt32 := v;

                def myArrayInt32_FunctionParam(v: MyArrayInt32): Base_Types::Boolean := 
                    (0 .. size(v) - 1) -> exists {i; v#(i) == 0[i32] };


                def myArrayStruct_FunctionReturn(v: MyArrayStruct): MyArrayStruct := v;

                def myArrayStruct_FunctionParam(v: MyArrayStruct): Base_Types::Boolean := 
                    (0 .. size(v) - 1) -> exists {i; v#(i).fieldSInt32 == 0[i32] };


                def myStructArray_i_FunctionReturn(v: MyStructArray_i): MyStructArray_i := v;

                def myStructArray_i_FunctionParam(v: MyStructArray_i): Base_Types::Boolean := 
                    (0 .. size(v.fieldArray) - 1) -> exists {i; v.fieldArray#(i).fieldSInt32 == 0[i32] };

                // for testing a call to a subclause spec function from an assume context
                @spec def subclauseSpecFunction_Assume(a: MyArrayInt32): Base_Types::Boolean;
                
                // for testing a call to a subclause spec function from a guarantee context
                @spec def subclauseSpecFunction_Guarantee(a: MyArrayInt32): Base_Types::Boolean;

            compute

                ///////////////////////////////////////////////////////////////////////////////////////
                //
                // A S S U M E S
                //
                ///////////////////////////////////////////////////////////////////////////////////////

                ////////////////////////////////////////////////////////////////////////////////////////
                // State Vars Assumes -- state vars will be wrapped in In/old 
                ////////////////////////////////////////////////////////////////////////////////////////
                assume isSorted_MyArrayInt32_StateVar_Assume:

                    (0 .. size(myArrayInt32_StateVar) - 2) -> forAll { in i;
                        myArrayInt32_StateVar#(i) <= myArrayInt32_StateVar#(i + 1) };

                assume isSorted_MyArrayStruct_StateVar_Assume:
                    (0 .. size(myArrayStruct_StateVar) - 2) -> forAll { in i;
                        myArrayStruct_StateVar#(i).fieldSInt32 <= myArrayStruct_StateVar#(i + 1).fieldSInt32 };

                assume isSorted_MyStructArray_StateVar_Assume:
                    (0 .. size(myStructArray_StateVar.fieldArray) - 2) -> forAll { in i;
                        myStructArray_StateVar.fieldArray#(i).fieldSInt32 <= myStructArray_StateVar.fieldArray#(i + 1).fieldSInt32 };


                ////////////////////////////////////////////////////////////////////////////////////////
                // Functions Assumes
                ////////////////////////////////////////////////////////////////////////////////////////

                //////////////////////////////////////////////////
                // Array of integers
                //////////////////////////////////////////////////

                assume isSorted_MyArrayInt32_Function_Assume 
                    "Ensure operations on an array returned by a function work as expected":
                    (0 .. size(myArrayInt32_FunctionReturn(myArrayInt32_StateVar)) - 2) -> forAll { in i;
                        myArrayInt32_FunctionReturn(myArrayInt32_StateVar)#(i) <= 
                            myArrayInt32_FunctionReturn(myArrayInt32_StateVar)#(i + 1) };

                assume myArrayInt32_FunctionParam_Assume "ensure functions can operate on arrays":
                    myArrayInt32_FunctionParam(myArrayInt32_StateVar);

                assume specFunctionAssumeTest:

                    subclauseSpecFunction_Assume(myArrayInt32_DataPort) &

                    GumboLib::GUMBO__Library::normalLibraryFunction(myArrayInt32_DataPort) &

                    GumboLib::GUMBO__Library::librarySpecFunction_Assume(myArrayInt32_DataPort);

                //////////////////////////////////////////////////
                // Array of structs
                //////////////////////////////////////////////////

                assume isSorted_MyArrayStruct_Function_Assume 
                    "Ensure operations on an array returned by a function work as expected":
                    (0 .. size(myArrayStruct_FunctionReturn(myArrayStruct_StateVar)) - 2) -> forAll { in i;
                        myArrayStruct_FunctionReturn(myArrayStruct_StateVar)#(i).fieldSInt32 <= 
                            myArrayStruct_FunctionReturn(myArrayStruct_StateVar)#(i + 1).fieldSInt32 };

                assume myArrayStruct_FunctionParam_Assume "ensure functions can operate on arrays":
                    myArrayStruct_FunctionParam(myArrayStruct_StateVar);

                //////////////////////////////////////////////////
                // Struct with array field
                //////////////////////////////////////////////////

                assume isSorted_MyStructArray_i_Function_Assume 
                    "Ensure operations on an array returned by a function work as expected":
                    (0 .. size(myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray) - 2) -> forAll { in i;
                        myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray#(i).fieldSInt32 <= 
                            myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray#(i + 1).fieldSInt32 };

                assume myStructArray_i_FunctionParam_Assume "ensure functions can operate on arrays":
                    myStructArray_i_FunctionParam(myStructArray_StateVar);


                ////////////////////////////////////////////////////////////////////////////////////////
                // Data Ports Assumes
                ////////////////////////////////////////////////////////////////////////////////////////
                
                //////////////////////////////////////////////////
                // Array of integers
                //////////////////////////////////////////////////

                assume atLeastOneZero_MyArrayInt32_DataPort_Assume:
                   (0 .. size(myArrayInt32_DataPort) - 1) -> exists { in i; myArrayInt32_DataPort#(i) == 0[i32] };
                        
                assume isSorted_MyArrayInt32_DataPort_Assume:
                    (0 .. size(myArrayInt32_DataPort) - 2) -> forAll { in i;
                        myArrayInt32_DataPort#(i) <= myArrayInt32_DataPort#(i + 1) };

                //////////////////////////////////////////////////
                // Array of structs
                //////////////////////////////////////////////////

                assume isSorted_MyArrayStruct_DataPort_Assume:
                    (0 .. size(myArrayStruct_DataPort) - 2) -> forAll { in i;
                        myArrayStruct_DataPort#(i).fieldSInt32 <= myArrayStruct_DataPort#(i + 1).fieldSInt32 };

                //////////////////////////////////////////////////
                // Struct with array field
                //////////////////////////////////////////////////

                assume isSorted_MyStructArray_DataPort_Assume:
                    (0 .. size(myStructArray_DataPort.fieldArray) - 2) -> forAll { in i;
                        myStructArray_DataPort.fieldArray#(i).fieldSInt32 <= myStructArray_DataPort.fieldArray#(i + 1).fieldSInt32 };


                ////////////////////////////////////////////////////////////////////////////////////////
                // Event Data Ports Assumes
                ////////////////////////////////////////////////////////////////////////////////////////   

                //////////////////////////////////////////////////
                // Array of integers
                //////////////////////////////////////////////////

                assume isSorted_MyArrayInt32_EventDataPort_Assume:
                    HasEvent(myArrayInt32_EventDataPort) implies
                        (0 .. size(myArrayInt32_EventDataPort) - 2) -> forAll { in i;
                            myArrayInt32_EventDataPort#(i) <= myArrayInt32_EventDataPort#(i + 1) };

                //////////////////////////////////////////////////
                // Array of structs
                //////////////////////////////////////////////////

                assume isSorted_MyArrayStruct_EventDataPort_Assume:
                    HasEvent(myArrayStruct_EventDataPort) implies
                        (0 .. size(myArrayStruct_EventDataPort) - 2) -> forAll { in i;
                            myArrayStruct_EventDataPort#(i).fieldSInt32 <= myArrayStruct_EventDataPort#(i + 1).fieldSInt32 };

                //////////////////////////////////////////////////
                // Struct with array field
                //////////////////////////////////////////////////

                assume isSorted_MyStructArray_EventDataPort_Assume:
                    HasEvent(myStructArray_EventDataPort) implies
                        (0 .. size(myStructArray_EventDataPort.fieldArray) - 2) -> forAll { in i;
                            myStructArray_EventDataPort.fieldArray#(i).fieldSInt32 <= myStructArray_EventDataPort.fieldArray#(i + 1).fieldSInt32 };




                ///////////////////////////////////////////////////////////////////////////////////////
                //
                // G U A R A N T E E S
                //
                // These are mainly copy/pasted versions of the assumes, except the pre and post state
                // values are compared/used.  The assume clauses constrain the pre-state so these should
                // hold (assuming the implementation code doesn't mutate anything)
                //
                ///////////////////////////////////////////////////////////////////////////////////////

                ////////////////////////////////////////////////////////////////////////////////////////
                // State Vars Guarantees (only state vars can be wrapped in In)
                ////////////////////////////////////////////////////////////////////////////////////////
                guarantee noChange_MyArrayInt32_StateVar_Guarantee:
                    In(myArrayInt32_StateVar)#(0) == myArrayInt32_StateVar#(0);

                guarantee noChange_MyArrayStruct_StateVar_Guarantee:
                    In(myArrayStruct_StateVar)#(0).fieldSInt32 == myArrayStruct_StateVar#(0).fieldSInt32;


                guarantee isSorted_MyArrayInt32_StateVar_Guarantee:
                    (0 .. size(myArrayInt32_StateVar) - 2) -> forAll { in i;
                        In(myArrayInt32_StateVar)#(i) <= myArrayInt32_StateVar#(i + 1) };

                guarantee isSorted_MyArrayStruct_StateVar_Guarantee:
                    (0 .. size(myArrayStruct_StateVar) - 2) -> forAll { in i;
                        In(myArrayStruct_StateVar)#(i).fieldSInt32 <= myArrayStruct_StateVar#(i + 1).fieldSInt32 };

                guarantee isSorted_MyStructArray_StateVar_Guarantee:
                    (0 .. size(myStructArray_StateVar.fieldArray) - 2) -> forAll { in i;
                        In(myStructArray_StateVar).fieldArray#(i).fieldSInt32 <= myStructArray_StateVar.fieldArray#(i + 1).fieldSInt32 };


                ////////////////////////////////////////////////////////////////////////////////////////
                // Functions Guarantees
                ////////////////////////////////////////////////////////////////////////////////////////

                //////////////////////////////////////////////////
                // Array of integers
                //////////////////////////////////////////////////

                guarantee isSorted_MyArrayInt32_Function_Guarantee
                    "Ensure operations on an array returned by a function work as expected":
                    (0 .. size(myArrayInt32_FunctionReturn(myArrayInt32_StateVar)) - 2) -> forAll { in i;
                        myArrayInt32_FunctionReturn(In(myArrayInt32_StateVar))#(i) <= 
                            myArrayInt32_FunctionReturn(myArrayInt32_StateVar)#(i + 1) };

                guarantee myArrayInt32_FunctionParam_Guarantee "ensure functions can operate on arrays":
                    myArrayInt32_FunctionParam(In(myArrayInt32_StateVar));

                guarantee librarySpecFunctionTest:
                    
                    subclauseSpecFunction_Assume(myArrayInt32_DataPort) &

                    subclauseSpecFunction_Guarantee(myArrayInt32_DataPort) &

                    GumboLib::GUMBO__Library::normalLibraryFunction(myArrayInt32_DataPort) &

                    GumboLib::GUMBO__Library::librarySpecFunction_Guarantee(myArrayInt32_DataPort);

                //////////////////////////////////////////////////
                // Array of structs
                //////////////////////////////////////////////////

                guarantee isSorted_MyArrayStruct_Function_Guarantee 
                    "Ensure operations on an array returned by a function work as expected":
                    (0 .. size(myArrayStruct_FunctionReturn(myArrayStruct_StateVar)) - 2) -> forAll { in i;
                        myArrayStruct_FunctionReturn(In(myArrayStruct_StateVar))#(i).fieldSInt32 <= 
                            myArrayStruct_FunctionReturn(myArrayStruct_StateVar)#(i + 1).fieldSInt32 };

                guarantee myArrayStruct_FunctionParam_Guarantee "ensure functions can operate on arrays":
                    myArrayStruct_FunctionParam(In(myArrayStruct_StateVar));

                //////////////////////////////////////////////////
                // Struct with array field
                //////////////////////////////////////////////////

                guarantee isSorted_MyStructArray_i_Function_Guarantee 
                    "Ensure operations on an array returned by a function work as expected":
                    (0 .. size(myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray) - 2) -> forAll { in i;
                        myStructArray_i_FunctionReturn(In(myStructArray_StateVar)).fieldArray#(i).fieldSInt32 <= 
                            myStructArray_i_FunctionReturn(myStructArray_StateVar).fieldArray#(i + 1).fieldSInt32 };

                guarantee myStructArray_i_FunctionParam_Guarantee "ensure functions can operate on arrays":
                    myStructArray_i_FunctionParam(In(myStructArray_StateVar));


                ////////////////////////////////////////////////////////////////////////////////////////
                // Data Ports Guarantees
                ////////////////////////////////////////////////////////////////////////////////////////
                
                //////////////////////////////////////////////////
                // Array of integers
                //////////////////////////////////////////////////

                guarantee atLeastOneZero_MyArrayInt32_DataPort_Guarantee:
                   (0 .. size(myArrayInt32_DataPort) - 1) -> exists { in i; myArrayInt32_DataPort#(i) == 0[i32] };
                        
                guarantee isSorted_MyArrayInt32_DataPort_Guarantee:
                    (0 .. size(myArrayInt32_DataPort) - 2) -> forAll { in i;
                        myArrayInt32_DataPort#(i) <= myArrayInt32_DataPort#(i + 1) };

                //////////////////////////////////////////////////
                // Array of structs
                //////////////////////////////////////////////////

                guarantee isSorted_MyArrayStruct_DataPort_Guarantee:
                    (0 .. size(myArrayStruct_DataPort) - 2) -> forAll { in i;
                        myArrayStruct_DataPort#(i).fieldSInt32 <= myArrayStruct_DataPort#(i + 1).fieldSInt32 };

                //////////////////////////////////////////////////
                // Struct with array field
                //////////////////////////////////////////////////

                guarantee isSorted_MyStructArray_DataPort_Guarantee:
                    (0 .. size(myStructArray_DataPort.fieldArray) - 2) -> forAll { in i;
                        myStructArray_DataPort.fieldArray#(i).fieldSInt32 <= myStructArray_DataPort.fieldArray#(i + 1).fieldSInt32 };


                ////////////////////////////////////////////////////////////////////////////////////////
                // Event Data Ports Guarantees
                ////////////////////////////////////////////////////////////////////////////////////////   

                //////////////////////////////////////////////////
                // Array of integers
                //////////////////////////////////////////////////

                guarantee isSorted_MyArrayInt32_EventDataPort_Guarantee:
                    HasEvent(myArrayInt32_EventDataPort) implies
                        (0 .. size(myArrayInt32_EventDataPort) - 2) -> forAll { in i;
                            myArrayInt32_EventDataPort#(i) <= myArrayInt32_EventDataPort#(i + 1) };

                //////////////////////////////////////////////////
                // Array of structs
                //////////////////////////////////////////////////

                guarantee isSorted_MyArrayStruct_EventDataPort_Guarantee:
                    HasEvent(myArrayStruct_EventDataPort) implies
                        (0 .. size(myArrayStruct_EventDataPort) - 2) -> forAll { in i;
                            myArrayStruct_EventDataPort#(i).fieldSInt32 <= myArrayStruct_EventDataPort#(i + 1).fieldSInt32 };

                //////////////////////////////////////////////////
                // Struct with array field
                //////////////////////////////////////////////////

                guarantee isSorted_MyStructArray_EventDataPort_Guarantee:
                    HasEvent(myStructArray_EventDataPort) implies
                        (0 .. size(myStructArray_EventDataPort.fieldArray) - 2) -> forAll { in i;
                            myStructArray_EventDataPort.fieldArray#(i).fieldSInt32 <= myStructArray_EventDataPort.fieldArray#(i + 1).fieldSInt32 };
        }*/
    }

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
        attribute :>> Implementation_Language = Implementation_Languages::Rust;
    }
}