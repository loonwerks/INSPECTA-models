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


PRINTF_OBJS := printf.o util.o
P1_T1_MON_OBJS := $(PRINTF_OBJS) p1_t1_MON.o
P1_T1_OBJS := $(PRINTF_OBJS) p1_t1.o
P2_T2_MON_OBJS := $(PRINTF_OBJS) p2_t2_MON.o
P2_T2_OBJS := $(PRINTF_OBJS) p2_t2.o
P3_T3_MON_OBJS := $(PRINTF_OBJS) p3_t3_MON.o
P3_T3_OBJS := $(PRINTF_OBJS) p3_t3.o
PACER_OBJS := $(PRINTF_OBJS) pacer.o

SYSTEM_FILE := ${TOP}/microkit.system

IMAGES := p1_t1.elf p1_t1_MON.elf p2_t2.elf p2_t2_MON.elf p3_t3.elf p3_t3_MON.elf pacer.elf
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

# monitor
p1_t1_MON.o: ${TOP}/components/p1_t1/src/p1_t1_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p1_t1/include

# user code
p1_t1_user.o: ${TOP}/components/p1_t1/src/p1_t1_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p1_t1/include

p1_t1.o: ${TOP}/components/p1_t1/src/p1_t1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p1_t1/include

# monitor
p2_t2_MON.o: ${TOP}/components/p2_t2/src/p2_t2_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p2_t2/include

# user code
p2_t2_user.o: ${TOP}/components/p2_t2/src/p2_t2_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p2_t2/include

p2_t2.o: ${TOP}/components/p2_t2/src/p2_t2.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p2_t2/include

# monitor
p3_t3_MON.o: ${TOP}/components/p3_t3/src/p3_t3_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p3_t3/include

# user code
p3_t3_user.o: ${TOP}/components/p3_t3/src/p3_t3_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p3_t3/include

p3_t3.o: ${TOP}/components/p3_t3/src/p3_t3.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/p3_t3/include

pacer.o: ${TOP}/components/pacer/src/pacer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include

p1_t1_MON.elf: $(PRINTF_OBJS) p1_t1_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

p1_t1.elf: $(PRINTF_OBJS) p1_t1_user.o p1_t1.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

p2_t2_MON.elf: $(PRINTF_OBJS) p2_t2_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

p2_t2.elf: $(PRINTF_OBJS) p2_t2_user.o p2_t2.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

p3_t3_MON.elf: $(PRINTF_OBJS) p3_t3_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

p3_t3.elf: $(PRINTF_OBJS) p3_t3_user.o p3_t3.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

pacer.elf: $(PRINTF_OBJS) pacer.o
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
	rm -f p1_t1.o p1_t1_user.o p1_t1_MON.o p2_t2.o p2_t2_user.o p2_t2_MON.o p3_t3.o p3_t3_user.o p3_t3_MON.o pacer.o

clobber:: clean
	rm -f p1_t1.elf p1_t1_MON.elf p2_t2.elf p2_t2_MON.elf p3_t3.elf p3_t3_MON.elf pacer.elf ${IMAGE_FILE} ${REPORT_FILE}
