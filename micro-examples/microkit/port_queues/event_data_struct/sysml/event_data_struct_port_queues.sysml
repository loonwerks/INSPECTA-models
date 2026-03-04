//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_mcs
package event_data_struct_port_queues {

    private import HAMR::*;

    ///////////////////////////////////////////////////////////////////////
    //
    //   D a t a   T y p e s
    //
    ///////////////////////////////////////////////////////////////////////

    part def ArrayOfInts :> Array {
        part :>> Base_Type    : Base_Types::Integer_32;
        attribute :>> Dimensions      = 10;
        attribute :>> Array_Size_Kind = Fixed;
        attribute :>> Data_Size       = 40 [byte];
    }

    part def struct_i :> Struct {
        part size     : Base_Types::Integer_32;
        part elements : ArrayOfInts;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   T h r e a d s
    //
    ///////////////////////////////////////////////////////////////////////

    part def producer_t_p :> Periodic_Thread {
        port write_port : EventDataPort { out :>> type : struct_i; }
    }

    part def consumer_queue_1_t_s :> Sporadic_Thread {
        port read_port : EventDataPort { in :>> type : struct_i; }
    }

    part def consumer_queue_2_t_s :> Sporadic_Thread {
        port read_port : EventDataPort {
            in :>> type : struct_i;
            attribute :>> Queue_Size = 2;
        }
    }

    part def consumer_queue_5_t_s :> Sporadic_Thread {
        port read_port : EventDataPort {
            in :>> type : struct_i;
            attribute :>> Queue_Size = 5;
        }
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   P r o c e s s e s
    //
    ///////////////////////////////////////////////////////////////////////

    part def producer_p_p :> Process {
        port write_port : EventDataPort { out :>> type : struct_i; }

        part producer : producer_t_p;

        connection c1 : PortConnection connect producer.write_port to write_port;

        attribute :>> Domain = 2;
    }

    part def consumer_p_s1 :> Process {
        port read_port : EventDataPort { in :>> type : struct_i; }

        part consumer : consumer_queue_1_t_s;

        connection c1 : PortConnection connect read_port to consumer.read_port;

        attribute :>> Domain = 3;
    }

    part def consumer_p_s2 :> Process {
        port read_port : EventDataPort { in :>> type : struct_i; }

        part consumer : consumer_queue_2_t_s;

        connection c1 : PortConnection connect read_port to consumer.read_port;

        attribute :>> Domain = 4;
    }

    part def consumer_p_s5 :> Process {
        port read_port : EventDataPort { in :>> type : struct_i; }

        part consumer : consumer_queue_5_t_s;

        connection c1 : PortConnection connect read_port to consumer.read_port;

        attribute :>> Domain = 5;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   S y s t e m
    //
    ///////////////////////////////////////////////////////////////////////

    part def proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    part def top_impl :> System {
        part proc          : proc_impl;
        part producer_p_p  : producer_p_p;
        part consumer_p_s1 : consumer_p_s1;
        part consumer_p_s2 : consumer_p_s2;
        part consumer_p_s5 : consumer_p_s5;

        connection c1 : PortConnection connect producer_p_p.write_port to consumer_p_s1.read_port;
        connection c2 : PortConnection connect producer_p_p.write_port to consumer_p_s2.read_port;
        connection c3 : PortConnection connect producer_p_p.write_port to consumer_p_s5.read_port;

        allocation pb0 : Deployment_Properties::Actual_Processor_Binding allocate producer_p_p  to proc;
        allocation pb1 : Deployment_Properties::Actual_Processor_Binding allocate consumer_p_s1 to proc;
        allocation pb2 : Deployment_Properties::Actual_Processor_Binding allocate consumer_p_s2 to proc;
        allocation pb3 : Deployment_Properties::Actual_Processor_Binding allocate consumer_p_s5 to proc;

        attribute :>> Scheduling = MCS;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   T h r e a d   H e l p e r s
    //
    ///////////////////////////////////////////////////////////////////////

    part def Periodic_Thread :> Thread {
        attribute :>> Period            = 1[s];
        attribute :>> Dispatch_Protocol = Periodic;
    }

    part def Sporadic_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Sporadic;
    }
}
