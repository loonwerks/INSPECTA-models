//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_mcs
package event_2_prod_2_cons {

    private import HAMR::*;

    part def producer_t_p1 :> Periodic_Thread {
        out port write_port: EventPort;
    }

    part def producer_p_p1 :> Process {
        out port write_port: EventPort;

        part producer: producer_t_p1;

        connection c1: PortConnection
            connect producer.write_port to write_port;

        attribute :>> Domain = 2;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def producer_t_p2 :> Periodic_Thread {
        out port write_port: EventPort;
    }

    part def producer_p_p2 :> Process {
        out port write_port: EventPort;

        part producer: producer_t_p2;

        connection c1: PortConnection
            connect producer.write_port to write_port;

        attribute :>> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def consumer_t_p :> Periodic_Thread {
        in port read_port1: EventPort;
        in port read_port2: EventPort;
    }

    part def consumer_p_p :> Process {
        in port read_port1: EventPort;
        in port read_port2: EventPort;

        part consumer: consumer_t_p;

        connection c1: PortConnection
            connect read_port1 to consumer.read_port1;

        connection c2: PortConnection
            connect read_port2 to consumer.read_port2;

        attribute :>> Domain = 4;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def consumer_t_s :> Sporadic_Thread {
        in port read_port1: EventPort;
        in port read_port2: EventPort;
    }

    part def consumer_p_s :> Process {
        in port read_port1: EventPort;
        in port read_port2: EventPort;

        part consumer: consumer_t_s;

        connection c1: PortConnection
            connect read_port1 to consumer.read_port1;

        connection c2: PortConnection
            connect read_port2 to consumer.read_port2;

        attribute :>> Domain = 5;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    ///////////////////////////////////////////////////////////////////////////

    part def top_impl :> System {
        part proc: proc_impl;
        part producer_p_p1: producer_p_p1;
        part producer_p_p2: producer_p_p2;
        part consumer_p_p: consumer_p_p;
        part consumer_p_s: consumer_p_s;

        connection d1: PortConnection
            connect producer_p_p1.write_port to consumer_p_p.read_port1;

        connection d2: PortConnection
            connect producer_p_p2.write_port to consumer_p_p.read_port2;

        connection d3: PortConnection
            connect producer_p_p1.write_port to consumer_p_s.read_port1;

        connection d4: PortConnection
            connect producer_p_p2.write_port to consumer_p_s.read_port2;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate producer_p_p1 to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate producer_p_p2 to proc;

        allocation pb2: Deployment_Properties::Actual_Processor_Binding
            allocate consumer_p_p to proc;

        allocation pb3: Deployment_Properties::Actual_Processor_Binding
            allocate consumer_p_s to proc;

        //attribute :> Scheduling = Domain_Scheduling;
        attribute :>> Scheduling = MCS;
    }

    part def Periodic_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Periodic;
    }

    part def Sporadic_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Sporadic;
    }
}
