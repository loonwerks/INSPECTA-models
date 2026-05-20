# Copyright 2025, UNSW
# SPDX-License-Identifier: BSD-2-Clause
import argparse
import struct
import xml.etree.ElementTree as ET
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
    entries: sequence of (channel, timeslice_ns)
    """
    part_ch, part_timeslices, is_user_partition = zip(*entries)
    return UserSchedule(list(part_timeslices), list(part_ch), list(is_user_partition))

def add_setvar_vaddr(xml_str, mappings):
    """
    Post-process sdf.render() output to add setvar_vaddr attributes.
    sdfgen v27-29 does not support setvar_vaddr natively.
    mappings: dict of (pd_name, mr_name) -> var_name
    """
    root = ET.fromstring(xml_str)
    for pd in root.iter('protection_domain'):
        pd_name = pd.get('name')
        for m in pd.findall('map'):
            key = (pd_name, m.get('mr'))
            if key in mappings:
                m.set('setvar_vaddr', mappings[key])
    ET.indent(root, space='  ')
    return ET.tostring(root, encoding='unicode', xml_declaration=True)

setvar_mappings = {
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region"): "upper_desired_temp_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region"): "lower_desired_temp_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region"): "displayed_temp_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region"): "regulator_status_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region"): "interface_failure_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region"): "regulator_mode_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region"): "lower_desired_tempWstatus_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region"): "upper_desired_tempWstatus_queue_1",
    ("thermostat_rt_mri_mri", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("thermostat_rt_mhs_mhs", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region"): "upper_desired_temp_queue_1",
    ("thermostat_rt_mhs_mhs", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region"): "lower_desired_temp_queue_1",
    ("thermostat_rt_mhs_mhs", "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region"): "heat_control_queue_1",
    ("thermostat_rt_mhs_mhs", "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region"): "sv_lastCmd_queue_1",
    ("thermostat_rt_mhs_mhs", "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region"): "regulator_mode_queue_1",
    ("thermostat_rt_mhs_mhs", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("thermostat_rt_mrm_mrm", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region"): "interface_failure_queue_1",
    ("thermostat_rt_mrm_mrm", "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region"): "regulator_mode_queue_1",
    ("thermostat_rt_mrm_mrm", "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region"): "sv_lastRegulatorMode_queue_1",
    ("thermostat_rt_mrm_mrm", "Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region"): "internal_failure_queue_1",
    ("thermostat_rt_mrm_mrm", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("thermostat_rt_drf_drf", "Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region"): "internal_failure_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region"): "upper_alarm_temp_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region"): "lower_alarm_temp_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region"): "monitor_status_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region"): "interface_failure_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region"): "sv_lastCmd_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region"): "monitor_mode_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region"): "lower_alarm_tempWstatus_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region"): "upper_alarm_tempWstatus_queue_1",
    ("thermostat_mt_mmi_mmi", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("thermostat_mt_ma_ma", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region"): "upper_alarm_temp_queue_1",
    ("thermostat_mt_ma_ma", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region"): "lower_alarm_temp_queue_1",
    ("thermostat_mt_ma_ma", "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region"): "alarm_control_queue_1",
    ("thermostat_mt_ma_ma", "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region"): "sv_lastCmd_queue_1",
    ("thermostat_mt_ma_ma", "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region"): "monitor_mode_queue_1",
    ("thermostat_mt_ma_ma", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("thermostat_mt_mmm_mmm", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region"): "interface_failure_queue_1",
    ("thermostat_mt_mmm_mmm", "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region"): "monitor_mode_queue_1",
    ("thermostat_mt_mmm_mmm", "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region"): "sv_lastMonitorMode_queue_1",
    ("thermostat_mt_mmm_mmm", "Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region"): "internal_failure_queue_1",
    ("thermostat_mt_mmm_mmm", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("thermostat_mt_dmf_dmf", "Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region"): "internal_failure_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region"): "display_temperature_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region"): "regulator_status_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region"): "monitor_status_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region"): "alarm_control_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region"): "lower_desired_tempWstatus_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region"): "upper_desired_tempWstatus_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region"): "lower_alarm_tempWstatus_queue_1",
    ("operator_interface_oip_oit", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region"): "upper_alarm_tempWstatus_queue_1",
    ("temperature_sensor_cpi_thermostat", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "current_tempWstatus_queue_1",
    ("temperature_sensor_cpi_thermostat", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region"): "air_queue_1",
    ("heat_source_cpi_heat_controller", "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region"): "heat_control_queue_1",
    ("heat_source_cpi_heat_controller", "Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region"): "heat_out_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region"): "thermostat_rt_mri_mri_upper_desired_temp_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region"): "thermostat_rt_mri_mri_lower_desired_temp_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region"): "thermostat_rt_mri_mri_displayed_temp_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region"): "thermostat_rt_mri_mri_regulator_status_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region"): "thermostat_rt_mri_mri_interface_failure_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region"): "thermostat_rt_mhs_mhs_heat_control_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region"): "thermostat_rt_mhs_mhs_sv_lastCmd_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region"): "thermostat_rt_mrm_mrm_regulator_mode_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region"): "thermostat_rt_mrm_mrm_sv_lastRegulatorMode_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region"): "thermostat_rt_drf_drf_internal_failure_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region"): "thermostat_mt_mmi_mmi_upper_alarm_temp_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region"): "thermostat_mt_mmi_mmi_lower_alarm_temp_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region"): "thermostat_mt_mmi_mmi_monitor_status_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region"): "thermostat_mt_mmi_mmi_interface_failure_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region"): "thermostat_mt_mmi_mmi_sv_lastCmd_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region"): "thermostat_mt_ma_ma_alarm_control_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region"): "thermostat_mt_ma_ma_sv_lastCmd_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region"): "thermostat_mt_mmm_mmm_monitor_mode_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region"): "thermostat_mt_mmm_mmm_sv_lastMonitorMode_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region"): "thermostat_mt_dmf_dmf_internal_failure_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region"): "operator_interface_oip_oit_lower_desired_tempWstatus_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region"): "operator_interface_oip_oit_upper_desired_tempWstatus_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region"): "operator_interface_oip_oit_lower_alarm_tempWstatus_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region"): "operator_interface_oip_oit_upper_alarm_tempWstatus_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region"): "temperature_sensor_cpi_thermostat_current_tempWstatus_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "sched_state"): "sched_state_queue_1",
    ("gumbo_monitor_process_gumbo_monitor_thread", "sched_schedule"): "sched_schedule_queue_1"
}

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
    thermostat_rt_mri_mri_MON = ProtectionDomain("thermostat_rt_mri_mri_MON", "thermostat_rt_mri_mri_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_rt_mri_mri_MON)
    thermostat_rt_mhs_mhs_MON = ProtectionDomain("thermostat_rt_mhs_mhs_MON", "thermostat_rt_mhs_mhs_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_rt_mhs_mhs_MON)
    thermostat_rt_mrm_mrm_MON = ProtectionDomain("thermostat_rt_mrm_mrm_MON", "thermostat_rt_mrm_mrm_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_rt_mrm_mrm_MON)
    thermostat_rt_drf_drf_MON = ProtectionDomain("thermostat_rt_drf_drf_MON", "thermostat_rt_drf_drf_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_rt_drf_drf_MON)
    thermostat_mt_mmi_mmi_MON = ProtectionDomain("thermostat_mt_mmi_mmi_MON", "thermostat_mt_mmi_mmi_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_mt_mmi_mmi_MON)
    thermostat_mt_ma_ma_MON = ProtectionDomain("thermostat_mt_ma_ma_MON", "thermostat_mt_ma_ma_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_mt_ma_ma_MON)
    thermostat_mt_mmm_mmm_MON = ProtectionDomain("thermostat_mt_mmm_mmm_MON", "thermostat_mt_mmm_mmm_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_mt_mmm_mmm_MON)
    thermostat_mt_dmf_dmf_MON = ProtectionDomain("thermostat_mt_dmf_dmf_MON", "thermostat_mt_dmf_dmf_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(thermostat_mt_dmf_dmf_MON)
    operator_interface_oip_oit_MON = ProtectionDomain("operator_interface_oip_oit_MON", "operator_interface_oip_oit_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(operator_interface_oip_oit_MON)
    temperature_sensor_cpi_thermostat_MON = ProtectionDomain("temperature_sensor_cpi_thermostat_MON", "temperature_sensor_cpi_thermostat_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(temperature_sensor_cpi_thermostat_MON)
    heat_source_cpi_heat_controller_MON = ProtectionDomain("heat_source_cpi_heat_controller_MON", "heat_source_cpi_heat_controller_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(heat_source_cpi_heat_controller_MON)
    gumbo_monitor_process_gumbo_monitor_thread_MON = ProtectionDomain("gumbo_monitor_process_gumbo_monitor_thread_MON", "gumbo_monitor_process_gumbo_monitor_thread_MON.elf", priority=150, passive=True)
    scheduler.add_child_pd(gumbo_monitor_process_gumbo_monitor_thread_MON)

    thermostat_rt_mri_mri = ProtectionDomain("thermostat_rt_mri_mri", "thermostat_rt_mri_mri.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_rt_mri_mri)
    thermostat_rt_mhs_mhs = ProtectionDomain("thermostat_rt_mhs_mhs", "thermostat_rt_mhs_mhs.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_rt_mhs_mhs)
    thermostat_rt_mrm_mrm = ProtectionDomain("thermostat_rt_mrm_mrm", "thermostat_rt_mrm_mrm.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_rt_mrm_mrm)
    thermostat_rt_drf_drf = ProtectionDomain("thermostat_rt_drf_drf", "thermostat_rt_drf_drf.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_rt_drf_drf)
    thermostat_mt_mmi_mmi = ProtectionDomain("thermostat_mt_mmi_mmi", "thermostat_mt_mmi_mmi.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_mt_mmi_mmi)
    thermostat_mt_ma_ma = ProtectionDomain("thermostat_mt_ma_ma", "thermostat_mt_ma_ma.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_mt_ma_ma)
    thermostat_mt_mmm_mmm = ProtectionDomain("thermostat_mt_mmm_mmm", "thermostat_mt_mmm_mmm.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_mt_mmm_mmm)
    thermostat_mt_dmf_dmf = ProtectionDomain("thermostat_mt_dmf_dmf", "thermostat_mt_dmf_dmf.elf", priority=140, passive=True)
    scheduler.add_child_pd(thermostat_mt_dmf_dmf)
    operator_interface_oip_oit = ProtectionDomain("operator_interface_oip_oit", "operator_interface_oip_oit.elf", priority=140, passive=True)
    scheduler.add_child_pd(operator_interface_oip_oit)
    temperature_sensor_cpi_thermostat = ProtectionDomain("temperature_sensor_cpi_thermostat", "temperature_sensor_cpi_thermostat.elf", priority=140, passive=True)
    scheduler.add_child_pd(temperature_sensor_cpi_thermostat)
    heat_source_cpi_heat_controller = ProtectionDomain("heat_source_cpi_heat_controller", "heat_source_cpi_heat_controller.elf", priority=140, passive=True)
    scheduler.add_child_pd(heat_source_cpi_heat_controller)
    gumbo_monitor_process_gumbo_monitor_thread = ProtectionDomain("gumbo_monitor_process_gumbo_monitor_thread", "gumbo_monitor_process_gumbo_monitor_thread.elf", priority=140, passive=True)
    scheduler.add_child_pd(gumbo_monitor_process_gumbo_monitor_thread)

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

    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x10_000_000, perms="rw"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x10_000_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_upper_desired_temp_1_Memory_Region, 0x10_000_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_001_000, perms="rw"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_001_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_lower_desired_temp_1_Memory_Region, 0x10_001_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_002_000, perms="rw"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_000_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_displayed_temp_1_Memory_Region, 0x10_002_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_003_000, perms="rw"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_001_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_regulator_status_1_Memory_Region, 0x10_003_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_004_000, perms="rw"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_000_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri_interface_failure_1_Memory_Region, 0x10_004_000, perms="r"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_002_000, perms="rw"))
    heat_source_cpi_heat_controller.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_000_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_heat_control_1_Memory_Region, 0x10_005_000, perms="r"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region, 0x10_003_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs_sv_lastCmd_1_Memory_Region, 0x10_006_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_005_000, perms="r"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_004_000, perms="r"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_001_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_regulator_mode_1_Memory_Region, 0x10_007_000, perms="r"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region, 0x10_002_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm_sv_lastRegulatorMode_1_Memory_Region, 0x10_008_000, perms="r"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_003_000, perms="r"))
    thermostat_rt_drf_drf.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_000_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf_internal_failure_1_Memory_Region, 0x10_009_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_000_000, perms="rw"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_000_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_upper_alarm_temp_1_Memory_Region, 0x10_00A_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_001_000, perms="rw"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_001_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_lower_alarm_temp_1_Memory_Region, 0x10_00B_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_002_000, perms="rw"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_002_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_monitor_status_1_Memory_Region, 0x10_00C_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_003_000, perms="rw"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_000_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_interface_failure_1_Memory_Region, 0x10_00D_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region, 0x10_004_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_sv_lastCmd_1_Memory_Region, 0x10_00E_000, perms="r"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_002_000, perms="rw"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_003_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_alarm_control_1_Memory_Region, 0x10_00F_000, perms="r"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region, 0x10_003_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma_sv_lastCmd_1_Memory_Region, 0x10_010_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_005_000, perms="r"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_004_000, perms="r"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_001_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_monitor_mode_1_Memory_Region, 0x10_011_000, perms="r"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region, 0x10_002_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm_sv_lastMonitorMode_1_Memory_Region, 0x10_012_000, perms="r"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_003_000, perms="r"))
    thermostat_mt_dmf_dmf.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_000_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf_internal_failure_1_Memory_Region, 0x10_013_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_006_000, perms="r"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_004_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_desired_tempWstatus_1_Memory_Region, 0x10_014_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_007_000, perms="r"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_005_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_desired_tempWstatus_1_Memory_Region, 0x10_015_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_006_000, perms="r"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_006_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_lower_alarm_tempWstatus_1_Memory_Region, 0x10_016_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_007_000, perms="r"))
    operator_interface_oip_oit.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_007_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_operator_interface_oip_oit_upper_alarm_tempWstatus_1_Memory_Region, 0x10_017_000, perms="r"))
    thermostat_rt_mri_mri.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_008_000, perms="r"))
    thermostat_rt_mhs_mhs.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_005_000, perms="r"))
    thermostat_rt_mrm_mrm.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_004_000, perms="r"))
    thermostat_mt_mmi_mmi.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_008_000, perms="r"))
    thermostat_mt_ma_ma.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_005_000, perms="r"))
    thermostat_mt_mmm_mmm.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_004_000, perms="r"))
    temperature_sensor_cpi_thermostat.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_000_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_current_tempWstatus_1_Memory_Region, 0x10_018_000, perms="r"))
    temperature_sensor_cpi_thermostat.add_map(Map(Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat_air_1_Memory_Region, 0x10_001_000, perms="r"))
    heat_source_cpi_heat_controller.add_map(Map(Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller_heat_out_1_Memory_Region, 0x10_001_000, perms="rw"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(sched_state, 0x10_019_000, perms="r"))
    gumbo_monitor_process_gumbo_monitor_thread.add_map(Map(sched_schedule, 0x10_01A_000, perms="r"))

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
    channel_gumbo_monitor_process_gumbo_monitor_thread_MON = 14

    sdf.add_channel(Channel(scheduler, thermostat_rt_mri_mri_MON, a_id=channel_thermostat_rt_mri_mri_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_rt_mri_mri_MON, thermostat_rt_mri_mri, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_rt_mhs_mhs_MON, a_id=channel_thermostat_rt_mhs_mhs_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_rt_mhs_mhs_MON, thermostat_rt_mhs_mhs, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_rt_mrm_mrm_MON, a_id=channel_thermostat_rt_mrm_mrm_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_rt_mrm_mrm_MON, thermostat_rt_mrm_mrm, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_rt_drf_drf_MON, a_id=channel_thermostat_rt_drf_drf_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_rt_drf_drf_MON, thermostat_rt_drf_drf, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_mt_mmi_mmi_MON, a_id=channel_thermostat_mt_mmi_mmi_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_mt_mmi_mmi_MON, thermostat_mt_mmi_mmi, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_mt_ma_ma_MON, a_id=channel_thermostat_mt_ma_ma_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_mt_ma_ma_MON, thermostat_mt_ma_ma, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_mt_mmm_mmm_MON, a_id=channel_thermostat_mt_mmm_mmm_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_mt_mmm_mmm_MON, thermostat_mt_mmm_mmm, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, thermostat_mt_dmf_dmf_MON, a_id=channel_thermostat_mt_dmf_dmf_MON, b_id=0))
    sdf.add_channel(Channel(thermostat_mt_dmf_dmf_MON, thermostat_mt_dmf_dmf, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, operator_interface_oip_oit_MON, a_id=channel_operator_interface_oip_oit_MON, b_id=0))
    sdf.add_channel(Channel(operator_interface_oip_oit_MON, operator_interface_oip_oit, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, temperature_sensor_cpi_thermostat_MON, a_id=channel_temperature_sensor_cpi_thermostat_MON, b_id=0))
    sdf.add_channel(Channel(temperature_sensor_cpi_thermostat_MON, temperature_sensor_cpi_thermostat, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, heat_source_cpi_heat_controller_MON, a_id=channel_heat_source_cpi_heat_controller_MON, b_id=0))
    sdf.add_channel(Channel(heat_source_cpi_heat_controller_MON, heat_source_cpi_heat_controller, a_id=1, b_id=0))
    sdf.add_channel(Channel(scheduler, gumbo_monitor_process_gumbo_monitor_thread_MON, a_id=channel_gumbo_monitor_process_gumbo_monitor_thread_MON, b_id=0))
    sdf.add_channel(Channel(gumbo_monitor_process_gumbo_monitor_thread_MON, gumbo_monitor_process_gumbo_monitor_thread, a_id=1, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_gumbo_monitor_process_gumbo_monitor_thread_MON = (channel_gumbo_monitor_process_gumbo_monitor_thread_MON, 50000000, False)
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
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_operator_interface_oip_oit_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_temperature_sensor_cpi_thermostat_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_dmf_dmf_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_mmi_mmi_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_mmm_mmm_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_mt_ma_ma_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_drf_drf_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_mri_mri_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_mrm_mrm_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
      ts_thermostat_rt_mhs_mhs_MON,
      ts_gumbo_monitor_process_gumbo_monitor_thread_MON,
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

    with open(f"{output_dir}/{sdf_path}", "w+") as f:
        f.write(add_setvar_vaddr(sdf.render(), setvar_mappings))


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
