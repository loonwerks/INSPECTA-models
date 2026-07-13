# R2U2 C monitor, vendored for seL4 Microkit (HAMR --sel4-aux-code-dirs)

Source: https://github.com/R2U2/r2u2 `monitors/c/src` (engine only; `cli/` dropped).
License: MIT or Apache-2.0.

Deviations from upstream, applied directly in the copied sources:

1. `r2u2/internals/config.h` -- accept freestanding platforms
   (`__STDC_HOSTED__ == 0`) in the platform compatibility check.
2. `r2u2/lib.c` -- `r2u2_load_string_signal` is a no-op when freestanding
   (needs hosted sscanf); use the bool/int/float loaders.
3. `r2u2/memory/monitor.h` -- freestanding fallback declarations for
   `FILE`/`fprintf` when `<stdio.h>` resolves to an empty stub (e.g. sDDF's
   `custom_libc` under the user-land/MCS build).
4. `r2u2/engines/mltl.c` -- parenthesized two `&&`-within-`||` conditions
   (no semantic change); the HAMR Microkit build uses `-Werror` with
   `-Wlogical-op-parentheses`.
5. `r2u2/internals/process_binary.c` -- removed an unused loop variable
   (`-Werror` with `-Wunused-variable`).

Deviations 1-3 are conditional on freestanding compilation and are marked
in-source with a `seL4 Microkit / bare-metal patch` comment; 4-5 are
unconditional warning fixes suitable for upstreaming as-is.

`support/` provides minimal freestanding stand-ins for the hosted headers the
engine includes (`assert.h`, `stdio.h`, `string.h`, `math.h`) plus stub
implementations (`fprintf` -- never called since `monitor.out_file` stays NULL;
naive `pow`/`sqrt` -- only reachable if a spec uses those operators).
`memcpy`/`memset` come from the HAMR-generated `util.c` under domain scheduling
and from sDDF's util library under user-land/MCS scheduling.
