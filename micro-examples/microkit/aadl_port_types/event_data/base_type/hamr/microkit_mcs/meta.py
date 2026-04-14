# Copyright 2025, UNSW
# SPDX-License-Identifier: BSD-2-Clause
import argparse
import struct
from random import randint
from dataclasses import dataclass
from typing import List, Tuple, Optional
from sdfgen import SystemDescription, Sddf, DeviceTree, LionsOs
from importlib.metadata import version

# This file will not be overwritten if HAMR codegen is rerun

assert version('sdfgen').split(".")[1] == "27", "Unexpected sdfgen version"

from sdfgen_helper import *

ProtectionDomain = SystemDescription.ProtectionDomain
MemoryRegion = SystemDescription.MemoryRegion
Map = SystemDescription.Map
Channel = SystemDescription.Channel

@dataclass
class Board:
    name: str
    arch: SystemDescription.Arch
    paddr_top: int
    serial: str
    timer: str
    ethernet: str
    i2c: Optional[str]


BOARDS: List[Board] = [
    Board(
        name="qemu_virt_aarch64",
        arch=SystemDescription.Arch.AARCH64,
        paddr_top=0x6_0000_000,
        serial="pl011@9000000",
        timer="timer",
        ethernet="virtio_mmio@a003e00",
        i2c=None,
    ),
]

def schedule(*entries):
    """
    entries: sequence of (channel, timeslice_ns, is_user_partition)
    """
    part_ch, part_timeslices, is_user_partition = zip(*entries)
    return UserSchedule(list(part_timeslices), list(part_ch), list(is_user_partition))

# Virtual address at which the schedule state shared memory region is mapped
# in the scheduler (rw) and in every _MON protection domain (r).
# Must match SCHED_STATE_VADDR / SCHED_STATE_SIZE in scheduler_config.h.
SCHED_STATE_VADDR = 0x4_000_000
SCHED_STATE_SIZE  = 0x1000  # 4 KB

def generate(sdf_path: str, output_dir: str, dtb: DeviceTree):
    timer_node = dtb.node(board.timer)
    assert timer_node is not None

    timer_driver = ProtectionDomain("timer_driver", "timer_driver.elf", priority=201)
    timer_system = Sddf.Timer(sdf, timer_node, timer_driver)

    scheduler = ProtectionDomain("scheduler", "scheduler.elf", priority=200)

    #######################################
    # SCHEDULE STATE
    # Broadcast region written by the scheduler before every dispatch.
    # The runtime monitor maps this region read-only to observe which
    # protection domain last yielded and which will be dispatched next.
    #######################################
    sched_state_mr = MemoryRegion(sdf, "sched_state", SCHED_STATE_SIZE)
    sdf.add_mr(sched_state_mr)
    scheduler.add_map(Map(sched_state_mr, SCHED_STATE_VADDR, perms="rw"))

    # BEGIN META MARKER

    #######################################
    # PARTITION PROTECTION DOMAINS
    #######################################
    producer_p_p1_producer_MON = ProtectionDomain("producer_p_p1_producer_MON", "producer_p_p1_producer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(producer_p_p1_producer_MON)
    producer_p_p2_producer_MON = ProtectionDomain("producer_p_p2_producer_MON", "producer_p_p2_producer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(producer_p_p2_producer_MON)
    consumer_p_p_consumer_MON = ProtectionDomain("consumer_p_p_consumer_MON", "consumer_p_p_consumer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(consumer_p_p_consumer_MON)
    consumer_p_s_consumer_MON = ProtectionDomain("consumer_p_s_consumer_MON", "consumer_p_s_consumer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(consumer_p_s_consumer_MON)

    producer_p_p1_producer = ProtectionDomain("producer_p_p1_producer", "producer_p_p1_producer.elf", priority=140, passive=True)
    scheduler.add_child_pd(producer_p_p1_producer)
    producer_p_p2_producer = ProtectionDomain("producer_p_p2_producer", "producer_p_p2_producer.elf", priority=140, passive=True)
    scheduler.add_child_pd(producer_p_p2_producer)
    consumer_p_p_consumer = ProtectionDomain("consumer_p_p_consumer", "consumer_p_p_consumer.elf", priority=140, passive=True)
    scheduler.add_child_pd(consumer_p_p_consumer)
    consumer_p_s_consumer = ProtectionDomain("consumer_p_s_consumer", "consumer_p_s_consumer.elf", priority=140, passive=True)
    scheduler.add_child_pd(consumer_p_s_consumer)

    #######################################
    # MEMORY REGIONS
    #######################################
    top_impl_Instance_producer_p_p1_producer_write_port_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p1_producer_write_port_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p1_producer_write_port_1_Memory_Region)
    top_impl_Instance_producer_p_p2_producer_write_port_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p2_producer_write_port_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p2_producer_write_port_1_Memory_Region)

    producer_p_p1_producer.add_map(Map(top_impl_Instance_producer_p_p1_producer_write_port_1_Memory_Region, 0x10_000_000, perms="rw"))
    consumer_p_p_consumer.add_map(Map(top_impl_Instance_producer_p_p1_producer_write_port_1_Memory_Region, 0x10_000_000, perms="r"))
    consumer_p_s_consumer.add_map(Map(top_impl_Instance_producer_p_p1_producer_write_port_1_Memory_Region, 0x10_000_000, perms="r"))
    producer_p_p2_producer.add_map(Map(top_impl_Instance_producer_p_p2_producer_write_port_1_Memory_Region, 0x10_000_000, perms="rw"))
    consumer_p_p_consumer.add_map(Map(top_impl_Instance_producer_p_p2_producer_write_port_1_Memory_Region, 0x10_001_000, perms="r"))
    consumer_p_s_consumer.add_map(Map(top_impl_Instance_producer_p_p2_producer_write_port_1_Memory_Region, 0x10_001_000, perms="r"))

    #######################################
    # CHANNELS
    #######################################
    channel_producer_p_p1_producer_MON = 2
    channel_producer_p_p2_producer_MON = 3
    channel_consumer_p_p_consumer_MON = 4
    channel_consumer_p_s_consumer_MON = 5

    sdf.add_channel(Channel(scheduler, producer_p_p1_producer_MON, a_id=channel_producer_p_p1_producer_MON, b_id=0))
    sdf.add_channel(Channel(producer_p_p1_producer_MON, producer_p_p1_producer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, producer_p_p2_producer_MON, a_id=channel_producer_p_p2_producer_MON, b_id=0))
    sdf.add_channel(Channel(producer_p_p2_producer_MON, producer_p_p2_producer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, consumer_p_p_consumer_MON, a_id=channel_consumer_p_p_consumer_MON, b_id=0))
    sdf.add_channel(Channel(consumer_p_p_consumer_MON, consumer_p_p_consumer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, consumer_p_s_consumer_MON, a_id=channel_consumer_p_s_consumer_MON, b_id=0))
    sdf.add_channel(Channel(consumer_p_s_consumer_MON, consumer_p_s_consumer, a_id=1, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_pad = (0, 800000000, False)
    ts_producer_p_p1_producer_MON = (channel_producer_p_p1_producer_MON, 50000000, True)
    ts_producer_p_p2_producer_MON = (channel_producer_p_p2_producer_MON, 50000000, True)
    ts_consumer_p_p_consumer_MON = (channel_consumer_p_p_consumer_MON, 50000000, True)
    ts_consumer_p_s_consumer_MON = (channel_consumer_p_s_consumer_MON, 50000000, True)

    user_schedule = schedule(
      ts_pad,
      ts_producer_p_p1_producer_MON,
      ts_producer_p_p2_producer_MON,
      ts_consumer_p_p_consumer_MON,
      ts_consumer_p_s_consumer_MON
    )

    # END META MARKER

    sdf.add_pd(timer_driver)
    sdf.add_pd(scheduler)
    timer_system.add_client(scheduler)

    assert timer_system.connect()
    assert timer_system.serialise_config(output_dir)

    data_path = output_dir + "/schedule_config.data"
    with open(data_path, "wb+") as f:
        f.write(user_schedule.serialise())
    update_elf_section(obj_copy, scheduler.program_image,
                       user_schedule.section_name,
                       data_path)

    with open(f"{output_dir}/{sdf_path}", "w+") as f:
        f.write(sdf.render())


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--dtb", required=True)
    parser.add_argument("--sddf", required=True)
    parser.add_argument("--board", required=True, choices=[b.name for b in BOARDS])
    parser.add_argument("--output", required=True)
    parser.add_argument("--sdf", required=True)
    parser.add_argument("--objcopy", required=True)

    args = parser.parse_args()

    # Import the config structs module from the build directory
    sys.path.append(args.output)
    from config_structs import *

    board = next(filter(lambda b: b.name == args.board, BOARDS))

    sdf = SystemDescription(board.arch, board.paddr_top)
    sddf = Sddf(args.sddf)

    global obj_copy
    obj_copy = args.objcopy

    with open(args.dtb, "rb") as f:
        dtb = DeviceTree(f.read())

    generate(args.sdf, args.output, dtb)
