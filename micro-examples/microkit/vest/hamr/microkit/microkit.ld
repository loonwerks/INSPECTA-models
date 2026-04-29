/*
 * Copyright 2021, Breakaway Consulting Pty. Ltd.
 *
 * SPDX-License-Identifier: BSD-2-Clause
 *
 * Local override of the microkit SDK's default linker script
 * (located at $MICROKIT_SDK/board/$BOARD/$CONFIG/lib/microkit.ld).
 *
 * This file is placed in the build/ directory so that ld.lld finds it
 * before the SDK copy (the linker searches the current directory first,
 * then -L paths).
 *
 * Problem: Rust and Verus generate many small per-symbol sections with
 * names like .rodata..Lanon.<hash>.<n> and .data.<mangled_symbol>.
 * The SDK linker script uses exact-match patterns such as *(.rodata) and
 * *(.data), which do NOT match these subsections. The linker treats the
 * unmatched sections as orphans and emits each one as its own output
 * section. A typical Verus/Vest build produces ~1000 orphan .rodata.*
 * sections, pushing the ELF section count above ~1100. The microkit
 * tool's ELF parser (elf.rs) computes section header offsets as
 *     hdr.shoff + (i * hdr.shentsize) as u64
 * where both i and shentsize are u16. With shentsize=64, the product
 * overflows u16 (max 65535) at section index 1024 (1024*64 = 65536).
 * In a Rust release build the multiplication wraps silently, so all
 * section headers beyond index 1023 are read from wrong file offsets.
 * Because .shstrtab sits at index ~1150 in these large ELFs, the parser
 * never finds it and fails with "unable to find string table section".
 *
 * This u16 overflow is fixed in microkit SDK 2.1.0, which casts both
 * operands to u64 before multiplying (i as u64 * hdr.shentsize as u64).
 * Updating to SDK 2.1.0 or later would resolve the parsing failure and
 * eliminate the need for this custom linker script. However, the wildcard
 * patterns below are still beneficial even with SDK 2.1.0+: they reduce
 * the ELF from ~1100 sections to ~25, producing smaller binaries and
 * faster tool/loader processing.
 *
 * Why we are on SDK 1.4.1: HAMR codegen that targets domain scheduling
 * (seL4's kernel-level mechanism for deterministic temporal isolation,
 * where the kernel cycles through a fixed table of time domains) rather
 * than MCS user-land scheduling (budget/period) currently requires a
 * custom fork of the microkit SDK based on 1.4.1. The upstream SDK does
 * not yet expose domain scheduling through its system description format.
 * This pins us to a 1.4.1-era codebase that predates the u16 overflow
 * fix, making this custom linker script necessary for Rust/Verus builds.
 *
 * UNSW is working on mainlining domain scheduling support into the
 * upstream microkit SDK, but the 2.2.0 release (2025-03-31) does not
 * yet include it. Once domain scheduling lands upstream, upgrading to
 * that SDK version should both remove the need for the custom fork and
 * resolve the ELF parsing issue, making this file unnecessary.
 *
 * Note: projects that only use Vest's generated parse methods (e.g. the
 * ardupilot-basic MavlinkFirewall) may not hit this issue on SDK 1.4.1
 * because parsing operates on borrowed slices and pulls in less code.
 * The section count tends to exceed the 1024 threshold when the generated
 * serialize methods are also used, as they exercise Vec/alloc machinery
 * whose monomorphized implementations add many additional .rodata.*
 * and .data.* subsections.
 *
 * Fix: use wildcard patterns (*(.rodata*), *(.data*), etc.) so that all
 * subsections are merged into their parent output section. Lines that
 * differ from the original SDK linker script are marked with "// CHANGED".
 */
PHDRS
{
    text PT_LOAD;
    data PT_LOAD;
}

ENTRY(_start);

SECTIONS
{
    . = 0x200000;

    .text :
    {
        _text = .;
        *(.text.start)
        *(.text*)
        *(.rodata*)              /* CHANGED: was *(.rodata) */
        _text_end = .;
    } :text

    . = ALIGN(0x1000);

    /* For some reason the ABI puts init array
     * into writable memory, so we have to follow suit */
    .init_array :
    {
        PROVIDE(__init_array_start = .);
        KEEP (*(SORT(.init_array.*)))
        KEEP (*(.init_array*))
        PROVIDE(__init_array_end = .);
    } :data

    .data :
    {
        _data = .;
        *(.data*)                /* CHANGED: was *(.data) */
        . = ALIGN(8);
        __global_pointer$ = . + 0x800;
        *(.srodata*)             /* CHANGED: was *(.srodata) */
        *(.sdata*)               /* CHANGED: was *(.sdata) */
        _data_end = .;
    } :data

    .bss :
    {
        _bss = .;
        *(.sbss*)                /* CHANGED: was *(.sbss) */
        *(.bss)
        *(.bss*)
        *(COMMON)
        . = ALIGN(4);
        _bss_end = .;
    } :data

    . = ALIGN(0x1000);
    .ipc_buffer (NOLOAD): {
        __sel4_ipc_buffer_obj = .;
    }
}
