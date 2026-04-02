# Copyright 2025, UNSW
# SPDX-License-Identifier: BSD-2-Clause
import argparse
import struct
from random import randint
from dataclasses import dataclass
from typing import List, Tuple, Optional
from sdfgen import SystemDescription, Sddf, DeviceTree, LionsOs
from importlib.metadata import version

# This file will not be overwritten if codegen is rerun

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
    entries: sequence of (channel, timeslice_ns)
    """
    part_ch, part_timeslices = zip(*entries)
    return UserSchedule(list(part_timeslices), list(part_ch))

def generate(sdf_path: str, output_dir: str, dtb: DeviceTree):
    timer_node = dtb.node(board.timer)
    assert timer_node is not None

    timer_driver = ProtectionDomain("timer_driver", "timer_driver.elf", priority=201)
    timer_system = Sddf.Timer(sdf, timer_node, timer_driver)

    scheduler = ProtectionDomain("scheduler", "scheduler.elf", priority=200)

    # BEGIN META MARKER

    #######################################
    # PARTITION PROTECTION DOMAINS
    #######################################
    producer_p_p_producer_MON = ProtectionDomain("producer_p_p_producer_MON", "producer_p_p_producer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(producer_p_p_producer_MON)
    consumer_p_s1_consumer_MON = ProtectionDomain("consumer_p_s1_consumer_MON", "consumer_p_s1_consumer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(consumer_p_s1_consumer_MON)
    consumer_p_s2_consumer_MON = ProtectionDomain("consumer_p_s2_consumer_MON", "consumer_p_s2_consumer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(consumer_p_s2_consumer_MON)
    consumer_p_s5_consumer_MON = ProtectionDomain("consumer_p_s5_consumer_MON", "consumer_p_s5_consumer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(consumer_p_s5_consumer_MON)

    producer_p_p_producer = ProtectionDomain("producer_p_p_producer", "producer_p_p_producer.elf", priority=140, passive=True)
    scheduler.add_child_pd(producer_p_p_producer)
    consumer_p_s1_consumer = ProtectionDomain("consumer_p_s1_consumer", "consumer_p_s1_consumer.elf", priority=140, passive=True)
    scheduler.add_child_pd(consumer_p_s1_consumer)
    consumer_p_s2_consumer = ProtectionDomain("consumer_p_s2_consumer", "consumer_p_s2_consumer.elf", priority=140, passive=True)
    scheduler.add_child_pd(consumer_p_s2_consumer)
    consumer_p_s5_consumer = ProtectionDomain("consumer_p_s5_consumer", "consumer_p_s5_consumer.elf", priority=140, passive=True)
    scheduler.add_child_pd(consumer_p_s5_consumer)

    #######################################
    # MEMORY REGIONS
    #######################################
    top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region)
    top_impl_Instance_producer_p_p_producer_write_port_2_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p_producer_write_port_2_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p_producer_write_port_2_Memory_Region)
    top_impl_Instance_producer_p_p_producer_write_port_5_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p_producer_write_port_5_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p_producer_write_port_5_Memory_Region)

    producer_p_p_producer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x10_000_000, perms="rw"))
    consumer_p_s1_consumer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x10_000_000, perms="r"))
    producer_p_p_producer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_2_Memory_Region, 0x10_001_000, perms="rw"))
    consumer_p_s2_consumer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_2_Memory_Region, 0x10_000_000, perms="r"))
    producer_p_p_producer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_5_Memory_Region, 0x10_002_000, perms="rw"))
    consumer_p_s5_consumer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_5_Memory_Region, 0x10_000_000, perms="r"))

    #######################################
    # CHANNELS
    #######################################
    channel_producer_p_p_producer_MON = 2
    channel_consumer_p_s1_consumer_MON = 3
    channel_consumer_p_s2_consumer_MON = 4
    channel_consumer_p_s5_consumer_MON = 5

    sdf.add_channel(Channel(scheduler, producer_p_p_producer_MON, a_id=channel_producer_p_p_producer_MON, b_id=0))
    sdf.add_channel(Channel(producer_p_p_producer_MON, producer_p_p_producer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, consumer_p_s1_consumer_MON, a_id=channel_consumer_p_s1_consumer_MON, b_id=0))
    sdf.add_channel(Channel(consumer_p_s1_consumer_MON, consumer_p_s1_consumer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, consumer_p_s2_consumer_MON, a_id=channel_consumer_p_s2_consumer_MON, b_id=0))
    sdf.add_channel(Channel(consumer_p_s2_consumer_MON, consumer_p_s2_consumer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, consumer_p_s5_consumer_MON, a_id=channel_consumer_p_s5_consumer_MON, b_id=0))
    sdf.add_channel(Channel(consumer_p_s5_consumer_MON, consumer_p_s5_consumer, a_id=1, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_pad = (0, 800000000)
    ts_producer_p_p_producer_MON = (channel_producer_p_p_producer_MON, 50000000)
    ts_consumer_p_s1_consumer_MON = (channel_consumer_p_s1_consumer_MON, 50000000)
    ts_consumer_p_s2_consumer_MON = (channel_consumer_p_s2_consumer_MON, 50000000)
    ts_consumer_p_s5_consumer_MON = (channel_consumer_p_s5_consumer_MON, 50000000)

    user_schedule = schedule(
      ts_pad,
      ts_producer_p_p_producer_MON,
      ts_consumer_p_s1_consumer_MON,
      ts_consumer_p_s2_consumer_MON,
      ts_consumer_p_s5_consumer_MON
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
