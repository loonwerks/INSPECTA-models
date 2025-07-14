/*
 * Copyright 2023, UNSW
 * Copyright 2024, DornerWorks
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
#include <stddef.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>
#include <libvmm/util/util.h>
#include <libvmm/arch/aarch64/vgic/vgic.h>
#include <libvmm/arch/aarch64/linux.h>
#include <libvmm/arch/aarch64/fault.h>
#include <libvmm/arch/aarch64/smc.h>
#include <libvmm/guest.h>
#include <libvmm/virq.h>
#include <libvmm/tcb.h>
#include <libvmm/vcpu.h>
#include "sb_aadl_types.h"
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
bool put_TxData(const SW_EthernetMessages *data);
bool put_RxQueueFree(const SW_BufferQueue_Impl *data);
bool put_TxQueueAvail(const SW_BufferQueue_Impl *data);
bool RxQueueAvail_is_empty(void);
bool get_RxQueueAvail_poll(sb_event_counter_t *numDropped, SW_BufferQueue_Impl *data);
bool get_RxQueueAvail(SW_BufferQueue_Impl *data);
bool TxQueueFree_is_empty(void);
bool get_TxQueueFree_poll(sb_event_counter_t *numDropped, SW_BufferQueue_Impl *data);
bool get_TxQueueFree(SW_BufferQueue_Impl *data);
bool get_RxData(SW_EthernetMessages *data);

// Outputs
volatile SW_BufferQueue_Impl TxQueueAvail;
volatile SW_BufferQueue_Impl RxQueueFree;
extern volatile SW_EthernetMessages *TxData_queue_1;

// Inputs
volatile SW_BufferQueue_Impl TxQueueFree;
volatile SW_BufferQueue_Impl RxQueueAvail;
extern volatile SW_EthernetMessages *RxData_queue_1;

bool full (volatile SW_BufferQueue_Impl *queue, volatile SW_BufferQueue_Impl *other_queue) {
    return !((queue->tail + 1 - other_queue->head) % QUEUE_SIZE);
}

bool empty (volatile SW_BufferQueue_Impl *queue, volatile SW_BufferQueue_Impl *other_queue) {
    return !((queue->tail - other_queue->head) % QUEUE_SIZE);
}

int enqueue(volatile SW_BufferQueue_Impl *queue, volatile SW_BufferQueue_Impl *other_queue, SW_BufferDesc_Impl buffer) {
    if (full(queue, other_queue)) return -1;
    queue->buffers[queue->tail] = buffer;
    // TODO: Not needed until we support multicore
    // memory_release();
    queue->tail++;
    return 0;
}

int dequeue(volatile SW_BufferQueue_Impl *queue, volatile SW_BufferQueue_Impl *other_queue, SW_BufferDesc_Impl *buffer) {
    if (empty(queue, other_queue)) return -1;
    *buffer = queue->buffers[other_queue->head];
    // TODO: Not needed until we support multicore
    // memory_release();
    other_queue->head++;
    return 0;
}

int tx_avail_enqueue(SW_BufferDesc_Impl buffer) {
    return enqueue(&TxQueueAvail, &TxQueueFree, buffer);
}

int tx_free_dequeue(SW_BufferDesc_Impl *buffer) {
    return dequeue(&TxQueueFree, &TxQueueAvail, buffer);
}

int rx_free_enqueue(SW_BufferDesc_Impl buffer) {
    return enqueue(&RxQueueFree, &RxQueueAvail, buffer);
}

int rx_avail_dequeue(SW_BufferDesc_Impl *buffer) {
    return dequeue(&RxQueueAvail, &RxQueueFree, buffer);
}

void rx_free_init() {
    for( int i = 0; i<QUEUE_SIZE; i++) {
        SW_BufferDesc_Impl buffer = {.index = i, .length = base_SW_RawEthernetMessage_Impl_SIZE};
        rx_free_enqueue(buffer);
    }
}

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
    rx_free_init();
    
    /* Finally start the guest */
    guest_start(GUEST_VCPU_ID, kernel_pc, GUEST_DTB_VADDR, GUEST_INIT_RAM_DISK_VADDR);
}

void seL4_ArduPilot_ArduPilot_notify(microkit_channel ch) {
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

// static uint8_t tx_idx = 0;

void vmm_virtio_net_tx(void *tx_buf) {

    SW_BufferDesc_Impl buffer;
    int err = tx_free_dequeue(&buffer);
    if(!err) {
        buffer.length = base_SW_RawEthernetMessage_Impl_SIZE;
        memcpy((void *)TxData_queue_1[buffer.index], tx_buf, buffer.length);
        tx_avail_enqueue(buffer);
        put_TxQueueAvail((const SW_BufferQueue_Impl*) &TxQueueAvail);
    }
    else {
        LOG_VMM("No Bufs available. Dropped a Tx packet from guest\n");
    }
    // TODO: When do we copy?

    // // LOG_VMM("Sending TX Message from guest\n");
    // switch (tx_idx) {
    //     case 0:
    //         put_EthernetFramesTx0((base_SW_RawEthernetMessage_Impl *)tx_buf);
    //         break;
    //     case 1:
    //         put_EthernetFramesTx1((base_SW_RawEthernetMessage_Impl *)tx_buf);
    //         break;
    //     case 2:
    //         put_EthernetFramesTx2((base_SW_RawEthernetMessage_Impl *)tx_buf);
    //         break;
    //     case 3:
    //         put_EthernetFramesTx3((base_SW_RawEthernetMessage_Impl *)tx_buf);
    //         break;
    // }
    // tx_idx = (tx_idx + 1) % 4; 
    // LOG_VMM("TX Packet: ");
    // int i;
    // uint8_t* tx = tx_buf;

    // for(i=0; i<128; i++) {
    //     printf("%02x ", tx[i]);
    // }
    // printf("\n");
}

// bool get_EthernetFramesRx(uint8_t idx, base_SW_RawEthernetMessage_Impl *data) {
//     bool avail = false;
//     switch (idx) {
//         case 0:
//             avail = !EthernetFramesRx0_is_empty();
//             if (avail) {
//                 get_EthernetFramesRx0(data);
//             }
//             return avail;
//         case 1:
//             avail = !EthernetFramesRx1_is_empty();
//             if (avail) {
//                 get_EthernetFramesRx1(data);
//             }
//             return avail;
//         case 2:
//             avail = !EthernetFramesRx2_is_empty();
//             if (avail) {
//                 get_EthernetFramesRx2(data);
//             }
//             return avail;
//         case 3:
//             avail = !EthernetFramesRx3_is_empty();
//             if (avail) {
//                 get_EthernetFramesRx3(data);
//             }
//             return avail;
//         default:
//             return false;
//     }
// }


void seL4_ArduPilot_ArduPilot_timeTriggered(void) {
    // printf("Ardupilot: Time Triggered\n");
    // TODO: Implement API funcs <-> virtio-net backend translation

    get_TxQueueFree((SW_BufferQueue_Impl *) &TxQueueFree);
    get_RxQueueAvail((SW_BufferQueue_Impl *) &RxQueueAvail);
    SW_BufferDesc_Impl buffer;
    while(!rx_avail_dequeue(&buffer)) {
        bool respond = virtio_net_handle_rx(&virtio_net, (void *) &RxData_queue_1[buffer.index], buffer.length);
        if (respond) {
             virtio_net_respond_to_guest(&virtio_net);
        }
        rx_free_enqueue(buffer);
    }
    put_RxQueueFree((const SW_BufferQueue_Impl *) &RxQueueFree);
    // base_SW_RawEthernetMessage_Impl rx;
    // for(int i = 0; i < 4; i++){
    //     if (get_EthernetFramesRx(i, &rx)) {
    //         bool respond = virtio_net_handle_rx(&virtio_net, &rx, base_SW_RawEthernetMessage_Impl_SIZE);
    //         if (respond) {
    //              virtio_net_respond_to_guest(&virtio_net);
    //         }
    //         // int i;
    //         // LOG_VMM("Ardu: Rx Packet: ");

    //         // for(i=0; i<128; i++) {
    //         //     printf("%02x ", rx[i]);
    //         // }
    //         // printf("\n");
    //     }
    // }
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
