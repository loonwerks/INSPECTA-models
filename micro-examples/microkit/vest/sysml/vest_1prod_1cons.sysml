//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit
package vest_1prod_1cons {
    
    private import HAMR::*;

    part def ArrayOfByte :> Array {
        part :>> Base_Type : Base_Types::Unsigned_8;
        attribute :>> Dimensions = 11;
        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;
        attribute :>> Data_Size = 11 [byte];
    }

    ///////////////////////////////////////////////////////////////////////////

    part def producer_t_p :> Periodic_Rust_Thread {
        port write_port: DataPort { out :>> type : ArrayOfByte; }
    }

    part def producer_p_p :> Process {
        port write_port: DataPort { out :>> type : ArrayOfByte; }

        part producer: producer_t_p;

        connection c1 : PortConnection
            connect producer.write_port to write_port;

        attribute :>> Domain = 2;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def consumer_t_p :> Periodic_Rust_Thread {
        port read_port: DataPort { in :>> type : ArrayOfByte; }
    }

    part def consumer_p_p :> Process {
        port read_port: DataPort { in :>> type : ArrayOfByte; }

        part consumer: consumer_t_p;

        connection c1: PortConnection
            connect read_port to consumer.read_port;

        attribute :>> Domain = 3;
    }

    ///////////////////////////////////////////////////////////////////////////

    part def proc_impl :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    ///////////////////////////////////////////////////////////////////////////

    part def top_impl :> System {
        part proc: proc_impl;
        part producer_p_p: producer_p_p;
        part consumer_p_p: consumer_p_p;

        connection d1: PortConnection
            connect producer_p_p.write_port to consumer_p_p.read_port;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate producer_p_p to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate consumer_p_p to proc;

        attribute :> Scheduling = Domain_Scheduling;
        //attribute :>> Scheduling = MCS;
    }
    
    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Period = 1[s];
        attribute :>> Dispatch_Protocol = Periodic;

        attribute :>> Implementation_Language = Rust;
    }

    part def Sporadic_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Sporadic;
    }
}
