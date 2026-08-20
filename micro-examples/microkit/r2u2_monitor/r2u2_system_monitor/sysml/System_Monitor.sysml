//@ HAMR: --platform Microkit --output-dir ../hamr
// A minimal HAMR/Microkit example with a dedicated R2U2 monitor component.
package System_Monitor {

    private import HAMR::*;

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Periodic;
        attribute :>> Period = 1000 [ms];
        attribute :>> Compute_Execution_Time = 10 [ms] .. 10 [ms];
        attribute :>> Implementation_Language = Rust;
    }

    part def ProducerThr_i :> Periodic_Rust_Thread {
        port sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
    }

    part def ProducerProc_i :> Process {
        port sample: EventDataPort { out :>> type : Base_Types::Integer_32; }

        part producer: ProducerThr_i;

        connection sample_connection: PortConnection
            connect producer.sample to sample;

        attribute :>> Domain = 2;
    }

    part def ConsumerThr_i :> Periodic_Rust_Thread {
        port sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port observed_sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
        port alert_flag: EventDataPort { in :>> type : Base_Types::Boolean; }
    }

    part def ConsumerProc_i :> Process {
        port sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port observed_sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
        port alert_flag: EventDataPort { in :>> type : Base_Types::Boolean; }

        part consumer: ConsumerThr_i;

        connection sample_connection: PortConnection
            connect sample to consumer.sample;

        connection observed_connection: PortConnection
            connect consumer.observed_sample to observed_sample;

        connection alert_connection: PortConnection
            connect alert_flag to consumer.alert_flag;

        attribute :>> Domain = 3;
    }

    // This component has no application responsibility. It receives independent
    // observations of what the producer sent and what the consumer processed.
    part def MonitorThr_i :> Periodic_Rust_Thread {
        port sent_sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port observed_sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port alert_flag: EventDataPort { out :>> type : Base_Types::Boolean; }

        language "GUMBO" /*{
            monitor
                guarantee samples_match_until_producer_pauses
                    "The consumer matches every produced sample until the producer pauses, which must occur within five dispatches":
                    (
                        HasEvent(sent_sample) &
                        HasEvent(observed_sample) &
                        observed_sample == sent_sample
                    ) Until[0,5] (
                        not HasEvent(sent_sample)
                    );

                alert samples_match_until_producer_pauses on alert_flag;
        }*/
    }

    part def MonitorProc_i :> Process {
        port sent_sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port observed_sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port alert_flag: EventDataPort { out :>> type : Base_Types::Boolean; }

        part monitor_thread: MonitorThr_i;

        connection sent_connection: PortConnection
            connect sent_sample to monitor_thread.sent_sample;

        connection observed_connection: PortConnection
            connect observed_sample to monitor_thread.observed_sample;

        connection alert_connection: PortConnection
            connect monitor_thread.alert_flag to alert_flag;

        attribute :>> Domain = 4;
    }

    part def MicrokitProcessor :> Processor {
        attribute :>> Frame_Period = 1000 [ms];
        attribute :>> Clock_Period = 1 [ms];
    }

    part def Sys_i :> System {
        part producer: ProducerProc_i;
        part consumer: ConsumerProc_i;
        part monitor_process: MonitorProc_i;
        part cpu: MicrokitProcessor;

        connection producer_to_consumer: PortConnection
            connect producer.sample to consumer.sample;

        connection producer_to_monitor: PortConnection
            connect producer.sample to monitor_process.sent_sample;

        connection consumer_to_monitor: PortConnection
            connect consumer.observed_sample to monitor_process.observed_sample;

        connection monitor_to_consumer: PortConnection
            connect monitor_process.alert_flag to consumer.alert_flag;

        allocation producer_binding: Deployment_Properties::Actual_Processor_Binding
            allocate producer to cpu;

        allocation consumer_binding: Deployment_Properties::Actual_Processor_Binding
            allocate consumer to cpu;

        allocation monitor_binding: Deployment_Properties::Actual_Processor_Binding
            allocate monitor_process to cpu;

        attribute :>> Scheduling = MCS;
    }
}
