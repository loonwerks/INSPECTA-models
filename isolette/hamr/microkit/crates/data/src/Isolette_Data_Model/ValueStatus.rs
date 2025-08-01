// Do not edit this file as it will be overwritten if codegen is rerun

use vstd::prelude::*;

use super::*;

verus! {
  #[repr(C)]
  #[derive(Copy, Clone, Debug, PartialEq, Eq, Structural)]
  pub enum ValueStatus {
    pub Valid = 0,
    pub Invalid = 1,
  }

  impl Default for ValueStatus {
    fn default() -> Self
    {
      ValueStatus::Valid
    }
  }
}