microkit_config := debug
microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(microkit_config)

sel4_include_dirs := $(microkit_sdk_config_dir)/include

all:
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
		cargo build -Z build-std=core,alloc,compiler_builtins \
		-Z build-std-features=compiler-builtins-mem \
		--target aarch64-unknown-none --release

clean:
	cargo clean
