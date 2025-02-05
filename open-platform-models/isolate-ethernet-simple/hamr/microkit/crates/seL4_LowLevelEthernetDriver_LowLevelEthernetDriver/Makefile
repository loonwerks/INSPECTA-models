# This file will not be overwritten if codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(microkit_sdk_config_dir)/include

all:
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
		cargo build \
			-Z build-std=core,alloc,compiler_builtins \
			-Z build-std-features=compiler-builtins-mem \
			--target aarch64-unknown-none \
			--release

test:
	cargo test

clean:
	cargo clean
