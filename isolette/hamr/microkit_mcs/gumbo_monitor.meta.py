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
    thermostat_rt_mri_mri_MON = ProtectionDomain(
      name="thermostat_rt_mri_mri_MON",
      program_image="thermostat_rt_mri_mri_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_rt_mri_mri_MON)

    thermostat_rt_mri_mri = ProtectionDomain(
      name="thermostat_rt_mri_mri",
      program_image="thermostat_rt_mri_mri.elf",
      priority=140,
      passive=True)
    thermostat_rt_mri_mri_MON.add_child_pd(thermostat_rt_mri_mri, child_id=1)

    thermostat_rt_mhs_mhs_MON = ProtectionDomain(
      name="thermostat_rt_mhs_mhs_MON",
      program_image="thermostat_rt_mhs_mhs_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_rt_mhs_mhs_MON)

    thermostat_rt_mhs_mhs = ProtectionDomain(
      name="thermostat_rt_mhs_mhs",
      program_image="thermostat_rt_mhs_mhs.elf",
      priority=140,
      passive=True)
    thermostat_rt_mhs_mhs_MON.add_child_pd(thermostat_rt_mhs_mhs, child_id=1)

    thermostat_rt_mrm_mrm_MON = ProtectionDomain(
      name="thermostat_rt_mrm_mrm_MON",
      program_image="thermostat_rt_mrm_mrm_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_rt_mrm_mrm_MON)

    thermostat_rt_mrm_mrm = ProtectionDomain(
      name="thermostat_rt_mrm_mrm",
      program_image="thermostat_rt_mrm_mrm.elf",
      priority=140,
      passive=True)
    thermostat_rt_mrm_mrm_MON.add_child_pd(thermostat_rt_mrm_mrm, child_id=1)

    thermostat_rt_drf_drf_MON = ProtectionDomain(
      name="thermostat_rt_drf_drf_MON",
      program_image="thermostat_rt_drf_drf_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_rt_drf_drf_MON)

    thermostat_rt_drf_drf = ProtectionDomain(
      name="thermostat_rt_drf_drf",
      program_image="thermostat_rt_drf_drf.elf",
      priority=140,
      passive=True)
    thermostat_rt_drf_drf_MON.add_child_pd(thermostat_rt_drf_drf, child_id=1)

    thermostat_mt_mmi_mmi_MON = ProtectionDomain(
      name="thermostat_mt_mmi_mmi_MON",
      program_image="thermostat_mt_mmi_mmi_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_mt_mmi_mmi_MON)

    thermostat_mt_mmi_mmi = ProtectionDomain(
      name="thermostat_mt_mmi_mmi",
      program_image="thermostat_mt_mmi_mmi.elf",
      priority=140,
      passive=True)
    thermostat_mt_mmi_mmi_MON.add_child_pd(thermostat_mt_mmi_mmi, child_id=1)

    thermostat_mt_ma_ma_MON = ProtectionDomain(
      name="thermostat_mt_ma_ma_MON",
      program_image="thermostat_mt_ma_ma_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_mt_ma_ma_MON)

    thermostat_mt_ma_ma = ProtectionDomain(
      name="thermostat_mt_ma_ma",
      program_image="thermostat_mt_ma_ma.elf",
      priority=140,
      passive=True)
    thermostat_mt_ma_ma_MON.add_child_pd(thermostat_mt_ma_ma, child_id=1)

    thermostat_mt_mmm_mmm_MON = ProtectionDomain(
      name="thermostat_mt_mmm_mmm_MON",
      program_image="thermostat_mt_mmm_mmm_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_mt_mmm_mmm_MON)

    thermostat_mt_mmm_mmm = ProtectionDomain(
      name="thermostat_mt_mmm_mmm",
      program_image="thermostat_mt_mmm_mmm.elf",
      priority=140,
      passive=True)
    thermostat_mt_mmm_mmm_MON.add_child_pd(thermostat_mt_mmm_mmm, child_id=1)

    thermostat_mt_dmf_dmf_MON = ProtectionDomain(
      name="thermostat_mt_dmf_dmf_MON",
      program_image="thermostat_mt_dmf_dmf_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(thermostat_mt_dmf_dmf_MON)

    thermostat_mt_dmf_dmf = ProtectionDomain(
      name="thermostat_mt_dmf_dmf",
      program_image="thermostat_mt_dmf_dmf.elf",
      priority=140,
      passive=True)
    thermostat_mt_dmf_dmf_MON.add_child_pd(thermostat_mt_dmf_dmf, child_id=1)

    operator_interface_oip_oit_MON = ProtectionDomain(
      name="operator_interface_oip_oit_MON",
      program_image="operator_interface_oip_oit_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(operator_interface_oip_oit_MON)

    operator_interface_oip_oit = ProtectionDomain(
      name="operator_interface_oip_oit",
      program_image="operator_interface_oip_oit.elf",
      priority=140,
      passive=True)
    operator_interface_oip_oit_MON.add_child_pd(operator_interface_oip_oit, child_id=1)

    temperature_sensor_cpi_thermostat_MON = ProtectionDomain(
      name="temperature_sensor_cpi_thermostat_MON",
      program_image="temperature_sensor_cpi_thermostat_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(temperature_sensor_cpi_thermostat_MON)

    temperature_sensor_cpi_thermostat = ProtectionDomain(
      name="temperature_sensor_cpi_thermostat",
      program_image="temperature_sensor_cpi_thermostat.elf",
      priority=140,
      passive=True)
    temperature_sensor_cpi_thermostat_MON.add_child_pd(temperature_sensor_cpi_thermostat, child_id=1)

    heat_source_cpi_heat_controller_MON = ProtectionDomain(
      name="heat_source_cpi_heat_controller_MON",
      program_image="heat_source_cpi_heat_controller_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(heat_source_cpi_heat_controller_MON)

    heat_source_cpi_heat_controller = ProtectionDomain(
      name="heat_source_cpi_heat_controller",
      program_image="heat_source_cpi_heat_controller.elf",
      priority=140,
      passive=True)
    heat_source_cpi_heat_controller_MON.add_child_pd(heat_source_cpi_heat_controller, child_id=1)

    gumbo_monitor_process_gumbo_monitor_thread_MON = ProtectionDomain(
      name="gumbo_monitor_process_gumbo_monitor_thread_MON",
      program_image="gumbo_monitor_process_gumbo_monitor_thread_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(gumbo_monitor_process_gumbo_monitor_thread_MON)

    gumbo_monitor_process_gumbo_monitor_thread = ProtectionDomain(
      name="gumbo_monitor_process_gumbo_monitor_thread",
      program_image="gumbo_monitor_process_gumbo_monitor_thread.elf",
      priority=140,
      passive=True)
    gumbo_monitor_process_gumbo_monitor_thread_MON.add_child_pd(gumbo_monitor_process_gumbo_monitor_thread, child_id=1)


    #######################################
    # MEMORY REGIONS
    #######################################
    Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region)
    Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region)
    Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region)
    Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region)
    Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region)
    Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region)
    Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region)
    Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region)
    Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region = MemoryRegion(sdf, "Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region", 0x1_000)
    sdf.add_mr(Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region)

    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="upper_desired_temp_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="upper_desired_temp_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="thermostat_rt_mri_mri_upper_desired_temp_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="lower_desired_temp_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="lower_desired_temp_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="thermostat_rt_mri_mri_lower_desired_temp_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="displayed_temp_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="display_temperature_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="thermostat_rt_mri_mri_displayed_temp_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_003_000, perms="rw", setvar_vaddr="regulator_status_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="regulator_status_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_003_000, perms="r", setvar_vaddr="thermostat_rt_mri_mri_regulator_status_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="interface_failure_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="interface_failure_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="thermostat_rt_mri_mri_interface_failure_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="heat_control_queue_1"))
    heat_source_cpi_heat_controller.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="heat_control_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_005_000, perms="r", setvar_vaddr="thermostat_rt_mhs_mhs_heat_control_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region, 0x10_003_000, perms="rw", setvar_vaddr="sv_lastCmd_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="thermostat_rt_mhs_mhs_sv_lastCmd_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_005_000, perms="r", setvar_vaddr="regulator_mode_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="regulator_mode_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="regulator_mode_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_007_000, perms="r", setvar_vaddr="thermostat_rt_mrm_mrm_regulator_mode_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="sv_lastRegulatorMode_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="thermostat_rt_mrm_mrm_sv_lastRegulatorMode_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_003_000, perms="r", setvar_vaddr="internal_failure_queue_1"))
    thermostat_rt_drf_drf.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="internal_failure_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_009_000, perms="r", setvar_vaddr="thermostat_rt_drf_drf_internal_failure_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="upper_alarm_temp_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="upper_alarm_temp_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_00A_000, perms="r", setvar_vaddr="thermostat_mt_mmi_mmi_upper_alarm_temp_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="lower_alarm_temp_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="lower_alarm_temp_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_00B_000, perms="r", setvar_vaddr="thermostat_mt_mmi_mmi_lower_alarm_temp_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="monitor_status_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="monitor_status_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_00C_000, perms="r", setvar_vaddr="thermostat_mt_mmi_mmi_monitor_status_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_003_000, perms="rw", setvar_vaddr="interface_failure_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="interface_failure_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_00D_000, perms="r", setvar_vaddr="thermostat_mt_mmi_mmi_interface_failure_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="sv_lastCmd_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region, 0x10_00E_000, perms="r", setvar_vaddr="thermostat_mt_mmi_mmi_sv_lastCmd_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="alarm_control_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_003_000, perms="r", setvar_vaddr="alarm_control_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_00F_000, perms="r", setvar_vaddr="thermostat_mt_ma_ma_alarm_control_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region, 0x10_003_000, perms="rw", setvar_vaddr="sv_lastCmd_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region, 0x10_010_000, perms="r", setvar_vaddr="thermostat_mt_ma_ma_sv_lastCmd_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_005_000, perms="r", setvar_vaddr="monitor_mode_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="monitor_mode_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="monitor_mode_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_011_000, perms="r", setvar_vaddr="thermostat_mt_mmm_mmm_monitor_mode_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="sv_lastMonitorMode_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region, 0x10_012_000, perms="r", setvar_vaddr="thermostat_mt_mmm_mmm_sv_lastMonitorMode_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_003_000, perms="r", setvar_vaddr="internal_failure_queue_1"))
    thermostat_mt_dmf_dmf.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="internal_failure_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_013_000, perms="r", setvar_vaddr="thermostat_mt_dmf_dmf_internal_failure_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="lower_desired_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="lower_desired_tempWstatus_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_014_000, perms="r", setvar_vaddr="operator_interface_oip_oit_lower_desired_tempWstatus_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_007_000, perms="r", setvar_vaddr="upper_desired_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_005_000, perms="rw", setvar_vaddr="upper_desired_tempWstatus_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_015_000, perms="r", setvar_vaddr="operator_interface_oip_oit_upper_desired_tempWstatus_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="lower_alarm_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_006_000, perms="rw", setvar_vaddr="lower_alarm_tempWstatus_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_016_000, perms="r", setvar_vaddr="operator_interface_oip_oit_lower_alarm_tempWstatus_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_007_000, perms="r", setvar_vaddr="upper_alarm_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_007_000, perms="rw", setvar_vaddr="upper_alarm_tempWstatus_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_017_000, perms="r", setvar_vaddr="operator_interface_oip_oit_upper_alarm_tempWstatus_queue_1"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_005_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_005_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    temperature_sensor_cpi_thermostat.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="current_tempWstatus_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_018_000, perms="r", setvar_vaddr="temperature_sensor_cpi_thermostat_current_tempWstatus_queue_1"))
    temperature_sensor_cpi_thermostat.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region, 0x10_001_000, perms="r", setvar_vaddr="air_queue_1"))
    heat_source_cpi_heat_controller.add_map(Map(Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region, 0x10_001_000, perms="rw", setvar_vaddr="heat_out_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(sched_state, 0x10_019_000, perms="r", setvar_vaddr="sched_state_queue_1"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(sched_schedule, 0x10_01A_000, perms="r", setvar_vaddr="sched_schedule_queue_1"))



    #######################################
    # CHANNELS
    #######################################
    channel_thermostat_rt_mri_mri_MON = 7
    channel_thermostat_rt_mhs_mhs_MON = 9
    channel_thermostat_rt_mrm_mrm_MON = 8
    channel_thermostat_rt_drf_drf_MON = 10
    channel_thermostat_mt_mmi_mmi_MON = 4
    channel_thermostat_mt_ma_ma_MON = 5
    channel_thermostat_mt_mmm_mmm_MON = 3
    channel_thermostat_mt_dmf_dmf_MON = 6
    channel_operator_interface_oip_oit_MON = 12
    channel_temperature_sensor_cpi_thermostat_MON = 2
    channel_heat_source_cpi_heat_controller_MON = 11
    channel_gumbo_monitor_process_gumbo_monitor_thread_MON = 14

    sdf.add_channel(Channel(a=scheduler, a_id=7, b=thermostat_rt_mri_mri_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_mri_mri_MON, a_id=1, b=thermostat_rt_mri_mri, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=9, b=thermostat_rt_mhs_mhs_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_mhs_mhs_MON, a_id=1, b=thermostat_rt_mhs_mhs, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=8, b=thermostat_rt_mrm_mrm_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_mrm_mrm_MON, a_id=1, b=thermostat_rt_mrm_mrm, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=10, b=thermostat_rt_drf_drf_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_drf_drf_MON, a_id=1, b=thermostat_rt_drf_drf, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=4, b=thermostat_mt_mmi_mmi_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_mmi_mmi_MON, a_id=1, b=thermostat_mt_mmi_mmi, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=5, b=thermostat_mt_ma_ma_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_ma_ma_MON, a_id=1, b=thermostat_mt_ma_ma, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=thermostat_mt_mmm_mmm_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_mmm_mmm_MON, a_id=1, b=thermostat_mt_mmm_mmm, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=6, b=thermostat_mt_dmf_dmf_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_dmf_dmf_MON, a_id=1, b=thermostat_mt_dmf_dmf, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=12, b=operator_interface_oip_oit_MON, b_id=0))
    sdf.add_channel(Channel(a=operator_interface_oip_oit_MON, a_id=1, b=operator_interface_oip_oit, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=2, b=temperature_sensor_cpi_thermostat_MON, b_id=0))
    sdf.add_channel(Channel(a=temperature_sensor_cpi_thermostat_MON, a_id=1, b=temperature_sensor_cpi_thermostat, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=11, b=heat_source_cpi_heat_controller_MON, b_id=0))
    sdf.add_channel(Channel(a=heat_source_cpi_heat_controller_MON, a_id=1, b=heat_source_cpi_heat_controller, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=14, b=gumbo_monitor_process_gumbo_monitor_thread_MON, b_id=0))
    sdf.add_channel(Channel(a=gumbo_monitor_process_gumbo_monitor_thread_MON, a_id=1, b=gumbo_monitor_process_gumbo_monitor_thread, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_gumbo_monitor_process_gumbo_monitor_thread_MON = (channel_gumbo_monitor_process_gumbo_monitor_thread_MON, 50000000, False)
    ts_temperature_sensor_cpi_thermostat_MON = (channel_temperature_sensor_cpi_thermostat_MON, 60000000, True)
    ts_thermostat_mt_mmm_mmm_MON = (channel_thermostat_mt_mmm_mmm_MON, 60000000, True)
    ts_thermostat_mt_mmi_mmi_MON = (channel_thermostat_mt_mmi_mmi_MON, 60000000, True)
    ts_thermostat_mt_ma_ma_MON = (channel_thermostat_mt_ma_ma_MON, 60000000, True)
    ts_thermostat_mt_dmf_dmf_MON = (channel_thermostat_mt_dmf_dmf_MON, 60000000, True)
    ts_thermostat_rt_mri_mri_MON = (channel_thermostat_rt_mri_mri_MON, 60000000, True)
    ts_thermostat_rt_mrm_mrm_MON = (channel_thermostat_rt_mrm_mrm_MON, 60000000, True)
    ts_thermostat_rt_mhs_mhs_MON = (channel_thermostat_rt_mhs_mhs_MON, 60000000, True)
    ts_thermostat_rt_drf_drf_MON = (channel_thermostat_rt_drf_drf_MON, 60000000, True)
    ts_heat_source_cpi_heat_controller_MON = (channel_heat_source_cpi_heat_controller_MON, 60000000, True)
    ts_operator_interface_oip_oit_MON = (channel_operator_interface_oip_oit_MON, 60000000, True)

    user_schedule = schedule(
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_temperature_sensor_cpi_thermostat_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_mmm_mmm_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_mmi_mmi_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_ma_ma_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_dmf_dmf_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_mri_mri_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_mrm_mrm_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_mhs_mhs_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_drf_drf_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_heat_source_cpi_heat_controller_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_operator_interface_oip_oit_MON
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
