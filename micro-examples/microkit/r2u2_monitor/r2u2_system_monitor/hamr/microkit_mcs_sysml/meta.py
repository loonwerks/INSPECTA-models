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
    producer_producer_MON = ProtectionDomain(
      name="producer_producer_MON",
      program_image="producer_producer_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(producer_producer_MON)

    producer_producer = ProtectionDomain(
      name="producer_producer",
      program_image="producer_producer.elf",
      priority=140,
      passive=True)
    producer_producer_MON.add_child_pd(producer_producer, child_id=1)

    consumer_consumer_MON = ProtectionDomain(
      name="consumer_consumer_MON",
      program_image="consumer_consumer_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(consumer_consumer_MON)

    consumer_consumer = ProtectionDomain(
      name="consumer_consumer",
      program_image="consumer_consumer.elf",
      priority=140,
      passive=True)
    consumer_consumer_MON.add_child_pd(consumer_consumer, child_id=1)

    monitor_process_monitor_thread_MON = ProtectionDomain(
      name="monitor_process_monitor_thread_MON",
      program_image="monitor_process_monitor_thread_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(monitor_process_monitor_thread_MON)

    monitor_process_monitor_thread = ProtectionDomain(
      name="monitor_process_monitor_thread",
      program_image="monitor_process_monitor_thread.elf",
      priority=140,
      passive=True)
    monitor_process_monitor_thread_MON.add_child_pd(monitor_process_monitor_thread, child_id=1)


    #######################################
    # MEMORY REGIONS
    #######################################
    Sys_i_Instance_producer_producer_sample_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_producer_producer_sample_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_producer_producer_sample_1_Memory_Region)
    Sys_i_Instance_consumer_consumer_observed_sample_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_consumer_consumer_observed_sample_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_consumer_consumer_observed_sample_1_Memory_Region)
    Sys_i_Instance_monitor_process_monitor_thread_alert_flag_1_Memory_Region = MemoryRegion(sdf, "Sys_i_Instance_monitor_process_monitor_thread_alert_flag_1_Memory_Region", 0x1_000)
    sdf.add_mr(Sys_i_Instance_monitor_process_monitor_thread_alert_flag_1_Memory_Region)

    producer_producer.add_map(Map(Sys_i_Instance_producer_producer_sample_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="sample_queue_1"))
    consumer_consumer.add_map(Map(Sys_i_Instance_producer_producer_sample_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="sample_queue_1"))
    monitor_process_monitor_thread.add_map(Map(Sys_i_Instance_producer_producer_sample_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="sent_sample_queue_1"))
    consumer_consumer.add_map(Map(Sys_i_Instance_consumer_consumer_observed_sample_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="observed_sample_queue_1"))
    monitor_process_monitor_thread.add_map(Map(Sys_i_Instance_consumer_consumer_observed_sample_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="observed_sample_queue_1"))
    consumer_consumer.add_map(Map(Sys_i_Instance_monitor_process_monitor_thread_alert_flag_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="alert_flag_queue_1"))
    monitor_process_monitor_thread.add_map(Map(Sys_i_Instance_monitor_process_monitor_thread_alert_flag_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="alert_flag_queue_1"))



    #######################################
    # CHANNELS
    #######################################
    channel_producer_producer_MON = 2
    channel_consumer_consumer_MON = 3
    channel_monitor_process_monitor_thread_MON = 4

    sdf.add_channel(Channel(a=scheduler, a_id=2, b=producer_producer_MON, b_id=0))
    sdf.add_channel(Channel(a=producer_producer_MON, a_id=1, b=producer_producer, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=consumer_consumer_MON, b_id=0))
    sdf.add_channel(Channel(a=consumer_consumer_MON, a_id=1, b=consumer_consumer, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=4, b=monitor_process_monitor_thread_MON, b_id=0))
    sdf.add_channel(Channel(a=monitor_process_monitor_thread_MON, a_id=1, b=monitor_process_monitor_thread, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_producer_producer_MON = (channel_producer_producer_MON, 10000000, True)
    ts_consumer_consumer_MON = (channel_consumer_consumer_MON, 10000000, True)
    ts_monitor_process_monitor_thread_MON = (channel_monitor_process_monitor_thread_MON, 10000000, True)
    ts_pad = (0, 970000000, False)

    user_schedule = schedule(
      ts_producer_producer_MON,
      ts_consumer_consumer_MON,
      ts_monitor_process_monitor_thread_MON,
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
