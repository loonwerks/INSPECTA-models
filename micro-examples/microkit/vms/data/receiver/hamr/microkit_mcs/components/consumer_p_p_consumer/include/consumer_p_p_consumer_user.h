/*
 * Copyright 2024, DornerWorks
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
#pragma once

#include <microkit.h>

// This file will not be overwritten if HAMR codegen is rerun

#if defined(BOARD_qemu_virt_aarch64)
// Where the guest sees its RAM. This must match the memory region the virtual
// machine's <map> element gives it in the system description.
#define GUEST_RAM_START_GPA       0x40000000
#define GUEST_RAM_SIZE            0x10000000
// Where the DTB and initial RAM disk are copied to within that RAM
#define GUEST_DTB_GPA             0x4f000000
#define GUEST_INIT_RAM_DISK_GPA   0x4d000000
#else
#error Need to define guest kernel image address and DTB address
#endif

#define MAX_IRQS 1

#if defined(BOARD_qemu_virt_aarch64)
#define SERIAL_IRQ_CH 1
#define SERIAL_IRQ 33
#else
#error Need to define IRQs
#endif

// Device IRQs passed through to the guest: the guest sees interrupt 'irq',
// and this component is notified on 'channel' when it fires.
struct mk_irq {
  int irq;
  microkit_channel channel;
};

struct mk_irq mk_irqs[MAX_IRQS] = {
  { // Serial
    .irq = SERIAL_IRQ,
    .channel = SERIAL_IRQ_CH
  }
};
