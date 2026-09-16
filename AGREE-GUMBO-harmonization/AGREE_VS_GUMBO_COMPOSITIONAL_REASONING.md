# AGREE vs. GUMBO Compositional Reasoning

## AGREE and GUMBO side by side

| Dimension | AGREE | GUMBO system specifications | Advantage |
|---|---|---|---|
| Primary abstraction | Synchronous dataflow over logical ticks. | Contract transitions over explicitly named schedule places. | Depends: AGREE for logical behavior; GUMBO for scheduled execution. |
| Composition | Hierarchical assume-guarantee reasoning. | Generated Hoare-style VCs over flattened proof state. | **AGREE** — composition is more direct and requires fewer supporting assertions. |
| Order sensitivity | Standard AGREE does not model physical dispatch order. See the scheduled-AGREE note below. | Distinguishes component execution order using a declared schedule schema. | **GUMBO** — schedule order is part of the current system-property language. |
| Property declaration | Parent guarantees and assumption-discharge obligations. | Concrete property plus supporting place assertions. | **AGREE** — system properties are generally more concise. |
| Contract reuse | Child contracts are consumed through the hierarchy; `lift contract` supports wrappers. | Leaf contracts are copied into the proof crate and projected separately for each property. | **AGREE** — contracts are reused directly across architectural levels. |
| Temporal reasoning | Native temporal operators and k-induction. | Supports frame invariants through `START` and `END`, but not general temporal operators. | **AGREE** — it provides broader temporal reasoning. |
| Proof engine | Lustre/JKind model checking and induction. | HAMR-generated Verus/SMT verification conditions. | Depends: AGREE for model checking; GUMBO for integration with Rust verification. |
| Failure feedback | Model-level counterexample traces. | Reports failed generated VCs; mapping failures back to the model is currently more manual. | **AGREE** — its counterexamples provide better diagnostic feedback. |
| Implementation path | Conformance of the lowest-level implementation to its contract is external to AGREE. | Rust component implementations can be verified against their generated GUMBO contracts. | **GUMBO** — it connects the model contracts to implementation verification. |
| Environmental assumptions | A parent assumption is treated as a premise supplied by the parent environment. | An `at START` assertion is a proof obligation; initialization must establish it, and `END` must imply it for later frames. | **AGREE** — environmental assumptions can be stated directly at the system boundary. |
| Intermediate facts | Derived internally from the composed child contracts and dataflow equations. | Must be exposed explicitly as assertions at the schedule places where they are needed. | **AGREE** — less manual proof plumbing is required. |
| Contract activation | Direct-child contracts participate in proving the parent contract. | A component contract participates in a property only when the property binds one of the component’s `after` places. | **AGREE** — contract participation is more automatic and predictable. |
| Hierarchical closure | A verified parent contract becomes a reusable abstraction at the next level. | The system proof consumes component contracts but does not automatically produce a reusable verified system contract for the next level. | **AGREE** — it supports hierarchical compositional reasoning directly. |

### Scheduled AGREE

The statement that AGREE does not model dispatch order applies to standard synchronous AGREE. The scheduled-AGREE extension introduces explicit dispatch and completion events, input-freezing rules, and a declared component schedule. However, it retains AGREE’s hierarchical assume-guarantee composition: upstream guarantees are used to establish downstream assumptions, and a verified subsystem can be abstracted by its system contract at the next level. Paper available here: https://loonwerks.com/publications/pdf/liu2022nfm.pdf

## What “compositional reasoning” means in each approach

Both AGREE and GUMBO use component contracts compositionally, but the composition occurs differently.

AGREE uses the assumptions and guarantees of a system’s direct subcomponents to prove the parent component’s assumptions and guarantees. Once the parent contract is proved, the internal subcomponents can be hidden and the parent contract can be reused at the next architectural level.

GUMBO applies a covered component’s contract locally at that component’s dispatch. The assertion immediately before the component must establish its compute assumptions, and the component’s guarantees, together with its write frame and the preceding assertion, must establish the assertion immediately after it. Facts needed later in the schedule must then be explicitly restated as place assertions.

Consequently, GUMBO’s system proof is compositional at individual schedule transitions, but the end-to-end reasoning is assertion-mediated: component contracts do not automatically form the direct hierarchical contract chain used by AGREE.

## Problems with GUMBO Compositional Reasoning

 1. The schedule in the model is ill-defined as follows:
   
     a. The generated schedule by HAMR is defined by the `Domain` attribute set in the model based on increasing order (i.e., smaller domains are run before larger domains)

     - **RESTRICTION:** Each component can only be placed once in the schedule.
     - **RESTRICTION:** The schedule will not be regenerated after the first HAMR generation.

     b. The `composition` clause has a schedule redefined in the `schema` section.

     - **RESTRICTION:** The only check that this `schema` matches the schedule occurs if the `runtime-monitoring` feature is enabled
 2. Each component, port, and state variable that is utilized in the GUMBO `composition` subclause MUST be assiegned an alias.
     - **RESTRICTION:** Cannot refer to components, ports, or states natively in property, which can unnecessarily inflate the `composition` subclause. Refer to below for an example.
       <p align="center"><img src="./Toy_Example/image4.png" width="250"></p>
 3. The GUMBO system proof is generated into a Verus proof alongside all the other HAMR artifacts. To verify, the user has to verify a specific rust crate within the generated code.
     - **RESTRICTION:** The GUMBO system proof is not restricted to the model level anaylsis (i.e., analyzed before a user moves to implementation).
 4. The structure of the model must follow HAMR expected structure, e.g., single thread to a single process.
     - **RESTRICTION:** Cannot use GUMBO to analyze models without HAMR targets.
 5. No counterexamples or guidance produced when VCs fail. See image below for an example.

     <p align="center"><img src="./Toy_Example/image.png" width="750"></p>
 6. GUMBO does not directly chain component contracts to prove a system property; each contract is used only to prove the assertions immediately before and after that component (using `before` and `after` assertions), and those assertions are then used to continue the proof through the schedule.
     - **RESTRICTION:** Contracts are duplicated or restated. Review below the AGREE and GUMBO system property side-by-side.
<table>
  <tr>
    <td><img src="./Toy_Example/image2.png" width="80%"></td>
    <td><img src="./Toy_Example/image3.png" width="100%"></td>
  </tr>
</table>


## Possible Solutions to Problems Above

 1. Define the schedule in SysMLv2, and use this for HAMR code generation and the schedule in the proof.
 2. Remove the alias restriction
 3. Enable true model-level GUMBO analysis (while retaining whats already been done) by:
   
    a. Enable system proof only generation from HAMR CLI
    b. Create VSCode plugin that simply verifies the model
    c. No longer generate the system proof in the regular HAMR output (to avoid confusion with user).
 4. For the system proof only generation body from HAMR CLI, allow non-HAMR conformed models.
 5. This CANNOT be solved as Verus does not give counterexamples.
 6. Directly utilize component contracts as assertions on the schedule. 

## What do we value most? Let's prioritize!
- Temporal Reasoning
- Counterexample explanation
- One supported contract language and verification workflow
- Proof scalability
- Model level verification (i.e., does not require code generation)
- Hierarchical structure such that verified subsystem contracts can be used by its parent
- Schedule-aware reasoning
- Control over the tool and its release roadmap
- Support for models outside HAMR’s expected architecture and deployment structure (i.e., outside of a single thread to a single process structure)