# This file will not be overwritten if codegen is rerun

ifeq ($(strip $(MICROKIT_SDK)),)
$(error MICROKIT_SDK must be specified)
endif

ifeq ($(strip $(MICROKIT_BOARD)),)
$(error MICROKIT_BOARD must be specified)
endif

consumer_p_p_consumer_DIR := $(shell dirname $(shell pwd))

# @ivanv: need to have a step for putting in the initrd node into the DTB,
# 		  right now it is unfortunately hard-coded.

# @ivanv: check that the path of SDK_PATH/MICROKIT_BOARD exists
# @ivanv: Have a list of supported boards to check with, if it's not one of those
# have a helpful message that lists all the support boards.

# @ivanv: incremental builds don't work with LINUX_IMAGE_DIR changing

LIB_VMM_DIR ?= $(VMM_DIR)
ifeq ("$(wildcard $(LIB_VMM_DIR)/tools)","")
LIB_VMM_DIR := $(consumer_p_p_consumer_DIR)/libvmm
ifeq ("$(wildcard $(LIB_VMM_DIR)/tools)","")
$(error Set VMM_DIR to point to a copy of https://github.com/au-ts/libvmm")
endif
endif

LIB_VMM_TOOLS_DIR := $(LIB_VMM_DIR)/tools
LIB_VMM_SRC_DIR := $(LIB_VMM_DIR)/src
export SDDF=$(LIB_VMM_DIR)/dep/sddf

ARCH := aarch64
SDDF_CUSTOM_LIBC := 1

LINUX_IMAGE_DIR := $(consumer_p_p_consumer_DIR)/board/$(MICROKIT_BOARD)
LINUX := $(LINUX_IMAGE_DIR)/linux
DTS := $(LINUX_IMAGE_DIR)/linux.dts
DTB := $(TOP_BUILD_DIR)/linux.dtb
INITRD := $(LINUX_IMAGE_DIR)/rootfs.cpio.gz

LINUX_VER ?= 85000f3f42a882e4476e57003d53f2bbec8262b0-linux
INITRD_VER ?= 6dcd1debf64e6d69b178cd0f46b8c4ae7cebe2a5-rootfs.cpio.gz

# Toolchain flags
# FIXME: For optimisation we should consider providing the flag -mcpu.
# FIXME: We should also consider whether -mgeneral-regs-only should be
# used to avoid the use of the FPU and therefore seL4 does not have to
# context switch the FPU.
# Note we only need -Wno-unused-command-line-argument because in Nix
# passes an extra `--gcc-toolchain` flag which we do not need.
CFLAGS := \
	-mstrict-align \
	-ffreestanding \
	-g3 \
	-O3 \
	-Wno-unused-command-line-argument \
	-Wall -Wno-unused-function -Werror \
	-DMICROKIT_CONFIG_$(MICROKIT_CONFIG) \
	-DBOARD_$(MICROKIT_BOARD) \
	-I$(LIB_VMM_DIR)/include \
	-I$(MICROKIT_BOARD_DIR)/include \
	-I$(SDDF)/include \
	-I$(SDDF)/include/sddf/util \
	-I$(SDDF)/include/sddf/util/custom_libc \
	-I$(SDDF)/include/microkit \
	-MD \
	-MP \
	-target $(TARGET)

CHECK_FLAGS_BOARD_MD5:=.board_cflags-$(shell echo -- $(CFLAGS) $(MICROKIT_BOARD) $(MICROKIT_CONFIG) | shasum | sed 's/ *-//')

$(CHECK_FLAGS_BOARD_MD5):
	-rm -f .board_cflags-*
	touch $@

consumer_p_p_consumer_INCLUDE = -I$(consumer_p_p_consumer_DIR)/include

all: $(TOP_BUILD_DIR)/consumer_p_p_consumer.a

$(LINUX):
	curl -L https://trustworthy.systems/Downloads/libvmm/images/${LINUX_VER}.tar.gz -o $@.tar.gz
	tar -xf $@.tar.gz -C $(LINUX_IMAGE_DIR)
	cp $(LINUX_IMAGE_DIR)/$(LINUX_VER)/linux $@

$(INITRD):
	curl -L https://trustworthy.systems/Downloads/libvmm/images/${INITRD_VER}.tar.gz -o $@.tar.gz
	tar xf $@.tar.gz -C $(LINUX_IMAGE_DIR)
	cp $(LINUX_IMAGE_DIR)/$(INITRD_VER)/rootfs.cpio.gz $@

$(DTB):
ifeq ("", "$(shell which $(DTC))")
	$(error "Could not find dependency: Device Tree Compiler (dtc)")
endif
	$(LIB_VMM_TOOLS_DIR)/dtscat $(DTS) $(LINUX_IMAGE_DIR)/overlay.dts > $(TOP_BUILD_DIR)/linux.dts
	# @ivanv: Shouldn't supress warnings
	$(DTC) -q -I dts -O dtb $(TOP_BUILD_DIR)/linux.dts > $@

#$(TOP_BUILD_DIR)/package_guest_images.o: $(LIB_VMM_TOOLS_DIR)/package_guest_images.S $(LINUX_IMAGE_DIR) $(LINUX) $(INITRD) $(DTB)
$(TOP_BUILD_DIR)/package_guest_images.o: $(LIB_VMM_TOOLS_DIR)/package_guest_images.S $(LINUX) $(INITRD) $(DTB)
	$(CC) -c -g3 -x assembler-with-cpp \
		-DGUEST_KERNEL_IMAGE_PATH=\"$(LINUX)\" \
		-DGUEST_DTB_IMAGE_PATH=\"$(DTB)\" \
		-DGUEST_INITRD_IMAGE_PATH=\"$(INITRD)\" \
		-target $(TARGET) \
		$< \
		-o $@

# NOTE: the type libraries will be linked in via the top level makefile so here
# we just need to include $(TOP_TYPES_INCLUDE)
$(TOP_BUILD_DIR)/%.o: $(consumer_p_p_consumer_DIR)/src/%.c $(TOP_BUILD_DIR)/Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_TYPES_INCLUDE) $(consumer_p_p_consumer_INCLUDE)

-include vmm.d
include $(LIB_VMM_DIR)/vmm.mk
include $(SDDF)/util/util.mk
vpath %.c $(LIB_VMM_DIR)

$(TOP_BUILD_DIR)/consumer_p_p_consumer.a: libvmm.a libsddf_util.a $(TOP_BUILD_DIR)/consumer_p_p_consumer.o $(TOP_BUILD_DIR)/consumer_p_p_consumer_user.o $(TOP_BUILD_DIR)/package_guest_images.o Makefile
	mkdir -p libsddf_util; cd libsddf_util; $(AR) -x ../libsddf_util.a
	$(AR) r libvmm.a libsddf_util/*.o $(TOP_BUILD_DIR)/consumer_p_p_consumer.o $(TOP_BUILD_DIR)/consumer_p_p_consumer_user.o $(TOP_BUILD_DIR)/package_guest_images.o
	cp libvmm.a $@
