/*
 * Copyright 2024, UNSW (ABN 57 195 873 179)
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
#include <libvmm/virtio/config.h>
#include <libvmm/virtio/virtq.h>
#include <libvmm/virtio/mmio.h>
#include "net.h"
#include <libvmm/virq.h>
#include <libvmm/util/util.h>
// #include <sddf/network/queue.h>

/* Uncomment this to enable debug logging */
#define DEBUG_NET

#if defined(DEBUG_NET)
#define LOG_NET(...) do{ printf("VIRTIO(NET): "); printf(__VA_ARGS__); }while(0)
#else
#define LOG_NET(...) do{}while(0)
#endif

#define LOG_NET_ERR(...) do{ printf("VIRTIO(NET)|ERROR: "); printf(__VA_ARGS__); }while(0)


static inline struct virtio_net_device *device_state(struct virtio_device *dev)
{
    return (struct virtio_net_device *)dev->device_data;
}

static void virtio_net_reset(struct virtio_device *dev)
{
    LOG_NET("operation: reset\n");
    for (int i = 0; i < dev->num_vqs; i++) {
        dev->vqs[i].ready = false;
        dev->vqs[i].last_idx = 0;
    }
}

static bool driver_ok(struct virtio_device *dev)
{
    return (dev->data.Status & VIRTIO_CONFIG_S_DRIVER_OK) &&
           (dev->data.Status & VIRTIO_CONFIG_S_FEATURES_OK);
}

static bool virtio_net_get_device_features(struct virtio_device *dev, uint32_t *features)
{
    LOG_NET("operation: get device features\n");

    if (dev->data.Status & VIRTIO_CONFIG_S_FEATURES_OK) {
        LOG_NET_ERR("Driver tried to read device features after FEATURES_OK\n");
    }

    switch (dev->data.DeviceFeaturesSel) {
    /* Feature bits 0 to 31 */
    case 0:
        *features = BIT_LOW(VIRTIO_NET_F_MAC) | BIT_LOW(VIRTIO_NET_F_MTU);
        break;
    /* Features bits 32 to 63 */
    case 1:
        *features = BIT_HIGH(VIRTIO_F_VERSION_1);
        break;
    default:
        LOG_NET_ERR("Bad DeviceFeaturesSel 0x%x\n", dev->data.DeviceFeaturesSel);
        return false;
    }
    return true;
}


static bool virtio_net_set_driver_features(struct virtio_device *dev, uint32_t features)
{
    bool success = true;
    LOG_NET("data.DriverFeaturesSel: %lu\n", dev->data.DriverFeaturesSel);
    LOG_NET("features: 0x%x\n", features);

    switch (dev->data.DriverFeaturesSel) {
    /* Feature bits 0 to 31 */
    case 0:
        /** F_MAC is required */
        // success = (features == (BIT_LOW(VIRTIO_NET_F_MAC) | BIT_LOW(VIRTIO_NET_F_MTU)));
        success = (features == BIT_LOW(VIRTIO_NET_F_MAC));
        break;

    /* Features bits 32 to 63 */
    case 1:
        success = (features == BIT_HIGH(VIRTIO_F_VERSION_1));
        break;

    default:
        LOG_NET_ERR("Bad DriverFeaturesSel 0x%x\n", dev->data.DriverFeaturesSel);
        success = false;
    }
    if (success) {
        dev->data.features_happy = 1;
    }
    return success;
}

static bool virtio_net_get_device_config(struct virtio_device *dev,
                                         uint32_t offset,
                                         uint32_t *ret_val)
{
    struct virtio_net_config *config = &device_state(dev)->config;

    uint32_t word_offset = (offset - REG_VIRTIO_MMIO_CONFIG) / sizeof(uint32_t);
    LOG_NET("device config: %lu\n", word_offset);
    switch (word_offset) {
    case 0:
        *ret_val = config->mac[0];
        *ret_val |= config->mac[1] << 8;
        *ret_val |= config->mac[2] << 16;
        *ret_val |= config->mac[3] << 24;
        break;

    case 1:
        *ret_val = config->mac[4];
        *ret_val |= config->mac[5] << 8;
        break;

    case 2:
        *ret_val = (uint32_t)config->mtu;
        break;

    default:
        LOG_NET_ERR("Unknown device config register: 0x%x\n", offset);
        return false;
    }
    return true;
}

static bool virtio_net_set_device_config(struct virtio_device *dev, uint32_t offset, uint32_t val)
{
    LOG_NET_ERR("All configuration fields are readonly\n");
    return false;
}

static void virtq_enqueue_used(struct virtq *virtq, uint32_t desc_head, uint32_t bytes_written)
{
    struct virtq_used_elem *used_elem = &virtq->used->ring[virtq->used->idx % virtq->num];
    used_elem->id = desc_head;
    used_elem->len = bytes_written;
    virtq->used->idx++;
}

static bool virtio_net_respond(struct virtio_device *dev)
{
    dev->data.InterruptStatus = BIT_LOW(0);
    bool success = virq_inject(GUEST_VCPU_ID, dev->virq);
    assert(success);

    return success;
}

static void handle_tx_msg(struct virtio_device *dev,
                          struct virtq *virtq,
                          uint16_t desc_head,
                          bool *respond_to_guest)
{
    struct virtio_net_device *state = device_state(dev);
    struct virtio_net_config *config = &state->config;

    // if (net_queue_full_active(&state->tx)) {
    //     goto fail;
    // }

    // net_buff_desc_t sddf_buffer;
    // int error = net_dequeue_free(&state->tx, &sddf_buffer);
    // if (error) {
    //     goto fail;
    // }

    // void *dest_buf = state->tx_data + sddf_buffer.io_or_offset;

    uint32_t written = 0;
    uint32_t dest_remaining = config->mtu;

    /* Strip virtio header before copying to sDDF */
    uint32_t skip_remaining = sizeof(struct virtio_net_hdr_mrg_rxbuf);

    struct virtq_desc *desc = &virtq->desc[desc_head];

    while (dest_remaining > 0) {
        uint32_t skipping = 0;
        /* Work out how much of this descriptor must be skipped */
        skipping = MIN(skip_remaining, desc->len);
        /* Truncate packets that are large than BUF_SIZE */
        uint32_t writing = MIN(dest_remaining, desc->len - skipping);

        // memcpy(dest_buf + written, (void *)desc->addr + skipping, writing);

        vmm_virtio_net_tx((void *)desc->addr + skipping);

        skip_remaining -= skipping;
        written += writing;
        dest_remaining -= writing;

        if (desc->flags & VIRTQ_DESC_F_NEXT) {
            desc = &virtq->desc[desc->next];
        } else {
            break;
        }
    }

    // sddf_buffer.len = written;
    // error = net_enqueue_active(&state->tx, sddf_buffer);
    /* This cannot fail as we check above */
    // assert(!error);

    virtq_enqueue_used(virtq, desc_head, written);
    *respond_to_guest = true;
    // *notify_tx_server = true;
    return;
}

static bool virtio_net_queue_notify(struct virtio_device *dev)
{
    // struct virtio_net_device *state = device_state(dev);

    if (!driver_ok(dev)) {
        LOG_NET_ERR("Driver not ready\n");
        return false;
    }
    if (dev->data.QueueSel != VIRTIO_NET_TX_VIRTQ) {
        LOG_NET_ERR("Invalid queue\n");
        return false;
    }
    if (!dev->vqs[VIRTIO_NET_TX_VIRTQ].ready) {
        LOG_NET_ERR("TX virtq not ready\n");
        return false;
    }

    virtio_queue_handler_t *vq = &dev->vqs[VIRTIO_NET_TX_VIRTQ];
    struct virtq *virtq = &vq->virtq;

    uint16_t guest_idx = virtq->avail->idx;
    uint16_t idx = vq->last_idx;

    // bool notify_tx_server = false;
    bool respond_to_guest = false;

    for (; idx != guest_idx; idx++) {
        uint16_t desc_head = virtq->avail->ring[idx % virtq->num];
        handle_tx_msg(dev, virtq, desc_head, &respond_to_guest);
    }

    vq->last_idx = idx;

    // if (notify_tx_server && net_require_signal_active(&state->tx)) {
    //     net_cancel_signal_active(&state->tx);
    //     microkit_notify(state->tx_ch);
    // }

    bool success = true;
    if (respond_to_guest) {
        success = virtio_net_respond(dev);
    }

    return success;
}

static uint32_t copy_rx(struct virtq *virtq,
                        uint16_t *curr_desc_head,
                        uint32_t *desc_copied,
                        const void *buf, uint32_t size)
{
    uint32_t copied = 0;
    do {
        uint32_t copying = MIN(size - copied, virtq->desc[*curr_desc_head].len - *desc_copied);

        memcpy((void *)virtq->desc[*curr_desc_head].addr + *desc_copied, buf + copied, copying);

        copied += copying;
        *desc_copied += copying;

        if (*desc_copied == virtq->desc[*curr_desc_head].len) {
            if (!(virtq->desc[*curr_desc_head].flags & VIRTQ_DESC_F_NEXT)) {
                break;
            }
            *curr_desc_head = virtq->desc[*curr_desc_head].next;
            *desc_copied = 0;
        }
    } while (copied < size);

    return copied;
}

static void handle_rx_buffer(struct virtio_device *dev,
                             void *buf, uint32_t size,
                             bool *respond_to_guest)
{
    virtio_queue_handler_t *vq = &dev->vqs[VIRTIO_NET_RX_VIRTQ];
    struct virtq *virtq = &vq->virtq;

    uint16_t guest_idx = virtq->avail->idx;
    uint16_t idx = vq->last_idx;

    if (idx == guest_idx) {
        /* vq is full or not initialised, drop the packet */
        return;
    }

    /* Read the head of the descriptor chain */
    uint16_t desc_head = virtq->avail->ring[idx % virtq->num];
    uint16_t curr_desc_head = desc_head;

    uint32_t copied = 0;
    /* Amount of the current descriptor copied */
    uint32_t desc_copied = 0;

    struct virtio_net_hdr_mrg_rxbuf virtio_hdr = {0};
    virtio_hdr.num_buffers = 1;

    copied += copy_rx(virtq, &curr_desc_head, &desc_copied, &virtio_hdr, sizeof(struct virtio_net_hdr_mrg_rxbuf));
    copied += copy_rx(virtq, &curr_desc_head, &desc_copied, buf, size);

    /* Put it in the used ring */
    virtq_enqueue_used(virtq, desc_head, copied);

    /* Record that we've used this descriptor chain now */
    vq->last_idx++;

    *respond_to_guest = true;
}

bool virtio_net_handle_rx(struct virtio_net_device *state, void *buf, uint32_t size)
{
    struct virtio_device *dev = &state->virtio_device;

    if (!driver_ok(dev)) {
        return false;
    }
    if (!dev->vqs[VIRTIO_NET_RX_VIRTQ].ready) {
        /* vq is not initialised, drop the packet */
        return false;
    }

    // net_buff_desc_t sddf_buffer;
    // bool reprocess = true;
    bool respond_to_guest = false;
    
    handle_rx_buffer(dev, buf, size, &respond_to_guest);


    // if (respond_to_guest) {
    //     return virtio_net_respond(dev);
    // }

    return respond_to_guest;
}

void virtio_net_respond_to_guest(struct virtio_net_device *state) {
    struct virtio_device *dev = &state->virtio_device;
    virtio_net_respond(dev);
}

static virtio_device_funs_t functions = {
    .device_reset = virtio_net_reset,
    .get_device_features = virtio_net_get_device_features,
    .set_driver_features = virtio_net_set_driver_features,
    .get_device_config = virtio_net_get_device_config,
    .set_device_config = virtio_net_set_device_config,
    .queue_notify = virtio_net_queue_notify,
};

bool virtio_mmio_net_init(struct virtio_net_device *net_dev,
                          uint8_t mac[VIRTIO_NET_CONFIG_MAC_SZ],
                          uint16_t mtu,
                          uintptr_t region_base,
                          uintptr_t region_size,
                          size_t virq)
{
    struct virtio_device *dev = &net_dev->virtio_device;

    dev->data.DeviceID = DEVICE_ID_VIRTIO_NET;
    dev->data.VendorID = VIRTIO_MMIO_DEV_VENDOR_ID;
    dev->funs = &functions;
    dev->vqs = net_dev->vqs;
    dev->num_vqs = VIRTIO_NET_NUM_VIRTQ;
    dev->virq = virq;
    dev->device_data = net_dev;

    memcpy(net_dev->config.mac, mac, VIRTIO_NET_CONFIG_MAC_SZ);
    net_dev->config.mtu = mtu;

    return virtio_mmio_register_device(dev, region_base, region_size, virq);
}
