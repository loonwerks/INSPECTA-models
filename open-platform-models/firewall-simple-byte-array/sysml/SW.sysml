//@ HAMR: --platform JVM --runtime-monitoring --slang-output-dir ../hamr/slang_sysml --package-name firewall
//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_sysml
package SW {

    private import HAMR::*;

    // ==========================================================================
    //   D a t a    T y p e s
    // ==========================================================================

    part def u16Array :> Array {
        part :>> Base_Type : Base_Types::Unsigned_16;

        attribute :>> Dimensions = 4;

        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 8[byte];
    }

    part def RawEthernetMessage :> Array {
        part :>> Base_Type : Base_Types::Unsigned_8;

        attribute :>> Dimensions = 1600;

        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 1600[byte];
    }

    // ==========================================================================
    //   T h r e a d s
    // ==========================================================================

    part def Periodic_Rust_Thread :> Thread {
        attribute :>> Period = 1000[ms];
        attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
        attribute :>> Implementation_Language = Implementation_Languages::Rust;
        attribute :>> Compute_Execution_Time = 100[ms] .. 100[ms] as Compute_Execution_Time;
        attribute :>> Stack_Size = 1048576[byte] as Size;
    }

    // ----------------------------------------------------------------------------------------

    part def LowLevelEthernetDriver_i :> Periodic_Rust_Thread {
        // Ethernet Frames: Raw Ethernet frames received from the physical network interface.
        port EthernetFramesRx: EventDataPort { out :>> type : RawEthernetMessage; }
        port EthernetFramesTx: EventDataPort { in :>> type : RawEthernetMessage; }
    }

    // ----------------------------------------------------------------------------------------

    part def Firewall_i :> Periodic_Rust_Thread {
        port EthernetFramesRxIn:  EventDataPort { in :>> type : RawEthernetMessage; }
        port EthernetFramesRxOut: EventDataPort { out :>> type : RawEthernetMessage; }

        port EthernetFramesTxIn:  EventDataPort { in :>> type : RawEthernetMessage; }
        port EthernetFramesTxOut: EventDataPort { out :>> type : RawEthernetMessage; }

        language "GUMBO" /*{
            functions
                // TODO: add const/vals to gumbo
                // def MIN_ETH2_FRAME_LENGTH(): Base_Types::Integer := 64;
                // def MAX_ETH2_FRAME_LENGTH(): Base_Types::Integer := 1518;


                // TODO: TCP_ALLOWED_PORTS and UDP_ALLOWED_PORTS should probably be sig/ui functions.
                //       HAMR would just emit the signature and an empty body that the developer must
                //       subsequently fill in
                def TCP_ALLOWED_PORTS(): u16Array := SW::u16Array(5760[u16], 0[u16], 0[u16], 0[u16]);

                def UDP_ALLOWED_PORTS(): u16Array := SW::u16Array(68[u16], 0[u16], 0[u16], 0[u16]);


                def two_bytes_to_u16(byte0: Base_Types::Unsigned_8, byte1: Base_Types::Unsigned_8): Base_Types::Unsigned_16 :=
                    byte0.toU16 * 256[u16] + byte1.toU16;

                def frame_is_wellformed_eth2(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    // if (!(MIN_ETH2_FRAME_LENGTH() <= eth_frame.size && eth_frame.size <= MAX_ETH2_FRAME_LENGTH())) F
                    if (not(eth_frame#(12) >= 6[u8] and eth_frame#(13) >= 0[u8]))? false
                    else true;


                def frame_has_ipv4(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame),
                        if (not(eth_frame#(12) == 8[u8] and eth_frame#(13) == 0[u8]))? false
                        else true);

                def frame_has_ipv4_tcp(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame) and frame_has_ipv4(eth_frame),
                        if (eth_frame#(23) != 6[u8])? false
                        else true);

                def frame_has_ipv4_udp(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame) and frame_has_ipv4(eth_frame),
                        if (not(eth_frame#(23) == 17[u8]))? false
                        else true);

                def frame_has_ipv4_tcp_on_allowed_port(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame) and
                        frame_has_ipv4(eth_frame) and
                        frame_has_ipv4_tcp(eth_frame),
                    TCP_ALLOWED_PORTS()#(0) == two_bytes_to_u16(eth_frame#(36), eth_frame#(37)));

                def frame_has_ipv4_tcp_on_allowed_port_quant(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    (0 .. size(TCP_ALLOWED_PORTS()) - 1) -> exists {i;
                        TCP_ALLOWED_PORTS()#(i) == two_bytes_to_u16(eth_frame#(36), eth_frame#(37))};

                def frame_has_ipv4_udp_on_allowed_port(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame) and
                        frame_has_ipv4(eth_frame) and
                        frame_has_ipv4_udp(eth_frame),
                    UDP_ALLOWED_PORTS()#(0) == two_bytes_to_u16(eth_frame#(36), eth_frame#(37)));

                def frame_has_ipv4_udp_on_allowed_port_quant(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    (0 .. size(UDP_ALLOWED_PORTS()) - 1) -> exists {i;
                        UDP_ALLOWED_PORTS()#(i) == two_bytes_to_u16(eth_frame#(36), eth_frame#(37))};

                def frame_has_ipv6(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame),
                        if (not(eth_frame#(12) == 134[u8] and eth_frame#(13) == 221[u8]))? false
                        else true);

                def frame_has_arp(eth_frame: RawEthernetMessage): Base_Types::Boolean :=
                    '->:'(frame_is_wellformed_eth2(eth_frame),
                        if (not(eth_frame#(12) == 8[u8] and eth_frame#(13) == 6[u8]))? false
                        else true);


                // ------------------------------------------------------------------------
                // RX Firewall
                // ------------------------------------------------------------------------

                def hlr_1_1(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (not frame_is_wellformed_eth2(eth_frame))? should_allow == false
                    else true;

                def hlr_1_2(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and frame_has_ipv6(eth_frame))? should_allow == false
                    else true;

                def hlr_1_3(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and
                            frame_has_ipv4(eth_frame) and
                            not(frame_has_ipv4_tcp(eth_frame) or frame_has_ipv4_udp(eth_frame)))? should_allow == false
                    else true;

                def hlr_1_4(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and
                            frame_has_ipv4(eth_frame) and
                            frame_has_ipv4_tcp(eth_frame) and
                            not frame_has_ipv4_tcp_on_allowed_port(eth_frame))? should_allow == false
                    else true;

                def hlr_1_5(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and
                            frame_has_ipv4(eth_frame) and
                            frame_has_ipv4_udp(eth_frame) and
                            not frame_has_ipv4_udp_on_allowed_port(eth_frame))? should_allow == false
                    else true;

                def hlr_1_6(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and
                            frame_has_arp(eth_frame))? should_allow == true
                    else true;

                def hlr_1_7(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and
                            frame_has_ipv4(eth_frame) and
                            frame_has_ipv4_tcp(eth_frame) and
                            frame_has_ipv4_tcp_on_allowed_port(eth_frame))? should_allow == true
                    else true;

                def hlr_1_8(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and
                            frame_has_ipv4(eth_frame) and
                            frame_has_ipv4_udp(eth_frame) and
                            frame_has_ipv4_udp_on_allowed_port(eth_frame))? should_allow == true
                    else true;

                def should_allow_inbound_frame_rx(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    hlr_1_1(eth_frame, should_allow) and
                    hlr_1_2(eth_frame, should_allow) and
                    hlr_1_3(eth_frame, should_allow) and
                    hlr_1_4(eth_frame, should_allow) and
                    hlr_1_5(eth_frame, should_allow) and
                    hlr_1_6(eth_frame, should_allow) and
                    hlr_1_7(eth_frame, should_allow) and
                    hlr_1_8(eth_frame, should_allow);

                // ------------------------------------------------------------------------
                // TX Firewall
                // ------------------------------------------------------------------------

                def hlr_2_1(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (not frame_is_wellformed_eth2(eth_frame))? should_allow == false
                    else true;

                def hlr_2_2(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and frame_has_ipv6(eth_frame))? should_allow == false
                    else true;

                def hlr_2_3(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and frame_has_arp(eth_frame))? should_allow == true
                    else true;

                def hlr_2_4(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    if (frame_is_wellformed_eth2(eth_frame) and frame_has_ipv4(eth_frame))? should_allow == true
                    else true;

                def should_allow_outbound_frame_tx(eth_frame: RawEthernetMessage, should_allow: Base_Types::Boolean): Base_Types::Boolean :=
                    hlr_2_1(eth_frame, should_allow) and
                    hlr_2_2(eth_frame, should_allow) and
                    hlr_2_3(eth_frame, should_allow) and
                    hlr_2_4(eth_frame, should_allow);

            compute

                guarantee rx:
                    (HasEvent(EthernetFramesRxIn) implies
                            (HasEvent(EthernetFramesRxOut) implies should_allow_inbound_frame_rx(EthernetFramesRxIn, true) and EthernetFramesRxIn == EthernetFramesRxOut) and
                            '->:'(NoSend(EthernetFramesRxOut), should_allow_inbound_frame_rx(EthernetFramesRxIn, false))) and
                    '->:'(not HasEvent(EthernetFramesRxIn), NoSend(EthernetFramesRxOut));

                guarantee tx:
                    (HasEvent(EthernetFramesTxIn) implies
                            (HasEvent(EthernetFramesTxOut) implies should_allow_outbound_frame_tx(EthernetFramesTxIn, true) and EthernetFramesTxIn == EthernetFramesTxOut) and
                            '->:'(NoSend(EthernetFramesTxOut), should_allow_outbound_frame_tx(EthernetFramesTxIn, false))) and
                    '->:'(not HasEvent(EthernetFramesTxIn), NoSend(EthernetFramesTxOut));
        }*/
    }

    // ----------------------------------------------------------------------------------------

    part def ArduPilot_i :> Periodic_Rust_Thread {
        port EthernetFramesRx: EventDataPort { in :>> type : RawEthernetMessage; }
        port EthernetFramesTx: EventDataPort { out :>> type : RawEthernetMessage; }
    }

    // ==========================================================================
    //   P r o c e s s e s
    // ==========================================================================

    part def LowLevelEthernetDriver_seL4_i :> Process {
        // Ethernet Frames: Raw Ethernet frames received from the physical network interface.
        port EthernetFramesRx: EventDataPort { out :>> type : RawEthernetMessage; }
        port EthernetFramesTx: EventDataPort { in :>> type : RawEthernetMessage; }

        part LowLevelEthernetDriver: LowLevelEthernetDriver_i;

        connection c1: PortConnection connect EthernetFramesTx to LowLevelEthernetDriver.EthernetFramesTx;
        connection c2: PortConnection connect LowLevelEthernetDriver.EthernetFramesRx to EthernetFramesRx;

        attribute :>> Domain = 2;
    }

    part def Firewall_seL4_i :> Process {
        port EthernetFramesRxIn:  EventDataPort { in :>> type : RawEthernetMessage; }
        port EthernetFramesRxOut: EventDataPort { out :>> type : RawEthernetMessage; }

        port EthernetFramesTxIn:  EventDataPort { in :>> type : RawEthernetMessage; }
        port EthernetFramesTxOut: EventDataPort { out :>> type : RawEthernetMessage; }

        part Firewall: Firewall_i;

        connection c1: PortConnection connect EthernetFramesRxIn to Firewall.EthernetFramesRxIn;
        connection c2: PortConnection connect EthernetFramesTxIn to Firewall.EthernetFramesTxIn;
        connection c3: PortConnection connect Firewall.EthernetFramesRxOut to EthernetFramesRxOut;
        connection c4: PortConnection connect Firewall.EthernetFramesTxOut to EthernetFramesTxOut;

        attribute :>> Domain = 3;
    }

    part def ArduPilot_seL4_i :> Process {
        port EthernetFramesRx: EventDataPort { in :>> type : RawEthernetMessage; }
        port EthernetFramesTx: EventDataPort { out :>> type : RawEthernetMessage; }

        part ArduPilot: ArduPilot_i;

        connection c1: PortConnection connect EthernetFramesRx to ArduPilot.EthernetFramesRx;
        connection c2: PortConnection connect ArduPilot.EthernetFramesTx to EthernetFramesTx;

        attribute :>> Domain = 4;
    }

    // ==========================================================================
    //   P r o c e s s o r
    // ==========================================================================

    part def Firewall_Processor :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 1[ms];
    }

    // ==========================================================================
    //   T o p - L e v e l    S y s t e m
    // ==========================================================================

    part def seL4_i :> System {
        part proc: Firewall_Processor;

        part ArduPilot: ArduPilot_seL4_i;
        part Firewall: Firewall_seL4_i;
        part LowLevelEthernetDriver: LowLevelEthernetDriver_seL4_i;

        // Incoming traffic
        connection c1: PortConnection connect LowLevelEthernetDriver.EthernetFramesRx to Firewall.EthernetFramesRxIn;
        connection c2: PortConnection connect Firewall.EthernetFramesRxOut to ArduPilot.EthernetFramesRx;
        // Outgoing traffic
        connection c3: PortConnection connect ArduPilot.EthernetFramesTx to Firewall.EthernetFramesTxIn;
        connection c4: PortConnection connect Firewall.EthernetFramesTxOut to LowLevelEthernetDriver.EthernetFramesTx;

        allocation pb0: Deployment_Properties::Actual_Processor_Binding
            allocate ArduPilot to proc;

        allocation pb1: Deployment_Properties::Actual_Processor_Binding
            allocate Firewall to proc;

        allocation pb2: Deployment_Properties::Actual_Processor_Binding
            allocate LowLevelEthernetDriver to proc;
    }
}
