// This file will not be overwritten if HAMR codegen is rerun

//! The scheduler smoke tests codegen seeds tests.rs with.

use crate::system_tests::api;
use crate::{system_tests, sys_assert, sys_assert_eq};

system_tests! {
  suite smoke {
    fn schedule_advances_by_hyperperiod() {
      let before = api::info_state();
      let after = api::hstep(1);
      sys_assert!(after.flags & api::FLAG_BAD_COMMAND == 0);
      sys_assert_eq!(after.hyperperiod_num, before.hyperperiod_num + 1);
    }

    fn stepping_one_slot_at_a_time_wraps() {
      let n = api::schedule().num_timeslices;
      let before = api::info_state();
      let mut i = 0;
      while i < n {
        let _ = api::sstep(1);
        i += 1;
      }
      let after = api::info_state();
      sys_assert_eq!(after.current_timeslice, before.current_timeslice);
      sys_assert_eq!(after.hyperperiod_num, before.hyperperiod_num + 1);
    }
  }
}
