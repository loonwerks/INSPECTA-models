# Copyright 2025, UNSW
# SPDX-License-Identifier: BSD-2-Clause
import argparse
import struct
from random import randint
from dataclasses import dataclass
from typing import List, Tuple, Optional
from sdfgen import SystemDescription, Sddf, DeviceTree, LionsOs
from importlib.metadata import version

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


def generate(sdf_path: str, output_dir: str, dtb: DeviceTree):
    timer_node = dtb.node(board.timer)
    assert timer_node is not None

    timer_driver = ProtectionDomain("timer_driver", "timer_driver.elf", priority=201)
    timer_system = Sddf.Timer(sdf, timer_node, timer_driver)

    scheduler = ProtectionDomain("scheduler", "scheduler.elf", priority=200)

    #######################################
    # PARTITION PROTECTION DOMAINS
    #######################################
    producer_p_p_producer_MON = ProtectionDomain("producer_p_p_producer_MON", "producer_p_p_producer_MON.elf", priority=150, passive=True)
    consumer_p_p_consumer_MON = ProtectionDomain("consumer_p_p_consumer_MON", "consumer_p_p_consumer_MON.elf", priority=150, passive=True)
    consumer_p_s_consumer_MON = ProtectionDomain("consumer_p_s_consumer_MON", "consumer_p_s_consumer_MON.elf", priority=150, passive=True)

    producer_p_p_producer = ProtectionDomain("producer_p_p_producer", "producer_p_p_producer.elf", priority=140, passive=True)
    consumer_p_p_consumer = ProtectionDomain("consumer_p_p_consumer", "consumer_p_p_consumer.elf", priority=140, passive=True)
    consumer_p_s_consumer = ProtectionDomain("consumer_p_s_consumer", "consumer_p_s_consumer.elf", priority=140, passive=True)

    partition_initial_pds = [
      producer_p_p_producer_MON,
      consumer_p_p_consumer_MON,
      consumer_p_s_consumer_MON
    ]

    partition_user_pds = [
      producer_p_p_producer,
      consumer_p_p_consumer,
      consumer_p_s_consumer
    ]

    pds = [
      timer_driver,
      scheduler
    ]

    for pd in pds:
      sdf.add_pd(pd)

    for pd in partition_initial_pds:
      scheduler.add_child_pd(pd)


    #######################################
    # TIME SLICES
    #######################################

    # These timeslices are in nanoseconds
    part_timeslices = [50000000, 50000000, 900000000]

    part_ch = [0, 1, 2]

    user_schedule = UserSchedule(part_timeslices, part_ch)

    # @kwinter: For now make these all children of the scheduler.
    # Once microkit supports handing TCBs to different PD's we will
    # make these user pds children of the initial pds
    for pd in partition_user_pds:
        scheduler.add_child_pd(pd)

    # These channels will start at 0 from the schedulers point of view
    for pd in partition_initial_pds:
        pd_channel = Channel(scheduler, pd)
        sdf.add_channel(pd_channel)

    for pd_init, pd_user in zip(partition_initial_pds, partition_user_pds):
        partition_channel = Channel(pd_init, pd_user)
        sdf.add_channel(partition_channel)


    #######################################
    # MEMORY REGIONS
    #######################################
    top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region)

    producer_p_p_producer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x10_000_000, perms="rw"))
    consumer_p_p_consumer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x10_000_000, perms="r"))
    consumer_p_s_consumer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x10_000_000, perms="r"))

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
