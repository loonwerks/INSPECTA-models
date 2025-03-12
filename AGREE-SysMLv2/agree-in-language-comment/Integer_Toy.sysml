package Integer_Toy {
	import AADL::*;
	import AADL_Project::*;

	language "agree" /*
		fun timesOne(i : int) : int = i * 1;
	*/

	part def A :> System {
		in port Input : DataPort { in :> type : ScalarValues::Integer; }
		out port Output : DataPort { out :> type : ScalarValues::Integer; }
		language "agree" /*
			assume "A input range" : Input < 20;
			guarantee "A output range" : Output < 2*Input;
		*/
	}

	part def B :> System {
		in port Input : DataPort { in :> type : ScalarValues::Integer; }
		out port Output : DataPort { out :> type : ScalarValues::Integer; }
		language "agree" /* 
			assume "B input range" : Input < 20;
			guarantee "B output range" : Output < Input + 15;
		*/
	}

	part def C :> System {
		in port Input1 : DataPort { in :> type : ScalarValues::Integer; }
		in port Input2 : DataPort { in :> type : ScalarValues::Integer; }
		out port Output : DataPort { out :> type : ScalarValues::Integer; }

		language "agree" /* 
	  		eq mode : int;
	  
	  		guarantee "mode always is increasing" : mode >= 0 -> mode > pre(mode);
			guarantee "C output range" : Output = if mode = 3 then (Input1 + Input2) else 0;
		*/
	}

	part def top_level :> System {

		in port Input : DataPort { in :> type : ScalarValues::Integer; }
		out port Output : DataPort { out :> type : ScalarValues::Integer; }
		
		language "agree" /* 
	  		eq mode : int;
			assume "System input range " : Input < 10;
			guarantee "mode is always positive" : mode >= 0;
			guarantee "System output range" : Output < 50;
		*/
	}

	part def 'top_level.Impl' :> top_level {
		part a : Integer_Toy::A;
		part b : Integer_Toy::B;
		part c : Integer_Toy::C;

		connection IN_TO_A : PortConnection connect Input to a.Input;
		connection A_TO_B : PortConnection connect a.Output to b.Input;
		connection A_TO_C : PortConnection connect a.Output to c.Input1;
		connection B_TO_C : PortConnection connect b.Output to c.Input2;
		connection C_TO_OUTPIT : PortConnection connect c.Output to Output;
		language "agree" /*
		assign mode = c.mode;
		*/
	}
}