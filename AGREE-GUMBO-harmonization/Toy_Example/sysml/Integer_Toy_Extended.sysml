package Integer_Toy_Extended {

    private import HAMR::*;

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
        attribute :>> Period = 1000 [ms];
        attribute :>> Implementation_Language = Rust;
    }

    part def Toy_Processor :> Processor {
        attribute :>> Frame_Period = 1000 [ms];
        attribute :>> Clock_Period = 1 [ms];
    }

    part def A :> Periodic_Rust_Thread {
        port Input: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            compute
                assume A_Input_Range "A input range":
                    Input < 20 [i32];

                guarantee A_Output_Range "A output range":
                    Output < 2 [i32] * Input;
        }*/
    }

    part def B :> Periodic_Rust_Thread {
        port Input: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            compute
                assume B_Input_Range "B input range":
                    Input < 20 [i32];

                guarantee B_Output_Range "B output range":
                    Output < Input + 15 [i32];
        }*/
    }

    part def C :> Periodic_Rust_Thread {
        port Input1: DataPort { in :>> type : Base_Types::Integer_32; }
        port Input2: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            compute
                guarantee C_Output_Range "C output range":
                    Output == Input1 + Input2;
        }*/
    }

    part def A_Process :> Process {
        port Input: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }

        part a: A;

        connection input_to_a: PortConnection connect Input to a.Input;
        connection a_to_output: PortConnection connect a.Output to Output;

    }

    part def B_Process :> Process {
        port Input: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }

        part b: B;

        connection input_to_b: PortConnection connect Input to b.Input;
        connection b_to_output: PortConnection connect b.Output to Output;

    }

    part def C_Process :> Process {
        port Input1: DataPort { in :>> type : Base_Types::Integer_32; }
        port Input2: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }

        part c: C;

        connection input1_to_c: PortConnection connect Input1 to c.Input1;
        connection input2_to_c: PortConnection connect Input2 to c.Input2;
        connection c_to_output: PortConnection connect c.Output to Output;

    }

    part def top_level :> System {
        port Input: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }
    }

    part def 'top_level.Impl' :> top_level {
        part A_sub: A_Process {
            attribute :>> Domain = 2;
        }
        part B_sub: B_Process {
            attribute :>> Domain = 3;
        }
        part C_sub: C_Process {
            attribute :>> Domain = 4;
        }

        connection IN_TO_A: PortConnection connect Input to A_sub.Input;
        connection A_TO_B: PortConnection connect A_sub.Output to B_sub.Input;
        connection A_TO_C: PortConnection connect A_sub.Output to C_sub.Input1;
        connection B_TO_C: PortConnection connect B_sub.Output to C_sub.Input2;
        connection C_TO_Output: PortConnection connect C_sub.Output to Output;

        language "GUMBO" /*{
            composition top_level_contracts {
                components
                    a = A_sub.a;
                    b = B_sub.b;
                    c = C_sub.c;

                ports
                    system_input = a.Input;
                    a_output = a.Output;
                    b_input = b.Input;
                    b_output = b.Output;
                    c_input1 = c.Input1;
                    c_input2 = c.Input2;
                    system_output = c.Output;

                schema {
                    a;
                    b;
                    c
                }

                abstract property System_Input_Range "System input range" {
                    at START "environment assumption: the system input is less than 10":
                        system_input < 10 [i32];
                }

                property System_Output_Range :> System_Input_Range
                    "System output range: Output < 50." {

                    after a "A establishes its output bound and B's input assumption":
                        (system_input < 10 [i32]) &
                        (a_output < 2 [i32] * system_input) &
                        (b_input < 20 [i32]);

                    after b "B establishes its output bound for C":
                        (system_input < 10 [i32]) &
                        (c_input1 < 2 [i32] * system_input) &
                        (c_input2 < c_input1 + 15 [i32]);

                    after c "the top-level result is available":
                        system_output < 50 [i32];
                }
            }
        }*/
    }

    part def above_top :> System {
        port Input: DataPort { in :>> type : Base_Types::Integer_32; }
        port Output: DataPort { out :>> type : Base_Types::Integer_32; }
    }

    part def 'above_top.Impl' :> above_top {
        part cpu: Toy_Processor;
        part A_sub: A_Process {
            attribute :>> Domain = 5;
        }
        part top_sub: 'top_level.Impl';

        connection IN_TO_TOP: PortConnection connect Input to top_sub.Input;
        connection TOP_TO_A: PortConnection connect top_sub.Output to A_sub.Input;
        connection A_TO_OUT: PortConnection connect A_sub.Output to Output;

        allocation top_binding: Deployment_Properties::Actual_Processor_Binding
            allocate top_sub to cpu;
        allocation outer_A_binding: Deployment_Properties::Actual_Processor_Binding
            allocate A_sub to cpu;

        attribute :>> Scheduling = MCS;

        language "GUMBO" /*{
            composition above_top_contracts {
                components
                    top_a = top_sub.A_sub.a;
                    top_b = top_sub.B_sub.b;
                    top_c = top_sub.C_sub.c;
                    outer_a = A_sub.a;

                ports
                    system_input = top_a.Input;
                    top_a_output = top_a.Output;
                    top_b_input = top_b.Input;
                    top_b_output = top_b.Output;
                    top_c_input1 = top_c.Input1;
                    top_c_input2 = top_c.Input2;
                    top_output = top_c.Output;
                    outer_input = outer_a.Input;
                    system_output = outer_a.Output;

                schema {
                    top_a;
                    top_b;
                    top_c;
                    outer_a
                }

                abstract property System_Input_Range "System input range" {
                    at START "environment assumption: the system input is less than 10":
                        system_input < 10 [i32];
                }

                property System_Output_Range :> System_Input_Range
                    "System output range: Output < 55." {

                    after top_a "the nested A establishes its output bound and B's input assumption":
                        (system_input < 10 [i32]) &
                        (top_a_output < 2 [i32] * system_input) &
                        (top_b_input < 20 [i32]);

                    after top_b "the nested B establishes its output bound for C":
                        (system_input < 10 [i32]) &
                        (top_c_input1 < 2 [i32] * system_input) &
                        (top_c_input2 < top_c_input1 + 15 [i32]);

                    after top_c "the nested top-level result is available":
                        top_output < 50 [i32];

                    after outer_a "the above-top result is available":
                        system_output < 55 [i32];
                }
            }
        }*/
    }
}
