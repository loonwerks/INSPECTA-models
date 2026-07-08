// Regression model for hamr-tutorials BR-02:
//   https://github.com/santoslab/hamr-tutorials/blob/main/bug-reports/BR-02-microkit-codegen-pure-eventport-integration-crash.md
//
// A Rust thread that has a GUMBO integration section and also owns a pure
// EventPort used to halt codegen with "Need to handle event ports"
// (GumboRustPlugin.handleIntegrationConstraints) even though no integration
// constraint can mention a pure event port (they have no payload to
// constrain, and GclResolver rejects event predicates such as HasEvent in
// integration clauses).
//
// Both threads exhibit the trigger: a GUMBO integration constraint on the
// data port plus an unconstrained pure event port; either thread alone is
// sufficient to reproduce the halt.
package pure_event_port {

    private import HAMR::*;

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Periodic;
        attribute :>> Implementation_Language = Rust;
    }

    part def snd_t :> Periodic_Rust_Thread {
        port out_val: DataPort { out :>> type : Base_Types::Integer_32; }
        out port evt: EventPort;

        language "GUMBO" /*{
            integration
                guarantee out_val_range:
                    out_val <= 100 [i32];

            initialize
                guarantee initOutVal "outgoing data ports must be initialized":
                    out_val == 0 [i32];

            compute
                guarantee sendVal "every dispatch publishes an in-range value on out_val; the
                                  |integration constraint provides the upper bound of 100":
                    out_val >= 0 [i32];

                guarantee announce "every dispatch announces that data is available to consume":
                    MustSend(evt);
        }*/
    }

    part def snd_p :> Process {
        port out_val: DataPort { out :>> type : Base_Types::Integer_32; }
        out port evt: EventPort;

        part snd: snd_t;

        connection c1: PortConnection
            connect snd.out_val to out_val;

        connection c2: PortConnection
            connect snd.evt to evt;

        attribute :>> Domain = 2;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def rcv_t :> Periodic_Rust_Thread {
        port in_val: DataPort { in :>> type : Base_Types::Integer_32; }
        in port evt: EventPort;

        language "GUMBO" /*{
            state
                lastVal: Base_Types::Integer_32;

            integration
                assume in_val_range:
                    in_val >= 0 [i32];

            initialize
                modifies lastVal;

                guarantee initLastVal "state is initialized before the first dispatch":
                    lastVal == 0 [i32];

            compute
                modifies lastVal;

                assume dataBounded "a received event announces a value of at most 100; the
                                   |integration constraint provides the lower bound of 0":
                    HasEvent(evt) implies in_val <= 100 [i32];

                guarantee latchOnEvent "record the announced value":
                    HasEvent(evt) implies lastVal == in_val;

                guarantee holdOtherwise "no event, no state change":
                    (not HasEvent(evt)) implies lastVal == In(lastVal);
        }*/
    }

    part def rcv_p :> Process {
        port in_val: DataPort { in :>> type : Base_Types::Integer_32; }
        in port evt: EventPort;

        part rcv: rcv_t;

        connection c1: PortConnection
            connect in_val to rcv.in_val;

        connection c2: PortConnection
            connect evt to rcv.evt;

        attribute :>> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    part def top_impl :> System {
        part proc: proc_impl;
        part snd_p: snd_p;
        part rcv_p: rcv_p;

        connection d1: PortConnection
            connect snd_p.out_val to rcv_p.in_val;

        connection d2: PortConnection
            connect snd_p.evt to rcv_p.evt;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate snd_p to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate rcv_p to proc;

        attribute :>> Scheduling = MCS;
    }
}
