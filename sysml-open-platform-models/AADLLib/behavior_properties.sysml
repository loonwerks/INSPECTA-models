/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506/3 AADL annex D
 * OMG SysML v2 */
 
package Behavior_Properties {

    // Subprogram_Call_Protocol: enumeration (HSER,LSER,ASER) => HSER 
    //    applies to (subprogram access);
    // PDI001 Suggest to use Supported_*s instead of *_t
	enum def Supported_Subprogram_Call_Protocols {
		enum HSER; 
		enum LSER; 
		enum ASER;
	}
	alias HSER for Supported_Subprogram_Call_Protocols::HSER;
	alias LSER for Supported_Subprogram_Call_Protocols::LSER;
	alias ASER for Supported_Subprogram_Call_Protocols::ASER;
	attribute def Subprogram_Call_Protocol :> Supported_Subprogram_Call_Protocols;
	
	// attribute Subprogram_Call_Protocol : Subprogram_Call_Protocol_d 
	//	default Subprogram_Call_Protocol_t::HSER;
    // PDI003 Suggest to put default value in AADL.sysml
    // JH: That would be wrong. This property has to be imported explicitly. TBD when
    // we address user-defined AADL property sets/SysMLv2 attributes
}