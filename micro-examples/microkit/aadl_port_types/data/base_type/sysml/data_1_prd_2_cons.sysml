//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_sysml
package data_1_prod_2_cons {
    
    private import HAMR::*;

    part def producer_t_p :> Periodic_Thread {
        port write_port: DataPort { out :>> type : Base_Types::Integer_8; }
    }

    part def producer_p_p :> Process {
        port write_port: DataPort { out :>> type : Base_Types::Integer_8; }

        part producer: producer_t_p;

        connection c1 : PortConnection
            connect producer.write_port to write_port;

        attribute :> Domain = 2;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def consumer_t_p :> Periodic_Thread {
        port read_port: DataPort { in :>> type : Base_Types::Integer_8; }
    }

    part def consumer_p_p :> Process {
        port read_port: DataPort { in :>> type : Base_Types::Integer_8; }

        part consumer: consumer_t_p;

        connection c1: PortConnection
            connect read_port to consumer.read_port;

        attribute :> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    part def consumer_t_s :> Periodic_Thread {
        port read_port: DataPort { in :>> type : Base_Types::Integer_8; }
    }

    part def consumer_p_s :> Process {
        port read_port: DataPort { in :>> type : Base_Types::Integer_8; }

        part consumer: consumer_t_s;

        connection c1: PortConnection
            connect read_port to consumer.read_port;

        attribute :> Domain = 4;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def proc_impl :> Processor {
        attribute :> Frame_Period = 1000[ms];
        attribute :> Clock_Period = 2[ms];
    }

    ///////////////////////////////////////////////////////////////////////////

    part def top_impl :> System {
        part proc: proc_impl;
        part producer_p_p: producer_p_p;
        part consumer_p_p: consumer_p_p;
        part consumer_p_s: consumer_p_s;

        connection d1: PortConnection
            connect producer_p_p.write_port to consumer_p_p.read_port;

        connection d2: PortConnection
            connect producer_p_p.write_port to consumer_p_s.read_port;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate producer_p_p to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate consumer_p_p to proc;

        allocation pb2: Deployment_Properties::Actual_Processor_Binding
            allocate consumer_p_s to proc;

        //attribute :> Scheduling = Domain_Scheduling;
        attribute :> Scheduling = MCS;
    }
    
    part def Periodic_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Periodic;
    }
}