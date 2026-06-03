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
    producer_p_p_producer_MON = ProtectionDomain(
      name="producer_p_p_producer_MON",
      program_image="producer_p_p_producer_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(producer_p_p_producer_MON)

    producer_p_p_producer = ProtectionDomain(
      name="producer_p_p_producer",
      program_image="producer_p_p_producer.elf",
      priority=140,
      passive=True)
    producer_p_p_producer_MON.add_child_pd(producer_p_p_producer, child_id=1)

    consumer_p_p_consumer_MON = ProtectionDomain(
      name="consumer_p_p_consumer_MON",
      program_image="consumer_p_p_consumer_MON.elf",
      priority=150,
      passive=True)
    scheduler.add_child_pd(consumer_p_p_consumer_MON)

    consumer_p_p_consumer = ProtectionDomain(
      name="consumer_p_p_consumer",
      program_image="consumer_p_p_consumer.elf",
      priority=140,
      passive=True)
    consumer_p_p_consumer_MON.add_child_pd(consumer_p_p_consumer, child_id=1)
    consumer_p_p_consumer.add_irq(IrqConventional(irq=33, id=1))

    #######################################
    # MEMORY REGIONS
    #######################################
    top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM = MemoryRegion(sdf, "top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM", 0x10_000_000, paddr=0x40_000_000)
    sdf.add_mr(top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM)
    top_impl_Instance_consumer_p_p_consumer_VM_GIC = MemoryRegion(sdf, "top_impl_Instance_consumer_p_p_consumer_VM_GIC", 0x1_000, paddr=0x8_040_000)
    sdf.add_mr(top_impl_Instance_consumer_p_p_consumer_VM_GIC)
    top_impl_Instance_consumer_p_p_consumer_VM_Serial = MemoryRegion(sdf, "top_impl_Instance_consumer_p_p_consumer_VM_Serial", 0x1_000, paddr=0x9_000_000)
    sdf.add_mr(top_impl_Instance_consumer_p_p_consumer_VM_Serial)
    top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region = MemoryRegion(sdf, "top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region", 0x1_000)
    sdf.add_mr(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region)

    consumer_p_p_consumer.add_map(Map(top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM, 0x40_000_000, perms="rw", setvar_vaddr="top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM_vaddr"))
    producer_p_p_producer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x10_000_000, perms="rw", setvar_vaddr="write_port_queue_1"))
    consumer_p_p_consumer.add_map(Map(top_impl_Instance_producer_p_p_producer_write_port_1_Memory_Region, 0x20_000_000, perms="r", setvar_vaddr="read_port_queue_1"))

    #######################################
    # VMMs
    #######################################
    consumer_p_p_consumer_VM_vm = VirtualMachine("consumer_p_p_consumer_VM", [VirtualMachine.Vcpu(id=0)])
    consumer_p_p_consumer_VM_vm.add_map(Map(top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM, 0x40_000_000, perms="rwx"))
    consumer_p_p_consumer_VM_vm.add_map(Map(top_impl_Instance_consumer_p_p_consumer_VM_GIC, 0x8_010_000, perms="rw", cached=False))
    consumer_p_p_consumer_VM_vm.add_map(Map(top_impl_Instance_consumer_p_p_consumer_VM_Serial, 0x9_000_000, perms="rw", cached=False))
    consumer_p_p_consumer.set_virtual_machine(consumer_p_p_consumer_VM_vm)

    #######################################
    # CHANNELS
    #######################################
    channel_producer_p_p_producer_MON = 2
    channel_consumer_p_p_consumer_MON = 3

    sdf.add_channel(Channel(a=scheduler, a_id=2, b=producer_p_p_producer_MON, b_id=0))
    sdf.add_channel(Channel(a=producer_p_p_producer_MON, a_id=1, b=producer_p_p_producer, b_id=0))
    sdf.add_channel(Channel(a=scheduler, a_id=3, b=consumer_p_p_consumer_MON, b_id=0))
    sdf.add_channel(Channel(a=consumer_p_p_consumer_MON, a_id=1, b=consumer_p_p_consumer, b_id=0))

    #######################################
    # SCHEDULE
    #######################################
    ts_producer_p_p_producer_MON = (channel_producer_p_p_producer_MON, 50000000, True)
    ts_consumer_p_p_consumer_MON = (channel_consumer_p_p_consumer_MON, 50000000, True)
    ts_pad = (0, 900000000, False)

    user_schedule = schedule(
      ts_producer_p_p_producer_MON,
      ts_consumer_p_p_consumer_MON,
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

    # Post-process sdf.render() to add page_size attributes not yet
    # supported by sdfgen's Python API.
    def add_page_size(xml_str, mappings):
        root = ET.fromstring(xml_str)
        for mr in root.iter('memory_region'):
            name = mr.get('name')
            if name in mappings:
                mr.set('page_size', mappings[name])
        ET.indent(root, space='  ')
        return ET.tostring(root, encoding='unicode', xml_declaration=True)

    page_size_mappings = {
        "top_impl_Instance_consumer_p_p_consumer_VM_Guest_RAM": "0x200_000"
    }

    with open(f"{output_dir}/{sdf_path}", "w+") as f:
        f.write(add_page_size(sdf.render(), page_size_mappings))


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
