package Integer_Toy_AADL_2_SysMLV2_trail1{
    
    private import ScalarValues::*;
    private import AGREE::*;
    
    part def Top_Level{
        in input : Integer; // I have modeled these as direction al reference properties BUT perhaps a better way would be to use ports and direcitonal refernece propreties on those ports
        out output : Integer;
        attribute mode : Integer;
        comment about mode /* I have captured mode as an integer here but it might make more sense as a state machine? */

        // I have defined this statement in the context of Top_Level but it could easily be elsewhere
        #agree satisfy requirement test{
            assume constraint 'System Input Range' {input > 10}
            require constraint 'Mode is always positive' {mode >= 0}
            require constraint 'System Output Range' {output > 0} // I am using the "require" keyword here. I think that we could redefine this to  "guarantee" or add a new one.
        }
    }
    
    part def A{
        in input [1] : Integer; // adding multiplicity here as an example to drive conversation
        out output [1] : Integer;
        
        #agree satisfy requirement test{
            assume constraint 'A input range' {input < 20}
            require constraint 'A output range' {output < 2*input}
        }
    }
    
    part def B{
        in input : Integer;
        out output : Integer;
        
        #agree satisfy requirement test{
            assume constraint 'B input range' {input < 20}
            assume constraint 'B output range' {output < input + 15}
        }
    }
    
    part def C{
        in input1 : Integer;
        in input2 : Integer;
        out output : Integer;
        attribute mode : Integer;
        
        #agree satisfy requirement test{
            //assume nothing
            require constraint 'mode is always increasing' {(mode > 0) and (mode > pre(mode))} // not sure how to express this yet...
            require constraint 'C output range' {if mode == 3? output == (input1 + input2) else output == 0}
        }
    }
    
    part 'top_level.Impl' : Top_Level{
        part a_sub : A;
        part b_sub : B;
        part c_sub : C;
        
        flow from 'top_level.Impl'.input to a_sub.input;
        connect a_sub to b_sub;
        connect a_sub to c_sub;
        connect b_sub to c_sub;
        flow from c_sub.output to 'top_level.Impl'.output;
        comment about 'top_level.Impl' /* I am not sure if we should use connectors or flows here */
    }
}