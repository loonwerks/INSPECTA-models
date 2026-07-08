# Temp-Control — System-Verification Example (SysML v2 → seL4 Microkit)

A minimal closed-loop thermostat used as a **system-verification** example for HAMR: a temperature
sensor, a temperature controller, and a fan, wired into a static cyclic schedule.  The point of the
model is not the control logic (which is deliberately simple) but the **system-level GUMBO
specification** and its discharge — statically by a Verus system proof and dynamically by a runtime
monitor.

The system runs a periodic loop each frame: the **sensor** publishes the current air temperature,
the **controller** compares it against the operator set point and decides the fan command, and the
**fan** actuates and acknowledges.  The controller keeps the fan **Off** below the set point's low
bound and **On** above its high bound.

This model is provided as a **SysML v2 model only** (there is no AADL variant).  HAMR codegen
targets **seL4 Microkit** with **user-land scheduling** and **Verus attribute syntax**; the
generated project lives under [`hamr/microkit_mcs/`](hamr/microkit_mcs/).

For build / simulate / verify instructions see **[sysml_readme.md](sysml_readme.md)**.

## Table of Contents
  * [System Architecture](#system-architecture)
  * [GUMBO Contracts](#gumbo-contracts)
    * [System-Level Properties](#system-level-properties)
  * [Mixed-Language and Trusted Components](#mixed-language-and-trusted-components)
  * [What's in this model](#whats-in-this-model)

---

## System Architecture

Three periodic (1000 ms) threads run in the fixed schedule order **sensor → control → fan**:

| Thread | Instance | Component | Language | Function |
|---|---|---|---|---|
| Temp Sensor | `sensor` (`tsp.tst`) | `tsp_tst` | **C** | Reads the (simulated) air temperature and publishes it on the outgoing `currentTemp` event-data port |
| Temp Control | `control` (`tcp.tct`) | `tcp_tct` | **Rust** | Consumes `currentTemp` and `fanAck`, and computes the `fanCmd` from the current temperature vs. the set point; carries the state variables `currentSetPoint`, `currentFanState`, `latestTemp`, `fanError` |
| Fan | `fan` (`fp.ft`) | `fp_ft` | **Rust** | Consumes `fanCmd`, actuates the fan, and returns `fanAck` |

Ports use **event-data** connections (the current-temperature reading is delivered on a single
`currentTemp` event-data port — the data value and its arrival event merged into one port).

---

## GUMBO Contracts

GUMBO (Grand Unified Modeling of Behavioral Operators) contracts are attached to the threads as
assume/guarantee pairs (`compute`) and port invariants (`integration`).  For example, `TempControl`
guarantees `TC_Req_01`/`TC_Req_02` — the fan is Off below the low set point and On above the high
set point — under its compute assumptions about the previously received values.

### System-Level Properties

The `composition nominal` block in
[`sysml/TempControl_SysVerif.sysml`](sysml/TempControl_SysVerif.sysml) names the participating
components (`sensor`/`control`/`fan`), the system-level ports and state variables it observes, and
the schedule order (`schema`), then states end-to-end assertions at scheduling points:

- **`Control_Invariant_Base`** *(abstract)* — a **loop invariant** carrying `TempControl`'s compute
  assumes (`a1`/`a2`) across the whole frame.  The predicates hold at `START`, are framed across the
  sensor and fan steps, are re-established by the controller's `TC_Req` guarantees, and close the
  loop (`END |- START`).  Every concrete property specializes it (`:>`) so it inherits the invariant
  and can discharge the controller's Pre-Assert.
- **`TC_Req_01 :> Control_Invariant_Base`** — if the temperature published by the sensor is below the
  set point's low bound, the fan state is `Off` (stated over the sensor's outgoing `currentTemp`
  port).
- **`TC_Req_02 :> Control_Invariant_Base`** — if the sensor's temperature is above the set point's
  high bound, the fan state is `On`.

(`TC_Req_03` — fan state maintained when the temperature is in range — needs a pre/post `In()`
reference not yet supported for system properties, and is deferred.)

These properties are discharged **statically** by the `sys_nominal_proof` crate (Verus verification
conditions) and can be checked **dynamically** by the runtime monitor
(see [sysml_readme.md](sysml_readme.md)).

---

## Mixed-Language and Trusted Components

This system deliberately mixes implementation languages: the controller and fan are **Rust**
(their GUMBO contracts are discharged by Verus with `cargo-verus`), while the sensor is **C**.

Because the sensor is not a Rust component, Verus cannot discharge its contract, so the system proof
**trusts** it: the sensor's guarantees are assumed rather than proven.  This trust is recorded
explicitly in the generated proof crate —
[`hamr/microkit_mcs/crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md`](hamr/microkit_mcs/crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md)
and its machine-readable `.json` companion, plus a greppable marker in
`src/trusted_assumptions.rs`.  A trusted C component's contract must be established by other means
(e.g. testing).  The proof-crate build re-announces the trusted contracts on every `make`.

---

## What's in this model

| Path | Contents |
|---|---|
| `sysml/TempControl_SysVerif.sysml` | The SysML v2 model: system structure, threads, GUMBO contracts, and the `composition nominal` system-level properties |
| `sysml/bin/run-hamr.cmd` | Sireum Slang driver that runs HAMR codegen for this model |
| `hamr/microkit_mcs/` | The generated seL4 Microkit project (user-land scheduling): `crates/` (Rust: `data`, `tcp_tct`, `fp_ft`, the runtime-monitor crates, and **`sys_nominal_proof`**), `components/` (per-thread support incl. the C sensor `tsp_tst`), `scheduler/` (the user-land scheduler), `types/`, `util/`, `Makefile`, `system.mk`, and the runtime-monitor build configs `gumbo_monitor.mk` / `sys_nominal_monitor.mk` / `userland_monitor.mk` |
