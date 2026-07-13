# R2U2 runtime monitor for the temperature sensor

This directory holds the inputs for the R2U2-based runtime assurance of the C temperature
sensor (`tsp_tst`).  See the [R2U2 runtime assurance](../../../sysml_readme.md#r2u2-runtime-assurance-for-the-c-sensor)
section of the model's readme for background and build/run instructions.

Both subdirectories are passed to HAMR codegen via `--sel4-aux-code-dirs` (together with
`--sel4-aux-code-symlink`); codegen symlinks them into `../aux_code/` and wires them into
the generated build (compile rules, include paths, and linking into the C component ELFs).
Without the symlink flag, codegen instead copies their `.c`/`.h` files into `../aux_code/`.

- [`r2u2-lib/`](r2u2-lib/) — the vendored [R2U2](https://github.com/R2U2/r2u2) C monitor
  engine plus freestanding support shims.  See its [README.md](r2u2-lib/README.md) for the
  (small, documented) deviations from upstream.
- [`sensor-spec/`](sensor-spec/) — the monitor specification:
  - [`sensor.c2po`](sensor-spec/sensor.c2po) / [`sensor.map`](sensor-spec/sensor.map) —
    the MLTL specification of the sensor's `Sensor_Temperature_Range` GUMBO contract and
    its signal mapping (C2PO sources; not copied into the build).
  - `sensor_spec.bin` — the specification compiled by C2PO (not copied into the build).
  - `sensor_spec.h` — `sensor_spec.bin` as an embeddable byte array (`xxd -i`); this is
    what the sensor's user code loads into the monitor.

## Regenerating the specification

After editing `sensor.c2po`, recompile with [C2PO](https://github.com/R2U2/r2u2)
(requires Python 3):

```sh
python3 <r2u2-repo>/compiler/c2po.py --spec sensor.c2po --map sensor.map -bz -o sensor_spec.bin
xxd -i -n sensor_spec_bin sensor_spec.bin > sensor_spec.h
```

C2PO gotchas encountered (R2U2 commit circa July 2026):

- The assembler rejects negative integer immediates (`x >= -40` crashes it); rewrite as
  `x + 40 >= 0`.
- A *named* implication formula (`name: p => q;`) is compiled as a three-verdict
  assume-guarantee contract; use `name: !p || q;` for a single verdict stream.
