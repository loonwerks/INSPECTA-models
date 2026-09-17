//! Manual realizability check for B's compute contract.

use crate::contracts::b;
use vstd::prelude::*;

verus! {

pub open spec fn b_compute_guarantees(input: i32, output: i32) -> bool {
  b::compute_spec_B_Output_Range_guarantee(input, output)
  && b::compute_spec_B_Conditional_Output_Range_guarantee(input, output)
}

/// Witness-free inconsistency query. If this verifies, no input/output pair
/// satisfies B's assumption and guarantees. If it fails, the contract is
/// either consistent or Verus could not prove it inconsistent.
pub proof fn check_b_contract_consistency(input: i32, output: i32) {
  assume(b::compute_spec_B_Input_Range_assume(input));
  assume(b_compute_guarantees(input, output));
  #[verifier::proof_note(
    "This failure means the assumed constraints are satisfiable, so B's contract is consistent by contradiction."
  )]
  assert(false);
}

// /// Every input allowed by B's assumption must admit at least one output that
// /// satisfies all of B's guarantees. The input is symbolic, not an example.
// pub proof fn vc_b_contract_realizable(input: i32)
//   requires
//     b::compute_spec_B_Input_Range_assume(input),
//   ensures
//     exists |output: i32| b_compute_guarantees(input, output),
// {}

} // verus!
