/*
 * Copyright 2025, Proofcraft Pty Ltd
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */

#include <microkit.h>

void notified(microkit_channel ch) {
    microkit_dbg_puts("Worker 1 woken up!\n");
    /* ... */
    microkit_dbg_puts("Worker 1 finished work.\n");
}

void init() {
    /* nothing to do for the example */
}
