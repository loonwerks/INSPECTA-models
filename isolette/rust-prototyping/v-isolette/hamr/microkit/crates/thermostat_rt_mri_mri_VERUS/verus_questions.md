- how to use the verus command line tool to generate a static lib that can be used in a microkit system (e.g. includes sel4-logging [logging.rs](./src/logging.rs), targets ``aarch64-unknown-none``, builds ``core``, etc as seen in the [Makefile](./Makefile) ``all`` target)

- where is documentation on the verus option ``--crate-type`` used [here](./Makefile#L22) (and any other hidden options?)?

- verus doesn't support f32 or f64, but does it have an arbitrary precision 'real' type?

- is there a way to get a detailed (but high level, i.e. not smt scripts) log of what has been verified via the verus command line tool (e.g. something like "verified ensures statement at line 31 in file.rs; verified assert statement at line 50 ...")

- it appears verus doesn't support comparing enum values via == and != in exec code, but does support the operators in specs.  See [this issue](./src/component/manage_regulator_interface_app.rs#L194).

- why does verus require a static lib to have a main method [here](./src/lib.rs#L26)

- can verus handle cargo path dependencies.  E.g. it would be preferable to have a common rust 'types' package [here](../types/) as used in the non-verus version of the [mhs](../thermostat_rt_mhs_mhs/Cargo.toml#L13) and the [mri](../thermostat_rt_mri_mri/Cargo.toml#L13).  The current work-around is to copy the types into each component's rust package (e.g. for the verus version [here](./src/types/))