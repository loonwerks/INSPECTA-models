# This file will not be overwritten if codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

all:
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
		cargo build \
			--features sel4 \
			-Z build-std=core,alloc,compiler_builtins \
			-Z build-std-features=compiler-builtins-mem \
			--target aarch64-unknown-none \
			--release

verus:
	verus src/lib.rs

#verus-build:
#	 verus \
#	 	--crate-type=staticlib \
#		--multiple-errors 5 \
#		--compile \
#		src/lib.rs
	
test:
	cargo test

clean:
	cargo clean
