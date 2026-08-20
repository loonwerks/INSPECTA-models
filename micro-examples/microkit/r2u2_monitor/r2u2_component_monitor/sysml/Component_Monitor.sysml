//@ HAMR: --platform Microkit --output-dir ../hamr/microkit_mcs
// A minimal HAMR/Microkit example whose consumer invokes an R2U2 monitor.
package Component_Monitor {

    private import HAMR::*;

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Periodic;
        attribute :>> Period = 1000 [ms];
        attribute :>> Compute_Execution_Time = 10 [ms] .. 10 [ms];
        attribute :>> Implementation_Language = Rust;
    }

    part def ProducerThr_i :> Periodic_Rust_Thread {
        port sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
        in port sample_alert: EventPort;
    }

    part def ProducerProc_i :> Process {
        port sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
        in port sample_alert: EventPort;

        part producer: ProducerThr_i;

        connection sample_connection: PortConnection
            connect producer.sample to sample;

        connection sample_alert_connection: PortConnection
            connect sample_alert to producer.sample_alert;

        attribute :>> Domain = 2;
    }

    part def ConsumerThr_i :> Periodic_Rust_Thread {
        port sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        out port sample_alert: EventPort;

        // HAMR translates this monitor guarantee to a C2PO specification and
        // invokes R2U2 from the generated consumer component on every dispatch.
        language "GUMBO" /*{
            monitor
                guarantee sample_arrives "A sample arrives now or within the next two dispatches":
                    Eventually[0,2] (HasEvent(sample));

                alert sample_arrives on sample_alert;
        }*/
    }

    part def ConsumerProc_i :> Process {
        port sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        out port sample_alert: EventPort;

        part consumer: ConsumerThr_i;

        connection sample_connection: PortConnection
            connect sample to consumer.sample;

        connection sample_alert_connection: PortConnection
            connect consumer.sample_alert to sample_alert;

        attribute :>> Domain = 3;
    }

    part def MicrokitProcessor :> Processor {
        attribute :>> Frame_Period = 1000 [ms];
        attribute :>> Clock_Period = 1 [ms];
    }

    part def Sys_i :> System {
        part producer: ProducerProc_i;
        part consumer: ConsumerProc_i;
        part cpu: MicrokitProcessor;

        connection samples: PortConnection
            connect producer.sample to consumer.sample;

        connection alerts: PortConnection
            connect consumer.sample_alert to producer.sample_alert;

        allocation producer_binding: Deployment_Properties::Actual_Processor_Binding
            allocate producer to cpu;

        allocation consumer_binding: Deployment_Properties::Actual_Processor_Binding
            allocate consumer to cpu;

        attribute :>> Scheduling = MCS;
    }
}
