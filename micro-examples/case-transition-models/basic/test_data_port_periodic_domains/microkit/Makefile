# If you would like to choose a different path to the SDK, you can pass it as an
# argument.
ifndef MICROKIT_SDK
	MICROKIT_SDK := ../sdk
endif

# In case the default compiler triple doesn't work for you or your package manager
# only has aarch64-none-elf or something, you can specifiy the toolchain.
ifndef TOOLCHAIN
	# Get whether the common toolchain triples exist
	TOOLCHAIN_AARCH64_LINUX_GNU := $(shell command -v aarch64-linux-gnu-gcc 2> /dev/null)
	TOOLCHAIN_AARCH64_UNKNOWN_LINUX_GNU := $(shell command -v aarch64-unknown-linux-gnu-gcc 2> /dev/null)
	# Then check if they are defined and select the appropriate one
	ifdef TOOLCHAIN_AARCH64_LINUX_GNU
		TOOLCHAIN := aarch64-linux-gnu
	else ifdef TOOLCHAIN_AARCH64_UNKNOWN_LINUX_GNU
		TOOLCHAIN := aarch64-unknown-linux-gnu
	else
		$(error "Could not find an AArch64 cross-compiler")
	endif
endif

BOARD := qemu_arm_virt
MICROKIT_CONFIG := debug
BUILD_DIR := build

CPU := cortex-a53

CC := $(TOOLCHAIN)-gcc
LD := $(TOOLCHAIN)-ld
AS := $(TOOLCHAIN)-as
MICROKIT_TOOL ?= $(MICROKIT_SDK)/bin/microkit

PRINTF_OBJS := printf.o util.o
PERIODIC_DISPATCHER_OBJS := $(PRINTF_OBJS) periodic_dispatcher.o
PRODUCER_OBJS := $(PRINTF_OBJS) producer.o
CONSUMER_OBJS := $(PRINTF_OBJS) consumer.o
END_OF_SCHEDULE_PING_OBJS := $(PRINTF_OBJS) end_of_schedule_ping.o

BOARD_DIR := $(MICROKIT_SDK)/board/$(BOARD)/$(MICROKIT_CONFIG)

IMAGES := periodic_dispatcher.elf producer.elf consumer.elf end_of_schedule_ping.elf

# Note that these warnings being disabled is to avoid compilation errors while in the middle of completing each exercise part
CFLAGS := -mcpu=$(CPU) -mstrict-align -nostdlib -ffreestanding -g -Wall -Wno-array-bounds -Wno-unused-variable -Wno-unused-function -Werror -I$(BOARD_DIR)/include -Iinclude -DBOARD_$(BOARD)
LDFLAGS := -L$(BOARD_DIR)/lib
LIBS := -lmicrokit -Tmicrokit.ld

IMAGE_FILE_DATAPORT = $(BUILD_DIR)/dataport.img
IMAGE_FILE_LOADER = $(BUILD_DIR)/loader.img
REPORT_FILE = $(BUILD_DIR)/report.txt

all: directories $(IMAGE_FILE_LOADER)

directories:
	$(info $(shell mkdir -p $(BUILD_DIR)))

run: directories $(IMAGE_FILE_LOADER)
	qemu-system-aarch64 -machine virt,virtualization=on \
		-cpu $(CPU) \
		-serial mon:stdio \
		-device loader,file=$(IMAGE_FILE_LOADER),addr=0x70000000,cpu-num=0 \
		-m size=2G \
		-nographic \
		-netdev user,id=mynet0 \
		-device virtio-net-device,netdev=mynet0,mac=52:55:00:d1:55:01

#part1: directories $(BUILD_DIR)/producer.elf $(IMAGE_FILE_DATAPORT)

#part2: directories $(BUILD_DIR)/consumer.elf $(IMAGE_FILE_LOADER)


$(BUILD_DIR)/%.o: %.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/printf.o: include/printf.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/util.o: include/util.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@

$(BUILD_DIR)/periodic_dispatcher.elf: $(addprefix $(BUILD_DIR)/, $(PERIODIC_DISPATCHER_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(BUILD_DIR)/producer.elf: $(addprefix $(BUILD_DIR)/, $(PRODUCER_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(BUILD_DIR)/consumer.elf: $(addprefix $(BUILD_DIR)/, $(CONSUMER_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(BUILD_DIR)/end_of_schedule_ping.elf: $(addprefix $(BUILD_DIR)/, $(END_OF_SCHEDULE_PING_OBJS))
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(IMAGE_FILE_LOADER): $(addprefix $(BUILD_DIR)/, $(IMAGES)) dataport.system
	$(MICROKIT_TOOL) dataport.system --search-path $(BUILD_DIR) --board $(BOARD) --config $(MICROKIT_CONFIG) -o $(IMAGE_FILE_LOADER) -r $(REPORT_FILE)
