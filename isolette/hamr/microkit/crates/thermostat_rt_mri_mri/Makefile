# This file will not be overwritten if codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

ENV_VARS = RUSTC_BOOTSTRAP=1 \
           SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs))

CARGO_FLAGS = -Z build-std=core,alloc,compiler_builtins \
              -Z build-std-features=compiler-builtins-mem \
              --target aarch64-unknown-none

all: build-verus-release

build-verus-release:
	$(ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS) --release

build-verus:
	$(ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS)

build-release:
	$(ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS) --release

build:
	$(ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS)

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
