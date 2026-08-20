//@ HAMR: --platform Microkit --output-dir ../hamr
// Coverage model for the GUMBO monitor-to-C2PO/R2U2 translation.
//
// The guarantees are intentionally independent. They are a code-generation
// test matrix, not a claim that every guarantee can hold simultaneously.
package Exhaustive_Monitor {

    private import HAMR::*;
    private import SequenceFunctions::*;
    private import ControlFunctions::*;

    enum def OperatingState {
        Idle;
        Active;
        Fault;
    }

    part def Samples :> Array {
        part :>> Base_Type : Base_Types::Integer_32;
        attribute :>> Dimensions = 4;
        attribute :>> Array_Size_Kind = Fixed;
        attribute :>> Data_Size = 16 [byte];
    }

    part def Telemetry_i :> Struct {
        part sequence_number: Base_Types::Integer_32;
        part valid: Base_Types::Boolean;
        attribute operating_state: OperatingState;
        part values: Samples;
    }

    part def TelemetryHistory :> Array {
        part :>> Base_Type : Telemetry_i;
        attribute :>> Dimensions = 2;
        attribute :>> Array_Size_Kind = Fixed;
        attribute :>> Data_Size = 64 [byte];
    }

    part def NestedTelemetry_i :> Struct {
        part latest: Telemetry_i;
        part history: TelemetryHistory;
    }

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Dispatch_Protocol = Periodic;
        attribute :>> Period = 1000 [ms];
        attribute :>> Compute_Execution_Time = 10 [ms] .. 10 [ms];
        attribute :>> Implementation_Language = Rust;
    }

    // Produces every incoming signal category used by the monitor.
    part def ProducerThr_i :> Periodic_Rust_Thread {
        out port pulse: EventPort;
        port sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
        port level: DataPort { out :>> type : Base_Types::Integer_32; }
        port flag: EventDataPort { out :>> type : Base_Types::Boolean; }
        port boolean_value: DataPort { out :>> type : Base_Types::Boolean; }
        port character_value: DataPort { out :>> type : Base_Types::Character; }
        port signed_8_value: DataPort { out :>> type : Base_Types::Integer_8; }
        port signed_16_value: DataPort { out :>> type : Base_Types::Integer_16; }
        port unsigned_8_value: DataPort { out :>> type : Base_Types::Unsigned_8; }
        port unsigned_16_value: DataPort { out :>> type : Base_Types::Unsigned_16; }
        port operating_state: EventDataPort { out :>> type : OperatingState; }
        port samples: EventDataPort { out :>> type : Samples; }
        port telemetry: EventDataPort { out :>> type : Telemetry_i; }
    }

    part def ProducerProc_i :> Process {
        out port pulse: EventPort;
        port sample: EventDataPort { out :>> type : Base_Types::Integer_32; }
        port level: DataPort { out :>> type : Base_Types::Integer_32; }
        port flag: EventDataPort { out :>> type : Base_Types::Boolean; }
        port boolean_value: DataPort { out :>> type : Base_Types::Boolean; }
        port character_value: DataPort { out :>> type : Base_Types::Character; }
        port signed_8_value: DataPort { out :>> type : Base_Types::Integer_8; }
        port signed_16_value: DataPort { out :>> type : Base_Types::Integer_16; }
        port unsigned_8_value: DataPort { out :>> type : Base_Types::Unsigned_8; }
        port unsigned_16_value: DataPort { out :>> type : Base_Types::Unsigned_16; }
        port operating_state: EventDataPort { out :>> type : OperatingState; }
        port samples: EventDataPort { out :>> type : Samples; }
        port telemetry: EventDataPort { out :>> type : Telemetry_i; }

        part producer: ProducerThr_i;

        connection pulse_connection: PortConnection connect producer.pulse to pulse;
        connection sample_connection: PortConnection connect producer.sample to sample;
        connection level_connection: PortConnection connect producer.level to level;
        connection flag_connection: PortConnection connect producer.flag to flag;
        connection boolean_connection: PortConnection connect producer.boolean_value to boolean_value;
        connection character_connection: PortConnection connect producer.character_value to character_value;
        connection signed_8_connection: PortConnection connect producer.signed_8_value to signed_8_value;
        connection signed_16_connection: PortConnection connect producer.signed_16_value to signed_16_value;
        connection unsigned_8_connection: PortConnection connect producer.unsigned_8_value to unsigned_8_value;
        connection unsigned_16_connection: PortConnection connect producer.unsigned_16_value to unsigned_16_value;
        connection state_connection: PortConnection connect producer.operating_state to operating_state;
        connection samples_connection: PortConnection connect producer.samples to samples;
        connection telemetry_connection: PortConnection connect producer.telemetry to telemetry;

        attribute :>> Domain = 2;
    }

    part def MonitorThr_i :> Periodic_Rust_Thread {
        // All three incoming AADL port categories.
        in port pulse: EventPort;
        port sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port level: DataPort { in :>> type : Base_Types::Integer_32; }

        // Remaining R2U2 scalar and complex signal payloads.
        port flag: EventDataPort { in :>> type : Base_Types::Boolean; }
        port boolean_value: DataPort { in :>> type : Base_Types::Boolean; }
        port character_value: DataPort { in :>> type : Base_Types::Character; }
        port signed_8_value: DataPort { in :>> type : Base_Types::Integer_8; }
        port signed_16_value: DataPort { in :>> type : Base_Types::Integer_16; }
        port unsigned_8_value: DataPort { in :>> type : Base_Types::Unsigned_8; }
        port unsigned_16_value: DataPort { in :>> type : Base_Types::Unsigned_16; }
        port operating_state: EventDataPort { in :>> type : OperatingState; }
        port samples: EventDataPort { in :>> type : Samples; }
        port telemetry: EventDataPort { in :>> type : Telemetry_i; }

        // All three outgoing AADL port categories.
        out port ack: EventPort;
        port echo: EventDataPort { out :>> type : Base_Types::Integer_32; }
        port alert_result: EventDataPort { out :>> type : Base_Types::Boolean; }
        port healthy: DataPort { out :>> type : Base_Types::Boolean; }

        attribute :>> Stack_Size = 2097152 [byte] as Size;

        language "GUMBO" /*{
            state
                sprevious_level: Base_Types::Integer_32;

            functions
                def isNonNegative(value: Base_Types::Integer_32): Base_Types::Boolean :=
                    value >= 0 [i32];

                def absoluteValue(value: Base_Types::Integer_32): Base_Types::Integer_32 :=
                    if (value < 0 [i32])? -value else value;

            monitor
                // Incoming event and event-data presence predicates.
                guarantee input_event
                    "An untyped incoming event port":
                    HasEvent(pulse);

                guarantee input_event_data
                    "A typed incoming event-data port and its payload":
                    HasEvent(sample) implies sample >= 0 [i32];

                // Incoming data ports are continuously sampled and need no HasEvent guard.
                guarantee input_data
                    "An incoming integer data port":
                    level >= 0 [i32];

                // AADL Base_Types normalized to C2PO bool and int inputs.
                // Integer_64, Unsigned_32, and Unsigned_64 are omitted because the
                // R2U2 integer signal is i32. Unbounded types and strings are omitted.
                // Floating-point Base_Types are omitted because HAMR rejects them.
                guarantee base_type_boolean:
                    boolean_value == true;

                guarantee base_type_character:
                    character_value == "A"[c];

                guarantee base_type_integer_8:
                    signed_8_value >= 0 [i8];

                guarantee base_type_integer_16:
                    signed_16_value >= 0 [i16];

                guarantee base_type_integer_32:
                    level >= 0 [i32];

                guarantee base_type_unsigned_8:
                    unsigned_8_value >= 0 [u8];

                guarantee base_type_unsigned_16:
                    unsigned_16_value >= 0 [u16];

                guarantee bool_signal
                    "A Boolean event-data payload":
                    HasEvent(flag) implies flag;

                // Outgoing event predicates, including both MustSend overloads.
                guarantee output_event_must_send
                    "An outgoing event must be emitted":
                    MustSend(ack);

                guarantee output_event_data_must_send
                    "An outgoing event-data port must be emitted":
                    MustSend(echo);

                guarantee output_event_no_send
                    "An outgoing event must not be emitted":
                    NoSend(ack);

                guarantee output_event_data_no_send
                    "An outgoing event-data port must not be emitted":
                    NoSend(echo);

                guarantee output_event_data_must_send_value
                    "An outgoing event-data port must carry the expected value":
                    MustSend(echo, 1 [i32]);

                guarantee output_data
                    "An outgoing data port used as a signal":
                    healthy == true;

                // Every temporal unary spelling accepted by the GUMBO grammar.
                guarantee temporal_future:
                    Future[0,3] HasEvent(pulse);

                guarantee temporal_eventually:
                    Eventually[0,3] HasEvent(sample);

                guarantee temporal_globally:
                    Globally[0,3] (level >= 0 [i32]);

                guarantee temporal_once:
                    Once[0,3] HasEvent(pulse);

                guarantee temporal_historically:
                    Historically[0,3] (level >= 0 [i32]);

                // Every temporal binary operator accepted by the GUMBO grammar.
                guarantee temporal_until:
                    (level >= 0 [i32]) Until[0,5] HasEvent(pulse);

                guarantee temporal_release:
                    (level >= 0 [i32]) Release[0,5] HasEvent(pulse);

                guarantee temporal_since:
                    (level >= 0 [i32]) Since[0,5] HasEvent(pulse);

                guarantee temporal_trigger:
                    (level >= 0 [i32]) Trigger[0,5] HasEvent(pulse);

                // Scalar expression/operator coverage used by monitor inputs.
                guarantee comparison_operators:
                    level == level &
                    level != -1 [i32] &
                    level < 1000 [i32] &
                    level <= 1000 [i32] &
                    level > -1000 [i32] &
                    level >= -1000 [i32];

                guarantee arithmetic_operators:
                    level + 1 [i32] - 1 [i32] == level &
                    level * 2 [i32] / 2 [i32] == level &
                    level % 2 [i32] >= 0 [i32];

                guarantee bitwise_operators:
                    (level & 1 [i32]) >= 0 [i32] &
                    (level | 1 [i32]) >= level &
                    (level xor 1 [i32]) >= 0 [i32];

                guarantee boolean_operators:
                    not false and
                    (HasEvent(pulse) or HasEvent(sample)) implies
                        (HasEvent(pulse) | HasEvent(sample));

                guarantee implication_operator:
                    (level >= 0 [i32]) implies (level + 1 [i32] > 0 [i32]);

                // Enum, array, record, member-access, array-access, and size coverage.
                guarantee enum_signal:
                    HasEvent(operating_state) implies
                        operating_state != OperatingState.Fault;

                guarantee array_signal:
                    HasEvent(samples) implies
                        (size(samples) == 4 & samples#(0) >= 0 [i32]);

                guarantee record_signal:
                    HasEvent(telemetry) implies
                        (telemetry.sequence_number >= 0 [i32] &
                         telemetry.valid &
                         telemetry.operating_state != OperatingState.Fault &
                         telemetry.values#(0) >= 0 [i32]);

                // Conditional expressions can produce either the guarantee Boolean or
                // a value consumed by the surrounding Boolean expression.
                guarantee conditional_boolean:
                    if (level >= 0 [i32])?
                        telemetry.sequence_number >= 0 [i32]
                    else telemetry.sequence_number >= 5 [i32];

                // Whole-array ranges with both quantifiers and upper-bound forms.
                guarantee quantified_all_until:
                    HasEvent(samples) implies
                        ((0 .. size(samples) - 1) -> forAll { in i;
                            samples#(i) >= 0 [i32] });

                guarantee quantified_all_to:
                    HasEvent(samples) implies
                        ((0 .. size(samples) - 1) -> forAll { in i;
                            samples#(i) >= 0 [i32] });

                guarantee quantified_exists_until:
                    HasEvent(samples) implies
                        ((0 .. size(samples) - 1) -> exists { in i;
                            samples#(i) == 0 [i32] });

                guarantee quantified_exists_to:
                    HasEvent(samples) implies
                        ((0 .. size(samples) - 1) -> exists { in i;
                            samples#(i) == 0 [i32] });

                // A proper subrange becomes a C2PO array slice.
                guarantee quantified_slice:
                    HasEvent(samples) implies
                        ((1 .. 2) -> forAll { in i;
                            samples#(i) >= 0 [i32] });

                // Index-sensitive predicates are statically instantiated for C2PO.
                guarantee quantified_index_sensitive:
                    HasEvent(samples) implies
                        ((0 .. size(samples) - 2) -> forAll { in i;
                            samples#(i) <= samples#(i + 1) });

                // Distinct binders exercise nested C2PO aggregations.
                guarantee quantified_nested:
                    HasEvent(samples) implies
                        ((0 .. size(samples) - 1) -> forAll { in i;
                            (0 .. size(samples) - 1) -> exists { in j;
                                samples#(i) <= samples#(j) }});

                // Current and pre-dispatch state-variable values.
                guarantee state_variable:
                    sprevious_level <= level;

                guarantee state_variable_in:
                    In(sprevious_level) == sprevious_level;

                // Boolean- and value-returning GUMBO function calls.
                guarantee boolean_function:
                    isNonNegative(level);

                guarantee value_function:
                    absoluteValue(level) >= 0 [i32];

                // Event alert ports are emitted only for false verdicts. Boolean
                // event-data alert ports carry the latest mapped verdict.
                alert comparison_operators on ack;
                alert base_type_boolean on alert_result;
        }*/
    }

    part def MonitorProc_i :> Process {
        in port pulse: EventPort;
        port sample: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port level: DataPort { in :>> type : Base_Types::Integer_32; }
        port flag: EventDataPort { in :>> type : Base_Types::Boolean; }
        port boolean_value: DataPort { in :>> type : Base_Types::Boolean; }
        port character_value: DataPort { in :>> type : Base_Types::Character; }
        port signed_8_value: DataPort { in :>> type : Base_Types::Integer_8; }
        port signed_16_value: DataPort { in :>> type : Base_Types::Integer_16; }
        port unsigned_8_value: DataPort { in :>> type : Base_Types::Unsigned_8; }
        port unsigned_16_value: DataPort { in :>> type : Base_Types::Unsigned_16; }
        port operating_state: EventDataPort { in :>> type : OperatingState; }
        port samples: EventDataPort { in :>> type : Samples; }
        port telemetry: EventDataPort { in :>> type : Telemetry_i; }
        out port ack: EventPort;
        port echo: EventDataPort { out :>> type : Base_Types::Integer_32; }
        port alert_result: EventDataPort { out :>> type : Base_Types::Boolean; }
        port healthy: DataPort { out :>> type : Base_Types::Boolean; }

        part monitor_thread: MonitorThr_i;

        connection pulse_connection: PortConnection connect pulse to monitor_thread.pulse;
        connection sample_connection: PortConnection connect sample to monitor_thread.sample;
        connection level_connection: PortConnection connect level to monitor_thread.level;
        connection flag_connection: PortConnection connect flag to monitor_thread.flag;
        connection boolean_connection: PortConnection connect boolean_value to monitor_thread.boolean_value;
        connection character_connection: PortConnection connect character_value to monitor_thread.character_value;
        connection signed_8_connection: PortConnection connect signed_8_value to monitor_thread.signed_8_value;
        connection signed_16_connection: PortConnection connect signed_16_value to monitor_thread.signed_16_value;
        connection unsigned_8_connection: PortConnection connect unsigned_8_value to monitor_thread.unsigned_8_value;
        connection unsigned_16_connection: PortConnection connect unsigned_16_value to monitor_thread.unsigned_16_value;
        connection state_connection: PortConnection connect operating_state to monitor_thread.operating_state;
        connection samples_connection: PortConnection connect samples to monitor_thread.samples;
        connection telemetry_connection: PortConnection connect telemetry to monitor_thread.telemetry;
        connection ack_connection: PortConnection connect monitor_thread.ack to ack;
        connection echo_connection: PortConnection connect monitor_thread.echo to echo;
        connection alert_result_connection: PortConnection connect monitor_thread.alert_result to alert_result;
        connection healthy_connection: PortConnection connect monitor_thread.healthy to healthy;

        attribute :>> Domain = 3;
    }

    part def ConsumerThr_i :> Periodic_Rust_Thread {
        in port ack: EventPort;
        port echo: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port alert_result: EventDataPort { in :>> type : Base_Types::Boolean; }
        port healthy: DataPort { in :>> type : Base_Types::Boolean; }
    }

    part def ConsumerProc_i :> Process {
        in port ack: EventPort;
        port echo: EventDataPort { in :>> type : Base_Types::Integer_32; }
        port alert_result: EventDataPort { in :>> type : Base_Types::Boolean; }
        port healthy: DataPort { in :>> type : Base_Types::Boolean; }

        part consumer: ConsumerThr_i;

        connection ack_connection: PortConnection connect ack to consumer.ack;
        connection echo_connection: PortConnection connect echo to consumer.echo;
        connection alert_result_connection: PortConnection connect alert_result to consumer.alert_result;
        connection healthy_connection: PortConnection connect healthy to consumer.healthy;

        attribute :>> Domain = 4;
    }

    part def MicrokitProcessor :> Processor {
        attribute :>> Frame_Period = 1000 [ms];
        attribute :>> Clock_Period = 1 [ms];
    }

    part def Sys_i :> System {
        part producer: ProducerProc_i;
        part monitor_process: MonitorProc_i;
        part consumer: ConsumerProc_i;
        part cpu: MicrokitProcessor;

        connection pulse_connection: PortConnection connect producer.pulse to monitor_process.pulse;
        connection sample_connection: PortConnection connect producer.sample to monitor_process.sample;
        connection level_connection: PortConnection connect producer.level to monitor_process.level;
        connection flag_connection: PortConnection connect producer.flag to monitor_process.flag;
        connection boolean_connection: PortConnection connect producer.boolean_value to monitor_process.boolean_value;
        connection character_connection: PortConnection connect producer.character_value to monitor_process.character_value;
        connection signed_8_connection: PortConnection connect producer.signed_8_value to monitor_process.signed_8_value;
        connection signed_16_connection: PortConnection connect producer.signed_16_value to monitor_process.signed_16_value;
        connection unsigned_8_connection: PortConnection connect producer.unsigned_8_value to monitor_process.unsigned_8_value;
        connection unsigned_16_connection: PortConnection connect producer.unsigned_16_value to monitor_process.unsigned_16_value;
        connection state_connection: PortConnection connect producer.operating_state to monitor_process.operating_state;
        connection samples_connection: PortConnection connect producer.samples to monitor_process.samples;
        connection telemetry_connection: PortConnection connect producer.telemetry to monitor_process.telemetry;
        connection ack_connection: PortConnection connect monitor_process.ack to consumer.ack;
        connection echo_connection: PortConnection connect monitor_process.echo to consumer.echo;
        connection alert_result_connection: PortConnection connect monitor_process.alert_result to consumer.alert_result;
        connection healthy_connection: PortConnection connect monitor_process.healthy to consumer.healthy;

        allocation producer_binding: Deployment_Properties::Actual_Processor_Binding
            allocate producer to cpu;

        allocation monitor_binding: Deployment_Properties::Actual_Processor_Binding
            allocate monitor_process to cpu;

        allocation consumer_binding: Deployment_Properties::Actual_Processor_Binding
            allocate consumer to cpu;

        attribute :>> Scheduling = MCS;
    }
}
