# Do not edit this file as it will be overwritten if codegen is rerun

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

CFLAGS := -mcpu=$(CPU) \
	-mstrict-align \
	-ffreestanding \
	-nostdlib \
	-g3 \
	-O3 \
	-Wall -Wno-unused-function -Werror -Wno-unused-command-line-argument \
	-I$(MICROKIT_BOARD_DIR)/include \
	-target $(TARGET)

LDFLAGS := -L$(MICROKIT_BOARD_DIR)/lib
LIBS := --start-group -lmicrokit -Tmicrokit.ld --end-group

SYSTEM_FILE := $(TOP_DIR)/microkit.system

IMAGES := producer_p_p1_producer.elf producer_p_p1_producer_MON.elf producer_p_p2_producer.elf producer_p_p2_producer_MON.elf consumer_p_p_consumer.elf consumer_p_p_consumer_MON.elf consumer_p_s_consumer.elf consumer_p_s_consumer_MON.elf pacer.elf
IMAGE_FILE = loader.img
REPORT_FILE = report.txt

UTIL_OBJS = printf.o util.o

TYPES_DIR = $(TOP_DIR)/types
TYPE_OBJS := $(TOP_DIR)/build/sb_queue_event_data_2_prod_2_cons_struct_i_1.o $(TOP_DIR)/build/sb_queue_event_data_2_prod_2_cons_struct_i_1.o

# exporting TOP_TYPES_INCLUDE in case other makefiles need it
export TOP_TYPES_INCLUDE = -I$(TYPES_DIR)/include

TOP_INCLUDE = $(TOP_TYPES_INCLUDE) -I$(TOP_DIR)/util/include

all: $(IMAGE_FILE)
	CHECK_FLAGS_BOARD_MD5:=.board_cflags-$(shell echo -- ${CFLAGS} ${MICROKIT_BOARD} ${MICROKIT_CONFIG}| shasum | sed 's/ *-//')

${CHECK_FLAGS_BOARD_MD5}:
	-rm -f .board_cflags-*
	touch $@

%.o: ${TOP_DIR}/util/src/%.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I$(TOP_DIR)/util/include

$(TOP_DIR)/build/sb_queue_event_data_2_prod_2_cons_struct_i_1.o: $(TOP_DIR)/types/src/sb_queue_event_data_2_prod_2_cons_struct_i_1.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE)


# monitor
producer_p_p1_producer_MON.o: $(TOP_DIR)/components/producer_p_p1_producer/src/producer_p_p1_producer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/producer_p_p1_producer/include

# user code
producer_p_p1_producer_user.o: $(TOP_DIR)/components/producer_p_p1_producer/src/producer_p_p1_producer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE)/ -I$(TOP_DIR)/components/producer_p_p1_producer/include

producer_p_p1_producer.o: $(TOP_DIR)/components/producer_p_p1_producer/src/producer_p_p1_producer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/producer_p_p1_producer/include

# monitor
producer_p_p2_producer_MON.o: $(TOP_DIR)/components/producer_p_p2_producer/src/producer_p_p2_producer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/producer_p_p2_producer/include

# user code
producer_p_p2_producer_user.o: $(TOP_DIR)/components/producer_p_p2_producer/src/producer_p_p2_producer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE)/ -I$(TOP_DIR)/components/producer_p_p2_producer/include

producer_p_p2_producer.o: $(TOP_DIR)/components/producer_p_p2_producer/src/producer_p_p2_producer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/producer_p_p2_producer/include

# monitor
consumer_p_p_consumer_MON.o: $(TOP_DIR)/components/consumer_p_p_consumer/src/consumer_p_p_consumer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/consumer_p_p_consumer/include

# user code
consumer_p_p_consumer_user.o: $(TOP_DIR)/components/consumer_p_p_consumer/src/consumer_p_p_consumer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE)/ -I$(TOP_DIR)/components/consumer_p_p_consumer/include

consumer_p_p_consumer.o: $(TOP_DIR)/components/consumer_p_p_consumer/src/consumer_p_p_consumer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/consumer_p_p_consumer/include

# monitor
consumer_p_s_consumer_MON.o: $(TOP_DIR)/components/consumer_p_s_consumer/src/consumer_p_s_consumer_MON.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/consumer_p_s_consumer/include

# user code
consumer_p_s_consumer_user.o: $(TOP_DIR)/components/consumer_p_s_consumer/src/consumer_p_s_consumer_user.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE)/ -I$(TOP_DIR)/components/consumer_p_s_consumer/include

consumer_p_s_consumer.o: $(TOP_DIR)/components/consumer_p_s_consumer/src/consumer_p_s_consumer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ $(TOP_INCLUDE) -I$(TOP_DIR)/components/consumer_p_s_consumer/include

pacer.o: $(TOP_DIR)/components/pacer/src/pacer.c Makefile
	$(CC) -c $(CFLAGS) $< -o $@ -I$(TOP_INCLUDE)

producer_p_p1_producer_MON.elf: producer_p_p1_producer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

producer_p_p1_producer.elf: $(UTIL_OBJS) $(TYPE_OBJS) producer_p_p1_producer_user.o producer_p_p1_producer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

producer_p_p2_producer_MON.elf: producer_p_p2_producer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

producer_p_p2_producer.elf: $(UTIL_OBJS) $(TYPE_OBJS) producer_p_p2_producer_user.o producer_p_p2_producer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_p_consumer_MON.elf: consumer_p_p_consumer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_p_consumer.elf: $(UTIL_OBJS) $(TYPE_OBJS) consumer_p_p_consumer_user.o consumer_p_p_consumer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_s_consumer_MON.elf: consumer_p_s_consumer_MON.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

consumer_p_s_consumer.elf: $(UTIL_OBJS) $(TYPE_OBJS) consumer_p_s_consumer_user.o consumer_p_s_consumer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

pacer.elf: $(UTIL_OBJS) $(TYPE_OBJS) pacer.o
	$(LD) $(LDFLAGS) $^ $(LIBS) -o $@

$(IMAGE_FILE): $(IMAGES) $(SYSTEM_FILE)
	$(MICROKIT_TOOL) $(SYSTEM_FILE) --search-path $(TOP_BUILD_DIR) --board $(MICROKIT_BOARD) --config $(MICROKIT_CONFIG) -o $(IMAGE_FILE) -r $(REPORT_FILE)


qemu: $(IMAGE_FILE)
	$(QEMU) -machine virt,virtualization=on \
			-cpu cortex-a53 \
			-serial mon:stdio \
			-device loader,file=$(IMAGE_FILE),addr=0x70000000,cpu-num=0 \
			-m size=2G \
			-nographic

clean::
	rm -f ${(oFiles, " ")}

clobber:: clean
	rm -f producer_p_p1_producer.elf producer_p_p1_producer_MON.elf producer_p_p2_producer.elf producer_p_p2_producer_MON.elf consumer_p_p_consumer.elf consumer_p_p_consumer_MON.elf consumer_p_s_consumer.elf consumer_p_s_consumer_MON.elf pacer.elf ${IMAGE_FILE} ${REPORT_FILE}
