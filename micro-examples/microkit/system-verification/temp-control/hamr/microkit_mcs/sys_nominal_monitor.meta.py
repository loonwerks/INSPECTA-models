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

    #######################################
    # SCHEDULE STATE
    # Broadcast region written by the scheduler before every dispatch.
    # The runtime monitor maps this region read-only to observe which
    # protection domain last yielded and which will be dispatched next.
    # Must match SCHED_STATE_VADDR / SCHED_STATE_SIZE in scheduler_config.h.
    #######################################
    SCHED_STATE_VADDR = 0x4_000_000
    SCHED_STATE_SIZE  = 0x1000  # 4 KB
    sched_state = MemoryRegion(sdf, "sched_state", SCHED_STATE_SIZE)
    sdf.add_mr(sched_state)
    scheduler.add_map(Map(sched_state, SCHED_STATE_VADDR, perms="rw"))

    #######################################
    # SCHEDULE
    # The full user_schedule published by the scheduler at init.
    # Monitors that map this region read-only can correlate
    # current_timeslice indices with channel IDs and durations.
    # Must match SCHED_SCHEDULE_VADDR / SCHED_SCHEDULE_SIZE in scheduler_config.h.
    #######################################
    SCHED_SCHEDULE_VADDR = 0x4_001_000
    SCHED_SCHEDULE_SIZE  = 0x1000  # 4 KB
    sched_schedule = MemoryRegion(sdf, "sched_schedule", SCHED_SCHEDULE_SIZE)
    sdf.add_mr(sched_schedule)
    scheduler.add_map(Map(sched_schedule, SCHED_SCHEDULE_VADDR, perms="rw"))

    # BEGIN META MARKER

    #######################################
    # PARTITION PROTECTION DOMAINS
    #######################################
    tsp_tst_MON = ProtectionDomain(
      name="tsp_tst_MON",
      program_image="tsp_tst_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(tsp_tst_MON)

    tsp_tst = ProtectionDomain(
      name="tsp_tst",
      program_image="tsp_tst.elf",
      priority=140,
      passive=True)
    tsp_tst_MON.add_child_pd(tsp_tst, child_id=1)

    tcp_tct_MON = ProtectionDomain(
      name="tcp_tct_MON",
      program_image="tcp_tct_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(tcp_tct_MON)

    tcp_tct = ProtectionDomain(
      name="tcp_tct",
      program_image="tcp_tct.elf",
      priority=140,
      passive=True)
    tcp_tct_MON.add_child_pd(tcp_tct, child_id=1)

    fp_ft_MON = ProtectionDomain(
      name="fp_ft_MON",
      program_image="fp_ft_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(fp_ft_MON)

    fp_ft = ProtectionDomain(
      name="fp_ft",
      program_image="fp_ft.elf",
      priority=140,
      passive=True)
    fp_ft_MON.add_child_pd(fp_ft, child_id=1)

    sys_nominal_monitor_process_sys_nominal_monitor_thread_MON = ProtectionDomain(
      name="sys_nominal_monitor_process_sys_nominal_monitor_thread_MON",
      program_image="sys_nominal_monitor_process_sys_nominal_monitor_thread_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(sys_nominal_monitor_process_sys_nominal_monitor_thread_MON)

    sys_nominal_monitor_process_sys_nominal_monitor_thread = ProtectionDomain(
      name="sys_nominal_monitor_process_sys_nominal_monitor_thread",
      program_image="sys_nominal_monitor_process_sys_nominal_monitor_thread.elf",
      priority=140,
      passive=True,
      stack_size=0x10_000)
    sys_nominal_monitor_process_sys_nominal_monitor_thread_MON.add_child_pd(sys_nominal_monitor_process_sys_nominal_monitor_thread, child_id=1)


    #######################################
    # MEMORY REGIONS
    #######################################
    TempControlSystem_Instance_tsp_tst_currentTemp_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tsp_tst_currentTemp_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tsp_tst_currentTemp_1_Memory_Region)
    TempControlSystem_Instance_tcp_tct_fanCmd_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tcp_tct_fanCmd_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tcp_tct_fanCmd_1_Memory_Region)
    TempControlSystem_Instance_tcp_tct_sv_currentSetPoint_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tcp_tct_sv_currentSetPoint_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tcp_tct_sv_currentSetPoint_1_Memory_Region)
    TempControlSystem_Instance_tcp_tct_sv_currentFanState_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tcp_tct_sv_currentFanState_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tcp_tct_sv_currentFanState_1_Memory_Region)
    TempControlSystem_Instance_tcp_tct_sv_latestTemp_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tcp_tct_sv_latestTemp_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tcp_tct_sv_latestTemp_1_Memory_Region)
    TempControlSystem_Instance_tcp_tct_sv_fanError_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tcp_tct_sv_fanError_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tcp_tct_sv_fanError_1_Memory_Region)
    TempControlSystem_Instance_tcp_tct_setPoint_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_tcp_tct_setPoint_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_tcp_tct_setPoint_1_Memory_Region)
    TempControlSystem_Instance_fp_ft_fanAck_1_Memory_Region = MemoryRegion(sdf, "TempControlSystem_Instance_fp_ft_fanAck_1_Memory_Region", 0x1_000)
    sdf.add_mr(TempControlSystem_Instance_fp_ft_fanAck_1_Memory_Region)

    tsp_tst.add_map(Map(TempControlSystem_Instance_tsp_tst_currentTemp_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="currentTemp_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tsp_tst_currentTemp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="currentTemp_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tsp_tst_currentTemp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="tsp_tst_currentTemp_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tcp_tct_fanCmd_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="fanCmd_queue_1"))
    fp_ft.add_map(Map(TempControlSystem_Instance_tcp_tct_fanCmd_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="fanCmd_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tcp_tct_fanCmd_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="tcp_tct_fanCmd_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_currentSetPoint_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="sv_currentSetPoint_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_currentSetPoint_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="tcp_tct_sv_currentSetPoint_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_currentFanState_1_Memory_Region, 0x10_003_000, perms="rw", setvar_vaddr="sv_currentFanState_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_currentFanState_1_Memory_Region, 0x10_003_000, perms="r", setvar_vaddr="tcp_tct_sv_currentFanState_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_latestTemp_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="sv_latestTemp_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_latestTemp_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="tcp_tct_sv_latestTemp_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_fanError_1_Memory_Region, 0x10_005_000, perms="rw", setvar_vaddr="sv_fanError_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tcp_tct_sv_fanError_1_Memory_Region, 0x10_005_000, perms="r", setvar_vaddr="tcp_tct_sv_fanError_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_tcp_tct_setPoint_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="setPoint_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_tcp_tct_setPoint_1_Memory_Region, 0x10_007_000, perms="r", setvar_vaddr="tcp_tct_setPoint_queue_1"))
    tcp_tct.add_map(Map(TempControlSystem_Instance_fp_ft_fanAck_1_Memory_Region, 0x10_007_000, perms="r", setvar_vaddr="fanAck_queue_1"))
    fp_ft.add_map(Map(TempControlSystem_Instance_fp_ft_fanAck_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="fanAck_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(TempControlSystem_Instance_fp_ft_fanAck_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="fp_ft_fanAck_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(sched_state, 0x10_008_000, perms="r", setvar_vaddr="sched_state_queue_1"))
    sys_nominal_monitor_process_sys_nominal_monitor_thread.add_map(Map(sched_schedule, 0x10_009_000, perms="r", setvar_vaddr="sched_schedule_queue_1"))



    #######################################
    # CHANNELS
    #######################################
    channel_tsp_tst_MON = 2
    channel_tcp_tct_MON = 3
    channel_fp_ft_MON = 4
    channel_sys_nominal_monitor_process_sys_nominal_monitor_thread_MON = 7

    sdf.add_channel(Channel(a=scheduler, a_id=2, b=tsp_tst_MON, b_id=0))
    sdf.add_channel(Channel(a=tsp_tst_MON, a_id=1, b=tsp_tst, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=tcp_tct_MON, b_id=0))
    sdf.add_channel(Channel(a=tcp_tct_MON, a_id=1, b=tcp_tct, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=4, b=fp_ft_MON, b_id=0))
    sdf.add_channel(Channel(a=fp_ft_MON, a_id=1, b=fp_ft, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=7, b=sys_nominal_monitor_process_sys_nominal_monitor_thread_MON, b_id=0))
    sdf.add_channel(Channel(a=sys_nominal_monitor_process_sys_nominal_monitor_thread_MON, a_id=1, b=sys_nominal_monitor_process_sys_nominal_monitor_thread, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_pad = (0, 700000000, False)
    ts_sys_nominal_monitor_process_sys_nominal_monitor_thread_MON = (channel_sys_nominal_monitor_process_sys_nominal_monitor_thread_MON, 50000000, False)
    ts_tsp_tst_MON = (channel_tsp_tst_MON, 50000000, True)
    ts_tcp_tct_MON = (channel_tcp_tct_MON, 50000000, True)
    ts_fp_ft_MON = (channel_fp_ft_MON, 50000000, True)

    user_schedule = schedule(
      ts_pad,
      ts_sys_nominal_monitor_process_sys_nominal_monitor_thread_MON,
      ts_tsp_tst_MON,
      ts_sys_nominal_monitor_process_sys_nominal_monitor_thread_MON,
      ts_tcp_tct_MON,
      ts_sys_nominal_monitor_process_sys_nominal_monitor_thread_MON,
      ts_fp_ft_MON
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
