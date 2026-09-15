# AGREE vs. GUMBO Compositional Reasoning

## AGREE and GUMBO side by side

| Dimension | AGREE | GUMBO system specifications |
|---|---|---|
| Primary abstraction | Synchronous dataflow over logical ticks. | Contract transitions over explicitly named schedule places. |
| Composition | Hierarchical assume-guarantee reasoning. | Generated Hoare-style VCs over flattened proof state. |
| Order sensitivity | Does not model physical dispatch order. | Can distinguish before/after dispatches in the declared schema. |
| Property declaration | Parent guarantee or assumption discharge. | Concrete property plus supporting place assertions. |
| Contract reuse | Child contracts consumed through hierarchy; `lift contract` supports wrappers. | Leaf contracts copied into the proof crate and projected per property. |
| Temporal reasoning | Native temporal operators and k-induction. | No native temporal reasoning. |
| Proof engine | Lustre/JKind model checking and induction. | HAMR-generated Verus/SMT VCs. |
| Failure feedback | Model-level counterexample traces. | Failed generated VC; current mapping is more manual. |
| Implementation path | Lowest-level implementation conformance is external. | Rust code can be verified against generated contracts; unsupported/non-Rust code remains trusted. |
| Best fit here | Compact proof of synchronous safety relationships. | A focused claim whose requirement explicitly depends on dispatch order. |


## Problems with GUMBO Compositional Reasoning

 1. The schedule in the model is ill-defined as follows:
   
     a. The generated schedule by HAMR is defined by the `Domain` attribute set in the model based on increasing order (i.e., smaller domains are run before larger domains)

     - RESTRICTION: Each component can only be placed once in the schedule.
     - RESTRICTION: The schedule will not be regenerated after the first HAMR generation.

     b. The `composition` clause has a schedule redefined in the `schema` section.

     - RESTRICTION: The only check that this `schema` matches the schedule occurs if the `runtime-monitoring` feature is enabled
 2. The GUMBO system proof is generated with all the other HAMR artifacts. To verify, the user has to verify a specific rust crate within the generated code.
     - RESTRICTION: The GUMBO system proof is not restricted to the model level anaylsis (i.e., analyzed before a user moves to implementation).
     - NOTE: The system proof is written in Verus proof.
 3. The structure of the model must follow HAMR expected structure, e.g., single thread to a single process.
     - RESTRICTION: Cannot use GUMBO to analyze models without HAMR targets.

## Possible Solutions to Problems Above

 1. Define the schedule in SysMLv2, and use this for HAMR code generation and the schedule in the proof.
 2. Enable true model-level GUMBO analysis (while retaining whats already been done) by:
   
    a. Enable system proof only generation from HAMR CLI
    b. Create VSCode plugin that simply verifies the model
    c. No longer generate the system proof in the regular HAMR output (to avoid confusion with user).
3. For the system proof only generation body from HAMR CLI, allow non-HAMR conformed models.