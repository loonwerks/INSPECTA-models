ifeq ($(strip $(MICROKIT_SDK)),)
$(error MICROKIT_SDK must be specified)
endif

MICROKIT_TOOL ?= $(MICROKIT_SDK)/bin/microkit

ifeq ("$(wildcard $(MICROKIT_TOOL))","")
$(error Microkit tool not found at ${MICROKIT_TOOL})
endif

ifeq ($(strip $(MICROKIT_BOARD)),)
$(error MICROKIT_BOARD must be specified)
endif

BUILD_DIR ?= build
# By default we make a debug build so that the client debug prints can be seen.
MICROKIT_CONFIG ?= debug

QEMU := qemu-system-aarch64

CC := clang
LD := ld.lld
AR := llvm-ar
RANLIB := llvm-ranlib

CFLAGS := -mcpu=$(CPU) \
	-mstrict-align \
	-nostdlib \
	-ffreestanding \
	-g3 \
	-O3 \
	-Wall -Wno-unused-function -Werror -Wno-unused-command-line-argument \
	-target aarch64-none-elf \
	-I$(BOARD_DIR)/include
LDFLAGS := -L$(BOARD_DIR)/lib
LIBS := --start-group -lmicrokit -Tmicrokit.ld --end-group


TYPE_OBJS := printf.o util.o sb_queue_base_SW_RawEthernetMessage_Impl_1.o sb_queue_base_SW_RawEthernetMessage_Impl_1.o sb_queue_base_SW_SizedEthernetMessage_Impl_1.o sb_queue_base_SW_RawEthernetMessage_Impl_1.o

SYSTEM_FILE := ${TOP}/microkit.system

IMAGES := seL4_ArduPilot_ArduPilot.elf seL4_ArduPilot_ArduPilot_MON.elf seL4_Firewall_Firewall.elf seL4_Firewall_Firewall_MON.elf seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.elf seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.elf pacer.elf
IMAGE_FILE_DATAPORT = microkit.img
IMAGE_FILE = loader.img
REPORT_FILE = /report.txt

all: $(IMAGE_FILE)
	CHECK_FLAGS_BOARD_MD5:=.board_cflags-$(shell echo -- ${CFLAGS} ${BOARD} ${MICROKIT_CONFIG}| shasum | sed 's/ *-//')

${CHECK_FLAGS_BOARD_MD5}:
	-rm -f .board_cflags-*
	touch $@

%.o: ${TOP}/%.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include

printf.o: ${TOP}/src/printf.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include

util.o: ${TOP}/src/util.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include

sb_queue_base_SW_RawEthernetMessage_Impl_1.o: ${TOP}/src/sb_queue_base_SW_RawEthernetMessage_Impl_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include


sb_queue_base_SW_RawEthernetMessage_Impl_1.o: ${TOP}/src/sb_queue_base_SW_RawEthernetMessage_Impl_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include


sb_queue_base_SW_SizedEthernetMessage_Impl_1.o: ${TOP}/src/sb_queue_base_SW_SizedEthernetMessage_Impl_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include


sb_queue_base_SW_RawEthernetMessage_Impl_1.o: ${TOP}/src/sb_queue_base_SW_RawEthernetMessage_Impl_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include


# monitor
seL4_ArduPilot_ArduPilot_MON.o: ${TOP}/components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_ArduPilot_ArduPilot/include

# user code
seL4_ArduPilot_ArduPilot_user.o: ${TOP}/components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_ArduPilot_ArduPilot/include

seL4_ArduPilot_ArduPilot.o: ${TOP}/components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_ArduPilot_ArduPilot/include

# monitor
seL4_Firewall_Firewall_MON.o: ${TOP}/components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_Firewall_Firewall/include

# user code
seL4_Firewall_Firewall_user.o: ${TOP}/components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_Firewall_Firewall/include

seL4_Firewall_Firewall.o: ${TOP}/components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_Firewall_Firewall/include

# monitor
seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.o: ${TOP}/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/include

# user code
seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_user.o: ${TOP}/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/include

seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.o: ${TOP}/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/include

pacer.o: ${TOP}/components/pacer/src/pacer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include

seL4_ArduPilot_ArduPilot_MON.elf: $(TYPE_OBJS) seL4_ArduPilot_ArduPilot_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

seL4_ArduPilot_ArduPilot.elf: $(TYPE_OBJS) seL4_ArduPilot_ArduPilot_user.o seL4_ArduPilot_ArduPilot.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

seL4_Firewall_Firewall_MON.elf: $(TYPE_OBJS) seL4_Firewall_Firewall_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

seL4_Firewall_Firewall.elf: $(TYPE_OBJS) seL4_Firewall_Firewall_user.o seL4_Firewall_Firewall.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.elf: $(TYPE_OBJS) seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.elf: $(TYPE_OBJS) seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_user.o seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

pacer.elf: $(TYPE_OBJS) pacer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(IMAGE_FILE): $(IMAGES) $(SYSTEM_FILE)
	$(MICROKIT_TOOL) $(SYSTEM_FILE) --search-path $(BUILD_DIR) --board $(MICROKIT_BOARD) --config $(MICROKIT_CONFIG) -o $(IMAGE_FILE) -r $(REPORT_FILE)


qemu: $(IMAGE_FILE)
	$(QEMU) -machine virt,virtualization=on \
			-cpu cortex-a53 \
			-serial mon:stdio \
			-device loader,file=$(IMAGE_FILE),addr=0x70000000,cpu-num=0 \
			-m size=2G \
			-nographic

clean::
	rm -f seL4_ArduPilot_ArduPilot.o seL4_ArduPilot_ArduPilot_user.o seL4_ArduPilot_ArduPilot_MON.o seL4_Firewall_Firewall.o seL4_Firewall_Firewall_user.o seL4_Firewall_Firewall_MON.o seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.o seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_user.o seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.o pacer.o

clobber:: clean
	rm -f seL4_ArduPilot_ArduPilot.elf seL4_ArduPilot_ArduPilot_MON.elf seL4_Firewall_Firewall.elf seL4_Firewall_Firewall_MON.elf seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.elf seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_MON.elf pacer.elf ${IMAGE_FILE} ${REPORT_FILE}
