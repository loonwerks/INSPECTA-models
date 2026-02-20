# This file will not be overwritten if codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

all:
	RUSTC_BOOTSTRAP=1 \
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
		cargo verus build \
			--features sel4 \
			-Z build-std=core,alloc,compiler_builtins \
			-Z build-std-features=compiler-builtins-mem \
			--target aarch64-unknown-none \
			--release

verus:
	RUSTC_BOOTSTRAP=1 \
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
	cargo verus verify \
		-Z build-std=core,alloc,compiler_builtins \
		-Z build-std-features=compiler-builtins-mem \
		--target aarch64-unknown-none \
		--release

test:
	cargo test

clean:
	cargo clean
