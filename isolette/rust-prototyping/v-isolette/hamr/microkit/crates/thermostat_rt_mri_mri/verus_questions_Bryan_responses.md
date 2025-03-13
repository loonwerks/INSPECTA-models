
- how to use the verus command line tool to generate a static lib that can be used in a microkit system (e.g. includes sel4-logging [logging.rs](./src/logging.rs), targets ``aarch64-unknown-none``, builds ``core``, etc as seen in the [Makefile](./Makefile) ``all`` target)
    - The [verified-node-replication project](https://github.com/verus-lang/verified-node-replication/tree/main/verified-node-replication)might be a good guide for this.  For complicated projects, we generally use Verus to verify the code, and then cargo to handle the build.  Eventually `cargo verus build` should be able to handle both (see below).  When using normal `cargo build`, I would suggest putting your options in your Cargo.toml file, like [this one](https://github.com/verus-lang/verified-node-replication/blob/main/verified-node-replication/Cargo.toml) 

- where is documentation on the verus option ``--crate-type`` used [here](./Makefile#L22) (and any other hidden options?)?
    - This is actually [a `rustc` argument](https://doc.rust-lang.org/rustc/command-line-arguments.html#--crate-type-a-list-of-types-of-crates-for-the-compiler-to-emit) that Verus just passes along.

- verus doesn't support f32 or f64, but does it have an arbitrary precision 'real' type?
    - No, we don't currently support floats or reals.  They've been low priority thus far, since none of our systems/applications have needed them.  Historically, when we tried to reason about reals in Dafny, we found the resulting SMT queries to be extremely flaky, so that's another reason we've steered clear of them so far.   

- is there a way to get a detailed (but high level, i.e. not smt scripts) log of what has been verified via the verus command line tool (e.g. something like "verified ensures statement at line 31 in file.rs; verified assert statement at line 50 ...")
    - Hmm, no that's not something we emit at present.  For each function, Verus computes a single verification condition (this is mostly true, but oversimplifies) that it feeds to Z3.  The verification condition incorporates all of the proof obligations inside that function (e.g., that all preconditions on operations and calls to other functions are met), as well as the ultimate obligation that the ensures clauses hold.  The query is instrumented with additional "location variables", that help us identify where errors occurred.  In theory, you could add some functionality to list those locations, if you wanted to give a more detailed description of what was verified as part of verifying the entire function.  Most of the time, however, we're happy to know a function verified, and only when it doesn't verify do we care about the specifics of which part failed (and we care a lot of about the part that failed, much less about what succeed).

- it appears verus doesn't support comparing enum values via == and != in exec code, but does support the operators in specs.  See [this issue](./src/component/manage_regulator_interface_app.rs#L194).
    - As discussed in the [Verus tutorial](https://verus-lang.github.io/verus/guide/equality.html), the challenge is that for user-defined types, `eq` could have extra behaviors: it might have side effects, behave nondeterministically, or fail to fulfill its promise to implement an equivalence relation, even if the type implements the Rust [`Eq` trait](https://doc.rust-lang.org/std/cmp/trait.Eq.html)
    - However, for cases where it's straightforward to rule that out, you can derive the `Structural` trait, which will allow your example to go through:
        ```
        #[derive(Structural, Debug, Clone, Copy, PartialEq, Eq)]
        enum ValueStatus {
          Valid = 0,
          Invalid = 1
        }


        fn test(status: ValueStatus) {
            if !(status == ValueStatus::Valid) {

            }
        }
        ```

- why does verus require a static lib to have a main method [here](./src/lib.rs#L26)
    - If you want to verify a library, just pass `--crate-type=lib` to Verus, and then it (really meaning `rustc`) shouldn't complain about missing a main function.

- can verus handle cargo path dependencies.  E.g. it would be preferable to have a common rust 'types' package [here](../types/) as used in the non-verus version of the [mhs](../thermostat_rt_mhs_mhs/Cargo.toml#L13) and the [mri](../thermostat_rt_mri_mri/Cargo.toml#L13).  The current work-around is to copy the types into each component's rust package (e.g. for the verus version [here](./src/types/))
    - You can do this using `--export` and `--import` directives, although it's a bit tedious.  We have [active work](https://github.com/verus-lang/verus/pull/1475)on actual cargo integration, so that you could simply run `cargo verus verify` or `cargo verus build`.
