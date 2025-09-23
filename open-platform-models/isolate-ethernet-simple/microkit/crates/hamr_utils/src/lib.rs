#![cfg_attr(not(test), no_std)]

use data::SW::{BufferDesc_Impl, BufferQueue_Impl, SW_BufferDescArray_DIM_0};

pub const QUEUE_SIZE: usize = 8;

pub struct QueuePair {
    pub avail: BufferQueue_Impl,
    pub free: BufferQueue_Impl,
}

pub const fn empty_buf_queue() -> BufferQueue_Impl {
    BufferQueue_Impl {
        head: 0,
        tail: 0,
        consumer_signalled: 0,
        buffers: [BufferDesc_Impl {
            index: 0,
            length: 0,
        }; SW_BufferDescArray_DIM_0],
    }
}

pub fn full(queue: &BufferQueue_Impl) -> bool {
    (queue.tail as usize - queue.head as usize) == QUEUE_SIZE
}

pub fn empty(queue: &BufferQueue_Impl) -> bool {
    (queue.tail as usize - queue.head as usize) == 0
}

pub fn enqueue(queue: &mut BufferQueue_Impl, buffer: BufferDesc_Impl) -> bool {
    if full(queue) {
        false
    } else {
        queue.buffers[queue.tail as usize % QUEUE_SIZE] = buffer;
        // TODO: Not needed until we support multicore
        // memory_release();
        let old_tail = queue.tail;
        queue.tail = old_tail + 1;
        true
    }
}

pub fn dequeue(queue: &mut BufferQueue_Impl) -> Option<BufferDesc_Impl> {
    if empty(queue) {
        None
    } else {
        let buffer = queue.buffers[queue.head as usize % QUEUE_SIZE];
        let old_head = queue.head;
        queue.head = old_head + 1;
        Some(buffer)
    }
}
