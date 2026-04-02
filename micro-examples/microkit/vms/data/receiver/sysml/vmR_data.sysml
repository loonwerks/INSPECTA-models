//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_mcs
package vmR_data {

    private import HAMR::*;

    ///////////////////////////////////////////////////////////////////////
    //
    //   T h r e a d s
    //
    ///////////////////////////////////////////////////////////////////////

    part def producer_t_p :> Periodic_Thread {
        port write_port : DataPort { out :>> type : Base_Types::Integer_8; }
    }

    part def consumer_t_p :> Periodic_Thread {
        port read_port : DataPort { in :>> type : Base_Types::Integer_8; }
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   P r o c e s s e s
    //
    ///////////////////////////////////////////////////////////////////////

    part def producer_p_p :> Process {
        port write_port : DataPort { out :>> type : Base_Types::Integer_8; }

        part producer : producer_t_p;

        connection c1 : PortConnection connect producer.write_port to write_port;

        attribute :>> Domain = 2;
    }

    part def consumer_p_p :> Process {
        port read_port : DataPort { in :>> type : Base_Types::Integer_8; }

        part consumer : consumer_t_p;

        connection c1 : PortConnection connect read_port to consumer.read_port;

        attribute :>> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   S y s t e m
    //
    ///////////////////////////////////////////////////////////////////////

    part def virt_proc :> VirtualProcessor;

    part def proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    part def top_impl :> System {
        part virt_proc    : virt_proc;
        part proc         : proc_impl;
        part producer_p_p : producer_p_p;
        part consumer_p_p : consumer_p_p;

        connection d1 : PortConnection connect producer_p_p.write_port to consumer_p_p.read_port;

        allocation pb0 : Deployment_Properties::Actual_Processor_Binding allocate consumer_p_p to virt_proc;
        allocation pb1 : Deployment_Properties::Actual_Processor_Binding allocate producer_p_p to proc;
        allocation pb2 : Deployment_Properties::Actual_Processor_Binding allocate virt_proc     to proc;

        attribute :>> Scheduling = Domain_Scheduling;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   T h r e a d   H e l p e r s
    //
    ///////////////////////////////////////////////////////////////////////

    part def Periodic_Thread :> Thread {
        attribute :>> Period            = 1000[ms];
        attribute :>> Dispatch_Protocol = Periodic;
    }
}
