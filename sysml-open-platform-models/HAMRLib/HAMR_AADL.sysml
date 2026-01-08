package HAMR_AADL {
  private import AADL::*;
  private import HAMR;

  part def Processor :> AADL::Processor {
    attribute Frame_Period: Timing_Properties::Frame_Period;
    attribute Clock_Period: Timing_Properties::Clock_Period;
  }

  part def Process :> AADL::Process {
    attribute Domain: CASE_Scheduling::Domain;
  }

  part def Thread :> AADL::Thread {
    attribute Implementation_Language: HAMR::Implementation_Language;
  }

  part def Array :> Data {
    part Base_Type : Data;

    attribute Dimensions: ScalarValues::Positive;

    attribute Array_Size_Kind: HAMR::Array_Size_Kinds;
  }  
}
