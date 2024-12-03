/*
 * Copyright 2023, UNSW
 * Copyright 2024, DornerWorks
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
#include <stddef.h>
#include <stdint.h>
#include <microkit.h>
#include <libvmm/util/util.h>
#include <libvmm/arch/aarch64/vgic/vgic.h>
#include <libvmm/arch/aarch64/linux.h>
#include <libvmm/arch/aarch64/fault.h>
#include <libvmm/arch/aarch64/smc.h>
#include <libvmm/guest.h>
#include <libvmm/virq.h>
#include <libvmm/tcb.h>
#include <libvmm/vcpu.h>
#include "vmm_config.h"
#include "virtio/net.h"

/* Data for the guest's kernel image. */
extern char _guest_kernel_image[];
extern char _guest_kernel_image_end[];
/* Data for the device tree to be passed to the kernel. */
extern char _guest_dtb_image[];
extern char _guest_dtb_image_end[];
/* Data for the initial RAM disk to be passed to the kernel. */
extern char _guest_initrd_image[];
extern char _guest_initrd_image_end[];
/* Microkit will set this variable to the start of the guest RAM memory region. */
uintptr_t guest_ram_vaddr;

#define base_SW_RawEthernetMessage_Impl_SIZE 1600

typedef uint8_t base_SW_RawEthernetMessage_Impl [base_SW_RawEthernetMessage_Impl_SIZE];
bool get_EthernetFramesRx(base_SW_RawEthernetMessage_Impl *data);
bool put_EthernetFramesTx(const base_SW_RawEthernetMessage_Impl *data);

// Zynqmp has reserved IRQs: 129-135
#define VIRTIO_NET_IRQ (129)
#define VIRTIO_NET_BASE (0x150000)
#define VIRTIO_NET_SIZE (0x1000)

static struct virtio_net_device virtio_net;

static int get_dev_irq_by_ch(microkit_channel ch) {
    for(int i=0; i<MAX_IRQS; i++) {
        if (mk_irqs[i].channel == ch) {
            return mk_irqs[i].irq;
        }
    }

    return -1;
}

static int get_dev_ch_by_irq(int irq, microkit_channel *ch) {
    for(int i=0; i<MAX_IRQS; i++) {
        if (mk_irqs[i].irq == irq) {
            *ch = mk_irqs[i].channel;
            return 0;
        }
    }

    return -1;
}

static void pt_dev_ack(size_t vcpu_id, int irq, void *cookie) {
    /*
     * For now we by default simply ack the IRQ, we have not
     * come across a case yet where more than this needs to be done.
     */
    microkit_channel ch = 0;
    int status = get_dev_ch_by_irq(irq, &ch);
    if (!status) {
        microkit_irq_ack(ch);
    }
}



void seL4_ArduPilot_ArduPilot_initialize(void) {
    /* Initialise the VMM, the VCPU(s), and start the guest */
    LOG_VMM("starting \"%s\"\n", microkit_name);
    /* Place all the binaries in the right locations before starting the guest */
    size_t kernel_size = _guest_kernel_image_end - _guest_kernel_image;
    size_t dtb_size = _guest_dtb_image_end - _guest_dtb_image;
    size_t initrd_size = _guest_initrd_image_end - _guest_initrd_image;
    uintptr_t kernel_pc = linux_setup_images(guest_ram_vaddr,
                                      (uintptr_t) _guest_kernel_image,
                                      kernel_size,
                                      (uintptr_t) _guest_dtb_image,
                                      GUEST_DTB_VADDR,
                                      dtb_size,
                                      (uintptr_t) _guest_initrd_image,
                                      GUEST_INIT_RAM_DISK_VADDR,
                                      initrd_size
                                      );
    if (!kernel_pc) {
        LOG_VMM_ERR("Failed to initialise guest images\n");
        return;
    }
    /* Initialise the virtual GIC driver */
    bool success = virq_controller_init(GUEST_VCPU_ID);
    if (!success) {
        LOG_VMM_ERR("Failed to initialise emulated interrupt controller\n");
        return;
    }
    /* Initialise the SMC SIP Handler */
    success = smc_register_sip_handler(smc_sip_forward);
    if (!success) {
        LOG_VMM_ERR("Failed to initialise SMC SIP Handler\n");
        return;
    }
    /* Register Pass-through device IRQs */
    for(int i=0; i<MAX_IRQS; i++) {
        success = virq_register(GUEST_VCPU_ID, mk_irqs[i].irq, &pt_dev_ack, NULL);
        /* Just in case there are already interrupts available to handle, we ack them here. */
        microkit_irq_ack(mk_irqs[i].channel);
    }
    

    uint8_t mac[VIRTIO_NET_CONFIG_MAC_SZ] = {0x00, 0x0A, 0x35, 0x03, 0x78, 0xA1};
    success = virtio_mmio_net_init(&virtio_net,
                                  mac,
                                  base_SW_RawEthernetMessage_Impl_SIZE,
                                  VIRTIO_NET_BASE,
                                  VIRTIO_NET_SIZE,
                                  VIRTIO_NET_IRQ);

    if (!success) {
        LOG_VMM_ERR("Failed to initialise virtio_net\n");
        return;
    }
    
    /* Finally start the guest */
    guest_start(GUEST_VCPU_ID, kernel_pc, GUEST_DTB_VADDR, GUEST_INIT_RAM_DISK_VADDR);
}

void seL4_ArduPilot_ArduPilot_irqHandler(microkit_channel ch) {
    switch (ch) {
        // case A_CHANNEL: {
        //     /* Handle the channel notification */
        //     break;
        // }
        default: {
            int irq = get_dev_irq_by_ch(ch);
            if (irq < 0) {
                printf("Unexpected channel, ch: 0x%lx\n", ch);
            }
            else {
                bool success = virq_inject(GUEST_VCPU_ID, irq);
                if (!success) {
                    LOG_VMM_ERR("IRQ %d dropped on vCPU %d\n", irq, GUEST_VCPU_ID);
                }
            }
            break;
        }
    }
}


void vmm_virtio_net_tx(void *tx_buf) {
    LOG_VMM("Sending TX Message from guest\n");
    // LOG_VMM("TX Packet: ");
    // int i;
    // uint8_t* tx = tx_buf;

    // for(i=0; i<128; i++) {
    //     printf("%02x ", tx[i]);
    // }
    // printf("\n");
    put_EthernetFramesTx((base_SW_RawEthernetMessage_Impl *)tx_buf);
}

void seL4_ArduPilot_ArduPilot_timeTriggered(void) {
    // printf("Ardupilot: Time Triggered\n");
    // TODO: Implement API funcs <-> virtio-net backend translation
    base_SW_RawEthernetMessage_Impl rx;
    if (get_EthernetFramesRx(&rx)) {
        bool respond = virtio_net_handle_rx(&virtio_net, &rx, base_SW_RawEthernetMessage_Impl_SIZE);
        if (respond) {
             virtio_net_respond_to_guest(&virtio_net);
        }
        // int i;
        // LOG_VMM("Ardu: Rx Packet: ");

        // for(i=0; i<128; i++) {
        //     printf("%02x ", rx[i]);
        // }
        // printf("\n");
    }
}

/*
 * The primary purpose of the VMM after initialisation is to act as a fault-handler.
 * Whenever our guest causes an exception, it gets delivered to this entry point for
 * the VMM to handle.
 */
seL4_Bool fault(microkit_child child, microkit_msginfo msginfo, microkit_msginfo *reply_msginfo) {
    bool success = fault_handle(child, msginfo);
    if (success) {
        /* Now that we have handled the fault successfully, we reply to it so
         * that the guest can resume execution. */
        *reply_msginfo = microkit_msginfo_new(0, 0);
        return seL4_True;
    }

    return seL4_False;
}
