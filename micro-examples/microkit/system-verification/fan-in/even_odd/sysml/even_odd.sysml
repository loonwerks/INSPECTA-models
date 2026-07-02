//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_mcs
package even_odd {

    private import HAMR::*;

    ///////////////////////////////////////////////////////////////////////////
    // Thread A: periodic producer with an outgoing event data port

    part def A_Even :> Periodic_Thread {
        port output: EventDataPort { out :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            integration
                guarantee outputIsEven "The value produced on the output port is always even":
                    output % 2[i32] == 0[i32];
        }*/
    }

    part def A_Even_Process :> Process {
        port output: EventDataPort { out :>> type : Base_Types::Integer_32; }

        part a: A_Even;

        connection c1: PortConnection
            connect a.output to output;

        attribute :>> Domain = 2;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Thread B: periodic producer with an outgoing event data port

    part def B_Odd :> Periodic_Thread {
        port output: EventDataPort { out :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            integration
                guarantee outputIsOdd "The value produced on the output port is always odd":
                    output % 2[i32] != 0[i32];
        }*/
    }

    part def B_Odd_Process :> Process {
        port output: EventDataPort { out :>> type : Base_Types::Integer_32; }

        part b: B_Odd;

        connection c1: PortConnection
            connect b.output to output;

        attribute :>> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Thread C: periodic consumer with a single incoming port (fan-in target)

    part def C_Even_or_Odd :> Periodic_Thread {
        port input: EventDataPort { in :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            integration
                assume inputIsEvenOrOdd "The value recevied on the input port is either even or odd":
                    input % 2[i32] == 0[i32] | 
                    input % 2[i32] != 0[i32];
        }*/        
    }

    part def C_Even_or_Odd_Process :> Process {
        port input: EventDataPort { in :>> type : Base_Types::Integer_32; }

        part c: C_Even_or_Odd;

        connection c1: PortConnection
            connect input to c.input;

        attribute :>> Domain = 4;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def C_Odd :> Periodic_Thread {
        port input: EventDataPort { in :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            integration
                assume inputIsOdd "The value recevied on the input port is always odd":
                    input % 2[i32] != 0[i32];
        }*/        
    }

    part def C_Odd_Process :> Process {
        port input: EventDataPort { in :>> type : Base_Types::Integer_32; }

        part c: C_Odd;

        connection c1: PortConnection
            connect input to c.input;

        attribute :>> Domain = 4;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def C_Even :> Periodic_Thread {
        port input: EventDataPort { in :>> type : Base_Types::Integer_32; }

        language "GUMBO" /*{
            integration
                assume inputIsEven "The value recevied on the input port is always even":
                    input % 2[i32] == 0[i32];
        }*/        
    }

    part def C_Even_Process :> Process {
        port input: EventDataPort { in :>> type : Base_Types::Integer_32; }

        part c: C_Even;

        connection c1: PortConnection
            connect input to c.input;

        attribute :>> Domain = 4;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    ///////////////////////////////////////////////////////////////////////////

    part def S1 :> System {
        part proc: proc_impl;
        part a_process: A_Even_Process;
        part b_process: B_Odd_Process;
        part c_process: C_Even_or_Odd_Process;

        connection d1: PortConnection
            connect a_process.output to c_process.input;

        connection d2: PortConnection
            connect b_process.output to c_process.input;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate a_process to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate b_process to proc;

        allocation pb2: Deployment_Properties::Actual_Processor_Binding
            allocate c_process to proc;

        attribute :>> Scheduling = MCS;

        language "GUMBO" /*{
          composition nominal {

            components
              a = a_process.a;
              b = b_process.b;
              c = c_process.c;

            ports
              a_output = a.output;
              b_output = b.output;

            schema {
              split {
                sequence { a },
                sequence { b }
              };
              c
            }

            property After_Join_Parity
              "after the join, a.output is provably even and b.output is provably odd" {

              before c "join exit: a produced an even value and b produced an odd value":
                (a_output % 2[i32] == 0[i32]) & (b_output % 2[i32] != 0[i32]);
            }
          }
        }*/
    }

    ///////////////////////////////////////////////////////////////////////////

    part def S2 :> System {
        part proc: proc_impl;
        part a_process: A_Even_Process;
        part b_process: B_Odd_Process;
        part c_process: C_Odd_Process;

        connection d1: PortConnection
            connect a_process.output to c_process.input;

        connection d2: PortConnection
            connect b_process.output to c_process.input;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate a_process to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate b_process to proc;

        allocation pb2: Deployment_Properties::Actual_Processor_Binding
            allocate c_process to proc;

        attribute :>> Scheduling = MCS;
    }

    ///////////////////////////////////////////////////////////////////////////

        part def S3 :> System {
        part proc: proc_impl;
        part a_process: A_Even_Process;
        part b_process: B_Odd_Process;
        part c_process: C_Even_Process;

        connection d1: PortConnection
            connect a_process.output to c_process.input;

        connection d2: PortConnection
            connect b_process.output to c_process.input;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate a_process to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate b_process to proc;

        allocation pb2: Deployment_Properties::Actual_Processor_Binding
            allocate c_process to proc;

        attribute :>> Scheduling = MCS;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def Periodic_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Periodic;
    }
}
