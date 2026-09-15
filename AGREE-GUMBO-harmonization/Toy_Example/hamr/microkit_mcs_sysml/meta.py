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

assert int(version('sdfgen').split(".")[1]) >= 30, f"Requires sdfgen >= 0.30, found {version('sdfgen')}"

from sdfgen_helper import *

ProtectionDomain = SystemDescription.ProtectionDomain
MemoryRegion = SystemDescription.MemoryRegion
Map = SystemDescription.Map
Channel = SystemDescription.Channel
IrqConventional = SystemDescription.IrqConventional
VirtualMachine = SystemDescription.VirtualMachine

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
    part_ch, part_timeslices, is_user_partition = zip(*entries)
    return UserSchedule(list(part_timeslices), list(part_ch), list(is_user_partition))

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
    A_sub_a_MON = ProtectionDomain(
      name="A_sub_a_MON",
      program_image="A_sub_a_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(A_sub_a_MON)

    A_sub_a = ProtectionDomain(
      name="A_sub_a",
      program_image="A_sub_a.elf",
      priority=140,
      passive=True)
    A_sub_a_MON.add_child_pd(A_sub_a, child_id=1)

    top_sub_A_sub_a_MON = ProtectionDomain(
      name="top_sub_A_sub_a_MON",
      program_image="top_sub_A_sub_a_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(top_sub_A_sub_a_MON)

    top_sub_A_sub_a = ProtectionDomain(
      name="top_sub_A_sub_a",
      program_image="top_sub_A_sub_a.elf",
      priority=140,
      passive=True)
    top_sub_A_sub_a_MON.add_child_pd(top_sub_A_sub_a, child_id=1)

    top_sub_B_sub_b_MON = ProtectionDomain(
      name="top_sub_B_sub_b_MON",
      program_image="top_sub_B_sub_b_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(top_sub_B_sub_b_MON)

    top_sub_B_sub_b = ProtectionDomain(
      name="top_sub_B_sub_b",
      program_image="top_sub_B_sub_b.elf",
      priority=140,
      passive=True)
    top_sub_B_sub_b_MON.add_child_pd(top_sub_B_sub_b, child_id=1)

    top_sub_C_sub_c_MON = ProtectionDomain(
      name="top_sub_C_sub_c_MON",
      program_image="top_sub_C_sub_c_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(top_sub_C_sub_c_MON)

    top_sub_C_sub_c = ProtectionDomain(
      name="top_sub_C_sub_c",
      program_image="top_sub_C_sub_c.elf",
      priority=140,
      passive=True)
    top_sub_C_sub_c_MON.add_child_pd(top_sub_C_sub_c, child_id=1)


    #######################################
    # MEMORY REGIONS
    #######################################
    'above_top.Impl'_Instance_A_sub_a_Output_1_Memory_Region = MemoryRegion(sdf, "'above_top.Impl'_Instance_A_sub_a_Output_1_Memory_Region", 0x1_000)
    sdf.add_mr('above_top.Impl'_Instance_A_sub_a_Output_1_Memory_Region)
    'above_top.Impl'_Instance_top_sub_A_sub_a_Output_1_Memory_Region = MemoryRegion(sdf, "'above_top.Impl'_Instance_top_sub_A_sub_a_Output_1_Memory_Region", 0x1_000)
    sdf.add_mr('above_top.Impl'_Instance_top_sub_A_sub_a_Output_1_Memory_Region)
    'above_top.Impl'_Instance_top_sub_A_sub_a_Input_1_Memory_Region = MemoryRegion(sdf, "'above_top.Impl'_Instance_top_sub_A_sub_a_Input_1_Memory_Region", 0x1_000)
    sdf.add_mr('above_top.Impl'_Instance_top_sub_A_sub_a_Input_1_Memory_Region)
    'above_top.Impl'_Instance_top_sub_B_sub_b_Output_1_Memory_Region = MemoryRegion(sdf, "'above_top.Impl'_Instance_top_sub_B_sub_b_Output_1_Memory_Region", 0x1_000)
    sdf.add_mr('above_top.Impl'_Instance_top_sub_B_sub_b_Output_1_Memory_Region)
    'above_top.Impl'_Instance_top_sub_C_sub_c_Output_1_Memory_Region = MemoryRegion(sdf, "'above_top.Impl'_Instance_top_sub_C_sub_c_Output_1_Memory_Region", 0x1_000)
    sdf.add_mr('above_top.Impl'_Instance_top_sub_C_sub_c_Output_1_Memory_Region)

    A_sub_a.add_map(Map('above_top.Impl'_Instance_A_sub_a_Output_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="Output_queue_1"))
    top_sub_A_sub_a.add_map(Map('above_top.Impl'_Instance_top_sub_A_sub_a_Output_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="Output_queue_1"))
    top_sub_B_sub_b.add_map(Map('above_top.Impl'_Instance_top_sub_A_sub_a_Output_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="Input_queue_1"))
    top_sub_C_sub_c.add_map(Map('above_top.Impl'_Instance_top_sub_A_sub_a_Output_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="Input1_queue_1"))
    top_sub_A_sub_a.add_map(Map('above_top.Impl'_Instance_top_sub_A_sub_a_Input_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="Input_queue_1"))
    top_sub_B_sub_b.add_map(Map('above_top.Impl'_Instance_top_sub_B_sub_b_Output_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="Output_queue_1"))
    top_sub_C_sub_c.add_map(Map('above_top.Impl'_Instance_top_sub_B_sub_b_Output_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="Input2_queue_1"))
    A_sub_a.add_map(Map('above_top.Impl'_Instance_top_sub_C_sub_c_Output_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="Input_queue_1"))
    top_sub_C_sub_c.add_map(Map('above_top.Impl'_Instance_top_sub_C_sub_c_Output_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="Output_queue_1"))



    #######################################
    # CHANNELS
    #######################################
    channel_A_sub_a_MON = 5
    channel_top_sub_A_sub_a_MON = 2
    channel_top_sub_B_sub_b_MON = 3
    channel_top_sub_C_sub_c_MON = 4

    sdf.add_channel(Channel(a=scheduler, a_id=5, b=A_sub_a_MON, b_id=0))
    sdf.add_channel(Channel(a=A_sub_a_MON, a_id=1, b=A_sub_a, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=2, b=top_sub_A_sub_a_MON, b_id=0))
    sdf.add_channel(Channel(a=top_sub_A_sub_a_MON, a_id=1, b=top_sub_A_sub_a, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=top_sub_B_sub_b_MON, b_id=0))
    sdf.add_channel(Channel(a=top_sub_B_sub_b_MON, a_id=1, b=top_sub_B_sub_b, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=4, b=top_sub_C_sub_c_MON, b_id=0))
    sdf.add_channel(Channel(a=top_sub_C_sub_c_MON, a_id=1, b=top_sub_C_sub_c, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_top_sub_A_sub_a_MON = (channel_top_sub_A_sub_a_MON, 50000000, True)
    ts_top_sub_B_sub_b_MON = (channel_top_sub_B_sub_b_MON, 50000000, True)
    ts_top_sub_C_sub_c_MON = (channel_top_sub_C_sub_c_MON, 50000000, True)
    ts_A_sub_a_MON = (channel_A_sub_a_MON, 50000000, True)
    ts_pad = (0, 800000000, False)

    user_schedule = schedule(
      ts_top_sub_A_sub_a_MON,
      ts_top_sub_B_sub_b_MON,
      ts_top_sub_C_sub_c_MON,
      ts_A_sub_a_MON,
      ts_pad
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
