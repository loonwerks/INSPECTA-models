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
    # TEST SCHEDULER REGIONS
    # Command input, status output, and the published schedule.  The virtual addresses
    # must match TEST_CMD_VADDR / TEST_STATUS_VADDR / TEST_SCHEDULE_VADDR in
    # test_scheduler.scheduler_config.h.  Change both if there is a conflict.
    #######################################
    TEST_CMD_VADDR      = 0x4_002_000
    TEST_CMD_SIZE       = 0x1000  # 4 KB
    TEST_STATUS_VADDR   = 0x4_003_000
    TEST_STATUS_SIZE    = 0x1000  # 4 KB
    TEST_SCHEDULE_VADDR = 0x4_004_000
    TEST_SCHEDULE_SIZE  = 0x1000  # 4 KB

    test_cmd = MemoryRegion(sdf, "test_cmd", TEST_CMD_SIZE)
    sdf.add_mr(test_cmd)
    scheduler.add_map(Map(test_cmd, TEST_CMD_VADDR, perms="r"))

    test_status = MemoryRegion(sdf, "test_status", TEST_STATUS_SIZE)
    sdf.add_mr(test_status)
    scheduler.add_map(Map(test_status, TEST_STATUS_VADDR, perms="rw"))

    test_schedule = MemoryRegion(sdf, "test_schedule", TEST_SCHEDULE_SIZE)
    sdf.add_mr(test_schedule)
    scheduler.add_map(Map(test_schedule, TEST_SCHEDULE_VADDR, perms="rw"))
    #######################################
    # STATE VARIABLE INJECTION
    # One region per GUMBO state variable, carrying a value from the test
    # controller to the owning thread.  Declared here rather than as AADL
    # ports so they stay out of the component test harness and the system
    # verification model (design D16a).
    #######################################
    inj_thermostat_rt_mhs_mhs_sv_lastCmd = MemoryRegion(sdf, "inj_thermostat_rt_mhs_mhs_sv_lastCmd", 0x1000)
    sdf.add_mr(inj_thermostat_rt_mhs_mhs_sv_lastCmd)

    inj_thermostat_rt_mrm_mrm_sv_lastRegulatorMode = MemoryRegion(sdf, "inj_thermostat_rt_mrm_mrm_sv_lastRegulatorMode", 0x1000)
    sdf.add_mr(inj_thermostat_rt_mrm_mrm_sv_lastRegulatorMode)

    inj_thermostat_mt_mmi_mmi_sv_lastCmd = MemoryRegion(sdf, "inj_thermostat_mt_mmi_mmi_sv_lastCmd", 0x1000)
    sdf.add_mr(inj_thermostat_mt_mmi_mmi_sv_lastCmd)

    inj_thermostat_mt_ma_ma_sv_lastCmd = MemoryRegion(sdf, "inj_thermostat_mt_ma_ma_sv_lastCmd", 0x1000)
    sdf.add_mr(inj_thermostat_mt_ma_ma_sv_lastCmd)

    inj_thermostat_mt_mmm_mmm_sv_lastMonitorMode = MemoryRegion(sdf, "inj_thermostat_mt_mmm_mmm_sv_lastMonitorMode", 0x1000)
    sdf.add_mr(inj_thermostat_mt_mmm_mmm_sv_lastMonitorMode)

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

    test_controller_process_test_controller_thread_MON = ProtectionDomain(
      name="test_controller_process_test_controller_thread_MON",
      program_image="test_controller_process_test_controller_thread_MON.elf",
      priority=101,
      passive=True)
    scheduler.add_child_pd(test_controller_process_test_controller_thread_MON)

    test_controller_process_test_controller_thread = ProtectionDomain(
      name="test_controller_process_test_controller_thread",
      program_image="test_controller_process_test_controller_thread.elf",
      priority=100,
      passive=True,
      stack_size=0x10_000)
    test_controller_process_test_controller_thread_MON.add_child_pd(test_controller_process_test_controller_thread, child_id=1)


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
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x20_000_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="lower_desired_temp_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="lower_desired_temp_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x20_002_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="displayed_temp_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="display_temperature_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x20_004_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_006_000, perms="rw", setvar_vaddr="regulator_status_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="regulator_status_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x20_006_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_008_000, perms="rw", setvar_vaddr="interface_failure_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="interface_failure_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x20_008_000, perms="rw"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="heat_control_queue_1"))
    heat_source_cpi_heat_controller.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="heat_control_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x20_00A_000, perms="rw"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region, 0x10_006_000, perms="rw", setvar_vaddr="sv_lastCmd_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region, 0x20_00C_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_00A_000, perms="r", setvar_vaddr="regulator_mode_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="regulator_mode_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="regulator_mode_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x20_00E_000, perms="rw"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="sv_lastRegulatorMode_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region, 0x20_010_000, perms="rw"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="internal_failure_queue_1"))
    thermostat_rt_drf_drf.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="internal_failure_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x20_012_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="upper_alarm_temp_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="upper_alarm_temp_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x20_014_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="lower_alarm_temp_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="lower_alarm_temp_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x20_016_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="monitor_status_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_004_000, perms="r", setvar_vaddr="monitor_status_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x20_018_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_006_000, perms="rw", setvar_vaddr="interface_failure_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_000_000, perms="r", setvar_vaddr="interface_failure_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x20_01A_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region, 0x10_008_000, perms="rw", setvar_vaddr="sv_lastCmd_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region, 0x20_01C_000, perms="rw"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="alarm_control_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="alarm_control_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x20_01E_000, perms="rw"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region, 0x10_006_000, perms="rw", setvar_vaddr="sv_lastCmd_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region, 0x20_020_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_00A_000, perms="r", setvar_vaddr="monitor_mode_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="monitor_mode_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="monitor_mode_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x20_022_000, perms="rw"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region, 0x10_004_000, perms="rw", setvar_vaddr="sv_lastMonitorMode_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region, 0x20_024_000, perms="rw"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_006_000, perms="r", setvar_vaddr="internal_failure_queue_1"))
    thermostat_mt_dmf_dmf.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="internal_failure_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x20_026_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_00C_000, perms="r", setvar_vaddr="lower_desired_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_008_000, perms="rw", setvar_vaddr="lower_desired_tempWstatus_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x20_028_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_00E_000, perms="r", setvar_vaddr="upper_desired_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_00A_000, perms="rw", setvar_vaddr="upper_desired_tempWstatus_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x20_02A_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_00C_000, perms="r", setvar_vaddr="lower_alarm_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_00C_000, perms="rw", setvar_vaddr="lower_alarm_tempWstatus_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x20_02C_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_00E_000, perms="r", setvar_vaddr="upper_alarm_tempWstatus_queue_1"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_00E_000, perms="rw", setvar_vaddr="upper_alarm_tempWstatus_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x20_02E_000, perms="rw"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_010_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_00A_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_010_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_00A_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_008_000, perms="r", setvar_vaddr="current_tempWstatus_queue_1"))
    temperature_sensor_cpi_thermostat.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="current_tempWstatus_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x20_030_000, perms="rw"))
    temperature_sensor_cpi_thermostat.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region, 0x10_002_000, perms="r", setvar_vaddr="air_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region, 0x20_032_000, perms="rw"))
    heat_source_cpi_heat_controller.add_map(Map(Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region, 0x10_002_000, perms="rw", setvar_vaddr="heat_out_queue_1"))
    test_controller_process_test_controller_thread.add_map(Map(Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region, 0x20_034_000, perms="rw"))
    test_controller_process_test_controller_thread.add_map(Map(test_cmd, 0x4_002_000, perms="rw"))
    test_controller_process_test_controller_thread.add_map(Map(test_status, 0x4_003_000, perms="r"))
    test_controller_process_test_controller_thread.add_map(Map(test_schedule, 0x4_004_000, perms="r"))
    thermostat_rt_mhs_mhs.add_map(Map(inj_thermostat_rt_mhs_mhs_sv_lastCmd, 0x20_800_000, perms="r", setvar_vaddr="inj_sv_lastCmd_queue"))
    test_controller_process_test_controller_thread.add_map(Map(inj_thermostat_rt_mhs_mhs_sv_lastCmd, 0x20_400_000, perms="rw"))
    thermostat_rt_mrm_mrm.add_map(Map(inj_thermostat_rt_mrm_mrm_sv_lastRegulatorMode, 0x20_802_000, perms="r", setvar_vaddr="inj_sv_lastRegulatorMode_queue"))
    test_controller_process_test_controller_thread.add_map(Map(inj_thermostat_rt_mrm_mrm_sv_lastRegulatorMode, 0x20_402_000, perms="rw"))
    thermostat_mt_mmi_mmi.add_map(Map(inj_thermostat_mt_mmi_mmi_sv_lastCmd, 0x20_804_000, perms="r", setvar_vaddr="inj_sv_lastCmd_queue"))
    test_controller_process_test_controller_thread.add_map(Map(inj_thermostat_mt_mmi_mmi_sv_lastCmd, 0x20_404_000, perms="rw"))
    thermostat_mt_ma_ma.add_map(Map(inj_thermostat_mt_ma_ma_sv_lastCmd, 0x20_806_000, perms="r", setvar_vaddr="inj_sv_lastCmd_queue"))
    test_controller_process_test_controller_thread.add_map(Map(inj_thermostat_mt_ma_ma_sv_lastCmd, 0x20_406_000, perms="rw"))
    thermostat_mt_mmm_mmm.add_map(Map(inj_thermostat_mt_mmm_mmm_sv_lastMonitorMode, 0x20_808_000, perms="r", setvar_vaddr="inj_sv_lastMonitorMode_queue"))
    test_controller_process_test_controller_thread.add_map(Map(inj_thermostat_mt_mmm_mmm_sv_lastMonitorMode, 0x20_408_000, perms="rw"))



    #######################################
    # CHANNELS
    #######################################
    channel_thermostat_rt_mri_mri_MON = 9
    channel_thermostat_rt_mhs_mhs_MON = 11
    channel_thermostat_rt_mrm_mrm_MON = 10
    channel_thermostat_rt_drf_drf_MON = 8
    channel_thermostat_mt_mmi_mmi_MON = 5
    channel_thermostat_mt_ma_ma_MON = 7
    channel_thermostat_mt_mmm_mmm_MON = 6
    channel_thermostat_mt_dmf_dmf_MON = 4
    channel_operator_interface_oip_oit_MON = 2
    channel_temperature_sensor_cpi_thermostat_MON = 3
    channel_heat_source_cpi_heat_controller_MON = 12
    channel_test_controller_process_test_controller_thread_MON = 14

    sdf.add_channel(Channel(a=scheduler, a_id=9, b=thermostat_rt_mri_mri_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_mri_mri_MON, a_id=1, b=thermostat_rt_mri_mri, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=11, b=thermostat_rt_mhs_mhs_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_mhs_mhs_MON, a_id=1, b=thermostat_rt_mhs_mhs, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=10, b=thermostat_rt_mrm_mrm_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_mrm_mrm_MON, a_id=1, b=thermostat_rt_mrm_mrm, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=8, b=thermostat_rt_drf_drf_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_rt_drf_drf_MON, a_id=1, b=thermostat_rt_drf_drf, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=5, b=thermostat_mt_mmi_mmi_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_mmi_mmi_MON, a_id=1, b=thermostat_mt_mmi_mmi, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=7, b=thermostat_mt_ma_ma_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_ma_ma_MON, a_id=1, b=thermostat_mt_ma_ma, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=6, b=thermostat_mt_mmm_mmm_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_mmm_mmm_MON, a_id=1, b=thermostat_mt_mmm_mmm, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=4, b=thermostat_mt_dmf_dmf_MON, b_id=0))
    sdf.add_channel(Channel(a=thermostat_mt_dmf_dmf_MON, a_id=1, b=thermostat_mt_dmf_dmf, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=2, b=operator_interface_oip_oit_MON, b_id=0))
    sdf.add_channel(Channel(a=operator_interface_oip_oit_MON, a_id=1, b=operator_interface_oip_oit, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=temperature_sensor_cpi_thermostat_MON, b_id=0))
    sdf.add_channel(Channel(a=temperature_sensor_cpi_thermostat_MON, a_id=1, b=temperature_sensor_cpi_thermostat, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=12, b=heat_source_cpi_heat_controller_MON, b_id=0))
    sdf.add_channel(Channel(a=heat_source_cpi_heat_controller_MON, a_id=1, b=heat_source_cpi_heat_controller, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=14, b=test_controller_process_test_controller_thread_MON, b_id=0))
    sdf.add_channel(Channel(a=test_controller_process_test_controller_thread_MON, a_id=1, b=test_controller_process_test_controller_thread, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_pad = (0, 340000000, False)
    ts_operator_interface_oip_oit_MON = (channel_operator_interface_oip_oit_MON, 60000000, True)
    ts_temperature_sensor_cpi_thermostat_MON = (channel_temperature_sensor_cpi_thermostat_MON, 60000000, True)
    ts_thermostat_mt_dmf_dmf_MON = (channel_thermostat_mt_dmf_dmf_MON, 60000000, True)
    ts_thermostat_mt_mmi_mmi_MON = (channel_thermostat_mt_mmi_mmi_MON, 60000000, True)
    ts_thermostat_mt_mmm_mmm_MON = (channel_thermostat_mt_mmm_mmm_MON, 60000000, True)
    ts_thermostat_mt_ma_ma_MON = (channel_thermostat_mt_ma_ma_MON, 60000000, True)
    ts_thermostat_rt_drf_drf_MON = (channel_thermostat_rt_drf_drf_MON, 60000000, True)
    ts_thermostat_rt_mri_mri_MON = (channel_thermostat_rt_mri_mri_MON, 60000000, True)
    ts_thermostat_rt_mrm_mrm_MON = (channel_thermostat_rt_mrm_mrm_MON, 60000000, True)
    ts_thermostat_rt_mhs_mhs_MON = (channel_thermostat_rt_mhs_mhs_MON, 60000000, True)
    ts_heat_source_cpi_heat_controller_MON = (channel_heat_source_cpi_heat_controller_MON, 60000000, True)

    user_schedule = schedule(
      ts_pad,
      ts_operator_interface_oip_oit_MON,
      ts_temperature_sensor_cpi_thermostat_MON,
      ts_thermostat_mt_dmf_dmf_MON,
      ts_thermostat_mt_mmi_mmi_MON,
      ts_thermostat_mt_mmm_mmm_MON,
      ts_thermostat_mt_ma_ma_MON,
      ts_thermostat_rt_drf_drf_MON,
      ts_thermostat_rt_mri_mri_MON,
      ts_thermostat_rt_mrm_mrm_MON,
      ts_thermostat_rt_mhs_mhs_MON,
      ts_heat_source_cpi_heat_controller_MON
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

    #######################################
    # TEST SELECTION
    # Which system tests to run, from the TESTS make variable.  Substring match
    # against the qualified suite::test name; empty selects all of them.
    #######################################
    test_selection = bytearray(260)
    _filter = tests_filter.encode()[:255]
    test_selection[0:len(_filter)] = _filter
    test_selection_path = output_dir + "/test_selection.data"
    with open(test_selection_path, "wb+") as f:
        f.write(bytes(test_selection))
    update_elf_section(obj_copy, test_controller_process_test_controller_thread.program_image,
                       "test_selection",
                       test_selection_path)

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
    # Substring filter selecting which system tests to run; empty means all of
    # them.  Only the test scheduler variant consumes it.
    parser.add_argument("--tests", required=False, default="")

    args = parser.parse_args()

    # Import the config structs module from the build directory
    sys.path.append(args.output)
    from config_structs import *

    board = next(filter(lambda b: b.name == args.board, BOARDS))

    sdf = SystemDescription(board.arch, board.paddr_top)
    sddf = Sddf(args.sddf)

    global obj_copy
    obj_copy = args.objcopy

    global tests_filter
    tests_filter = args.tests

    with open(args.dtb, "rb") as f:
        dtb = DeviceTree(f.read())

    generate(args.sdf, args.output, dtb)
