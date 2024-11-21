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


TYPE_OBJS := printf.o util.o sb_queue_int8_t_1.o sb_queue_int8_t_1.o

SYSTEM_FILE := ${TOP}/microkit.system

IMAGES := producer_p_p1_producer.elf producer_p_p1_producer_MON.elf producer_p_p2_producer.elf producer_p_p2_producer_MON.elf consumer_p_p_consumer.elf consumer_p_p_consumer_MON.elf consumer_p_s_consumer.elf consumer_p_s_consumer_MON.elf pacer.elf
IMAGE_FILE = loader.img
REPORT_FILE = report.txt

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

sb_queue_int8_t_1.o: ${TOP}/src/sb_queue_int8_t_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include


sb_queue_int8_t_1.o: ${TOP}/src/sb_queue_int8_t_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include


# monitor
producer_p_p1_producer_MON.o: ${TOP}/components/producer_p_p1_producer/src/producer_p_p1_producer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/producer_p_p1_producer/include

# user code
producer_p_p1_producer_user.o: ${TOP}/components/producer_p_p1_producer/src/producer_p_p1_producer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/producer_p_p1_producer/include

producer_p_p1_producer.o: ${TOP}/components/producer_p_p1_producer/src/producer_p_p1_producer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/producer_p_p1_producer/include

# monitor
producer_p_p2_producer_MON.o: ${TOP}/components/producer_p_p2_producer/src/producer_p_p2_producer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/producer_p_p2_producer/include

# user code
producer_p_p2_producer_user.o: ${TOP}/components/producer_p_p2_producer/src/producer_p_p2_producer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/producer_p_p2_producer/include

producer_p_p2_producer.o: ${TOP}/components/producer_p_p2_producer/src/producer_p_p2_producer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/producer_p_p2_producer/include

# monitor
consumer_p_p_consumer_MON.o: ${TOP}/components/consumer_p_p_consumer/src/consumer_p_p_consumer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/consumer_p_p_consumer/include

# user code
consumer_p_p_consumer_user.o: ${TOP}/components/consumer_p_p_consumer/src/consumer_p_p_consumer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/consumer_p_p_consumer/include

consumer_p_p_consumer.o: ${TOP}/components/consumer_p_p_consumer/src/consumer_p_p_consumer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/consumer_p_p_consumer/include

# monitor
consumer_p_s_consumer_MON.o: ${TOP}/components/consumer_p_s_consumer/src/consumer_p_s_consumer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/consumer_p_s_consumer/include

# user code
consumer_p_s_consumer_user.o: ${TOP}/components/consumer_p_s_consumer/src/consumer_p_s_consumer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/consumer_p_s_consumer/include

consumer_p_s_consumer.o: ${TOP}/components/consumer_p_s_consumer/src/consumer_p_s_consumer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include -I${TOP}/components/consumer_p_s_consumer/include

pacer.o: ${TOP}/components/pacer/src/pacer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I${TOP}/include

producer_p_p1_producer_MON.elf: $(TYPE_OBJS) producer_p_p1_producer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

producer_p_p1_producer.elf: $(TYPE_OBJS) producer_p_p1_producer_user.o producer_p_p1_producer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

producer_p_p2_producer_MON.elf: $(TYPE_OBJS) producer_p_p2_producer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

producer_p_p2_producer.elf: $(TYPE_OBJS) producer_p_p2_producer_user.o producer_p_p2_producer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_p_consumer_MON.elf: $(TYPE_OBJS) consumer_p_p_consumer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_p_consumer.elf: $(TYPE_OBJS) consumer_p_p_consumer_user.o consumer_p_p_consumer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_s_consumer_MON.elf: $(TYPE_OBJS) consumer_p_s_consumer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_s_consumer.elf: $(TYPE_OBJS) consumer_p_s_consumer_user.o consumer_p_s_consumer.o
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
	rm -f producer_p_p1_producer.o producer_p_p1_producer_user.o producer_p_p1_producer_MON.o producer_p_p2_producer.o producer_p_p2_producer_user.o producer_p_p2_producer_MON.o consumer_p_p_consumer.o consumer_p_p_consumer_user.o consumer_p_p_consumer_MON.o consumer_p_s_consumer.o consumer_p_s_consumer_user.o consumer_p_s_consumer_MON.o pacer.o

clobber:: clean
	rm -f producer_p_p1_producer.elf producer_p_p1_producer_MON.elf producer_p_p2_producer.elf producer_p_p2_producer_MON.elf consumer_p_p_consumer.elf consumer_p_p_consumer_MON.elf consumer_p_s_consumer.elf consumer_p_s_consumer_MON.elf pacer.elf ${IMAGE_FILE} ${REPORT_FILE}
