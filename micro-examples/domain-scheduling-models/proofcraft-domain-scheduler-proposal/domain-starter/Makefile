#
# Copyright 2025, Proofcraft Pty Ltd
# Copyright 2021, Breakaway Consulting Pty. Ltd.
#
# SPDX-License-Identifier: BSD-2-Clause
#

BUILD_DIR ?= tmp_build
MICROKIT_SDK ?= ~/microkit/release/microkit-sdk-1.4.1
MICROKIT_BOARD = odroidc4
MICROKIT_CONFIG ?= debug

TOOLCHAIN := aarch64-none-elf
CPU := cortex-a55

CC := $(TOOLCHAIN)-gcc
LD := $(TOOLCHAIN)-ld
AS := $(TOOLCHAIN)-as
MICROKIT_TOOL ?= $(MICROKIT_SDK)/bin/microkit

W1_OBJS := worker-1.o
W2_OBJS := worker-2.o
W3_OBJS := worker-3.o
STARTER_OBJS := starter.o

BOARD_DIR := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

IMAGES := starter.elf worker-1.elf worker-2.elf worker-3.elf
CFLAGS := -mcpu=$(CPU) -mstrict-align -nostdlib -ffreestanding -g -O3 -Wall  -Wno-unused-function -Werror -I$(BOARD_DIR)/include
LDFLAGS := -L$(BOARD_DIR)/lib
LIBS := -lmicrokit -Tmicrokit.ld

IMAGE_FILE = $(BUILD_DIR)/loader.img
REPORT_FILE = $(BUILD_DIR)/report.txt

all: $(IMAGE_FILE)

$(BUILD_DIR)/%.o: %.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/worker-1.elf: $(addprefix $(BUILD_DIR)/, $(W1_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(BUILD_DIR)/worker-2.elf: $(addprefix $(BUILD_DIR)/, $(W2_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(BUILD_DIR)/worker-3.elf: $(addprefix $(BUILD_DIR)/, $(W3_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(BUILD_DIR)/starter.elf: $(addprefix $(BUILD_DIR)/, $(STARTER_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(IMAGE_FILE) $(REPORT_FILE): $(addprefix $(BUILD_DIR)/, $(IMAGES)) domains.system
	$(MICROKIT_TOOL) domains.system --search-path $(BUILD_DIR) --board $(MICROKIT_BOARD) --config $(MICROKIT_CONFIG) -o $(IMAGE_FILE) -r $(REPORT_FILE)
