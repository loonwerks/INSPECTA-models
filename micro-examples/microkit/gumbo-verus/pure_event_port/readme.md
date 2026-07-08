# Pure Event Port + GUMBO Integration Constraints

Regression model for [hamr-tutorials BR-02](https://github.com/santoslab/hamr-tutorials/blob/main/bug-reports/BR-02-microkit-codegen-pure-eventport-integration-crash.md):
Microkit codegen halts with

```
java.lang.Error: Need to handle event ports
  at ...GumboRustPlugin.handleIntegrationConstraints
```

for any Rust thread that has a GUMBO `integration` section and also owns a
pure `EventPort` — even when no integration constraint mentions the event
port. `handleIntegrationConstraints` destructures every port's payload type
*before* checking whether the port actually carries an integration
constraint, so the pure event port (which has no payload) hits an
unconditional `halt`.

Integration constraints can never apply to pure event ports: they constrain
a port's payload value, and `GclResolver` additionally rejects event
predicates (e.g. `HasEvent`) in integration clauses. The halt therefore
guards an infeasible case and only needs to be moved behind the
constraint lookup.

## Model

`sysml/pure_event_port.sysml`: two periodic Rust threads connected by both a
`Base_Types::Integer_32` data port and a pure event port.

- `snd_t` — integration **guarantee** on the outgoing data port `out_val`;
  unconstrained pure event port `evt`
- `rcv_t` — integration **assume** on the incoming data port `in_val`;
  unconstrained pure event port `evt`

Either thread alone reproduces the halt; both sides are included to mirror
the original tutorial scenario ("Temp Control Mixed" with a `temp_changed`
event), where both endpoints carry integration constraints.

## Status

Fixed: `handleIntegrationConstraints` now inspects a port's payload type only
after looking it up in the integration-constraint map, so pure event ports are
skipped. Codegen succeeds, the generated APIs carry the data-port integration
contracts (`put_out_val` requires / `get_in_val` ensures the range constraint)
while the pure event port gets the standard uncontracted `get_evt() -> bool` /
`put_evt()` API, and `make verus` verifies all generated crates. This model
remains as the regression test for that behavior.
