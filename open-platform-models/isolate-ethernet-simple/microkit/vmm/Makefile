#
# Copyright 2021, Breakaway Consulting Pty. Ltd.
# Copyright 2022, UNSW (ABN 57 195 873 179)
# Copyright 2024, DornerWorks
#
# SPDX-License-Identifier: BSD-2-Clause
#

ifeq ($(strip $(MICROKIT_SDK)),)
$(error MICROKIT_SDK must be specified)
endif

ifeq ($(strip $(MICROKIT_BOARD)),)
$(error MICROKIT_BOARD must be specified)
endif

# All dependencies needed to compile the VMM
DTC := dtc
CC := clang
LD := ld.lld
AR := llvm-ar
MICROKIT_TOOL ?= $(MICROKIT_SDK)/bin/microkit

BUILD_DIR := ../build
CONFIG ?= debug
	
# @ivanv: need to have a step for putting in the initrd node into the DTB,
# 		  right now it is unfortunately hard-coded.

# @ivanv: check that the path of SDK_PATH/MICROKIT_BOARD exists
# @ivanv: Have a list of supported boards to check with, if it's not one of those
# have a helpful message that lists all the support boards.

# @ivanv: incremental builds don't work with IMAGE_DIR changing

MICROKIT_BOARD_DIR := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(CONFIG)
VMM := deps/libvmm
VMM_TOOLS := $(VMM)/tools
VMM_SRC_DIR := $(VMM)/src

IMAGE_DIR := board/$(MICROKIT_BOARD)
LINUX := $(IMAGE_DIR)/linux
DTS := $(IMAGE_DIR)/linux.dts
DTB := $(BUILD_DIR)/linux.dtb
INITRD := $(IMAGE_DIR)/rootfs.cpio.gz

IMAGE_URL := https://github.com/dornerworks/meta-inspecta-sut/releases/download
IMAGE_VER := v0.2.0-phase-1

# @ivanv: should only compile printf.o in debug
# VMM_OBJS := vmm.o printf.o virq.o linux.o guest.o psci.o smc.o fault.o util.o vgic.o vgic_v2.o package_guest_images.o tcb.o vcpu.o
# VMM_OBJS := vmm.o virq.o linux.o guest.o psci.o smc.o fault.o vmm_util.o vgic.o vgic_v2.o package_guest_images.o tcb.o vcpu.o net.o mmio.o
VMM_OBJS := vmm.o virq.o linux.o guest.o psci.o smc.o fault.o vgic.o vgic_v2.o package_guest_images.o tcb.o vcpu.o net.o mmio.o

# Toolchain flags
# FIXME: For optimisation we should consider providing the flag -mcpu.
# FIXME: We should also consider whether -mgeneral-regs-only should be
# used to avoid the use of the FPU and therefore seL4 does not have to
# context switch the FPU.
# Note we only need -Wno-unused-command-line-argument because in Nix
# passes an extra `--gcc-toolchain` flag which we do not need.
CFLAGS := -mstrict-align \
		  -g3 \
		  -O3 \
		  -ffreestanding \
		  -nostdlib \
		  -Wno-unused-command-line-argument \
		  -Wall -Wno-unused-function -Werror \
		  -I$(VMM)/include -I$(VMM_SRC_DIR) -I$(MICROKIT_BOARD_DIR)/include \
		  -DMICROKIT_BOARD_$(MICROKIT_BOARD) \
		  -DCONFIG_$(CONFIG) \
		  -target aarch64-none-elf

LDFLAGS := -L$(MICROKIT_BOARD_DIR)/lib
LIBS := -lmicrokit -Tmicrokit.ld

# all: directories $(BUILD_DIR)/vmm.elf
all: directories $(addprefix $(BUILD_DIR)/, $(VMM_OBJS))

directories:
	$(shell mkdir -p $(BUILD_DIR))

$(DTB): $(DTS)
ifeq ("", "$(shell which $(DTC))")
	$(error "Could not find dependency: Device Tree Compiler (dtc)")
endif
	# @ivanv: Shouldn't supress warnings
	$(DTC) -q -i $(IMAGE_DIR) -I dts -O dtb $< > $@

$(LINUX):
ifeq (zcu102,$(MICROKIT_BOARD))
	echo "Downloading VM kernel image for $(MICROKIT_BOARD)"
	wget -P $(IMAGE_DIR) $(IMAGE_URL)/$(IMAGE_VER)/linux
else
	echo "No VM kernel image at $(LINUX)"
endif

$(INITRD):
ifeq (zcu102,$(MICROKIT_BOARD))
	echo "Downloading VM kernel rootfs for $(MICROKIT_BOARD)"
	wget -P $(IMAGE_DIR) $(IMAGE_URL)/$(IMAGE_VER)/rootfs.cpio.gz
else
	echo "No VM kernel rootfs at $(INITRD)"
endif

$(BUILD_DIR)/package_guest_images.o: $(VMM_TOOLS)/package_guest_images.S $(IMAGE_DIR) $(LINUX) $(INITRD) $(DTB)
	$(CC) -c -g3 -x assembler-with-cpp \
					-DGUEST_KERNEL_IMAGE_PATH=\"$(LINUX)\" \
					-DGUEST_DTB_IMAGE_PATH=\"$(DTB)\" \
					-DGUEST_INITRD_IMAGE_PATH=\"$(INITRD)\" \
					-target aarch64-none-elf \
					$< -o $@

$(BUILD_DIR)/%.o: %.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/%.o: virtio/%.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/%.o: $(VMM_SRC_DIR)/%.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/vmm_util.o: $(VMM_SRC_DIR)/util/util.c Makefile
	$(CC) -c $(CFLAGS) $< -o $(BUILD_DIR)/vmm_util.o

$(BUILD_DIR)/mmio.o: $(VMM_SRC_DIR)/virtio/mmio.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/%.o: $(VMM_SRC_DIR)/arch/aarch64/%.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/%.o: $(VMM_SRC_DIR)/arch/aarch64/vgic/%.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

# $(BUILD_DIR)/vmm.elf: $(addprefix $(BUILD_DIR)/, $(VMM_OBJS))
# 	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

clean:
	rm -rf $(BUILD_DIR)
