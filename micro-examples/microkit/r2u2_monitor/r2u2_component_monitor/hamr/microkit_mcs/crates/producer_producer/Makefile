# This file will not be overwritten if HAMR codegen is rerun

microkit_sdk_config_dir := $(MICROKIT_SDK)/board/$(MICROKIT_BOARD)/$(MICROKIT_CONFIG)

sel4_include_dirs := $(firstword $(wildcard $(microkit_sdk_config_dir)/include \
                                            $(microkit_sdk_config_dir)/debug/include))

# BEGIN MARKER R2U2 MAKE RULES
# END MARKER R2U2 MAKE RULES

R2U2_BUILD_DEPS = $(R2U2_SPEC_BIN)

# The toolchain is pinned to a stable release channel (see rust-toolchain.toml),
# which rejects the #![feature(..)] attributes the generated crates declare, so
# every cargo invocation -- building, verifying and testing alike -- needs this.
ENV_VARS = RUSTC_BOOTSTRAP=1

BUILD_ENV_VARS = $(ENV_VARS) \
                 SEL4_INCLUDE_DIRS=$(abspath $(sel4_include_dirs))

CARGO_FLAGS = -Z build-std=core,alloc,compiler_builtins \
              -Z build-std-features=compiler-builtins-mem \
              --target aarch64-unknown-none

# Extra arguments forwarded to Verus (placed after `--`), for every
# cargo-verus target below. These apply to dependency crates too -- notably
# vstd, which is verified from source on a clean build, so its proofs must
# discharge under these settings as well as our own.
#
# --rlimit 100
#   Verus' SMT resource limit for a function body check. Its default of 10
#   is not enough here. 100 is an order-of-magnitude bump that stays inside
#   the range Verus itself uses on its heavier projects.
#
# --smt-option smt.random_seed=7
#   7 is arbitrary. Z3's default seed is 0, and 0 is the one value at which
#   vstd's GhostSubseq::agree_map (resource/impls/seq.rs) fails its
#   postcondition; seeds 1, 2, 3, 7 and 42 all discharge it. The point is to
#   be off the default and identical on every machine, not this number.
SMT_OPTS ?= --rlimit 100 --smt-option smt.random_seed=7

all: build-verus-release

# NOTE: cargo-verus requires Verus-relevant cargo options (e.g. --features,
#       --release, --package, --manifest-path) to precede Verus-irrelevant
#       ones (e.g. --target, -Z ...), otherwise it errors out.  CARGO_FLAGS
#       holds the Verus-irrelevant options, so it must come last.

build-verus-release: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo-verus build --features sel4 --release $(CARGO_FLAGS) -- $(SMT_OPTS)

build-verus: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo-verus build --features sel4 $(CARGO_FLAGS) -- $(SMT_OPTS)

build-release: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS) --release

build: $(R2U2_BUILD_DEPS)
	$(BUILD_ENV_VARS) cargo build --features sel4 $(CARGO_FLAGS)

verus: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo-verus verify $(CARGO_FLAGS) -- $(SMT_OPTS)

verus-json: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo-verus verify $(CARGO_FLAGS) -- $(SMT_OPTS) --output-json --time > verus_results.json

# Test Example:
#   Run all unit tests
#   Usage: make test
#
#   Run only unit tests whose name contains 'proptest'
#   Usage: make test args=proptest

test-release: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo test $(args) --release

test: $(R2U2_BUILD_DEPS)
	$(ENV_VARS) cargo test $(args)

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
	$(ENV_VARS) CARGO_INCREMENTAL=0 RUSTFLAGS='-Cinstrument-coverage' LLVM_PROFILE_FILE='target/coverage/cargo-test-%p-%m.profraw' \
	cargo test $(args); \
	grcov . --binary-path ./target/debug/deps/ -s . -t html --branch --ignore-not-existing -o target/coverage/report; \
	if [ $$exists -eq 0 ]; then open target/coverage/report/index.html; fi

clean:
	cargo clean
	rm -f src/component/spec.bin