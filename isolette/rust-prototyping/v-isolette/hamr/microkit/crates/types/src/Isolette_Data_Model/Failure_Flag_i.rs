// Do not edit this file as it will be overwritten if codegen is rerun

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq)]
pub struct Failure_Flag_i {
    pub flag: bool,
}

impl Default for Failure_Flag_i {
    fn default() -> Self {
        Self { flag: false }
    }
}
