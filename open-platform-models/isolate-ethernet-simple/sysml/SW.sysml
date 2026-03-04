//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit_sysml
package SW {

    private import HAMR::*;

    // ==========================================================================
    //   D a t a    T y p e s
    // ==========================================================================

    enum def InternetProtocol {
        enum IPV4;
        enum IPV6;
    }

    enum def FrameProtocol {
        enum TCP;
        enum ARP;
    }

    enum def ARP_Type {
        enum REQUEST;
        enum REPLY;
        enum NA;
    }

    part def RawEthernetMessage :> Array {
        part :>> Base_Type : Base_Types::Unsigned_8;

        attribute :>> Dimensions = 1600;

        attribute :>> Array_Size_Kind = Array_Size_Kinds::Fixed;

        attribute :>> Data_Size = 1600[byte];
    }

    part def StructuredEthernetMessage_i :> Data {
        part malformedFrame : Base_Types::Boolean;
        attribute internetProtocol : InternetProtocol;
        attribute frameProtocol : FrameProtocol;
        part portIsWhitelisted : Base_Types::Boolean;
        attribute arpType : ARP_Type;
        part rawMessage : RawEthernetMessage;

        language "GUMBO" /*{
            invariants
                inv relateFrameProtocolToArpType:
                    (frameProtocol == SW::FrameProtocol.TCP & arpType == SW::ARP_Type.NA) |
                    (frameProtocol == SW::FrameProtocol.ARP &
                        (arpType == SW::ARP_Type.REQUEST | arpType == SW::ARP_Type.REPLY));
        }*/
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
        port EthernetFramesRx: EventDataPort { out :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTx: EventDataPort { in :>> type : StructuredEthernetMessage_i; }
    }

    // ----------------------------------------------------------------------------------------

    part def Firewall_i :> Periodic_Rust_Thread {
        port EthernetFramesRxIn:  EventDataPort { in :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTxIn:  EventDataPort { in :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesRxOut: EventDataPort { out :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTxOut: EventDataPort { out :>> type : StructuredEthernetMessage_i; }

        language "GUMBO" /*{
            functions
                def isMalformedFrame(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.malformedFrame;

                def getInternetProtocol(v: StructuredEthernetMessage_i): InternetProtocol := v.internetProtocol;
                def isIPV4(v: StructuredEthernetMessage_i): Base_Types::Boolean := getInternetProtocol(v) == SW::InternetProtocol.IPV4;
                def isIPV6(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.internetProtocol == SW::InternetProtocol.IPV6;

                def getFrameProtocol(v: StructuredEthernetMessage_i): FrameProtocol := v.frameProtocol;
                def isTCP(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.frameProtocol == SW::FrameProtocol.TCP;
                def isARP(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.frameProtocol == SW::FrameProtocol.ARP;

                def isPortWhitelisted(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.portIsWhitelisted;

                def getARP_Type(v: StructuredEthernetMessage_i): ARP_Type := v.arpType;
                def isARP_Request(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.arpType == SW::ARP_Type.REQUEST;
                def isARP_Reply(v: StructuredEthernetMessage_i): Base_Types::Boolean := v.arpType == SW::ARP_Type.REPLY;

            compute
                assume onlyOneInEvent "Allow at most one incoming event per dispatch":
                    ((not HasEvent(EthernetFramesRxIn)) & (not HasEvent(EthernetFramesTxIn))) |
                    (HasEvent(EthernetFramesRxIn) xor HasEvent(EthernetFramesTxIn));

                guarantee RC_INSPECTA_00_HLR_2 "1.2 firewall: drop ipv6 frames (RC_INSPECTA_00-HLR-2) The firewall shall drop any frame that is type ipv6.":
                    (HasEvent(EthernetFramesRxIn) implies
                        '->:'(EthernetFramesRxIn.internetProtocol == SW::InternetProtocol.IPV6,
                            NoSend(EthernetFramesRxOut)));

                guarantee RC_INSPECTA_00_HLR_4 "1.4 firewall: drop RxIn ipv4 tcp frames with unexpected ports (RC_INSPECTA_00-HLR-4) The firewall shall drop any frame from RxIn that is an Ipv4 frame whose protocol is TCP and whose port is not defined in the port whitelist.":
                    (HasEvent(EthernetFramesRxIn) implies
                        '->:'(EthernetFramesRxIn.internetProtocol == SW::InternetProtocol.IPV6 &
                                EthernetFramesRxIn.frameProtocol == SW::FrameProtocol.TCP &
                                (not EthernetFramesRxIn.portIsWhitelisted),
                            NoSend(EthernetFramesRxOut) or EthernetFramesRxOut != EthernetFramesRxIn));

                guarantee RC_INSPECTA_00_HLR_5 "1.5 firewall: reply to RxIn arp requests (RC_INSPECTA_00-HLR-5) If the firewall gets an Arp request frame from RxIn, the firewall shall send an Arp reply frame to TxOut.":
                    (HasEvent(EthernetFramesRxIn) implies
                        '->:'(isIPV4(EthernetFramesRxIn) & isARP(EthernetFramesRxIn),
                            MustSend(EthernetFramesTxOut) and isARP_Reply(EthernetFramesTxOut)));

                guarantee RC_INSPECTA_00_HLR_6 "1.6 firewall: copy through allowed tcp port packets (RC_INSPECTA_00-HLR-6) The firewall shall copy any frame from RxIn that is an Ipv4 frame with the TCP protocol and whose port is defined in the port whitelist to RxOut.":
                    (HasEvent(EthernetFramesRxIn) implies
                        '->:'(EthernetFramesRxIn.internetProtocol == SW::InternetProtocol.IPV4 &
                                EthernetFramesRxIn.frameProtocol == SW::FrameProtocol.TCP &
                                EthernetFramesRxIn.portIsWhitelisted,
                            MustSend(EthernetFramesRxOut) and EthernetFramesRxIn == EthernetFramesRxOut));

                guarantee RC_INSPECTA_00_HLR_7 "1.7 firewall: copy out tx arp and ipv4 frames (RC_INSPECTA_00-HLR-7) The firewall shall copy any frame from TxIn that is an Ipv4 or Arp frame to TxOut.":
                    (HasEvent(EthernetFramesTxIn) implies
                        '->:'(EthernetFramesTxIn.internetProtocol == SW::InternetProtocol.IPV4 |
                                EthernetFramesTxIn.frameProtocol == SW::FrameProtocol.ARP,
                            MustSend(EthernetFramesTxOut) and EthernetFramesTxIn == EthernetFramesTxOut));
        }*/
    }

    // ----------------------------------------------------------------------------------------

    part def ArduPilot_i :> Periodic_Rust_Thread {
        port EthernetFramesRx: EventDataPort { in :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTx: EventDataPort { out :>> type : StructuredEthernetMessage_i; }
    }

    // ==========================================================================
    //   P r o c e s s e s
    // ==========================================================================

    part def LowLevelEthernetDriver_seL4_i :> Process {
        // Ethernet Frames: Raw Ethernet frames received from the physical network interface.
        port EthernetFramesRx: EventDataPort { out :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTx: EventDataPort { in :>> type : StructuredEthernetMessage_i; }

        part LowLevelEthernetDriver: LowLevelEthernetDriver_i;

        connection c1: PortConnection connect EthernetFramesTx to LowLevelEthernetDriver.EthernetFramesTx;
        connection c2: PortConnection connect LowLevelEthernetDriver.EthernetFramesRx to EthernetFramesRx;

        attribute :>> Domain = 2;
    }

    part def Firewall_seL4_i :> Process {
        port EthernetFramesRxIn:  EventDataPort { in :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTxIn:  EventDataPort { in :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesRxOut: EventDataPort { out :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTxOut: EventDataPort { out :>> type : StructuredEthernetMessage_i; }

        part Firewall: Firewall_i;

        connection c1: PortConnection connect EthernetFramesRxIn to Firewall.EthernetFramesRxIn;
        connection c2: PortConnection connect EthernetFramesTxIn to Firewall.EthernetFramesTxIn;
        connection c3: PortConnection connect Firewall.EthernetFramesRxOut to EthernetFramesRxOut;
        connection c4: PortConnection connect Firewall.EthernetFramesTxOut to EthernetFramesTxOut;

        attribute :>> Domain = 3;
    }

    part def ArduPilot_seL4_i :> Process {
        port EthernetFramesRx: EventDataPort { in :>> type : StructuredEthernetMessage_i; }
        port EthernetFramesTx: EventDataPort { out :>> type : StructuredEthernetMessage_i; }

        part ArduPilot: ArduPilot_i;

        connection c1: PortConnection connect EthernetFramesRx to ArduPilot.EthernetFramesRx;
        connection c2: PortConnection connect ArduPilot.EthernetFramesTx to EthernetFramesTx;

        attribute :>> Domain = 4;
    }

    // ==========================================================================
    //   P r o c e s s o r
    // ==========================================================================

    part def ZCU102_Processor :> Processor {
        attribute :>> Frame_Period = 1000[ms];
        attribute :>> Clock_Period = 2[ms];
    }

    // ==========================================================================
    //   T o p - L e v e l    S y s t e m
    // ==========================================================================

    part def seL4_i :> System {
        part proc: ZCU102_Processor;

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
