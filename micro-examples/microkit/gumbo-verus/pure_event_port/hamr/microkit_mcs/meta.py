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
    snd_p_snd_MON = ProtectionDomain(
      name="snd_p_snd_MON",
      program_image="snd_p_snd_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(snd_p_snd_MON)

    snd_p_snd = ProtectionDomain(
      name="snd_p_snd",
      program_image="snd_p_snd.elf",
      priority=140,
      passive=True)
    snd_p_snd_MON.add_child_pd(snd_p_snd, child_id=1)

    rcv_p_rcv_MON = ProtectionDomain(
      name="rcv_p_rcv_MON",
      program_image="rcv_p_rcv_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(rcv_p_rcv_MON)

    rcv_p_rcv = ProtectionDomain(
      name="rcv_p_rcv",
      program_image="rcv_p_rcv.elf",
      priority=140,
      passive=True)
    rcv_p_rcv_MON.add_child_pd(rcv_p_rcv, child_id=1)


    #######################################
    # MEMORY REGIONS
    #######################################
    top_impl_Instance_snd_p_snd_out_val_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_snd_p_snd_out_val_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_snd_p_snd_out_val_1_Memory_Region)
    top_impl_Instance_snd_p_snd_evt_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_snd_p_snd_evt_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_snd_p_snd_evt_1_Memory_Region)

    snd_p_snd.add_map(Map(top_impl_Instance_snd_p_snd_out_val_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="out_val_queue_1"))
    rcv_p_rcv.add_map(Map(top_impl_Instance_snd_p_snd_out_val_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="in_val_queue_1"))
    snd_p_snd.add_map(Map(top_impl_Instance_snd_p_snd_evt_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="evt_queue_1"))
    rcv_p_rcv.add_map(Map(top_impl_Instance_snd_p_snd_evt_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="evt_queue_1"))



    #######################################
    # CHANNELS
    #######################################
    channel_snd_p_snd_MON = 2
    channel_rcv_p_rcv_MON = 3

    sdf.add_channel(Channel(a=scheduler, a_id=2, b=snd_p_snd_MON, b_id=0))
    sdf.add_channel(Channel(a=snd_p_snd_MON, a_id=1, b=snd_p_snd, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=rcv_p_rcv_MON, b_id=0))
    sdf.add_channel(Channel(a=rcv_p_rcv_MON, a_id=1, b=rcv_p_rcv, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_snd_p_snd_MON = (channel_snd_p_snd_MON, 50000000, True)
    ts_rcv_p_rcv_MON = (channel_rcv_p_rcv_MON, 50000000, True)
    ts_pad = (0, 900000000, False)

    user_schedule = schedule(
      ts_snd_p_snd_MON,
      ts_rcv_p_rcv_MON,
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
