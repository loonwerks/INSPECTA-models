# This file will not be overwritten if codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

ENV_VARS = RUSTC_BOOTSTRAP=1

BUILD_ENV_VARS = $(ENV_VARS) \
                 SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs))

CARGO_FLAGS = -Z build-std=core,alloc,compiler_builtins \
              -Z build-std-features=compiler-builtins-mem \
              --target aarch64-unknown-none


# This target is based off what verus-analyzer outputs (below) when trying to verify component::thermostat_rt_mhs_mhs_app.
#
#[crates/flycheck/src/lib.rs:784:9] args = [
#    "verify",
#    "--message-format=json",
#    "--target",
#    "aarch64-unknown-none",
#    "--",
#    "-Z build-std=core,alloc,compiler_builtins",
#    "-Z build-std-features=compiler-builtins-mem",
#    "--verify-module",
#    "component::thermostat_rt_mhs_mhs_app",
#]
#
# It fails with the error
# error[E0463]: can't find crate for `core`
#  |
#  = note: the `aarch64-unknown-none` target may not be installed
#  = help: consider downloading the target with `rustup target add aarch64-unknown-none`
#  = help: consider building the standard library from source with `cargo build -Zbuild-std`
verus-analyzer:
	$(ENV_VARS) cargo-verus \
	verify \
	--target \
	aarch64-unknown-none \
	-- \
	-Z build-std=core,alloc,compiler_builtins \
	-Z build-std-features=compiler-builtins-mem \
	--verify-module \
	component::thermostat_rt_mhs_mhs_app


# Moving the -Z options so that they are passed to the 'verify' command rather than to verus
# results in 
#   error: could not find module component::thermostat_rt_mhs_mhs_app specified by --verify-module or --verify-only-module
#
# however, running 'make verus-analyzer2' first, and then running this target succeeds
verus-analyzer1:
	$(ENV_VARS) cargo-verus \
	verify \
	--target aarch64-unknown-none \
	-Z build-std=core,alloc,compiler_builtins \
	-Z build-std-features=compiler-builtins-mem \
	-- \
	--verify-module \
	component::thermostat_rt_mhs_mhs_app


# Removing the options passed to verus succeeds
verus-analyzer2:
	$(ENV_VARS) cargo-verus \
	verify \
	--target aarch64-unknown-none \
	-Z build-std=core,alloc,compiler_builtins \
	-Z build-std-features=compiler-builtins-mem


all: build-verus-release

build-verus-release:
	$(BUILD_ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS) --release

build-verus:
	$(BUILD_ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS)

build-release:
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS) --release

build:
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS)

verus:
	$(ENV_VARS) cargo-verus verify $(CARGO_FLAGS)

verus-json:
	$(ENV_VARS) cargo-verus verify $(CARGO_FLAGS) -- --output-json --time > verus_results.json

# Test Example:
#   Run all unit tests
#   Usage: make test
#
#   Run only unit tests whose name contains 'proptest'
#   Usage: make test args=proptest

test-release:
	cargo test $(args) --release

test:
	cargo test $(args)

# Coverage Example:
#   Generate a test coverage report combining the results of all unit tests
#   Usage: make coverage
#
#   Generate a test coverage report for unit tests whose name contains 'proptest'
#   Usage: make coverage args=proptest

coverage:
	cargo install grcov
	@exists=0; if [ -f target/coverage/report/index.html ]; then exists=1; fi; \
	rm -rf target/coverage; \
	CARGO_INCREMENTAL=0 RUSTFLAGS='-Cinstrument-coverage' LLVM_PROFILE_FILE='target/coverage/cargo-test-%p-%m.profraw' \
	cargo test $(args); \
	grcov . --binary-path ./target/debug/deps/ -s . -t html --branch --ignore-not-existing -o target/coverage/report; \
	if [ $$exists -eq 0 ]; then open target/coverage/report/index.html; fi

clean:
	cargo clean
