# `above_top_contracts` proof walkthrough

This crate verifies the concrete GUMBO property `System_Output_Range` for the
flattened schedule

```text
top_a -> top_b -> top_c -> outer_a
```

The schedule comes from the `above_top_contracts` schema in
[`Integer_Toy_Extended.sysml`](../../../../sysml/Integer_Toy_Extended.sysml#L191-L245).

The short answer to "what is the top-level call?" is:

```text
make system_output_range
  -> cargo-verus verify --verify-module ...
  -> Verus independently checks VC[0] through VC[10]
```

Within those obligations, [`vc_init_state`](src/system_output_range/vc_init.rs#L21-L28)
is the first proof function and
[`vc_post_pre`](src/system_output_range/vc_post_pre.rs#L21-L27) is the last. They
are endpoints in the generated logical ordering; `vc_init_state` does **not**
call `vc_post_pre`, directly or indirectly.

## The most important point: there is no top-level proof function

No Rust proof function calls `vc_init_state`, which then calls the sequential
VCs. The entry point is the verification command, not a Rust function:

- `make all` invokes `cargo-verus verify` for the entire crate
  ([`Makefile`](Makefile#L32-L35)).
- `make system_output_range` asks Verus to verify only the four modules belonging
  to this property ([`Makefile`](Makefile#L45-L48)).

Verus discovers the modules through ordinary Rust `pub mod` declarations. It
then verifies every `pub proof fn` in the selected modules independently. Each
generated proof function has an empty body, so the obligation is simply:

```text
requires ==> ensures
```

The functions do not execute, and they do not call one another. The logical
chain exists because the conclusion of one VC is the assertion used as a premise
at the next schedule place.

## Verification-discovery tree

```text
make all
  |
  `-- cargo-verus verify
        |
        `-- src/lib.rs
              |
              |-- shared definitions
              |     |-- system_state.rs
              |     |-- contracts.rs
              |     |-- write_frames.rs
              |     |-- actions.rs
              |     |-- vc_integration.rs
              |     `-- vc_commutativity.rs
              |
              `-- system_output_range/mod.rs
                    |-- assertions.rs
                    |-- vc_init.rs
                    |     `-- VC[0] vc_init_state
                    |-- vc_sequential.rs
                    |     |-- VC[1] vc_pre_assert_top_a
                    |     |-- VC[2] vc_next_assert_task_top_a
                    |     |-- VC[3] vc_pre_assert_top_b
                    |     |-- VC[4] vc_next_assert_task_top_b
                    |     |-- VC[5] vc_pre_assert_top_c
                    |     |-- VC[6] vc_next_assert_task_top_c
                    |     |-- VC[7] vc_pre_assert_outer_a
                    |     |-- VC[8] vc_next_assert_task_outer_a
                    |     `-- VC[9] vc_next_assert_skip_t4
                    |-- vc_post_pre.rs
                    |     `-- VC[10] vc_post_pre
                    `-- vc_non_disabling.rs (no VCs for this linear schema)
```

The crate root declares the shared modules and the property module in
[`src/lib.rs`](src/lib.rs#L26-L40). The property module declares its assertion
and VC modules in
[`src/system_output_range/mod.rs`](src/system_output_range/mod.rs#L3-L7).

## What is flattened

The proof does not treat `top_sub` as an already-verified subsystem. The schema
names its internal leaf threads directly, so the proof contains four leaf
transitions:

1. `top_a` - `top_sub.A_sub.a`
2. `top_b` - `top_sub.B_sub.b`
3. `top_c` - `top_sub.C_sub.c`
4. `outer_a` - `A_sub.a`

The generated `SystemState` therefore contains the connection values needed by
all four leaf components
([`src/system_state.rs`](src/system_state.rs#L12-L25)):

```text
system_input  -- input read by top_a
top_a_output  -- written by top_a; read by top_b and top_c
top_b_output  -- written by top_b; read by top_c
top_output    -- written by top_c; read by outer_a
system_output -- written by outer_a
```

Connected input and output aliases become the same state field. For example,
`top_b_input` and `top_c_input1` both resolve to `top_a_output`.

## Inputs to every VC

Every schedule-step VC combines three kinds of generated predicates:

1. **Place assertions.** These are the predicates written at `START`, after a
   component, or at `END`. They are defined in
   [`src/system_output_range/assertions.rs`](src/system_output_range/assertions.rs#L17-L69).
2. **Component contracts.** The component compute assumptions, compute
   guarantees, and initialize guarantees are copied into
   [`src/contracts.rs`](src/contracts.rs#L15-L119).
3. **Write frames.** A component may change its own output, while every other
   `SystemState` field must remain unchanged. These predicates are defined in
   [`src/write_frames.rs`](src/write_frames.rs#L15-L89).

The system proof uses the copied component contracts as premises. It does not
verify the component implementations in this crate; component conformance is a
separate verification obligation.

## The logical proof chain

Although the functions do not call one another, the complete proof can be read
in the following order.

### VC[0]: establish `START`

[`vc_init_state`](src/system_output_range/vc_init.rs#L21-L28) assumes all
relevant initialize guarantees and proves the `START` assertion.

The useful premise is nested A's initialize guarantee:

```text
a.initialize guarantee
    system_input < 10

therefore

START
    system_input < 10
```

The initialize contract is defined in
[`src/contracts.rs`](src/contracts.rs#L52-L59), and the resulting `START`
predicate is defined in
[`src/system_output_range/assertions.rs`](src/system_output_range/assertions.rs#L17-L23).

### VC[1]: `START` establishes `top_a`'s precondition

[`vc_pre_assert_top_a`](src/system_output_range/vc_sequential.rs#L21-L27)
proves:

```text
system_input < 10 ==> system_input < 50
```

The conclusion is the compute assumption on A from
[`src/contracts.rs`](src/contracts.rs#L61-L68).

### VC[2]: firing `top_a` establishes `after top_a`

[`vc_next_assert_task_top_a`](src/system_output_range/vc_sequential.rs#L29-L38)
assumes:

- the `START` assertion;
- A's write frame; and
- A's guarantee `top_a_output < 2 * system_input`.

It proves the complete `after top_a` assertion
([`assertions.rs`](src/system_output_range/assertions.rs#L25-L33)):

```text
system_input < 10
top_a_output < 2 * system_input
top_a_output < 20
```

A's frame preserves `system_input` while allowing `top_a_output` to change
([`write_frames.rs`](src/write_frames.rs#L34-L50)).

### VC[3]: `after top_a` establishes `top_b`'s precondition

[`vc_pre_assert_top_b`](src/system_output_range/vc_sequential.rs#L40-L46)
uses `top_a_output < 20` to prove B's compute assumption.

This is where the fact written as `b_input < 20` in GUMBO appears in the
flattened proof as `top_a_output < 20`.

### VC[4]: firing `top_b` establishes `after top_b`

[`vc_next_assert_task_top_b`](src/system_output_range/vc_sequential.rs#L48-L57)
uses B's guarantee:

```text
top_b_output < top_a_output + 15
```

and B's frame to establish the `after top_b` assertion
([`assertions.rs`](src/system_output_range/assertions.rs#L35-L43)). B may change
only `top_b_output`, so `system_input` and `top_a_output` are carried forward
unchanged ([`write_frames.rs`](src/write_frames.rs#L53-L69)).

### VC[5]: check `top_c`'s precondition

[`vc_pre_assert_top_c`](src/system_output_range/vc_sequential.rs#L59-L65)
has the conclusion `true` because C has no compute assumption.

### VC[6]: firing `top_c` establishes `after top_c`

[`vc_next_assert_task_top_c`](src/system_output_range/vc_sequential.rs#L67-L76)
uses C's guarantee:

```text
top_output == top_a_output + top_b_output
```

together with the earlier correlated bounds. Because all values are integers:

```text
system_input < 10                         gives system_input <= 9
top_a_output < 2 * system_input           gives top_a_output <= 17
top_b_output < top_a_output + 15          gives top_b_output <= 31
top_output == top_a_output + top_b_output gives top_output <= 48
```

Therefore Verus proves `top_output < 50`, which is the `after top_c` assertion
([`assertions.rs`](src/system_output_range/assertions.rs#L45-L52)).

### VC[7]: `after top_c` establishes `outer_a`'s precondition

[`vc_pre_assert_outer_a`](src/system_output_range/vc_sequential.rs#L78-L84)
uses `top_output < 50` to prove outer A's input assumption.

### VC[8]: firing `outer_a` establishes the system result

[`vc_next_assert_task_outer_a`](src/system_output_range/vc_sequential.rs#L86-L95)
uses outer A's guarantee:

```text
system_output < 2 * top_output
```

Since `top_output < 50`, Verus proves `system_output < 100`. This is the actual
system result, recorded in the `after outer_a` assertion
([`assertions.rs`](src/system_output_range/assertions.rs#L54-L61)).

### VC[9]: move from `after outer_a` to `END`

[`vc_next_assert_skip_t4`](src/system_output_range/vc_sequential.rs#L97-L103)
is a control-point transition: no component fires and the state is unchanged.
It proves the weaker `END` assertion, `system_input < 10`, from the stronger
`after outer_a` assertion.

### VC[10]: close the hyperperiod loop

[`vc_post_pre`](src/system_output_range/vc_post_pre.rs#L21-L27) proves:

```text
END:   system_input < 10
  ==>
START: system_input < 10
```

This is the final generated VC. It closes the induction from one schedule frame
to the next.

## The complete proof, in one formula

The generated files collectively establish the following concrete chain. Here,
`pre` is the state before a component dispatch and `post` is the state after
that dispatch.

```text
VC[0] initialization
  a initialize guarantee:       system_input < 10
  outer_a initialize guarantee: top_output < 10
  ==> START: system_input < 10

VC[1] enable top_a
  START: system_input < 10
  ==> top_a compute assumption: system_input < 50

VC[2] dispatch top_a
  START: pre.system_input < 10
  + top_a compute guarantee:
      post.top_a_output < 2 * pre.system_input
  + top_a frame:
      post.system_input == pre.system_input
  ==> after_top_a:
      post.system_input < 10
      post.top_a_output < 2 * post.system_input
      post.top_a_output < 20

VC[3] enable top_b
  after_top_a: top_a_output < 20
  ==> top_b compute assumption: top_a_output < 20

VC[4] dispatch top_b
  after_top_a
  + top_b compute guarantee:
      post.top_b_output < pre.top_a_output + 15
  + top_b frame:
      post.system_input == pre.system_input
      post.top_a_output == pre.top_a_output
  ==> after_top_b:
      post.system_input < 10
      post.top_a_output < 2 * post.system_input
      post.top_b_output < post.top_a_output + 15

VC[5] enable top_c
  after_top_b
  ==> top_c compute assumption: true

VC[6] dispatch top_c
  after_top_b
  + top_c compute guarantee:
      post.top_output == pre.top_a_output + pre.top_b_output
  + top_c frame:
      post.system_input == pre.system_input
      post.top_a_output == pre.top_a_output
      post.top_b_output == pre.top_b_output
  ==> after_top_c:
      post.system_input < 10
      post.top_output < 50

VC[7] enable outer_a
  after_top_c: top_output < 50
  ==> outer_a compute assumption: top_output < 50

VC[8] dispatch outer_a
  after_top_c
  + outer_a compute guarantee:
      post.system_output < 2 * pre.top_output
  + outer_a frame:
      post.system_input == pre.system_input
      post.top_output == pre.top_output
  ==> after_outer_a:
      post.system_input < 10
      post.system_output < 100

VC[9] control-point transition
  after_outer_a
  ==> END: system_input < 10

VC[10] close the hyperperiod
  END: system_input < 10
  ==> START: system_input < 10 for the next frame
```

The exact generated guarantee predicates are in
[`src/contracts.rs`](src/contracts.rs#L19-L44) for outer A,
[`src/contracts.rs`](src/contracts.rs#L52-L77) for nested A,
[`src/contracts.rs`](src/contracts.rs#L85-L101) for B, and
[`src/contracts.rs`](src/contracts.rs#L109-L117) for C.

Passing all eleven VCs is what constitutes the property proof. There is no
additional master theorem or final function call after `vc_post_pre`.

## What schedule reasoning occurs in this crate

The schema is linear, so this crate proves the assertion chain only for:

```text
top_a -> top_b -> top_c -> outer_a
```

There are no `split` branches and therefore no concurrently enabled transition
pairs. Consequently:

- [`src/vc_commutativity.rs`](src/vc_commutativity.rs#L21-L24) contains no
  commutativity VCs; and
- [`src/system_output_range/vc_non_disabling.rs`](src/system_output_range/vc_non_disabling.rs#L24-L27)
  contains no assertion-preservation VCs.

Thus, this proof crate verifies the property along the declared schema. It does
not execute the scheduler, and there is no single schedule function being called
by the proof.

## One modeling detail to keep in mind

`system_input < 10` is proved initially from A's initialize guarantee, then
preserved because none of the four component write frames permits
`system_input` to change, and finally carried through `END ==> START`. An
environment update to `system_input` between frames is not represented in this
proof chain.
