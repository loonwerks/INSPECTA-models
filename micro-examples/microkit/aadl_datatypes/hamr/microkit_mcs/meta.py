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
    producer_producer_MON = ProtectionDomain("producer_producer_MON", "producer_producer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(producer_producer_MON)
    consumer_consumer_MON = ProtectionDomain("consumer_consumer_MON", "consumer_consumer_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(consumer_consumer_MON)

    producer_producer = ProtectionDomain("producer_producer", "producer_producer.elf", priority=140, passive=True)
    scheduler.add_child_pd(producer_producer)
    consumer_consumer = ProtectionDomain("consumer_consumer", "consumer_consumer.elf", priority=140, passive=True)
    scheduler.add_child_pd(consumer_consumer)

    #######################################
    # MEMORY REGIONS
    #######################################
    Sys_i_Instance_producer_producer_myBoolean_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myBoolean_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myBoolean_1_Memory_Region)
    Sys_i_Instance_producer_producer_myCharacter_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myCharacter_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myCharacter_1_Memory_Region)
    Sys_i_Instance_producer_producer_myString_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myString_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myString_1_Memory_Region)
    Sys_i_Instance_producer_producer_myInt8_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myInt8_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myInt8_1_Memory_Region)
    Sys_i_Instance_producer_producer_myInt16_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myInt16_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myInt16_1_Memory_Region)
    Sys_i_Instance_producer_producer_myInt32_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myInt32_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myInt32_1_Memory_Region)
    Sys_i_Instance_producer_producer_myInt64_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myInt64_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myInt64_1_Memory_Region)
    Sys_i_Instance_producer_producer_myUInt8_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myUInt8_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myUInt8_1_Memory_Region)
    Sys_i_Instance_producer_producer_myUInt16_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myUInt16_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myUInt16_1_Memory_Region)
    Sys_i_Instance_producer_producer_myUInt32_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myUInt32_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myUInt32_1_Memory_Region)
    Sys_i_Instance_producer_producer_myUInt64_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myUInt64_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myUInt64_1_Memory_Region)
    Sys_i_Instance_producer_producer_myFloat32_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myFloat32_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myFloat32_1_Memory_Region)
    Sys_i_Instance_producer_producer_myFloat64_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myFloat64_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myFloat64_1_Memory_Region)
    Sys_i_Instance_producer_producer_myEnum_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myEnum_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myEnum_1_Memory_Region)
    Sys_i_Instance_producer_producer_myStruct_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myStruct_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myStruct_1_Memory_Region)
    Sys_i_Instance_producer_producer_myArray1_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_myArray1_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_myArray1_1_Memory_Region)

    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myBoolean_1_Memory_Region, 0x10_000_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myBoolean_1_Memory_Region, 0x10_000_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myCharacter_1_Memory_Region, 0x10_001_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myCharacter_1_Memory_Region, 0x10_001_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myString_1_Memory_Region, 0x10_002_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myString_1_Memory_Region, 0x10_002_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myInt8_1_Memory_Region, 0x10_003_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myInt8_1_Memory_Region, 0x10_003_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myInt16_1_Memory_Region, 0x10_004_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myInt16_1_Memory_Region, 0x10_004_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myInt32_1_Memory_Region, 0x10_005_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myInt32_1_Memory_Region, 0x10_005_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myInt64_1_Memory_Region, 0x10_006_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myInt64_1_Memory_Region, 0x10_006_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myUInt8_1_Memory_Region, 0x10_007_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myUInt8_1_Memory_Region, 0x10_007_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myUInt16_1_Memory_Region, 0x10_008_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myUInt16_1_Memory_Region, 0x10_008_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myUInt32_1_Memory_Region, 0x10_009_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myUInt32_1_Memory_Region, 0x10_009_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myUInt64_1_Memory_Region, 0x10_00A_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myUInt64_1_Memory_Region, 0x10_00A_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myFloat32_1_Memory_Region, 0x10_00B_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myFloat32_1_Memory_Region, 0x10_00B_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myFloat64_1_Memory_Region, 0x10_00C_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myFloat64_1_Memory_Region, 0x10_00C_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myEnum_1_Memory_Region, 0x10_00D_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myEnum_1_Memory_Region, 0x10_00D_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myStruct_1_Memory_Region, 0x10_00E_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myStruct_1_Memory_Region, 0x10_00E_000, perms="r"))
    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_myArray1_1_Memory_Region, 0x10_00F_000, perms="rw"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_myArray1_1_Memory_Region, 0x10_00F_000, perms="r"))

    #######################################
    # CHANNELS
    #######################################
    channel_producer_producer_MON = 2
    channel_consumer_consumer_MON = 3

    sdf.add_channel(Channel(scheduler, producer_producer_MON, a_id=channel_producer_producer_MON, b_id=0))
    sdf.add_channel(Channel(producer_producer_MON, producer_producer, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, consumer_consumer_MON, a_id=channel_consumer_consumer_MON, b_id=0))
    sdf.add_channel(Channel(consumer_consumer_MON, consumer_consumer, a_id=1, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_pad = (0, 900000000)
    ts_producer_producer_MON = (channel_producer_producer_MON, 50000000)
    ts_consumer_consumer_MON = (channel_consumer_consumer_MON, 50000000)

    user_schedule = schedule(
      ts_pad,
      ts_producer_producer_MON,
      ts_consumer_consumer_MON
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
