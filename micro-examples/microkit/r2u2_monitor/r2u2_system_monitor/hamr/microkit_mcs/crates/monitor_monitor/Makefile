# This file will not be overwritten if HAMR codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

# BEGIN MARKER R2U2 MAKE RULES
.DEFAULT_GOAL := all
R2U2_SPEC_BIN := src/component/spec.bin

r2u2_cli:
	@echo "Checking/Updating r2u2_cli from crates.io..."
	cargo +stable install r2u2_cli --version 4.2.3

$(R2U2_SPEC_BIN): r2u2_cli
	mkdir -p .cargo && \
	cd src/component && \
	sed '/^--/d' spec.map > temp.map && \
	r2u2_cli compile -o . -b ../../.cargo/config.toml spec.c2po temp.map && \
	rm temp.map
# END MARKER R2U2 MAKE RULES

R2U2_BUILD_DEPS = $(R2U2_SPEC_BIN)
ENV_VARS = RUSTC_BOOTSTRAP=1

BUILD_ENV_VARS = $(ENV_VARS) \
                 SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs))

CARGO_FLAGS = -Z build-std=core,alloc,compiler_builtins \
              -Z build-std-features=compiler-builtins-mem \
              --target aarch64-unknown-none

all: build-verus-release

build-verus-release: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS) --release

build-verus: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS)

build-release: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS) --release

build: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS)

verus: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo-verus verify $(CARGO_FLAGS)

verus-json: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo-verus verify $(CARGO_FLAGS) -- --output-json --time > verus_results.json

# Test Example:
#   Run all unit tests
#   Usage: make test
#
#   Run only unit tests whose name contains 'proptest'
#   Usage: make test args=proptest

test-release: $(R2U2_BUILD_DEPS)
	cargo test $(args) --release

test: $(R2U2_BUILD_DEPS)
	cargo test $(args)

# Coverage Example:
#   Generate a test coverage report combining the results of all unit tests
#   Usage: make coverage
#
#   Generate a test coverage report for unit tests whose name contains 'proptest'
#   Usage: make coverage args=proptest

coverage: $(R2U2_BUILD_DEPS)
	cargo install grcov
	@exists=0; if [ -f target/coverage/report/index.html ]; then exists=1; fi; \
	rm -rf target/coverage; \
	CARGO_INCREMENTAL=0 RUSTFLAGS='-Cinstrument-coverage' LLVM_PROFILE_FILE='target/coverage/cargo-test-%p-%m.profraw' \
	cargo test $(args); \
	grcov . --binary-path ./target/debug/deps/ -s . -t html --branch --ignore-not-existing -o target/coverage/report; \
	if [ $$exists -eq 0 ]; then open target/coverage/report/index.html; fi

clean:
	cargo clean
	rm -f src/component/spec.bin