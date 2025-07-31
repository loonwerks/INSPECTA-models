# This file will not be overwritten if codegen is rerun

SHELL := /bin/bash

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

all:
	RUSTC_BOOTSTRAP=1 \
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
		cargo-verus build \
			--features sel4 \
			-Z build-std=core,alloc,compiler_builtins \
			-Z build-std-features=compiler-builtins-mem \
			--target aarch64-unknown-none \
			--release \
			-- --output-json --time > verus_results.json

build:
	RUSTC_BOOTSTRAP=1 \
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
		cargo build \
			--features sel4 \
			-Z build-std=core,alloc,compiler_builtins \
			-Z build-std-features=compiler-builtins-mem \
			--target aarch64-unknown-none \
			--release

verus:
	RUSTC_BOOTSTRAP=1 \
	SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs)) \
	cargo-verus verify \
		-Z build-std=core,alloc,compiler_builtins \
		-Z build-std-features=compiler-builtins-mem \
		--target aarch64-unknown-none \
		--release \
		-- --output-json --time > verus_results.json

test:
	cargo test

coverage:
	rm -rf target/coverage
	CARGO_INCREMENTAL=0 RUSTFLAGS='-Cinstrument-coverage' LLVM_PROFILE_FILE='target/coverage/cargo-test-%p-%m.profraw' cargo test
	grcov . --binary-path ./target/debug/deps/ -s . -t html --branch --ignore-not-existing -o target/coverage/rx_firewall
	grcov . --binary-path ./target/debug/deps/ -s ../firewall_core -t html --branch --ignore-not-existing -o target/coverage/firewall_core

clean:
	cargo clean
