/*
 * Copyright 2025, Proofcraft Pty ltd
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */

 #include <stdbool.h>
 #include <microkit.h>

void notify_loop() {
    while (true) {
        microkit_notify(0);
        seL4_Yield();
    }
}

void notified(microkit_channel ch) {
    /* unreachable */
}

void init() {
    notify_loop();
}
